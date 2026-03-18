package com.example.codigozero; // <-- REVISA TU PAQUETE

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

public class GameView4 extends SurfaceView implements Runnable {

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

    public GameView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paintTexto = new Paint();
        listaObstaculos = new ArrayList<>();

        Bitmap originalAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_ti);
        escalarNave(originalAvatar);

        // Usamos una imagen genérica para el reto final (puedes cambiarla después por un "bug" o "tesis")
        Bitmap obsTemporal = BitmapFactory.decodeResource(getResources(), R.drawable.mecatronica);
        float targetWidthObstaculo = 170f;
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

        // NIVEL 3 (FINAL): Probabilidad alta (0.06) y velocidad extrema (hasta 28f)
        if (getWidth() > 0 && Math.random() < 0.06) {
            float posXAleatoria = (float) (Math.random() * (getWidth() - imagenObstaculo.getWidth()));
            float velocidadAleatoria = 18f + (float) (Math.random() * 10);
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
            // Fondo Rojo Oscuro "Nivel Jefe Final"
            canvas.drawColor(Color.parseColor("#2A0808"));

            paintTexto.setColor(Color.parseColor("#FFD700")); // Texto dorado
            paintTexto.setTextSize(50);
            canvas.drawText("NIVEL FINAL: TITULACIÓN", 50, 100, paintTexto);

            paintTexto.setColor(Color.WHITE);
            paintTexto.setTextSize(40);
            canvas.drawText("Puntos: " + puntos + "/200", 50, 160, paintTexto);

            if (vidas == 1) paintTexto.setColor(Color.RED);
            canvas.drawText("Vidas: " + vidas, 50, 220, paintTexto);

            if (vidas <= 0) {
                paintTexto.setColor(Color.RED);
                paintTexto.setTextSize(100);
                canvas.drawText("PROYECTO RECHAZADO", canvas.getWidth()/2f - 480, canvas.getHeight()/2f, paintTexto);
            } else if (juegoGanado) {
                paintTexto.setColor(Color.parseColor("#FFD700"));
                paintTexto.setTextSize(80);
                canvas.drawText("¡PROYECTO APROBADO!", canvas.getWidth()/2f - 420, canvas.getHeight()/2f, paintTexto);
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
                // Viajamos a la pantalla de Graduación Final
                Intent intent = new Intent(getContext(), ResumenTresActivity.class);
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