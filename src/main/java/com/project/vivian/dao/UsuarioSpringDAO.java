package com.project.vivian.dao;

import com.project.vivian.entidad.UsuarioSpring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

@Repository("usuarioSpringDAO")
public interface UsuarioSpringDAO extends JpaRepository<UsuarioSpring,Integer> {

    public Optional<UsuarioSpring> findByUsername(String username);

    public Optional<UsuarioSpring> findByDni(String dni);

    @Query(value = "{call ModificarUsuarioSpring(:id, :dni, :nombresUsuario, :apellidosUsuario, :username, :telefono, :estado, @val)}", nativeQuery = true)
    public Integer updateWithOutPass(@Param("id") Integer id, @Param("dni") String dni, @Param("nombresUsuario") String nombresUsuario,
                                           @Param("apellidosUsuario") String apellidosUsuario, @Param("username") String username, @Param("telefono") String telefono,
                                           @Param("estado") Integer estado);

    @Query(value = "{call ModificarResetPassword(:id, :flgReset, @val)}",nativeQuery = true)
    public Integer updateFlgResetPassword(@Param("id") Integer id, @Param("flgReset") Integer flgReset);

    @Query(value = "{call ModificarPasswordOlvidada(:id, :password, @val)}",nativeQuery = true)
    public Integer updateForgottenPass(@Param("id") Integer id, @Param("password") String password);
}
