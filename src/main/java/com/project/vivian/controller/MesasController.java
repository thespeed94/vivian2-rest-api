package com.project.vivian.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vivian.entidad.Mesa;
import com.project.vivian.entidad.Reserva;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.MesaService;
import com.project.vivian.service.UsuarioService;
import com.project.vivian.service.constants.ResponseEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mesas")
public class MesasController {

	@Autowired
	private MesaService mesaService;

	private int codigo = 5;

	@ResponseBody @GetMapping
	public ResponseEntity<List<Mesa>> listar() throws Exception {

		return ResponseEntity.ok(mesaService.listarMesas());
	}

	@PostMapping("")
	public ResponseEntity<Confirmacion> insertarMesa(@RequestBody Mesa mesa) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			mesaService.crearMesa(mesa);
			
			confirmacion.setEstado(ResponseEstado.OK);
			confirmacion.setMensaje("Mesa Ingresada correctamente.");

			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@PutMapping("")
	public ResponseEntity<Confirmacion> ActualizarMesa(@RequestBody Mesa mesa) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			mesaService.actualizarMesa(mesa);
			confirmacion.setEstado(ResponseEstado.OK);
			confirmacion.setMensaje("Mesa actualizada correctamente.");

			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Confirmacion> deleteReserva(@PathVariable("id") Integer id) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		Mesa mesa= new Mesa();
		mesa.setId(id);
		try {
			if (mesaService.eliminarPorId(mesa)) {
				confirmacion.setEstado(ResponseEstado.OK);
				confirmacion.setMensaje("Mesa eliminada correctamente.");
			} else {
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("Error al eliminar la mesa.");
			}
			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

}
