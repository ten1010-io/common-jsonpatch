package io.ten1010.common.jsonpatch.validation;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidJsonPatchException extends RuntimeException {

	private final List<PatchError> patchErrors;

	public InvalidJsonPatchException(List<PatchError> patchErrors) {
		super(patchErrors.stream().map(PatchError::getReason).collect(Collectors.joining("; ")));
		this.patchErrors = patchErrors;
	}

	public List<PatchError> getPatchErrors() {
		return this.patchErrors;
	}

}
