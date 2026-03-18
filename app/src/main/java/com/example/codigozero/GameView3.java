package com.example.codigozero;

import android.app.Activity;
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
import java.util.ArrayList;
import java.util.List;

public class GameView3 extends SurfaceView implements Runnable {

    private Thread hiloJuego;
    private boolean estaJugando;
    private SurfaceHolder holder;
    private Paint paintTexto;

    private float naveX, naveY;
    private Bitmap imagenAvatar;
    private int carreraActual = 0;

    private Bitmap imagenObstaculo;
    private List<Obstaculo> listaObstaculos;

    private int puntos = 0;
    private int vidas = 3;
    private boolean juegoGanado = false;

    public GameView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paintTexto = new Paint();
        listaObstaculos = new ArrayList<>();

        Bitmap originalAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_ti);
        escalarNave(originalAvatar);

        // Usamos una imagen diferente para los obstáculos del nivel 2
        Bitmap obsTemporal = BitmapFactory.decodeResource(getResources(), R.drawable.ti2);
        float targetWidthObstaculo = 160f;
        float aspect = (float) obsTemporal.getHeight() / obsTemporal.getWidth();
        imagenObstaculo = Bitmap.createScaledBitmap(obsTemporal, (int)targetWidthObstaculo, (int)(targetWidthObstaculo * aspect), false);
        obsTemporal.recycle();

        naveX = 400;
        naveY = 1000;
    }

    public void setSkinYCarrera(int carreraElegida) {
        this.carreraActual = carreraElegida;
        int recursoImagen = R.drawable.avatar_ti;
        if (carreraActual == 1) recursoImagen = R.drawable.avatar_financiera;
        else if (carreraActual == 2) recursoImagen = R.drawable.avatar_bio;

        Bitmap nuevaSkin = BitmapFactory.decodeResource(getResources(), recursoImagen);
        escalarNave(nuevaSkin);
    }

    private void escalarNave(Bitmap original) {
        float maxTamanoNave = 150f;
        float aspectRatio = (float) original.getHeight() / original.getWidth();
        imagenAvatar = Bitmap.createScaledBitmap(original, (int) maxTamanoNave, (int) (maxTamanoNave * aspectRatio), false);
        original.recycle();
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
        if (vidas <= 0 || juegoGanado) return;

        // NIVEL 2: Más rápido y más enemigos (0.05 de probabilidad)
        if (getWidth() > 0 && Math.random() < 0.05) {
            float posXAleatoria = (float) (Math.random() * (getWidth() - imagenObstaculo.getWidth()));
            float velocidadAleatoria = 15f + (float) (Math.random() * 18); // Caen más rápido
            listaObstaculos.add(new Obstaculo(posXAleatoria, -100f, velocidadAleatoria));
        }

        RectF rectAvatar = new RectF(naveX, naveY, naveX + imagenAvatar.getWidth(), naveY + imagenAvatar.getHeight());

        for (int i = 0; i < listaObstaculos.size(); i++) {
            Obstaculo obs = listaObstaculos.get(i);
            obs.y = obs.y + obs.velocidad;
            RectF rectObs = new RectF(obs.x, obs.y, obs.x + imagenObstaculo.getWidth(), obs.y + imagenObstaculo.getHeight());

            if (RectF.intersects(rectAvatar, rectObs)) {
                vidas--;
                listaObstaculos.remove(i);
                i--;
                continue;
            }

            if (obs.y > getHeight()) {
                puntos += 10;
                listaObstaculos.remove(i);
                i--;
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
            // Fondo Azul Oscuro para el Nivel 2
            canvas.drawColor(Color.parseColor("#001F3F"));

            paintTexto.setColor(Color.parseColor("#00FF41"));
            paintTexto.setTextSize(50);
            canvas.drawText("NIVEL 2: T.S.U.", 50, 100, paintTexto);

            paintTexto.setColor(Color.WHITE);
            paintTexto.setTextSize(40);
            canvas.drawText("Puntos: " + puntos, 50, 160, paintTexto);

            if (vidas == 1) paintTexto.setColor(Color.RED);
            canvas.drawText("Vidas: " + vidas, 50, 220, paintTexto);

            if (vidas <= 0) {
                paintTexto.setColor(Color.RED);
                paintTexto.setTextSize(100);
                canvas.drawText("REPROBADO", canvas.getWidth()/2f - 300, canvas.getHeight()/2f, paintTexto);
            } else if (juegoGanado) {
                paintTexto.setColor(Color.parseColor("#00FF41"));
                paintTexto.setTextSize(80);
                canvas.drawText("¡CICLO COMPLETADO!", canvas.getWidth()/2f - 400, canvas.getHeight()/2f, paintTexto);
            } else {
                canvas.drawBitmap(imagenAvatar, naveX, naveY, null);
                for (Obstaculo obs : listaObstaculos) canvas.drawBitmap(imagenObstaculo, obs.x, obs.y, null);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (vidas > 0 && !juegoGanado && (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)) {
            naveX = event.getX() - (imagenAvatar.getWidth() / 2f);
            naveY = event.getY() - (imagenAvatar.getHeight() / 2f);
        }
        if (vidas <= 0 && event.getAction() == MotionEvent.ACTION_DOWN) {
            vidas = 3; puntos = 0; listaObstaculos.clear();
        }
        return true;
    }

    private void controlarFrames() {
        try { Thread.sleep(17); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void reanudar() { estaJugando = true; hiloJuego = new Thread(this); hiloJuego.start(); }
    public void pausar() { try { estaJugando = false; hiloJuego.join(); } catch (InterruptedException e) { e.printStackTrace(); } }

    private void avanzarASiguientePantalla() {
        estaJugando = false;
        post(new Runnable() {
            @Override
            public void run() {
                // Al ganar, vamos a la pantalla del T.S.U.
                Intent intent = new Intent(getContext(), ResumenDosActivity.class);
                intent.putExtra("CARRERA_SELECCIONADA", carreraActual);
                getContext().startActivity(intent);
                if (getContext() instanceof Activity) { ((Activity) getContext()).finish(); }
            }
        });
    }

    private class Obstaculo {
        float x, y, velocidad;
        public Obstaculo(float startX, float startY, float vel) { x = startX; y = startY; velocidad = vel; }
    }
}