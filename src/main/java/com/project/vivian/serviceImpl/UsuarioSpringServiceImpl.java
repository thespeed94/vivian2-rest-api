package com.project.vivian.serviceImpl;

import com.project.vivian.dao.TipoDAO;
import com.project.vivian.dao.UsuarioSpringDAO;
import com.project.vivian.entidad.Tipo;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.UsuarioSpring;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.UsuarioSpringService;
import com.project.vivian.service.constants.ResponseEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("usuarioService")
public class UsuarioSpringServiceImpl implements UsuarioSpringService {

    @Autowired
    UsuarioSpringDAO usuarioSpringDAO;

    @Autowired
    TipoDAO tipoDAO;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) throws Exception {
        UsuarioSpring usuario = usuarioSpringDAO.findByUsername(username).orElse(null);
        String str;
        if (usuario != null) {
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                str = UUID.randomUUID().toString();
            } else {
                str = "0";
            }
        } else {
            str = "-1";
        }
        return str;
    }

    @Override
    public List<UsuarioSpring> obtenerAdminUsuarios() throws Exception {
        try {
            List<UsuarioSpring> lista = usuarioSpringDAO.findAll();
            return lista;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Optional<UsuarioSpring> obtenerPorEmail(String email) {
        return usuarioSpringDAO.findByUsername(email);
    }

    @Override
    public Optional<UsuarioSpring> obtenerPorId(Integer integer) {
        Optional<UsuarioSpring> usuarioSpring = usuarioSpringDAO.findById(integer);
        return usuarioSpring;
    }

    @Override
    public UsuarioSpring obtenerPorDni(String dni) throws Exception {
        UsuarioSpring usuario = usuarioSpringDAO.findByDni(dni).orElse(null);
        return usuario;
    }

    @Override
    public boolean eliminarPorId(Integer integer) {
        try {
            usuarioSpringDAO.deleteById(integer);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public UsuarioSpring crearAdminUsuario(UsuarioSpring entity) throws Exception {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Tipo tipoAdmin = tipoDAO.getById(3);
            entity.setIdTipo(tipoAdmin);
            entity.setFechaRegistro(now.toInstant(ZoneOffset.UTC));
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            UsuarioSpring usuarioCreated = usuarioSpringDAO.save(entity);
            return usuarioCreated;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Confirmacion actualizarAdminUsuario(Integer id, String dni, String nombresUsuario, String apellidosUsuario, String username, String telefono, Integer estado) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {

            Optional<UsuarioSpring> searchEmail = usuarioSpringDAO.findByUsername(username);
            Optional<UsuarioSpring> searchDni = usuarioSpringDAO.findByDni(dni);
            Optional<UsuarioSpring> usuarioActualizar = usuarioSpringDAO.findById(id);

            if (usuarioActualizar.isPresent()) {
                if (searchEmail.isPresent() && !searchEmail.get().getId().equals(id)) {
                    confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                    confirmacion.setMensaje("El email ya existe");
                    return confirmacion;
                }
                if (searchDni.isPresent() && !searchDni.get().getId().equals(id)) {
                    confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                    confirmacion.setMensaje("El dni ya existe");
                    return confirmacion;
                }

                UsuarioSpring usuario = usuarioActualizar.get();
                usuario.setDni(dni);
                usuario.setNombresUsuario(nombresUsuario);
                usuario.setApellidosUsuario(apellidosUsuario);
                usuario.setUsername(username);
                usuario.setTelefono(telefono);
                usuario.setEstado(estado);
                usuarioSpringDAO.save(usuario);
            } else {
                confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                confirmacion.setMensaje("El usuario no existe");
            }

            confirmacion.setEstado(ResponseEstado.OK);
            confirmacion.setMensaje("Usuario actualizado correctamente");
            return confirmacion;

        } catch (Exception ex) {
            ex.printStackTrace();
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje("Error al actualizar usuario");
            return confirmacion;
        }
    }

    @Override
    public Integer actualizarFlgResetPassword(Integer id, Integer flgReset) throws Exception {
        try {
            return usuarioSpringDAO.updateFlgResetPassword(id, flgReset);
        } catch (Exception ex) {
            return 0;
        }
    }

    @Override
    public Integer actualizarPassOlvidada(Integer id, String pass) throws Exception {
        try {
            String passEncoded = passwordEncoder.encode(pass);
            return usuarioSpringDAO.updateForgottenPass(id, passEncoded);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
