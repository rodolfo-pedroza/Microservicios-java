package org.acme.controllers;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.acme.models.entity.Cliente;
import org.acme.service.ClienteService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import static javax.ws.rs.core.MediaType.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.net.URI;

@Path("/clientes")
@Tag(name = "Clientes")
public class ClienteController {

	@Inject
	ClienteService service;

	@Operation(summary = "Regresa una lista con los clientes", description = "Regresa una lista con todos los clientes de la base de datos")
	@APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Cliente.class, required = true)))
	@GET
	public Response getAllClientes() {
		List<Cliente> clientes = service.listar();
		return Response.ok(clientes).build();
	}

	@Operation(summary = "Regresa el cliente por id", description = "Busca al cliente en la base de datos por su id")
	@GET
	@Path("/{id}")
	@APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Cliente.class)))
	@APIResponse(responseCode = "204", description = "No hay cliente con el identificador proporcionado")
	public Response getCliente(@PathParam("id") Long id) {
		Cliente cliente = service.findClienteById(id);
		if (cliente != null) {
			return Response.ok(cliente).build();
		} else {
			return Response.noContent().build();
		}
	}

	@Operation(summary = "Crea un cliente", description = "crea un cliente si tiene la edad adecuada")
	@POST
	@APIResponse(responseCode = "201", description = "URI del cliente creado", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
	@APIResponse(responseCode = "400", description = "El cliente no tiene la edad adecuada", content = @Content(mediaType = APPLICATION_JSON))
	public Response createCliente(@Valid Cliente cliente, @Context UriInfo uriInfo) {
		if (cliente.calculateAge(cliente.fechaNacimiento) > 18 && cliente.calculateAge(cliente.fechaNacimiento) < 64) {
			cliente = service.persistCliente(cliente);
			URI location = URI.create(String.format("localhost:8080/clientes/%s", Long.toString(cliente.id)));
			return Response.created(location).build();
		}
		return Response.status(400).build();
	}

	@Operation(summary = "Modifica un cliente existente")
	@PUT
	@APIResponse(responseCode = "200", description = "Cliente modificado", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Cliente.class)))
	public Response updateCliente(@Valid Cliente cliente) {
		cliente = service.updateCliente(cliente);
		return Response.ok(cliente).build();
	}

	@Operation(summary = "Elimina un cliente existente")
	@DELETE
	@Path("/{id}")
	@APIResponse(responseCode = "204", description = "Cliente eliminado")
	public Response deleteCliente(@PathParam("id") Long id) {
		service.deleteCliente(id);
		return Response.noContent().build();
	}

}
