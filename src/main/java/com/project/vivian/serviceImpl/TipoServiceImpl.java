package com.project.vivian.serviceImpl;

import com.project.vivian.dao.TipoDAO;
import com.project.vivian.entidad.Tipo;
import com.project.vivian.service.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tipoService")
public class TipoServiceImpl implements TipoService {

    @Autowired
    private TipoDAO tipoDAO;

    @Override
    public List<Tipo> obtenerTipos() {
        return tipoDAO.findAll();
    }

    @Override
    public Tipo obtenerPorId(Integer id) throws Exception {
        try{
            return tipoDAO.getById(id);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
}
