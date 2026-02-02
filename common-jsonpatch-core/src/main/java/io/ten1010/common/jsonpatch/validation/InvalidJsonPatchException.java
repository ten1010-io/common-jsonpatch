package io.ten1010.common.jsonpatch.validation;

import java.util.List;

public class InvalidJsonPatchException extends RuntimeException {

	private final List<PatchError> patchErrors;

	public InvalidJsonPatchException(List<PatchError> patchErrors) {
		this.patchErrors = patchErrors;
	}

	public List<PatchError> getPatchErrors() {
		return this.patchErrors;
	}

}
