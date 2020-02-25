package com.github.hydos.ginger.engine.postprocessing;

import org.lwjgl.opengl.GL11;

public class ImageRenderer
{
	private Fbo fbo;

	protected ImageRenderer()
	{}

	protected ImageRenderer(int width, int height)
	{ this.fbo = new Fbo(new ContrastChanger()); }

	protected void cleanUp()
	{}

	protected int getOutputTexture()
	{ return fbo.colorTexture; }

	protected void renderQuad()
	{
		if (fbo != null)
		{ fbo.bindFBO(); }
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null)
		{ fbo.unbindFBO(); }
	}
}
