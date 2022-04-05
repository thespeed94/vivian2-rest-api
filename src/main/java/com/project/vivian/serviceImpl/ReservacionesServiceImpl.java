package com.project.vivian.serviceImpl;

import com.project.vivian.dao.ReservacionesDAO;
import com.project.vivian.dao.UsuarioDAO;
import com.project.vivian.entidad.Reserva;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.service.ReservacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("reservacionesService")
public class ReservacionesServiceImpl implements ReservacionesService {

	@Autowired
	ReservacionesDAO reservacionesDAO;

	@Autowired
	UsuarioDAO usuarioDAO;
	
	@Override
	public List<Reserva> obtenerReservaciones() throws Exception {
		try {
			List<Reserva> lista = reservacionesDAO.findAll();
			return lista;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Reserva crearReserva(Reserva entity) throws Exception {
		try {
			entity.setIdReserva(null);
			Reserva reservaCreated = reservacionesDAO.save(entity);
			return reservaCreated;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Reserva actualizarReserva(Reserva entity) throws Exception {
		try {
			Reserva reservaCreated = reservacionesDAO.save(entity);
			return reservaCreated;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Optional<Reserva> obtenerPorUsuario(Usuario usuario) {
		Optional<Reserva> lstReservas = reservacionesDAO.findByUsuarioId(usuario);
		return lstReservas;
	}

	@Override
	public Reserva obtenerReservaPorId(Integer id) {
		Reserva objReserva = reservacionesDAO.getById(id);
		return objReserva;
	}

	@Override
	public boolean eliminarReserva(Integer id) throws Exception {
		try {
			reservacionesDAO.deleteById(id);
			return true;
		}catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean existeCruceReserva(Reserva reserva) throws Exception {
		boolean existeCruce=false;
		try {
			System.out.println(">>> "+ reserva.getFechaReservacion() + "  "+ reserva.getTurno().getId() + "   " + reserva.getnMesa());
			existeCruce=reservacionesDAO.ExisteCruceReserva(reserva.getFechaReservacion(), reserva.getTurno().getId(), reserva.getnMesa());
			return existeCruce;
		}catch (Exception e) {
			return false;
		}
	}

}
