package com.project.vivian.controller;

import com.project.vivian.entidad.*;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.UsuarioSpringService;
import com.project.vivian.service.constants.ResponseEstado;
import com.project.vivian.service.util.EncriptacionUtil;
import com.project.vivian.service.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class UsuarioSpringController {

	@Autowired
	private UsuarioSpringService usuarioSpringService;

	private int codigo = 0;

	private String DECRYPT_KEY = "AplicacionWebUno";

	String userDesencriptado = "";

	public void obtenerDatosUsuario(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UsuarioSpring> usuarioSpring = usuarioSpringService.obtenerPorEmail(auth.getName());
		model.addAttribute("nombreCompleto",usuarioSpring.get().getNombresUsuario() + " " + usuarioSpring.get().getApellidosUsuario());
	}

	// ENDPOINT PARA PROBAR EL SERVER
	@GetMapping("/usuarios")
	public String listar(Model model) throws Exception {
		List<UsuarioSpring> usuarios = usuarioSpringService.obtenerAdminUsuarios();
		model.addAttribute("usuarios", usuarios);
		return "pruebausuarios";
	}

	@GetMapping({"/","/login"})
	public String login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName().equals("anonymousUser")){
			return "index";
		}else{
			return "redirect:/principal";
		}
	}

	@GetMapping("/recuperation")
	public String forgotPassword(){
		return "forgotpassword";
	}

	@PostMapping("/recuperation")
	public ResponseEntity<Confirmacion> enviarForgotPassword(@RequestParam String username){
		Confirmacion confirmacion = new Confirmacion();
		try{
			Optional<UsuarioSpring> searchEmail = usuarioSpringService.obtenerPorEmail(username);
			if (searchEmail.isPresent()){
				usuarioSpringService.actualizarFlgResetPassword(searchEmail.get().getId(), 1);
			}
			String textoEncriptado = EncriptacionUtil.encriptar(username);
			MailUtil mailUtil = new MailUtil();
			String asunto = "Alguien (esperemos que tú) nos ha solicitado restablecer la contraseña de tu cuenta de administrador en Vivian." +
					"\n\nHaz clic en el siguiente enlace para hacerlo http://localhost:8091/resetpassword?user="+textoEncriptado+ "\n\nSi no solicitaste restablecer la contraseña, puedes ignorar este mensaje.";
			mailUtil.EnviarEmail(username,"Tu contraseña de Vivian",asunto);
			confirmacion.setEstado(ResponseEstado.OK);
			confirmacion.setMensaje("Correo enviado satisfactoriamente.");
			return ResponseEntity.accepted().body(confirmacion);
		}catch (Exception ex){
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}

	}

	@GetMapping("/resetpassword")
	public String cambiarPassword(@RequestParam String user) throws Exception {
		try{
			String userMas = user.replace(" ","+");
			userDesencriptado = EncriptacionUtil.desencriptar(userMas,DECRYPT_KEY);
			Optional<UsuarioSpring> searchUser = usuarioSpringService.obtenerPorEmail(userDesencriptado);
			if (searchUser.isPresent()){
				return "resetpassword";
			} else {
				return "error";
			}
		}catch (Exception ex){
			throw new Exception(ex.getMessage());
		}
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<Confirmacion> enviarCambioPassword(@RequestParam String password) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try {
			Optional<UsuarioSpring> searchEmail = usuarioSpringService.obtenerPorEmail(userDesencriptado);
			if (searchEmail.isPresent()){
				if (searchEmail.get().getFlgresetpassword() == 1){
					Integer valido = usuarioSpringService.actualizarPassOlvidada(searchEmail.get().getId(),password);
					if (valido == 1){
						usuarioSpringService.actualizarFlgResetPassword(searchEmail.get().getId(), 0);
						confirmacion.setEstado(ResponseEstado.OK);
						confirmacion.setMensaje("Contraseña actualizada correctamente.");
					} else {
						confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
						confirmacion.setMensaje("Hubo un error al actualizar su contraseña.");
					}
				} else {
					confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
					confirmacion.setMensaje("Hubo un error al actualizar su contraseña.");
				}
			}
			return ResponseEntity.accepted().body(confirmacion);
		}catch (Exception ex){
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@GetMapping("/principal")
	public String principal(Model model) {	
		obtenerDatosUsuario(model);		
		codigo = 0;
		model.addAttribute("verFragmento", codigo);
		return "general-summary";
	}

	@GetMapping(value="/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	
	@GetMapping(value="/adminusers")
	public String adminUsers(Model model) throws Exception {
		obtenerDatosUsuario(model);
		List<UsuarioSpring> listaAdminUsuarios = usuarioSpringService.obtenerAdminUsuarios();
		model.addAttribute("usuarioSpring",new UsuarioSpring());
		model.addAttribute("adminusers",listaAdminUsuarios);
		codigo = 1;
		model.addAttribute("verFragmento", codigo);
		return "general-summary";
	}

	@PostMapping(value="/adminusers")
	public ResponseEntity<Confirmacion> insertarAdminUser(UsuarioSpring usuarioSpring) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try{
			Optional<UsuarioSpring> usuarioEmail = usuarioSpringService.obtenerPorEmail(usuarioSpring.getUsername());
			if (usuarioEmail.isPresent()){
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("El usuario ya existe.");
			} else {
				UsuarioSpring usuarioDni = usuarioSpringService.obtenerPorDni(usuarioSpring.getDni());
				if (usuarioDni != null){
					confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
					confirmacion.setMensaje("El DNI ya existe.");
				} else {
					UsuarioSpring usuarioCreated = usuarioSpringService.crearAdminUsuario(usuarioSpring);
					if (usuarioCreated != null){
						confirmacion.setEstado(ResponseEstado.OK);
						confirmacion.setMensaje("Usuario ingresado correctamente.");
					}
				}
			}
			return ResponseEntity.accepted().body(confirmacion);
		}catch (Exception ex){
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@DeleteMapping(value="/adminusers")
	public ResponseEntity<Confirmacion> deleteAdminUser(@RequestParam Integer id) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try{
			if (usuarioSpringService.eliminarPorId(id)){
				confirmacion.setEstado(ResponseEstado.OK);
				confirmacion.setMensaje("Usuario eliminado correctamente.");
			} else {
				confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
				confirmacion.setMensaje("Error al eliminar el usuario.");
			}
			return ResponseEntity.accepted().body(confirmacion);
		}catch (Exception ex){
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

	@PutMapping(value="/adminusers")
	public ResponseEntity<Confirmacion> updateAdminUser(UsuarioSpring usuarioSpring) throws Exception {
		Confirmacion confirmacion = new Confirmacion();
		try{
			Optional<UsuarioSpring> searchEmail = usuarioSpringService.obtenerPorEmail(usuarioSpring.getUsername());
			UsuarioSpring searchDni = usuarioSpringService.obtenerPorDni(usuarioSpring.getDni());
			Optional<UsuarioSpring> usuarioActualizar = usuarioSpringService.obtenerPorId(usuarioSpring.getId());

			if (searchEmail.isEmpty() && searchDni == null){
				Integer valido = usuarioSpringService.actualizarAdminUsuario(usuarioSpring.getId(), usuarioSpring.getDni(),
						usuarioSpring.getNombresUsuario(), usuarioSpring.getApellidosUsuario(), usuarioSpring.getUsername(), usuarioSpring.getTelefono(), usuarioSpring.getEstado());
				if (valido == 1){
					confirmacion.setEstado(ResponseEstado.OK);
					confirmacion.setMensaje("Usuario actualizado correctamente.");
				}
			} else if (searchEmail.isEmpty() && searchDni != null){
				if (searchDni.getId() == usuarioActualizar.get().getId()){
					Integer valido = usuarioSpringService.actualizarAdminUsuario(usuarioSpring.getId(), usuarioSpring.getDni(),
							usuarioSpring.getNombresUsuario(), usuarioSpring.getApellidosUsuario(), usuarioSpring.getUsername(), usuarioSpring.getTelefono(), usuarioSpring.getEstado());
					if (valido == 1){
						confirmacion.setEstado(ResponseEstado.OK);
						confirmacion.setMensaje("Usuario actualizado correctamente.");
					}
				} else {
					confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
					confirmacion.setMensaje("El DNI ya existe.");
				}
			} else if (searchEmail.isPresent() && searchDni == null){
				if (searchEmail.get().getId() == usuarioActualizar.get().getId()){
					Integer valido = usuarioSpringService.actualizarAdminUsuario(usuarioSpring.getId(), usuarioSpring.getDni(),
							usuarioSpring.getNombresUsuario(), usuarioSpring.getApellidosUsuario(), usuarioSpring.getUsername(), usuarioSpring.getTelefono(), usuarioSpring.getEstado());
					if (valido == 1){
						confirmacion.setEstado(ResponseEstado.OK);
						confirmacion.setMensaje("Usuario actualizado correctamente.");
					}
				} else {
					confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
					confirmacion.setMensaje("El Usuario ya existe.");
				}
			} else {
				if (searchEmail.get().getId() == usuarioActualizar.get().getId()){
					if (searchDni.getId() == usuarioActualizar.get().getId()){
						Integer valido = usuarioSpringService.actualizarAdminUsuario(usuarioSpring.getId(), usuarioSpring.getDni(),
								usuarioSpring.getNombresUsuario(), usuarioSpring.getApellidosUsuario(), usuarioSpring.getUsername(), usuarioSpring.getTelefono(), usuarioSpring.getEstado());
						if (valido == 1){
							confirmacion.setEstado(ResponseEstado.OK);
							confirmacion.setMensaje("Usuario actualizado correctamente.");
						}
					} else {
						confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
						confirmacion.setMensaje("El DNI ya existe.");
					}
				} else {
					confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
					confirmacion.setMensaje("El Usuario ya existe.");
				}
			}
			return ResponseEntity.accepted().body(confirmacion);
		}catch (Exception ex){
			System.out.println(ex.getMessage());
			confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
			confirmacion.setMensaje(ex.getMessage());
			return ResponseEntity.badRequest().body(confirmacion);
		}
	}

}
