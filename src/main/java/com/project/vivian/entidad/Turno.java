package com.project.vivian.entidad;

import javax.persistence.*;

@Table(name = "turno", indexes = {
        @Index(name = "uq_descripcion", columnList = "descripcion", unique = true)
})
@Entity
public class Turno {
    @Id
    @Column(name = "idTurno", nullable = false, length = 30)
    private String id;

    @Column(name = "descripcion", nullable = false, length = 35)
    private String descripcion;

    @Column(name = "horario", nullable = false, length = 35)
    private String horario;

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}