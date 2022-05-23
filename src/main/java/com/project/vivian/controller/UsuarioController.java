package com.project.vivian.controller;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/{id}") @ResponseBody
    public ResponseEntity<HashMap<String, Object>> deleteCustomerUser(@PathVariable int id) throws Exception {
        try {
            usuarioService.eliminarPorId(id);
            output.put("msg", "Usuario eliminado");
        }

        catch(Exception e) {
            e.printStackTrace();
            output.put("msg", e.getMessage());
        }

        return ResponseEntity.ok(output);
    }

    @PutMapping @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updateCustomerUser(@RequestBody Usuario u) throws Exception {
        try {
            Optional<Usuario> searchEmail = usuarioService.obtenerPorEmail(u.getEmail());
            Optional<Usuario> searchDni = usuarioService.obtenerPorDni(u.getDni());
            Optional<Usuario> usuarioActualizar = usuarioService.obtenerPorId(u.getId());

            if (searchEmail.isEmpty() && searchDni.isEmpty()){
                usuarioService.actualizarCustomerUsuario(u.getId(), u.getDni(), u.getNombresUsuario(), u.getApellidosUsuario(), u.getEmail(), u.getTelefono(), u.getEstado());
                output.put("msg", "Usuario actualizado");
            } 
            
            else if (searchEmail.isEmpty() && searchDni.isPresent()){
                if (searchDni.get().getId() == usuarioActualizar.get().getId()){
                    Integer valido = usuarioService.actualizarCustomerUsuario(u.getId(), u.getDni(), 
                    u.getNombresUsuario(), u.getApellidosUsuario(), u.getEmail(), u.getTelefono(), u.getEstado());

                    if (valido == 1) output.put("msg", "Usuario actualizado");
                } 
                
                else {
                    output.put("msg", "El dni ya existe");
                }
            } 
            
            else if (searchEmail.isPresent() && searchDni.isEmpty()) {
                if (searchEmail.get().getId() == usuarioActualizar.get().getId()) {
                    Integer valido = usuarioService.actualizarCustomerUsuario(u.getId(), u.getDni(), 
                    u.getNombresUsuario(), u.getApellidosUsuario(), u.getEmail(), u.getTelefono(), u.getEstado());

                    if (valido == 1) output.put("msg", "Usuario actualizado");
                } 
                
                else {
                    output.put("msg", "El usuario ya existe");
                }
            } 
            
            else {
                if (searchEmail.get().getId() == usuarioActualizar.get().getId()) {

                    if (searchDni.get().getId() == usuarioActualizar.get().getId()) {
                        Integer valido = usuarioService.actualizarCustomerUsuario(u.getId(), u.getDni(),
                        u.getNombresUsuario(), u.getApellidosUsuario(), u.getEmail(), u.getTelefono(), u.getEstado());

                        if (valido == 1) output.put("msg", "Usuario actualizado");
                    } 
                    
                    else {
                        output.put("msg", "El dni ya existe");
                    }
                } 
                
                else {
                    output.put("msg", "El usuario ya existe");
                }
            }
        }
        
        catch (Exception e){
            e.printStackTrace();
            output.put("msg", e.getMessage());
        }

        return ResponseEntity.ok(output);
    }

}
