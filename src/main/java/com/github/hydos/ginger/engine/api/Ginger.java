package com.github.hydos.ginger.engine.api;

import com.github.halotroop.litecraft.Litecraft;
import com.github.halotroop.litecraft.logic.Timer;
import com.github.halotroop.litecraft.logic.Timer.TickListener;
import com.github.halotroop.litecraft.world.World;
import com.github.hydos.ginger.engine.api.game.*;
import com.github.hydos.ginger.engine.elements.buttons.TextureButton;
import com.github.hydos.ginger.engine.font.*;
import com.github.hydos.ginger.engine.io.Window;
import com.github.hydos.ginger.engine.math.vectors.Vector2f;
import com.github.hydos.ginger.engine.particle.ParticleMaster;
import com.github.hydos.ginger.engine.postprocessing.*;
import com.github.hydos.ginger.engine.render.MasterRenderer;
import com.github.hydos.ginger.engine.render.tools.MousePicker;
import com.github.hydos.ginger.engine.screen.Screen;
import com.github.hydos.ginger.engine.utils.Loader;

public class Ginger
{
	private static Ginger INSTANCE;
	
	public GingerRegister gingerRegister;
	public MousePicker picker;
	public FontType globalFont;
	public Fbo contrastFbo;
	Timer timer;
	TickListener gameTickListener = new TickListener()
	{
		@Override
		public void onTick(float deltaTime)
		{ 
			gingerRegister.game.tick(); 
			if(gingerRegister.currentScreen != null) {
				gingerRegister.currentScreen.tick();
			}
		};
	};

	public void cleanup()
	{
		Window.stop();
		PostProcessing.cleanUp();
		ParticleMaster.cleanUp();
		gingerRegister.masterRenderer.cleanUp();
		TextMaster.cleanUp();
		Loader.cleanUp();
	}
	
	public void openScreen(Screen screen)
	{
		gingerRegister.currentScreen = screen;
	}

	public void postRender()
	{ Window.swapBuffers(); }

	public TextureButton registerButton(String resourceLocation, Vector2f position, Vector2f scale)
	{
		TextureButton button = new TextureButton(resourceLocation, position, scale);
		gingerRegister.registerButton(button);
		return button;
	}

	public GUIText registerText(String string, int textSize, Vector2f position, float maxLineLength, boolean centered, String id)
	{
		GUIText text = new GUIText(string, textSize, globalFont, position, maxLineLength, false);
		text.textID = id;
		gingerRegister.registerText(text);
		return text;
	}

	public void renderOverlays(Game game)
	{
		gingerRegister.masterRenderer.renderGuis(game.data.guis);
		if(gingerRegister.currentScreen != null) {
			gingerRegister.masterRenderer.renderGuis(gingerRegister.currentScreen.elements);
		}
		TextMaster.render();
	}

	public void renderWorld(Game game, World world)
	{
		GingerUtils.preRenderScene(gingerRegister.masterRenderer);
		contrastFbo.bindFBO();
		gingerRegister.masterRenderer.renderSceneNoTerrain(game.data.entities, game.data.normalMapEntities, game.data.lights, game.data.camera, game.data.clippingPlane, world);
		ParticleMaster.renderParticles(game.data.camera);
		contrastFbo.unbindFBO();
		PostProcessing.doPostProcessing(contrastFbo.colorTexture);
		if (game.data.handleGuis)
		{ renderOverlays(game); }

	}

	public void setGlobalFont(FontType font)
	{ this.globalFont = font; }

	public void setup(MasterRenderer masterRenderer, Game game)
	{
		INSTANCE = this;
		gingerRegister = new GingerRegister();
		gingerRegister.registerGame(game);
		timer = new Timer(game.data.tickSpeed);
		timer.addTickListener(gameTickListener);
		contrastFbo = new Fbo(new ContrastChanger());
		gingerRegister.masterRenderer = masterRenderer;
		picker = new MousePicker(game.data.camera, masterRenderer.getProjectionMatrix(), null);
		PostProcessing.init();
		ParticleMaster.init(masterRenderer.getProjectionMatrix());
	}

	public void startGame()
	{
		while (!Window.closed())
		{
			Litecraft.getInstance().ups++;
			if (Window.isUpdating())
			{
				timer.tick();
				gingerRegister.game.render();
			}
		}
		Litecraft.getInstance().exit();
	}

	public void update(GameData data)
	{
		Window.update();
		data.camera.move();
		GingerUtils.update();
		picker.update();
		ParticleMaster.update(data.camera);
	}

	public static Ginger getInstance() {
		return INSTANCE;
	}
}