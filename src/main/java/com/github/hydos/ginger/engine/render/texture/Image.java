package com.github.hydos.ginger.engine.render.texture;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.system.MemoryStack;

import com.github.hydos.ginger.engine.render.tools.IOUtil;

public class Image
{
	public static Image createImage(String imagePath)
	{
		ByteBuffer img;
		ByteBuffer imageBuffer = null;
		try
		{
			imageBuffer = IOUtil.ioResourceToByteBuffer(imagePath, 8 * 1024);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try (MemoryStack stack = stackPush())
		{
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			if (!stbi_info_from_memory(imageBuffer, w, h, comp))
			{ throw new RuntimeException("Failed to read image information: " + stbi_failure_reason()); }
			img = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
			if (img == null)
			{ throw new RuntimeException("Failed to load image: " + stbi_failure_reason()); }
			return new Image(w.get(0), h.get(0), img, comp);
		}
	}

	private ByteBuffer image;

	private int width, height;
	
	private IntBuffer comp;

	Image(int width, int heigh, ByteBuffer image, IntBuffer comp)
	{
		this.image = image;
		this.height = heigh;
		this.width = width;
		this.comp = comp;
	}

	public Image(String imagePath)
	{
		ByteBuffer img;
		ByteBuffer imageBuffer;
		try
		{
			imageBuffer = IOUtil.ioResourceToByteBuffer(imagePath, 8 * 1024);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		try (MemoryStack stack = stackPush())
		{
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			// Use info to read image metadata without decoding the entire image.
			// We don't need this for this demo, just testing the API.
			if (!stbi_info_from_memory(imageBuffer, w, h, comp))
			{ throw new RuntimeException("Failed to read image information: " + stbi_failure_reason()); }
			// Decode the image
			img = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
			if (img == null)
			{ throw new RuntimeException("Failed to load image: " + stbi_failure_reason()); }
			this.image = img;
			this.width = w.get(0);
			this.height = h.get(0);
			this.comp = comp;
		}
	}

	public int getHeight()
	{ return height; }
	public ByteBuffer getImage()
	{ return image; }

	public int getWidth()
	{ return width; }

	public IntBuffer getComp() {
		return comp;
	}
}