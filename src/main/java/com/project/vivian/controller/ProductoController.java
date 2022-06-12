package com.project.vivian.controller;


import com.project.vivian.entidad.Producto;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.CategoriaService;
import com.project.vivian.service.ProductoService;
import com.project.vivian.service.constants.ResponseEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    private int codigo = 4;

    @GetMapping("")
    public List<Producto> listar() throws Exception {

        return productoService.obtenerProductos();
    }


    @PostMapping("")
    public ResponseEntity<Confirmacion> insertarProducto(@RequestBody Producto producto) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            productoService.agregarProducto(producto);

            confirmacion.setEstado(ResponseEstado.OK);
            confirmacion.setMensaje("Producto ingresado correctamente.");
            
            return ResponseEntity.accepted().body(confirmacion);            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

    @PutMapping("")
    public ResponseEntity<Confirmacion> actualizarProducto(@RequestBody Producto producto) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            productoService.actualizarProducto(producto);

            confirmacion.setEstado(ResponseEstado.OK);
            confirmacion.setMensaje("Producto actualizado correctamente." );

            return ResponseEntity.accepted().body(confirmacion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Confirmacion> eliminarProducto(@RequestParam Integer id) throws Exception {
        Confirmacion confirmacion = new Confirmacion();
        try {
            if (productoService.eliminarProducto(id)){
                confirmacion.setEstado(ResponseEstado.OK);
                confirmacion.setMensaje("Producto eliminado correctamente.");
            } else {
                confirmacion.setEstado(ResponseEstado.ERROR_NEGOCIO);
                confirmacion.setMensaje("El producto no existe.");
            }
            return ResponseEntity.accepted().body(confirmacion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            confirmacion.setEstado(ResponseEstado.ERROR_APLICACION);
            confirmacion.setMensaje(ex.getMessage());
            return ResponseEntity.badRequest().body(confirmacion);
        }
    }

    @GetMapping("/reportes")
    public String reportesProducto(Model model){
        codigo = 10;
        model.addAttribute("verFragmento",codigo);
        return "general-summary";
    }

    @GetMapping("/reportes/cantidad")
    public ResponseEntity<Object[]> reportesCantidadProducto(Model model) throws Exception {

        Object[] cantidadProductosVendidosPorMesData = new Object[]{};
        try {
            cantidadProductosVendidosPorMesData = productoService.reporteCantidadProductosVentidadPorMes();
            return ResponseEntity.accepted().body(cantidadProductosVendidosPorMesData);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(cantidadProductosVendidosPorMesData);
        }
    }

    @GetMapping("/reportes/ingresos")
    public ResponseEntity<Object[]> reportesTopIngresosProducto(Model model) throws Exception {

        Object[] topIngresosProducto = new Object[]{};
        try {
            topIngresosProducto = productoService.reporteProductoConMasIngresos();
            return ResponseEntity.accepted().body(topIngresosProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(topIngresosProducto);
        }
    }

    @GetMapping("/reportes/ingresosxmes")
    public ResponseEntity<Object[]> reportesTopIngresosProductoPorMes(@RequestParam Integer mes, Model model) throws Exception {

        Object[] topIngresosProductoPorMes = new Object[]{};
        try {
            topIngresosProductoPorMes = productoService.reporteProductoConMasIngresosPorMes(mes);
            return ResponseEntity.accepted().body(topIngresosProductoPorMes);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(topIngresosProductoPorMes);
        }
    }

    @GetMapping("/reportes/categoriasxmes")
    public ResponseEntity<Object[]> reportesTopCategoriasVendidasPorMes(@RequestParam Integer mes, Model model) throws Exception {

        Object[] topCategoriasVendidasPorMes = new Object[]{};
        try {
            topCategoriasVendidasPorMes = productoService.reporteCategoriasVendidasPorMes(mes);
            return ResponseEntity.accepted().body(topCategoriasVendidasPorMes);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(topCategoriasVendidasPorMes);
        }
    }

    @GetMapping("/reportes/gs")
    public ResponseEntity<Object[]> dataParaGeneralSummary(Model model) throws Exception {

        Object[] dataParaGeneralSummary = new Object[]{};
        try {
            dataParaGeneralSummary = productoService.obtenerDataParaGeneralSummary();
            return ResponseEntity.accepted().body(dataParaGeneralSummary);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(dataParaGeneralSummary);
        }
    }

}
