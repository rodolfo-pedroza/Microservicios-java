package com.springboot.ejemplo.app.service;

import java.util.List;

import com.springboot.ejemplo.app.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();

	public Cliente findById(Long id);

	public Cliente save(Cliente cliente);

	public void deleteById(Long id);
}
