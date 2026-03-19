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
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.NullNode;

public class JsonPatchValidator {

	private static final JsonNode DEFAULT_VALUE = NullNode.getInstance();

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

	private static boolean isDefaultValue(JsonNode value) {
		return DEFAULT_VALUE.equals(value);
	}

	private static Optional<PatchError> error(String reason) {
		return Optional.of(new GlobalPatchError(reason));
	}

	public List<PatchError> validate(JsonPatch patch) {
		List<JsonPatchOperation> operations = patch.getOperations();

		if (operations == null) {
			return List.of(new GlobalPatchError(invalidValueReason("operations", null)));
		}

		List<PatchError> errors = new ArrayList<>();
		for (int i = 0; i < operations.size(); i++) {
			int index = i;
			JsonPatchOperation operation = operations.get(i);
			validate(operation).ifPresent((e) -> errors.add(new OperationElementPatchError(index, e.getReason())));
		}
		return Collections.unmodifiableList(errors);
	}

	public Optional<PatchError> validate(JsonPatchOperation operation) {
		String op = operation.getOp();
		String path = operation.getPath();
		JsonNode value = operation.getValue();
		String from = operation.getFrom();

		if (op == null) {
			return error(invalidValueReason("op", null));
		}
		Optional<JsonPatchOp> opOpt = JsonPatchOp.from(op);
		if (opOpt.isEmpty()) {
			return error(invalidValueReason("op", op));
		}
		if (!isValidPath(path)) {
			return error(invalidValueReason("path", path));
		}

		return switch (opOpt.get()) {
			case REMOVE -> {
				if (!isDefaultValue(value)) {
					yield error(fieldCanNotBeSetReason("value", op));
				}
				if (from != null) {
					yield error(fieldCanNotBeSetReason("from", op));
				}
				yield Optional.empty();
			}
			case ADD, REPLACE, TEST -> {
				if (from != null) {
					yield error(fieldCanNotBeSetReason("from", op));
				}
				yield Optional.empty();
			}
			case COPY, MOVE -> {
				if (!isValidPath(from)) {
					yield error(invalidValueReason("from", from));
				}
				if (!isDefaultValue(value)) {
					yield error(fieldCanNotBeSetReason("value", op));
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
		validate(operation).ifPresent((e) -> {
			throw new InvalidJsonPatchException(List.of(e));
		});
	}

}
