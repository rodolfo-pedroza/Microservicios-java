package org.acme.models.entity;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "clientes")
public class Cliente extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único del cliente.", example = "1", required = true)
	public Long id;

	@Schema(description = "Nombre del cliente.", example = "Miguel Angel", required = true)
	public String nombre;

	@Schema(description = "Primer apellido del cliente.", example = "Arevalo", required = true)
	@Column(name = "primer_apellido")
	public String primerApellido;
	
	@Schema(description = "Segundo apellido del cliente.", example = "Andrade", required = true)
	@Column(name = "segundo_apellido")
	public String segundoApellido;

	@Schema(description = "Fecha de nacimiento del cliente.", example = "1998-07-09", required = true)
	@Column(name = "fecha_nacimiento")
	public LocalDate fechaNacimiento;

	@Schema(description = "domicilio del cliente.", example = "av.siempre viva #111", required = true)
	public String domicilio;

	@Schema(description = "Edad del cliente.", example = "24")
	public Integer edad;

	public int calculateAge(LocalDate birthDate) {
		LocalDate today = LocalDate.now();
		return Period.between(birthDate, today).getYears();
	}

	@Override
	public String toString() {
		return "Cliente [nombre=" + nombre + ", primerApellido=" + primerApellido + ", segundoApellido="
				+ segundoApellido + ", fechaNacimiento=" + fechaNacimiento + ", domicilio=" + domicilio + ", edad="
				+ edad + "]";
	}

}
