package com.project.vivian.service;

import com.project.vivian.entidad.Categoria;
import com.project.vivian.entidad.Turno;

import java.util.List;

public interface TurnoService {

	public Integer agregarTurno(Turno turno) throws Exception;
	public List<Turno> listarTurnos() throws Exception;
    public int actualizarTurno(Turno turno) throws Exception;
    public int eliminarTurno(int id) throws Exception;
    
	
}
