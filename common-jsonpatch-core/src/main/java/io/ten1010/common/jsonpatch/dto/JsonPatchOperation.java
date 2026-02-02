package io.ten1010.common.jsonpatch.dto;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class JsonPatchOperation {

	@Nullable private String op;

	@Nullable private String path;

	@Nullable private Object value;

	@Nullable private String from;

	public JsonPatchOperation() {
	}

	public @Nullable String getOp() {
		return this.op;
	}

	public void setOp(@Nullable String op) {
		this.op = op;
	}

	public @Nullable String getPath() {
		return this.path;
	}

	public void setPath(@Nullable String path) {
		this.path = path;
	}

	public @Nullable Object getValue() {
		return this.value;
	}

	public void setValue(@Nullable Object value) {
		this.value = value;
	}

	public @Nullable String getFrom() {
		return this.from;
	}

	public void setFrom(@Nullable String from) {
		this.from = from;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JsonPatchOperation that = (JsonPatchOperation) o;
		return Objects.equals(this.op, that.op) && Objects.equals(this.path, that.path)
				&& Objects.equals(this.value, that.value) && Objects.equals(this.from, that.from);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.op, this.path, this.value, this.from);
	}

	@Override
	public String toString() {
		return "JsonPatchOperation{" + "op='" + this.op + '\'' + ", path='" + this.path + '\'' + ", value=" + this.value
				+ ", from='" + this.from + '\'' + '}';
	}

}
