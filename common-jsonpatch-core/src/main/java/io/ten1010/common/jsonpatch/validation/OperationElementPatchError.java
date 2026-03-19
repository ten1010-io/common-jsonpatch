package io.ten1010.common.jsonpatch.validation;

public record OperationElementPatchError(int elementIndex, String reason) implements PatchError {

	@Override
	public String getReason() {
		return String.format("Invalid operation element at index[%d] : %s", this.elementIndex, this.reason);
	}

}
