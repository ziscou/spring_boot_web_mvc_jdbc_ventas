package org.iesvdm.modelo;

import java.sql.Date;

import lombok.Data;

@Data

public class PedidoDTO extends Pedido {
	private String nombre;
	private String apellido1;
	private String apellido2;
	
	public PedidoDTO(int id, double total, Date fecha, int idCliente, int idComercial, String nombre, String apellido1,
			String apellido2) {
		super(id, total, fecha, idCliente, idComercial);
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
	}


}
