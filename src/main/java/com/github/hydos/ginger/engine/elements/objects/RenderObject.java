package com.github.hydos.ginger.engine.elements.objects;

import com.github.hydos.ginger.engine.math.vectors.Vector3f;
import com.github.hydos.ginger.engine.render.models.TexturedModel;

public class RenderObject
{
	private TexturedModel model;
	public Vector3f position;
	private float rotX = 0, rotY = 0, rotZ = 0;
	private Vector3f scale;
	public boolean isVisible = true;

	public RenderObject(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale)
	{
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public void x(float x) {
		this.position.x = x;
	}
	
	public void y(float y) {
		this.position.y = y;
	}
	
	public void z(float z) {
		this.position.z = z;
	}

	public TexturedModel getModel()
	{ return model; }

	public Vector3f getPosition()
	{ return position; }

	public float getRotX()
	{ return rotX; }

	public float getRotY()
	{ return rotY; }

	public float getRotZ()
	{ return rotZ; }

	public Vector3f getScale()
	{ return scale; }

	public void increasePosition(float dx, float dy, float dz)
	{
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz)
	{
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public void setModel(TexturedModel model)
	{ this.model = model; }

	public void setPosition(Vector3f position)
	{ this.position = position; }

	public void setRotX(float rotX)
	{ this.rotX = rotX; }

	public void setRotY(float rotY)
	{ this.rotY = rotY; }

	public void setRotZ(float rotZ)
	{ this.rotZ = rotZ; }

	public void setScale(Vector3f scale)
	{ this.scale = scale; }
}
