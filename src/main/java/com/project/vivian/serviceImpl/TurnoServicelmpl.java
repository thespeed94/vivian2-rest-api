package com.project.vivian.serviceImpl;
import com.project.vivian.dao.TurnosDAO;
import com.project.vivian.entidad.Turno;
import com.project.vivian.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("turnoService")
public class TurnoServicelmpl implements TurnoService {

    @Autowired
    private TurnosDAO turnoDAO;

	@Override
	public Turno agregarTurno(Turno turno) throws Exception {
        try {
        	return turnoDAO.save(turno);
            //return turnoDAO.saveTurno(id,turno.getDescripcion(),turno.getHorario());
        }
        
        catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}

    @Override
    public String darId() {
        return turnoDAO.obtenerUltimoId();
    }

	@Override
	public List<Turno> listarTurnos() throws Exception {
        try {
            return turnoDAO.findAll();
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}

	@Override
	public int actualizarTurno(Turno turno) throws Exception {
        try {
        	turnoDAO.save(turno);
            return 1;
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}

	@Override
	public int eliminarTurno(int id) throws Exception {
        try { 
            return turnoDAO.deleteWithId(id);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
	}

    @Override
    public Optional<Turno> listarPorId(int id) {
       return turnoDAO.findById(id);
    }

   
}