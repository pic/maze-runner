package com.simplificator.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

        mazeToMesh.addFloor();

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
        if (position + 1000 > vertices.length) {
            vertices = Arrays.copyOf(vertices, vertices.length + 1024);
        }
    }

    public Mesh create() {
        Mesh mesh = new Mesh(true, position, 0,
                                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                                new VertexAttribute(VertexAttributes.Usage.Normal, POSITION_COMPONENTS, "a_normal"),
                                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        mesh.setVertices(Arrays.copyOfRange(vertices, 0, position));
        return mesh;
    }

    private void addWallImpl(float height, float width, float x, float y, float rotation, Color color) {
        check();


        //Color color = new Color(1, 0, (float) Math.random(), 1);

        float halfWidth = width / 2f;
        float halfWidthRotX = (float) (halfWidth * Math.sin(rotation));
        float halfWidthRotZ = (float) (halfWidth * Math.cos(rotation));

        RotMatrix matrix = new RotMatrix(rotation);

        Vector3 normal = new Vector3(0,0,-1);
        Vector3 normalInv = new Vector3(0,0,1);
        Matrix4 rotmat = new Matrix4();
        rotmat.rotateRad(new Vector3(0, 1, 0), rotation);
        normal = normal.mul(rotmat);
        normalInv = normalInv.mul(rotmat);

        System.out.println(normal.toString());

        float dist = 0.49f;

        Vector2 vec2;
        vec2 = new Vector2( - halfWidth, dist);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = height;
        vertices[position++] = matrix.getZ(vec2)+y;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( halfWidth, dist);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] =  height;
        vertices[position++] = matrix.getZ(vec2)+y;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( halfWidth, dist);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = 0;
        vertices[position++] = matrix.getZ(vec2)+y;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( - halfWidth, dist);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = height;
        vertices[position++] = matrix.getZ(vec2)+y;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( halfWidth, dist);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = 0;
        vertices[position++] = matrix.getZ(vec2)+y;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vec2 = new Vector2( - halfWidth,dist);
        vertices[position++] = matrix.getX(vec2)+x;
        vertices[position++] = 0;
        vertices[position++] = matrix.getZ(vec2)+y;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;
// backside
//
//        vec2 = new Vector2( - halfWidth, dist);
//        vertices[position++] = matrix.getX(vec2)+x;
//        vertices[position++] = height;
//        vertices[position++] = matrix.getZ(vec2)+y;
//        addNormal(normalInv);
//        vertices[position++] = color.r;    //Color(r, g, b, a)
//        vertices[position++] = color.g;
//        vertices[position++] = color.b;
//        vertices[position++] = color.a;
//
//        vec2 = new Vector2( halfWidth, dist);
//        vertices[position++] = matrix.getX(vec2)+x;
//        vertices[position++] = 0;
//        vertices[position++] = matrix.getZ(vec2)+y;
//        addNormal(normalInv);
//        vertices[position++] = color.r;    //Color(r, g, b, a)
//        vertices[position++] = color.g;
//        vertices[position++] = color.b;
//        vertices[position++] = color.a;
//
//        vec2 = new Vector2( halfWidth, dist);
//        vertices[position++] = matrix.getX(vec2)+x;
//        vertices[position++] =  height;
//        vertices[position++] = matrix.getZ(vec2)+y;
//        addNormal(normalInv);
//        vertices[position++] = color.r;    //Color(r, g, b, a)
//        vertices[position++] = color.g;
//        vertices[position++] = color.b;
//        vertices[position++] = color.a;
//
//        vec2 = new Vector2( - halfWidth, dist);
//        vertices[position++] = matrix.getX(vec2)+x;
//        vertices[position++] = height;
//        vertices[position++] = matrix.getZ(vec2)+y;
//        addNormal(normalInv);
//        vertices[position++] = color.r;    //Color(r, g, b, a)
//        vertices[position++] = color.g;
//        vertices[position++] = color.b;
//        vertices[position++] = color.a;
//
//        vec2 = new Vector2( - halfWidth,dist);
//        vertices[position++] = matrix.getX(vec2)+x;
//        vertices[position++] = 0;
//        vertices[position++] = matrix.getZ(vec2)+y;
//        addNormal(normalInv);
//        vertices[position++] = color.r;    //Color(r, g, b, a)
//        vertices[position++] = color.g;
//        vertices[position++] = color.b;
//        vertices[position++] = color.a;
//
//        vec2 = new Vector2( halfWidth, dist);
//        vertices[position++] = matrix.getX(vec2)+x;
//        vertices[position++] = 0;
//        vertices[position++] = matrix.getZ(vec2)+y;
//        addNormal(normalInv);
//        vertices[position++] = color.r;    //Color(r, g, b, a)
//        vertices[position++] = color.g;
//        vertices[position++] = color.b;
//        vertices[position++] = color.a;


    }

    private void addNormal(Vector3 normal) {
        vertices[position++] = normal.x;
        vertices[position++] = normal.y;
        vertices[position++] = normal.z;
    }

    public void addFloor(){
        check();

        float yFloor = 0;

        Color color = Color.BLUE;

        Vector3 normal = new Vector3(0,-1,0);
        vertices[position++] = -1000;
        vertices[position++] = yFloor;
        vertices[position++] = -1000;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = 1000;
        vertices[position++] = yFloor;
        vertices[position++] = -1000;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = 1000;
        vertices[position++] = yFloor;
        vertices[position++] = 1000;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = -1000;
        vertices[position++] = yFloor;
        vertices[position++] = -1000;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = 1000;
        vertices[position++] = yFloor;
        vertices[position++] = 1000;
        addNormal(normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = -1000;
        vertices[position++] = yFloor;
        vertices[position++] = 1000;
        addNormal(normal);
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
