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
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MazeRunner extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private Mesh mesh;
    private PerspectiveCamera cam;
    private CameraHandler cameraHandler;
    private ShaderProgram shader;
    private long frameLastTime = 0;

    //Position attribute - (x, y)
    public static final int POSITION_COMPONENTS = 3;

    //Color attribute - (r, g, b, a)
    public static final int COLOR_COMPONENTS = 4;

    //Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;

    //The maximum number of triangles our mesh will hold
    public static final int MAX_TRIS = 1;

    //The maximum number of vertices our mesh will hold
    public static final int MAX_VERTS = MAX_TRIS * 3;

    //The array which holds all the data, interleaved like so:
    //    x, y, r, g, b, a
    //    x, y, r, g, b, a,
    //    x, y, r, g, b, a,
    //    ... etc ...
    private float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];

    //The index position
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

        mesh = GridToMesh.generate();

        shader = createMeshShader();
        cam = new PerspectiveCamera();
        cam.position.z = 4f;

        cameraHandler = new CameraHandler(cam);


        cheatCam = new PerspectiveCamera();

        cheatCam.position.y = -10f;
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
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cameraHandler.moveBack();
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

        int vertexCount = mesh.getMaxVertices() / 10;//(idx / NUM_COMPONENTS);
        //start the shader before setting any uniforms
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
        idx = 0;
    }


    @Override
    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }
}
