package com.springboot.ejemplo.app.models.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único del cliente.", example = "1", required = true)
	private Long id;

	@Schema(description = "Nombre del cliente.", example = "Miguel Angel", required = true)
	private String nombre;

	@Column(name = "primer_apellido")
	@Schema(description = "Primer apellido del cliente.", example = "Arevalo", required = true)
	private String primerApellido;
	@Column(name = "segundo_apellido")
	@Schema(description = "Segundo apellido del cliente.", example = "Andrade", required = true)
	private String segundoApellido;

	@Column(name = "fecha_nacimiento")
	@Schema(description = "Fecha de nacimiento del cliente.", example = "1998-07-09", required = true)
	private LocalDate fechaNacimiento;

	@Schema(description = "Edad del cliente.", example = "24")
	private Integer edad;

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@Schema(description = "domicilio del cliente.", example = "av.siempre viva #111", required = true)
	private String domicilio;

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public int calculateAge(LocalDate birthDate) {
		LocalDate today = LocalDate.now();
		return Period.between(birthDate, today).getYears();
	}

	private static final long serialVersionUID = -4237809601922030134L;
}
