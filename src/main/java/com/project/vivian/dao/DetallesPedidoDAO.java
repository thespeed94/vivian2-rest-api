package com.project.vivian.dao;

import com.project.vivian.entidad.DetallesPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("detallesPedidoDAO")
public interface DetallesPedidoDAO extends JpaRepository<DetallesPedido, Integer> {

    public List<DetallesPedido> findByIdPedido(Integer id);
}
