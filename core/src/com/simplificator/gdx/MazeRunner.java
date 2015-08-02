package com.simplificator.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Arrays;

public class MazeRunner extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private Mesh mesh;
    private PerspectiveCamera cam;
    private CameraHandler cameraHandler;
    private ShaderProgram shader;
    private long frameLastTime = 0;

    // Position attribute - (x, y, z)
    public static final int POSITION_COMPONENTS = 3;

    public static final int NORMAL_COMPONENTS = 3;

    // Color attribute - (r, g, b, a)
    public static final int COLOR_COMPONENTS = 4;

    // Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + NORMAL_COMPONENTS + COLOR_COMPONENTS;

    // The index position
    private int idx = 0;


    public static final String VERT_SHADER =
        "attribute vec3 a_position;\n" +
            "attribute vec4 a_color;\n" +
            "attribute vec3 a_normal;\n" +
            "uniform mat4 u_projTrans;\n" +
            "uniform mat3 u_normalTrans;\n" +
            "varying vec4 vColor;\n" +
            "varying vec3 vNormal;\n" +
            "void main() {\n" +
            "	vColor = a_color;\n" +
            "   vNormal = u_normalTrans * a_normal; \n"+
            "	gl_Position =  u_projTrans * vec4(a_position.xyz, 1.0);\n" +
            "}";

    public static final String FRAG_SHADER =
        "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 vColor;\n" +
            "varying vec3 vNormal;\n" +
            "void main() {\n" +
            "   float brightness = max(0.0,dot(vNormal, vec3(0,0,-1)));  \n" +
            "   float brightness2 = max(0.0,dot(vNormal, vec3(0,0,1)));  \n" +
            "   brightness = max(brightness, brightness2);  \n" +
            "	gl_FragColor =  vColor*(brightness*0.5)+vColor*0.5;\n" +
            //"	gl_FragColor =  vec4(vNormal,1.0)*0.5+0.5;\n" +
            "}";
    private PerspectiveCamera cheatCam;


    protected static ShaderProgram createMeshShader() {
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
        String log = shader.getLog();
        if (!shader.isCompiled())
            throw new GdxRuntimeException(log);
        if (log != null && log.length() != 0)
            System.out.println("Shader Log: " + log);
        return shader;
    }


    @Override
    public void create() {

//        mesh = new Mesh(true, MAX_VERTS, 0,
//                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
//                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));

        MeshAndIndex meshAndIndex = GridToMesh.generate();

        mesh = meshAndIndex.mesh;
        idx = meshAndIndex.index;

        shader = createMeshShader();
        cam = new PerspectiveCamera();
        cam.position.z = 4f;

        cameraHandler = new CameraHandler(cam);


        cheatCam = new PerspectiveCamera();

        cheatCam.position.y = 10f;
        cheatCam.position.z = 4f;
        cheatCam.lookAt(0, 0, 0);
        cheatCam.update();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_CCW);


    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        cam.viewportWidth = width;
        cam.viewportHeight = height;

        cam.near = 0.1f;
        cam.far = 50f;

        cam.update();


        cheatCam.viewportWidth = width;
        cheatCam.viewportHeight = height;

        cheatCam.near = 0.1f;
        cheatCam.far = 50f;

        cheatCam.update();

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        updateCamera();
        updateCheatCamera();

        //this will render the triangles to GL
        flush();

    }

    private void addVertices(float[] vertices) {
        System.out.println(idx);
        float[] newVertices = new float[idx + vertices.length];
        newVertices = mesh.getVertices(newVertices);

        for (int i = 0 ; i < vertices.length; i++) {
            newVertices[i + idx] = vertices[i];
        }
        //mesh.setVertices(newVertices);

        mesh.setVertices(newVertices);

        //mesh.setVertices(vertices, idx - 60, vertices.length);
//        System.out.println(vertices.length);
//        mesh.setVertices(vertices, idx, idx + vertices.length);
//        idx += vertices.length;
    }

    private int addNormal(float[] vertices, int base, Vector3 normal) {
        vertices[base] = normal.x;
        vertices[base + 1] = normal.y;
        vertices[base + 2] = normal.z;
        return base + 3;
    }

    private void addRunner() {
        float[] vertices = new float[6 * NUM_COMPONENTS];

        System.out.println("aaaa");
        System.out.println(vertices.length);

        float yRunner = 1;

        Color color = Color.RED;

        int position = 0;

        Vector3 normal = new Vector3(1, 1, 1);

        vertices[position++] = cam.position.x - 0.3f;
        vertices[position++] = yRunner;
        vertices[position++] = cam.position.z - 0.3f;
        position = addNormal(vertices, position, normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = cam.position.x + 0.3f;
        vertices[position++] = yRunner;
        vertices[position++] = cam.position.z - 0.3f;
        position = addNormal(vertices, position, normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = cam.position.x + 0.3f;
        vertices[position++] = yRunner;
        vertices[position++] = cam.position.z + 0.3f;
        position = addNormal(vertices, position, normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = cam.position.x - 0.3f;
        vertices[position++] = yRunner;
        vertices[position++] = cam.position.z - 0.3f;
        position = addNormal(vertices, position, normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = cam.position.x + 0.3f;
        vertices[position++] = yRunner;
        vertices[position++] = cam.position.z + 0.3f;
        position = addNormal(vertices, position, normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        vertices[position++] = cam.position.x - 0.3f;
        vertices[position++] = yRunner;
        vertices[position++] = cam.position.z + 0.3f;
        position = addNormal(vertices, position, normal);
        vertices[position++] = color.r;    //Color(r, g, b, a)
        vertices[position++] = color.g;
        vertices[position++] = color.b;
        vertices[position++] = color.a;

        addVertices(vertices);
    }

    private void updateCamera() {

        if (frameLastTime == 0) {
            frameLastTime = System.currentTimeMillis();
        }

        long now = System.currentTimeMillis();
        long delta = now - frameLastTime;
        frameLastTime = now;

        cameraHandler.setFrameDelta(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cameraHandler.lookLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cameraHandler.lookRight();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cameraHandler.lookUp();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cameraHandler.lookDown();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cameraHandler.moveForward();
            addRunner();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cameraHandler.moveBack();
            addRunner();
        }

        cameraHandler.update();

    }

    private void updateCheatCamera() {
        cheatCam.position.x = cam.position.x;
        cheatCam.position.z = cam.position.z;

        cheatCam.lookAt(cam.position.x, 0, cam.position.z);

        cheatCam.update();
    }


    void flush() {


        //if we've already flushed
//        if (idx == 0)
//            return;

        //sends our vertex data to the mesh
        //  mesh.setVertices(verts);

        //enable blending, for alpha
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //number of vertices we need to render

        int vertexCount = mesh.getMaxVertices() / NUM_COMPONENTS; // (idx / NUM_COMPONENTS);
        //start the shader before setting any uniforms
        //System.out.println("vertexCount: " + vertexCount);

        shader.begin();

        //update the projection matrix so our triangles are rendered in 2D

        Matrix4 camCombined;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            camCombined = cheatCam.combined;
        } else {
            camCombined = cam.combined;
        }

        shader.setUniformMatrix("u_projTrans", camCombined);
        shader.setUniformMatrix("u_normalTrans", new Matrix3().set(cam.view).inv().transpose());

        //render the mesh
        mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);

        shader.end();

        //reset index to zero
        //idx = 0;
    }


    @Override
    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }
}
