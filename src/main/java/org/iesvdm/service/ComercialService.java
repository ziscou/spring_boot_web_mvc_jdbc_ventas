package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDTO;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {
	
	private ComercialDAO comercialDAO;
	private PedidoDAO pedidoDAO;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}
	
	public List<Comercial> listAll() {
		
		return comercialDAO.getAll();
		
	}

	public List<PedidoDTO> listAllPedidoDTO(int id) {
		
		return pedidoDAO.getAllByComercialId(id);
		
	}
	public Comercial one(Integer id) {
		Optional<Comercial> optCom = comercialDAO.find(id);
		if (optCom.isPresent())
			return optCom.get();
		else 
			return null;
	}
	
	public void newComercial(Comercial comercial) {
		
		comercialDAO.create(comercial);
		
	}
	
	public void replaceComercial(Comercial comercial) {
		
		comercialDAO.update(comercial);
		
	}
	
	public void deleteComercial(int id) {
		
		comercialDAO.delete(id);
		
	}
	
	

}