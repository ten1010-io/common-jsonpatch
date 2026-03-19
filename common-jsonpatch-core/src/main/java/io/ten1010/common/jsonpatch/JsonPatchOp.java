package io.ten1010.common.jsonpatch;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum JsonPatchOp {

	ADD("add"), COPY("copy"), MOVE("move"), REMOVE("remove"), REPLACE("replace"), TEST("test");

	private static final Map<String, JsonPatchOp> OP_MAP = Arrays.stream(values())
		.collect(Collectors.toUnmodifiableMap(JsonPatchOp::getStr, Function.identity()));

	public static Optional<JsonPatchOp> from(String str) {
		JsonPatchOp parsed = OP_MAP.get(str.toLowerCase());
		return Optional.ofNullable(parsed);
	}

	private final String str;

	JsonPatchOp(String str) {
		this.str = str;
	}

	public String getStr() {
		return this.str;
	}

}
