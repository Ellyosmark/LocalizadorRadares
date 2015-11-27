package com.example.ios.localizadorradar.model;

/**
 * Created by Mark on 25/11/2015.
 */
public class Radar {
    private float latitude;
    private float longitude;
    private String tipo;
    private int velocidade;

    public Radar() {

    }

    public Radar(float latitude, float longitude, String tipo, int velocidade) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipo = tipo;
        this.velocidade = velocidade;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }
}
