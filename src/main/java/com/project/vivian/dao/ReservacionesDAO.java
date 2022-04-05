package com.project.vivian.dao;

import com.project.vivian.entidad.Reserva;
import com.project.vivian.entidad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository("reservacionesDAO")
public interface ReservacionesDAO extends JpaRepository<Reserva, Integer> {

	public List<Reserva> findAllByOrderByFechaReservacionDesc();

	public Optional<Reserva> findByUsuarioId(Usuario usuario);

	public Reserva getById(int id);
	
	@Query(value = "{call ExisteCruceReserva(:inFechaReservacion, :inTurno, :inNMesa, @val)}", nativeQuery = true)
	public boolean ExisteCruceReserva(@Param("inFechaReservacion") Date inFechaReservacion ,@Param("inTurno")String inTurno ,@Param("inNMesa")int inNMesa);

}
