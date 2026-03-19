package io.ten1010.common.jsonpatch.validation;

public record GlobalPatchError(String reason) implements PatchError {

	@Override
	public String getReason() {
		return this.reason;
	}

}
