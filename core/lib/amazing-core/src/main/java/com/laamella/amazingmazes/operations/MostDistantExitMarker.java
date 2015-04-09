package com.laamella.amazingmazes.operations;

import com.laamella.amazingmazes.generators.MazeGenerator;
import com.laamella.amazingmazes.mazemodel.MazeDefinitionState;
import com.laamella.amazingmazes.mazemodel.graph.Graph;
import com.laamella.amazingmazes.mazemodel.graph.Vertex;

/**
 * Not a solver, but uses a solving algorithm. Finds the square on the border of
 * the grid that is the most distant from the entrance.
 */
public class MostDistantExitMarker {
	public void findMostDistantExit(final Graph graph) {
		final Graph.UtilityWrapper utilityGraph = new Graph.UtilityWrapper(graph);
		final Vertex entrance = utilityGraph.getEntrance();
		new VertexDistanceMarker().mark(entrance);

		Vertex mostDistantExit = null;
		int largestDistance = 0;
		for (final Vertex vertex : graph.getVertices()) {
			if (vertex.hasState(MazeGenerator.POSSIBLE_EXIT)) {
				final int distance = vertex.getState(VertexDistanceMarker.DISTANCE);
				if (distance > largestDistance) {
					largestDistance = distance;
					mostDistantExit = vertex;
				}
			}
		}
		mostDistantExit.setState(MazeDefinitionState.EXIT, true);
	}
}
