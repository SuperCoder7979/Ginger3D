package com.github.hydos.ginger.engine.api.game;

public abstract class Game
{
	public GameData data;

	public Game()
	{}

	public abstract void exit();

	public abstract void render();

	public abstract void tick();
}