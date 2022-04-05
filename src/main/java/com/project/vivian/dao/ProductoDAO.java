package com.project.vivian.dao;

import com.project.vivian.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("productoDAO")
public interface ProductoDAO extends JpaRepository<Producto, Integer> {

    @Query(value = "{call ReporteCantidadProductosVendidosPorMeses()}", nativeQuery = true)
    public Object[] getReportQuantitySoldProductsByMonth();

    @Query(value = "{call ReporteTopProductosIngresos()}", nativeQuery = true)
    public Object[] getReportMostValuableProduct();

    @Query(value = "{call ReporteTopProductosVendidosPorMes(:mes)}", nativeQuery = true)
    public Object[] getReportMostValuableProductPerMonth(@Param("mes") Integer mes);

    @Query(value = "{call ReporteTopCategoriasVendidasPorMes(:mes)}", nativeQuery = true)
    public Object[] getReportQuantitySoldCategoriesPerMonth(@Param("mes") Integer mes);

    @Query(value = "{call ReportesProductoGeneralSummary()}", nativeQuery = true)
    public Object[] getDataforGeneralSummary();

}
