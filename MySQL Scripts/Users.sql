-- Users Concurso VEX
use concursoVEX;
use mysql;


-- Nivel Base de datos
create user 'admin'@'localhost' identified by 'admin123';
grant select, update, insert on concursoVEX.* to 'admin'@'localhost';
grant execute on procedure concursoVEX.addCampus to 'admin'@'localhost';
grant execute on procedure concursoVEX.addJudge to 'admin'@'localhost';
grant execute on procedure concursoVEX.isCampusEmpty to 'admin'@'localhost';
grant execute on procedure concursoVEX.getCampus to 'admin'@'localhost';
grant execute on procedure concursoVEX.addEvent to 'admin'@'localhost';
grant execute on procedure concursoVEX.isEventsEmpty to 'admin'@'localhost';
grant execute on procedure concursoVEX.getEvent to 'admin'@'localhost';
grant execute on procedure concursoVEX.getEventByName to 'admin'@'localhost';
grant execute on procedure concursoVEX.getCategories to 'admin'@'localhost';
grant execute on procedure concursoVEX.isJudgesEmpty to 'admin'@'localhost';
grant execute on procedure concursoVEX.getJudges to 'admin'@'localhost';
grant execute on procedure concursoVEX.addJury to 'admin'@'localhost';
grant execute on function concursoVEX.getJudgeId to 'admin'@'localhost';
grant execute on procedure concursoVEX.addJuryJudge to 'admin'@'localhost';
grant execute on procedure concursoVEX.getEventByID to 'admin'@'localhost';
grant execute on procedure concursoVEX.getJudgeJury to 'admin'@'localhost';
grant execute on function concursoVEX.getInstitution to 'admin'@'localhost';
grant execute on procedure concursoVEX.addEvaluation to 'admin'@'localhost';
grant execute on procedure concursoVEX.updateEvent to 'admin'@'localhost';
grant execute on procedure concursoVEX.getTeamsToEvaluate to 'admin'@'localhost';
grant execute on procedure concursoVEX.userJudge to 'admin'@'localhost';
grant execute on procedure concursoVEX.grantJudge to 'admin'@'localhost';


-- El admin no tiene permitido eliminar campos ni tablas de la base de datos

-- Nivel de tabla
	-- Consulta

create user 'consulta'@'localhost' identified by '';
grant select on concursoVEX.eventos to 'consulta'@'localhost';
grant execute on procedure concursoVex.isEventsEmpty to 'consulta'@'localhost';
grant execute on procedure concursoVex.getEventNamesByCategory to 'consulta'@'localhost';
grant execute on procedure concursoVex.getEvent to 'consulta'@'localhost';
grant execute on procedure concursoVex.getEventbyID to 'consulta'@'localhost';
grant execute on procedure concursoVex.instituteGetEvent to 'consulta'@'localhost';
grant execute on procedure concursoVex.userInstitution to 'consulta'@'localhost';
grant execute on procedure concursoVex.getActiveEvent to 'consulta'@'localhost';

flush privileges;