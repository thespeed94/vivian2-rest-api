package com.project.vivian.service;

import com.project.vivian.entidad.Reserva;
import com.project.vivian.entidad.Usuario;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservacionesService {
	public List<Reserva> obtenerReservaciones() throws Exception;

	public Reserva crearReserva(Reserva entity) throws Exception;

	public Reserva actualizarReserva(Reserva entity) throws Exception;

	Optional<Reserva> obtenerPorUsuario(Usuario usuario);

	public Reserva obtenerReservaPorId(Integer id) throws Exception;

	public boolean eliminarReserva(Integer id) throws Exception;

	public boolean existeCruceReserva(Reserva reserva) throws Exception;

}
