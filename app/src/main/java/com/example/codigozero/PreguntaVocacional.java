package com.example.codigozero; // Revisa que sea tu paquete

public class PreguntaVocacional {

    // Lo que el usuario lee en la tarjeta
    private String textoPregunta;

    // Los puntos ocultos para cada carrera (0 si no tiene nada que ver, 10 si es muy afín)
    private int ptsMecatronica;
    private int ptsBiotecnologia;
    private int ptsAutomotriz;
    private int ptsTIs;
    private int ptsIndustrial;
    private int ptsFinanciera;
    private int ptsElectronica;

    // Constructor para armar la carta fácilmente
    public PreguntaVocacional(String textoPregunta, int meca, int bio, int auto, int tis, int ind, int fin, int elec) {
        this.textoPregunta = textoPregunta;
        this.ptsMecatronica = meca;
        this.ptsBiotecnologia = bio;
        this.ptsAutomotriz = auto;
        this.ptsTIs = tis;
        this.ptsIndustrial = ind;
        this.ptsFinanciera = fin;
        this.ptsElectronica = elec;
    }

    // Métodos para leer los valores (Getters)
    public String getTextoPregunta() { return textoPregunta; }
    public int getPtsMecatronica() { return ptsMecatronica; }
    public int getPtsBiotecnologia() { return ptsBiotecnologia; }
    public int getPtsAutomotriz() { return ptsAutomotriz; }
    public int getPtsTIs() { return ptsTIs; }
    public int getPtsIndustrial() { return ptsIndustrial; }
    public int getPtsFinanciera() { return ptsFinanciera; }
    public int getPtsElectronica() { return ptsElectronica; }
}