package io.ten1010.common.jsonpatch;

import java.util.List;

import io.ten1010.common.jsonpatch.dto.JsonPatch;
import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import io.ten1010.common.jsonpatch.validation.InvalidJsonPatchException;
import io.ten1010.common.jsonpatch.validation.OperationElementPatchError;
import io.ten1010.common.jsonpatch.validation.PatchError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class JsonPatchBuilderTests {

	JsonPatchBuilder builder = new JsonPatchBuilder();

	@Test
	void given_valid_operations() {
		JsonPatchOperation op1 = new JsonPatchOperation();
		op1.setOp("remove");
		op1.setPath("/foo");
		JsonPatchOperation op2 = new JsonPatchOperation();
		op2.setOp("remove");
		op2.setPath("/bar");

		JsonPatch patch1 = this.builder.addToOperations(op1).addToOperations(op2).build();
		JsonPatch patch2 = this.builder.withOperations(List.of(op1, op2)).build();

		assertThat(patch1).isEqualTo(patch2);
	}

	@Test
	void given_invalid_operations() {
		JsonPatchOperation op1 = new JsonPatchOperation();
		op1.setOp("remove");
		op1.setPath("/foo");
		JsonPatchOperation op2 = new JsonPatchOperation();
		op2.setOp("remove");
		op2.setPath("bar");

		try {
			this.builder.addToOperations(op1).addToOperations(op2).build();
			fail();
		}
		catch (InvalidJsonPatchException ex) {
			assertThat(ex).isInstanceOf(InvalidJsonPatchException.class);
			List<PatchError> errors = ex.getPatchErrors();
			assertThat(errors.size()).isEqualTo(1);
			assertThat(errors.get(0)).isInstanceOf(OperationElementPatchError.class);
		}
	}

}
