package io.ten1010.common.jsonpatch.validation;

public sealed interface PatchError permits GlobalPatchError, OperationElementPatchError {

	String getReason();

}
