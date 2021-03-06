package com.github.hydos.ginger.engine.render.shaders;

import java.util.List;

import org.joml.Vector4f;

import com.github.hydos.ginger.engine.elements.objects.Light;
import com.github.hydos.ginger.engine.math.matrixes.Matrix4f;
import com.github.hydos.ginger.engine.math.vectors.*;

public class NormalMappingShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "normalMapVertexShader.glsl";
	private static final String FRAGMENT_FILE = "normalMapFragmentShader.glsl";
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPositionEyeSpace[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_modelTexture;
	private int location_normalMap;

	public NormalMappingShader()
	{ super(VERTEX_FILE, FRAGMENT_FILE); }

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	public void connectTextureUnits()
	{
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_normalMap, 1);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_normalMap = super.getUniformLocation("normalMap");
		location_lightPositionEyeSpace = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			location_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	public Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix)
	{
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
	}

	public void loadClipPlane(Vector4f plane)
	{ super.loadVector(location_plane, plane); }

	public void loadLights(List<Light> lights, Matrix4f viewMatrix)
	{
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			if (i < lights.size())
			{
				super.loadVector(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}
			else
			{
				super.loadVector(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadNumberOfRows(int numberOfRows)
	{ super.loadFloat(location_numberOfRows, numberOfRows); }

	public void loadOffset(float x, float y)
	{ super.load2DVector(location_offset, new Vector2f(x, y)); }

	public void loadProjectionMatrix(Matrix4f projection)
	{ super.loadMatrix(location_projectionMatrix, projection); }

	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadSkyColour(float r, float g, float b)
	{ super.loadVector(location_skyColour, new Vector3f(r, g, b)); }

	public void loadSkyColour(Vector3f colour)
	{ super.loadVector(location_skyColour, colour); }

	public void loadTransformationMatrix(Matrix4f matrix)
	{ super.loadMatrix(location_transformationMatrix, matrix); }

	public void loadViewMatrix(Matrix4f viewMatrix)
	{ super.loadMatrix(location_viewMatrix, viewMatrix); }
}
