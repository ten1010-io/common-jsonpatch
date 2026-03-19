package io.ten1010.common.jsonpatch;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class JsonPatchSerializer extends StdSerializer<JsonPatch> {

	public JsonPatchSerializer() {
		super(JsonPatch.class);
	}

	@Override
	public void serialize(JsonPatch value, JsonGenerator gen, SerializationContext provider) throws JacksonException {
		List<JsonPatchOperation> operations = value.getOperations();
		if (operations == null) {
			operations = List.of();
		}

		gen.writeStartArray();
		for (JsonPatchOperation e : operations) {
			gen.writePOJO(e);
		}
		gen.writeEndArray();
	}

}
