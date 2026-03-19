package io.ten1010.common.jsonpatch;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.NullNode;
import tools.jackson.databind.node.StringNode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonPatchDeserializationTests {

	ObjectMapper mapper = new ObjectMapper();

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
		String json = "[{\"from\":null,\"op\":\"remove\",\"path\":\"/foo\",\"value\":null},{\"from\":null,\"op\":\"add\",\"path\":\"/bar\",\"value\":\"dummy\"},{\"from\":null,\"op\":\"add\",\"path\":\"/bar\",\"value\":null}]";

		JsonPatchOperation op1 = new JsonPatchOperation();
		op1.setOp("remove");
		op1.setPath("/foo");
		JsonPatchOperation op2 = new JsonPatchOperation();
		op2.setOp("add");
		op2.setPath("/bar");
		op2.setValue(StringNode.valueOf("dummy"));
		JsonPatchOperation op3 = new JsonPatchOperation();
		op3.setOp("add");
		op3.setPath("/bar");
		op3.setValue(NullNode.getInstance());
		JsonPatch expected = new JsonPatch();
		expected.setOperations(List.of(op1, op2, op3));
		JsonPatch deserialized = this.mapper.readValue(json, JsonPatch.class);

		assertThat(deserialized).isEqualTo(expected);
	}

}
