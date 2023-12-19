package com.bradensamson.snake;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Apple {

    public Rectangle apple;

    private float x, y;

    public Apple() {

        apple = new Rectangle();

        getRandomCoords();

        apple.x = x;
        apple.y = y;
        apple.width = 32;
        apple.height = 32;
    }
    
    private void getRandomCoords() {
        float tempX = (int) (Math.random() * 24);
        float tempY = (int) (Math.random() * 14);

        x = 32 * tempX;
        y = 32 * tempY;
    }

    public void spawnApple(ArrayList<Rectangle> snakeBody) {
        getRandomCoords();
        // checks the see if the snakes body is where x and y are
        for (int i = 0; i < snakeBody.size(); i++) {
            if (snakeBody.get(i).x == x && snakeBody.get(i).y == y) {
                getRandomCoords();
            }
        }

        apple.x = x;
        apple.y = y;
    }

    public void render(SpriteBatch batch, ShapeRenderer renderer, Color color) {
        batch.begin();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.rect(apple.x, apple.y, apple.width, apple.height);
                renderer.setColor(color);
            renderer.end();
        batch.end();

    }
    
}
