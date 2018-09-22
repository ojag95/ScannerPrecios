package com.dev.comparador.comparador;
public class Category {

    private String precio;
    private String tienda;
    private String direccion;

    public Category() {
        super();
    }

    public Category(String precio, String tienda, String direccion) {
        super();
        this.precio = precio;
        this.tienda = tienda;
        this.direccion = direccion;
    }


    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }


    public String getDireccion(){
        return direccion;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

}
