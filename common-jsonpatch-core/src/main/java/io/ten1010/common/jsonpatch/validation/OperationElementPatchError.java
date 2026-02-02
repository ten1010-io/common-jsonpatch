package io.ten1010.common.jsonpatch.validation;

public class OperationElementPatchError implements PatchError {

	private final int elementIndex;

	private final String reason;

	public OperationElementPatchError(int elementIndex, String reason) {
		this.elementIndex = elementIndex;
		this.reason = reason;
	}

	@Override
	public String getReason() {
		return String.format("Invalid operation element at index[%d] : %s", this.elementIndex, this.reason);
	}

}
