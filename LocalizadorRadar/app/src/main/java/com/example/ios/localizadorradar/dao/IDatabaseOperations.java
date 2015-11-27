package com.example.ios.localizadorradar.dao;

import android.location.Location;

import java.util.List;

/**
 * Created by Mark on 25/11/2015.
 */
public interface IDatabaseOperations<T> {
    public T find(int id);
    public List<T> findAllByRadius(float Radius, Location posicaoAtual);
}
