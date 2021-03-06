package com.github.hydos.ginger.engine.particle;

import com.github.hydos.ginger.engine.cameras.Camera;
import com.github.hydos.ginger.engine.io.Window;
import com.github.hydos.ginger.engine.math.vectors.*;
import com.github.hydos.ginger.main.settings.Constants;

public class Particle
{
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private Vector3f scale;
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	private ParticleTexture texture;
	private float elapsedTime = 0;
	private float distance;

	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
		Vector3f scale)
	{
		super();
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.texture = texture;
		ParticleMaster.addParticle(this);
	}

	public float getBlend()
	{ return blend; }

	public float getDistance()
	{ return distance; }

	public Vector3f getPosition()
	{ return position; }

	public float getRotation()
	{ return rotation; }

	public Vector3f getScale()
	{ return scale; }

	public Vector2f getTexOffset1()
	{ return texOffset1; }

	public Vector2f getTexOffset2()
	{ return texOffset2; }

	public ParticleTexture getTexture()
	{ return texture; }

	private void setTextureOffset(Vector2f offset, int index)
	{
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}

	public boolean update(Camera camera)
	{
		float time = (float) Window.getTime() / 1000000;
		velocity.y += Constants.gravity.y() * gravityEffect * time;
		Vector3f change = new Vector3f(velocity);
		change.scale(time);
		Vector3f.add(change, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		elapsedTime += time;
		updateTextureCoordInfo();
		return elapsedTime < lifeLength;
	}

	private void updateTextureCoordInfo()
	{
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}
}
