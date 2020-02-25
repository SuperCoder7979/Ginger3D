package com.github.hydos.ginger.engine.elements.objects;

import org.lwjgl.glfw.GLFW;

import com.github.hydos.ginger.engine.io.Window;
import com.github.hydos.ginger.engine.math.vectors.Vector3f;
import com.github.hydos.ginger.engine.render.models.TexturedModel;
import com.github.hydos.ginger.engine.terrain.Terrain;
import com.github.hydos.ginger.main.settings.Constants;

public class Player extends RenderObject
{
	private static float terrainHeight = 0;
	private float currentSpeed = 0;
	private float currentTurn = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale)
	{ super(model, position, rotX, rotY, rotZ, scale); }

	private void checkInputs()
	{
		if (Window.isKeyDown(GLFW.GLFW_KEY_W))
		{
			this.currentSpeed = Constants.movementSpeed;
		}
		else if (Window.isKeyPressed(GLFW.GLFW_KEY_S))
		{
			this.currentSpeed = -Constants.movementSpeed;
		}
		else
		{
			this.currentSpeed = 0;
		}
		if (Window.isKeyDown(GLFW.GLFW_KEY_A))
		{
			this.currentTurn = Constants.turnSpeed;
		}
		else if (Window.isKeyDown(GLFW.GLFW_KEY_D))
		{ this.currentTurn = -Constants.turnSpeed; }
		if (!Window.isKeyDown(68) || !Window.isKeyDown(65))
		{ this.currentTurn = 0; }
		if (Window.isKeyDown(GLFW.GLFW_KEY_SPACE))
		{
			jump();
		}
		else
		{
		}
	}

	private void jump()
	{
		if (!isInAir)
		{
			isInAir = true;
			this.upwardsSpeed = Constants.jumpPower;
		}
	}

	public void move(Terrain t)
	{
		checkInputs();
		super.increaseRotation(0, (float) ((currentTurn) * Window.getTime()), 0);
		float distance = (float) ((currentSpeed) * (Window.getTime()));
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		if (t != null)
		{ terrainHeight = t.getHeightOfTerrain(super.getPosition().x, super.getPosition().z); }
		super.increasePosition(0, (float) (upwardsSpeed * (Window.getTime())), 0);
		upwardsSpeed += Constants.gravity * Window.getTime();
		if (super.getPosition().y < terrainHeight)
		{
			isInAir = false;
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
		}
	}
}
