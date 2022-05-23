package com.project.vivian.controller;

import com.project.vivian.entidad.Turno;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.TurnoService;
import com.project.vivian.service.constants.ResponseEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/turno")
public class TurnosController {

	@Autowired
	private TurnoService turnoService;

	HashMap<String, Object> output = new HashMap<String, Object>();

	// @Autowired
	// private UsuarioService usuarioService;

	// private int codigo = 7;

	// public void obtenerDatosUsuario(Model model) throws Exception {
	// 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// 	Optional<Usuario> usuarioSpring = usuarioService.obtenerPorEmail(auth.getName());
	// 	model.addAttribute("nombreCompleto",
	// 			usuarioSpring.get().getNombresUsuario() + " " + usuarioSpring.get().getApellidosUsuario());
	// }

	@GetMapping @ResponseBody
	public ResponseEntity<List<Turno>> listarTodo() throws Exception {
		return ResponseEntity.ok(turnoService.listarTurnos());

		// List<Turno> turnos = turnoService.listarTurnos();
		// model.addAttribute("turno", new Turno());
		// model.addAttribute("turnos", turnos);
		// model.addAttribute("verFragmento", codigo);
		// return "general-summary";
	}

	@PostMapping @ResponseBody
	public ResponseEntity<HashMap<String, Object>> insertarTurno(@RequestBody Turno t) throws Exception {
		try {
			t.setId(turnoService.darId());
			turnoService.agregarTurno(t);
			output.put("msg", "Insert successful");
		}

		catch(Exception e) {
			e.printStackTrace();
			output.put("msg", "Descripción de turno duplicado");
		}

		return ResponseEntity.ok(output);
	}
	
	@PutMapping("")
	public ResponseEntity<Confirmacion> actualizarTurno(Turno turno) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			if(turnoService.actualizarTurno(turno)>0) {
				confirmacion.setEstado(ResponseEstado.OK);
				confirmacion.setMensaje("Turno actualizado correctamente.");
			}else {	
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("No se pudo actualizar el Turno");
			}

			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}
	
	@DeleteMapping("")
	public ResponseEntity<Confirmacion> eliminarTurno(int id) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			if(turnoService.eliminarTurno(id)>0) {
				confirmacion.setEstado(ResponseEstado.OK);
				confirmacion.setMensaje("Turno eliminado correctamente.");
			}else {	
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("No se pudo eliminar el Turno");
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
