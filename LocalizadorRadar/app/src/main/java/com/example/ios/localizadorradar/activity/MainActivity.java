package com.localizadorradar;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.localizadorradar.dao.RadarDAO;
import com.localizadorradar.model.Radar;

import java.util.List;

public class MainActivity extends Activity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap map;
    private LocationManager locationManager;
    private RadarDAO radarDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.radarDAO = new RadarDAO(this);

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //findViewById(R.id.btnRadius).setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                TextView progressBar = (TextView) findViewById(R.id.seekBarProgress);
                progressBar.setText(""+progresValue);
                //getMap();

                Location location = map.getMyLocation();

                if (map != null){
                    LatLng actualPosition = new LatLng(location.getLatitude() , location.getLongitude());


                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(actualPosition)
                            .zoom(13)
                            .bearing(90)
                            .tilt(30)
                            .build();

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    SeekBar sp = (SeekBar) findViewById(R.id.seekBar);
                    List<Radar> radares = radarDAO.findAllByRadius(sp.getProgress(), location);
                    map.clear();
                    for(Radar r : radares){
                        String nomeImagen = getNomeImagem(r);
                        int idImage = getResources().getIdentifier(nomeImagen, "drawable", MainActivity.this.getPackageName());
                        map.addMarker(new MarkerOptions().position(new LatLng(r.getLatitude(),r.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), idImage))));
                    }

                    Circle circle = map.addCircle(new CircleOptions()
                            .center(actualPosition)
                            .radius(progresValue*1000)
                            .strokeColor(Color.RED));

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }

        });
    }



    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setMyLocationEnabled(true);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Log.i("App:", location.getLatitude() + ":" + location.getLongitude());
        if (location != null){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(13)
                    .bearing(90)
                    .tilt(30)
                    .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    private String getNomeImagem(Radar r){
        String nomeImagen = null;
        switch (r.getTipo()){
            case "Radar Fixo":
                return "fixo" + r.getVelocidade();
            case "Radar Movel":
                return "movel" + r.getVelocidade();
            case "Semaforo com Camera":
                return "semaforo";
            case "Semaforo com Radar":
                return "semaforo" + r.getVelocidade();
            case "Pedagio":
                return "pedagio";
            case "Policia Rodoviaria":
                return "prf";
            default:
                return nomeImagen;
        }
    }

    @Override
    public void onClick(View v) {
        //if(((EditText)findViewById(R.id.editTextRadius)).getText().toString().equals(""))
          //  return;
/*
        switch (v.getId()){
            case R.id.btnRadius:
                getMap();
        }
        */
    }

    private void getMap(){
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (map != null){
                    LatLng actualPosition = new LatLng(location.getLatitude() , location.getLongitude());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(actualPosition)
                            .zoom(13)
                            .bearing(90)
                            .tilt(30)
                            .build();

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    SeekBar sp = (SeekBar) findViewById(R.id.seekBar);
                    List<Radar> radares = radarDAO.findAllByRadius(sp.getProgress(), location);
                    map.clear();
                    for(Radar r : radares){
                        String nomeImagen = getNomeImagem(r);
                        int idImage = getResources().getIdentifier(nomeImagen, "drawable", MainActivity.this.getPackageName());
                        map.addMarker(new MarkerOptions().position(new LatLng(r.getLatitude(),r.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), idImage))));
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, locationListener);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        Log.i("haha","hehe");
    }
}
