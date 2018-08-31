package com.filipstrelec.flappyinspace;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.io.Console;
import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyInSpace extends ApplicationAdapter {
    private Sprite spriteBird;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Preferences prefs;
    private Texture background2;
    private Texture background;
    private Texture birdUp;
    private Texture birdDown;
    private Texture spaceStation1t;
    private Texture spaceStation2t;
    private Texture spaceStation1b;
    private Texture spaceStation2b;
    private Texture enemyFloating;
    private Texture laserH;
    private Texture laserV;
    private Texture laserS1;
    private Texture laserS2;
    private Texture panel;
    //    private Texture panel2;
    private Texture message;


    private Sound dead;
    private Sound flap1;

    private Sound laserSound;


    private Circle birdCircle;
    private Rectangle[] rectangleDown;
    private Rectangle[] rectangleUp;
    private Rectangle[] rectangleUp2;
    private Rectangle[] rectangleDown2;
    private Rectangle[] rectangleBoss;
    private Circle[] circle1Down;
    private Circle[] circle1Up;

    private Rectangle[] laserHRectangle;
    private Rectangle[] laserVRectangle;
    private Circle[] laserS1Circle;
    private Circle[] laserS2Circle;


    private TextureAtlas atlas;
    private Animation animation;
    private float timeCounter = 0;
    private float birdY = 0;
    private double birdX = 0;
    private float velocity = 0;
    private float objectVelocityX = 0;
    private float objectX[] = new float[7];
    private int gameStart = 0;
    private float accelerometer;
    private float gap = 350;
    private float maxOffset;
    Random r = new Random();
    private float randomOffset[] = new float[7];
    private int randomObjects[] = new int[7];
    private int randomLaser[] = new int[7];
    private int waveCounter = 0;
    private int laserSpeed[] = new int[7];
    private int objectType[] = new int[7];
    private int turnLasers = 0;
    private int gameOver = 0;
    private float score = 0;
    private int scoreFlored = 0;
    private int fontBegin = 500;
    private int highScore;
    private int highScoreW;
    private int waveFont = 0;
    private int currentWave = 0;
    int vibrator = 1;
    private int waveAll = 0;
    private int touchTap = 0;
    private int flapSound = 1;
    private int laserSoundCount = 0;


    @Override
    public void create() {



        dead = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
        flap1 = Gdx.audio.newSound(Gdx.files.internal("flap1.mp3"));

        laserSound = Gdx.audio.newSound(Gdx.files.internal("laser1.ogg"));

        prefs = Gdx.app.getPreferences("My pref");

//        prefs.putInteger("highScoreW", 0);
//        prefs.flush();
//        prefs.putInteger("highScore", 0);
//prefs.flush();

        birdCircle = new Circle();
        rectangleUp = new Rectangle[7];
        rectangleDown = new Rectangle[7];
        rectangleUp2 = new Rectangle[7];
        rectangleDown2 = new Rectangle[7];
        rectangleBoss = new Rectangle[7];
        circle1Up = new Circle[7];
        circle1Down = new Circle[7];
        laserHRectangle = new Rectangle[14];
        laserVRectangle = new Rectangle[7];
        laserS1Circle = new Circle[14];
        laserS2Circle = new Circle[14];
        shapeRenderer = new ShapeRenderer();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        background = new Texture("bg.png");
        background2 = new Texture("bg2.png");
        panel = new Texture("panel.png");
//        panel2 = new Texture("panel2.png");
        birdUp = new Texture("bird.png");
        birdDown = new Texture("bird2.png");
        spaceStation1t = new Texture("ss1t.png");
        spaceStation2t = new Texture("ss2t.png");
        spaceStation1b = new Texture("ss1b.png");
        spaceStation2b = new Texture("ss2b.png");
        enemyFloating = new Texture("enemyFloating2.png");
        laserH = new Texture("laserH.png");
        laserV = new Texture("laserV.png");
        laserS1 = new Texture("laserS1.png");
        laserS2 = new Texture("laserS2.png");
        message = new Texture("message.png");
        birdY = Gdx.graphics.getHeight() / 2 - (birdUp.getHeight() / 2);
        birdX = Gdx.graphics.getWidth() / 2 - (birdUp.getWidth() / 2);
        atlas = new TextureAtlas(Gdx.files.internal("flappy.atlas"));
        animation = new Animation(1 / 10f, atlas.getRegions());
        maxOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 200;
        objectVelocityX = 4; //brzina kretanja tubes-a


        offsetGenerator();


    }


    public void draw() {

//waveAll=1;


        for (int j = 0; j < 14; j++) {

            laserHRectangle[j] = new Rectangle();
            laserS1Circle[j] = new Circle();
            laserS2Circle[j] = new Circle();


        }

        for (int i = 0; i < 7; i++) {
            font.getData().setScale(7);
            font.draw(batch, "Wave " + String.valueOf(currentWave), objectX[0] - 700, birdY + 290);

//            font.getData().setScale(8);
//            font.draw(batch, "Wave2",  objectX[6]+700, birdY+290);
            rectangleUp2[i] = new Rectangle();
            rectangleDown2[i] = new Rectangle();
            rectangleUp[i] = new Rectangle();
            rectangleDown[i] = new Rectangle();
            rectangleBoss[i] = new Rectangle();
            circle1Down[i] = new Circle();
            circle1Up[i] = new Circle();
            laserVRectangle[i] = new Rectangle();

            if (waveCounter == 1) {

                batch.draw(spaceStation1t, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                batch.draw(spaceStation1b, objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i]);
                objectType[i] = 1;

//                rectangleUp[i].set( objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i],spaceStation1t.getWidth(),spaceStation1t.getHeight());
//                rectangleDown[i].set(objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i],spaceStation1b.getWidth(),spaceStation1b.getHeight());

                rectangleUp[i].set(objectX[i] + spaceStation1t.getWidth() / 4, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 80, spaceStation1t.getWidth() / 2, spaceStation1b.getHeight() + 150);
                circle1Up[i].set(objectX[i] + spaceStation1t.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 120, spaceStation1b.getWidth() / 2 - 10);

                rectangleDown[i].set(objectX[i] + spaceStation1b.getWidth() / 4, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getHeight() + randomOffset[i] - 80, spaceStation1b.getWidth() / 2, spaceStation1b.getHeight() - 150);
                circle1Down[i].set(objectX[i] + spaceStation1t.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] - 470, spaceStation1b.getWidth() / 2 - 10);

//                shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);
//                shapeRenderer.circle(circle1Up[i].x,circle1Up[i].y,circle1Up[i].radius);
//                shapeRenderer.rect(rectangleDown[i].x,rectangleDown[i].y,rectangleDown[i].width,rectangleDown[i].height);
//                shapeRenderer.circle(circle1Down[i].x,circle1Down[i].y,circle1Down[i].radius);
//                Intersector.overlaps(birdCircle,)
//

            } else if (waveCounter == 2) {

                batch.draw(spaceStation2t, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                batch.draw(spaceStation2b, objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i]);
                objectType[i] = 2;
                rectangleDown[i].set(objectX[i] + spaceStation1b.getWidth() / 4 + 40, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getHeight() + 75 + randomOffset[i] - 80, spaceStation1b.getWidth() / 2 - 60, spaceStation1b.getHeight() - 70);
                rectangleUp[i].set(objectX[i] + spaceStation1b.getWidth() / 4 + 30, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 5, spaceStation1b.getWidth() / 2 - 60, spaceStation1b.getHeight());
                rectangleUp2[i].set(objectX[i] + spaceStation1b.getWidth() / 4 - 55, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 5, spaceStation1b.getWidth() - 15, spaceStation1b.getWidth() / 2 - 40);
                rectangleDown2[i].set(objectX[i] + spaceStation1b.getWidth() / 4 - 55, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getWidth() * 1 / 3 - 10 + randomOffset[i] + 5, spaceStation1b.getWidth() - 15, spaceStation1b.getWidth() / 2 - 40);
//                shapeRenderer.rect(rectangleUp2[i].x,rectangleUp2[i].y,rectangleUp2[i].width,rectangleUp2[i].height);
//                shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);
//                shapeRenderer.rect(rectangleDown[i].x,rectangleDown[i].y,rectangleDown[i].width,rectangleDown[i].height);
//                shapeRenderer.rect(rectangleDown2[i].x,rectangleDown2[i].y,rectangleDown2[i].width,rectangleDown2[i].height);


            } else if (waveCounter == 3) {

                if (randomObjects[i] == 0) {

                    batch.draw(spaceStation2t, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                    batch.draw(spaceStation2b, objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i]);
                    rectangleDown[i].set(objectX[i] + spaceStation1b.getWidth() / 4 - 20, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getHeight() + 75 + randomOffset[i] - 80, spaceStation1b.getWidth() / 2 + 60, spaceStation1b.getHeight());
                    rectangleUp[i].set(objectX[i] + spaceStation1t.getWidth() / 4 - 20, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 5, spaceStation1t.getWidth() / 2 + 60, spaceStation1b.getHeight());
//                    shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);
//                    shapeRenderer.rect(rectangleDown[i].x,rectangleDown[i].y,rectangleDown[i].width,rectangleDown[i].height);
                    objectType[i] = 2;

                } else {


                    batch.draw(spaceStation1t, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                    batch.draw(spaceStation1b, objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i]);
                    objectType[i] = 1;
                    rectangleUp[i].set(objectX[i] + spaceStation1t.getWidth() / 4, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 80, spaceStation1t.getWidth() / 2, spaceStation1b.getHeight() + 150);
                    circle1Up[i].set(objectX[i] + spaceStation1t.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 120, spaceStation1b.getWidth() / 2 - 10);

                    rectangleDown[i].set(objectX[i] + spaceStation1b.getWidth() / 4, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getHeight() + randomOffset[i] - 80, spaceStation1b.getWidth() / 2, spaceStation1b.getHeight() - 150);
                    circle1Down[i].set(objectX[i] + spaceStation1t.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] - 470, spaceStation1b.getWidth() / 2 - 10);

//                    shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);
//                    shapeRenderer.circle(circle1Up[i].x,circle1Up[i].y,circle1Up[i].radius);
//                    shapeRenderer.rect(rectangleDown[i].x,rectangleDown[i].y,rectangleDown[i].width,rectangleDown[i].height);
//                    shapeRenderer.circle(circle1Down[i].x,circle1Down[i].y,circle1Down[i].radius);

                }


            } else if (waveCounter == 4) {


                batch.draw(enemyFloating, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                objectType[i] = 3;

                rectangleBoss[i].set(objectX[i] + 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 10, enemyFloating.getWidth() - 10, enemyFloating.getHeight() - 10);
//                shapeRenderer.rect(rectangleBoss[i].x,rectangleBoss[i].y,rectangleBoss[i].width,rectangleBoss[i].height);

            } else {

                if (randomObjects[i] == 0) {

                    batch.draw(spaceStation2t, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                    batch.draw(spaceStation2b, objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i]);
                    rectangleDown[i].set(objectX[i] + spaceStation1b.getWidth() / 4 - 20, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getHeight() + 75 + randomOffset[i] - 80, spaceStation1b.getWidth() / 2 + 60, spaceStation1b.getHeight());
                    rectangleUp[i].set(objectX[i] + spaceStation1t.getWidth() / 4 - 20, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 5, spaceStation1t.getWidth() / 2 + 60, spaceStation1b.getHeight());
//                    shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);
//                    shapeRenderer.rect(rectangleDown[i].x,rectangleDown[i].y,rectangleDown[i].width,rectangleDown[i].height);
                    objectType[i] = 2;

                } else if (randomObjects[i] == 1) {


                    batch.draw(spaceStation1t, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                    batch.draw(spaceStation1b, objectX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation2b.getHeight() + randomOffset[i]);
                    rectangleUp[i].set(objectX[i] + spaceStation1t.getWidth() / 4, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 80, spaceStation1t.getWidth() / 2, spaceStation1b.getHeight() + 150);
                    circle1Up[i].set(objectX[i] + spaceStation1t.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 120, spaceStation1b.getWidth() / 2 - 10);

                    rectangleDown[i].set(objectX[i] + spaceStation1b.getWidth() / 4, Gdx.graphics.getHeight() / 2 - gap / 2 - spaceStation1b.getHeight() + randomOffset[i] - 80, spaceStation1b.getWidth() / 2, spaceStation1b.getHeight() - 150);
                    circle1Down[i].set(objectX[i] + spaceStation1t.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] - 470, spaceStation1b.getWidth() / 2 - 10);

//                    shapeRenderer.rect(rectangleUp[i].x,rectangleUp[i].y,rectangleUp[i].width,rectangleUp[i].height);
//                    shapeRenderer.circle(circle1Up[i].x,circle1Up[i].y,circle1Up[i].radius);
//                    shapeRenderer.rect(rectangleDown[i].x,rectangleDown[i].y,rectangleDown[i].width,rectangleDown[i].height);
//                    shapeRenderer.circle(circle1Down[i].x,circle1Down[i].y,circle1Down[i].radius);
                    objectType[i] = 1;

                } else if (randomObjects[i] == 2) {

                    batch.draw(enemyFloating, objectX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i]);
                    rectangleBoss[i].set(objectX[i] + 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[i] + 10, enemyFloating.getWidth() - 10, enemyFloating.getHeight() - 10);
//                    shapeRenderer.rect(rectangleBoss[i].x,rectangleBoss[i].y,rectangleBoss[i].width,rectangleBoss[i].height);
                    objectType[i] = 3;

                }


            }


        }


    }

    public void offsetGenerator() {


//        if (waveCounter >= 15 && waveCounter <= 17) {
//            objectVelocityX++;
//        }

//        if (waveCounter >= 20 && waveCounter <= 23) {
//            objectVelocityX++;
//        }


        if (waveCounter > 1 && turnLasers == 0) {
            turnLasers = 1;
            waveCounter = 0;

        }
        currentWave++;
        waveCounter++;
        for (int i = 0; i < 7; i++) {
            objectX[i] = Gdx.graphics.getWidth() + i * 700;
            randomOffset[i] = r.nextInt((int) maxOffset * 2) - maxOffset;
            randomObjects[i] = r.nextInt(3);
            randomLaser[i] = r.nextInt(500);
            laserSpeed[i] = 0;
//            Gdx.app.log("randomObject", String.valueOf(randomObjects[i]));


        }


    }


    public void highScore() {

        if (scoreFlored > prefs.getInteger("highScore", 0)) {
            prefs.putInteger("highScore", scoreFlored);
            prefs.flush();
            highScore = prefs.getInteger("highScore", 0);
        } else {
            highScore = prefs.getInteger("highScore");

        }


        if (currentWave > prefs.getInteger("highScoreW", 0)) {
            prefs.putInteger("highScoreW", currentWave);
            prefs.flush();
            highScoreW = prefs.getInteger("highScoreW", 0);
        } else {
            highScoreW = prefs.getInteger("highScoreW");

        }
    }


    @Override
    public void render() {


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        Gdx.app.log("objectrandom", String.valueOf(randomObjects[2]));
        accelerometer = Gdx.input.getAccelerometerX();
//        Gdx.app.log("ziroskop", String.valueOf(accelerometer));

//        Gdx.app.log("birdY", String.valueOf(birdY));
//        Gdx.app.log("height",String.valueOf(velocity));
//        Gdx.app.log("deltaTime", String.valueOf(timeCounterFlored));
        batch.begin();


        if (turnLasers == 0) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (gameStart == 0) {
                batch.draw(message, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        } else {


            batch.draw(background2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        }


        batch.end();

        if (gameStart == 0) {

            if (Gdx.input.justTouched()) {



                gameStart = 1;
//
//                waveFont++;
//
//


            }


        }

        if (gameStart == 1) {


            objectX[0] -= objectVelocityX;
            objectX[1] -= objectVelocityX;
            objectX[2] -= objectVelocityX;
            objectX[3] -= objectVelocityX;
            objectX[4] -= objectVelocityX;
            objectX[5] -= objectVelocityX;
            objectX[6] -= objectVelocityX;


            batch.begin();


            if (gameOver == 0) {
                draw();
            }

            if (gameOver == 1) {


                if (turnLasers == 0) {
                    batch.draw(panel, Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                } else {

                    batch.draw(panel, Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);


//                    batch.draw(panel2, Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2-Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);


                }


                font.draw(batch, String.valueOf(scoreFlored), Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4 + 35, Gdx.graphics.getHeight() * 3 / 4 - 40);
                font.draw(batch, String.valueOf(currentWave), Gdx.graphics.getWidth() * 3 / 5 - 20, Gdx.graphics.getHeight() * 3 / 4 - 40);
                font.getData().setScale(6);
                font.draw(batch, "All time best", Gdx.graphics.getWidth() / 2 * 4 / 7 - 15, Gdx.graphics.getHeight() / 2 * 6 / 5);
                font.getData().setScale(5);
                font.draw(batch, "Score: " + String.valueOf(highScore), Gdx.graphics.getWidth() / 2 * 4 / 7 - 15, Gdx.graphics.getHeight() / 2 * 6 / 5 - Gdx.graphics.getHeight() * 1 / 12);
                font.draw(batch, "Waves: " + String.valueOf(highScoreW), Gdx.graphics.getWidth() / 2 * 4 / 7 - 15, Gdx.graphics.getHeight() / 2 * 6 / 5 - Gdx.graphics.getHeight() * 1 / 24 - Gdx.graphics.getHeight() * 1 / 12);

//                Gdx.app.log("highScore",String.valueOf(highScore));
                if (Gdx.input.justTouched()) {
                    touchTap++;
                    vibrator = 2;
                    birdY = -200;
                    birdX = -500;
                    fontBegin = 300;
                    if (touchTap > 1 && Gdx.input.justTouched()) {
                        birdY = Gdx.graphics.getHeight() / 2 - (birdUp.getHeight() / 2);
                        birdX = Gdx.graphics.getWidth() / 2 - (birdUp.getWidth() / 2);
                        turnLasers = 0;
                        timeCounter = 0;
                        vibrator = 1;
                        score = 0;
                        gameStart = 0;
                        gameOver = 0;
                        currentWave = 0;
                        waveCounter = 0;
                        objectVelocityX = 4;

                        offsetGenerator();
                        draw();

//                    waveFont++;


                        touchTap = 0;

                    }
                }


            }

            if (objectX[6] < 0 - spaceStation1b.getWidth()) {
                offsetGenerator();
            }


//        Gdx.graphics.getWidth() + i * 700;

//            waveAll++;
//
//
//
//            if( waveAll<10000&& waveAll>=10){
//
//
//                font.getData().setScale(8);
//                font.draw(batch, "Waveee", 100, birdY+290);
//
//
//
//
//            }


            if (turnLasers == 1) {
                if (objectX[0] < Gdx.graphics.getWidth() - 200 - randomLaser[0]) {
                    laserSpeed[0] = laserSpeed[0] + 10;
                    if (objectType[0] == 2) {

                        batch.draw(laserH, objectX[0] + laserSpeed[0] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0], 70, 70);
                        batch.draw(laserH, objectX[0] - laserSpeed[0] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[0] + 90, 70, 70);
                        if (objectX[0] < Gdx.graphics.getWidth() - 200 - randomLaser[0] && objectX[0] > Gdx.graphics.getWidth() - 200 - randomLaser[0] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }


                        laserHRectangle[0].set(objectX[0] + laserSpeed[0] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[0].x,laserHRectangle[0].y,laserHRectangle[0].width,laserHRectangle[0].height);
                        laserHRectangle[1].set(objectX[0] - laserSpeed[0] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[0] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[1].x,laserHRectangle[1].y,laserHRectangle[1].width,laserHRectangle[1].height);


                    } else if (objectType[0] == 1) {

                        batch.draw(laserV, objectX[0] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] - laserSpeed[0], 70, 70);
                        laserVRectangle[0].set(objectX[0] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] - laserSpeed[0], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[0].x,laserVRectangle[0].y,laserVRectangle[0].width,laserVRectangle[0].height);

                        if (objectX[0] < Gdx.graphics.getWidth() - 200 - randomLaser[0] && objectX[0] > Gdx.graphics.getWidth() - 200 - randomLaser[0] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }

//                        }
                    } else if (objectType[0] == 3) {


                        batch.draw(laserS1, objectX[0] - laserSpeed[0], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] - laserSpeed[0], 70, 70);
                        batch.draw(laserS1, objectX[0] + laserSpeed[0] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] + laserSpeed[0] + 200, 70, 70);
                        batch.draw(laserS2, objectX[0] + laserSpeed[0] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] - laserSpeed[0], 70, 70);
                        batch.draw(laserS2, objectX[0] - laserSpeed[0] - 10 + 0, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] + laserSpeed[0] + 160, 70, 70);

                        laserS1Circle[0].set(objectX[0] + 30 - laserSpeed[0], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] - laserSpeed[0] + 30, 30);
                        laserS1Circle[1].set(objectX[0] + 30 + laserSpeed[0] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] + laserSpeed[0] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[0].x,laserS1Circle[0].y,30);
//                        shapeRenderer.circle(laserS1Circle[1].x,laserS1Circle[1].y,30);

                        laserS2Circle[0].set(objectX[0] + laserSpeed[0] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] - laserSpeed[0] + 40, 30);
                        laserS2Circle[1].set(objectX[0] - laserSpeed[0] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[0] + laserSpeed[0] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[0].x,laserS2Circle[0].y,30);
//                        shapeRenderer.circle(laserS2Circle[1].x,laserS2Circle[1].y,30);
                        if (objectX[0] < Gdx.graphics.getWidth() - 200 - randomLaser[0] && objectX[0] > Gdx.graphics.getWidth() - 200 - randomLaser[0] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }


                    }


                }

                if (objectX[1] < Gdx.graphics.getWidth() - 200 - randomLaser[1]) {
                    laserSpeed[1] = laserSpeed[1] + 10;
                    if (objectType[1] == 2) {

                        batch.draw(laserH, objectX[1] + laserSpeed[1] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1], 70, 70);
                        batch.draw(laserH, objectX[1] - laserSpeed[1] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[1] + 90, 70, 70);

                        if (objectX[1] < Gdx.graphics.getWidth() - 200 - randomLaser[1] && objectX[1] > Gdx.graphics.getWidth() - 200 - randomLaser[1] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }

                        laserHRectangle[2].set(objectX[1] + laserSpeed[1] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[2].x,laserHRectangle[2].y,laserHRectangle[2].width,laserHRectangle[2].height);
                        laserHRectangle[3].set(objectX[1] - laserSpeed[1] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[1] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[3].x,laserHRectangle[3].y,laserHRectangle[3].width,laserHRectangle[3].height);

                    } else if (objectType[1] == 1) {

                        batch.draw(laserV, objectX[1] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] - laserSpeed[1], 70, 70);
                        laserVRectangle[1].set(objectX[1] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] - laserSpeed[1], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[1].x,laserVRectangle[1].y,laserVRectangle[1].width,laserVRectangle[1].height);
//                        if(laserSoundCount==0){
//                            flap1.setPitch(laserSound.play(),0.9f);
//                            laserSoundCount++;
//
//                        }
                        if (objectX[1] < Gdx.graphics.getWidth() - 200 - randomLaser[1] && objectX[1] > Gdx.graphics.getWidth() - 200 - randomLaser[1] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }


                    } else if (objectType[1] == 3) {


                        batch.draw(laserS1, objectX[1] - laserSpeed[1], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] - laserSpeed[1], 70, 70);
                        batch.draw(laserS1, objectX[1] + laserSpeed[1] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] + laserSpeed[1] + 200, 70, 70);
                        batch.draw(laserS2, objectX[1] + laserSpeed[1] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] - laserSpeed[1], 70, 70);
                        batch.draw(laserS2, objectX[1] - laserSpeed[1] - 10 + 0, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] + laserSpeed[1] + 160, 70, 70);


                        laserS1Circle[2].set(objectX[1] + 30 - laserSpeed[1], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] - laserSpeed[1] + 30, 30);
                        laserS1Circle[3].set(objectX[1] + 30 + laserSpeed[1] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] + laserSpeed[1] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[2].x,laserS1Circle[2].y,30);
//                        shapeRenderer.circle(laserS1Circle[3].x,laserS1Circle[3].y,30);

                        laserS2Circle[2].set(objectX[1] + laserSpeed[1] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] - laserSpeed[1] + 40, 30);
                        laserS2Circle[3].set(objectX[1] - laserSpeed[1] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[1] + laserSpeed[1] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[2].x,laserS2Circle[2].y,30);
//                        shapeRenderer.circle(laserS2Circle[3].x,laserS2Circle[3].y,30);
                        if (objectX[1] < Gdx.graphics.getWidth() - 200 - randomLaser[1] && objectX[1] > Gdx.graphics.getWidth() - 200 - randomLaser[1] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }

                    }


                }


                if (objectX[2] < Gdx.graphics.getWidth() - 200 - randomLaser[2]) {
                    laserSpeed[2] = laserSpeed[2] + 10;
                    if (objectType[2] == 2) {

                        batch.draw(laserH, objectX[2] + laserSpeed[2] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2], 70, 70);
                        batch.draw(laserH, objectX[2] - laserSpeed[2] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[2] + 90, 70, 70);

                        if (objectX[2] < Gdx.graphics.getWidth() - 200 - randomLaser[2] && objectX[2] > Gdx.graphics.getWidth() - 200 - randomLaser[2] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }

                        laserHRectangle[4].set(objectX[2] + laserSpeed[2] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[4].x,laserHRectangle[4].y,laserHRectangle[4].width,laserHRectangle[4].height);
                        laserHRectangle[5].set(objectX[2] - laserSpeed[2] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[2] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[5].x,laserHRectangle[5].y,laserHRectangle[5].width,laserHRectangle[5].height);

                    } else if (objectType[2] == 1) {

                        batch.draw(laserV, objectX[2] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] - laserSpeed[2], 70, 70);
                        laserVRectangle[2].set(objectX[2] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] - laserSpeed[2], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[2].x,laserVRectangle[2].y,laserVRectangle[2].width,laserVRectangle[2].height);
                        if (objectX[2] < Gdx.graphics.getWidth() - 200 - randomLaser[2] && objectX[2] > Gdx.graphics.getWidth() - 200 - randomLaser[2] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }


                    } else if (objectType[2] == 3) {


                        batch.draw(laserS1, objectX[2] - laserSpeed[2], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] - laserSpeed[2], 70, 70);
                        batch.draw(laserS1, objectX[2] + laserSpeed[2] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] + laserSpeed[2] + 200, 70, 70);
                        batch.draw(laserS2, objectX[2] + laserSpeed[2] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] - laserSpeed[2], 70, 70);
                        batch.draw(laserS2, objectX[2] - laserSpeed[2] - 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] + laserSpeed[2] + 160, 70, 70);


                        laserS1Circle[4].set(objectX[2] + 30 - laserSpeed[2], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] - laserSpeed[2] + 30, 30);
                        laserS1Circle[5].set(objectX[2] + 30 + laserSpeed[2] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] + laserSpeed[2] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[4].x,laserS1Circle[4].y,30);
//                        shapeRenderer.circle(laserS1Circle[5].x,laserS1Circle[5].y,30);

                        laserS2Circle[4].set(objectX[2] + laserSpeed[2] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] - laserSpeed[2] + 40, 30);
                        laserS2Circle[5].set(objectX[2] - laserSpeed[2] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[2] + laserSpeed[2] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[4].x,laserS2Circle[4].y,30);
//                        shapeRenderer.circle(laserS2Circle[5].x,laserS2Circle[5].y,30);
                        if (objectX[2] < Gdx.graphics.getWidth() - 200 - randomLaser[2] && objectX[2] > Gdx.graphics.getWidth() - 200 - randomLaser[2] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }

                    }

                }

                if (objectX[3] < Gdx.graphics.getWidth() - 200 - randomLaser[3]) {
                    laserSpeed[3] = laserSpeed[3] + 10;
                    if (objectType[3] == 2) {

                        batch.draw(laserH, objectX[3] + laserSpeed[3] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3], 70, 70);
                        batch.draw(laserH, objectX[3] - laserSpeed[3] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[3] + 90, 70, 70);
                        if (objectX[3] < Gdx.graphics.getWidth() - 200 - randomLaser[3] && objectX[3] > Gdx.graphics.getWidth() - 200 - randomLaser[3] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }


                        laserHRectangle[6].set(objectX[3] + laserSpeed[3] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[6].x,laserHRectangle[6].y,laserHRectangle[6].width,laserHRectangle[6].height);
                        laserHRectangle[7].set(objectX[3] - laserSpeed[3] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[3] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[7].x,laserHRectangle[7].y,laserHRectangle[7].width,laserHRectangle[7].height);


                    } else if (objectType[3] == 1) {

                        batch.draw(laserV, objectX[3] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] - laserSpeed[3], 70, 70);
                        laserVRectangle[3].set(objectX[3] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] - laserSpeed[3], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[3].x,laserVRectangle[3].y,laserVRectangle[3].width,laserVRectangle[3].height);
                        if (objectX[3] < Gdx.graphics.getWidth() - 200 - randomLaser[3] && objectX[3] > Gdx.graphics.getWidth() - 200 - randomLaser[3] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }


                    } else if (objectType[3] == 3) {


                        batch.draw(laserS1, objectX[3] - laserSpeed[3], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] - laserSpeed[3], 70, 70);
                        batch.draw(laserS1, objectX[3] + laserSpeed[3] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] + laserSpeed[3] + 200, 70, 70);
                        batch.draw(laserS2, objectX[3] + laserSpeed[3] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] - laserSpeed[3], 70, 70);
                        batch.draw(laserS2, objectX[3] - laserSpeed[3] - 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] + laserSpeed[3] + 160, 70, 70);

                        laserS1Circle[6].set(objectX[3] + 30 - laserSpeed[3], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] - laserSpeed[3] + 30, 30);
                        laserS1Circle[7].set(objectX[3] + 30 + laserSpeed[3] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] + laserSpeed[3] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[6].x,laserS1Circle[6].y,30);
//                        shapeRenderer.circle(laserS1Circle[7].x,laserS1Circle[7].y,30);

                        laserS2Circle[6].set(objectX[3] + laserSpeed[3] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] - laserSpeed[3] + 40, 30);
                        laserS2Circle[7].set(objectX[3] - laserSpeed[3] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[3] + laserSpeed[3] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[6].x,laserS2Circle[6].y,30);
//                        shapeRenderer.circle(laserS2Circle[7].x,laserS2Circle[7].y,30);
                        if (objectX[3] < Gdx.graphics.getWidth() - 200 - randomLaser[3] && objectX[3] > Gdx.graphics.getWidth() - 200 - randomLaser[3] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }

                    }

                }


                if (objectX[4] < Gdx.graphics.getWidth() - 200 - randomLaser[4]) {
                    laserSpeed[4] = laserSpeed[4] + 10;
                    if (objectType[4] == 2) {

                        batch.draw(laserH, objectX[4] + laserSpeed[4] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4], 70, 70);
                        batch.draw(laserH, objectX[4] - laserSpeed[4] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[4] + 90, 70, 70);
                        if (objectX[4] < Gdx.graphics.getWidth() - 200 - randomLaser[4] && objectX[4] > Gdx.graphics.getWidth() - 200 - randomLaser[4] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }


                        laserHRectangle[8].set(objectX[4] + laserSpeed[4] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[8].x,laserHRectangle[8].y,laserHRectangle[8].width,laserHRectangle[8].height);
                        laserHRectangle[9].set(objectX[4] - laserSpeed[4] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[4] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[9].x,laserHRectangle[9].y,laserHRectangle[9].width,laserHRectangle[9].height);


                    } else if (objectType[4] == 1) {

                        batch.draw(laserV, objectX[4] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] - laserSpeed[4], 70, 70);
                        laserVRectangle[4].set(objectX[4] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] - laserSpeed[4], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[4].x,laserVRectangle[4].y,laserVRectangle[4].width,laserVRectangle[4].height);
                        if (objectX[4] < Gdx.graphics.getWidth() - 200 - randomLaser[4] && objectX[4] > Gdx.graphics.getWidth() - 200 - randomLaser[4] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }


                    } else if (objectType[4] == 3) {


                        batch.draw(laserS1, objectX[4] - laserSpeed[4], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] - laserSpeed[4], 70, 70);
                        batch.draw(laserS1, objectX[4] + laserSpeed[4] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] + laserSpeed[4] + 200, 70, 70);
                        batch.draw(laserS2, objectX[4] + laserSpeed[4] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] - laserSpeed[4], 70, 70);
                        batch.draw(laserS2, objectX[4] - laserSpeed[4] - 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] + laserSpeed[4] + 160, 70, 70);


                        laserS1Circle[8].set(objectX[4] + 30 - laserSpeed[4], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] - laserSpeed[4] + 30, 30);
                        laserS1Circle[9].set(objectX[4] + 30 + laserSpeed[4] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] + laserSpeed[4] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[8].x,laserS1Circle[8].y,30);
//                        shapeRenderer.circle(laserS1Circle[9].x,laserS1Circle[9].y,30);

                        laserS2Circle[8].set(objectX[4] + laserSpeed[4] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] - laserSpeed[4] + 40, 30);
                        laserS2Circle[9].set(objectX[4] - laserSpeed[4] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[4] + laserSpeed[4] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[8].x,laserS2Circle[8].y,30);
//                        shapeRenderer.circle(laserS2Circle[9].x,laserS2Circle[9].y,30);
                        if (objectX[4] < Gdx.graphics.getWidth() - 200 - randomLaser[4] && objectX[4] > Gdx.graphics.getWidth() - 200 - randomLaser[4] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }

                    }

                }

                if (objectX[5] < Gdx.graphics.getWidth() - 200 - randomLaser[5]) {
                    laserSpeed[5] = laserSpeed[5] + 10;
                    if (objectType[5] == 2) {

                        batch.draw(laserH, objectX[5] + laserSpeed[5] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5], 70, 70);
                        batch.draw(laserH, objectX[5] - laserSpeed[5] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[5] + 90, 70, 70);
                        if (objectX[5] < Gdx.graphics.getWidth() - 200 - randomLaser[5] && objectX[5] > Gdx.graphics.getWidth() - 200 - randomLaser[5] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }

                        laserHRectangle[10].set(objectX[5] + laserSpeed[5] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[10].x,laserHRectangle[10].y,laserHRectangle[10].width,laserHRectangle[10].height);
                        laserHRectangle[11].set(objectX[5] - laserSpeed[5] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[5] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[11].x,laserHRectangle[11].y,laserHRectangle[11].width,laserHRectangle[11].height);

                    } else if (objectType[5] == 1) {

                        batch.draw(laserV, objectX[5] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] - laserSpeed[5], 70, 70);
                        laserVRectangle[5].set(objectX[5] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] - laserSpeed[5], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[5].x,laserVRectangle[5].y,laserVRectangle[5].width,laserVRectangle[5].height);
                        if (objectX[5] < Gdx.graphics.getWidth() - 200 - randomLaser[5] && objectX[5] > Gdx.graphics.getWidth() - 200 - randomLaser[5] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }


                    } else if (objectType[5] == 3) {


                        batch.draw(laserS1, objectX[5] - laserSpeed[5], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] - laserSpeed[5], 70, 70);
                        batch.draw(laserS1, objectX[5] + laserSpeed[5] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] + laserSpeed[5] + 200, 70, 70);
                        batch.draw(laserS2, objectX[5] + laserSpeed[5] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] - laserSpeed[5], 70, 70);
                        batch.draw(laserS2, objectX[5] - laserSpeed[5] - 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] + laserSpeed[5] + 160, 70, 70);


                        laserS1Circle[10].set(objectX[5] + 30 - laserSpeed[5], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] - laserSpeed[5] + 30, 30);
                        laserS1Circle[11].set(objectX[5] + 30 + laserSpeed[5] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] + laserSpeed[5] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[10].x,laserS1Circle[10].y,30);
//                        shapeRenderer.circle(laserS1Circle[11].x,laserS1Circle[11].y,30);

                        laserS2Circle[10].set(objectX[5] + laserSpeed[5] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] - laserSpeed[5] + 40, 30);
                        laserS2Circle[11].set(objectX[5] - laserSpeed[5] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[5] + laserSpeed[5] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[10].x,laserS2Circle[10].y,30);
//                        shapeRenderer.circle(laserS2Circle[11].x,laserS2Circle[11].y,30);
                        if (objectX[5] < Gdx.graphics.getWidth() - 200 - randomLaser[5] && objectX[5] > Gdx.graphics.getWidth() - 200 - randomLaser[5] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }

                    }

                }


                if (objectX[6] < Gdx.graphics.getWidth() - 200 - randomLaser[6]) {
                    laserSpeed[6] = laserSpeed[6] + 10;
                    if (objectType[6] == 2) {

                        batch.draw(laserH, objectX[6] + laserSpeed[6] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6], 70, 70);
                        batch.draw(laserH, objectX[6] - laserSpeed[6] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[6] + 90, 70, 70);
                        if (objectX[6] < Gdx.graphics.getWidth() - 200 - randomLaser[6] && objectX[6] > Gdx.graphics.getWidth() - 200 - randomLaser[6] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.2f);
                        }


                        laserHRectangle[12].set(objectX[6] + laserSpeed[6] + spaceStation2t.getWidth(), Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6], 55, 55);
//                        shapeRenderer.rect(laserHRectangle[12].x,laserHRectangle[12].y,laserHRectangle[12].width,laserHRectangle[12].height);
                        laserHRectangle[13].set(objectX[6] - laserSpeed[6] + 4, Gdx.graphics.getHeight() / 2 - gap + randomOffset[6] + 90, 55, 55);
//                        shapeRenderer.rect(laserHRectangle[13].x,laserHRectangle[13].y,laserHRectangle[13].width,laserHRectangle[13].height);
                    } else if (objectType[6] == 1) {

                        batch.draw(laserV, objectX[6] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] - laserSpeed[6], 70, 70);
                        laserVRectangle[6].set(objectX[6] + spaceStation2t.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] - laserSpeed[6], 50, 50);
//                        shapeRenderer.rect(laserVRectangle[6].x,laserVRectangle[6].y,laserVRectangle[6].width,laserVRectangle[6].height);
                        if (objectX[6] < Gdx.graphics.getWidth() - 200 - randomLaser[6] && objectX[6] > Gdx.graphics.getWidth() - 200 - randomLaser[6] - 5) {
                            laserSound.setPitch(laserSound.play(), 0.9f);
                        }


                    } else if (objectType[6] == 3) {


                        batch.draw(laserS1, objectX[6] - laserSpeed[6], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] - laserSpeed[6], 70, 70);
                        batch.draw(laserS1, objectX[6] + laserSpeed[6] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] + laserSpeed[6] + 200, 70, 70);
                        batch.draw(laserS2, objectX[6] + laserSpeed[6] + 175, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] - laserSpeed[6], 70, 70);
                        batch.draw(laserS2, objectX[6] - laserSpeed[6] - 10, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] + laserSpeed[6] + 160, 70, 70);


                        laserS1Circle[12].set(objectX[6] + 30 - laserSpeed[6], Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] - laserSpeed[6] + 30, 30);
                        laserS1Circle[13].set(objectX[6] + 30 + laserSpeed[6] + 200, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] + laserSpeed[6] + 200 + 30, 30);
//                        shapeRenderer.circle(laserS1Circle[12].x,laserS1Circle[12].y,30);
//                        shapeRenderer.circle(laserS1Circle[13].x,laserS1Circle[13].y,30);

                        laserS2Circle[12].set(objectX[6] + laserSpeed[6] + 175 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] - laserSpeed[6] + 40, 30);
                        laserS2Circle[13].set(objectX[6] - laserSpeed[6] - 10 + 40, Gdx.graphics.getHeight() / 2 + gap / 2 + randomOffset[6] + laserSpeed[6] + 160 + 40, 30);
//                        shapeRenderer.circle(laserS2Circle[12].x,laserS2Circle[12].y,30);
//                        shapeRenderer.circle(laserS2Circle[13].x,laserS2Circle[13].y,30);
                        if (objectX[6] < Gdx.graphics.getWidth() - 200 - randomLaser[6] && objectX[6] > Gdx.graphics.getWidth() - 200 - randomLaser[6] - 5) {
                            laserSound.setPitch(laserSound.play(), 1.7f);
                        }

                    }


                }
            }
            if (gameOver == 0) {


                for (int i = 0; i < 7; i++) {

                    if (Intersector.overlaps(birdCircle, rectangleUp[i]) || Intersector.overlaps(birdCircle, rectangleDown[i])
                            || Intersector.overlaps(birdCircle, circle1Down[i]) || Intersector.overlaps(birdCircle, circle1Up[i]) || Intersector.overlaps(birdCircle, rectangleBoss[i])
                            || Intersector.overlaps(birdCircle, laserVRectangle[i]) || Intersector.overlaps(birdCircle, rectangleDown2[i]) || Intersector.overlaps(birdCircle, rectangleUp2[i])) {

//                    Gdx.app.log("COLLISION","Laser test");
                        objectVelocityX = 0;
                        gameOver = 1;
                        dead.play(0.4f);
                        Gdx.input.vibrate(200);

                        highScore();
//                    Gdx.app.log("highScore",String.valueOf(highScore));

                    }
                }


                for (int j = 0; j < 14; j++) {

                    if (Intersector.overlaps(birdCircle, laserHRectangle[j]) || Intersector.overlaps(birdCircle, laserS1Circle[j]) || Intersector.overlaps(birdCircle, laserS2Circle[j])) {

//                    Gdx.app.log("k","collision laser");
                        objectVelocityX = 0;
                        gameOver = 1;
                        dead.play(0.4f);
                        Gdx.input.vibrate(200);
                        highScore();
//                    Gdx.app.log("highScore",String.valueOf(highScore));
                    }
                }


            }

            batch.end();
            if (objectX[6] < 0 - spaceStation1b.getWidth()) {
                offsetGenerator();
            }


            if (Gdx.input.justTouched() && birdY <= Gdx.graphics.getHeight() - birdUp.getHeight() && gameOver == 0) {

                if (flapSound == 1) {
//
                    flap1.play(1.9f);
                    flapSound = 2;

                } else {

                    flap1.setPitch(flap1.play(1.9f), 1.2f);
                    flapSound = 1;


                }


                velocity = -20;

            }

            if (birdY >= 0 || velocity < -5) {
//            velocity=0;
                velocity++;


                birdY -= velocity;
            }

            if (birdY < 1) {
                gameOver = 1;


                if (vibrator == 1) {
                    Gdx.input.vibrate(200);
                    dead.play(0.4f);
                    vibrator++;

                }

                highScore();
//                Gdx.app.log("highScore",String.valueOf(highScore));
                objectVelocityX = 0;
            }


            if (birdX <= 0 || birdX > Gdx.graphics.getWidth()) {


                birdX = 0.00;

                if (accelerometer < 0 && gameOver == 0) {

                    birdX = birdX - (5 * accelerometer);

                }

            } else {
                if (gameOver == 0) {
                    birdX = birdX - (5 * accelerometer);
                }
            }


        }


        if (gameStart == 1 && gameOver == 0) {


            if (birdX > Gdx.graphics.getWidth() && gameOver == 0) {

//        Gdx.app.log("SCORE", "bravo");
                score = score + 6;
                fontBegin = 1;

            }

            if (fontBegin < 40) {

                batch.begin();
                font.getData().setScale(5);
                font.draw(batch, "+2!", Gdx.graphics.getWidth() - 150, birdY - 40);
                batch.end();
                fontBegin++;
            }


            timeCounter += Gdx.graphics.getDeltaTime();
            score += Gdx.graphics.getDeltaTime();
            scoreFlored = (int) Math.floor(score) / 3;


        }


//        batch.begin();
//        batch.draw((TextureRegion) animation.getKeyFrame(timeCounter, true), (float) birdX, birdY);
//        batch.end();
        spriteBird = new Sprite((TextureRegion) animation.getKeyFrame(timeCounter, true));

        spriteBird.setPosition((float) birdX, birdY);


        batch.begin();


//
//        if(waveFont<100&&waveFont>=1){
//
//
//            font.getData().setScale(8);
//            font.draw(batch, "Wave 1", Gdx.graphics.getWidth()/3-10, birdY+290);

//
//waveFont++;
//
//
//        }


        spriteBird.draw(batch);
        font.getData().setScale(8);
        font.draw(batch, String.valueOf(scoreFlored), Gdx.graphics.getWidth() / 2 - 40, Gdx.graphics.getHeight() - 100);
        batch.end();


        birdCircle.set((float) birdX + birdUp.getWidth() / 2, birdY + birdUp.getHeight() / 2, birdUp.getWidth() / 2 - 10);
//
//        shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
////
//        shapeRenderer.end();


    }


    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        birdUp.dispose();
        birdDown.dispose();
        atlas.dispose();
        spaceStation1b.dispose();
        spaceStation1t.dispose();
        spaceStation2b.dispose();
        spaceStation2t.dispose();
        enemyFloating.dispose();
        laserH.dispose();
        laserV.dispose();
        laserS2.dispose();
        laserS1.dispose();
        panel.dispose();
//        panel2.dispose();
        flap1.dispose();
        message.dispose();
        laserSound.dispose();
        dead.dispose();
    }
}
