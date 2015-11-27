package com.example.ios.localizadorradar.dao;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;

import com.example.ios.localizadorradar.model.Radar;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 25/11/2015.
 */
public class RadarDAO extends GenericDAO<Radar> {

    private static final String TABLE_NAME = "radares";

    public RadarDAO(Context context) {
        super(context);
    }

    @Override
    public Radar find(int id) {
        return null;
    }

    @Override
    public List<Radar> findAllByRadius(float radius, Location posicaoAtual) {

        List<Radar> radares = new ArrayList<>();
        Location radarLocation = new Location(LocationManager.GPS_PROVIDER);

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "_id");

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            for (int  i = 0; i < cursor.getCount(); i++){
                cursor.moveToPosition(i);

                Radar radar = new Radar();

                radar.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
                radar.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
                radar.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                radar.setVelocidade(cursor.getInt(cursor.getColumnIndex("velocidade")));

                radarLocation.setLatitude(radar.getLatitude());
                radarLocation.setLongitude(radar.getLongitude());

                float distance = posicaoAtual.distanceTo(radarLocation);

                if ((distance / 1000) <= radius) {
                    radares.add(radar);
                }
            }

        }

        return radares;
    }
}
