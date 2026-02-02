package io.ten1010.common.jsonpatch;

import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPatchOperationBuilderTests {

	@Test
	void given_add_operation() {
		JsonPatchOperation expected = new JsonPatchOperation();
		expected.setOp("add");
		expected.setPath("/foo");
		expected.setValue(null);

		JsonPatchOperationBuilder builder = new JsonPatchOperationBuilder();
		JsonPatchOperation operation = builder.add().setPath("/foo").setValue(null).build();

		assertThat(operation).isEqualTo(expected);
	}

	@Test
	void given_copy_operation() {
		JsonPatchOperation expected = new JsonPatchOperation();
		expected.setOp("copy");
		expected.setPath("/foo");
		expected.setFrom("/bar");

		JsonPatchOperationBuilder builder = new JsonPatchOperationBuilder();
		JsonPatchOperation operation = builder.copy().setPath("/foo").setFrom("/bar").build();

		assertThat(operation).isEqualTo(expected);
	}

	@Test
	void given_move_operation() {
		JsonPatchOperation expected = new JsonPatchOperation();
		expected.setOp("move");
		expected.setPath("/foo");
		expected.setFrom("/bar");

		JsonPatchOperationBuilder builder = new JsonPatchOperationBuilder();
		JsonPatchOperation operation = builder.move().setPath("/foo").setFrom("/bar").build();

		assertThat(operation).isEqualTo(expected);
	}

	@Test
	void given_remove_operation() {
		JsonPatchOperation expected = new JsonPatchOperation();
		expected.setOp("remove");
		expected.setPath("/foo");

		JsonPatchOperationBuilder builder = new JsonPatchOperationBuilder();
		JsonPatchOperation operation = builder.remove().setPath("/foo").build();

		assertThat(operation).isEqualTo(expected);
	}

	@Test
	void given_replace_operation() {
		JsonPatchOperation expected = new JsonPatchOperation();
		expected.setOp("replace");
		expected.setPath("/foo");
		expected.setValue(null);

		JsonPatchOperationBuilder builder = new JsonPatchOperationBuilder();
		JsonPatchOperation operation = builder.replace().setPath("/foo").setValue(null).build();

		assertThat(operation).isEqualTo(expected);
	}

	@Test
	void given_test_operation() {
		JsonPatchOperation expected = new JsonPatchOperation();
		expected.setOp("test");
		expected.setPath("/foo");
		expected.setValue(null);

		JsonPatchOperationBuilder builder = new JsonPatchOperationBuilder();
		JsonPatchOperation operation = builder.test().setPath("/foo").setValue(null).build();

		assertThat(operation).isEqualTo(expected);
	}

}
