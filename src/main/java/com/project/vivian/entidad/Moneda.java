package com.project.vivian.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "moneda")
@Entity
public class Moneda {
    @Id
    @Column(name = "moneda", nullable = false, length = 30)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}