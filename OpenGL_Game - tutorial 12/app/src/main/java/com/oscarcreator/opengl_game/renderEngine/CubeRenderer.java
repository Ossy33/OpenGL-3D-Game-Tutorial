package com.oscarcreator.opengl_game.renderEngine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.oscarcreator.opengl_game.R;
import com.oscarcreator.opengl_game.entities.Camera;
import com.oscarcreator.opengl_game.entities.Entity;
import com.oscarcreator.opengl_game.entities.Light;
import com.oscarcreator.opengl_game.library.Vector3f;
import com.oscarcreator.opengl_game.models.RawModel;
import com.oscarcreator.opengl_game.models.TexturedModel;
import com.oscarcreator.opengl_game.shaders.StaticShader;
import com.oscarcreator.opengl_game.textures.ModelTexture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;


public class CubeRenderer implements Renderer {

    private final Context context;


    private StaticShader shaderProgram;
    private com.oscarcreator.opengl_game.renderEngine.Renderer renderer;
    private Loader loader;
    private Entity entity;
    private Camera camera;
    private Light light;

    private GLSurfaceView surfaceView;

    //private int texture;

    public CubeRenderer(Context context, GLSurfaceView surfaceView) {
        this.context = context;
        this.surfaceView = surfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        //Clear color to black
        glClearColor(0,0,0,0);


        Log.i("CubeRenderer","width:" + surfaceView.getWidth() + " height:" +
                surfaceView.getHeight());

        shaderProgram = new StaticShader(context);

        loader = new Loader();
        renderer = new com.oscarcreator.opengl_game.renderEngine.Renderer(surfaceView.getWidth(),
                surfaceView.getHeight(), shaderProgram);


        RawModel model = OBJLoader.loadObjModel(context, R.raw.dragon, loader);

        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(context, R.drawable.white)));
        ModelTexture texture = texturedModel.getTexture();
        texture.setShineDamper(8);
        texture.setReflectivity(1);

        entity = new Entity(texturedModel, new Vector3f(0, -5, -13), 0,0,0,1f);
        light = new Light(new Vector3f(0, 1, -9), new Vector3f(1,1,1));
        camera = new Camera();

    }


    @Override
    public void onDrawFrame(GL10 gl) {
        //Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0.7f,0.7f, 0.7f, 1);


        entity.increaseRotation(0,1,0);
        camera.move();
        renderer.prepare();
        shaderProgram.useProgram();
        shaderProgram.loadLight(light);
        shaderProgram.loadViewMatrix(camera);
        renderer.render(entity, shaderProgram);
        shaderProgram.stop();

    }


    //This is called when the size on the screen is changed.
    //For example from portrait to landscape mode.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

    }

}
