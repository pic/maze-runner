package com.simplificator.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import static com.badlogic.gdx.graphics.VertexAttributes.Usage.Position;

public class MazeRunner extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	Mesh mesh;
	PerspectiveCamera cam;
	ShaderProgram shader;

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
					"uniform mat4 u_projTrans;\n" +
					"varying vec4 vColor;\n" +
					"void main() {\n" +
					"	vColor = a_color;\n" +
					"	gl_Position =  u_projTrans * vec4(a_position.xyz, 1.0);\n" +
					"}";

	public static final String FRAG_SHADER =
			"#ifdef GL_ES\n" +
					"precision mediump float;\n" +
					"#endif\n" +
					"varying vec4 vColor;\n" +
					"void main() {\n" +
					"	gl_FragColor = vColor;\n" +
					"}";


	protected static ShaderProgram createMeshShader() {
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
		String log = shader.getLog();
		if (!shader.isCompiled())
			throw new GdxRuntimeException(log);
		if (log!=null && log.length()!=0)
			System.out.println("Shader Log: "+log);
		return shader;
	}


	@Override
	public void create () {

		mesh = new Mesh(true, MAX_VERTS, 0,
				new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
				new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));

		shader = createMeshShader();
		cam = new PerspectiveCamera();
		cam.position.z = 4f;

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		cam.viewportWidth = width;
		cam.viewportHeight = height;

		cam.near = 0.1f;
		cam.far = 50f;


		cam.update();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		//this will push the triangles into the batch
		drawTriangle(-1, -1, 0, 2, 2, Color.RED);
		drawTriangle(-2, -2, -2, 2, 2, Color.BLUE);

		//this will render the triangles to GL
		flush();

	}

	void flush() {
		//if we've already flushed
		if (idx==0)
			return;

		//sends our vertex data to the mesh
		mesh.setVertices(verts);




		//enable blending, for alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		//number of vertices we need to render
		int vertexCount = (idx/NUM_COMPONENTS);

		if( Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			cam.direction.x-=0.01f;
		}
		if( Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			cam.direction.x+=0.01f;
		}

		if( Gdx.input.isKeyPressed(Input.Keys.UP)){
			cam.position.z-=0.04f;
		}
		if( Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			cam.position.z+=0.04f;
		}


		cam.update();

			//start the shader before setting any uniforms
		shader.begin();

		//update the projection matrix so our triangles are rendered in 2D
		shader.setUniformMatrix("u_projTrans", cam.combined);

		//render the mesh
		mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);

		shader.end();

		//reset index to zero
		idx = 0;
	}


	void drawTriangle(float x, float y, float z, float width, float height, Color color) {
		//we don't want to hit any index out of bounds exception...
		//so we need to flush the batch if we can't store any more verts
		if (idx==verts.length)
			flush();

		//now we push the vertex data into our array
		//we are assuming (0, 0) is lower left, and Y is up

		//bottom left vertex
		verts[idx++] = x; 			//Position(x, y)
		verts[idx++] = y;
		verts[idx++] = z;
		verts[idx++] = color.r; 	//Color(r, g, b, a)
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;

		//top left vertex
		verts[idx++] = x; 			//Position(x, y)
		verts[idx++] = y + height;
		verts[idx++] = z;
		verts[idx++] = color.r; 	//Color(r, g, b, a)
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;

		//bottom right vertex
		verts[idx++] = x + width;	 //Position(x, y)
		verts[idx++] = y;
		verts[idx++] = z;
		verts[idx++] = color.r;		 //Color(r, g, b, a)
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;

	}
	@Override
	public void dispose() {
		mesh.dispose();
		shader.dispose();
	}
}
