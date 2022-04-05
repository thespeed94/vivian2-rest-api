package com.project.vivian.serviceImpl;

import com.project.vivian.dao.TipoDAO;
import com.project.vivian.dao.UsuarioDAO;
import com.project.vivian.entidad.Tipo;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.UsuarioSpring;
import com.project.vivian.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private TipoDAO tipoDAO;

    @Override
    public List<Usuario> obtenerCustomerUsuarios() throws Exception {
        try{
            return usuarioDAO.findAll();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Integer crearCustomerUsuario(Usuario entity) throws Exception {
        try{
            Tipo tipoAdmin = tipoDAO.getById(1);
            entity.setIdTipo(tipoAdmin);
            Integer usuarioCreated = usuarioDAO.saveWithOutPass(entity.getDni(),entity.getNombresUsuario(),entity.getApellidosUsuario(),
                    entity.getEmail(),entity.getClave(),entity.getTelefono(),entity.getEstado());
            return usuarioCreated;
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Optional<Usuario> obtenerPorEmail(String email) throws Exception {
        try{
            return usuarioDAO.findByEmail(email);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Optional<Usuario> obtenerPorId(Integer integer) throws Exception {
        try{
            return usuarioDAO.findById(integer);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Optional<Usuario> obtenerPorDni(String dni) throws Exception {
        try{
            return usuarioDAO.findByDni(dni);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public boolean eliminarPorId(Integer integer) {
        try{
            usuarioDAO.deleteById(integer);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public Integer actualizarCustomerUsuario(Integer id, String dni, String nombresUsuario, String apellidosUsuario, String email, String telefono, Integer estado) throws Exception {
        try{
            return usuarioDAO.updateWithOutPass(id,dni,nombresUsuario,apellidosUsuario,email,telefono,estado);
        }catch (Exception ex){
            return 0;
        }
    }
}
