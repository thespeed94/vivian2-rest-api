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

@Controller
@RequestMapping("/mesas")
public class MesasController {

	@Autowired
	private MesaService mesaService;

	private int codigo = 5;

	@GetMapping("")
	public String listar(Model model) throws Exception {

		List<Mesa> mesas = mesaService.listarMesas();
		
		model.addAttribute("mesa", new Mesa());
		model.addAttribute("mesas", mesas);
		model.addAttribute("verFragmento", codigo);
		return "general-summary";
	}

	@PostMapping("")
	public ResponseEntity<Confirmacion> insertarMesa(@RequestParam String json) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			Mesa mesa = new ObjectMapper().readValue(json, Mesa.class);
			mesaService.crearMesa(mesa);
			confirmacion.setEstado(ResponseEstado.OK);
			confirmacion.setMensaje("Mesa ingresada correctamente.");

			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@PutMapping("")
	public ResponseEntity<Confirmacion> updateMesa(String json) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			Mesa mesa = new ObjectMapper().readValue(json, Mesa.class);

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

	@DeleteMapping("")
	public ResponseEntity<Confirmacion> deleteReserva(@RequestParam Integer id) throws Exception {
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
