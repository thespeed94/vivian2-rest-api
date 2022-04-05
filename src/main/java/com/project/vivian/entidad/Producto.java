package com.project.vivian.entidad;

import javax.persistence.*;

@Table(name = "producto")
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false)
    private Integer id;

    @Column(name = "nombreProducto", nullable = false, length = 40)
    private String nombreProducto;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idCategoria", nullable = false)
    private Categoria idCategoria;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "reparto", nullable = false)
    private Integer reparto;

    @Column(name = "estado")
    private Integer estado;

    public String estadoString(){
        if (estado == 1){
            return "Activo";
        }else{
            return "Inactivo";
        }
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getReparto() {
        return reparto;
    }

    public void setReparto(Integer reparto) {
        this.reparto = reparto;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}