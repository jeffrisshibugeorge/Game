package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;

public class RoadRash extends Game {
	public final static int WIDTH = 2000;
	public final static int HEIGHT = 1000;
	private GameScreen gameScreen;

	
	@Override
	public void create () {
		Assets.load();
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}


	
	@Override
	public void dispose () {
		Assets.dispose();
		gameScreen.dispose();
	}
}
