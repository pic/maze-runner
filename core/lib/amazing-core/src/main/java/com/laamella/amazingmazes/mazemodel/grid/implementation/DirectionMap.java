package com.laamella.amazingmazes.mazemodel.grid.implementation;

import com.laamella.amazingmazes.mazemodel.grid.Direction;

public class DirectionMap<T> {
	public T left;
	public T right;
	public T up;
	public T down;
	
	public DirectionMap(){
		//
	}

	public DirectionMap(final T up, final T right, final T down, final T left) {
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
	}

	public T get(Direction direction) {
		switch (direction) {
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case UP:
			return up;
		case DOWN:
			return down;
		default:
			throw new IllegalStateException();
		}
	}
}
