package com.simplificator.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector2;
import com.laamella.amazingmazes.mazemodel.Position;
import com.laamella.amazingmazes.mazemodel.grid.Direction;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MazeToMesh {


    private static final int POSITION_COMPONENTS = 3;
    private static final int COLOR_COMPONENTS = 4;
    float[] vertices = new float[1024];
    int position = 0;


    public static Mesh getRandom() {
        MazeToMesh mazeToMesh = new MazeToMesh();
        for (int i = 0; i < 100; i++) {
            mazeToMesh.addWall(1, (int) (Math.random() * 100) - 50, 0f, (int) (Math.random() * 100) - 50);
        }
        return mazeToMesh.create();
    }

    public void addWall(float size, float x, float y, float z) {
        addWallImpl(2.4f, size, x, y, z, 0);
    }


    public void addWall(Position position, Direction direction, Color color) {
        float rotRad = (float) Math.toRadians(getDegFromDirection(direction));
        addWallImpl(2.4f, 1, -position.x, -position.y, rotRad, color);
    }

    private float getDegFromDirection(Direction direction) {
        switch (direction) {
            case UP:
                return 0;
            case DOWN:
                return 180;
            case RIGHT:
                return 90;
            case LEFT:
                return 270;
        }
        return 0;
    }


    private void check() {
        if (position + 100 > vertices.length) {
            vertices = Arrays.copyOf(vertices, vertices.length + 1024);
        }
    }

    public Mesh create() {
        Mesh mesh = new Mesh(true, vertices.length, 0,
                                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        mesh.setVertices(vertices);
        return mesh;
    }

    private void addWallImpl(float height, float width, float x, float y, float rotation, Color color) {
        check();


        //Color color = new Color(1, 0, (float) Math.random(), 1);

        float halfWidth = width / 2f;
        float halfWidthRotX = (float) (halfWidth * Math.sin(rotation));
        float halfWidthRotZ = (float) (halfWidth * Math.cos(rotation));

        RotMatrix matrix = new RotMatrix(rotation);

        Vector2 vec2;
        vec2 = new Vector2( - halfWidth, 0.5f);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = height;
        vertices[position++] = matrix.getZ(vec2)+y;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( halfWidth, 0.5f);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] =  height;
        vertices[position++] = matrix.getZ(vec2)+y;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( halfWidth, 0.5f);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = 0;
        vertices[position++] = matrix.getZ(vec2)+y;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( - halfWidth, 0.5f);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = height;
        vertices[position++] = matrix.getZ(vec2)+y;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( halfWidth, 0.5f);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = 0;
        vertices[position++] = matrix.getZ(vec2)+y;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( - halfWidth,0.5f);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = 0;
        vertices[position++] = matrix.getZ(vec2)+y;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

    }

    private void addWallImpl(float height, float width, float x, float y, float z, float rotation) {
        check();

        Color color = new Color(1, 0, (float) Math.random(), 1);

        float halfWidth = width / 2f;
        float halfWidthRotX = (float) (halfWidth * Math.sin(rotation));
        float halfWidthRotZ = width / 2f;

        vertices[position++] = x - halfWidth;
        vertices[position++] = y + height;
        vertices[position++] = z;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = x + halfWidth;
        vertices[position++] = y + height;
        vertices[position++] = z;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = x + halfWidth;
        vertices[position++] = y;
        vertices[position++] = z;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = x - halfWidth;
        vertices[position++] = y + height;
        vertices[position++] = z;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = x + halfWidth;
        vertices[position++] = y;
        vertices[position++] = z;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = x - halfWidth;
        vertices[position++] = y;
        vertices[position++] = z;
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

    }

}
