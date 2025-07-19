package com.unal.micasaya.ui.calculadora.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PlanoEstructuralView extends View {

    private int columnasFila;
    private int columnasColumna;
    private Paint paintColumna;
    private Paint paintViga;

    public PlanoEstructuralView(Context context, int columnasFila, int columnasColumna) {
        super(context);
        this.columnasFila = columnasFila;
        this.columnasColumna = columnasColumna;

        // Inicializar los pinceles (pinturas)
        paintColumna = new Paint();
        paintColumna.setColor(Color.BLUE);
        paintColumna.setStyle(Paint.Style.FILL);

        paintViga = new Paint();
        paintViga.setColor(Color.DKGRAY);
        paintViga.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        float espacioX = (float) width / (columnasFila + 1);
        float espacioY = (float) height / (columnasColumna + 1);

        for (int i = 0; i < columnasColumna; i++) {
            for (int j = 0; j < columnasFila; j++) {
                float x = espacioX * (j + 1);
                float y = espacioY * (i + 1);

                // Dibuja columna como un círculo azul
                canvas.drawCircle(x, y, 10, paintColumna);

                // Dibuja viga horizontal hacia la derecha
                if (j < columnasFila - 1) {
                    float x2 = espacioX * (j + 2);
                    canvas.drawLine(x, y, x2, y, paintViga);
                }

                // Dibuja viga vertical hacia abajo
                if (i < columnasColumna - 1) {
                    float y2 = espacioY * (i + 2);
                    canvas.drawLine(x, y, x, y2, paintViga);
                }
            }
        }
    }
}
