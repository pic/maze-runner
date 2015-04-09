package com.laamella.amazingmazes.mazemodel.matrix;

import com.laamella.amazingmazes.mazemodel.Position;
import com.laamella.amazingmazes.mazemodel.Size;

public interface Matrix<T> {
	void set(Position position, T value);

	T get(Position position);

	Size getSize();

	public static class UtilityWrapper<T> implements Matrix<T> {
		private final Matrix<T> delegateMatrix;

		public UtilityWrapper(final Matrix<T> delegateMatrix) {
			this.delegateMatrix = delegateMatrix;
		}

		public interface MatrixVisitor<T> {
			void visit(Position position, T value);

			void endRow();

			void startRow();
		}

		public void visitAllSquares(final MatrixVisitor<T> visitor) {
			for (int y = 0; y < delegateMatrix.getSize().height; y++) {
				visitor.startRow();
				for (int x = 0; x < delegateMatrix.getSize().width; x++) {
					final Position position = new Position(x, y);
					visitor.visit(position, delegateMatrix.get(position));
				}
				visitor.endRow();
			}
		}

		@Override
		public T get(final Position position) {
			return delegateMatrix.get(position);
		}

		@Override
		public Size getSize() {
			return delegateMatrix.getSize();
		}

		@Override
		public void set(final Position position, final T value) {
			delegateMatrix.set(position, value);
		}

		@Override
		public String toString() {
			return "\n" + delegateMatrix.toString();
		}
	}
}
