package com.project.vivian.controller;

import com.project.vivian.entidad.Categoria;
import com.project.vivian.entidad.DetallesPedido;
import com.project.vivian.entidad.Pedido;
import com.project.vivian.entidad.Producto;
import com.project.vivian.service.PedidoService;
import com.project.vivian.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    private int codigo = 8;

    @GetMapping("")
    public String listar(Model model) throws Exception {
        codigo = 8;
        List<Pedido> pedidos = pedidoService.obtenerPedidos();
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("verFragmento", codigo);
        return "general-summary";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable("id") Integer id, Model model) throws Exception {
        codigo = 9;
        List<DetallesPedido> detalles = pedidoService.obtenerDetalles(id);
        model.addAttribute("detalles", detalles);
        model.addAttribute("idpedido", id);
        model.addAttribute("verFragmento", codigo);
        return "general-summary";
    }

    @GetMapping("/pdf/{id}")
    public void verPdf(@PathVariable("id") Integer id, Model model, HttpServletResponse response) throws Exception {
        try{
            pedidoService.openPdf(id, response);

        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

}
