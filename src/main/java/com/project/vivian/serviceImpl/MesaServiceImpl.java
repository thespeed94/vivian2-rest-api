package com.project.vivian.serviceImpl;

import com.project.vivian.dao.MesasDAO;
import com.project.vivian.dao.TipoDAO;
import com.project.vivian.dao.UsuarioDAO;
import com.project.vivian.entidad.Mesa;
import com.project.vivian.entidad.Tipo;
import com.project.vivian.entidad.Usuario;
import com.project.vivian.entidad.UsuarioSpring;
import com.project.vivian.service.MesaService;
import com.project.vivian.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaServiceImpl implements MesaService {

	@Autowired
	private MesasDAO mesaDAO;

	@Override
	public List<Mesa> listarMesasActivas() throws Exception {
		try {
			return mesaDAO.listarMesasActivas();
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public List<Mesa> listarMesas() throws Exception {
		try {
			return mesaDAO.findAll();
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Integer crearMesa(Mesa entity) throws Exception {
		try {
			if (mesaDAO.save(entity) != null) {
				return 1;
			} else {
				return 0;
			}

		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Integer actualizarMesa(Mesa entity) throws Exception {
		try {
			if (mesaDAO.save(entity) != null) {
				return 1;
			} else {
				return 0;
			}

		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public boolean eliminarPorId(Mesa mesa) {
		mesaDAO.delete(mesa);
		return true;

	}

}
