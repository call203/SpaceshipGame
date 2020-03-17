package org.techtown.spaceshipgame;

public class Missile {
    int x,y;
    int missileSpeed=35;

    //미사일의 위치
    Missile(int x, int y){
        this.x = x;
        this.y = y;
    }

    //미사일 속도 값에 따라 미사일이 움직이도록(y방향으로)
    public void move(){
        y-=missileSpeed;
    }
}
