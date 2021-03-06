package com.github.hydos.ginger.engine.cameras;

import org.lwjgl.glfw.*;

import com.github.hydos.ginger.engine.elements.objects.Player;
import com.github.hydos.ginger.engine.io.Window;
import com.github.hydos.ginger.engine.math.vectors.Vector3f;

public class Camera
{
	private float distanceFromPlayer = 5;
	private float angleAroundPlayer = 0;
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch, yaw;
	private float roll;
	public Player player;

	public Camera(Player player)
	{ this.player = player; }

	public Camera(Vector3f vector3f, Player player)
	{
		this.position = vector3f;
		this.player = player;
	}

	private void calculateAngleAroundPlayer()
	{
		if (Window.isMouseDown(1))
		{
			float angleChange = (float) (Window.dx * 0.3f);
			angleAroundPlayer -= angleChange;
		}
	}

	private void calculateCameraPosition(float horizDistance, float verticDistance)
	{
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;
	}

	private float calculateHorizontalDistance()
	{
		float hD = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		if (hD < 0)
			hD = 0;
		return hD;
	}

	private void calculatePitch()
	{
		if (Window.isMouseDown(1))
		{
			float pitchChange = (float) (Window.dy * 0.2f);
			pitch += pitchChange;
			if (pitch < 0)
			{
				pitch = 0;
			}
			else if (pitch > 90)
			{ pitch = 90; }
		}
	}

	private float calculateVerticalDistance()
	{ return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch + 4))); }

	private void calculateZoom()
	{
		GLFW.glfwSetScrollCallback(Window.window, new GLFWScrollCallback()
		{
			@Override
			public void invoke(long win, double dx, double dy)
			{
				float zoomLevel = (float) dy * 0.1f;
				distanceFromPlayer -= zoomLevel;
			}
		});
	}

	public float getPitch()
	{ return pitch; }

	public Vector3f getPosition()
	{ return position; }

	public float getRoll()
	{ return roll; }

	public float getYaw()
	{ return yaw; }

	public void invertPitch()
	{ this.pitch = -pitch; }

	public void move()
	{
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
}
