package com.example.codigozero;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread hiloJuego;
    private boolean estaJugando;
    private SurfaceHolder holder;
    private Paint paintTexto;
    private float naveX;
    private float naveY;
    private Bitmap imagenBuho;

    private List<Bitmap> listaImagenesCarreras;
    private List<Obstaculo> listaObstaculos;
    private int puntos = 0;
    private int vidas = 3;
    private boolean juegoGanado = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paintTexto = new Paint();
        listaObstaculos = new ArrayList<>();

        // --- CARGAMOS LA IMAGEN DEL BÚHO ---
        Bitmap originalBuho = BitmapFactory.decodeResource(getResources(), R.drawable.buho_nave);
        float maxTamanoNave = 150f;
        float aspectRatio = (float) originalBuho.getHeight() / originalBuho.getWidth();
        int nuevoAncho = (int) maxTamanoNave;
        int nuevoAlto = (int) (maxTamanoNave * aspectRatio);
        imagenBuho = Bitmap.createScaledBitmap(originalBuho, nuevoAncho, nuevoAlto, false);
        originalBuho.recycle();

        // --- PRE-CARGAMOS LAS IMÁGENES DE CARRERAS ---
        listaImagenesCarreras = new ArrayList<>();
        float targetWidthObstaculo = 400f;
        int[] drawableCarrerasIds = {
                R.drawable.mecatronica,
                R.drawable.biotecnologia,
                R.drawable.automotriz,
                R.drawable.ti2,
                R.drawable.industrial,
                R.drawable.financiera
        };

        for (int drawableId : drawableCarrerasIds) {
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            if (originalBitmap != null) {
                float aspect = (float) originalBitmap.getHeight() / originalBitmap.getWidth();
                int width = (int) targetWidthObstaculo;
                int height = (int) (targetWidthObstaculo * aspect);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
                listaImagenesCarreras.add(scaledBitmap);
                originalBitmap.recycle();
            }
        }

        // --- POSICIÓN INICIAL ---
        naveX = 400;
        naveY = 1000;
    }

    @Override
    public void run() {
        while (estaJugando) {
            actualizar();
            dibujar();
            controlarFrames();
        }
    }

    private void actualizar() {
        // NUEVO: Si no tenemos vidas o ya ganamos, el juego se detiene
        if (vidas <= 0 || juegoGanado) {
            return;
        }

        // 1. Generar nuevos obstáculos
        if (getWidth() > 0 && Math.random() < 0.03 && !listaImagenesCarreras.isEmpty()) {
            float posXAleatoria = (float) (Math.random() * (getWidth() - 100));
            float velocidadAleatoria = 10f + (float) (Math.random() * 15);
            int indiceAleatorio = (int) (Math.random() * listaImagenesCarreras.size());
            Bitmap imagenCarrera = listaImagenesCarreras.get(indiceAleatorio);
            listaObstaculos.add(new Obstaculo(posXAleatoria, -100f, velocidadAleatoria, imagenCarrera));
        }

        RectF rectBuho = new RectF(naveX, naveY, naveX + imagenBuho.getWidth(), naveY + imagenBuho.getHeight());

        // 2. Mover obstáculos y detectar choques
        for (int i = 0; i < listaObstaculos.size(); i++) {
            Obstaculo obs = listaObstaculos.get(i);
            obs.y = obs.y + obs.velocidad;

            RectF rectObs = new RectF(obs.x, obs.y, obs.x + obs.imagen.getWidth(), obs.y + obs.imagen.getHeight());

            if (RectF.intersects(rectBuho, rectObs)) {
                vidas--;
                listaObstaculos.remove(i);
                i--;
                continue;
            }

            // 3. Si el obstáculo sale por abajo, ganamos puntos
            if (obs.y > getHeight()) {
                puntos += 10;
                listaObstaculos.remove(i);
                i--;

                // NUEVO: COMPROBAMOS SI LLEGAMOS A 200 PUNTOS
                if (puntos >= 200) {
                    juegoGanado = true;
                    avanzarASiguientePantalla();
                }
            }
        }
    }

    private void dibujar() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();

            canvas.drawColor(Color.parseColor("#0A0A12"));

            paintTexto.setColor(Color.parseColor("#00FF41"));
            paintTexto.setTextSize(50);
            canvas.drawText("NIVEL 0", 50, 100, paintTexto);

            paintTexto.setColor(Color.WHITE);
            paintTexto.setTextSize(40);
            canvas.drawText("Puntos: " + puntos, 50, 160, paintTexto);

            if (vidas == 1) paintTexto.setColor(Color.RED);
            canvas.drawText("Vidas: " + vidas, 50, 220, paintTexto);

            if (vidas <= 0) {
                paintTexto.setColor(Color.RED);
                paintTexto.setTextSize(100);
                canvas.drawText("MISIÓN FALLIDA", canvas.getWidth()/2f - 350, canvas.getHeight()/2f, paintTexto);
            } else if (juegoGanado) {
                // NUEVO: Mensaje de victoria rápido antes de cambiar de pantalla
                paintTexto.setColor(Color.parseColor("#00FF41"));
                paintTexto.setTextSize(90);
                canvas.drawText("¡SISTEMA HACKEADO!", canvas.getWidth()/2f - 400, canvas.getHeight()/2f, paintTexto);
            } else {
                canvas.drawBitmap(imagenBuho, naveX, naveY, null);
                for (Obstaculo obs : listaObstaculos) {
                    canvas.drawBitmap(obs.imagen, obs.x, obs.y, null);
                }
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (vidas > 0 && !juegoGanado && (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)) {
            naveX = event.getX() - (imagenBuho.getWidth() / 2f);
            naveY = event.getY() - (imagenBuho.getHeight() / 2f);
        }

        if (vidas <= 0 && event.getAction() == MotionEvent.ACTION_DOWN) {
            vidas = 3;
            puntos = 0;
            listaObstaculos.clear();
        }
        return true;
    }

    private void controlarFrames() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reanudar() {
        estaJugando = true;
        hiloJuego = new Thread(this);
        hiloJuego.start();
    }

    public void pausar() {
        try {
            estaJugando = false;
            hiloJuego.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // NUEVO: Método para cambiar de Activity de forma segura desde el hilo secundario
    private void avanzarASiguientePantalla() {
        estaJugando = false; // Detenemos el motor gráfico

        // Usamos post() para enviar la acción al hilo principal de la interfaz
        post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), Seleccion.class);
                getContext().startActivity(intent);

                // Cerramos este nivel para que el botón "Atrás" del celular no regrese al juego
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    private class Obstaculo {
        float x, y;
        float velocidad;
        Bitmap imagen;

        public Obstaculo(float startX, float startY, float vel, Bitmap bmp) {
            x = startX;
            y = startY;
            velocidad = vel;
            imagen = bmp;
        }
    }
}