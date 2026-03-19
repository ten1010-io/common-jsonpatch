package io.ten1010.common.jsonpatch;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.NullNode;
import tools.jackson.databind.node.StringNode;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPatchSerializationTests {

	ObjectMapper mapper = new ObjectMapper();

	@Test
	void given_empty_JsonPatch() {
		JsonPatch jsonPatch1 = new JsonPatch();
		JsonPatch jsonPatch2 = new JsonPatch();
		jsonPatch2.setOperations(List.of());

		String expected = "[]";

		String serialized1 = this.mapper.writeValueAsString(jsonPatch1);
		String serialized2 = this.mapper.writeValueAsString(jsonPatch2);

		assertThat(serialized1).isEqualTo(expected);
		assertThat(serialized2).isEqualTo(expected);
	}

	@Test
	void given_JsonPatch_that_has_operations() {
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
		JsonPatch jsonPatch = new JsonPatch();
		jsonPatch.setOperations(List.of(op1, op2, op3));

		String expected = "[{\"from\":null,\"op\":\"remove\",\"path\":\"/foo\",\"value\":null},{\"from\":null,\"op\":\"add\",\"path\":\"/bar\",\"value\":\"dummy\"},{\"from\":null,\"op\":\"add\",\"path\":\"/bar\",\"value\":null}]";
		String serialized = this.mapper.writeValueAsString(jsonPatch);

		assertThat(serialized).isEqualTo(expected);
	}

}
