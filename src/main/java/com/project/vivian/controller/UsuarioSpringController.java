package com.project.vivian.controller;

import com.project.vivian.dto.request.Login;
import com.project.vivian.entidad.*;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.UsuarioSpringService;
import com.project.vivian.service.constants.ResponseEstado;
import com.project.vivian.service.util.EncriptacionUtil;
import com.project.vivian.service.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioSpringController {

    @Autowired
    private UsuarioSpringService usuarioSpringService;

    private String DECRYPT_KEY = "AplicacionWebUno";

    String userDesencriptado = "";

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Login login) {
        Map<String, Object> response = new HashMap<>();
        String str;
        System.out.println(login.getUsername());
        try {
            str = usuarioSpringService.login(login.getUsername(), login.getPassword());
            if (str.equals("-1") || str.equals("0")) {
                response.put("estado", ResponseEstado.ERROR_NEGOCIO);
                response.put("mensaje", "Usuario y/o contraseña incorrecta");
            } else {
                response.put("estado", ResponseEstado.OK);
                response.put("mensaje", "Usuario logeado correctamente");
                response.put("token", str);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            response.put("estado", ResponseEstado.ERROR_APLICACION);
            response.put("mensaje", ex.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recuperation")
    public String forgotPassword() {
        return "forgotpassword";
    }

    @PostMapping("/recuperation")
    public ResponseEntity<Confirmacion> enviarForgotPassword(@RequestParam String username) {
        Confirmacion confirmacion = new Confirmacion();
        try {
            Optional<UsuarioSpring> searchEmail = usuarioSpringService.obtenerPorEmail(username);
            if (searchEmail.isPresent()) {
                usuarioSpringService.actualizarFlgResetPassword(searchEmail.get().getId(), 1);
            }
            String textoEncriptado = EncriptacionUtil.encriptar(username);
            MailUtil mailUtil = new MailUtil();
            String asunto = "Alguien (esperemos que tú) nos ha solicitado restablecer la contraseña de tu cuenta de administrador en Vivian." +
                    "\n\nHaz clic en el siguiente enlace para hacerlo http://localhost:8091/resetpassword?user=" + textoEncriptado + "\n\nSi no solicitaste restablecer la contraseña, puedes ignorar este mensaje.";
            mailUtil.EnviarEmail(username, "Tu contraseña de Vivian", asunto);
            confirmacion.setEstado(ResponseEstado.OK);
            confirmacion.setMensaje("Correo enviado satisfactoriamente.");
            return ResponseEntity.accepted().body(confirmacion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }

    }

    @GetMapping("/resetpassword")
    public String cambiarPassword(@RequestParam String user) throws Exception {
        try {
            String userMas = user.replace(" ", "+");
            userDesencriptado = EncriptacionUtil.desencriptar(userMas, DECRYPT_KEY);
            Optional<UsuarioSpring> searchUser = usuarioSpringService.obtenerPorEmail(userDesencriptado);
            if (searchUser.isPresent()) {
                return "resetpassword";
            } else {
                return "error";
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<Confirmacion> enviarCambioPassword(@RequestParam String password) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            Optional<UsuarioSpring> searchEmail = usuarioSpringService.obtenerPorEmail(userDesencriptado);
            if (searchEmail.isPresent()) {
                if (searchEmail.get().getFlgresetpassword() == 1) {
                    Integer valido = usuarioSpringService.actualizarPassOlvidada(searchEmail.get().getId(), password);
                    if (valido == 1) {
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
        } catch (Exception ex) {
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }


    @GetMapping(value = "/adminusers")
    public List<UsuarioSpring> adminUsers() throws Exception {

        return usuarioSpringService.obtenerAdminUsuarios();
    }

    @PostMapping(value = "/adminusers")
    public ResponseEntity<Confirmacion> insertarAdminUser(@RequestBody UsuarioSpring usuarioSpring) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            Optional<UsuarioSpring> usuarioEmail = usuarioSpringService.obtenerPorEmail(usuarioSpring.getUsername());
            if (usuarioEmail.isPresent()) {
                confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                confirmacion.setMensaje("El username ya existe.");
            } else {
                UsuarioSpring usuarioDni = usuarioSpringService.obtenerPorDni(usuarioSpring.getDni());
                if (usuarioDni != null) {
                    confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                    confirmacion.setMensaje("El DNI ya existe.");
                } else {
                    UsuarioSpring usuarioCreated = usuarioSpringService.crearAdminUsuario(usuarioSpring);
                    if (usuarioCreated != null) {
                        confirmacion.setEstado(ResponseEstado.OK);
                        confirmacion.setMensaje("Usuario ingresado correctamente.");
                    }
                }
            }
            return ResponseEntity.accepted().body(confirmacion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

    @DeleteMapping(value = "/adminusers")
    public ResponseEntity<Confirmacion> deleteAdminUser(@RequestParam Integer id) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            if (usuarioSpringService.eliminarPorId(id)) {
                confirmacion.setEstado(ResponseEstado.OK);
                confirmacion.setMensaje("Usuario eliminado correctamente.");
            } else {
                confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                confirmacion.setMensaje("El usuario no existe.");
            }
            return ResponseEntity.accepted().body(confirmacion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

    @PutMapping(value = "/adminusers")
    public ResponseEntity<Confirmacion> updateAdminUser(@RequestBody UsuarioSpring usuarioSpring) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            confirmacion = usuarioSpringService.actualizarAdminUsuario(usuarioSpring.getId(),
                    usuarioSpring.getDni(), usuarioSpring.getNombresUsuario(), usuarioSpring.getApellidosUsuario(),
                    usuarioSpring.getUsername(), usuarioSpring.getTelefono(), usuarioSpring.getEstado());

            return ResponseEntity.accepted().body(confirmacion);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

}
