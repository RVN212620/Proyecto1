package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

//@SpringBootApplication //desactivamos la maquina de Spring y ya no se correra desde esta clase
public class ZonaFitApplication implements CommandLineRunner {
	@Autowired
	private IClienteServicio clienteServicio;
	private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);

	public static void main(String[] args) {
		logger.info("Iniciando la aplicacion!");
		//Levantar la fabrica de spring
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("Aplicacion Finalizada!");
	}

	@Override
	public void run(String... args) throws Exception {
		zonaFitApp();
	}
	private void zonaFitApp(){
		logger.info("*** Aplicacion Zona Fit (GYM) ***");
		var salir = false;
		var consola = new Scanner(System.in);
		while(!salir){
			var opcion = mostrarMenu(consola);
			salir = ejecutarOpciones(consola, opcion);
			logger.info("");

		}
	}
	private int mostrarMenu(Scanner consola){
		logger.info("""
		*** Aplicacion Zona Fit (GYM) ***
		1. Listar Cliente
		2. Buscar Cliente 
		3. Agregar Cliente
		4. Modificar Cliente
		5. Eliminar CLiente
		6. Salir
		Elige una opcion:\s""");
		return Integer.parseInt(consola.nextLine());
	}
	private boolean ejecutarOpciones(Scanner consola, int opcion){
		var salir = false;
		switch (opcion){
			case 1->{
				logger.info("--- Listado de Clientes ---");
				List<Cliente> clientes = clienteServicio.listarClientes();
				clientes.forEach(cliente -> logger.info(cliente.toString()));
			}
			case 2 ->{
				logger.info("--- Buscar Cliente por Id ---");
				logger.info("ID cliente a buscar: ");
				var idCliente = Integer.parseInt(consola.nextLine());
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
				if (cliente != null)
					logger.info("Cliente encontrado: " + cliente);
				else
					logger.info("Cliente NO Encontrado: " + cliente);
			}
			case 3 ->{
				logger.info("--- Agregar Cliente ---");
				logger.info("Nombre: ");
				var nombre = consola.nextLine();
				logger.info("Apellido: ");
				var apellido = consola.nextLine();
				logger.info("Membresia: ");
				var membresia = Integer.parseInt(consola.nextLine());
				var cliente = new Cliente();
				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setMembresia(membresia);
				clienteServicio.guardarCliente(cliente);
				logger.info("Cliente Agregado: " + cliente);
			}
			case 4 ->{
				logger.info("--- Modificar CLiente ---");
				logger.info("Id Cliente: ");
				var idCliente = Integer.parseInt(consola.nextLine());
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
				if (cliente != null){
					logger.info("Nombre: ");
					var nombre = consola.nextLine();
					logger.info("Apellido: ");
					var apellido = consola.nextLine();
					logger.info("Membresia: ");
					var membresia = Integer.parseInt(consola.nextLine());
					cliente.setNombre(nombre);
					cliente.setApellido(apellido);
					cliente.setMembresia(membresia);
					clienteServicio.guardarCliente(cliente);
					logger.info("Cliente Modificado: " + cliente);
				}
				else{
					logger.info("Cliente NO Encontrado: " + cliente);
				}
			}
			case 5 -> {
				logger.info("--- Eliminar un CLiente ---");
				logger.info("Id Cliente: ");
				var idCliente = Integer.parseInt(consola.nextLine());
				var cliente = clienteServicio.buscarClientePorId(idCliente);
				if(cliente != null){
					clienteServicio.eliminarCliente(cliente);
					logger.info("Cliente ELiminado: " + cliente);
				}
				else {
					logger.info("Cliente NO Encontrado: " + cliente);
				}
			}
			case 6 -> {
				logger.info("Hasta Luego Amigos del Barrio! ");
				logger.info("");
				salir = true;
			}
			default -> logger.info("Opcion no Reconocida: " + opcion);
		}
		return salir;
	}
}
