package com.example.codigozero; // <-- REVISA QUE ESTE SEA TU PAQUETE

import android.content.Context;
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
    // private Paint paintEnemigo; // CAMBIO PARA CARRERAS: Ya no necesitamos este pincel rojo

    // Variables de nuestra nave (El Búho)
    private float naveX;
    private float naveY;
    private Bitmap imagenBuho; // La imagen ya escalada

    // --- CAMBIO PARA CARRERAS: NUEVAS VARIABLES PARA PRE-CARGAR LAS IMÁGENES ---
    private List<Bitmap> listaImagenesCarreras;

    // Lista de obstáculos
    private List<Obstaculo> listaObstaculos;

    // Variables de Puntos y Vidas
    private int puntos = 0;
    private int vidas = 3;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();

        paintTexto = new Paint();

        // paintEnemigo = new Paint(); // CAMBIO PARA CARRERAS: Borramos esto
        // paintEnemigo.setColor(Color.parseColor("#FF0044")); // Rojo para los obstáculos

        listaObstaculos = new ArrayList<>();

        // --- 1. CARGAMOS Y ESCALAMOS LA IMAGEN DEL BÚHO ---
        // Cargamos la imagen original gigante
        Bitmap originalBuho = BitmapFactory.decodeResource(getResources(), R.drawable.buho_nave);

        // Queremos que el búho mida 100 píxeles de ancho (como el cubo original)
        float maxTamanoNave = 200f;

        // Calculamos la proporción para que no se deforme
        float aspectRatio = (float) originalBuho.getHeight() / originalBuho.getWidth();
        int nuevoAncho = (int) maxTamanoNave;
        int nuevoAlto = (int) (maxTamanoNave * aspectRatio);

        // Escalamos la imagen. El 'false' al final mantiene los píxeles nítidos (estilo 8-bits)
        imagenBuho = Bitmap.createScaledBitmap(originalBuho, nuevoAncho, nuevoAlto, false);

        // Liberamos la memoria de la imagen original gigante
        originalBuho.recycle();

        // --- 2. CAMBIO PARA CARRERAS: PRE-CARGAR Y ESCALAR TODAS LAS IMÁGENES DE CARRERAS ---
        listaImagenesCarreras = new ArrayList<>();

        // Define el tamaño objetivo de los obstáculos (ej. 80 píxeles de ancho)
        float targetWidthObstaculo = 300f;

        // Lista de drawables.
        int[] drawableCarrerasIds = {
                R.drawable.mecatronica,
                R.drawable.biotecnologia,
                R.drawable.automotriz,
                R.drawable.ti,
                R.drawable.industrial,
                R.drawable.financiera,
                //R.drawable.electronica
        };

        // Iteramos a través de la lista de IDs, cargamos y escalamos cada imagen
        for (int drawableId : drawableCarrerasIds) {
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            if (originalBitmap != null) {
                // Calculamos proporción y nuevo tamaño
                float aspect = (float) originalBitmap.getHeight() / originalBitmap.getWidth();
                int width = (int) targetWidthObstaculo;
                int height = (int) (targetWidthObstaculo * aspect);

                // Escalamos (false para nitidez 8-bits)
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
                listaImagenesCarreras.add(scaledBitmap);

                // Liberamos la memoria de la imagen original gigante
                originalBitmap.recycle();
            }
        }

        // --- 3. POSICIÓN INICIAL ---
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
        // Si no tenemos vidas, el juego se detiene
        if (vidas <= 0) {
            return;
        }

        // 1. Generar nuevos obstáculos
        // Usamos la probabilidad del 3% y nos aseguramos de que haya imágenes cargadas
        if (getWidth() > 0 && Math.random() < 0.03 && !listaImagenesCarreras.isEmpty()) {
            float posXAleatoria = (float) (Math.random() * (getWidth() - 100));
            float velocidadAleatoria = 10f + (float) (Math.random() * 15);

            // --- CAMBIO PARA CARRERAS: ELEGIMOS UNA IMAGEN ALEATORIA ---
            int indiceAleatorio = (int) (Math.random() * listaImagenesCarreras.size());
            Bitmap imagenCarrera = listaImagenesCarreras.get(indiceAleatorio);

            // Creamos el obstáculo pasándole la imagen elegida
            listaObstaculos.add(new Obstaculo(posXAleatoria, -100f, velocidadAleatoria, imagenCarrera));
        }

        // Creamos el rectángulo de colisión usando el tamaño exacto del Búho ya escalado
        RectF rectBuho = new RectF(naveX, naveY, naveX + imagenBuho.getWidth(), naveY + imagenBuho.getHeight());

        // 2. Mover obstáculos y detectar choques
        for (int i = 0; i < listaObstaculos.size(); i++) {
            Obstaculo obs = listaObstaculos.get(i);
            obs.y = obs.y + obs.velocidad;

            // --- CAMBIO PARA CARRERAS: El rectángulo de colisión usa el tamaño real de la imagen de la carrera ---
            RectF rectObs = new RectF(obs.x, obs.y, obs.x + obs.imagen.getWidth(), obs.y + obs.imagen.getHeight());

            // ¿Chocaron?
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
            }
        }
    }

    private void dibujar() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();

            // Fondo oscuro cibernético
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

            // Si perdimos, mostramos GAME OVER
            if (vidas <= 0) {
                paintTexto.setColor(Color.RED);
                paintTexto.setTextSize(100);
                canvas.drawText("MISIÓN FALLIDA", canvas.getWidth()/2f - 350, canvas.getHeight()/2f, paintTexto);
            } else {
                // DIBUJAMOS LA IMAGEN DEL BÚHO
                canvas.drawBitmap(imagenBuho, naveX, naveY, null);

                // --- CAMBIO PARA CARRERAS: DIBUJAMOS LA IMAGEN DE LA CARRERA ---
                // Reemplazamos canvas.drawRect por canvas.drawBitmap
                for (Obstaculo obs : listaObstaculos) {
                    canvas.drawBitmap(obs.imagen, obs.x, obs.y, null);
                }
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Solo podemos movernos si seguimos vivos
        if (vidas > 0 && (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)) {
            // El centro del toque ahora se calcula con el ancho/alto de la imagen del búho
            naveX = event.getX() - (imagenBuho.getWidth() / 2f);
            naveY = event.getY() - (imagenBuho.getHeight() / 2f);
        }

        // Tocar la pantalla para reiniciar si perdimos
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

    // --- CAMBIO PARA CARRERAS: MODIFICAMOS LA CLASE INTERNA ---
    private class Obstaculo {
        float x, y;
        float velocidad;
        Bitmap imagen; // NUEVO: Ahora el obstáculo tiene su propia imagen

        // Constructor actualizado
        public Obstaculo(float startX, float startY, float vel, Bitmap bmp) {
            x = startX;
            y = startY;
            velocidad = vel;
            imagen = bmp;
        }
    }
}