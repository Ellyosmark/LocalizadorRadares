package com.example.ios.localizadorradar.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.ios.localizadorradar.R;
import com.example.ios.localizadorradar.dao.RadarDAO;
import com.example.ios.localizadorradar.model.Radar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        findViewById(R.id.btnRadius).setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        
        if(((EditText)findViewById(R.id.editTextRadius)).getText().toString().equals(""))
            return;
        
        switch (v.getId()){
            case R.id.btnRadius:
                getMap();
        }
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
                    EditText editTextRadius = (EditText) findViewById(R.id.editTextRadius);
                    List<Radar> radares = radarDAO.findAllByRadius(Float.parseFloat(editTextRadius.getText().toString()), location);
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
    }
}
