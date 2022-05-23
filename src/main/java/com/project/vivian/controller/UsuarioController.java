package com.project.vivian.controller;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.UsuarioService;
import com.project.vivian.service.constants.ResponseEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customerusers")
@CrossOrigin(origins = "*")
public class UsuarioController {

    HashMap<String, Object> output = new HashMap<String, Object>();

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping @ResponseBody
    public ResponseEntity<List<Usuario>> listarTodos() throws Exception {
        return ResponseEntity.ok(usuarioService.obtenerCustomerUsuarios());
    }

    @PostMapping @ResponseBody
    public ResponseEntity<HashMap<String, Object>> insertarCustomerUser(@RequestBody Usuario u) throws Exception {
        try {
            usuarioService.crearCustomerUsuario(u);
            output.put("msg", "insertado correcto");
        }

        catch(Exception e) {
            e.printStackTrace();
            output.put("msg", e.getMessage());
        }

        return ResponseEntity.ok(output);
    }

    @DeleteMapping @ResponseBody
    public ResponseEntity<Confirmacion> deleteCustomerUser(@RequestParam Integer id) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try{
            if (usuarioService.eliminarPorId(id)){
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

    @PutMapping("")
    public ResponseEntity<Confirmacion> updateCustomerUser(Usuario usuario) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try{
            Optional<Usuario> searchEmail = usuarioService.obtenerPorEmail(usuario.getEmail());
            Optional<Usuario> searchDni = usuarioService.obtenerPorDni(usuario.getDni());
            Optional<Usuario> usuarioActualizar = usuarioService.obtenerPorId(usuario.getId());

            if (searchEmail.isEmpty() && searchDni.isEmpty()){
                Integer valido = usuarioService.actualizarCustomerUsuario(usuario.getId(), usuario.getDni(),
                        usuario.getNombresUsuario(), usuario.getApellidosUsuario(), usuario.getEmail(), usuario.getTelefono(), usuario.getEstado());
                if (valido == 1){
                    confirmacion.setEstado(ResponseEstado.OK);
                    confirmacion.setMensaje("Usuario actualizado correctamente.");
                }
            } else if (searchEmail.isEmpty() && searchDni.isPresent()){
                if (searchDni.get().getId() == usuarioActualizar.get().getId()){
                    Integer valido = usuarioService.actualizarCustomerUsuario(usuario.getId(), usuario.getDni(),
                            usuario.getNombresUsuario(), usuario.getApellidosUsuario(), usuario.getEmail(), usuario.getTelefono(), usuario.getEstado());
                    if (valido == 1){
                        confirmacion.setEstado(ResponseEstado.OK);
                        confirmacion.setMensaje("Usuario actualizado correctamente.");
                    }
                } else {
                    confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                    confirmacion.setMensaje("El DNI ya existe.");
                }
            } else if (searchEmail.isPresent() && searchDni.isEmpty()){
                if (searchEmail.get().getId() == usuarioActualizar.get().getId()){
                    Integer valido = usuarioService.actualizarCustomerUsuario(usuario.getId(), usuario.getDni(),
                            usuario.getNombresUsuario(), usuario.getApellidosUsuario(), usuario.getEmail(), usuario.getTelefono(), usuario.getEstado());
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
                    if (searchDni.get().getId() == usuarioActualizar.get().getId()){
                        Integer valido = usuarioService.actualizarCustomerUsuario(usuario.getId(), usuario.getDni(),
                                usuario.getNombresUsuario(), usuario.getApellidosUsuario(), usuario.getEmail(), usuario.getTelefono(), usuario.getEstado());
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
