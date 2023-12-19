package com.bradensamson.snake;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Snake {
    
    private static enum direction {
        left,
        right,
        up,
        down
    }

    public int score; // also length of snake
    public direction snakeDirection;

    public Rectangle snakeHead;
    public ArrayList<Rectangle> snakeBody;

    public long lastMoveTime;
    public int moveDistance = 32;

    public boolean alive = true;

    public Snake() {
        snakeHead = new Rectangle();
        snakeHead.width = 32;
        snakeHead.height = 32;
        snakeHead.x = 400 - snakeHead.height / 2;
        snakeHead.y = 240 - snakeHead.width / 2;

        snakeBody = new ArrayList<>();
    }

    public void getInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && snakeDirection != direction.right) {
            snakeDirection = direction.left;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && snakeDirection != direction.left) {
            snakeDirection = direction.right;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && snakeDirection != direction.down) {
            snakeDirection = direction.up;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && snakeDirection != direction.up) {
            snakeDirection = direction.down;
        }
        // pauses the game
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            snakeDirection = null;
        }
        // restarts the game
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            restart();
        }
    }

    public void moveSnake() {
        if (snakeDirection == direction.left) {
            snakeHead.x = snakeHead.x - moveDistance;
            lastMoveTime = TimeUtils.nanoTime();
        }
        if (snakeDirection == direction.right) {
            snakeHead.x = snakeHead.x + moveDistance;
            lastMoveTime = TimeUtils.nanoTime();
        }
        if (snakeDirection == direction.up) {
            snakeHead.y = snakeHead.y + moveDistance;
            lastMoveTime = TimeUtils.nanoTime();
        }
        if (snakeDirection == direction.down) {
            snakeHead.y = snakeHead.y - moveDistance;
            lastMoveTime = TimeUtils.nanoTime();
        }

        // changes to see if snakes head goes out of bounds
        if (snakeHead.x < 0) {
            alive = false;
        }
        if (snakeHead.x > 800) {
            alive = false;
        }
        if (snakeHead.y < 0) {
            alive = false;
        }
        if (snakeHead.y > 480) {
            alive = false;
        }

        // checks if pleyer hits their tail
        for (int i = 0; i < snakeBody.size(); i++) {
            if (snakeHead.overlaps(snakeBody.get(i))) {
                alive = false;
            }
        }
    }

    public void restart() {
        snakeDirection = null;
        snakeBody.removeAll(snakeBody);
        score = 0;
        alive = true;
        lastMoveTime = 0;
        snakeHead.x = 400 - snakeHead.height / 2;
        snakeHead.y = 240 - snakeHead.width / 2;
    }

    public void checkTail() {
        // adds to the tail if the snakebody size isnt the same as the score
        if (snakeDirection != null && score != 0) {
            if (snakeBody.size() != score) {
                snakeBody.add(0, new Rectangle());
                snakeBody.get(0).x = snakeHead.x;
                snakeBody.get(0).y = snakeHead.y;
                snakeBody.get(0).height = 32;
                snakeBody.get(0).width = 32;
            }

            // checks to see his the score is the same as the size, if so remove the last snake tail peice thing
            if (snakeBody.size() == score) {
                snakeBody.remove(snakeBody.size() - 1);
            }
        }
    }

    // checks if the snake eats the apple
    public void checkIfOverlapingApple(Apple apple) {
        if (snakeHead.overlaps(apple.apple)) {
            score = score + 5;
            apple.spawnApple(snakeBody);
        }
    }

    public void update(Apple apple, long gameSpeed) {

        getInput();

		if (alive) {
			if (TimeUtils.nanoTime() - lastMoveTime > 105000000) {
				checkTail();
				moveSnake();
			}

			checkIfOverlapingApple(apple);
		}
    }

    public void render(SpriteBatch batch, ShapeRenderer headrenderer, ShapeRenderer bodyRenderer, Color headColor, Color snakeBodyColor) {
        batch.begin();
            headrenderer.begin(ShapeRenderer.ShapeType.Filled);
                headrenderer.rect(snakeHead.x, snakeHead.y, snakeHead.width, snakeHead.height);
                headrenderer.setColor(headColor);
            headrenderer.end();
            bodyRenderer.begin(ShapeRenderer.ShapeType.Filled);
                for (int i = 0; i < snakeBody.size(); i++) {
                    bodyRenderer.rect(snakeBody.get(i).x, snakeBody.get(i).y, snakeBody.get(i).width, snakeBody.get(i).height);
                }
                bodyRenderer.setColor(snakeBodyColor);
            bodyRenderer.end();
        batch.end();
    }

}
