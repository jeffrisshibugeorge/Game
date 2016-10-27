package com.subway.jeffris.golf;

/**
 * Created by 30811 on 27/09/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;



public class SimulationView extends View implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Display mDisplay;

    private Bitmap mGrass;
    private Bitmap mHole;
    private Bitmap mBitmap;
    private static final int BALL_SIZE=40;
    private static final int HOLE_SIZE=70;

    private float mXOrigins;
    private float mYorigin;

    private float mHorizontalBound;
    private float mVerticalBound;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    private Particle mBall=new Particle();

    public SimulationView(Context context){
        super(context);

        WindowManager mWindowManager=(WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay=mWindowManager.getDefaultDisplay();

        mSensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Bitmap ball= BitmapFactory.decodeResource(getResources(),R.drawable.golfball);
        mBitmap= Bitmap.createScaledBitmap(ball,BALL_SIZE,BALL_SIZE,true);

        Bitmap hole= BitmapFactory.decodeResource(getResources(),R.drawable.hole);
        mHole= Bitmap.createScaledBitmap(hole,HOLE_SIZE,HOLE_SIZE,true);

        Options opts= new Options();
        opts.inDither= true;
        opts.inPreferredConfig=Bitmap.Config.RGB_565;
        mGrass=BitmapFactory.decodeResource(getResources(),R.drawable.grass,opts);
    }

    public void startSimulation(){
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
    }

    public void stopSimulation(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()!=Sensor.TYPE_ACCELEROMETER)
            return;

        switch(mDisplay.getRotation()){

            case Surface.ROTATION_0:
                mSensorX=event.values[0];
                mSensorY=event.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX=-event.values[1];
                mSensorY=event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX=-event.values[0];
                mSensorY=-event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX=event.values[1];
                mSensorY=-event.values[0];
                break;
        }
        mSensorZ=event.values[2];
        mSensorTimeStamp=event.timestamp;



    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        mXOrigins=w*0.5f;
        mYorigin=h*0.5f;

        mHorizontalBound=(w-BALL_SIZE)*0.5f;
        mVerticalBound=(h-BALL_SIZE)*0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(mGrass,0,0,null);
        canvas.drawBitmap(mHole,mXOrigins - HOLE_SIZE/2,mYorigin - HOLE_SIZE/2,null);

        mBall.updatePosition(mSensorX,mSensorY,mSensorZ,mSensorTimeStamp);

        mBall.resolveCollisionWithBounds(mHorizontalBound,mVerticalBound);

        canvas.drawBitmap(mBitmap,(mXOrigins - BALL_SIZE/2)+mBall.mPosX,(mYorigin - BALL_SIZE/2)
        -mBall.mPosY,null);

        if(mBall.mPosX>mXOrigins - HOLE_SIZE&&mBall.mPosX<mXOrigins+HOLE_SIZE)
          if(mBall.mPosY>mYorigin - HOLE_SIZE&&mBall.mPosY<mYorigin+HOLE_SIZE)
              Toast.makeText(this.getContext(),"You WON!!!",Toast.LENGTH_LONG).show();

        invalidate();
    }

    }


