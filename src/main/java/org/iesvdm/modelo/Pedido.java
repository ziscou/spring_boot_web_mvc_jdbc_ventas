package org.iesvdm.modelo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor
public class Pedido {
	private int id;
	private double total;
	private Date fecha;
	private int idCliente;
	private int idComercial;
	

}
