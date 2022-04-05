package com.project.vivian;

import com.project.vivian.dao.UsuarioSpringDAO;
import com.project.vivian.service.util.EncriptacionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class VivianApplicationTests {

	@Autowired
	UsuarioSpringDAO usuarioDAO;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Test
	void obtenerContraseñaEncriptada() {
		System.out.println("Contraseña encriptada:");
		System.out.println(passwordEncoder.encode("1234"));
	}

	@Test
	void obtenerTextoEncriptado() throws Exception {
		System.out.println("Texto encriptado:");
		System.out.println(EncriptacionUtil.encriptar("mauroar1001@hotmail.com"));
	}

	@Test
	void obtenerTextoDesencriptado() throws Exception{
		System.out.println("Texto descriptado:");
		System.out.println(EncriptacionUtil.desencriptar("+ZaaO3/cJ+1knF19Zuqz+cTqX+ADERzp","AplicacionWebUno"));
	}

}
