package io.ten1010.common.jsonpatch.jackson;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPatchSerializationTests {

	JsonMapper mapper;

	JsonPatchSerializationTests() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(JsonPatch.class, new JsonPatchSerializer(JsonPatch.class));
		this.mapper = JsonMapper.builder().addModule(module).build();
	}

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
		op2.setOp("remove");
		op2.setPath("/bar");
		JsonPatch jsonPatch = new JsonPatch();
		jsonPatch.setOperations(List.of(op1, op2));

		String expected = "[{\"from\":null,\"op\":\"remove\",\"path\":\"/foo\",\"value\":null},{\"from\":null,\"op\":\"remove\",\"path\":\"/bar\",\"value\":null}]";

		System.out.println(jsonPatch);
		String serialized = this.mapper.writeValueAsString(jsonPatch);

		assertThat(serialized).isEqualTo(expected);
	}

}
