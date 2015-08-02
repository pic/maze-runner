package com.simplificator.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.laamella.amazingmazes.mazemodel.Position;
import com.laamella.amazingmazes.mazemodel.grid.Direction;
import com.laamella.amazingmazes.mazemodel.grid.Grid;
import com.laamella.amazingmazes.mazemodel.grid.Square;
import com.laamella.amazingmazes.mazemodel.grid.Wall;
import com.simplificator.amazing.AmazingWrapper;

public class GridToMesh {

    public GridToMesh() {
    }

    public static Color[] colors = new Color[]{new Color(1, 0, 0, 0), new Color(0, 1, 0, 0)};
    int colorpointer = 0;


    public static MeshAndIndex generate() {


        Grid grid = AmazingWrapper.generate();
        MazeToMesh mazeToMesh = new MazeToMesh();

        for (int i = 0; i < grid.getSize().width; i++) {
            for (int j = 0; j < grid.getSize().height; j++) {
                Position position = new Position(i, j);
                Square square = grid.getSquare(position);
                for (Direction direction : Direction.values()) {
                    Wall wall = square.getWall(direction);
                    if (!wall.isOpen()) {
                        mazeToMesh.addWall(position, direction, new Color(0.5f + (float) Math.random() * 0.2f, 0.7f + (float) Math.random() * 0.2f, 0.2f + (float) Math.random() * 0.2f, 1));
                    }
                    //System.out.println("" + i + "," + j + ": " + direction.name() + ", " + !wall.isOpen());
                }
                //System.out.println();
            }
        }

        //mazeToMesh.addFloor();
        return mazeToMesh.create();
    }
}
