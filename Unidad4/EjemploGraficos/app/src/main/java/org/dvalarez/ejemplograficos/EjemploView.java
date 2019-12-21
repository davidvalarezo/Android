package org.dvalarez.ejemplograficos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.content.res.AppCompatResources;

public class EjemploView extends View {
    private Drawable miImagen;
    private ShapeDrawable miImagenShape;

   /* public EjemploView(Context context) {
        super(context);
        miImagen = AppCompatResources.getDrawable(context, R.drawable.ic_comet_icon);
        // miImagen.setBounds(30,30,200,200);
        miImagen.setBounds(30,30,1000, 1000);
        //this.setBackgroundResource(R.drawable.degradado);
        miImagenShape = new ShapeDrawable(new OvalShape());
        miImagenShape.getPaint().setColor(0xff0000ff);
        miImagenShape.setBounds(10, 10, 310, 60);
    }*/

    public EjemploView(Context context, AttributeSet attrs) {
        super(context, attrs);
        miImagenShape = new ShapeDrawable(new OvalShape());
        miImagenShape.getPaint().setColor(0xff0000ff);
    }

    @Override protected void onSizeChanged(int ancho, int alto,
                                           int ancho_anterior, int alto_anterior){
        miImagenShape.setBounds(0, 0, ancho, alto);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Path trazo = new Path();
        //trazo.addCircle(150, 150, 100, Path.Direction.CCW);
        /*Path trazo = new Path();
        trazo.moveTo(50, 100);
        trazo.cubicTo(60,70, 150,90, 200,110);
        trazo.lineTo(300,200);

        canvas.drawColor(Color.WHITE);
        Paint pincel = new Paint();
        pincel.setColor(Color.BLUE);
        pincel.setStrokeWidth(8);
        pincel.setStyle(Paint.Style.STROKE);
        canvas.drawPath(trazo, pincel);
        pincel.setStrokeWidth(1);
        pincel.setStyle(Paint.Style.FILL);
        pincel.setTextSize(20);
        pincel.setTypeface(Typeface.SANS_SERIF);
        canvas.drawTextOnPath("Desarrollo de aplicaciones para móviles con Android", trazo, 0, -40, pincel);
        miImagen.draw(canvas);*/
        miImagenShape.draw(canvas);

    }
}
