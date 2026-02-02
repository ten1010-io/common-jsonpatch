package io.ten1010.common.jsonpatch;

import io.ten1010.common.jsonpatch.dto.JsonPatchOperation;
import io.ten1010.common.jsonpatch.validation.JsonPatchValidator;
import org.jspecify.annotations.Nullable;

public class JsonPatchOperationBuilder {

	private final JsonPatchValidator validator;

	@Nullable private String op;

	@Nullable private String path;

	@Nullable private Object value;

	@Nullable private String from;

	public JsonPatchOperationBuilder() {
		this.validator = new JsonPatchValidator();
	}

	public AddPathStep add() {
		reset();
		return new AddPathStep();
	}

	public CopyPathStep copy() {
		reset();
		return new CopyPathStep();
	}

	public MovePathStep move() {
		reset();
		return new MovePathStep();
	}

	public RemovePathStep remove() {
		reset();
		return new RemovePathStep();
	}

	public ReplacePathStep replace() {
		reset();
		return new ReplacePathStep();
	}

	public TestPathStep test() {
		reset();
		return new TestPathStep();
	}

	private JsonPatchOperation build() {
		JsonPatchOperation operation = new JsonPatchOperation();
		operation.setOp(this.op);
		operation.setPath(this.path);
		operation.setValue(this.value);
		operation.setFrom(this.from);
		this.validator.requireValid(operation);
		return operation;
	}

	private void reset() {
		this.op = null;
		this.path = null;
		this.value = null;
		this.from = null;
	}

	public abstract static class Step<T> {

		protected abstract T next();

	}

	public abstract class PathStep<T> extends Step<T> {

		public T setPath(String path) {
			JsonPatchOperationBuilder.this.path = path;
			return next();
		}

	}

	public abstract class ValueStep<T> extends Step<T> {

		public T setValue(@Nullable Object value) {
			JsonPatchOperationBuilder.this.value = value;
			return next();
		}

	}

	public abstract class FromStep<T> extends Step<T> {

		public T setFrom(String from) {
			JsonPatchOperationBuilder.this.from = from;
			return next();
		}

	}

	public class BuildStep {

		public JsonPatchOperation build() {
			return JsonPatchOperationBuilder.this.build();
		}

	}

	public class AddPathStep extends PathStep<AddValueStep> {

		public AddPathStep() {
			JsonPatchOperationBuilder.this.op = JsonPatchOp.ADD.getStr();
		}

		@Override
		protected AddValueStep next() {
			return new AddValueStep();
		}

	}

	public class AddValueStep extends ValueStep<BuildStep> {

		@Override
		protected BuildStep next() {
			return new BuildStep();
		}

	}

	public class CopyPathStep extends PathStep<CopyFromStep> {

		public CopyPathStep() {
			JsonPatchOperationBuilder.this.op = JsonPatchOp.COPY.getStr();
		}

		@Override
		protected CopyFromStep next() {
			return new CopyFromStep();
		}

	}

	public class CopyFromStep extends FromStep<BuildStep> {

		@Override
		protected BuildStep next() {
			return new BuildStep();
		}

	}

	public class MovePathStep extends PathStep<MoveFromStep> {

		public MovePathStep() {
			JsonPatchOperationBuilder.this.op = JsonPatchOp.MOVE.getStr();
		}

		@Override
		protected MoveFromStep next() {
			return new MoveFromStep();
		}

	}

	public class MoveFromStep extends FromStep<BuildStep> {

		@Override
		protected BuildStep next() {
			return new BuildStep();
		}

	}

	public class RemovePathStep extends PathStep<BuildStep> {

		public RemovePathStep() {
			JsonPatchOperationBuilder.this.op = JsonPatchOp.REMOVE.getStr();
		}

		@Override
		protected BuildStep next() {
			return new BuildStep();
		}

	}

	public class ReplacePathStep extends PathStep<ReplaceValueStep> {

		public ReplacePathStep() {
			JsonPatchOperationBuilder.this.op = JsonPatchOp.REPLACE.getStr();
		}

		@Override
		protected ReplaceValueStep next() {
			return new ReplaceValueStep();
		}

	}

	public class ReplaceValueStep extends ValueStep<BuildStep> {

		@Override
		protected BuildStep next() {
			return new BuildStep();
		}

	}

	public class TestPathStep extends PathStep<TestValueStep> {

		public TestPathStep() {
			JsonPatchOperationBuilder.this.op = JsonPatchOp.TEST.getStr();
		}

		@Override
		protected TestValueStep next() {
			return new TestValueStep();
		}

	}

	public class TestValueStep extends ValueStep<BuildStep> {

		@Override
		protected BuildStep next() {
			return new BuildStep();
		}

	}

}
