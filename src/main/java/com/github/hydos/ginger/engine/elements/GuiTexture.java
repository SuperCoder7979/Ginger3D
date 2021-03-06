package com.github.hydos.ginger.engine.elements;

import com.github.hydos.ginger.engine.math.vectors.Vector2f;

public class GuiTexture
{
	private int texture;
	private Vector2f position, scale;

	public GuiTexture(int texture, Vector2f position, Vector2f scale)
	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	public Vector2f getPosition()
	{ return position; }

	public Vector2f getScale()
	{ return scale; }

	public int getTexture()
	{ return texture; }

	public void setPosition(Vector2f position)
	{ this.position = position; }

	public void setScale(Vector2f scale)
	{ this.scale = scale; }

	public void setTexture(int texture)
	{ this.texture = texture; }
}
