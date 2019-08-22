package com.oscarcreator.opengl_game.renderEngine;


import android.content.Context;

import com.oscarcreator.opengl_game.entities.Entity;
import com.oscarcreator.opengl_game.library.Matrix4f;
import com.oscarcreator.opengl_game.models.RawModel;
import com.oscarcreator.opengl_game.models.TexturedModel;
import com.oscarcreator.opengl_game.shaders.StaticShader;
import com.oscarcreator.opengl_game.textures.ModelTexture;
import com.oscarcreator.opengl_game.toolbox.Maths;
import com.oscarcreator.opengl_game.util.Constants;

import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import static com.oscarcreator.opengl_game.util.Constants.*;

public class Renderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;


	private Matrix4f projectionMatrix;

	private int width, height;

	public Renderer(int width, int height, StaticShader shader){
		this.width = width;
		this.height = height;

		createProjectionMatrix();
		shader.useProgram();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void prepare(){
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(1,0, 0, 1);
	}

	public void render(Entity entity, StaticShader shader){

		TexturedModel texturedModel = entity.getModel();

		RawModel model = texturedModel.getRawModel();

		//Bind vertex array
		glBindVertexArray(model.getVaoID());

		//Enabling attribarray at the position 0 which we put the model into
		glEnableVertexAttribArray(POSITION_VBO_LOCATION);
		//enable and bind textures
		glEnableVertexAttribArray(TEXTURE_VBO_LOCATION);
		//enable attribute array 2 for normals
		glEnableVertexAttribArray(NORMALS_VBO_LOCATION);


		Matrix4f transformationMatrix = Maths.creatTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);

		ModelTexture texture = texturedModel.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getID());

		//Draw the triangles
		glDrawElements(GL_TRIANGLES, model.getVertexCount(),
				GL_UNSIGNED_INT, 0);
		//Disable the vertex attrib arrays
		glDisableVertexAttribArray(POSITION_VBO_LOCATION);
		glDisableVertexAttribArray(TEXTURE_VBO_LOCATION);
		glDisableVertexAttribArray(NORMALS_VBO_LOCATION);

		//Unbind vertex array
		glBindVertexArray(0);
	}


	private void createProjectionMatrix(){
		float aspectRatio = (float) width / (float) height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;

	}

}
