create table clientes (
	id bigint auto_increment,
	nombre varchar(255),
	primer_apellido varchar(255),
	segundo_apellido varchar(255),
	fecha_nacimiento date,
	domicilio varchar(255),
	edad integer,
	primary key (id)
	);


INSERT INTO clientes (nombre, primer_apellido, segundo_apellido, fecha_nacimiento, domicilio, edad) VALUES('Rodolfo', 'Pedroza', 'Garcia', '1999-03-04', 'av.siempre viva #111', 23);
INSERT INTO clientes (nombre, primer_apellido, segundo_apellido, fecha_nacimiento, domicilio, edad) VALUES('Juan', 'Perez', 'Perez', '1999-03-04', 'av.siempre viva #111', 23);
