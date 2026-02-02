package io.ten1010.common.jsonpatch.validation;

public class GlobalPatchError implements PatchError {

	private final String reason;

	public GlobalPatchError(String reason) {
		this.reason = reason;
	}

	@Override
	public String getReason() {
		return this.reason;
	}

}
