DROP SCHEMA IF EXISTS concursoVEX;

create schema concursoVEX;

use concursoVEX;

create table sedes(
	id varchar(15) primary key,
    nombre varchar(50) unique not null,
    direccion varchar(80) unique not null
);

create table eventos(
	id varchar(15) primary key,
    nombre varchar(80) unique not null,
    hora_i time,
    hora_f time,
    fecha date unique,
    id_sede varchar(15) not null,
    foreign key(id_sede) references sedes(id) on update cascade
);

create table juez(
	id varchar(20) primary key,
    nombre varchar(50) not null,
    institucion varchar(50) not null,
    nivel_academico varchar(20) not null
);

create table jurados(
	nombre varchar(30) not null,
    id_evento varchar(15) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    primary key(id_evento, nombre),
    foreign key(id_evento) references eventos(id) on update cascade
);

create table juez_jurados(
	id_juez varchar(20) not null,
    id_evento varchar(15) not null,
    nombre_jurado varchar(30) not null,
    primary key (id_juez, id_evento, nombre_jurado),
    foreign key (id_juez) references juez(id),
    foreign key (id_evento, nombre_jurado) references jurados(id_evento, nombre) on update cascade
);

create table instituciones(
	nombre varchar(100) not null,
    id_evento varchar(15) not null,
    direccion varchar(100) not null,
    nivel_academico varchar(15) not null,
    primary key(nombre, id_evento),
    foreign key(id_evento) references eventos(id) on update cascade
);

create table asesores(
	nombre varchar(50) unique not null,
    nombre_institucion varchar(100) not null,
    id_evento varchar(15) not null,
    correo varchar(100) not null,
    primary key(nombre, nombre_institucion, id_evento),
    foreign key(nombre_institucion, id_evento)
    references instituciones(nombre, id_evento) on update cascade
);

create table equipos(
	nombre varchar(30) not null,
    nombre_institucion varchar(100) not null,
    id_evento varchar(15) not null,
    nombre_asesor varchar(50) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    primary key(nombre, nombre_institucion, id_evento),
    foreign key(nombre_institucion, id_evento) references instituciones(nombre, id_evento) ,
    foreign key(nombre_asesor) references asesores(nombre) on update cascade
);

create table integrantes(
	nombre varchar(50) not null,
    nombre_equipo varchar(30) not null,
    nombre_institucion varchar(100) not null,
    id_evento varchar(15) not null,
    edad int not null,
    primary key(nombre, nombre_equipo, nombre_institucion, id_evento),
    foreign key(nombre_equipo, nombre_institucion, id_evento)
    references equipos(nombre, nombre_institucion, id_evento) on delete cascade /*----*/
);

create table diseno (
    id varchar(3) primary key,
    reg_fechas boolean not null,
    just_de_cam_proto boolean not null,
    diag_img boolean not null,
    ortog_redacc boolean not null,
    presentacion boolean not null,
    dis_mod_autodesk boolean not null,
    ana_elem boolean not null,
    ensamble_proto boolean not null,
    modelo_acorde_robot boolean not null,
    acorde_robot boolean not null,
    acorde_simulacion boolean not null,
    restricc_mov boolean not null,
    vid_anim boolean not null
);

create table programacion (
    id varchar(3) primary key,
    soft_robotc boolean not null,
    uso_funciones boolean not null,
    complejidad_prog boolean not null,
    just_sec_prog boolean not null,
    estrc_func_aplicadas boolean not null,
    depuracion_cod boolean not null,
    creacion_cod_mod boolean not null,
    documentacion boolean not null,
    vinculacion boolean not null,
    declaracion_uso_dep boolean not null,
    vinculo_joystick boolean not null,
    eficiencia_calib boolean not null,
    habilidad_joystick boolean not null,
    respuesta_mando boolean not null,
    docu_mod_driver boolean not null,
    demo_15_seg boolean not null,
    no_inconvenientes boolean not null,
    demo_objetivo boolean not null,
    exp_mod_auto boolean not null
);

create table construccion (
    id varchar(3) primary key,
    prototipo_estetico boolean not null,
    estructuras_estables boolean not null,
    sistemas boolean not null,
    sensors boolean not null,
    cableado boolean not null,
    calculo_implementacion boolean not null,
    alcance_robot boolean not null,
    imp_estc_vex_rob boolean not null,
    mono_core boolean not null,
    analisis_estructuras boolean not null,
    relacion_vel_ang boolean not null,
    tren_engranes boolean not null,
    centro_gravedad boolean not null,
    sist_transmision boolean not null,
    potencia boolean not null,
    torque boolean not null,
    velocidad boolean not null
);


create table evaluacion(
	nombre_equipo varchar(30) not null,
    nombre_institucion varchar(100) not null,
    id_evento varchar(15) not null,
    nombre_jurado varchar(30) not null,
    id_diseno varchar(3) not null,
    id_programacion varchar(3) not null,
    id_construccion varchar(3) not null,
    calificacion float,
    primary key(nombre_equipo, nombre_institucion, id_evento, nombre_jurado, id_diseno, id_programacion, id_construccion),
    foreign key(nombre_equipo, nombre_institucion, id_evento) references equipos(nombre, nombre_institucion, id_evento),
    foreign key(id_evento, nombre_jurado) references jurados(id_evento, nombre),
    foreign key(id_diseno) references diseno(id),
    foreign key(id_programacion) references programacion(id),
    foreign key(id_construccion) references construccion(id)
);

-- Nivel Base de datos
create user 'admin'@'localhost' identified by 'admin123';
grant select, update, insert on concursoVEX.* to 'admin'@'localhost';
-- El admin no tiene permitido eliminar campos ni tablas de la base de datos

-- Nivel de tabla
	-- Juez
create user 'juez'@'localhost' identified by 'juez123';
grant select on concursoVEX.sedes to 'juez'@'localhost';
grant select on concursoVEX.eventos to 'juez'@'localhost';
grant select on concursoVEX.juez to 'juez'@'localhost';
grant select on concursoVEX.jurado to 'juez'@'localhost';
grant select on concursoVEX.evaluacion to 'juez'@'localhost';
grant insert on concursoVEX.diseno to 'juez'@'localhost';
grant insert on concursoVEX.programacion to 'juez'@'localhost';
grant insert on concursoVEX.construccion to 'juez'@'localhost';

	-- Instituciones
create user 'itcm'@'localhost' identified by 'itcm123';
grant select on concursoVEX.sedes to 'itcm'@'localhost';
grant select on concursoVEX.eventos to 'itcm'@'localhost';
grant select, insert, update on concursoVEX.asesores to 'itcm'@'localhost';
grant select, insert, update on concursoVEX.instituciones to 'itcm'@'localhost';
grant select, insert, update on concursoVEX.equipos to 'itcm'@'localhost';
grant select, insert, update on concursoVEX.integrantes to 'itcm'@'localhost';
grant select on concursoVEX.evaluacion to 'itcm'@'localhost';