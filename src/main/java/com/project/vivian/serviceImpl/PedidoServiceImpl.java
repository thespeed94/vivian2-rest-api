package com.project.vivian.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.project.vivian.dao.DetallesPedidoDAO;
import com.project.vivian.dao.PedidoDAO;
import com.project.vivian.entidad.DetallesPedido;
import com.project.vivian.entidad.Pedido;
import com.project.vivian.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private DetallesPedidoDAO detallesPedidoDAO;

    @Override
    public List<Pedido> obtenerPedidos() throws Exception {
        try{
            return pedidoDAO.findAll();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public List<DetallesPedido> obtenerDetalles(Integer id) throws Exception {
        try{
            return detallesPedidoDAO.findByIdPedido(id);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Pedido obtenerPedidoPorId(Integer id) throws Exception {
        try{
            return pedidoDAO.findById(id).orElse(null);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public PdfPCell getCell(String text1, String text2, int alignment, com.itextpdf.text.Font font1, com.itextpdf.text.Font font2) {
        PdfPCell cell = new PdfPCell();
        Phrase frase1= new Phrase(text1,font1);
        Phrase frase2= new Phrase(text2,font2);
        cell.addElement(frase1);
        cell.addElement(frase2);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    public PdfPCell getCell2(String text3, String text1, String text2, int alignment, com.itextpdf.text.Font font1, com.itextpdf.text.Font font2,com.itextpdf.text.Font font3) {
        PdfPCell cell = new PdfPCell();
        Phrase frase3= new Phrase(text3,font3);
        Phrase frase1= new Phrase(text1,font1);
        Phrase frase2= new Phrase(text2,font2);
        cell.addElement(frase3);
        cell.addElement(frase1);
        cell.addElement(frase2);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    public PdfPCell getCellImg(Image img, int alignment) {
        PdfPCell cell = new PdfPCell(img);
        cell.setPadding(0);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    @Override
    public void openPdf(Integer id, HttpServletResponse response) throws Exception {
        try{
            response.setContentType("application/pdf");
            List<DetallesPedido> detallesPdf = obtenerDetalles(id);
            Pedido pedido = obtenerPedidoPorId(id);

            OutputStream out = response.getOutputStream();
            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, out);
            document.open();
            document.setMargins(75, 36, 75, 36);
            com.itextpdf.text.Font negrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            com.itextpdf.text.Font rucFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            com.itextpdf.text.Font datosBoleta = FontFactory.getFont(FontFactory.HELVETICA, 13);
            com.itextpdf.text.Font tituloBoletaVenta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            com.itextpdf.text.Font montoTotalFont = FontFactory.getFont(FontFactory.HELVETICA, 16);
            com.itextpdf.text.Font tituloBodega = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28);
            document.addTitle("Boleta");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = new URL("https://i.ibb.co/XXBxFW6/corazon.png").openStream();
            byte[] b = new byte[4096];
            int n;
            while ( (n = is.read(b)) > -1 ) {
                baos.write(b, 0, n);
            }

            Image img = Image.getInstance(baos.toByteArray());

            img.scaleAbsolute(70f, 70f);
            img.setAlignment(Element.ALIGN_CENTER);

            String padded = String.format("%07d" , id);

            PdfPTable tableCabecera = new PdfPTable(3);
            tableCabecera.setWidthPercentage(100);
            tableCabecera.setTotalWidth(new float[]{ 65, 25, 75});
            tableCabecera.addCell(getCell("VIVIAN\n","Jr. Rafael Saucedo #7071\nSAN ISIDRO - LIMA - LIMA", PdfPCell.ALIGN_LEFT,tituloBodega,datosBoleta));
            tableCabecera.addCell(getCellImg(img,PdfPCell.ALIGN_CENTER));
            tableCabecera.addCell(getCell2("RUC 20503644968","BOLETA DE VENTA ELECTRONICA\n","B001-  N° "+padded, PdfPCell.ALIGN_CENTER,tituloBoletaVenta,datosBoleta,rucFont));
            tableCabecera.setSpacingAfter(15f);
            document.add(tableCabecera);

            //DottedLineSeparator separator = new DottedLineSeparator();
            LineSeparator separator= new LineSeparator();
            separator.setPercentage(25500f / 255f);
            Chunk linebreak = new Chunk(separator);
            document.add(linebreak);

            PdfPTable tableDescripcion = new PdfPTable(2);
            tableDescripcion.setWidthPercentage(100);
            tableDescripcion.setTotalWidth(new float[]{75,75});
            tableDescripcion.addCell(getCell("Cliente: "+pedido.getIdUsuario().getNombresUsuario()+" "+pedido.getIdUsuario().getApellidosUsuario(),
                    "DNI: "+pedido.getIdUsuario().getDni()+"\nTelefono: "+pedido.getIdUsuario().getTelefono(), PdfPCell.ALIGN_LEFT,datosBoleta,datosBoleta));
            tableDescripcion.addCell(getCell("Fecha de compra: \n",pedido.fechaString(), PdfPCell.ALIGN_RIGHT,datosBoleta,datosBoleta));
            tableDescripcion.setSpacingAfter(15f);
            document.add(tableDescripcion);

            LineSeparator separator2= new LineSeparator();
            separator.setPercentage(25500f / 255f);
            Chunk linebreak2 = new Chunk(separator2);
            document.add(linebreak2);

            PdfPTable tableDetalle=new PdfPTable(4);
            tableDetalle.setTotalWidth(new float[]{ 90, 240 ,90,90});
            tableDetalle.setLockedWidth(true);
            PdfPCell cell = new  PdfPCell(new Phrase("CANTIDAD",negrita));
            cell.setFixedHeight(20);
            tableDetalle.addCell(cell);
            cell = new  PdfPCell(new Phrase("DESCRIPCION",negrita));
            cell.setFixedHeight(20);
            tableDetalle.addCell(cell);
            cell = new  PdfPCell(new Phrase("P. UNIT",negrita));
            cell.setFixedHeight(20);
            tableDetalle.addCell(cell);
            cell = new  PdfPCell(new Phrase("SUBTOTAL",negrita));
            cell.setFixedHeight(20);
            tableDetalle.addCell(cell);

            double montoTotal=0;
            for(DetallesPedido detalle:detallesPdf){
                tableDetalle.addCell(new Phrase(detalle.getCantidad()+"",datosBoleta));
                tableDetalle.addCell(new Phrase(detalle.getIdProducto().getNombreProducto()+"",datosBoleta));
                tableDetalle.addCell(new Phrase("S/ "+detalle.getPrecio(),datosBoleta));
                tableDetalle.addCell(new Phrase("S/ "+detalle.getPrecio()*detalle.getCantidad()+"",datosBoleta));
                montoTotal+=detalle.getPrecioTotal()*detalle.getCantidad();
            }
            tableDetalle.setSpacingBefore(20f);
            tableDetalle.setSpacingAfter(40f);
            document.add(tableDetalle);

            Paragraph fraseMonto= new Paragraph();
            fraseMonto.add(Chunk.NEWLINE);
            fraseMonto.add(new Phrase("IMPORTE TOTAL: S/ "+montoTotal+"",montoTotalFont));
            fraseMonto.setAlignment(Element.ALIGN_RIGHT);
            document.add(fraseMonto);



            document.close();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }

    }
}
