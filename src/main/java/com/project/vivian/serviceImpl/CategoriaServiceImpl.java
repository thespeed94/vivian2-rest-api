package com.project.vivian.serviceImpl;

import com.project.vivian.dao.CategoriaDAO;
import com.project.vivian.entidad.Categoria;
import com.project.vivian.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("categoriaService")
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    CategoriaDAO categoriaDAO;

    @Override
    public List<Categoria> obtenerCategorias() throws Exception {
        try {
            return categoriaDAO.findAll();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

	@Override
	public Categoria actualizarCategoria(Categoria categoria) throws Exception {
		try {
            return categoriaDAO.save(categoria);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}

	@Override
	public int eliminarCategoria(int id) throws Exception {
		try {
            return categoriaDAO.deleteWithId(id);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}

    @Override
    public List<Categoria> obtenerCategoriasActivas() throws Exception {
        try {
            return categoriaDAO.getAllActive();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
	public Categoria agregarCategoria(Categoria categoria) throws Exception {
		try {
            return categoriaDAO.save(categoria);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}
}
