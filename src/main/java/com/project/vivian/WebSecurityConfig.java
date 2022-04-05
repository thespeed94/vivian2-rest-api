package com.project.vivian;

import com.project.vivian.serviceImpl.UserDetailsServiceImpl;
import com.project.vivian.serviceImpl.UsuarioSpringServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        String[] resources = new String[]{
                "/include/**","/css/**","/icons/**","/img/**","/js/**", "/js/reportes/**", "/layer/**","/css/libcss/**"
        };

        http
                .authorizeRequests()
                .antMatchers(resources).permitAll()
                .antMatchers("/","/index").permitAll()
                .antMatchers("/adminusers").authenticated()
                .antMatchers("/customerusers").authenticated()
                .antMatchers("/turno").authenticated()
                .antMatchers("/categoria").authenticated()
                .antMatchers("/pedido/**").authenticated()
                .antMatchers("/producto/**").permitAll()
                .antMatchers("/mesas").authenticated()
                .antMatchers("/reserva").authenticated()
                .antMatchers("/recuperation").permitAll()
                .antMatchers("/resetpassword").permitAll()
                .antMatchers("/admin*").access("hasRole('ADMIN')")
                .antMatchers("/user*").access("hasRole('USER')")
                        .anyRequest().authenticated()
                        .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/principal")
                    .failureUrl("/login?error=true")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()
                    .csrf().disable()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/login?logout");
    }

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    //Registra el service para usuarios y el encriptador de contrasena
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }



}
