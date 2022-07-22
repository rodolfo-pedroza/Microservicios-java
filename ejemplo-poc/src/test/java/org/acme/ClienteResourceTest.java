package org.acme;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.acme.models.entity.Cliente;
import org.hamcrest.core.Is;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class ClienteResourceTest {
	
	private static final String JSON = "application/json;charset=UTF-8";
	
	private static final String DEFAULT_NOMBRE = "Juan";
	private static final String DEFAULT_PRIMER_APELLIDO = "Perez";
	private static final String DEFAULT_SEGUNDO_APELLIDO = "Perez";
	private static final LocalDate DEFAULT_FECHA = LocalDate.of(1988,10,23);
	private static final LocalDate DEFAULT_FECHA_OTRA = LocalDate.of(2010,10,23);
	private static final String DEFAULT_DOMICILIO = "av.siempre viva #111";
	private static final int NB_CLIENTES = 2;
	private static final String UPDATED_NOMBRE = "Juan Carlos";
	private static final String UPDATED_PRIMER_APELLIDO = "Lopez";
	
	private static String clienteId;
	
    @Test
    public void shouldNotAddInvalidItem() {
    	Cliente cliente = new Cliente();
    	cliente.nombre = DEFAULT_NOMBRE;
    	cliente.primerApellido = DEFAULT_PRIMER_APELLIDO;
    	cliente.segundoApellido = DEFAULT_SEGUNDO_APELLIDO;
    	cliente.fechaNacimiento = DEFAULT_FECHA_OTRA;
    	cliente.domicilio = DEFAULT_DOMICILIO;
    	
    	
    	given()
    		.body(cliente)
    		.header(CONTENT_TYPE, JSON)
    		.header(ACCEPT, JSON)
    		.when()
    		.post("/clientes")
    		.then()
    		.statusCode(BAD_REQUEST.getStatusCode());
    }
    

    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Cliente> clientes = get("/clientes").then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES, clientes.size());
    }
    
    @Test
    @Order(2)
    void shouldAddAnItem() {
    	Cliente cliente = new Cliente();
    	cliente.nombre = DEFAULT_NOMBRE;
    	cliente.primerApellido = DEFAULT_PRIMER_APELLIDO;
    	cliente.segundoApellido = DEFAULT_SEGUNDO_APELLIDO;
    	cliente.fechaNacimiento = DEFAULT_FECHA;
    	cliente.domicilio = DEFAULT_DOMICILIO;

        String location = given()
            .body(cliente)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .post("/clientes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().header("Location");

        // Stores the id
        String[] segments = location.split("/");
        clienteId = segments[segments.length - 1];
        assertNotNull(clienteId);

        given()
            .pathParam("id", clienteId)
            .when().get("/clientes/{id}")
            .then()
            .statusCode(200)
            .contentType(APPLICATION_JSON)
            .body("nombre", Is.is(DEFAULT_NOMBRE))
            .body("primerApellido", Is.is(DEFAULT_PRIMER_APELLIDO))
            .body("segundoApellido", Is.is(DEFAULT_SEGUNDO_APELLIDO))
//            .body("fechaNacimiento", Is.is(DEFAULT_FECHA))
            .body("domicilio", Is.is(DEFAULT_DOMICILIO));

        List<Cliente> villains = get("/clientes").then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES + 1, villains.size());
    }
    
    @Test
    @Order(3)
    void testUpdatingAnItem() {
    	Cliente cliente = new Cliente();
    	cliente.id = Long.valueOf(clienteId);
    	cliente.nombre = UPDATED_NOMBRE;
    	cliente.primerApellido = UPDATED_PRIMER_APELLIDO;
    	cliente.segundoApellido = DEFAULT_SEGUNDO_APELLIDO;
    	cliente.fechaNacimiento = DEFAULT_FECHA;
    	cliente.domicilio = DEFAULT_DOMICILIO;

        given()
            .body(cliente)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .put("/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("nombre", Is.is(UPDATED_NOMBRE))
            .body("primerApellido", Is.is(UPDATED_PRIMER_APELLIDO))
            .body("segundoApellido", Is.is(DEFAULT_SEGUNDO_APELLIDO))
            .body("domicilio", Is.is(DEFAULT_DOMICILIO));

        List<Cliente> clientes = get("/clientes").then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES + 1, clientes.size());
    }
    
    @Test
    @Order(4)
    void shouldRemoveAnItem() {
        given()
            .pathParam("id", clienteId)
            .when().delete("/clientes/{id}")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        List<Cliente> villains = get("/clientes").then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES, villains.size());
    }

    
}