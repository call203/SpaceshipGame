# SpaceshipGame

### 1. To obtain a WindowManager for a different display [단말기 해상도]

  Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
  
### 2. To create object [객체 생성]
  BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);
  
  &&
  
  Bitmap.createScaledBitmap(leftKey, button_width, button_width, true);
    
 ### 3.create view custom
<pre>
<code>
  class MyView extends View{
  public MyView(Context context) {
                super(context);
  }
  
  }
</code>
</pre>

### 4. Paint class for drawBitmap

### 5. onTouchEvent 

### 6. Handler for Repeated calls
<pre>
<code>
   public MyView(Context context) {
                super(context);
                setBackgroundColor(Color.BLUE);
                gHandler.sendEmptyMessageDelayed(0,1000);

            }
            
 //Handler --> 미사일, 우주선, 행선의 위치를 새로 그려줌
 Handler gHandler = new Handler(){
    public void handleMessage(Message msg){
      invalidate();
      gHandler.sendEmptyMessageDelayed(0,30);
   }
 };
</code>
</pre>

## Missile & Palent

to create one object with location and move method.
