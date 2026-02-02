package io.ten1010.common.jsonpatch.build;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.spring.javaformat.gradle.SpringJavaFormatPlugin;
import io.spring.javaformat.gradle.tasks.CheckFormat;
import io.spring.javaformat.gradle.tasks.Format;
import org.gradle.api.GradleException;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.quality.Checkstyle;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.plugins.quality.CheckstylePlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.tasks.Jar;
import org.gradle.jvm.toolchain.JavaCompiler;
import org.gradle.jvm.toolchain.JavaInstallationMetadata;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

public class ConventionsPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getPlugins().withType(JavaBasePlugin.class, (plugin) -> {
			configureJavaCompile(project);
			configureSpringJavaFormat(project);
			configureJavaTest(project);
			configureJspecify(project);
			configureMavenPublish(project);
		});
		project.getTasks().withType(Jar.class, (jar) -> jar.dependsOn("check"));
	}

	private void configureJavaCompile(Project project) {
		String javaVersion = (String) project.findProperty("javaVersion");

		project.getTasks().withType(JavaCompile.class, (compile) -> {
			compile.doFirst((task) -> assertCompatible(compile, javaVersion));
			compile.getOptions().setEncoding("UTF-8");
			compile.getOptions().getRelease().set(Integer.valueOf(javaVersion));
			Set<String> args = new LinkedHashSet<>(compile.getOptions().getCompilerArgs());
			args.addAll(List.of("-parameters", "-Werror", "-Xlint:unchecked", "-Xlint:deprecation", "-Xlint:rawtypes",
					"-Xlint:varargs"));
			compile.getOptions().setCompilerArgs(new ArrayList<>(args));
		});
	}

	private void configureSpringJavaFormat(Project project) {
		String springJavaFormatVersion = (String) project.findProperty("springJavaFormatVersion");
		String checkstyleToolVersion = (String) project.findProperty("checkstyleToolVersion");

		project.getPlugins().apply(SpringJavaFormatPlugin.class);
		project.getPlugins().apply(CheckstylePlugin.class);

		CheckstyleExtension checkstyle = project.getExtensions().getByType(CheckstyleExtension.class);
		checkstyle.setToolVersion(checkstyleToolVersion);
		checkstyle.getConfigDirectory().set(project.getRootProject().file("checkstyle"));

		DependencySet checkstyleDependencies = project.getConfigurations().getByName("checkstyle").getDependencies();
		checkstyleDependencies.add(project.getDependencies()
			.create("io.spring.javaformat:spring-javaformat-checkstyle:" + springJavaFormatVersion));

		project.getTasks().withType(Format.class, (format) -> format.setEncoding("UTF-8"));
	}

	private void configureJavaTest(Project project) {
		project.getConfigurations().create("mockitoAgent");
		Provider<List<String>> agentArgProvider = project.getConfigurations()
			.named("mockitoAgent")
			.map(FileCollection::getAsPath)
			.map((path) -> "-javaagent:" + path)
			.map(List::of);
		project.getPlugins().withType(JavaPlugin.class, (javaPlugin) -> addTestDependencies(project));
		project.getTasks().withType(Test.class, (test) -> {
			test.useJUnitPlatform();
			test.getJvmArgumentProviders().add(agentArgProvider::get);
			test.setMaxHeapSize("1536M");
			project.getTasks().withType(Checkstyle.class, test::mustRunAfter);
			project.getTasks().withType(CheckFormat.class, test::mustRunAfter);
		});
	}

	private void configureJspecify(Project project) {
		String jspecifyVersion = (String) project.findProperty("jspecifyVersion");

		project.getPlugins()
			.withType(JavaPlugin.class, (plugin) -> project.getDependencies()
				.add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, "org.jspecify:jspecify:" + jspecifyVersion));
		project.getPlugins()
			.withType(JavaLibraryPlugin.class, (plugin) -> project.getDependencies()
				.add(JavaPlugin.API_CONFIGURATION_NAME, "org.jspecify:jspecify:" + jspecifyVersion));
	}

	private void configureMavenPublish(Project project) {
		project.getPlugins().withType(JavaLibraryPlugin.class).all((plugin) -> {
			JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
			java.withSourcesJar();
			project.getPlugins().apply(MavenPublishPlugin.class);
			SoftwareComponent javaComponent = project.getComponents().getByName("java");
			PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);
			publishingExtension.getPublications()
				.create("mavenJava", MavenPublication.class, (publication) -> publication.from(javaComponent));
			publishingExtension.getRepositories().mavenLocal();
		});
	}

	private void addTestDependencies(Project project) {
		String jupiterVersion = (String) project.findProperty("junitJupiterVersion");
		String assertjVersion = (String) project.findProperty("assertjVersion");
		String mockitoVersion = (String) project.findProperty("mockitoVersion");

		DependencyHandler handler = project.getDependencies();
		handler.add(JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME, "org.junit.platform:junit-platform-launcher");
		ExternalModuleDependency mockitoDependency = (ExternalModuleDependency) project.getDependencies()
			.create("org.mockito:mockito-core:" + mockitoVersion);
		mockitoDependency.setTransitive(false);
		handler.add("mockitoAgent", mockitoDependency);
		handler.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME,
				handler.platform("org.junit:junit-bom:" + jupiterVersion));
		handler.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.junit.jupiter:junit-jupiter");
		handler.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.assertj:assertj-core:" + assertjVersion);
		handler.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.mockito:mockito-core:" + mockitoVersion);
	}

	private void assertCompatible(JavaCompile compile, String javaVersion) {
		JavaVersion requiredVersion = JavaVersion.toVersion(javaVersion);
		JavaVersion actualVersion = compile.getJavaCompiler()
			.map(JavaCompiler::getMetadata)
			.map(JavaInstallationMetadata::getLanguageVersion)
			.map(JavaLanguageVersion::asInt)
			.map(JavaVersion::toVersion)
			.orElse(JavaVersion.current())
			.get();
		if (!actualVersion.isCompatibleWith(requiredVersion)) {
			throw new GradleException("This project should be built with Java %s or above".formatted(requiredVersion));
		}
	}

}
