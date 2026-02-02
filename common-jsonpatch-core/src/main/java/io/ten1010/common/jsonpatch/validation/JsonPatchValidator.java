package io.ten1010.common.jsonpatch.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.ten1010.common.jsonpatch.JsonPatchOp;
import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JsonPointer;

public class JsonPatchValidator {

	private static String invalidValueReason(String field, @Nullable String value) {
		return String.format("Invalid value[%s] for field[%s]", value, field);
	}

	private static String fieldCanNotBeSetReason(String field, String op) {
		return String.format("field[%s] can not be set when using op[%s]", field, op);
	}

	private static boolean isValidPath(@Nullable String path) {
		try {
			JsonPointer.valueOf(path);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	public List<PatchError> validate(JsonPatch patch) {
		List<PatchError> errors = new ArrayList<>();
		List<JsonPatchOperation> operations = patch.getOperations();

		if (operations == null) {
			GlobalPatchError patchError = new GlobalPatchError(invalidValueReason("operations", null));
			errors.add(patchError);
			return Collections.unmodifiableList(errors);
		}

		for (int i = 0; i < operations.size(); i++) {
			JsonPatchOperation operation = operations.get(i);
			Optional<PatchError> opt = validate(operation);
			if (opt.isEmpty()) {
				continue;
			}
			errors.add(new OperationElementPatchError(i, opt.get().getReason()));
		}
		return Collections.unmodifiableList(errors);
	}

	public Optional<PatchError> validate(JsonPatchOperation operation) {
		String op = operation.getOp();
		String path = operation.getPath();
		Object value = operation.getValue();
		String from = operation.getFrom();

		if (op == null) {
			GlobalPatchError error = new GlobalPatchError(invalidValueReason("op", op));
			return Optional.of(error);
		}
		Optional<JsonPatchOp> opOpt = JsonPatchOp.from(op);
		if (opOpt.isEmpty()) {
			GlobalPatchError error = new GlobalPatchError(invalidValueReason("op", op));
			return Optional.of(error);
		}
		if (!isValidPath(path)) {
			GlobalPatchError error = new GlobalPatchError(invalidValueReason("path", path));
			return Optional.of(error);
		}

		return switch (opOpt.get()) {
			case REMOVE -> {
				if (value != null) {
					GlobalPatchError error = new GlobalPatchError(fieldCanNotBeSetReason("value", op));
					yield Optional.of(error);
				}
				if (from != null) {
					GlobalPatchError error = new GlobalPatchError(fieldCanNotBeSetReason("from", op));
					yield Optional.of(error);
				}
				yield Optional.empty();
			}
			case ADD, REPLACE, TEST -> {
				if (from != null) {
					GlobalPatchError error = new GlobalPatchError(fieldCanNotBeSetReason("from", op));
					yield Optional.of(error);
				}
				yield Optional.empty();
			}
			case COPY, MOVE -> {
				if (!isValidPath(from)) {
					GlobalPatchError error = new GlobalPatchError(invalidValueReason("from", from));
					yield Optional.of(error);
				}
				if (value != null) {
					GlobalPatchError error = new GlobalPatchError(fieldCanNotBeSetReason("value", op));
					yield Optional.of(error);
				}
				yield Optional.empty();
			}
		};
	}

	public void requireValid(JsonPatch patch) {
		List<PatchError> errors = validate(patch);
		if (!errors.isEmpty()) {
			throw new InvalidJsonPatchException(errors);
		}
	}

	public void requireValid(JsonPatchOperation operation) {
		Optional<PatchError> opt = validate(operation);
		if (opt.isPresent()) {
			throw new InvalidJsonPatchException(List.of(opt.get()));
		}
	}

}
