package io.ten1010.common.jsonpatch.jackson;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

public class JsonPatchDeserializer extends StdDeserializer<JsonPatch> {

	private static final TypeReference<List<JsonPatchOperation>> TYPE_REFERENCE = new TypeReference<>() {
	};

	public JsonPatchDeserializer(Class<?> c) {
		super(c);
	}

	@Override
	public JsonPatch deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		List<JsonPatchOperation> operations = p.readValueAs(TYPE_REFERENCE);

		JsonPatch jsonPatch = new JsonPatch();
		jsonPatch.setOperations(operations);

		return jsonPatch;
	}

}
