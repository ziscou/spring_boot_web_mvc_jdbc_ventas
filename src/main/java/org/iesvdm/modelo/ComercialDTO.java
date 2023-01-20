package org.iesvdm.modelo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ComercialDTO extends Comercial{
	
	
	public ComercialDTO(Double total, BigDecimal media, Double max, Double min, Comercial comercial) {
		super(comercial.getId(), comercial.getNombre(), comercial.getApellido1(), comercial.getApellido2(), comercial.getComision());
		this.total = total;
		this.media = media;
		this.max = max;
		this.min = min;
	}
	private  Double total;
	private BigDecimal media;
	private Double max;
	private Double min;

		
}
