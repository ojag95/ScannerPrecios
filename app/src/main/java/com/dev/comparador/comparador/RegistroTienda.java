package com.dev.comparador.comparador;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistroTienda extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener{
    int permiso=0;
    double latitud,longitud;
    LocationManager locManager;
    Location loc;
    Button btnRegistraTienda;
    LatLng ubicacion;
    Localizacion localiza;
    GoogleMap mMap;
    View view;
    Marker selector;
    EditText txtNombreTienda;
    String nombre,latitudString,longitudString,ubicacionMarcador;
    String url="http://18.216.102.240/Comparador/RegistroTienda.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setSubtitle("Arrastre para elegir la ubicacion de la tienda");
        btnRegistraTienda=(Button)findViewById(R.id.btnRegistrarTienda);
        btnRegistraTienda.setOnClickListener(this);
        txtNombreTienda=(EditText)findViewById(R.id.txtNombreTienda);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        view= this.getWindow().getDecorView().findViewById(android.R.id.content);

        ActivityCompat.requestPermissions(RegistroTienda.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            Snackbar.make(view, "No se han definido los permisos", Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }else {

            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            loc= locManager.getLastKnownLocation(locManager.getBestProvider(criteria, false));

            localiza = new Localizacion(getApplicationContext());
            localiza.getLocation();
            ubicacionMarcador= localiza.getAddressLine(getApplicationContext());
            latitud = localiza.getLatitude();
            longitud = localiza.getLongitude();

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        localiza.stopUsingGPS();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        ubicacion = new LatLng(latitud, longitud);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        mMap.setBuildingsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
        selector = mMap.addMarker(new MarkerOptions()
                .position(ubicacion)
                .draggable(true)
                .title(ubicacionMarcador)
                .snippet("Ubicacion Actual")



            );
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2500, null);

        selector.showInfoWindow();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

    }


    @Override
    public void onMapClick(LatLng latLng) {


    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        ubicacion=selector.getPosition();
        selector.hideInfoWindow();

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        ubicacionMarcador= localiza.getAddressLineLatitudeLongitude(getApplicationContext(),ubicacion.latitude,ubicacion.longitude);
        selector.setTitle(ubicacionMarcador);
        selector.showInfoWindow();


    }

    @Override
    public void onClick(View view) {
        validarInformacion();
        formatoInformacion();
    }
public void validarInformacion()
{
    nombre=txtNombreTienda.getText().toString();
    latitudString=""+ubicacion.latitude;
    longitudString=""+ubicacion.longitude;
}


    public void formatoInformacion() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombre", nombre);
            json.put("ubicacion", ubicacionMarcador);
            json.put("latitud", latitudString);
            json.put("longitud", longitudString);
        } catch (JSONException e) {
            Snackbar.make(view, "Error en el servidor, intente mas tarde"+e, Snackbar.LENGTH_LONG).show();
        }
        EnvioJSON envio = new EnvioJSON(getApplicationContext(),url, json, view);
        envio.enviar(new EnvioJSON.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    JSONObject jsonRespuesta= jsonarray.getJSONObject(0);
                    String respuesta=jsonRespuesta.getString("Mensaje");
                    if (respuesta.equals("Registro de tienda realizado exitosamente"))
                    {
                        Toast.makeText(getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }

            }
        });


    }


}

