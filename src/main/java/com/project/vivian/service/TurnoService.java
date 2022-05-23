package com.project.vivian.service;

import com.project.vivian.entidad.Turno;

import java.util.List;
import java.util.Optional;

public interface TurnoService {

	public Turno agregarTurno(Turno turno) throws Exception;
	public List<Turno> listarTurnos() throws Exception;
    public int actualizarTurno(Turno turno) throws Exception;
    public int eliminarTurno(int id) throws Exception;
    public String darId();

    public Optional<Turno> listarPorId(int id);
	
}
