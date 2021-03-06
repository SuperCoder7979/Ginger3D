package com.github.hydos.ginger.engine.render.renderers;

import java.util.List;

import org.lwjgl.opengl.*;

import com.github.hydos.ginger.engine.elements.GuiTexture;
import com.github.hydos.ginger.engine.math.Maths;
import com.github.hydos.ginger.engine.math.matrixes.Matrix4f;
import com.github.hydos.ginger.engine.render.Renderer;
import com.github.hydos.ginger.engine.render.models.RawModel;
import com.github.hydos.ginger.engine.render.shaders.GuiShader;
import com.github.hydos.ginger.engine.utils.Loader;

public class GuiRenderer extends Renderer
{
	private final RawModel quad;
	private GuiShader shader;

	public GuiRenderer(GuiShader shader)
	{
		this.shader = shader;
		float[] positions =
		{
			-1, 1, -1, -1, 1, 1, 1, -1
		};
		quad = Loader.loadToVAO(positions, 2);
	}

	public void cleanUp()
	{ shader.cleanUp(); }

	public void render(List<GuiTexture> guis)
	{
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (GuiTexture gui : guis)
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
}
