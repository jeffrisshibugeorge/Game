package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import com.badlogic.gdx.Input.Keys;
public class MyGdxGame extends ApplicationAdapter {
	private Texture steak;
	private Texture monster;
	private Sound bite;
	private Music background;
	private Rectangle mon;
	private Array<Rectangle> stks;
	private OrthographicCamera camera;
	private long lastDropTime;


	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		steak = new Texture("steak.png");
		monster = new Texture("monster.png");


		bite = Gdx.audio.newSound(Gdx.files.internal("bite.mp3"));
		background = Gdx.audio.newMusic(Gdx.files.internal("background.wav"));

		background.setLooping(true);
		background.play();

		mon = new Rectangle();
		mon.x = 800 / 2 - 140 / 2;
		mon.y = 20;
		mon.width = 140;
		mon.height = 125;

		stks = new Array<Rectangle>();
		spawnsteakdrop();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(monster, mon.x, mon.y);
		for (Rectangle stk: stks) {
			batch.draw(steak, stk.x, stk.y);
		}
		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			mon.x = touchPos.x- 140/2;
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT)) mon.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) mon.x += 200 * Gdx.graphics.getDeltaTime();

		if(mon.x < 0) mon.x=0;
		if(mon.x > 800) mon.x = 800 - 140;
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnsteakdrop();

		Iterator<Rectangle> iter = stks.iterator();
		while(iter.hasNext()) {
			Rectangle stk = iter.next();
			stk.y -= 200 * Gdx.graphics.getDeltaTime();
			if(stk.y + 64 < 0) iter.remove();
			if(stk.overlaps(mon)) {
				bite.play();
				iter.remove();
			}
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void spawnsteakdrop() {
		Rectangle stk = new Rectangle();
		stk.x = MathUtils.random(0, 800-64);
		stk.y = 480;
		stk.width = 64;
		stk.height = 64;
		stks.add(stk);
		lastDropTime = TimeUtils.nanoTime();

	}
}
