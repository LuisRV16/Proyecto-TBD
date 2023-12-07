use concursoVex;

delimiter //
create procedure isTeamsEmpty(out msg varchar(5))
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

create procedure getTeams(instituteName varchar(32), eventName varchar(50))
	select t.nombre, t.categoria from equipos as t where t.nombre_institucion = instituteName and t.nombre_evento = eventName order by t.categoria, t.nombre;
    
    
create procedure getTeamsInfo(teamName varchar(30), eventName varchar(50), category varchar(12))
	select i.nombre, i.edad from integrantes as i 
		where i.nombre_equipo = teamName and i.nombre_evento = eventName and i.categoria = category;

delimiter //
create function getTeamCoach(teamName varchar(30), eventName varchar(50), category varchar(12)) returns varchar(50) deterministic
begin
	declare coachName varchar(50);
	select t.nombre_asesor into coachName from equipos as t where t.nombre = teamName and t.nombre_evento = eventName and t.categoria = category;
    return coachName;
end//
delimiter ; 

use concursoVex;
drop procedure addTeam;
delimiter //
create procedure addTeam(eventName varchar(50), instituteName varchar(32), coachName varchar(50), coachMail varchar(100), teamName varchar(30), category varchar(12), 
	firstStudentName varchar (50), firstStudentAge int, secondStudentName varchar (50), secondStudentAge int, thirdStudentName varchar (50), thirdStudentAge int, out msg varchar(100))
begin
declare sameStudentSameTeam boolean;
declare coachNull boolean;
declare teamNull boolean;
declare studentSameEventAndInstitutionNull boolean;
declare coachNameError boolean;
declare ageKey boolean;

set msg = '';
set sameStudentSameTeam = false;
set coachNull = false;
set teamNull = false;
set studentSameEventAndInstitutionNull = false;
set coachNameError = false;
set ageKey = false;

start transaction;
	-- Comprobar edades
    if ((category = 'Primaria') and (firstStudentAge between 6 and 14) and (secondStudentAge between 6 and 14) and (thirdStudentAge between 6 and 14)) then
		set ageKey = true;
	elseif ((category = 'Secundaria') and (firstStudentAge between 11 and 17) and (secondStudentAge between 11 and 17) and (thirdStudentAge between 11 and 17)) then
		set ageKey = true;
	elseif ((category = 'Bachillerato') and (firstStudentAge between 14 and 19) and (secondStudentAge between 14 and 19) and (thirdStudentAge between 14 and 19)) then
		set ageKey = true;
	elseif ((category = 'Universidad') and (firstStudentAge between 17 and 27) and (secondStudentAge between 17 and 27) and (thirdStudentAge between 17 and 27)) then
		set ageKey = true;
    end if;
    -- comprueba que no se repita el estudiante en el equipo
    if (char_length(firstStudentName) < 3 or char_length(secondStudentName) < 3 or char_length(thirdStudentName) < 3) then
			set msg = 'Uno de los estudiantes tiene un nombre corto';
	elseif (char_length(firstStudentAge) <= 3 or char_length(secondStudentAge) <= 3 or char_length(thirdStudentAge) <= 3) then
			set msg = 'Uno de los estudiantes es demasiado joven';
	end if;
	if (firstStudentName = secondStudentName or firstStudentName = thirdStudentName or thirdStudentName = secondStudentName) then
        set sameStudentSameTeam = true;
	end if;
	-- Comprueba que no exista el asesor
	if not exists(select * from asesores where replace(upper(nombre_evento), ' ', '') = replace(upper(eventName), ' ', '') and replace(upper(nombre_institucion), ' ', '') = replace(upper(instituteName), ' ', '')
		and replace(upper(nombre), ' ', '') = replace(upper(coachName), ' ', '')) 
        then
		set coachNull = true;
	end if;
    -- comprueba que no exista el equipo en ese evento y categoria 
	if not exists(select * from equipos where replace(upper(nombre_evento), ' ', '') = replace(upper(eventName), ' ', '') and replace(upper(categoria), ' ', '') = replace(upper(category), ' ', '')
		and replace(upper(nombre),' ','') = replace(upper(teamName),' ','')) 
        then
        set teamNull = true;
	end if;
		-- comprueba que el estudiante no este en el mismo evento e institucion
	if not exists(select * from integrantes where replace(upper(nombre_evento), ' ', '') = replace(upper(eventName),' ','') and replace(upper(nombre_institucion),' ','') = replace(upper(instituteName),' ','') 
											and replace(upper(categoria),' ','') = replace(upper(category),' ','')
											and (replace(upper(nombre),' ','') = replace(upper(firstStudentName), ' ','')
                                            or replace(upper(nombre),' ','') = replace(upper(secondStudentName), ' ','')
                                            or replace(upper(nombre),' ','') = replace(upper(thirdStudentName), ' ',''))) then
		set studentSameEventAndInstitutionNull = true;
	end if;
			-- verifica la existencia del asesor
	if (sameStudentSameTeam = false and coachNull = true and teamNull = true and studentSameEventAndInstitutionNull = true and ageKey = true) then
		insert into asesores 	values (eventName, instituteName, coachName, coachMail);
		insert into equipos 	values (eventName, instituteName, teamName, coachName, category);  
		insert into integrantes values (eventName, instituteName, teamName, category, firstStudentName, firstStudentAge);
		insert into integrantes values (eventName, instituteName, teamName, category, secondStudentName, secondStudentAge);
		insert into integrantes values (eventName, instituteName, teamName, category, thirdStudentName, thirdStudentAge);
		set msg = 'Insercion exitosa';
	elseif (sameStudentSameTeam = false and coachNull = false and teamNull = true and studentSameEventAndInstitutionNull = true and ageKey = true) then
		if not exists(select * from asesores where nombre = coachName) then
			set msg = 'Error en el nombre del asesor';
            select msg;
        else
		insert into equipos 	values (eventName, instituteName, teamName, coachName, category);  
		insert into integrantes values (eventName, instituteName, teamName, category, firstStudentName, firstStudentAge);
		insert into integrantes values (eventName, instituteName, teamName, category, secondStudentName, secondStudentAge);
		insert into integrantes values (eventName, instituteName, teamName, category, thirdStudentName, thirdStudentAge);
		set msg = 'Insercion exitosa coach existente';
	end if;
	elseif (sameStudentSameTeam = true) then
		set msg = 'Se repite un estudiante';
	elseif (teamNull = false) then
		set msg = 'Ya existe el equipo';
	elseif (studentSameEventAndInstitutionNull = false) then
		set msg = 'Un estudiante participa en otro equipo';
	elseif (ageKey = false) then
		set msg = 'Un alumno no entra en el rango de edad';
	end if;
commit;
select msg;
end//
delimiter ;

delimiter //
create procedure joinEvent(instituteName varchar(32), eventName varchar(50), out msg varchar(100))
begin
declare address varchar(100);
	if not exists(select * from instituciones where replace(upper(nombre),' ','') = replace(upper(instituteName),' ','') and replace(upper(nombre_evento),' ','') = replace(upper(eventName),' ','')) 
    then
		set address = (select institutionAddress(instituteName));
		insert into instituciones values(eventName, instituteName, address);
        set msg = 'Se unio al evento';
	else
		set msg = 'Ya estas en este evento';
    end if;
SELECT msg;
end//
delimiter ;

delimiter //
create function institutionAddress(instituteName varchar(32)) returns varchar(100) deterministic
begin
	declare institutionAdress varchar(100);
	select distinct (i.direccion) into institutionAdress from instituciones as i where i.nombre = instituteName;
    return institutionAdress;
end//
delimiter ; 

delimiter //
create function getCoachMail(coachName varchar(50)) returns varchar(100) deterministic
begin
	declare mail varchar(100);
	select distinct (c.correo) into mail from asesores as c where c.nombre = coachName;
    return mail;
end//
delimiter ;

-- Crear institucion
delimiter //
create procedure newInstitution(instituteName varchar(32), eventName varchar(50), instituteAdress varchar(100), institutePassword varchar(32))
begin
	SET @createUserCMD = concat('CREATE USER ''', j_name, '''@''', 'localhost', '''IDENTIFIED BY ''', j_password, ''';');
	PREPARE createUserStatement FROM @createUserCMD;
	EXECUTE createUserStatement;
	DEALLOCATE PREPARE createUserStatement;
end//
delimiter ;
call assignGrant('Anglo');
delimiter //
create procedure assignGrant(instituteName varchar(32))
begin
	set @sedes = CONCAT('grant select on concursoVEX.sedes to ''',instituteName,'''@''localhost''');
    set @eventos = CONCAT('grant select on concursoVEX.eventos to ''',instituteName,'''@''localhost''');
    set @asesores = CONCAT('grant select, insert, update on concursoVEX.asesores to ''',instituteName,'''@''localhost''');
    set @instituciones = CONCAT('grant select, insert on concursoVEX.instituciones to ''',instituteName,'''@''localhost''');
    set @equipos = CONCAT('grant select, insert, update on concursoVEX.equipos to ''',instituteName,'''@''localhost''');
    set @integrantes = CONCAT('grant select, insert, update on concursoVEX.integrantes to ''',instituteName,'''@''localhost''');
    set @evaluacion = CONCAT('grant select on concursoVEX.evaluacion to ''',instituteName,'''@''localhost''');
    set @getActiveEvent = CONCAT('grant execute on procedure concursoVex.getActiveEvent to ''',instituteName,'''@''localhost''');
    set @isEventsEmpty = CONCAT('grant execute on procedure concursoVex.isEventsEmpty to ''',instituteName,'''@''localhost''');
    set @getEventNamesByCategory = CONCAT('grant execute on procedure concursoVex.getEventNamesByCategory to ''',instituteName,'''@''localhost''');
    set @getEvent = CONCAT('grant execute on procedure concursoVex.getEvent to ''',instituteName,'''@''localhost''');
    set @getEventbyID = CONCAT('grant execute on procedure concursoVex.getEventbyID to ''',instituteName,'''@''localhost''');
    set @instituteGetEventNames = CONCAT('grant execute on procedure concursoVex.instituteGetEventNames to ''',instituteName,'''@''localhost''');
    set @instituteGetEvent = CONCAT('grant execute on procedure concursoVex.instituteGetEvent to ''',instituteName,'''@''localhost''');
    set @isTeamsEmpty = CONCAT('grant execute on procedure concursoVex.isTeamsEmpty to ''',instituteName,'''@''localhost''');
    set @getTeams = CONCAT('grant execute on procedure concursoVex.getTeams to ''',instituteName,'''@''localhost''');
    set @getTeamsInfo = CONCAT('grant execute on procedure concursoVex.getTeamsInfo to ''',instituteName,'''@''localhost''');
    set @getTeamCoach = CONCAT('grant execute on function concursoVex.getTeamCoach to ''',instituteName,'''@''localhost''');
    set @addTeam = CONCAT('grant execute on procedure concursoVex.addTeam to ''',instituteName,'''@''localhost''');
    set @joinEvent = CONCAT('grant execute on procedure concursoVex.joinEvent to ''',instituteName,'''@''localhost''');
    set @institutionAdress = CONCAT('grant execute on function concursoVex.institutionAddress to ''',instituteName,'''@''localhost''');
    set @getCurdatePlus7 = CONCAT('grant execute on function concursoVex.getCurdatePlus7 to ''',instituteName,'''@''localhost''');
    set @getCurdate = CONCAT('grant execute on function concursoVex.getCurdate to ''',instituteName,'''@''localhost''');
    set @getCategory = CONCAT('grant execute on procedure concursoVex.getCategory to ''',instituteName,'''@''localhost''');
	SET @q = @sedes; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @eventos; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @asesores; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @instituciones; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @integrantes; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @evaluacion; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getActiveEvent; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @isEventsEmpty; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getEventNamesByCategory; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getEvent; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getEventbyID; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @instituteGetEventNames; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @instituteGetEvent; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @isTeamsEmpty; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getTeams; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getTeamsInfo; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getTeamCoach; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @addTeam; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @joinEvent; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @institutionAdress; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getCurdatePlus7; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getCurdate; PREPARE stmt FROM @q; EXECUTE stmt;
    SET @q = @getCategory; PREPARE stmt FROM @q; EXECUTE stmt;
    
	DEALLOCATE PREPARE stmt;
end//
delimiter ;

create function getCurdate() returns date deterministic
	return curdate() - interval 7 day;

create function getCurdatePlus7() returns date deterministic
	return curdate() + interval 7 day;

create procedure getActiveEvent()
	select nombre from eventos where fecha >= (select getCurdatePlus7()) order by fecha;


delimiter //
create procedure getEventNamesByCategory(category varchar(50))
begin
	if (category = 'Todos los eventos')
		then select nombre from eventos as e where e.estado = 'Vigente' and e.fecha >= CURDATE() + interval 7 day order by nombre;
    elseif (category = 'Primaria')
		then select nombre from eventos as e where e.estado = 'Vigente' and e.primaria = true and e.fecha >= CURDATE() + interval 7 day order by nombre;
    elseif (category = 'Secundaria')
		then select nombre from eventos as e where e.estado = 'Vigente' and e.secundaria = true and e.fecha >= CURDATE() + interval 7 day order by nombre;
	elseif (category = 'Bachillerato')
		then select nombre from eventos as e where e.estado = 'Vigente' and e.bachillerato = true and e.fecha >= CURDATE() + interval 7 day order by nombre;
	elseif (category = 'Universidad')
		then select nombre from eventos as e where e.estado = 'Vigente' and e.universidad = true and e.fecha >= CURDATE() + interval 7 day order by nombre;
	end if;
end//
delimiter ;

create procedure instituteGetEvent(instituteName varchar(32))
	select * from eventos as e where e.nombre_evento = instituteName;
    
create procedure instituteGetEventNames(instituteName varchar(32))
	select e.nombre from eventos as e join instituciones as i on e.nombre = i.nombre_evento 
		where i.nombre = instituteName order by e.fecha;

delimiter //
create procedure userInstitution(institutionName varchar(32), eventName varchar(50), institutionAddress varchar(100), institutionPassword varchar(32), out msg varchar(100))
begin
	start transaction;
		if (char_length(institutionName) < 3) then
			set msg = 'Nombre muy corto.';
		elseif (char_length(institutionAddress) <= 4) then
			set msg = 'Direccion muy corta.';
		elseif (char_length(institutionPassword) <= 8) then
			set msg = 'Contraseña muy corta. Debe ser mayor a 8 caracteres';
		elseif not exists (select * from instituciones where replace(upper(nombre),' ','') = replace(upper(institutionName),' ',''))
        then
			SET @createUser = concat('CREATE USER ''', institutionName, '''@''', 'localhost', '''IDENTIFIED BY ''', institutionPassword, ''';');
			PREPARE createUserStatement FROM @createUser; EXECUTE createUserStatement; DEALLOCATE PREPARE createUserStatement;
			call assignGrant(institutionName);
			insert into instituciones values(eventName,institutionName,institutionAddress);
            set msg = 'El usuario se creo de manera exitosa';
		elseif exists (select * from instituciones where replace(upper(nombre),' ','') = replace(upper(institutionName),' ',''))
        then
			set msg = 'El usuario ya existe';
		else
			set msg = 'No deberia decir esto, como le hizo';
        end if;
	commit;
end //
delimiter ;

delimiter //
create procedure getCategory(eventName varchar(50), out p boolean, out s boolean, out b boolean, out u boolean)
begin
set p = false;
set s = false;
set b = false;
set u = false;
	if exists (select * from eventos as e where e.nombre = eventName and e.primaria = true) then
		set p = true;
    end if ;
    if exists (select * from eventos as e where e.nombre = eventName and e.secundaria = true) then
		set s = true;
    end if ;
    if exists (select * from eventos as e where e.nombre = eventName and e.bachillerato = true) then
		set b = true;
    end if ;
    if exists (select * from eventos as e where e.nombre = eventName and e.universidad = true) then
		set u = true;
    end if ;
end //
delimiter ;