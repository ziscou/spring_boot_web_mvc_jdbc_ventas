package org.iesvdm.controlador;

import java.util.List;
import java.util.Map;

import org.iesvdm.mapper.ClienteMapper;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.ClienteDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ClienteService.VectorStats;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.Valid;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ClienteController {
	
	private ClienteService clienteService;
	private ClienteMapper clienteMapper;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ClienteController(ClienteService clienteService,ClienteMapper clienteMapper) {
		this.clienteService = clienteService;
		this.clienteMapper = clienteMapper;
	}
	
	//@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping("/clientes") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {
		
		List<Cliente> listaClientes =  clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);
				
		return "clientes";
		
	}
	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {
		
		Map<Comercial, VectorStats> mapComStats = clienteService.estadisticasPedidosClientePorComercial(id);
		Cliente cliente = clienteService.one(id);
		
		ClienteDTO clienteDTO = clienteMapper.clienteAClienteDTO(cliente, mapComStats);
		
		model.addAttribute("cliente", clienteDTO);
		
		
	
		
		return "detalle-cliente";
		
	}
	
	@GetMapping("/clientes/crear")
	public String crear(@ModelAttribute Cliente cliente,Model model) {
		//public String crear(Model model) { Antes del 3.8
		
		//Cliente cliente = new Cliente();
		//model.addAttribute("cliente", cliente);
		
		return "crear-cliente";
		
	}
	
	@PostMapping("/clientes/crear")
	public String submitCrear(@Valid @ModelAttribute Cliente cliente, BindingResult bindingResulted, Model model) {
	//public RedirectView submitCrear(@ModelAttribute("cliente") Cliente cliente) {
		
		if (bindingResulted.hasErrors()) {
			model.addAttribute("cliente", cliente);
			
			return "crear-cliente";
		}
		
		clienteService.newCliente(cliente);
				
		//return new RedirectView("/clientes") ;
		return "redirect:/clientes" ;
		
	}
	
	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		
		Cliente cliente = clienteService.one(id);
		model.addAttribute("cliente", cliente);
		
		return "editar-cliente";
		
	}
	
	
	@PostMapping("/clientes/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("cliente") Cliente cliente) {
		
		clienteService.replaceCliente(cliente);		
		
		return new RedirectView("/clientes");
	}
	
	@PostMapping("/clientes/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		
		clienteService.deleteCliente(id);
		
		return new RedirectView("/clientes");
	}
	
	
	
	
	

}
