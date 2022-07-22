package com.springboot.ejemplo.app.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import com.springboot.ejemplo.app.models.entity.Cliente;
import com.springboot.ejemplo.app.service.IClienteService;
import com.zaxxer.hikari.util.ClockSource.Factory;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@Operation(summary = "Regresa una lista con los clientes", description = "Regresa una lista con todos los clientes de la base de datos")
	@ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))})
	@GetMapping("/listar")
	public List<Cliente> listar() {
		return clienteService.findAll();
	}
	
	@Operation(summary = "Regresa el cliente por id", description = "Busca al cliente en la base de datos por su id")
	@GetMapping("/ver/{id}")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
	@ApiResponse(responseCode = "404", description = "No hay cliente con el identificador proporcionado")
	public ResponseEntity<Cliente> detalle(@PathVariable Long id) {
		Cliente cliente = clienteService.findById(id);
		if (cliente != null) {
			return new ResponseEntity<>(cliente, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Crea un cliente", description = "crea un cliente si tiene la edad adecuada")
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponse(responseCode = "201", description = "URI del cliente creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
	@ApiResponse(responseCode = "404", description = "No hay cliente con el identificador proporcionado")
	public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
		
		if ( cliente.calculateAge(cliente.getFechaNacimiento()) > 18 &&  cliente.calculateAge(cliente.getFechaNacimiento()) < 64) {
			cliente.setEdad(cliente.calculateAge(cliente.getFechaNacimiento()));
			clienteService.save(cliente);
			URI location = URI.create(String.format("localhost:8080/crear/%s", Long.toString(cliente.getId())));
			return ResponseEntity.created(location).body(cliente);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Modifica un cliente existente")
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponse(responseCode = "200", description = "Cliente modificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
	@ApiResponse(responseCode = "404", description = "No hay cliente con el identificador proporcionado")
	public ResponseEntity<Cliente> editar(@RequestBody Cliente cliente, @PathVariable Long id) {

		Cliente clienteDb = clienteService.findById(id);
		
		if (clienteDb != null) {
			clienteDb.setNombre(cliente.getNombre());
			clienteDb.setPrimerApellido(cliente.getPrimerApellido());
			clienteDb.setSegundoApellido(cliente.getSegundoApellido());
			clienteDb.setFechaNacimiento(cliente.getFechaNacimiento());
			clienteDb.setDomicilio(cliente.getDomicilio());
			
			return new ResponseEntity<>(clienteService.save(clienteDb), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

	@Operation(summary = "Elimina un cliente existente")
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiResponse(responseCode = "204", description = "Cliente eliminado")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		clienteService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
