package com.springboot.ejemplo.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ejemplo.app.models.dao.ClienteDao;
import com.springboot.ejemplo.app.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private ClienteDao clienteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		clienteDao.deleteById(id);
	}

	
	
}
