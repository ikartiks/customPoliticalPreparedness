package com.yrdtechnologies.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yrdtechnologies.R;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GifImageView extends View {

    Context context;
    private long mMovieStart;
    private Movie movie;
    private Paint p;
    private int width,height;

    String resourse;

    double movieWidth,movieHeight,factor,scaledMoveWidth,scaledMovieHeight;

    public GifImageView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        this.context = context;

        p = new Paint();
        p.setAntiAlias(true);
        // height=metrics.heightPixels;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GifImageView, 0, 0);
        try {
            resourse = a.getString(R.styleable.GifImageView_gifResourseId);
            // LoggerGeneral.e("res "+resourse);
			/*if (resourse != null) {
				initializeStreamFromXML(resourse);
			}*/
            invalidate();

        } finally {
            a.recycle();
        }
    }

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        p = new Paint();
        p.setAntiAlias(true);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GifImageView, 0, 0);
        try {
            resourse = a.getString(R.styleable.GifImageView_gifResourseId);
			/*if (resourse != null) {
				initializeStreamFromXML(resourse);
			}*/
            invalidate();

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //LoggerGeneral.e("onMeasure");

        if(isInEditMode()){

            setMeasuredDimension(widthMeasureSpec,widthMeasureSpec);
            return;
        }

        if(width==0){
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            //int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
            //int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);

            int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);

            if(widthSpecMode==MeasureSpec.EXACTLY){

                //xxdp or match parent
                //LoggerGeneral.e("width should be exact "+widthSpecSize);
                //my width is equal to hSpecSize i.e match parent
                width=widthSpecSize;

            }else{

                //wrapContent
                //LoggerGeneral.e("width should be wrapped "+widthSpecSize);
                width=widthSpecSize;
                //LoggerGeneral.e("width spec size"+widthSpecSize);
                //my width is equal to wrap_content
            }

            if (resourse != null) {
                initializeStreamFromXML(resourse);
            }

        }



        //LoggerGeneral.e("measure called"+width +"  "+height);
        setMeasuredDimension(width,height);
    }

    public GifImageView(Context context) {
        super(context);
        p = new Paint();
        p.setAntiAlias(true);
        invalidate();
        // height=metrics.heightPixels;
        this.context = context;
    }

    private void initializeStreamFromXML(String gifAssetName) {


        try {

            InputStream is = context.getAssets().open(gifAssetName);
            commonInitialization(is);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    boolean initializedOnce=false;
    public synchronized void  commonInitialization(InputStream is){

        if(initializedOnce)
            return;


        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(Integer.MAX_VALUE);
        // v.imp ,doesnt work without mark as well ,other wise we get
        // java.io.IOException: Mark has been invalidated.
        //LoggerGeneral.e("initializing..2");
        movie = Movie.decodeStream(bis);

        movieWidth=movie.width();
        movieHeight=movie.height();


        //LoggerGeneral.e("mw "+movieWidth+" noraml w"+width);
        if(movieWidth>width){

            factor = movieWidth / width;
            scaledMoveWidth = movieWidth / factor;
            scaledMovieHeight = movieHeight / factor;

            height=(int)scaledMovieHeight;
        }else if(movieWidth<width){

            factor = width / movieWidth;
            scaledMoveWidth = movieWidth * factor;
            scaledMovieHeight = movieHeight * factor;

            height=(int)scaledMovieHeight;
        }else{

            //LoggerGeneral.e("comes in else ");
            //doesnt matter if gif is bigger/smaller incase of wrap content than screen size
            scaledMoveWidth=movieWidth;
            scaledMovieHeight=movieHeight;

            height=(int)scaledMovieHeight;
        }

        initializedOnce=true;


        invalidate();
        requestLayout();
    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        if(isInEditMode()){

            Paint px=new Paint();
//            Bitmap bm= BitmapFactory.decodeResource(getResources(), R.drawable.loading);
//            canvas.drawBitmap(bm,0,0,px);

            //canvas.drawCircle(0,0,25f,px);
            canvas.drawText("gif preview",0,30f,px);

            invalidate();

        }else{
            //LoggerGeneral.e("ondraw 1");
            if (movie == null) {
                //LoggerGeneral.e("movie is null");
                canvas.drawPaint(p);
                return;
            }
            long now = android.os.SystemClock.uptimeMillis();
            if (mMovieStart == 0) { // first time
                mMovieStart = now;
            }
            if (movie != null) {

                int dur = movie.duration();
                if (dur == 0) {
                    dur = 1000;
                }
                int relTime = (int) ((now - mMovieStart) % dur);
                movie.setTime(relTime);

                Bitmap bm = Bitmap.createBitmap((int) movieWidth, (int) movieHeight, Bitmap.Config.ARGB_8888);

                Canvas c = new Canvas(bm);
                movie.draw(c, 0, 0);

                bm = Bitmap.createScaledBitmap(bm, (int) scaledMoveWidth, (int) scaledMovieHeight, true);
                canvas.drawBitmap(bm, new Matrix(), p);

                invalidate();
            }
        }

    }

}