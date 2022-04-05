package com.project.vivian.dao;

import com.project.vivian.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("usuarioDAO")
public interface UsuarioDAO extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByEmail(String email);
    public Optional<Usuario> findByDni(String dni);

    @Query(value = "{call AgregarUsuario(:dni, :nombresUsuario, :apellidosUsuario, :email, :clave, :telefono, 1, :estado,@val)}", nativeQuery = true)
    public Integer saveWithOutPass(@Param("dni") String dni, @Param("nombresUsuario") String nombresUsuario, @Param("apellidosUsuario") String apellidosUsuario,
                                   @Param("email") String email, @Param("clave") String clave, @Param("telefono") String telefono, @Param("estado") Integer estado);

    @Query(value = "{call ModificarUsuario(:id, :dni, :nombresUsuario, :apellidosUsuario, :email, :telefono, :estado, @val)}", nativeQuery = true)
    public Integer updateWithOutPass(@Param("id") Integer id, @Param("dni") String dni, @Param("nombresUsuario") String nombresUsuario,
                                     @Param("apellidosUsuario") String apellidosUsuario, @Param("email") String email, @Param("telefono") String telefono,
                                     @Param("estado") Integer estado);

}
