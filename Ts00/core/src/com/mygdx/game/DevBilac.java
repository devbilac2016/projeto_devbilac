package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screens.CriarConta;
import Screens.ScreenUs04;

public class DevBilac extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	public static final float PPM2 = 100;
	public SpriteBatch batch;
	//Box2D Collision Bits
	public static final short DEFAULT_BIT = 0;
	public static final short ESCADA_BIT = 1;
	public static final short DEVB_BIT = 2;
	public static final short CHEST_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short PASS_BIT = 16;
	/*ATENCAO Usar AssetManager em Static, pois pode trazer problemas.*/
	public static AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		//setScreen(new PlayScreen(this));
		//setScreen(new Ranking(this));
		//setScreen(new ScreenUs03(this));
		//setScreen(new ScreenUs07(this));
		//setScreen(new ScreenUs05(this));
		setScreen(new ScreenUs04(this));
		//setScreen(new CriarConta());
		//setScreen(new MainMenu());
		//setScreen(new MenuScreen());
		manager = new AssetManager();
		manager.load("audio/music/Fluxogame.ogg", Music.class);
		//manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/chest_open.wav", Sound.class);
		manager.load("audio/sounds/door_open.wav", Sound.class);
		manager.finishLoading();
		
	}

	@Override
	public void render () {
		super.render();
		//if(manager.update())
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}

}
