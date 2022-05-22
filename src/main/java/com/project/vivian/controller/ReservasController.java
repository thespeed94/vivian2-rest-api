package com.project.vivian.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vivian.entidad.Mesa;
import com.project.vivian.entidad.Pedido;
import com.project.vivian.entidad.Producto;
import com.project.vivian.entidad.Reserva;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.UsuarioSpring;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.MesaService;
import com.project.vivian.service.ReservacionesService;
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

@RestController
@RequestMapping("/reservaciones")
public class ReservasController {

	@Autowired
	private ReservacionesService reservacionesService;

	@Autowired
	private MesaService mesaService;

	@Autowired
	private UsuarioService usuarioService;

	private int codigo = 3;
	
	@GetMapping @ResponseBody
    public ResponseEntity<List<Reserva>> listar() throws Exception {

        return ResponseEntity.ok(reservacionesService.obtenerReservaciones());
    }

	
	@PostMapping("")
    public ResponseEntity<Confirmacion> insertarReserva(@RequestBody Reserva reserva) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
        	
        	//Optional<Usuario> usuarioPorDni = usuarioService.obtenerPorDni(reserva.getUsuario().getDni());
        	//if (!usuarioPorDni.isEmpty()) {
        		if (!reservacionesService.existeCruceReserva(reserva)) {
            		reservacionesService.crearReserva(reserva);
                	
                    confirmacion.setEstado(ResponseEstado.OK);
                    confirmacion.setMensaje("Reserva ingresada correctamente.");
                    return ResponseEntity.accepted().body(confirmacion);
            	} else {
            		confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
                    confirmacion.setMensaje("Ya existe una reserva.");
                    return ResponseEntity.badRequest().body(confirmacion);
            	}
        	//} else {
			//	confirmacion.setEstado(0);
			//	confirmacion.setMensaje("El usuario con dicho DNI no existe.");
			//	return ResponseEntity.badRequest().body(confirmacion);
			//}
        	
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

	
	@PutMapping("")
    public ResponseEntity<Confirmacion> updateReserva(@RequestBody Reserva reserva) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
        	
        	//Optional<Usuario> usuarioPorDni = usuarioService.obtenerPorDni(reserva.getUsuario().getDni());
        	//if (!usuarioPorDni.isEmpty()) {
	        	if (!reservacionesService.existeCruceReserva(reserva)) {
					//reserva.setUsuario(usuarioPorDni.get());
					reservacionesService.actualizarReserva(reserva);
					confirmacion.setEstado(ResponseEstado.OK);
					confirmacion.setMensaje("Actualizado correctamente");
					return ResponseEntity.accepted().body(confirmacion);
				} else {
					confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
					confirmacion.setMensaje("Ya existe una reserva.");
					return ResponseEntity.badRequest().body(confirmacion);
				}
        	
        	//} else {
			//	confirmacion.setEstado(0);
			//	confirmacion.setMensaje("El usuario con dicho DNI no existe.");
			//	return ResponseEntity.badRequest().body(confirmacion);
			//}
        	
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }



	
	@DeleteMapping("/{id}")
    public ResponseEntity<Confirmacion> deleteReserva(@PathVariable("id") String id) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        
        try {
        	Integer numberId = Integer.parseInt(id);
        	Reserva reservaEncontrada = reservacionesService.obtenerReservaPorId(numberId);
    		if (reservacionesService.eliminarReserva(numberId)) {
				confirmacion.setEstado(ResponseEstado.OK);
				confirmacion.setMensaje("Reserva eliminada correctamente.");
			} else {
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("Error al eliminar la reservacion.");
			}
        	
			return ResponseEntity.accepted().body(confirmacion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            String message = ex.getMessage();
            if(message.contains("Unable to find")) {
            	message = "No se puede eliminar una reservación no existente.";
            }
            confirmacion.setMensaje(message);
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }
	

}
