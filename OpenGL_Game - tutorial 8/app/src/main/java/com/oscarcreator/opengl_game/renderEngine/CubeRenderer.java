package com.oscarcreator.opengl_game.renderEngine;

import android.content.Context;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.SurfaceView;

import com.oscarcreator.opengl_game.R;
import com.oscarcreator.opengl_game.entities.Camera;
import com.oscarcreator.opengl_game.entities.Entity;
import com.oscarcreator.opengl_game.library.Vector3f;
import com.oscarcreator.opengl_game.models.RawModel;
import com.oscarcreator.opengl_game.models.TexturedModel;
import com.oscarcreator.opengl_game.shaders.StaticShader;
import com.oscarcreator.opengl_game.textures.ModelTexture;
import com.oscarcreator.opengl_game.toolbox.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;


public class CubeRenderer implements Renderer {

    private final Context context;

    //Viewmatrix representation
    private final float[] viewMatrix = new float[16];
    //Placeholders for multiplications
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

//    private Table table;
//    private Mallet mallet;
//    private Puck puck;
//    private Cube cube;
//
//    private TextureShaderProgram textureProgram;
//    private ColorShaderProgram colorProgram;

    private StaticShader shaderProgram;
    private com.oscarcreator.opengl_game.renderEngine.Renderer renderer;
    private Loader loader;
    private Entity entity;
    private Camera camera;

    private GLSurfaceView surfaceView;

    //private int texture;

    public CubeRenderer(Context context, GLSurfaceView surfaceView) {
        this.context = context;
        this.surfaceView = surfaceView;
//        glViewport(0,0, context.getResources().getDisplayMetrics().widthPixels,
//                context.getResources().getDisplayMetrics().heightPixels);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        //Clear color to black
        glClearColor(0,0,0,0);

        GLES10.glViewport(0,0, surfaceView.getWidth(), surfaceView.getHeight());


        Log.i("CubeRenderer","width:" + surfaceView.getWidth() + " height:" +
                surfaceView.getHeight());

        shaderProgram = new StaticShader(context);

        loader = new Loader();
        renderer = new com.oscarcreator.opengl_game.renderEngine.Renderer(surfaceView.getWidth(),
                surfaceView.getHeight(), shaderProgram);
        // OpenGL expects vertices to be defined counter clockwise by default
//        float[] vertices = {
//                -1f, 1f, 0,
//                -1f, -1f, 0,
//                1f, -1f, 0,
//                1f, 1f, 0
//        };
//
//        float[] textureCoords = {
//                0,0,
//                0,1,
//                1,1,
//                1,0
//        };
//
//        //Indexbuffer, the position of the order which to create triangles
//        int[] indices = {
//                0,1,3,
//                3,1,2
//        };
        float[] vertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f

        };

        float[] textureCoords = {

                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0


        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);

        ModelTexture texture = new ModelTexture(loader.loadTexture(context, R.drawable.image));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0,0,0,2f);
        camera = new Camera();

//        //Creating our table and mallet objects
//        table = new Table();
//        mallet = new Mallet(0.08f, 0.15f, 32);
//        puck = new Puck(0.06f, 0.02f, 32);
//        cube = new Cube();
//
//
//        //Creating our shader programs
//        textureProgram = new TextureShaderProgram(context);
//        colorProgram = new ColorShaderProgram(context);
//
//        //Loading table texture
//        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);

    }


    @Override
    public void onDrawFrame(GL10 gl) {
        //Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0,0, 0, 1);
        //This will cache the results of multiplying the projection and view matrices
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix,0,
                viewMatrix, 0);

        /*
        //Draw the table
        positionTableInScene();
        //Sets the textureProgram the current shader
        textureProgram.useProgram();
        //texture - loaded texture of the table
        textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        table.bindData(textureProgram);
        //Draws the mallet by using the bounded array with the
        // positions and texture
        table.draw();
*/

/*
        //Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        //Sets the colorProgram the current shader
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix, 1, 0, 0);
        mallet.bindData(colorProgram);
        mallet.draw();
        //Drawing the same mallet at a different position and color.
        positionObjectInScene(0f, mallet.height / 2f, 0.4f);
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        mallet.draw();
*/


        /*
        //Draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f);
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f,0.8f,1);
        puck.bindData(colorProgram);
        puck.draw();
*/

        entity.increaseRotation(1,1,0);
        camera.move();
        renderer.prepare();
        shaderProgram.useProgram();
        shaderProgram.loadViewMatrix(camera);
        renderer.render(entity, shaderProgram);
        shaderProgram.stop();

//        positionObjectInScene(0f, 0.1f, 0f);
//        colorProgram.useProgram();
//        colorProgram.setUniforms(modelViewProjectionMatrix, 1f,0f,0f);
//        cube.bindData(colorProgram);
//        cube.draw();


    }

    //This is called when the size on the screen is changed.
    //For example from portrait to landscape mode.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
        //Set up a projection matrix
        MatrixHelper.perspectiveM(projectionMatrix, 45,
                (float) width / (float) height, 1f, 10f);
        //Setting up a special type of projection matrix
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }


//    private void positionTableInScene(){
//        //The table is defined in xy-plane so we rotate it
//        // 90degrees to lie flat on the xz-plane.
//        setIdentityM(modelMatrix, 0);
//        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
//        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
//                0, modelMatrix, 0);
//    }
//
    private void positionObjectInScene(float x, float y, float z){
        //Identity matrix. ~1 in normal numbers
        //1 0 0
        //0 1 0
        //0 0 1

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }


}
