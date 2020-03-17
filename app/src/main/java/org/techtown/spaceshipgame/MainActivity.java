package org.techtown.spaceshipgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

import android.os.Handler;
public class MainActivity extends AppCompatActivity {
    Bitmap spaceship,leftKey,rightKey,screen;
    int spaceship_x,spaceship_y;
    int leftkey_x,leftkey_y;
    int rightkey_x,rightkey_y;
    int Width,Height;
    int button_width;

    Bitmap missileButton,missile,planetimg;
    int missileButton_x, missileButton_y;
    int missileWidth;
    int missile_middle;

    int count;
    ArrayList<Missile> myM;
    ArrayList<Planet> planet;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        //단말기 해상도 구하기
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Height = size.y;
        Width = size.x;

        myM = new ArrayList<Missile>();
        planet = new ArrayList<Planet>();

        //우주선 해상도 크기에 따라 조정
        spaceship = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);
        int x = Width / 8;
        int y = Height / 11;
        spaceship = Bitmap.createScaledBitmap(spaceship, x, y, true);

        spaceship_x = Width*1 /9;
        spaceship_y = Height*6 /9;

        //버튼 해상도 크기에 따라 조정
        //왼쪽 키
        leftKey = BitmapFactory.decodeResource(getResources(), R.drawable.leftkey);
        leftkey_x = Width * 5 / 9;
        leftkey_y = Height * 7 / 9;
        button_width = Width / 6;
        leftKey = Bitmap.createScaledBitmap(leftKey, button_width, button_width, true);

        //오른쪽 키
        rightKey = BitmapFactory.decodeResource(getResources(), R.drawable.rightkey);
        rightkey_x = Width * 7 / 9;
        rightkey_y = Height * 7 / 9;
        rightKey = Bitmap.createScaledBitmap(rightKey, button_width, button_width, true);

        missileButton = BitmapFactory.decodeResource(getResources(),R.drawable.shot);
        missileButton = Bitmap.createScaledBitmap(missileButton, button_width, button_width, true);
        missileButton_x = Width * 1/11;
        missileButton_y = Height * 7 / 9;

        missile = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
        missile = Bitmap.createScaledBitmap(missile, button_width/4, button_width/4, true);
        missileWidth = missile.getWidth();

        planetimg = BitmapFactory.decodeResource(getResources(),R.drawable.planet);
        planetimg = Bitmap.createScaledBitmap(planetimg, button_width, button_width, true);

        screen = BitmapFactory.decodeResource(getResources(), R.drawable.space);
        screen = Bitmap.createScaledBitmap(screen, Width, Height, true);


    }

        /**
         * 화면 그리기
         */
        class MyView extends View {

            public MyView(Context context) {
                super(context);
                setBackgroundColor(Color.BLUE);
                gHandler.sendEmptyMessageDelayed(0,1000);

            }
            public void onDraw(Canvas canvas){
                Paint p1 = new Paint();
                p1.setColor(Color.RED);
                p1.setTextSize(50);
                canvas.drawBitmap(screen,0,0,p1); //게임 배경

                canvas.drawText(Integer.toString(score),10,300,p1);

                canvas.drawBitmap(spaceship,spaceship_x,spaceship_y,p1);
                canvas.drawBitmap(leftKey,leftkey_x,leftkey_y,p1);
                canvas.drawBitmap(rightKey,rightkey_x,rightkey_y,p1);

                Random r1 = new Random();
                int x = r1.nextInt(Width);

                if(planet.size()<4) {
                    planet.add(new Planet(x, 100, 0));
                }




                canvas.drawBitmap(missileButton,missileButton_x,missileButton_y,p1);
                for(Missile tmp:myM)
                    canvas.drawBitmap(missile,tmp.x,tmp.y,p1);
                for(Planet tmp:planet)
                    canvas.drawBitmap(planetimg,tmp.x,tmp.y,p1);

                moveMissile();
                movePlanet();
                checkCollision();
                //count++;

            }

            public void moveMissile() {
                for (int i = myM.size() - 1; i >= 0; i--) {
                    myM.get(i).move();
                }
                for (int i = myM.size() - 1; i >= 0; i--) {
                    if (myM.get(i).y < 0) myM.remove(i);
                }
            }

            public void movePlanet(){
                //행성이 화면 밖을 나가지 않도록 조정
                for (int i = planet.size() - 1; i >= 0; i--) {
                    if((planet.get(i).x)< 0){
                        planet.get(i).dir=1;
                    }
                    else if(planet.get(i).x > Width){
                        planet.get(i).dir=0;
                    }
                }
                for (int i = planet.size() - 1; i >= 0; i--) {
                    planet.get(i).move();
                }
            }

            public void checkCollision(){
                for (int i = planet.size() - 1; i >= 0; i--) {
                    for (int j = myM.size() - 1; j >= 0; j--) {
                        if((myM.get(j).x>planet.get(i).x) && (myM.get(j).x+missileWidth<planet.get(i).x+button_width) &&
                                (myM.get(j).y>planet.get(i).y) && (myM.get(j).y+missileWidth<planet.get(i).y+button_width)){
                            planet.remove(i);
                            score+=10;
                        }
                    }
                }

            }

            //Handler --> 미사일, 우주선, 행선의 위치를 새로 그려줌
            Handler gHandler = new Handler(){
                public void handleMessage(Message msg){
                    invalidate();
                    gHandler.sendEmptyMessageDelayed(0,30);
                }

            };

            /**
             * 버튼 이벤트
             */
            public boolean onTouchEvent(MotionEvent event){
                int x=0,y =0;

                //버튼 눌렀을때마다 위치 구하기
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    x =(int)event.getX();
                    y=(int)event.getY();
                }

                //왼쪽 버튼을 눌렀을 때
                if((x >leftkey_x) && (x<leftkey_x+button_width) && (y>leftkey_y) &&(x<leftkey_y+button_width))
                    spaceship_x-=20;
                //오른쪽 버튼을 눌렀을 때
                if((x >rightkey_x) && (x<rightkey_x+button_width) && (y>rightkey_y) &&(x<rightkey_y+button_width))
                    spaceship_x+=20;

                //미사일 버튼을 눌렀을 때
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                if ((x > missileButton_x) && (x < missileButton_x + button_width) && (y > missileButton_y) && (x < missileButton_y + button_width))
                        if(myM.size()<1) {
                        myM.add(new Missile(spaceship_x + spaceship.getWidth() / 2 - missileWidth / 2, spaceship_y));
                }

                return true;

            }

        }




}
