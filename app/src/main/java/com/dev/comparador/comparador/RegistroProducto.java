package com.dev.comparador.comparador;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RegistroProducto extends AppCompatActivity implements View.OnClickListener{
TextInputLayout txtCodigo;
String url="http://18.216.102.240/Comparador/ConsultarTiendas.php";
    String urlRegistro="http://18.216.102.240/Comparador/registroProducto.php";
    String arregloTiendas[];
    AlertDialog.Builder builder;
View view;
Button btnSearch;
TextInputLayout txtInfoTienda,txtMarca,txtPresentacion,txtPrecio,txtNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_producto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProducto);
        setSupportActionBar(toolbar);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        txtInfoTienda=(TextInputLayout)findViewById(R.id.txtInfoTienda);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarInformacionProducto();
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view= this.getWindow().getDecorView().findViewById(android.R.id.content);

        String codigo = getIntent().getStringExtra("Codigo");

        txtCodigo=(TextInputLayout)findViewById(R.id.txtCodigo);
        txtCodigo.getEditText().setText(codigo);
        txtNombre=(TextInputLayout)findViewById(R.id.txtNombreProducto);
        txtMarca=(TextInputLayout)findViewById(R.id.txtMarca);
        txtPresentacion=(TextInputLayout)findViewById(R.id.txtPresentacion);
        txtPrecio=(TextInputLayout)findViewById(R.id.txtPrecio);


    }

    @Override
    public void onClick(View view) {
        builder = new AlertDialog.Builder(this);
        solicitarInformacionTienda();





    }
    public void solicitarInformacionTienda() {
        JSONObject json = new JSONObject();
        try {
            json.put("opcion", "1");

        } catch (JSONException e) {
            Snackbar.make(view, "Error en el servidor, intente mas tarde"+e, Snackbar.LENGTH_LONG).show();
        }
        EnvioJSON envio = new EnvioJSON(getApplicationContext(),url, json, view);
        envio.enviar(new EnvioJSON.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    arregloTiendas=new String[jsonarray.length()+1];
                            for (int i=0;i<jsonarray.length();i++)
                            {

                                    String strobjJson=jsonarray.getJSONObject(i).toString();
                                    JSONObject objetoJson =new JSONObject(strobjJson);

                                        System.out.println("Valores id"+objetoJson.getString("idTienda")+" nombre "+objetoJson.getString("nombre")+" "+objetoJson.getString("ubicacion"));
                                        arregloTiendas[i]=objetoJson.getString("idTienda")+" "+objetoJson.getString("nombre");
                            }
                            arregloTiendas[arregloTiendas.length-1]="Registrar otra tienda";
                    builder.setTitle("Seleccione la sucursal");
                    builder.setItems(arregloTiendas, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (arregloTiendas[item].toString().equals("Registrar otra tienda")) {
                                Intent intent = new Intent(RegistroProducto.this, RegistroTienda.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), arregloTiendas[item], Toast.LENGTH_SHORT).show();

                            }
                            else {
                                txtInfoTienda.getEditText().setText(arregloTiendas[item].toString());
                            }

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void enviarInformacionProducto() {
        String[] tienda=txtInfoTienda.getEditText().getText().toString().split(" ");

        JSONObject json = new JSONObject();
        try {
            json.put("codigo", txtCodigo.getEditText().getText().toString());
            json.put("nombre", txtNombre.getEditText().getText().toString());
            json.put("marca", txtMarca.getEditText().getText()).toString();
            json.put("presentacion", txtPresentacion.getEditText().getText().toString());
            json.put("precio", txtPrecio.getEditText().getText().toString());
            json.put("idTienda",tienda[0]);
        } catch (JSONException e) {
            Snackbar.make(view, "Error al recopilar la informacion, intente mas tarde"+e, Snackbar.LENGTH_LONG).show();
        }
        EnvioJSON envio = new EnvioJSON(getApplicationContext(),urlRegistro, json, view);
        envio.enviar(new EnvioJSON.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    JSONObject jsonRespuesta= jsonarray.getJSONObject(0);
                    String respuesta=jsonRespuesta.getString("Mensaje");
                        Toast.makeText(getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
