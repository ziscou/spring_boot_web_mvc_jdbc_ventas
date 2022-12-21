package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	
	private ClienteDAO clienteDAO;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ClienteService(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	
	public List<Cliente> listAll() {
		
		return clienteDAO.getAll();
		
	}
	public Cliente one(Integer id) {
		Optional<Cliente> optCli = clienteDAO.find(id);
		if (optCli.isPresent())
			return optCli.get();
		else 
			return null;
	}
	
	public void newCliente(Cliente cliente) {
		
		clienteDAO.create(cliente);
		
	}
	
	public void replaceCliente(Cliente cliente) {
		
		clienteDAO.update(cliente);
		
	}
	
	public void deleteCliente(int id) {
		
		clienteDAO.delete(id);
		
	}
	
	

}