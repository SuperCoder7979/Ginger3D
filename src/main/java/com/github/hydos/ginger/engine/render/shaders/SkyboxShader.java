package com.github.hydos.ginger.engine.render.shaders;

import com.github.hydos.ginger.engine.cameras.Camera;
import com.github.hydos.ginger.engine.math.Maths;
import com.github.hydos.ginger.engine.math.matrixes.Matrix4f;

public class SkyboxShader extends ShaderProgram
{
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public SkyboxShader()
	{ super("skyboxVertexShader.glsl", "skyboxFragmentShader.glsl"); }

	public void loadProjectionMatrix(Matrix4f matrix)
	{ super.loadMatrix(location_projectionMatrix, matrix); }

	public void loadViewMatrix(Camera camera)
	{
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		super.loadMatrix(location_viewMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes()
	{ super.bindAttribute(0, "position"); }
}