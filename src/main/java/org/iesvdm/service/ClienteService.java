package org.iesvdm.service;

import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	
	private ClienteDAO clienteDAO;
	private PedidoDAO pedidoDAO;
	private ComercialDAO comercialDAO;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ClienteService(ClienteDAO clienteDAO, PedidoDAO pedidoDAO, ComercialDAO comercialDAO) {
		this.clienteDAO = clienteDAO;
		this.pedidoDAO = pedidoDAO;
		this.comercialDAO = comercialDAO;
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
	
	public record VectorStats(Long trim, Long sem, Long anio, Long lustro) {};
	
	public Map< Comercial, VectorStats> estadisticasPedidosClientePorComercial(int id_Cliente) {
		
		List<Pedido> listaPed = pedidoDAO.getAllByClienteId(id_Cliente);
		
		Map<Integer, Comercial> mapComerciales = listaPed.stream().map(Pedido::getIdComercial).distinct().map( idComercial -> comercialDAO.find(idComercial).get())
				.collect(toMap(Comercial::getId, Function.identity()));
		
		// Stats por Trimestre
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -3);
		Date FechaTrimestre = c.getTime();
		
		Map<Comercial,DoubleSummaryStatistics> mapStatisticsTrimestre = listaPed.stream()
                .filter(p -> p.getFecha().after(FechaTrimestre))
                .collect(groupingBy(p -> mapComerciales.get(p.getIdComercial()), summarizingDouble(Pedido::getTotal)));
		
		// Stats por Trimestre
		
		c = Calendar.getInstance();
		c.add(Calendar.MONTH, -6);
		Date FechaSemestre = c.getTime();
		
		Map<Comercial,DoubleSummaryStatistics> mapStatisticsSemestre = listaPed.stream()
                .filter(p -> p.getFecha().after(FechaSemestre))
                .collect(groupingBy(p -> mapComerciales.get(p.getIdComercial()), summarizingDouble(Pedido::getTotal)));
		
		// Stats por Trimestre
		
		c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		Date FechaAnio = c.getTime();
		
		Map<Comercial,DoubleSummaryStatistics> mapStatisticsAnio = listaPed.stream()
                .filter(p -> p.getFecha().after(FechaAnio))
                .collect(groupingBy(p -> mapComerciales.get(p.getIdComercial()), summarizingDouble(Pedido::getTotal)));
		
		// Stats por Trimestre
		
		c = Calendar.getInstance();
		c.add(Calendar.YEAR, -5);
		Date FechaLostro = c.getTime();
		
		Map<Comercial,DoubleSummaryStatistics> mapStatisticsLustro = listaPed.stream()
                .filter(p -> p.getFecha().after(FechaLostro))
                .collect(groupingBy(p -> mapComerciales.get(p.getIdComercial()), summarizingDouble(Pedido::getTotal)));
		
		//LinkedHashMap para guardar el orden 
		
			Map<Comercial, VectorStats> mapComStats = new LinkedHashMap<>();
			Comparator<Comercial> comp = (o1, o2) -> o1.getNombre().compareTo(o2.getNombre());
			
			mapComerciales.values().stream().sorted(comp
					.thenComparing((o1, o2) -> o1.getApellido1().compareTo(o2.getApellido1()))
					.thenComparing((o1, o2) -> o1.getApellido2().compareTo(o2.getApellido2()))).forEach(com -> mapComStats.put(com, 
							new VectorStats( (mapStatisticsTrimestre.get(com) != null)? mapStatisticsTrimestre.get(com).getCount():0,
									(mapStatisticsSemestre.get(com) != null)?mapStatisticsSemestre.get(com).getCount():0,
									(mapStatisticsAnio.get(com) != null)?mapStatisticsAnio.get(com).getCount():0,
									(mapStatisticsLustro.get(com) != null)?mapStatisticsLustro.get(com).getCount():0)));
		return mapComStats;
		
	}
	
	public void replaceCliente(Cliente cliente) {
		
		clienteDAO.update(cliente);
		
	}
	
	public void deleteCliente(int id) {
		
		clienteDAO.delete(id);
		
	}
	
	

}