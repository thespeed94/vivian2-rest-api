package com.project.vivian.serviceImpl;

import com.project.vivian.dao.UsuarioSpringDAO;
import com.project.vivian.entidad.UsuarioSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioSpringDAO usuarioSpringDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Buscar el usuario con el repositorio y si no existe lanzar una exepcion
       UsuarioSpring appUser =
                usuarioSpringDAO.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDetails = new User(appUser.getUsername(),appUser.getPassword(),roles);

        return userDetails;
    }
}
