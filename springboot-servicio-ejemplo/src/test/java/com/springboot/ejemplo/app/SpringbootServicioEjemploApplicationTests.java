package com.springboot.ejemplo.app;


import java.time.LocalDate;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.springboot.ejemplo.app.models.entity.Cliente;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
class SpringbootServicioEjemploApplicationTests {

	private static final String DEFAULT_NOMBRE = "Juan";
	private static final String DEFAULT_PRIMER_APELLIDO = "Perez";
	private static final String DEFAULT_SEGUNDO_APELLIDO = "Perez";
	private static final LocalDate DEFAULT_FECHA = LocalDate.of(1988, 10, 23);
	private static final LocalDate DEFAULT_FECHA_OTRA = LocalDate.of(2010, 10, 23);
	private static final String DEFAULT_DOMICILIO = "av.siempre viva #111";
	private static final int NB_CLIENTES = 2;
	private static final String UPDATED_NOMBRE = "Juan Carlos";
	private static final String UPDATED_PRIMER_APELLIDO = "Lopez";
	
	private static String clienteId;
	
	@Test
	 public void shouldNotAddInvalidItem() {
	    Cliente cliente = new Cliente();
	    cliente.setNombre(DEFAULT_NOMBRE);
	    cliente.setPrimerApellido(DEFAULT_PRIMER_APELLIDO); 
	    cliente.setSegundoApellido(DEFAULT_SEGUNDO_APELLIDO); 
	    cliente.setFechaNacimiento(DEFAULT_FECHA_OTRA); 
	    cliente.setDomicilio(DEFAULT_DOMICILIO);  	
	    
    	given()
		.body(cliente)
		.contentType("application/json")
		.when()
		.post("/crear")
		.then()
		.statusCode(400);
	   }
	
    @Test
    @Order(1)
    void shouldGetInitialItems() {
        List<Cliente> clientes = get("/listar").then()
            .statusCode(200)
            .contentType("application/json")
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES, clientes.size());
    }
    
    @Test
    @Order(2)
    void shouldAddAnItem() {
    	Cliente cliente = new Cliente();
    	cliente.setNombre(DEFAULT_NOMBRE);
	    cliente.setPrimerApellido(DEFAULT_PRIMER_APELLIDO); 
	    cliente.setSegundoApellido(DEFAULT_SEGUNDO_APELLIDO); 
	    cliente.setFechaNacimiento(DEFAULT_FECHA); 
	    cliente.setDomicilio(DEFAULT_DOMICILIO);

        String location = given()
            .body(cliente)
            .contentType("application/json")
            .when()
            .post("/crear")
            .then()
            .statusCode(201)
            .extract().header("Location");

        // Stores the id
        String[] segments = location.split("/");
        clienteId = segments[segments.length - 1];
        assertNotNull(clienteId);

        given()
            .pathParam("id", clienteId)
            .when().get("/ver/{id}")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("nombre", Is.is(DEFAULT_NOMBRE))
            .body("primerApellido", Is.is(DEFAULT_PRIMER_APELLIDO))
            .body("segundoApellido", Is.is(DEFAULT_SEGUNDO_APELLIDO))
//            .body("fechaNacimiento", Is.is(DEFAULT_FECHA))
            .body("domicilio", Is.is(DEFAULT_DOMICILIO));

        List<Cliente> clientes = get("/listar").then()
            .statusCode(200)
            .contentType("application/json")
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES + 1, clientes.size());
    }
    
    @Test
    @Order(3)
    void testUpdatingAnItem() {
    	Cliente cliente = new Cliente();
    	cliente.setNombre(UPDATED_NOMBRE);
	    cliente.setPrimerApellido(UPDATED_PRIMER_APELLIDO); 
	    cliente.setSegundoApellido(DEFAULT_SEGUNDO_APELLIDO); 
	    cliente.setFechaNacimiento(DEFAULT_FECHA); 
	    cliente.setDomicilio(DEFAULT_DOMICILIO);

        given()
            .body(cliente)
            .pathParam("id", Long.valueOf(clienteId))
            .contentType("application/json")
            .when()
            .put("/editar/{id}")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("nombre", Is.is(UPDATED_NOMBRE))
            .body("primerApellido", Is.is(UPDATED_PRIMER_APELLIDO))
            .body("segundoApellido", Is.is(DEFAULT_SEGUNDO_APELLIDO))
            .body("domicilio", Is.is(DEFAULT_DOMICILIO));

        List<Cliente> clientes = get("/listar").then()
            .statusCode(200)
            .contentType("application/json")
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES + 1, clientes.size());
    }
    
    @Test
    @Order(4)
    void shouldRemoveAnItem() {
        given()
            .pathParam("id", Long.valueOf(clienteId))
            .when().delete("/eliminar/{id}")
            .then()
            .statusCode(204);

        List<Cliente> villains = get("/listar").then()
            .statusCode(200)
            .contentType("application/json")
            .extract().body().jsonPath().getList(".", Cliente.class);
        assertEquals(NB_CLIENTES, villains.size());
    }
    
}
