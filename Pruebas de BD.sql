use concursoVEX;

-- Sedes
insert sedes values ('FDSAHGD2345X1%G', 'Universidad Autonoma de Tamaulipas', 'Centro Universitario Tampico Madero S/N, Universidad Poniente, 89336 Tampico, Tamps.');

-- Eventos
insert eventos values('83ncv76keic903X', 'Decimo cuarto concurso de robotica versiculo 5', null, null, current_date(), (select id from sedes where nombre = 'Universidad Autonoma de Tamaulipas'));
insert eventos values('93ncv76keic903X', 'Decimo cuarto concurso de robotica versiculo 6', null, null, current_date()+1, (select id from sedes where nombre = 'Universidad Autonoma de Tamaulipas'));

select * from sedes;
select * from eventos;

-- Instituciones
insert instituciones values ('83ncv76keic903X', 'Universidad Autonoma de Tamaulipas', 'Centro Universitario Tampico Madero S/N, Universidad Poniente, 89336 Tampico, Tamps.');
insert instituciones values ('93ncv76keic903X', 'Universidad Autonoma de Tamaulipas', 'Centro Universitario Tampico Madero S/N, Universidad Poniente, 89336 Tampico, Tamps.');

select * from instituciones;