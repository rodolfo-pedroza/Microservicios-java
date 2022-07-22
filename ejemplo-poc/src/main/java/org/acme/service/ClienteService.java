package org.acme.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.acme.models.entity.Cliente;



@ApplicationScoped
@Transactional
public class ClienteService {
	
	
	public List<Cliente> listar() {
		return Cliente.listAll();
	}
	
	public Cliente findClienteById(Long id) {
        return Cliente.findById(id);
    }
	
    public Cliente persistCliente(@Valid Cliente cliente) {
    	cliente.edad = cliente.calculateAge(cliente.fechaNacimiento);
    	cliente.persist();
    	return cliente;
    }
    
    public Cliente updateCliente(@Valid Cliente cliente) {
    	
    	Cliente clienteDb = Cliente.findById(cliente.id);
    	
    	clienteDb.nombre = cliente.nombre;
    	clienteDb.fechaNacimiento = cliente.fechaNacimiento;
    	clienteDb.primerApellido = cliente.primerApellido;
    	clienteDb.segundoApellido = cliente.segundoApellido;
    	clienteDb.domicilio = cliente.domicilio;
    	clienteDb.edad = cliente.calculateAge(clienteDb.fechaNacimiento);
    	
		return clienteDb;
    }
	
    public void deleteCliente(Long id) {
    	Cliente cliente = Cliente.findById(id);
    	cliente.delete();
    }
}
