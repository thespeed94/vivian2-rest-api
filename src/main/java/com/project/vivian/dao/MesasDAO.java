package com.project.vivian.dao;

import com.project.vivian.entidad.Mesa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("mesasDAO")
public interface MesasDAO extends JpaRepository<Mesa,Integer> {

    public List<Mesa> findAll();
    
    public Mesa getById(int id);
    
    @Query(value = "{call listarMesasActivas()}", nativeQuery = true)
    public List<Mesa> listarMesasActivas();


}
