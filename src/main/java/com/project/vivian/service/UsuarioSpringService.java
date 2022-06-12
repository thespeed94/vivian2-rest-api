package com.project.vivian.service;

import com.project.vivian.entidad.UsuarioSpring;
import com.project.vivian.entidad.general.Confirmacion;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UsuarioSpringService {
    public String login(String username, String password) throws Exception;
    public List<UsuarioSpring> obtenerAdminUsuarios() throws Exception;
    public UsuarioSpring crearAdminUsuario(UsuarioSpring entity) throws Exception;
    Optional<UsuarioSpring> obtenerPorEmail(String email);
    public Optional<UsuarioSpring> obtenerPorId(Integer integer);
    public UsuarioSpring obtenerPorDni(String dni) throws Exception;
    public boolean eliminarPorId(Integer integer);
    public Confirmacion actualizarAdminUsuario(Integer id, String dni, String nombresUsuario, String apellidosUsuario, String username, String telefono, Integer estado) throws Exception;
    public Integer actualizarFlgResetPassword(Integer id, Integer flgReset) throws Exception;
    public Integer actualizarPassOlvidada(Integer id, String pass) throws Exception;
}
