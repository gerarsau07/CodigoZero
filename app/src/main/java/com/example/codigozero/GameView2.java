package com.example.codigozero; // <-- REVISA QUE ESTE SEA TU PAQUETE

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

public class GameView2 extends SurfaceView implements Runnable {

    private Thread hiloJuego;
    private boolean estaJugando;
    private SurfaceHolder holder;

    private Paint paintTexto;

    // Variables de nuestra nave (El Avatar Personalizado)
    private float naveX;
    private float naveY;
    private Bitmap imagenAvatar;

    // Guardará la carrera que estamos jugando para enviarla a los Logros
    private int carreraActual = 0;

    // Lista de obstáculos (enemigos genéricos del Nivel 1)
    private Bitmap imagenObstaculo;
    private List<Obstaculo> listaObstaculos;

    // Variables de Puntos y Vidas
    private int puntos = 0;
    private int vidas = 3;
    private boolean juegoGanado = false;

    public GameView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paintTexto = new Paint();
        listaObstaculos = new ArrayList<>();

        // 1. Cargamos una skin por defecto (luego se cambiará automáticamente desde NivelUnoActivity)
        Bitmap originalAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_ti);
        escalarNave(originalAvatar);

        // 2. Cargamos la imagen de los obstáculos para el NIVEL 1
        // (Temporalmente usaré el logo de biotecnología, luego puedes cambiarlo por un virus o enemigo)
        Bitmap obsTemporal = BitmapFactory.decodeResource(getResources(), R.drawable.biotecnologia);
        float targetWidthObstaculo = 150f; // Los obstáculos del nivel 1 son más pequeños
        float aspect = (float) obsTemporal.getHeight() / obsTemporal.getWidth();
        imagenObstaculo = Bitmap.createScaledBitmap(obsTemporal, (int)targetWidthObstaculo, (int)(targetWidthObstaculo * aspect), false);
        obsTemporal.recycle();

        // 3. Posición Inicial
        naveX = 400;
        naveY = 1000;
    }

    // =========================================================================
    // MÉTODO: Recibe la carrera desde NivelUnoActivity y cambia la imagen
    // =========================================================================
    public void setSkinYCarrera(int carreraElegida) {
        this.carreraActual = carreraElegida;
        int recursoImagen = R.drawable.avatar_ti; // Skin por defecto (TI)

        if (carreraActual == 1) {
            recursoImagen = R.drawable.avatar_financiera;
        } else if (carreraActual == 2) {
            recursoImagen = R.drawable.avatar_bio;
        }

        Bitmap nuevaSkin = BitmapFactory.decodeResource(getResources(), recursoImagen);
        escalarNave(nuevaSkin);
    }

    private void escalarNave(Bitmap original) {
        float maxTamanoNave = 150f;
        float aspectRatio = (float) original.getHeight() / original.getWidth();
        int nuevoAncho = (int) maxTamanoNave;
        int nuevoAlto = (int) (maxTamanoNave * aspectRatio);
        imagenAvatar = Bitmap.createScaledBitmap(original, nuevoAncho, nuevoAlto, false);
        original.recycle();
    }
    // =========================================================================

    @Override
    public void run() {
        while (estaJugando) {
            actualizar();
            dibujar();
            controlarFrames();
        }
    }

    private void actualizar() {
        if (vidas <= 0 || juegoGanado) {
            return;
        }

        // 1. Generar nuevos obstáculos (Solo usamos una imagen para todos en este nivel)
        if (getWidth() > 0 && Math.random() < 0.04) { // Ligeramente más rápido que el nivel 0
            float posXAleatoria = (float) (Math.random() * (getWidth() - imagenObstaculo.getWidth()));
            float velocidadAleatoria = 12f + (float) (Math.random() * 15);
            listaObstaculos.add(new Obstaculo(posXAleatoria, -100f, velocidadAleatoria));
        }

        RectF rectAvatar = new RectF(naveX, naveY, naveX + imagenAvatar.getWidth(), naveY + imagenAvatar.getHeight());

        // 2. Mover obstáculos y detectar choques
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

            // 3. Ganar puntos
            if (obs.y > getHeight()) {
                puntos += 10;
                listaObstaculos.remove(i);
                i--;

                // VICTORIA DEL NIVEL 1
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

            // Fondo diferente para el Nivel 1 (Morado oscuro)
            canvas.drawColor(Color.parseColor("#1A0A22"));

            // Textos del HUD
            paintTexto.setColor(Color.parseColor("#00FF41"));
            paintTexto.setTextSize(50);
            canvas.drawText("NIVEL 1: ESPECIALIDAD", 50, 100, paintTexto);

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
                paintTexto.setColor(Color.parseColor("#00FF41"));
                paintTexto.setTextSize(90);
                canvas.drawText("¡RUTAS DESBLOQUEADAS!", canvas.getWidth()/2f - 500, canvas.getHeight()/2f, paintTexto);
            } else {
                // Dibujamos
                canvas.drawBitmap(imagenAvatar, naveX, naveY, null);
                for (Obstaculo obs : listaObstaculos) {
                    canvas.drawBitmap(imagenObstaculo, obs.x, obs.y, null);
                }
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

    // =========================================================================
    // VIAJE A LA PANTALLA DE LOGROS
    // =========================================================================
    private void avanzarASiguientePantalla() {
        estaJugando = false;

        post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), ResumenActivity.class);
                intent.putExtra("CARRERA_SELECCIONADA", carreraActual);
                getContext().startActivity(intent);

                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    private class Obstaculo {
        float x, y;
        float velocidad;

        public Obstaculo(float startX, float startY, float vel) {
            x = startX;
            y = startY;
            velocidad = vel;
        }
    }
}