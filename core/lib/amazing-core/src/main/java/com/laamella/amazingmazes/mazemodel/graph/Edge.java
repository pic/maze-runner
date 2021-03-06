package com.laamella.amazingmazes.mazemodel.graph;

import com.laamella.amazingmazes.mazemodel.State;

/**
 * Connects two vertices.
 */
// TODO edges might need an orientation in degrees
// TODO edges can be made uni-directional
public interface Edge extends State {
	Vertex getVertexA();

	Vertex getVertexB();

	Vertex travel(Vertex sourceVertex);

	Graph getGraph();
}
