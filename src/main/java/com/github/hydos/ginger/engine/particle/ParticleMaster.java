package com.github.hydos.ginger.engine.particle;

import java.util.*;
import java.util.Map.Entry;

import com.github.hydos.ginger.engine.cameras.Camera;
import com.github.hydos.ginger.engine.math.matrixes.Matrix4f;
import com.github.hydos.ginger.engine.render.renderers.ParticleRenderer;

public class ParticleMaster
{
	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer particleRenderer;

	public static void addParticle(Particle particle)
	{
		List<Particle> list = particles.get(particle.getTexture());
		if (list == null)
		{
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}

	public static void cleanUp()
	{ particleRenderer.cleanUp(); }

	public static void init(Matrix4f projectionMatrix)
	{ particleRenderer = new ParticleRenderer(projectionMatrix); }

	public static void renderParticles(Camera camera)
	{ particleRenderer.render(particles, camera); }

	public static void update(Camera camera)
	{
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext())
		{
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext())
			{
				Particle p = iterator.next();
				boolean stillAlive = p.update(camera);
				if (!stillAlive)
				{
					iterator.remove();
					if (list.isEmpty())
					{ mapIterator.remove(); }
				}
			}
			InsertionSort.sortHighToLow(list);
		}
	}
}
