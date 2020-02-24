package com.github.halotroop.litecraft.type.block;

import org.joml.Vector3f;

import com.github.hydos.ginger.engine.elements.objects.RenderObject;
import com.github.hydos.ginger.engine.render.models.TexturedModel;

public class Block extends RenderObject
{
	public Block(TexturedModel blockModel, Vector3f position)
	{ super(blockModel, position, 0, 0, 0, new Vector3f(1f, 1f, 1f)); }
}