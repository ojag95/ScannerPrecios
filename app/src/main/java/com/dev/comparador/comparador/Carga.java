package com.dev.comparador.comparador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);
        cargar();

    }
    public void cargar()
    {
        new Thread (new Runnable() {
            public void run() {

                try {
                    Thread.sleep(3000);
                    Intent intent =new Intent(Carga.this,Login.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }}).start();

    }
}
