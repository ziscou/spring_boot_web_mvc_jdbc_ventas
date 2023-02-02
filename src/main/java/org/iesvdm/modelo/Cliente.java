package org.iesvdm.modelo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//La anotación @Data de lombok proporcionará el código de: 
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")


public class Cliente {
	
	private int id;
	@NotBlank
	@Size(min=4, message = "Nombre al menos de {min} caracteres. Ha introducido '${validatedValue}'.")
	@Size(max=30, message = "El nombre no puede tener mas de {max} caracteres")
	private String nombre;
	
	@NotBlank
	@Size(max=30, message = "El apellido1 no puede tener mas de 30 caracteres")
	private String apellido1;
	
	private String apellido2;
	
	@NotBlank
	@Size(max=50, message = "La ciudad no puede tener mas de 50 caracteres")
	private String ciudad;
	
	@NotNull(message = "Por favor, introduzca categoria.")
	@Min(value=100, message = "Categoria debe ser al menos de {value}.")
	@Max(value=1000, message = "Categoria no debe ser mayor de {value}.")
	private int categoria;
	
	//@Email(message = "Formato de email incorrecto.Ha introducido  '${validatedValue}'.", regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
	//@NotBlank(message = "Por favor, introduzca email.")
	
	//@DecimalMax(value=, inclusive=true)
	//@DecimalMin(value=, inclusive=true)
	
	
	
}

