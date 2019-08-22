package com.oscarcreator.opengl_game.shaders;



import android.content.Context;

import com.oscarcreator.opengl_game.R;
import com.oscarcreator.opengl_game.entities.Camera;
import com.oscarcreator.opengl_game.library.Matrix4f;
import com.oscarcreator.opengl_game.toolbox.Maths;

import static com.oscarcreator.opengl_game.util.Constants.POSITION_VBO_LOCATION;

public class StaticShader extends ShaderProgram{

	private static final int VERTEX_SHADER_ID = R.raw.vertex_shader;

	private static final int FRAGMENT_SHADER_ID = R.raw.fragment_shader;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public StaticShader(Context context) {
		super(context, VERTEX_SHADER_ID, FRAGMENT_SHADER_ID);
	}


	@Override
	protected void bindAttributes() {
		super.bindAttribute(POSITION_VBO_LOCATION, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix =
				super.getUniformfromLocation("transformationMatrix");
		location_projectionMatrix =
				super.getUniformfromLocation("projectionMatrix");
		location_viewMatrix =
				super.getUniformfromLocation("viewMatrix");
	}

	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}


	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.creatViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
