-- Tablas Concurso VEX
create schema concursoVEX;

use concursoVEX;

create table sedes(
    nombre varchar(50) primary key,
    direccion varchar(100) unique not null
);

create table eventos(
    nombre varchar(80) primary key,
    hora_i time not null,
    hora_f time not null,
    fecha date unique not null,
    nombre_sede varchar(50) not null,
    primaria boolean not null,
    secundaria boolean not null,
    bachillerato boolean not null,
    universidad boolean not null,
    estado enum ('Vigente', 'Cancelado', 'En espera', 'Finalizado'),
    foreign key(nombre_sede) references sedes(nombre) on update cascade
);

select * from eventos;

update eventos set hora_i = '23:00:00', hora_f = '23:59:00', fecha = curdate() + interval 8 day where nombre = 'Concurso VEX 2023-12';

create table jurados(
	nombre varchar(30) not null,
    nombre_evento varchar(80) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    primary key(nombre_evento, categoria),
    foreign key(nombre_evento) references eventos(nombre) on update cascade
);

create table jueces(
	id int primary key auto_increment,
    nombre varchar(32) not null,
    institucion varchar(50) not null,
    nivel_academico varchar(20) not null
);

create table jueces_jurados(
	id_juez int not null,
    nombre_evento varchar(80) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    primary key (id_juez, nombre_evento, categoria),
    foreign key (id_juez) references jueces(id),
    foreign key (nombre_evento, categoria) references jurados(nombre_evento, categoria) on update cascade
);

select * from instituciones;
create table instituciones(
	nombre_evento varchar(80) not null,
	nombre varchar(32) not null,
    direccion varchar(100) not null,
    primary key(nombre_evento, nombre),
    foreign key(nombre_evento) references eventos(nombre) on update cascade
);

create table asesores(
	nombre_evento varchar(80) not null,
	nombre_institucion varchar(32) not null,
	nombre varchar(50) not null,
    correo varchar(100) not null,
    primary key(nombre_evento, nombre_institucion, nombre),
    foreign key(nombre_evento, nombre_institucion) references instituciones(nombre_evento, nombre) on update cascade
);

create table equipos(
	nombre_evento varchar(80) not null,
	nombre_institucion varchar(32) not null,
	nombre varchar(30) not null,
    nombre_asesor varchar(50) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    primary key(nombre_evento, nombre_institucion, nombre, categoria),
    foreign key(nombre_evento, nombre_institucion, nombre_asesor) references asesores(nombre_evento, nombre_institucion, nombre)
);

create table integrantes(
    nombre_evento varchar(80) not null,
    nombre_institucion varchar(32) not null,
    nombre_equipo varchar(30) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    nombre varchar(50) not null,
    edad int not null,
    primary key(nombre_evento, nombre_institucion, nombre_equipo, categoria, nombre),
    foreign key(nombre_evento, nombre_institucion, nombre_equipo, categoria)
    references equipos(nombre_evento, nombre_institucion, nombre, categoria) on update cascade
);

create table diseno (
    id int auto_increment primary key,
    reg_fechas boolean not null,
    just_de_cam_proto boolean not null,
    diag_img boolean not null,
    ortog_redacc boolean not null,
    presentacion boolean not null,
    vid_anim boolean not null,
    dis_mod_autodesk boolean not null,
    ana_elem boolean not null,
    ensamble_proto boolean not null,
    modelo_acorde_robot boolean not null,
    acorde_simulacion boolean not null,
    restricc_mov boolean not null
);

create table programacion (
    id int auto_increment primary key,
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
    id int auto_increment primary key,
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
	nombre_evento varchar(80) not null,
    nombre_institucion varchar(32) not null,
	nombre_equipo varchar(30) not null,
    categoria enum ('primaria', 'secundaria', 'bachillerato', 'universidad') not null,
    id_juez int not null,
    id_diseno int unique,
    id_programacion int unique,
    id_construccion int unique,
    calificacion float,
    primary key(nombre_evento, nombre_institucion, nombre_equipo, categoria, id_juez),
    foreign key(nombre_evento, nombre_institucion, nombre_equipo, categoria) references equipos(nombre_evento, nombre_institucion, nombre, categoria),
    foreign key(id_juez, nombre_evento, categoria) references jueces_jurados(id_juez, nombre_evento, categoria),
    foreign key(id_diseno) references diseno(id),
    foreign key(id_programacion) references programacion(id),
    foreign key(id_construccion) references construccion(id)
);

delimiter //
create trigger before_insert_evaluacion
before insert on evaluacion
for each row
begin
    if new.calificacion < 0 then
        set new.calificacion = 0;
    end if;
end;
//
delimiter ;

