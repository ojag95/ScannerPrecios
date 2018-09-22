package com.dev.comparador.comparador;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EnvioJSON extends AppCompatActivity {
    public RequestQueue rqt;
    public StringRequest strq;
    public String response;


    View view;
    JSONObject json;
    String url;
    Context context;
public EnvioJSON(Context context, String url, JSONObject json, View view)
{
    this.json=json;
    this.url=url;
    this.view=view;
    this.context=context;

}

    public void enviar(final VolleyCallback callback)
    {

        //Volley

        rqt= Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Respuesta_servidor", response);

                        //Snackbar.make(view, response, Snackbar.LENGTH_SHORT).show();
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "Error en el servidor, intente mas tarde: "+error, Snackbar.LENGTH_LONG).show();
            }
        }){
            public byte[] getBody() throws AuthFailureError {
                return json.toString().getBytes();
            }
        };
        // Add the request to the RequestQueue.
        rqt.add(stringRequest);


    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }




}
