package com.project.vivian.service;

import com.project.vivian.entidad.Tipo;

import java.util.List;

public interface TipoService {

    public List<Tipo> obtenerTipos();

    public Tipo obtenerPorId(Integer id) throws Exception;
}
