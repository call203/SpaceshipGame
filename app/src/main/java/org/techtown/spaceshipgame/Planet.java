package org.techtown.spaceshipgame;

public class Planet {
    int x,y;
    int planetSpeed=15;
    int dir;

    //미사일의 위치
    Planet(int x, int y,int dir){
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    //미사일 속도 값에 따라 미사일이 움직이도록(y방향으로)
    //단, dir가 0/1을 기준으로 왼쪽 오른쪽으로 이동하게 함.
    public void move(){
        if(dir==0){
            x-=planetSpeed;
        }else x+=planetSpeed;
    }

}
