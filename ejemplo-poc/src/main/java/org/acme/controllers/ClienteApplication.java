package org.acme.controllers;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
	    info = @Info(title = "Cliente API",
	        description = "Esta API permite operaciones CRUD en un cliente",
	        version = "1.0",
	        contact = @Contact(name = "Rodo", url = "https://github.com/rodolfo-pedroza")),
	    servers = {
	        @Server(url = "http://localhost:8084")
	    }
	)
public class ClienteApplication extends Application{

}
