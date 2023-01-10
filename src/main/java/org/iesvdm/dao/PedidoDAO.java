package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDTO;

public interface PedidoDAO {

	public void create(Pedido pedido);
	
	public List<PedidoDTO> getAllByComercialId(int id_comer);
	
	public List<Pedido> getAll();
	
	public Optional<Pedido>  find(int id);
	
	public void update(Pedido pedido);
	
	public void delete(int id);
	
}
