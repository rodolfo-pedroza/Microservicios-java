package com.springboot.ejemplo.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.ejemplo.app.models.entity.Cliente;

public interface ClienteDao extends CrudRepository<Cliente, Long>{

}
