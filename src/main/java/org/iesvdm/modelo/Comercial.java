package org.iesvdm.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor

public class Comercial {
	
	private int id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	
	//@DecimalMax(value=, inclusive=true)
	//@DecimalMin(value=, inclusive=true)
	private Float comision;
	
	public Comercial() {
		
	}
	
	
	

}
