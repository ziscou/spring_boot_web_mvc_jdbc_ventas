package org.iesvdm.mapper;

import java.util.Map;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.ClienteDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.service.ClienteService.VectorStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {


		@Mapping(target = "mapComStats", source = "mapComStatIn")
		public ClienteDTO  clienteAClienteDTO(Cliente cliente, Map<Comercial, VectorStats> mapComStatIn);
		
		
		public Cliente clienteDTOACliente(ClienteDTO cliente);
		

}
