package io.ten1010.common.jsonpatch;

import java.util.List;
import java.util.Optional;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import io.ten1010.common.jsonpatch.validation.JsonPatchValidator;
import io.ten1010.common.jsonpatch.validation.PatchError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPatchValidatorTests {

	JsonPatchValidator validator = new JsonPatchValidator();

	@Test
	void given_null_op_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp(null);
		operation.setPath("/foo");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_invalid_op_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("foo");
		operation.setPath("/foo");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_valid_remove_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("remove");
		operation.setPath("/foo");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isFalse();
	}

	@Test
	void given_valid_add_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("add");
		operation.setPath("/foo");
		operation.setValue(null);

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isFalse();
	}

	@Test
	void given_invalid_path_add_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("add");
		operation.setPath("foo");
		operation.setValue(null);

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_add_operation_that_has_non_null_from() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("add");
		operation.setPath("/foo");
		operation.setValue(null);
		operation.setFrom("foo");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_invalid_fields_add_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("add");
		operation.setPath("foo");
		operation.setValue(null);

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_valid_copy_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("copy");
		operation.setPath("/foo");
		operation.setFrom("/bar");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isFalse();
	}

	@Test
	void given_invalid_path_copy_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("copy");
		operation.setPath("foo");
		operation.setFrom("/bar");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_invalid_from_copy_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("copy");
		operation.setPath("/foo");
		operation.setFrom("bar");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_copy_operation_that_has_non_null_value() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("copy");
		operation.setPath("/foo");
		operation.setFrom("/bar");
		operation.setValue("dummy");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_invalid_fields_copy_operation() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp("copy");
		operation.setPath("foo");
		operation.setFrom("bar");

		Optional<PatchError> opt = this.validator.validate(operation);
		assertThat(opt.isPresent()).isTrue();
	}

	@Test
	void given_null_operations() {
		JsonPatch jsonPatch = new JsonPatch();

		List<PatchError> errors = this.validator.validate(jsonPatch);
		assertThat(errors.size()).isGreaterThan(0);
	}

	@Test
	void given_valid_operations() {
		JsonPatchOperation op1 = new JsonPatchOperation();
		op1.setOp("remove");
		op1.setPath("/foo");
		JsonPatchOperation op2 = new JsonPatchOperation();
		op2.setOp("remove");
		op2.setPath("/bar");

		JsonPatch jsonPatch = new JsonPatch();
		jsonPatch.setOperations(List.of(op1, op2));

		List<PatchError> errors = this.validator.validate(jsonPatch);
		assertThat(errors.size()).isEqualTo(0);
	}

	@Test
	void given_invalid_operations() {
		JsonPatchOperation op1 = new JsonPatchOperation();
		op1.setOp("remove");
		op1.setPath("foo");
		JsonPatchOperation op2 = new JsonPatchOperation();
		op2.setOp("remove");
		op2.setPath("/bar");

		JsonPatch jsonPatch = new JsonPatch();
		jsonPatch.setOperations(List.of(op1, op2));

		List<PatchError> errors = this.validator.validate(jsonPatch);
		assertThat(errors.size()).isGreaterThan(0);
	}

}
