package com.bradensamson.snake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

	private BitmapFont scoreFont;

	private int highScore;
	private String score;

	private boolean isHighScoreWrittenToFile = false;

	private OrthographicCamera camera;

	@Override
	public void create () {
		// high score file creation and reading
		try {
			File highScoreFile = new File("highscore.snuf");
			if (!highScoreFile.createNewFile()) {
				try {
					Scanner highScoreScanner = new Scanner(highScoreFile);
					if (highScoreScanner.hasNext()) {
						highScore = highScoreScanner.nextInt();
					}
					highScoreScanner.close();
				} catch (FileNotFoundException e) {
					System.out.println("Error reading high score file");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println("Error with high score file");
			e.printStackTrace();
		}

		snake = new Snake();
		apple = new Apple();

		batch = new SpriteBatch();

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);

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

		// high score writing
		if (!snake.alive && !isHighScoreWrittenToFile) {
			try {
				FileWriter scoreWriter = new FileWriter("highscore.snuf");
				scoreWriter.write(Integer.toString(highScore));
				scoreWriter.close();
				isHighScoreWrittenToFile = true;
			} catch (IOException e) {
				System.out.println("Error with writing high score to file");
				e.printStackTrace();
			}
		}

		// restarts the game
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            snake.restart();
			isHighScoreWrittenToFile = false;
        }
		
		// pauses the game
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			snake.snakeDirection = null;
		}
		
		// sets the screen to be black
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		// renders the apple
		apple.render(batch, appleRenderer, appleColor);

		// renders the snake and the snake tail
		snake.render(batch, snakeHeadRenderer, snakeBodyRenderer, snakeHeadColor, snakeBodyColor);

		score = Integer.toString(snake.score);

		batch.begin();
			scoreFont.draw(batch, "High Score: " + highScore, 693, 475);
			scoreFont.draw(batch, "Score: " + score, 725, 460);
		batch.end();
	}
	
	@Override
	public void dispose () {
		appleRenderer.dispose();
		snakeHeadRenderer.dispose();
		snakeBodyRenderer.dispose();
		scoreFont.dispose();
		batch.dispose();
	}
}
