package com.project.vivian.serviceImpl;

import com.project.vivian.dao.ProductoDAO;
import com.project.vivian.entidad.Producto;
import com.project.vivian.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoDAO productoDAO;

    @Override
    public List<Producto> obtenerProductos() throws Exception {
        try {
            return productoDAO.findAll();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Producto agregarProducto(Producto producto) throws Exception {
        try{
            return productoDAO.save(producto);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Producto actualizarProducto(Producto producto) throws Exception {
        try{
            return productoDAO.save(producto);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public boolean eliminarProducto(Integer id) throws Exception {
        try{
            productoDAO.deleteById(id);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public Object[] reporteCantidadProductosVentidadPorMes() throws Exception {
        try{
            return productoDAO.getReportQuantitySoldProductsByMonth();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Object[] reporteProductoConMasIngresos() throws Exception {
        try{
            return productoDAO.getReportMostValuableProduct();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Object[] reporteProductoConMasIngresosPorMes(Integer mes) throws Exception {
        try{
            return productoDAO.getReportMostValuableProductPerMonth(mes);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Object[] reporteCategoriasVendidasPorMes(Integer mes) throws Exception {
        try{
            return productoDAO.getReportQuantitySoldCategoriesPerMonth(mes);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Object[] obtenerDataParaGeneralSummary() throws Exception {
        try{
            return productoDAO.getDataforGeneralSummary();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
}
