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

public class GameView extends SurfaceView implements Runnable {

    private Thread hiloJuego;
    private boolean estaJugando;
    private SurfaceHolder holder;

    private Paint paintTexto;

    // Variables de nuestra nave (El Búho / Avatar)
    private float naveX;
    private float naveY;
    private Bitmap imagenBuho;

    // NUEVO: Variable para saber qué carrera estamos jugando
    private int carreraActual = 0;

    // Listas para los obstáculos
    private List<Bitmap> listaImagenesCarreras;
    private List<Obstaculo> listaObstaculos;

    // Variables de Puntos y Vidas
    private int puntos = 0;
    private int vidas = 3;
    private boolean juegoGanado = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paintTexto = new Paint();
        listaObstaculos = new ArrayList<>();

        // Cargamos una skin por defecto (luego se cambiará automáticamente con setSkinYCarrera)
        Bitmap originalBuho = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_ti);
        escalarNave(originalBuho);

        // --- PRE-CARGAMOS LAS IMÁGENES DE LOS OBSTÁCULOS (CARRERAS) ---
        listaImagenesCarreras = new ArrayList<>();
        float targetWidthObstaculo = 400f; // Tamaño de los obstáculos que caen
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

        // --- POSICIÓN INICIAL DE TU AVATAR ---
        naveX = 400;
        naveY = 1000;
    }

    // =========================================================================
    // NUEVO MÉTODO: Recibe la carrera desde NivelUnoActivity y cambia la imagen
    // =========================================================================
    public void setSkinYCarrera(int carreraElegida) {
        this.carreraActual = carreraElegida;
        int recursoImagen = R.drawable.avatar_ti; // Skin por defecto (TI)

        if (carreraActual == 1) {
            recursoImagen = R.drawable.avatar_financiera;
        } else if (carreraActual == 2) {
            recursoImagen = R.drawable.avatar_bio;
        }

        // Cargamos y escalamos la nueva skin elegida
        Bitmap nuevaSkin = BitmapFactory.decodeResource(getResources(), recursoImagen);
        escalarNave(nuevaSkin);
    }

    // Método auxiliar para no repetir código al escalar la nave
    private void escalarNave(Bitmap original) {
        float maxTamanoNave = 150f;
        float aspectRatio = (float) original.getHeight() / original.getWidth();
        int nuevoAncho = (int) maxTamanoNave;
        int nuevoAlto = (int) (maxTamanoNave * aspectRatio);
        imagenBuho = Bitmap.createScaledBitmap(original, nuevoAncho, nuevoAlto, false);
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
        // Si perdimos o ganamos, el juego se detiene
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

            // 3. Si el obstáculo sale por abajo sin tocarnos, ganamos puntos
            if (obs.y > getHeight()) {
                puntos += 10;
                listaObstaculos.remove(i);
                i--;

                // COMPROBAMOS SI LLEGAMOS A 200 PUNTOS (VICTORIA)
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

            // Fondo oscuro
            canvas.drawColor(Color.parseColor("#0A0A12"));

            // Textos del HUD
            paintTexto.setColor(Color.parseColor("#00FF41"));
            paintTexto.setTextSize(50);
            canvas.drawText("NIVEL 1", 50, 100, paintTexto);

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
                canvas.drawText("¡SISTEMA HACKEADO!", canvas.getWidth()/2f - 400, canvas.getHeight()/2f, paintTexto);
            } else {
                // Dibujamos al avatar y a los obstáculos
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

    // =========================================================================
    // VIAJE A LA PANTALLA DE LOGROS
    // =========================================================================
    private void avanzarASiguientePantalla() {
        estaJugando = false; // Detenemos el motor gráfico

        post(new Runnable() {
            @Override
            public void run() {
                // Viajamos a la pantalla de ResumenActivity
                Intent intent = new Intent(getContext(), ResumenActivity.class);

                // MANDAMOS LA CARRERA ACTUAL para que la pantalla de resumen sepa qué materias mostrar
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
        Bitmap imagen;

        public Obstaculo(float startX, float startY, float vel, Bitmap bmp) {
            x = startX;
            y = startY;
            velocidad = vel;
            imagen = bmp;
        }
    }
}