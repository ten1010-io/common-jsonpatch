package io.ten1010.common.jsonpatch;

import java.util.ArrayList;
import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import io.ten1010.common.jsonpatch.validation.JsonPatchValidator;

public class JsonPatchBuilder {

	private List<JsonPatchOperation> operations;

	private final JsonPatchValidator validator;

	public JsonPatchBuilder() {
		this.operations = new ArrayList<>();
		this.validator = new JsonPatchValidator();
	}

	public JsonPatch build() {
		JsonPatch patch = new JsonPatch();
		patch.setOperations(this.operations);
		this.validator.requireValid(patch);
		return patch;
	}

	public JsonPatchBuilder withOperations(List<JsonPatchOperation> operations) {
		this.operations = new ArrayList<>(operations);
		return this;
	}

	public JsonPatchBuilder addToOperations(JsonPatchOperation operation) {
		this.operations.add(operation);
		return this;
	}

}
