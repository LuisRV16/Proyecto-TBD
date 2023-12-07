-- Procedures Concurso VEX
use concursoVEX;

delimiter //
create procedure addCampus (name varchar(50), dir varchar(100), OUT msg varchar(100)) -- dir = direccion | msg = mensaje
begin

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;

		if exists (select * from sedes where replace(upper(nombre), ' ', '') like replace(upper(name), ' ', '')) then
			set msg = 'La sede ya se encuentra registrada. Ingrese otra sede.';
		elseif exists (select * from sedes where replace(upper(direccion), ' ','') like replace(upper(dir), ' ','')) then
			set msg = 'La direccion ya se encuentra registrada. Ingrese una direccion diferente.';
		elseif (char_length(name) < 3 or char_length(dir) < 4)then
			set msg = 'Nombre o direccion demasiado corto. Ingrese valores validos';
		else
			insert into sedes values (name, dir);
			set msg = 'La sede ha sido registrada.';
		end if;
	commit;
end//
delimiter ;

	-- Insertar jueces
delimiter //
create procedure addJudge (judge_name varchar(32), institution varchar(32), grade varchar(20), judge_p varchar(32), out msg varchar(100)) -- grade = nivel academico | msg = mensaje
begin
    
	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;
		if (char_length(judge_name) < 3) then
			set msg = 'Nombre muy corto. Ingrese un nombre válido.';
		elseif (char_length(institution) < 3) then
			set msg = 'Nombre de institución muy corto. Ingrese un nombre válido.';
		elseif (char_length(grade) < 3) then
			set msg = 'Dato de grado de estudio muy corto. Ingrese un dato válido.';
		elseif (char_length(judge_p) < 3) then
			set msg = 'Contraseña demasiado corta.Ingrese una contraseña válida.';
		elseif exists (select * from jueces where replace(upper(nombre), ' ', '') like replace(upper(judge_name), ' ', '') and replace(upper(institucion), ' ', '') 
						like replace(upper(institution), ' ', '') and replace(upper(nivel_academico), ' ', '') like replace(upper(grade), ' ', '')) then
			set msg = 'La/El juez ya se encuentra registrada/o. Ingrese datos validos.';
		else
			insert into jueces (nombre, institucion, nivel_academico) values (judge_name, institution, grade);
            call userJudge(judge_name, judge_p);
            call grantJudge(judge_name);
			set msg = 'La/El juez ha sido registrada/o.';
		end if;
	commit;
end//
delimiter ;

delimiter //
create procedure userJudge(j_name varchar(32), j_password varchar(32))
begin
	start transaction;
		SET @createUserCMD = concat('CREATE USER ''', j_name, '''@''', 'localhost', ''' IDENTIFIED BY ''', j_password, ''';');
		PREPARE createUserStatement FROM @createUserCMD;
		EXECUTE createUserStatement;
		DEALLOCATE PREPARE createUserStatement;
	commit;
end //
delimiter ;

delimiter //
create procedure grantJudge(j_name varchar(32))
begin
	SET @grantJueces = concat('grant select on concursoVEX.jueces to ''', j_name, '''@''localhost''');
    set @grantJuecesJurados = concat('grant select on concursoVEX.jueces_jurados to ''', j_name, '''@''localhost''');
    set @grantEventos = concat('grant select(nombre, fecha) on concursoVEX.eventos to ''', j_name, '''@''localhost''');
    set @grantDiseno = concat('grant insert, select on concursoVEX.diseno to ''', j_name, '''@''localhost''');
    set @grantProgramacion = concat('grant insert, select on concursoVEX.programacion to ''', j_name, '''@''localhost''');
    set @grantConstruccion = concat('grant insert, select on concursoVEX.construccion to ''', j_name, '''@''localhost''');
    set @grantEvaluacion = concat('grant select, update on concursoVEX.evaluacion to ''', j_name, '''@''localhost''');
    set @grantGetEventByCurrentDate = concat('grant execute on function concursoVEX.getEventByCurrentDate to ''', j_name, '''@''localhost''');
    set @grantGetParticipatingCategories = concat('grant execute on procedure concursoVEX.getParticipatingCategories to ''', j_name, '''@''localhost''');
    set @grantGetTeamsToEvaluate = concat('grant execute on procedure concursoVEX.getTeamsToEvaluate to ''', j_name, '''@''localhost''');
    set @grantCreateRowDiseno = concat('grant execute on procedure concursoVEX.createRowDiseno to ''', j_name, '''@''localhost''');
    set @grantCreateRowProgramacion = concat('grant execute on procedure concursoVEX.createRowProgramacion to ''', j_name, '''@''localhost''');
    set @grantCreateRowConstruccion = concat('grant execute on procedure concursoVEX.createRowConstruccion to ''', j_name, '''@''localhost''');
    set @grantGetMaxIdWightings = concat('grant execute on function concursoVEX.getMaxIdWightings to ''', j_name, '''@''localhost''');
    set @grantEvaluar = concat('grant execute on procedure concursoVEX.evaluar to ''', j_name, '''@''localhost''');
    set @grantGetCalificacion = concat('grant execute on procedure concursoVEX.getCalificacion to ''', j_name, '''@''localhost''');
    set @grantIsCalificationSet = concat('grant execute on function concursoVEX.isCalificationSet to ''', j_name, '''@''localhost''');
    
    SET @q = @grantJueces; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantJuecesJurados; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantEventos; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantDiseno; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantProgramacion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantConstruccion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantEvaluacion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantGetEventByCurrentDate; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantGetParticipatingCategories; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantGetTeamsToEvaluate; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantCreateRowDiseno; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantCreateRowProgramacion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantCreateRowConstruccion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantGetMaxIdWightings; PREPARE stmt FROM @q; EXECUTE stmt;
	SET @q = @grantEvaluar; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantGetCalificacion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @grantIsCalificationSet; PREPARE stmt FROM @q; EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
end//
delimiter ;

	-- verificar si sedes tiene elementos
delimiter //
create procedure isCampusEmpty(out msg varchar(5))
begin
	declare count int;

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;

	select count(*) into count from sedes;

		if count <> 0 then
			set msg = 'true';
		else
			set msg = 'false';
		end if;

	commit;
end//
delimiter ;

	-- obtener sedes
create procedure getCampus()
	select * from sedes;

    -- dar de alta un nuevo evento
delimiter //
create procedure addEvent(name varchar(80), initialH time, finalH time, dateE date, campus varchar(50), p boolean, s boolean, b boolean, u boolean, status varchar(10), out msg varchar(100))
begin

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;

		if exists(select * from eventos where replace(upper(nombre), ' ', '') = replace(upper(name), ' ', '')) then
			set msg = 'El evento ya se encuentra registrado. Ingrese datos validos.';
		elseif(char_length(name) < 3) then
			set msg = 'Ingrese un nombre valido para el evento.';
		elseif exists(select * from eventos where fecha = dateE) then
			set msg = 'La fecha selecccionada ya se encuentra registrada. Ingrese una fecha valida';
		elseif (dateE < (select curdate() + interval 7 day)) then
			set msg = 'Fecha invalida. Ingrese una fecha posterior a la actual dentro de 7 días.';
		elseif (initialH >= finalH) then
			set msg = 'Horas invalidas. La hora inicial debe ser menor a la hora final del evento.';
		elseif(campus is null) then
			set msg = 'Sin sede seleccionada. Seleccione una sede';
		elseif(p = 0 and s = 0 and b = 0 and u = 0) then
			set msg = 'Debe seleccionar al menos una categoria para el evento.';
		else
			insert into eventos values (name, initialH, finalH, dateE, campus, p, s, b, u, status);
            set msg = 'El evento ha sido registrado.';
		end if;
        
	commit;
end//
delimiter ;

	-- verifica si eventos tiene registros
delimiter //
create procedure isEventsEmpty(out msg varchar(5))
begin
	declare count int;

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;

	select count(*) into count from eventos;

		if count <> 0 then
			set msg = 'true';
		else
			set msg = 'false';
		end if;

	commit;
end//
delimiter ;

	-- Obtener eventos global
create procedure getEvent()
	select * from eventos order by fecha;

create procedure getEventByID(eventName varchar(80))
	select * from eventos as e where e.nombre = eventName;
    
delimiter //
create function getEventName(eventName varchar(80)) returns varchar(80) deterministic
begin
	select nombre from eventos as e where e.nombre = eventName;
	return @idEvent;
end//    
delimiter ;

create procedure getEventNames()
	select nombre from eventos as e where e.estado = 'Vigente' order by nombre;
    
create procedure getEventByName(name varchar(80))
	select * from
	eventos as ev join instituciones as ins on ev.nombre = ins.nombre_evento
	join asesores as ase on ase.nombre_evento = ins.nombre_evento
	and ase.nombre_institucion = ins.nombre
	join equipos as eq on eq.nombre_evento = ase.nombre_evento
	and eq.nombre_institucion = ase.nombre_institucion
	and eq.nombre_asesor = ase.nombre
	join integrantes as intg on intg.nombre_evento = eq.nombre_evento
	and intg.nombre_institucion = eq.nombre_institucion
	and intg.nombre_equipo = eq.nombre
	and intg.categoria = eq.categoria where ev.nombre = name;

-- Obtener eventos institucion
create procedure instituteGetEvent(instituteName varchar(32))
	select * from eventos as e where e.nombre = instituteName;
    
create procedure getCategories(name varchar(80))
	select distinct(eq.categoria) from
	eventos as ev join instituciones as ins on ev.nombre = ins.nombre_evento
	join asesores as ase on ase.nombre_evento = ins.nombre_evento
	and ase.nombre_institucion = ins.nombre
	join equipos as eq on eq.nombre_evento = ase.nombre_evento
	and eq.nombre_institucion = ase.nombre_institucion
	and eq.nombre_asesor = ase.nombre
	join integrantes as intg on intg.nombre_evento = eq.nombre_evento
	and intg.nombre_institucion = eq.nombre_institucion
	and intg.nombre_equipo = eq.nombre
	and intg.categoria = eq.categoria where ev.nombre = name;

delimiter //
create procedure isJudgesEmpty(out msg boolean)
begin
	declare count int;

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;

	select count(*) into count from jueces;

		if count <> 0 then
			set msg = true;
		else
			set msg = false;
		end if;

	commit;
end//
delimiter ;

delimiter //
create procedure getJudges(category varchar(12))
begin

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;
		select distinct(jueces.nombre) from jueces where institucion not in (
			select distinct(eq.nombre_institucion) from
			equipos eq join integrantes intg on eq.nombre_evento = intg.nombre_evento 
			and eq.nombre_institucion = intg.nombre_institucion 
			and eq.categoria = intg.categoria 
			and eq.nombre = intg.nombre_equipo 
			where eq.categoria = category
		);
	commit;
end//
delimiter ;

delimiter //
create procedure addJury(nameJury varchar(30), event varchar(80), category varchar(12))
begin

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;
		insert into jurados values (nameJury, event, category);
	 commit;
end//
delimiter ;

delimiter //
create function getJudgeId(name varchar(32)) returns int deterministic
begin

	declare id_juez int;
    select id into @id_juez from jueces where nombre = name;
    
    return @id_juez;
end//
delimiter ;

delimiter //
create procedure addJuryJudge (nameJury varchar(30), event varchar(80), category varchar(12), nameJudge1 varchar(32), nameJudge2 varchar(32), nameJudge3 varchar(32), out msg varchar(100))
begin

	declare exit handler for sqlexception
	begin
		show errors;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings;
		rollback;
	end;

	start transaction;
    
		if (nameJudge1 is null or nameJudge2 is null or nameJudge3 is null) then
			set msg = 'Es necesario seleccionar los nombres para los 3 jueces.';
		elseif (nameJudge1 = nameJudge2 or nameJudge1 = nameJudge3 or nameJudge2 = nameJudge3) then
			set msg = 'Debe seleccionar jueces distintos.';
		elseif exists (select * from jurados where nombre_evento = event and categoria = category) then
			set msg = concat('Ya existe el jurado para la categoria ', category);
		else
			call addJury(nameJury, event, category);
			insert into jueces_jurados values
			((select getJudgeId(nameJudge1)), event, category),
			((select getJudgeId(nameJudge2)), event, category),
			((select getJudgeId(nameJudge3)), event, category);
			set msg = 'El jurado ha sido introducido correctamente.';
		end if;
   commit;
end//
delimiter ;

create procedure getJudgeJury(event varchar(80), category varchar(12))
	select nombre from jueces_jurados join jueces on id_juez = id
    where nombre_evento = event and categoria = category;

delimiter //
create function getInstitution(event varchar(80), team varchar(30), category varchar(12)) returns varchar(32) deterministic
begin
    
	declare institution varchar(32);
    
	select ins.nombre into @institution from eventos ev join instituciones ins 
	on ev.nombre = ins.nombre_evento
	join equipos eq on ins.nombre = eq.nombre_institucion 
	where categoria = category and ev.nombre = event 
	and eq.nombre = team and eq.nombre_evento = event;
    
    return @institution;
        
end//
delimiter ;

delimiter //
create procedure addEvaluation (event varchar(80), team varchar(30), category varchar(12), nameJudge varchar(32), out msg varchar(100))
begin

	declare ins varchar(32);
    declare id_judge int;

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;
    
    start transaction;
    
		set ins = (select getInstitution(event, team, category));
		set id_judge = (select id from jueces where nombre = nameJudge);
    
		if exists(select * from evaluacion where nombre_evento = event and nombre_institucion = ins and nombre_equipo = team and categoria = category and id_juez = id_judge) then
			set msg = concat('El equipo ', team, ' de la categoria ', category, ' ya cuenta con un juez asignado.');
		else
			insert into evaluacion (nombre_evento, nombre_institucion, nombre_equipo, categoria, id_juez)
			values (event, ins, team, category, id_judge);
			set msg = concat('Al equipo ', team, ' de la categoria ', category, ' se le ha asignado el juez ', nameJudge, '.');
		end if;
    
    commit;
    
end//
delimiter ;

delimiter //
create procedure updateEvent(name varchar(80), initialH time, finalH time, dateE date, campus varchar(50), p boolean, s boolean, b boolean, u boolean, status varchar(10), out msg varchar(100))
begin

	declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;

	start transaction;
    
		if exists(select * from eventos where nombre <> name and fecha = dateE) then
			set msg = 'La fecha selecccionada ya se encuentra registrada. Ingrese una fecha valida.';
		elseif (dateE < (select curdate() + interval 7 day)) then
			set msg = 'Fecha invalida. Ingrese una fecha igual o posterior a la del dia actual.';
		elseif (initialH >= finalH) then
			set msg = 'Horas invalidas. La hora inicial debe ser menor a la hora final del evento.';
		else
			update eventos
            set
				hora_i = initialH,
				hora_f = finalH,
				fecha = dateE,
				nombre_sede = campus,
				primaria = p,
				secundaria = s,
				bachillerato = b,
				universidad = u,
				estado = status
            where 
				nombre = name;
            set msg = 'El evento ha sido actualizado.';
		end if;
        
	commit;
end//
delimiter ;
-- ------------------------------------------- Juecessssss
delimiter //
create function getEventByCurrentDate() returns varchar(80) deterministic
begin
	declare event varchar(80);
    select nombre into @event from eventos where fecha = curdate() and curtime() between hora_i and hora_f;
    
    return @event;
end// 
delimiter ;

delimiter //
create procedure getParticipatingCategories(nameJudge varchar(32), out msg varchar(100))
begin
    
    declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;
    
    start transaction;
        
        if ((select getEventByCurrentDate()) is null) then
			set msg = 'El dia de hoy no se lleva a cabo ningun evento';
		else
			select categoria from jueces_jurados where id_juez = (select id from jueces where nombre = nameJudge) and nombre_evento = (select getEventByCurrentDate());
        end if;
        
    commit;
end//
delimiter ;

delimiter //
create procedure getTeamsToEvaluate(nameEvent varchar(80), nameJudge varchar(32), category varchar(12))
begin
	select * from evaluacion
    where nombre_evento = nameEvent
    and categoria = category
    and id_juez = (select getJudgeId(nameJudge));
end//
delimiter ;
-- ----------------------------------------------------------------------------------- Sigue siendo jueces
delimiter //
create procedure createRowDiseno (
    c1 boolean, c2 boolean, c3 boolean, c4 boolean, c5 boolean, c6 boolean,
    c7 boolean, c8 boolean, c9 boolean, c10 boolean, c11 boolean, c12 boolean
)
begin
	
    declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;
    
    start transaction;
		insert into diseno (
			reg_fechas,	just_de_cam_proto, diag_img,
			ortog_redacc, presentacion, vid_anim,
			dis_mod_autodesk, ana_elem, ensamble_proto,
			modelo_acorde_robot, acorde_simulacion,restricc_mov
		) values (
			c1, c2, c3, c4, c5, c6,
			c7, c8, c9, c10, c11, c12
		);
    commit;
end//
delimiter ;

delimiter //
create procedure createRowProgramacion (
    c1 boolean, c2 boolean, c3 boolean, c4 boolean, c5 boolean, c6 boolean,
    c7 boolean, c8 boolean, c9 boolean, c10 boolean, c11 boolean, c12 boolean,
    c13 boolean, c14 boolean, c15 boolean, c16 boolean, c17 boolean, c18 boolean,
    c19 boolean
)
begin
	
    declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;
    
    start transaction;
		insert into programacion (
			soft_robotc, uso_funciones, complejidad_prog, just_sec_prog,
			estrc_func_aplicadas, depuracion_cod, creacion_cod_mod, documentacion,
			vinculacion, declaracion_uso_dep, vinculo_joystick, eficiencia_calib,
			habilidad_joystick, respuesta_mando, docu_mod_driver, demo_15_seg,
			no_inconvenientes, demo_objetivo, exp_mod_auto
		) values (
			c1, c2, c3, c4, c5, c6, c7,
            c8, c9, c10, c11, c12, c13,
            c14, c15, c16, c17, c18, c19
		);
    commit;
end//
delimiter ;

delimiter //
create procedure createRowConstruccion (
    c1 boolean, c2 boolean, c3 boolean, c4 boolean, c5 boolean, c6 boolean,
    c7 boolean, c8 boolean, c9 boolean, c10 boolean, c11 boolean, c12 boolean,
    c13 boolean, c14 boolean, c15 boolean, c16 boolean, c17 boolean
)
begin
	
    declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;
    
    start transaction;
		insert into construccion (
			prototipo_estetico, estructuras_estables, sistemas,
			sensors, cableado, calculo_implementacion, alcance_robot,
			imp_estc_vex_rob, mono_core, analisis_estructuras,
			relacion_vel_ang, tren_engranes, centro_gravedad, sist_transmision,
			potencia, torque, velocidad
		) values (
			c1, c2, c3, c4, c5, c6, c7,
            c8, c9, c10, c11, c12, c13,
            c14, c15, c16, c17
		);
    commit;
end//
delimiter ;

create function getMaxIdWightings() returns int deterministic
	return (select max(id) from diseno);

delimiter //
create procedure evaluar(eventName varchar(80), institution varchar(32), team varchar(30), category varchar(12), judgeId int)
begin
	
    declare promD float;
    declare promP float;
    declare promC float;
    
    declare exit handler for sqlexception
	begin
		show errors limit 1;
		rollback;
	end;

	declare exit handler for sqlwarning
	begin
		show warnings limit 1;
		rollback;
	end;
    
    start transaction;
    
		update evaluacion
        set
			id_diseno = (select getMaxIdWightings()),
            id_programacion = (select getMaxIdWightings()),
            id_construccion = (select getMaxIdWightings())
		where
			nombre_evento = eventName and 
            nombre_institucion = institution and
            nombre_equipo = team and
            categoria = category and
            id_juez = judgeId;
            
            set @promD = (select 
							sum(case when reg_fechas = true then 1 else 0 end) +
							sum(case when just_de_cam_proto = true then 1 else 0 end) +
							sum(case when diag_img = true then 1 else 0 end) +
							sum(case when ortog_redacc = true then 1 else 0 end) +
							sum(case when presentacion = true then 1 else 0 end) +
							sum(case when vid_anim = true then 1 else 0 end) +
							sum(case when dis_mod_autodesk = true then 1 else 0 end) +
							sum(case when ana_elem = true then 1 else 0 end) +
							sum(case when ensamble_proto = true then 1 else 0 end) +
							sum(case when modelo_acorde_robot = true then 1 else 0 end) +
							sum(case when acorde_simulacion = true then 1 else 0 end) +
							sum(case when restricc_mov = true then 1 else 0 end)
							from diseno where id = (select getMaxIdWightings()));
			set @promP = (select 
							sum(case when soft_robotc = true then 1 else 0 end) +
							sum(case when uso_funciones = true then 1 else 0 end) +
							sum(case when complejidad_prog = true then 1 else 0 end) +
							sum(case when just_sec_prog = true then 1 else 0 end) +
							sum(case when estrc_func_aplicadas = true then 1 else 0 end) +
							sum(case when depuracion_cod = true then 1 else 0 end) +
							sum(case when creacion_cod_mod = true then 1 else 0 end) +
							sum(case when documentacion = true then 1 else 0 end) +
							sum(case when vinculacion = true then 1 else 0 end) +
							sum(case when declaracion_uso_dep = true then 1 else 0 end) +
							sum(case when vinculo_joystick = true then 1 else 0 end) +
							sum(case when eficiencia_calib = true then 1 else 0 end) +
							sum(case when habilidad_joystick = true then 1 else 0 end) +
							sum(case when respuesta_mando = true then 1 else 0 end) +
							sum(case when docu_mod_driver = true then 1 else 0 end) +
							sum(case when demo_15_seg = true then 1 else 0 end) +
							sum(case when no_inconvenientes = true then 1 else 0 end) +
							sum(case when demo_objetivo = true then 1 else 0 end) +
							sum(case when exp_mod_auto = true then 1 else 0 end)
							from programacion where id = (select getMaxIdWightings()));
			set @promC = (select 
							sum(case when prototipo_estetico = true then 1 else 0 end) +
							sum(case when estructuras_estables = true then 1 else 0 end) +
							sum(case when sistemas = true then 1 else 0 end) +
							sum(case when sensors = true then 1 else 0 end) +
							sum(case when cableado = true then 1 else 0 end) +
							sum(case when calculo_implementacion = true then 1 else 0 end) +
							sum(case when alcance_robot = true then 1 else 0 end) +
							sum(case when imp_estc_vex_rob = true then 1 else 0 end) +
							sum(case when mono_core = true then 1 else 0 end) +
							sum(case when analisis_estructuras = true then 1 else 0 end) +
							sum(case when relacion_vel_ang = true then 1 else 0 end) +
							sum(case when tren_engranes = true then 1 else 0 end) +
							sum(case when centro_gravedad = true then 1 else 0 end) +
							sum(case when sist_transmision = true then 1 else 0 end) +
							sum(case when potencia = true then 1 else 0 end) +
							sum(case when torque = true then 1 else 0 end) +
							sum(case when velocidad = true then 1 else 0 end) as cantidad_campos_true
							from construccion where id = (select getMaxIdWightings()));
			set @promD = @promD/12 * 10;
            set @promP = @promP/19 * 10;
            set @promC = @promC/17 * 10;
            
		update evaluacion
		set
			calificacion = @promD + @promP + @promC
		where
			nombre_evento = eventName and 
            nombre_institucion = institution and
            nombre_equipo = team and
            categoria = category and
            id_juez = judgeId and
            id_diseno = (select getMaxIdWightings()) and
            id_programacion = (select getMaxIdWightings()) and
            id_construccion = (select getMaxIdWightings());
    commit;
end//
delimiter ;

delimiter //
create procedure getCalificacion(eventName varchar(80), institution varchar(32), team varchar(30), category varchar(12), judgeId int)
    select calificacion from evaluacion
    where
		nombre_evento = eventName and 
        nombre_institucion = institution and
        nombre_equipo = team and
        categoria = category and
        id_juez = judgeId and
        id_diseno is not null and
        id_programacion is not null and
        id_construccion is not null;
end//
delimiter ;

delimiter //
create function isCalificationSet(eventName varchar(80), institution varchar(32), team varchar(30), category varchar(12), judgeId int) returns boolean deterministic
begin
declare resultado boolean;
    if exists (select calificacion from evaluacion
				where
					nombre_evento = eventName and 
					nombre_institucion = institution and
					nombre_equipo = team and
					categoria = category and
					id_juez = judgeId and
					id_diseno is not null and
					id_programacion is not null and
					id_construccion is not null
			  ) then
        set @resultado = true;
	else
		set @resultado = false;
	end if;
    
    return @resultado;
end//
delimiter ;