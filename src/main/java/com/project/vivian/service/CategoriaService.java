package com.project.vivian.service;

import com.project.vivian.entidad.Categoria;
import com.project.vivian.entidad.Turno;

import java.util.List;

public interface CategoriaService {

	public Categoria agregarCategoria(Categoria categoria) throws Exception;
    public List<Categoria> obtenerCategorias() throws Exception;
    public Categoria actualizarCategoria(Categoria categoria) throws Exception;
    public int eliminarCategoria(int id) throws Exception;
    public List<Categoria> obtenerCategoriasActivas() throws Exception;
    
}
