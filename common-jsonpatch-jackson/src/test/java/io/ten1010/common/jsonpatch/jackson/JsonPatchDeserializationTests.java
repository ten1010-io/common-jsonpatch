package io.ten1010.common.jsonpatch.jackson;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonPatchDeserializationTests {

	JsonMapper mapper;

	JsonPatchDeserializationTests() {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(JsonPatch.class, new JsonPatchDeserializer(JsonPatch.class));
		this.mapper = JsonMapper.builder().addModule(module).build();
	}

	@Test
	void given_empty_json() {
		String json = "";

		assertThatThrownBy(() -> this.mapper.readValue(json, JsonPatch.class)).isInstanceOf(Exception.class);
	}

	@Test
	void given_object_json() {
		String json = "{}";

		assertThatThrownBy(() -> this.mapper.readValue(json, JsonPatch.class)).isInstanceOf(Exception.class);
	}

	@Test
	void given_empty_array_json() {
		String json = "[]";
		JsonPatch expected = new JsonPatch();
		expected.setOperations(List.of());

		JsonPatch jsonPatch = this.mapper.readValue(json, JsonPatch.class);

		assertThat(jsonPatch).isEqualTo(expected);
	}

	@Test
	void given_operation_array_json() {
		String json = "[" + "{\"op\": \"remove\", \"path\": \"/foo\"}" + ", "
				+ "{\"op\": \"remove\", \"path\": \"/bar\"}" + "]";

		JsonPatchOperation op1 = new JsonPatchOperation();
		op1.setOp("remove");
		op1.setPath("/foo");
		JsonPatchOperation op2 = new JsonPatchOperation();
		op2.setOp("remove");
		op2.setPath("/bar");

		JsonPatch expected = new JsonPatch();
		expected.setOperations(List.of(op1, op2));

		JsonPatch jsonPatch = this.mapper.readValue(json, JsonPatch.class);

		assertThat(jsonPatch).isEqualTo(expected);
	}

}
