package com.project.vivian.service;

import com.project.vivian.entidad.DetallesPedido;
import com.project.vivian.entidad.Pedido;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PedidoService {

    public List<Pedido> obtenerPedidos() throws Exception;
    public Pedido obtenerPedidoPorId(Integer id) throws Exception;
    public List<DetallesPedido> obtenerDetalles(Integer id) throws Exception;
    public void openPdf(Integer id, HttpServletResponse response) throws Exception;
}
