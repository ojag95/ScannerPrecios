package com.dev.comparador.comparador;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener{
    Button btnRegistro,btnIniciarSesion;

    View view;
    String usuario,contrasenia;

    //Declaracion de la url del servidor al que se conectara
    public String url="http://XXXXXXXXXXXXXXXX/Comparador/login.php";
    //public String url="http://192.168.100.2/Comparador/login.php";

    TextInputLayout txtUsuario,txtContrasenia;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        setSupportActionBar(toolbar);
        btnRegistro=(Button)findViewById(R.id.btnRegistrarse);
        btnRegistro.setOnClickListener(this);
        btnIniciarSesion=(Button)findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);
        txtUsuario=(TextInputLayout)findViewById(R.id.txtUsuario);
        txtContrasenia=(TextInputLayout)findViewById(R.id.txtContrasenia);
        view= this.getWindow().getDecorView().findViewById(android.R.id.content);


    }
    @Override
    protected void onResume() {
        super.onResume();
        txtContrasenia.getEditText().setError(null);
        txtContrasenia.getEditText().setError(null);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnRegistrarse:
                intent=new Intent(Login.this,Registro.class);
                startActivity(intent);
                break;
            case R.id.btnIniciarSesion:
                if (validarDatos())
                {

                }
                else
                {
                    formatoInformacion();
                }
                break;
        }
    }
    public boolean validarDatos()
    {
        Boolean error =false;
        usuario=txtUsuario.getEditText().getText().toString();
        contrasenia=txtContrasenia.getEditText().getText().toString();
        //Usuario
        if (usuario.equals("")||usuario.equals(null))
        {
            txtUsuario.setError("El campo no puede estar vacio");
            error=true;
        }
        else
        {
            txtUsuario.setError(null);
        }
        //Contraseña1
        if (contrasenia.equals("")||contrasenia.equals(null))
        {
            txtContrasenia.setError("El campo no puede estar vacio");
            error=true;
        }
        else{
            txtContrasenia.setError(null);
        }
        return error;

    }
    public void formatoInformacion() {
        JSONObject json = new JSONObject();
        try {
            json.put("usuario", usuario);
            json.put("contrasenia", contrasenia);
        } catch (JSONException e) {
            Snackbar.make(view, "Error al crear la estructura de datos "+ e, Snackbar.LENGTH_LONG).show();
        }
        EnvioJSON envio = new EnvioJSON(getApplicationContext(),url, json, view);


        envio.enviar(new EnvioJSON.VolleyCallback(){
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonarray = new JSONArray(result);
                    JSONObject jsonRespuesta= jsonarray.getJSONObject(0);
                    String respuesta=jsonRespuesta.getString("Mensaje");
                    if (respuesta.contains("El usuario existe"))
                    {
                        txtContrasenia.getEditText().setText("");
                        txtUsuario.getEditText().setText("");

                        Intent intent =new Intent(Login.this,ComparadorPrecios.class);
                        intent.putExtra("Datos",result);
                        startActivity(intent);
                    }
                    else {
                        Snackbar.make(view, respuesta, Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }


            }




        });
    }

}
