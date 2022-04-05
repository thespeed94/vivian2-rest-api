package com.project.vivian.dao;

import com.project.vivian.entidad.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("tipoDAO")
public interface TipoDAO extends JpaRepository<Tipo, Integer> {


}
