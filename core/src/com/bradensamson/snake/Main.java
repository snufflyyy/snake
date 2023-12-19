package com.bradensamson.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

	private Snake snake;
	private Apple apple;

	private static final Color snakeHeadColor = new Color(0, 0, 1, 1); // blue
	private static final Color snakeBodyColor = new Color(0, 0, 0.75f, 1); // dark blue
	private static final Color appleColor = new Color(1, 0, 0, 1); // red

	private static final long gameSpeed = 105000000;

	private SpriteBatch batch;

	private ShapeRenderer snakeHeadRenderer;
	private ShapeRenderer snakeBodyRenderer;
	private ShapeRenderer appleRenderer;

	private BitmapFont socreFont;

	private int highScore;
	private String score;

	private OrthographicCamera camera;

	@Override
	public void create () {
		snake = new Snake();
		apple = new Apple();

		batch = new SpriteBatch();

		socreFont = new BitmapFont();
		socreFont.setColor(Color.WHITE);

		snakeHeadRenderer = new ShapeRenderer();
		snakeBodyRenderer = new ShapeRenderer();
		appleRenderer = new ShapeRenderer();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render () {
		// updates the snake
		snake.update(apple, gameSpeed);

		// high score checker
		if (snake.score > highScore) {
			highScore = snake.score;
		}
		
		// sets the screen to be black
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		// renders the apple
		apple.render(batch, appleRenderer, appleColor);

		// renders the snake and the snake tail
		snake.render(batch, snakeHeadRenderer, snakeBodyRenderer, snakeHeadColor, snakeBodyColor);

		batch.begin();
			score = Integer.toString(snake.score);
			socreFont.draw(batch, "High Score: " + highScore, 693, 475);
			socreFont.draw(batch, "Score: " + score, 725, 460);
		batch.end();
	}
	
	@Override
	public void dispose () {
		appleRenderer.dispose();
		snakeHeadRenderer.dispose();
		snakeBodyRenderer.dispose();
		socreFont.dispose();
		batch.dispose();
	}
}
