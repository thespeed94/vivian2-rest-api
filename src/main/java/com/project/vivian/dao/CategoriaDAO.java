package com.project.vivian.dao;

import com.project.vivian.entidad.Categoria;
import com.project.vivian.entidad.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("categoriaDAO")
public interface CategoriaDAO extends JpaRepository<Categoria, Integer> {
	    
	    @Query(value = "{call EliminarCategoria(:id, @val)}", nativeQuery = true)
	    public Integer deleteWithId(@Param("id") Integer id);

		@Query(value = "select * from Categoria where estado = 1", nativeQuery = true)
		public List<Categoria> getAllActive();
}
