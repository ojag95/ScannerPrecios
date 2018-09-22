package com.dev.comparador.comparador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Producto extends AppCompatActivity {
ListView lista;
    ArrayList<Category> category;
    String url="http://18.216.102.240/Comparador/ConsultarTiendasProducto.php";
    String codigoBarras;
    View view;
    JSONObject json;
    AdapterCategory adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        codigoBarras = getIntent().getStringExtra("codigoBarras");
        String nombre = getIntent().getStringExtra("nombre");
        String marca = getIntent().getStringExtra("marca");
        String presentacion = getIntent().getStringExtra("presentacion");
        toolbar.setTitle(nombre+" "+marca +" "+presentacion);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Producto.this,RegistroProducto.class);
                intent.putExtra("Codigo",codigoBarras);

                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view= this.getWindow().getDecorView().findViewById(android.R.id.content);
        lista = (ListView)findViewById(R.id.listaTiendas);
        category = new ArrayList<Category>();
        solicitarInformacionTienda();
        adapter = new AdapterCategory(this, category);

    }
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }

public void cargarDatos()
{
    solicitarInformacionTienda();
    adapter = new AdapterCategory(this, category);
    lista.setAdapter(adapter);
}

    public void solicitarInformacionTienda() {
        category.clear();
        json = new JSONObject();
        try {
            json.put("codigo", codigoBarras);

        } catch (JSONException e) {
            Snackbar.make(view, "Error en el servidor, intente mas tarde"+e, Snackbar.LENGTH_LONG).show();
        }
        EnvioJSON envio = new EnvioJSON(getApplicationContext(),url, json, view);
        envio.enviar(new EnvioJSON.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i=0;i<jsonarray.length();i++)
                    {

                        String strobjJson=jsonarray.getJSONObject(i).toString();
                        JSONObject objetoJson =new JSONObject(strobjJson);
                        Category objeto=new Category("Precio $"+objetoJson.getString("precio"),objetoJson.getString("nombre"),objetoJson.getString("ubicacion"));
                        category.add(objeto);
                        }

                    lista.setAdapter(adapter);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
