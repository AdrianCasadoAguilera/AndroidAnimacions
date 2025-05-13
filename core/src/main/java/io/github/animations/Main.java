package io.github.animations;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture spriteSheet;
    private Texture background;
    private TextureRegion bgRegion;
    private Animation<TextureRegion>[] animations;
    private float stateTime;
    private int currentDirection = 0;
    private float speed = 4f;

    private static final int FRAME_WIDTH = 64;
    private static final int FRAME_HEIGHT = 64;
    private static final float FRAME_DURATION = 0.1f;

    private float posx, posy;

    @Override
    public void create() {
        batch = new SpriteBatch();
        spriteSheet = new Texture(Gdx.files.internal("character.png"));
        background = new Texture(Gdx.files.internal("background.png"));
        background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        bgRegion = new TextureRegion(background);

        // Animacions
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);
        animations = new Animation[4];
        animations[0] = new Animation<>(FRAME_DURATION, tmp[2]);
        animations[1] = new Animation<>(FRAME_DURATION, tmp[3]);
        animations[2] = new Animation<>(FRAME_DURATION, tmp[0]);
        animations[3] = new Animation<>(FRAME_DURATION, tmp[1]);

        stateTime = 0f;
        posx = 0;
        posy = 0;
    }

    @Override
    public void render() {
        handleTouchInput();
        stateTime += Gdx.graphics.getDeltaTime();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        bgRegion.setRegion((int) posx, (int) posy, screenWidth, screenHeight);

        TextureRegion currentFrame = animations[currentDirection].getKeyFrame(stateTime, true);

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1);
        batch.begin();
        batch.draw(bgRegion, 0, 0, screenWidth, screenHeight);
        batch.draw(currentFrame, screenWidth / 2f - 64, screenHeight / 2f - 64, 128, 128);
        batch.end();
    }

    private void handleTouchInput() {
        if (Gdx.input.isTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            y = screenHeight - y;

            if (y < screenHeight * 0.25f) {
                currentDirection = 0;
                posy += speed;
            } else if (y > screenHeight * 0.75f) {
                currentDirection = 2;
                posy -= speed;
            } else if (x < screenWidth * 0.25f) {
                currentDirection = 3;
                posx -= speed;
            } else if (x > screenWidth * 0.75f) {
                currentDirection = 1;
                posx += speed;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        spriteSheet.dispose();
        background.dispose();
    }
}
