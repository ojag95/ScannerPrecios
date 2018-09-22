package com.dev.comparador.comparador;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    //Declaracion de variables de componentes
    TextInputLayout txtNombre,txtApellidoP,txtApellidoM,txtCorreo,txtUsuario,txtContrasenia,txtContrasenia2;
    //Declaracion de variables de tipo cadena
    String nombre,apellidoP,apellidoM,correo,usuario,contrasenia,contrasenia2,respuesta;
    //declarcion de View
    View view;

    //Declaracion de patrones de cadenas, el siguiente detecta letras de la "a" a la "z" en mayusculas y minusculas ademas de ñ Ñ ñ Á á É é Í í Ó ó Ú ú si se introduce un numero no se pasa el filtro
    Pattern patronLetras = Pattern.compile("^[a-zA-ZñÑñÁáÉéÍíÓóÚú ]+$");

    //Declaracion de la url del servidor al que se conectara
    //public String url="http://18.216.102.240/Comparador/registro.php";

    public String url="http://18.216.102.240/Comparador/registro.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        view= this.getWindow().getDecorView().findViewById(android.R.id.content);


        //Instancia de objetos
        txtNombre=(TextInputLayout) findViewById(R.id.txtRegistroNombre);
        txtApellidoP=(TextInputLayout) findViewById(R.id.txtRegistroApellidoP);
        txtApellidoM=(TextInputLayout) findViewById(R.id.txtRegistroApellidoM);
        txtCorreo=(TextInputLayout) findViewById(R.id.txtRegistroCorreo);
        txtUsuario=(TextInputLayout) findViewById(R.id.txtRegistroUsuario);
        txtContrasenia=(TextInputLayout) findViewById(R.id.txtRegistroContrasenia);
        txtContrasenia2=(TextInputLayout) findViewById(R.id.txtRegistroConfirmarContrasenia);


    }
    @Override   //
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_registroGuardar:
                validarDatos();
                if(validarDatos())
                {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
                else{
                    formatoInformacion();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean validarDatos()
    {
        boolean error=false;
        nombre=txtNombre.getEditText().getText().toString();
        apellidoP=txtApellidoP.getEditText().getText().toString();
        apellidoM=txtApellidoM.getEditText().getText().toString();
        correo=txtCorreo.getEditText().getText().toString();
        usuario=txtUsuario.getEditText().getText().toString();
        contrasenia=txtContrasenia.getEditText().getText().toString();
        contrasenia2=txtContrasenia2.getEditText().getText().toString();

        //Validacion de datos se verifica si esta vacio o si contiene caracteres no deseados o es mas largo que la capacidad establecida
        //Nombre
        if (nombre.equals("")||nombre.equals(null))
        {
            txtNombre.setError("El campo no puede estar vacio");
            error=true;
        }
        else if (!patronLetras.matcher(nombre).matches() || nombre.length() > 30) {
            txtNombre.setError("El nombre es inválido no puede contener numeros ni ser mayor a 30 caracteres");
            error=true;
        }
        else
        {
            txtNombre.setError(null);
        }
        //Apellido Paterno
        if (apellidoP.equals("")||apellidoP.equals(null))
        {
            txtApellidoP.setError("El campo no puede estar vacio");
            error=true;
        }
        else if (!patronLetras.matcher(apellidoP).matches() || apellidoP.length() > 30) {
            txtApellidoP.setError("El apellido es inválido no puede contener numeros ni ser mayor a 30 caracteres");
            error=true;
        }
        else
        {
            txtApellidoP.setError(null);
        }
        //Apellido Materno
        if (apellidoM.equals("")||apellidoM.equals(null))
        {
            txtApellidoM.setError("El campo no puede estar vacio");
            error=true;
        }
        else if (!patronLetras.matcher(apellidoM).matches() || apellidoM.length() > 30) {
            txtApellidoM.setError("El apellido es inválido no puede contener numeros ni ser mayor a 30 caracteres");
            error=true;
        }
        else
        {
            txtApellidoM.setError(null);
        }
        //Correo Electronico
        if (correo.equals("")||correo.equals(null))
        {
            txtCorreo.setError("El campo no puede estar vacio");
            error=true;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.setError("El formato el correo es invalido");
            error=true;
        }
        else
        {
            txtCorreo.setError(null);
        }
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
        //Contrasenia2
        if (contrasenia2.equals("")||contrasenia2.equals(null))
        {
            txtContrasenia2.setError("El campo no puede estar vacio");
            error=true;
        }
        else{
            txtContrasenia.setError(null);
        }

        //Validacion de contraseñas
        if (!contrasenia.equals(contrasenia2))
        {
            txtContrasenia.setError("Las contraseñas no coinciden");
            txtContrasenia2.setError("Las contraseñas no coinciden");
            error=true;
        }else
        {
            txtContrasenia.setError(null);
            txtContrasenia2.setError(null);

        }
        return error;
    }

    public void formatoInformacion() {
        JSONObject json = new JSONObject();
        try {
            json.put("nombre", nombre);
            json.put("apellidoP", apellidoP);
            json.put("apellidoM", apellidoM);
            json.put("correo", correo);
            json.put("usuario", usuario);
            json.put("contrasenia", contrasenia);
        } catch (JSONException e) {
            Snackbar.make(view, "Error en el servidor, intente mas tarde"+e, Snackbar.LENGTH_LONG).show();
        }
        EnvioJSON envio = new EnvioJSON(getApplicationContext(),url, json, view);
        // envio.enviar();

        envio.enviar(new EnvioJSON.VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    JSONObject jsonRespuesta= jsonarray.getJSONObject(0);
                    String respuesta=jsonRespuesta.getString("Mensaje");
                    regresarLogIN(respuesta);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }

            }
        });


        // regresarLogIN();
    }

    public void regresarLogIN(String respuesta)
    {

        if (respuesta.equals("Registro realizado exitosamente"))
        {
            Toast.makeText(getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
