package com.simplificator.amazing;

import com.laamella.amazingmazes.generators.Randomizer;
import com.laamella.amazingmazes.generators.daedalus.SideWinderMazeGenerator;
import com.laamella.amazingmazes.mazemodel.Position;
import com.laamella.amazingmazes.mazemodel.Size;
import com.laamella.amazingmazes.mazemodel.grid.*;
import com.laamella.amazingmazes.mazemodel.grid.implementation.GridMatrixStorage;
import com.laamella.amazingmazes.mazemodel.grid.implementation.GridRowGenerator;
import com.laamella.amazingmazes.mazemodel.grid.implementation.GridStateStorage;
import com.laamella.amazingmazes.mazemodel.grid.implementation.GridWithDecoupledState;
import com.laamella.amazingmazes.mazemodel.matrix.Matrix;
import com.laamella.amazingmazes.mazemodel.matrix.implementation.StateMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: pic
 * Date: 4/9/15
 * Time: 12:00 PM
 */
public class AmazingWrapper {

    public static void main(String[] args) {

        generate();


    }

    public static Grid generate() {
        Matrix matrix = new StateMatrix(new Size(121, 121));

        GridStateStorage matrixStorage = new GridMatrixStorage(matrix);


        Grid grid = new GridWithDecoupledState(matrixStorage);

        GridRowGenerator gridRowGenerator = new GridRowGenerator(grid);

        SideWinderMazeGenerator generator = new SideWinderMazeGenerator(new Randomizer.Default(0));

        generator.generateMaze(gridRowGenerator);

        //System.out.println(matrixStorage.getSquareState(new Position(0, 0)));
        //System.out.println(matrixStorage.getWallState(new Position(0, 0), false));

//        for (int i = 0; i < grid.getSize().width; i++) {
//            for (int j = 0; j < grid.getSize().height; j++) {
//                Square square = grid.getSquare(new Position(i, j));
//                for (Direction direction : Direction.values()) {
//                    Wall wall = square.getWall(direction);
//                    System.out.println("" + i + "," + j + ": " + direction.name()+ ", "+  !wall.isOpen());
//                }
//                System.out.println();
//
//            }
//        }

        return grid;
    }


}
