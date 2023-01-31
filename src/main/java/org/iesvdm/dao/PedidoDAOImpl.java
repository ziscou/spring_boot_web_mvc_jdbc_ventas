package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
//Un Repository es un componente y a su vez un estereotipo de Spring 
//que forma parte de la ‘capa de persistencia’.
@Repository
public class PedidoDAOImpl implements PedidoDAO {

	 //Plantilla jdbc inyectada automáticamente por el framework Spring, gracias a la anotación @Autowired.
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	
	/**
	 * Inserta en base de datos el nuevo Pedido, actualizando el id en el bean Pedido.
	 */
	@Override	
	public synchronized void create(Pedido pedido) {
		
							//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO pedido (total, fecha, id_cliente, id_comercial) 
							VALUES  (     ?,         ?,         ?,       ?)
						   """;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Sin recuperación de id generado
//		int rows = jdbcTemplate.update(sqlInsert,
//							pedido.getTotal(),
//							pedido.getFecha(),
//							pedido.getId_cliente(),
//							pedido.getCiudad(),
//							pedido.getCategoria()
//					);
		
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setDouble(idx++, pedido.getTotal());
			ps.setDate(idx++, pedido.getFecha());
			ps.setInt(idx, pedido.getIdCliente());
			ps.setInt(idx, pedido.getIdComercial());
			return ps;
		},keyHolder);
		
		pedido.setId(keyHolder.getKey().intValue());

		log.info("Insertados {} registros.", rows);
	}

	/**
	 * Devuelve lista con todos los Pedidos.
	 */
	@Override
	public List<Pedido> getAll() {
		
		List<Pedido> listCom = jdbcTemplate.query(
                "SELECT * FROM pedido",
                (rs, rowNum) -> new Pedido(rs.getInt("id"),
                						 	rs.getDouble("total"),
                						 	rs.getDate("fecha"),
                						 	rs.getInt("id_cliente"),
                						 	rs.getInt("id_comercial")
                						 	)
        );
		
		log.info("Devueltos {} registros.", listCom.size());
		
        return listCom;
        
	}
	/**
	 * Devuelve lista con todos loa Pedidos de un Comercial.
	 */
	@Override
	public List<PedidoDTO> getAllByComercialId(int id_comer) {
		
		List<PedidoDTO> listCom = jdbcTemplate.query(
                "Select p.*, ci.nombre, ci.apellido1, ci.apellido2 from pedido p left outer join comercial c on c.id = p.id_comercial left outer join cliente ci on ci.id = p.id_cliente where c.id = ?",
                (rs, rowNum) -> new PedidoDTO(rs.getInt("id"),
                							rs.getDouble("total"),
 											rs.getDate("fecha"),
 											rs.getInt("id_cliente"),
 											rs.getInt("id_comercial"),
 											rs.getString("nombre"),
 											rs.getString("apellido1"),
 											rs.getString("apellido2"))
                						,id_comer
        );
		
		log.info("Devueltos {} registros.", listCom.size());
		
        return listCom;
        
	}

	/**
	 * Devuelve lista con todos loa Pedidos de un Comercial.
	 */
	@Override
	public List<Pedido> getAllByClienteId(int id_client) {
		
		List<Pedido> listCom = jdbcTemplate.query(
                "Select p.* from pedido p left outer join cliente c on c.id = p.id_cliente where c.id = ?",
                (rs, rowNum) -> new Pedido(rs.getInt("id"),
                							rs.getDouble("total"),
 											rs.getDate("fecha"),
 											rs.getInt("id_cliente"),
 											rs.getInt("id_comercial"))
                						,id_client
        );
		
		log.info("Devueltos {} registros.", listCom.size());
		
        return listCom;
        
	}

	/**
	 * Devuelve Optional de Pedido con el ID dado.
	 */
	@Override
	public Optional<Pedido> find(int id) {
		
		Pedido com =  jdbcTemplate
				.queryForObject("SELECT * FROM pedido WHERE id = ?"														
								, (rs, rowNum) -> new Pedido(rs.getInt("id"),
            						 						rs.getDouble("total"),
            						 						rs.getDate("fecha"),
            						 						rs.getInt("id_cliente"),
            						 						rs.getInt("id_comercial"))
								, id
								);
		
		if (com != null) { 
			return Optional.of(com);}
		else { 
			log.info("Pedido no encontrado.");
			return Optional.empty(); }
        
	}
	/**
	 * Actualiza Pedido con campos del bean Pedido según ID del mismo.
	 */
	@Override
	public void update(Pedido pedido) {
		
		int rows = jdbcTemplate.update("""
										UPDATE pedido SET 
														total = ?, 
														fecha = ?, 
														id_cliente = ?,
														id_comercial = ?
												WHERE id = ?
										""", pedido.getTotal()
										, pedido.getFecha()
										, pedido.getIdCliente()
										, pedido.getIdComercial()
										, pedido.getId());
		
		log.info("Update de Pedido con {} registros actualizados.", rows);
    
	}

	/**
	 * Borra Pedido con ID proporcionado.
	 */
	@Override
	public void delete(int id) {
		
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
		
		log.info("Delete de Pedido con {} registros eliminados.", rows);		
		
	}

	@Override
	public Double totalPedidosComercial(int id) {
		
		Double total = jdbcTemplate.queryForObject("Select sum(p.total) as total from pedido p left outer join comercial c on c.id = p.id_comercial where c.id = ?",
													(rs, rowNum) -> rs.getDouble("total"), id);
		
		
		return total;
	}
	@Override
	public Double mediaPedidosComercial(int id) {
		
		Double media = jdbcTemplate.queryForObject("Select AVG(p.total) as media from pedido p left outer join comercial c on c.id = p.id_comercial where c.id = ?",
												(rs, rowNum) -> rs.getDouble("media"), id);
		
		
		return media;
	}
	@Override
	public Double maximoPedidosComercial(int id) {
		
		Double max = jdbcTemplate.queryForObject("Select MAX(p.total) as max from pedido p left outer join comercial c on c.id = p.id_comercial where c.id = ?",
												(rs, rowNum) -> rs.getDouble("max"), id);
		
		
		return max;
	}
	@Override
	public Double minimoPedidosComercial(int id) {
		
		Double min = jdbcTemplate.queryForObject("Select MIN(p.total) as min from pedido p left outer join comercial c on c.id = p.id_comercial where c.id = ?",
												(rs, rowNum) -> rs.getDouble("min"), id);
		
		
		return min;
	}
	
	
	
}
