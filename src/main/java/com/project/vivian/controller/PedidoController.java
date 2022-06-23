package com.project.vivian.controller;

import com.project.vivian.entidad.Categoria;
import com.project.vivian.entidad.DetallesPedido;
import com.project.vivian.entidad.Pedido;
import com.project.vivian.entidad.Producto;
import com.project.vivian.entidad.general.Confirmacion;
import com.project.vivian.service.PedidoService;
import com.project.vivian.service.ProductoService;
import com.project.vivian.service.constants.ResponseEstado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    private int codigo = 8;

    @GetMapping @ResponseBody
    public ResponseEntity<List<Pedido>> listar() throws Exception {

        return ResponseEntity.ok(pedidoService.obtenerPedidos());
    }
    
    
    /*
    Dejo coment, no estoy seguro si este metodo se cambia
    @GetMapping("")
    public String listar(Model model) throws Exception {
        codigo = 8;
        List<Pedido> pedidos = pedidoService.obtenerPedidos();
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("verFragmento", codigo);
        return "general-summary";
    }*/
    
    @GetMapping("/{id}") @ResponseBody
    public ResponseEntity<Map<String, Object>> verDetalle(@PathVariable("id") Integer id) throws Exception {
    	Map<String, Object> response = new HashMap<>();
    	try {
    		codigo = 9;
    		List<DetallesPedido> detalles = pedidoService.obtenerDetalles(id);
    		response.put("detalles", detalles);
    		response.put("idpedido", id);
    		response.put("verFragmento", codigo);
    		return ResponseEntity.ok(response);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
            response.put("estado", ResponseEstado.ERROR_NEGOCIO);
    		response.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
		}
        
    }

    /*@GetMapping("/1{id}")
    public String verDetalle2(@PathVariable("id") Integer id, Model model) throws Exception {
        codigo = 9;
        List<DetallesPedido> detalles = pedidoService.obtenerDetalles(id);
        model.addAttribute("detalles", detalles);
        model.addAttribute("idpedido", id);
        model.addAttribute("verFragmento", codigo);
        return "general-summary";
    }*/

    @ResponseBody
    @GetMapping(value="/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> verPdf(@PathVariable("id") Integer id, Model model) throws Exception {
        try{
        	ByteArrayInputStream bis = pedidoService.openPdfTwo(id);
        	HttpHeaders headers = new HttpHeaders();
        	headers.add("Content-Disposition", "inline; filename=details.pdf");
            System.out.println("Terminamos de contruir");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

}
