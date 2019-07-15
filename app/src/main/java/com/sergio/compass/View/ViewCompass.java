package com.sergio.compass.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sergio.compass.R;

public class ViewCompass extends View {

    private Paint paintCompass, triangleBlack, triangleWhite, textPaint, anglePaint;
    int textPadding = 3, textSize = 16;
    int pixelPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            textPadding, getResources().getDisplayMetrics());
    int pixelTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            textSize, getResources().getDisplayMetrics());

    //********************CONSTRUCTORS*********************
    public ViewCompass(Context context) {
        super(context);
        init();
    }

    public ViewCompass(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewCompass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //********************INSTANCE METHODS*********************
    private void init() {

        paintCompass = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCompass.setColor(getResources().getColor(R.color.colorLightBlue));

        triangleBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        triangleBlack.setColor(Color.BLACK);

        triangleWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        triangleWhite.setColor(Color.WHITE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(pixelTextSize);

        anglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        anglePaint.setTextSize(pixelTextSize / 2.0f);
        anglePaint.setColor(Color.BLACK);
    }
    public void paintAngles(Canvas canvas, Point center, int radio) {

        double x;
        double y;
        int xCenter = center.x;
        int yCenter = center.y;

        int angleVariation = 20;
        for (int i = 0; i < 360; i += angleVariation) {

            if (i != 0 && i != 90 && i != 180 && i != 270) {


                double radians;

                radians = Math.toRadians(i);

                x = radio * Math.sin(radians) + xCenter;

                y = -1 * radio * Math.cos(radians) + yCenter;


                if (i < 90) {

                    x += pixelPadding;
                    y -= pixelPadding;

                }
                if (i > 90 && i < 180) {

                    x += pixelPadding;
                    y += pixelPadding * 2;

                } else if (i > 180 && i < 270) {

                    x -= pixelPadding * 3;
                    y += pixelPadding * 2;

                } else if (i > 270) {

                    x -= pixelPadding * 3;
                    y -= pixelPadding;
                }
                canvas.drawText(String.valueOf(i), (int) x, (int) y, anglePaint);
            }

        }

    }

    public void drawTriangle(Canvas canvas, Point a, Point b, Point c, Paint paint) {

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paint);
    }

    //********************OVERRIDE METHODS*********************
    @Override
    protected void onDraw(Canvas canvas) {


        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int radius = getWidth() / 2 - 100;

        canvas.drawCircle(x, y, radius, paintCompass);

        int z = radius / 8;

        @SuppressLint("DrawAllocation") Point center = new Point(x, y);

        @SuppressLint("DrawAllocation") Point north = new Point(x, y - radius);
        @SuppressLint("DrawAllocation") Point east = new Point(x + radius, y);
        @SuppressLint("DrawAllocation") Point west = new Point(x - radius, y);
        @SuppressLint("DrawAllocation") Point south = new Point(x, y + radius);

        @SuppressLint("DrawAllocation") Point ne = new Point(x + z, y - z);
        @SuppressLint("DrawAllocation") Point nw = new Point(x - z, y - z);
        @SuppressLint("DrawAllocation") Point se = new Point(x + z, y + z);
        @SuppressLint("DrawAllocation") Point sw = new Point(x - z, y + z);

        canvas.drawText("N", x - pixelTextSize / 3.0f, y - radius - pixelPadding, textPaint);
        drawTriangle(canvas, center, north, ne, triangleBlack);
        drawTriangle(canvas, center, north, nw, triangleWhite);

        canvas.drawText("S", x - pixelTextSize / 3.0f, y + radius + pixelTextSize + pixelPadding, textPaint);
        drawTriangle(canvas, center, south, sw, triangleBlack);
        drawTriangle(canvas, center, south, se, triangleWhite);

        canvas.drawText("E", x + radius + pixelPadding, y + pixelTextSize / 3.0f, textPaint);
        drawTriangle(canvas, center, east, se, triangleBlack);
        drawTriangle(canvas, center, east, ne, triangleWhite);

        canvas.drawText("W", x - radius - pixelTextSize - pixelPadding, y + pixelTextSize / 3.0f, textPaint);
        drawTriangle(canvas, center, west, nw, triangleBlack);
        drawTriangle(canvas, center, west, sw, triangleWhite);

        paintAngles(canvas, center, radius);
    }

}
