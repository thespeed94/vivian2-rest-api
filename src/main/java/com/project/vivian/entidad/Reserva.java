package com.project.vivian.entidad;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

//wdwdwdwdwdwdwdw
@Table(name = "Reserva")
@Entity
public class Reserva {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idReserva", nullable = false)
	private Integer idReserva;

	@ManyToOne(optional = false)
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@Column(name = "nMesa", nullable = false)
	private Integer nMesa;

	@Column(name = "piso", nullable = false, length = 1)
	private char piso;

	@Column(name = "fechaReservacion")
	private Date fechaReservacion;

	@ManyToOne(optional = false)
	@JoinColumn(name = "idTurno")
	private Turno turno;

	public Integer getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getnMesa() {
		return nMesa;
	}

	public void setnMesa(Integer nMesa) {
		this.nMesa = nMesa;
	}

	public char getPiso() {
		return piso;
	}

	public void setPiso(char piso) {
		this.piso = piso;
	}

	public Date getFechaReservacion() {
		return fechaReservacion;
	}

	public void setFechaReservacion(Date fechaReservacion) {
System.out.println(">>FECHA : > " +fechaReservacion.toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(fechaReservacion); 
		c.add(Calendar.DATE, 1);
		fechaReservacion = c.getTime();
System.out.println(">>FECHA +1: > " +fechaReservacion.toString());		
		this.fechaReservacion = fechaReservacion;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno idTurno) {
		this.turno = idTurno;
	}

	public String fechaString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneOffset.UTC);
		return dtf.format(fechaReservacion.toInstant());
	}

	public String obtenerPiso() {
		String descripcion = "";
		switch (piso) {
		case '1':
			descripcion = "1er Piso";
			break;
		case '2':
			descripcion = "2do Piso";
			break;
		default:
			break;
		}
		return descripcion;
	}

	public Reserva() {
		usuario = new Usuario();
		turno = new Turno();

	}
}