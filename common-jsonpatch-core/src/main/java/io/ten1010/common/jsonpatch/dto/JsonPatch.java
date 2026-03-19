package io.ten1010.common.jsonpatch.dto;

import java.util.List;
import java.util.Objects;

import io.ten1010.common.jsonpatch.JsonPatchDeserializer;
import io.ten1010.common.jsonpatch.JsonPatchSerializer;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = JsonPatchSerializer.class)
@JsonDeserialize(using = JsonPatchDeserializer.class)
public class JsonPatch {

	@Nullable private List<JsonPatchOperation> operations;

	public JsonPatch() {
	}

	public @Nullable List<JsonPatchOperation> getOperations() {
		return this.operations;
	}

	public void setOperations(@Nullable List<JsonPatchOperation> operations) {
		this.operations = operations;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JsonPatch jsonPatch = (JsonPatch) o;
		return Objects.equals(this.operations, jsonPatch.operations);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.operations);
	}

	@Override
	public String toString() {
		return "JsonPatch{" + "operations=" + this.operations + '}';
	}

}
