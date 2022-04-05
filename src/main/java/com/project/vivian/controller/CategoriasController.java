package com.project.vivian.controller;

import com.project.vivian.entidad.Categoria;
import com.project.vivian.entidad.Turno;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.CategoriaService;
import com.project.vivian.service.TurnoService;
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
@RequestMapping("/categoria")
public class CategoriasController {
	@Autowired
	private CategoriaService categoriaService;

	private int codigo = 6;

	@GetMapping("")
	public String listar(Model model) throws Exception {

		List<Categoria> categorias = categoriaService.obtenerCategorias();
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("categorias", categorias);
		model.addAttribute("verFragmento", codigo);
		return "general-summary";
	}

	@PostMapping("")
	public ResponseEntity<Confirmacion> insertarCategoria(Categoria categoria) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			categoriaService.agregarCategoria(categoria);

			confirmacion.setEstado(ResponseEstado.OK);
			confirmacion.setMensaje("Categoria ingresada correctamente.");

			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@PutMapping("")
	public ResponseEntity<Confirmacion> actualizarCategoria(Categoria categoria) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			categoriaService.actualizarCategoria(categoria);

			confirmacion.setEstado(ResponseEstado.OK);
			confirmacion.setMensaje("Categoria actualizada correctamente" );

			return ResponseEntity.accepted().body(confirmacion);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@DeleteMapping("")
	public ResponseEntity<Confirmacion> deleteAdminUser(@RequestParam Integer id) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			if (categoriaService.eliminarCategoria(id)>0){
					confirmacion.setEstado(ResponseEstado.OK);
				confirmacion.setMensaje("Categoria eliminada correctamente.");
			} else {
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("Error al eliminar la categoria.");
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
