package com.project.vivian.controller;
import com.project.vivian.entidad.Turno;
import com.project.vivian.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
// import java.util.Optional;

@Controller
@RequestMapping("/turno")
public class TurnosController {

	@Autowired
	private TurnoService turnoService;

	HashMap<String, Object> output = new HashMap<String, Object>();

	@GetMapping @ResponseBody
	public ResponseEntity<List<Turno>> listarTodo() throws Exception {
		return ResponseEntity.ok(turnoService.listarTurnos());
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
	
	@PutMapping @ResponseBody
	public ResponseEntity<HashMap<String, Object>> actualizarTurno(@RequestBody Turno t) throws Exception {
		try {
			turnoService.agregarTurno(t);
			output.put("msg", "Actualización exitosa");
		}

		catch(Exception e) {
			e.printStackTrace();
			output.put("msg", "Descripción de turno duplicado");
		}

		return ResponseEntity.ok(output);
	}
	
	@DeleteMapping("/{id}") @ResponseBody
	public ResponseEntity<HashMap<String, Object>>  eliminarTurno(@PathVariable Integer id) throws Exception {
		try {
			turnoService.eliminarTurno(id);
			output.put("msg", "Eliminado exitoso");
			// Optional<Turno> opt = turnoService.listarPorId(id);

			// if(opt.isPresent()) {
			// 	turnoService.eliminarTurno(id);
			// 	output.put("msg", "Eliminado exitoso");
			// }

			// else {
			// 	output.put("msg", "El id " + id  + " no existe");
			// }
		}

		catch(Exception e) {
			e.printStackTrace();
			System.out.println(id.getClass().getSimpleName());
			output.put("msg", e.getMessage());
		}

		return ResponseEntity.ok(output);
	}

}
