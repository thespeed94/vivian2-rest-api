package com.project.vivian.entidad;

import javax.persistence.*;

@Table(name = "mesa")
@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nMesa", nullable = false)
    private Integer id;

    @Column(name = "piso", nullable = false)
    private String piso;

    @Column(name = "capacidadPersonas", nullable = false)
    private Integer capacidadPersonas;

    public Integer getCapacidadPersonas() {
        return capacidadPersonas;
    }

    public void setCapacidadPersonas(Integer capacidadPersonas) {
        this.capacidadPersonas = capacidadPersonas;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}