package com.dev.comparador.comparador;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerProductos extends BaseScanner implements
    ZXingScannerView.ResultHandler,View.OnClickListener  {
    View view;
    public String mensaje;
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    int permiso=0;
    public String codigo="";
    public String url="http://18.216.102.240/Comparador/VerificarExistencia.php";

    Menu menu;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
       solicitarPermiso();
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }
        setContentView(R.layout.activity_scanner_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        setSupportActionBar(toolbar);


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        setupFormats();
        contentFrame.addView(mScannerView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view = getWindow().getDecorView().getRootView().findViewById(R.id.layoutscanner);


    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }



    @Override
    public void handleResult(Result result) {
        codigo=result.getText();
        formatoInformacion(codigo);
        //mScannerView.resumeCameraPreview(this);
    }



    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }


    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_scannerproductos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_flash:
               if (mFlash)
               {

                   mFlash = false;
                   menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.flash_on));

               }
               else
               {
                   mFlash = true;
                   menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.flash_off));

               }

                mScannerView.setFlash(mFlash);
                return true;

            case R.id.action_autofocus:
                if (mAutoFocus)
                {

                    mAutoFocus = false;
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.center_focus));
                    Snackbar.make(view, "Auto enfoque deshabilitado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    mAutoFocus = true;
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.center_focus_weak));
                    Snackbar.make(view, "Auto enfoque habilitado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                mScannerView.setAutoFocus(mAutoFocus);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void solicitarPermiso()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ScannerProductos.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScannerProductos.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ScannerProductos.this,
                        new String[]{Manifest.permission.CAMERA},
                        permiso);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            }



    public void formatoInformacion(final String codigoArticulo) {
        JSONObject json = new JSONObject();
        try {
            json.put("codigo", codigoArticulo);
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
                    Toast.makeText(getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();
                    if (respuesta.contains("El producto no esta registrado"))
                    {
                        Intent intent=new Intent(ScannerProductos.this,RegistroProducto.class);
                        intent.putExtra("Codigo",codigoArticulo);

                        startActivity(intent);


                        //Intent intent =new Intent(Login.this,ComparadorPrecios.class);
                        //intent.putExtra("Datos",result);
                        //startActivity(intent);
                    }
                    else {

                        Snackbar.make(view, respuesta, Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(ScannerProductos.this,Producto.class);

                        intent.putExtra("codigoBarras",jsonRespuesta.getString("codigoBarras"));
                        intent.putExtra("nombre",jsonRespuesta.getString("nombre"));
                        intent.putExtra("marca",jsonRespuesta.getString("marca"));
                        intent.putExtra("presentacion",jsonRespuesta.getString("presentacion"));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }


            }




        });
    }

        }
