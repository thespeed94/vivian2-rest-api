package com.project.vivian.dao;
import com.project.vivian.entidad.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("turnosDAO")
public interface TurnosDAO extends JpaRepository<Turno, Integer> {
	    @Query(value = "{call AgregarTurno(:id, :descripcion, :horario, @val)}", nativeQuery = true)
	    public Integer saveTurno(@Param("id") String id, @Param("descripcion") String descripcion, @Param("horario") String horario);
	    
	    @Query(value = "{call ObtenerUltimoIdTurno(@val)}", nativeQuery = true)
	    public String obtenerUltimoId();
	    
	    @Query(value = "{call EliminarTurnoPorId(:id, @val)}", nativeQuery = true)
	    public Integer deleteWithId(@Param("id") Integer id);
}