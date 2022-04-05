package com.project.vivian.dao;

import com.project.vivian.entidad.DetallesPedido;
import com.project.vivian.entidad.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pedidoDAO")
public interface PedidoDAO extends JpaRepository<Pedido, Integer> {

    @Query(value = "{call VerDetallePedido(:id)}", nativeQuery = true)
    public List<DetallesPedido> getDetallesByPedido(@Param("id") Integer id);

}
