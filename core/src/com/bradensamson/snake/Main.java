package com.bradensamson.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Main extends ApplicationAdapter {

	private SpriteBatch batch;

	private Snake snake;
	private Apple apple;

	private ShapeRenderer snakeRenderer;
	private ShapeRenderer appleRenderer;

	private OrthographicCamera camera;


	@Override
	public void create () {

		batch = new SpriteBatch();

		snakeRenderer = new ShapeRenderer();
		appleRenderer = new ShapeRenderer();

		snake = new Snake();
		apple = new Apple();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

	}

	@Override
	public void render () {
		// snake logic
		if (snake.alive) {
			snake.getInput();

			if (TimeUtils.nanoTime() - snake.lastMoveTime > 105000000) {
				snake.checkTail();
				snake.moveSnake();
			}

			snake.checkIfOverlapingApple(apple);
		}

		// restart if r is pressed
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            snake.restart();
        }

		// sets the screen to be black
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// renders the apple in red
		appleRenderer.begin(ShapeType.Filled);
		appleRenderer.rect(apple.apple.x, apple.apple.y, apple.apple.width, apple.apple.height);
		appleRenderer.setColor(Color.RED);
		appleRenderer.end();

		// renders the snake and the snake tail in green
		snakeRenderer.begin(ShapeType.Filled);
		snakeRenderer.rect(snake.snakeHead.x, snake.snakeHead.y, snake.snakeHead.width, snake.snakeHead.height);
		for (int i = 0; i < snake.snakeBody.size(); i++) {
			snakeRenderer.rect(snake.snakeBody.get(i).x, snake.snakeBody.get(i).y, snake.snakeBody.get(i).width, snake.snakeBody.get(i).height);
		}
		snakeRenderer.setColor(Color.GREEN);
		snakeRenderer.end();

		batch.end();

	}
	
	@Override
	public void dispose () {
		appleRenderer.dispose();
		snakeRenderer.dispose();
		batch.dispose();
	}
}
