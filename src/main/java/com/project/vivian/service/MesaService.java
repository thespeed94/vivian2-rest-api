package com.project.vivian.service;

import com.project.vivian.entidad.Mesa;
import com.project.vivian.entidad.Usuario;

import java.util.List;
import java.util.Optional;

public interface MesaService {
    public List<Mesa> listarMesasActivas() throws Exception;
    public List<Mesa> listarMesas() throws Exception;
    public Integer crearMesa(Mesa entity) throws Exception;
    public Integer actualizarMesa(Mesa entity) throws Exception;
    public boolean eliminarPorId(Mesa mesa);
}
