package com.oscarcreator.opengl_game.shaders;



import android.content.Context;

import com.oscarcreator.opengl_game.R;
import com.oscarcreator.opengl_game.entities.Camera;
import com.oscarcreator.opengl_game.entities.Light;
import com.oscarcreator.opengl_game.library.Matrix4f;
import com.oscarcreator.opengl_game.toolbox.Maths;

import static com.oscarcreator.opengl_game.util.Constants.NORMALS_VBO_LOCATION;
import static com.oscarcreator.opengl_game.util.Constants.POSITION_VBO_LOCATION;
import static com.oscarcreator.opengl_game.util.Constants.TEXTURE_VBO_LOCATION;

public class StaticShader extends ShaderProgram{

	private static final int VERTEX_SHADER_ID = R.raw.vertex_shader;

	private static final int FRAGMENT_SHADER_ID = R.raw.fragment_shader;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;

	public StaticShader(Context context) {
		super(context, VERTEX_SHADER_ID, FRAGMENT_SHADER_ID);
	}


	@Override
	protected void bindAttributes() {
		super.bindAttribute(POSITION_VBO_LOCATION, "position");
		super.bindAttribute(TEXTURE_VBO_LOCATION, "textureCoords");
		super.bindAttribute(NORMALS_VBO_LOCATION, "normal");

	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix =
				super.getUniformLocation("transformationMatrix");
		location_projectionMatrix =
				super.getUniformLocation("projectionMatrix");
		location_viewMatrix =
				super.getUniformLocation("viewMatrix");

		location_lightPosition =
				super.getUniformLocation("lightPosition");
		location_lightColour =
				super.getUniformLocation("lightColour");
		location_shineDamper =
				super.getUniformLocation("shineDamper");
		location_reflectivity =
				super.getUniformLocation("reflectivity");
	}

	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}


	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.creatViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
