package com.dev.comparador.comparador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComparadorPrecios extends AppCompatActivity {
    ListView lista;
    ArrayAdapter<String> adaptador;
    SearchView busquedad;
String nombre,apellidoP,apellidoM,correo,usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparador_precios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ComparadorPrecios.this,ScannerProductos.class);
                startActivity(intent);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtencion de datos del Activity Anterior
        String datos=getIntent().getExtras().getString("Datos");
        try {
            JSONArray jsonarray = new JSONArray(datos);
            JSONObject jsonRespuesta= jsonarray.getJSONObject(0);
            nombre=jsonRespuesta.getString("nombre");
            apellidoP=jsonRespuesta.getString("apellidoP");
            apellidoM=jsonRespuesta.getString("apellidoM");
            usuario=jsonRespuesta.getString("usuario");
            correo=jsonRespuesta.getString("correo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toolbar.setSubtitle(nombre +" "+apellidoP+" "+apellidoM);







    }

}
