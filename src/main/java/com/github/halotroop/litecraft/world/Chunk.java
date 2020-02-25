package com.github.halotroop.litecraft.world;

import java.util.*;

import com.github.halotroop.litecraft.types.block.*;
import com.github.hydos.ginger.engine.elements.objects.RenderObject;
import com.github.hydos.ginger.engine.math.vectors.Vector3f;
import com.github.hydos.ginger.engine.render.renderers.ObjectRenderer;

import it.unimi.dsi.fastutil.longs.*;

public class Chunk implements TileAccess
{
	/** @param x in-chunk x coordinate.
	 * @param  y in-chunk y coordinate.
	 * @param  z in-chunk z coordinate.
	 * @return   creates a long that represents a coordinate, for use as a key in maps. */
	private static long posHash(int x, int y, int z)
	{ return ((long) x & 0b111) | (((long) y & 0b111) << 3) | (((long) z & 0b111) << 6); }
	
	List<BlockEntity> renderList;
	private final Long2ObjectMap<Block> blocks = new Long2ObjectArrayMap<>();
	private final Long2ObjectMap<BlockEntity> blockEntities = new Long2ObjectArrayMap<>();
	private boolean render = false;
	public final int chunkX, chunkY, chunkZ;
	private final int chunkStartX, chunkStartY, chunkStartZ;

	public Chunk(int chunkX, int chunkY, int chunkZ)
	{
		renderList = new ArrayList<BlockEntity>();
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		this.chunkStartX = chunkX << 3;
		this.chunkStartY = chunkY << 3;
		this.chunkStartZ = chunkZ << 3;
	}

	public boolean doRender()
	{ return this.render; }

	@Override
	public Block getBlock(int x, int y, int z)
	{
		long hash = posHash(x, y, z);
		return this.blocks.get(hash);
	}

	public BlockEntity getBlockEntity(int x, int y, int z)
	{
		long hash = posHash(x, y, z);
		return this.blockEntities.get(hash);
	}

	public void render(ObjectRenderer renderer)
	{
		renderList.clear();
		if (render)
		{
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					for(int k = 0; k < 8; k++) {
						BlockEntity block = getBlockEntity(i, j, k);
						renderList.add(block);
					}
				}
			}
			
			renderer.prepare();
			renderer.render(renderList);
		}
	}

	@Override
	public void setBlock(int x, int y, int z, Block block)
	{
		if (x > 7)
			x = 7;
		else if (x < 0) x = 0;
		if (y > 7)
			y = 7;
		else if (y < 0) y = 0;
		if (z > 7)
			z = 7;
		else if (z < 0) z = 0;
		long hash = posHash(x, y, z);
		this.blocks.put(hash, block);
		if (this.render)
		{
			// TODO remove current block entity from game data when this class is integrated with the game
			this.blockEntities.put(hash, new BlockEntity(block, new Vector3f(this.chunkStartX + x, this.chunkStartY + y, this.chunkStartZ + z)));
		}
	}

	public static Chunk generateChunk(int chunkX, int chunkY, int chunkZ) {
		Chunk result = new Chunk(chunkX, chunkY, chunkZ);

		for (int x = 0; x < 8; ++x) {
			for (int z = 0; z < 8; ++z) {
				for (int y = 0; y < 8; ++y) {
					result.setBlock(x, y, z, y == 7 ? Block.GRASS : Block.DIRT);
				}
			}
		}

		return result;
	}

	public void setRender(boolean render)
	{
		if (render && !this.render) // if it has been changed to true
		{
			for (int x = 0; x < 8; ++x)
			{
				for (int y = 0; y < 8; ++y)
				{
					for (int z = 0; z < 8; ++z)
					{
						long hash = posHash(x, y, z);
						Block block = this.blocks.get(hash);
						if (block.visible) this.blockEntities.put(hash, new BlockEntity(block,
							new Vector3f(
								this.chunkStartX + x,
								this.chunkStartY + y,
								this.chunkStartZ + z)));
					}
				}
			}
		}
		else if (this.render) // else if it has been changed to false
		{
			int length = blockEntities.size();
			for (int i = length; i >= 0; --i)
			{ this.blockEntities.remove(i); }
		}
		this.render = render;
	}
}
