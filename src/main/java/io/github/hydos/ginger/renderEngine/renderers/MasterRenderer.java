package io.github.hydos.ginger.renderEngine.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import io.github.hydos.ginger.elements.Entity;
import io.github.hydos.ginger.elements.Light;
import io.github.hydos.ginger.elements.ThirdPersonCamera;
import io.github.hydos.ginger.guis.GuiTexture;
import io.github.hydos.ginger.io.Window;
import io.github.hydos.ginger.mathEngine.matrixes.Matrix4f;
import io.github.hydos.ginger.renderEngine.models.TexturedModel;
import io.github.hydos.ginger.renderEngine.shaders.GuiShader;
import io.github.hydos.ginger.renderEngine.shaders.StaticShader;
import io.github.hydos.ginger.renderEngine.shaders.TerrainShader;
import io.github.hydos.ginger.terrain.Terrain;

public class MasterRenderer {
	
	private StaticShader entityShader;
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	
	private GuiShader guiShader;
	private GuiRenderer guiRenderer;
	
	private SkyboxRenderer skyboxRenderer;
	
	private Matrix4f projectionMatrix;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	public MasterRenderer() {
		createProjectionMatrix();
		entityShader = new StaticShader();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		
		skyboxRenderer = new SkyboxRenderer(projectionMatrix);
		
		guiShader = new GuiShader();
		guiRenderer = new GuiRenderer(guiShader);
		
		terrainShader = new TerrainShader();
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);

	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, ThirdPersonCamera camera) {
		renderEntities(entities, camera, lights);
		renderTerrains(terrains, lights, camera);
		renderEntities(entities, camera, lights);
		renderTerrains(terrains, lights, camera);
		skyboxRenderer.render(camera);
		
	}
	
	public void renderGuis(List<GuiTexture> guis) {
		guiRenderer.render(guis);
	}
	
	private void renderTerrains(List<Terrain> terrains, List<Light> lights, ThirdPersonCamera camera) {
		terrainShader.start();
		terrainShader.loadSkyColour(Window.getColour());
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();		
	}

	private void renderEntities(List<Entity> entities, ThirdPersonCamera camera, List<Light> lights) {
		for(Entity entity: entities) {
			processEntity(entity);
		}
		entityRenderer.prepare();
		entityShader.start();
		entityShader.loadSkyColour(Window.getColour());
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(this.entities);		
		entityShader.stop();
		this.entities.clear();
	}
	
	private void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp() {
		entityShader.cleanUp();
		terrainShader.cleanUp();
		guiRenderer.cleanUp();
		
	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Window.width / Window.height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_Scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_Scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2* NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		
	}
	
}
