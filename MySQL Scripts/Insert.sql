-- Insert Concurso VEX
use concursoVEX;
select * from eventos;
select * from instituciones;
grant execute on procedure concursoVEX.addCampus to 'Colegio Boston'@'localhost';
select * from jueces;
insert into sedes (nombre, direccion)values('sede 1', 'direccion 1'),
										   ('sede 2', 'direccion 2'),
                                           ('sede 3', 'direccion 3');
call addCampus('sede 4','direccion 4', @msg);
                                           
insert into eventos (nombre, hora_i, hora_f, fecha, nombre_sede, primaria, secundaria, bachillerato, universidad, estado)
	values('evento 1', '10:00', '20:00', '2023-01-01', 'sede 1', 0, true, true, true, 'Vigente'),
		  ('evento 2', '10:00', '20:00', '2023-02-01', 'sede 1', true, true, true, true, 'Vigente'),
          ('evento 3', '10:00', '20:00', '2024-01-01', 'sede 3', 0, 0, true, true, 'Vigente');

insert into eventos values ('Evento de Prueba', '10:00:00', '11:00:00', '2023-12-04', 'sede 1', true, true, true, true, 'Vigente');

select * from eventos;

insert into jurados (nombre, nombre_evento, categoria) values ('jurado 1', 'evento 1', 'secundaria'),
													          ('jurado 2', 'evento 1', 'bachillerato'),
															  ('jurado 3', 'evento 1', 'universidad'),
															  ('jurado 1', 'evento 2', 'primaria'),
															  ('jurado 2', 'evento 2', 'secundaria'),
															  ('jurado 3', 'evento 2', 'bachillerato'),
															  ('jurado 4', 'evento 2', 'universidad'),
															  ('jurado 1', 'evento 3', 'bachillerato'),
															  ('jurado 2', 'evento 3', 'universidad');
                                                            
truncate table jurados;

insert into jueces (nombre, institucion, nivel_academico)
	values('Luis', 'Instituto Tecnologico de Ciudad Madero', 'Maestria'),
		  ('Manuel', 'Universidad del Noreste', 'Licenciatura'),
          ('Gabriel', 'Universidad del Valle Bravo', 'Licenciatura'),
          ('Montserrat', 'Instituto de Estudios Superiores de Tamaulipas', 'Maestria'),
          ('Noe', 'Universidad Autonoma de Tamaulipas', 'Licenciatura'),
          ('Eduardo', 'Universidad Tecnologica de Altamira', 'Doctorado'),
          ('Mariana', 'Instituto Tecnologico de Ciudad Madero', 'Licenciatura'),
          ('Pedro', 'Instituto Tecnologico de Ciudad Madero', 'Licenciatura'),
          ('Javier', 'Instituto Cultural Tampico', 'Licenciatura'),
          ('Fernanda', 'Universidad Autonoma de Tamaulipas', 'Licenciatura');

insert into jueces_jurados (id_juez, nombre_evento, categoria)
	values(1, 'evento 1', 'secundaria'),
		  (2, 'evento 1', 'secundaria'),
          (3, 'evento 1', 'secundaria'),
          (4, 'evento 1', 'bachillerato'),
          (5, 'evento 1', 'bachillerato'),
          (6, 'evento 1', 'bachillerato'),
          (7, 'evento 1', 'universidad'),
          (8, 'evento 1', 'universidad'),
          (9, 'evento 1', 'universidad'),
          (1, 'evento 2', 'primaria'),
          (2, 'evento 2', 'primaria'),
          (3, 'evento 2', 'primaria'),
          (4, 'evento 2', 'secundaria'),
          (5, 'evento 2', 'secundaria'),
          (6, 'evento 2', 'secundaria'),
          (7, 'evento 2', 'bachillerato'),
          (8, 'evento 2', 'bachillerato'),
          (9, 'evento 2', 'bachillerato'),
          (10, 'evento 2', 'universidad'),
          (11, 'evento 2', 'universidad'),
          (12, 'evento 2', 'universidad'),
          (1, 'evento 3', 'bachillerato'),
          (2, 'evento 3', 'bachillerato'),
          (3, 'evento 3', 'bachillerato'),
          (4, 'evento 3', 'universidad'),
          (5, 'evento 3', 'universidad'),
          (6, 'evento 3', 'universidad');
          
insert into instituciones values ('evento 1', 'itcm', 'Av. primero de Mayo');
insert into instituciones values ('evento 2', 'itcm', 'Av. primero de Mayo');
insert into instituciones values ('evento 1', 'Institucion 1', 'direccion 1'),
								 ('evento 1', 'Institucion 2', 'direccion 2'),
                                 ('evento 1', 'Institucion 3', 'direccion 3'),
                                 ('evento 1', 'Institucion 4', 'direccion 4'),
                                 ('evento 1', 'Institucion 5', 'direccion 5'),
                                 ('evento 2', 'Institucion 1', 'direccion 1'),
								 ('evento 2', 'Institucion 2', 'direccion 2'),
                                 ('evento 2', 'Institucion 3', 'direccion 3'),
                                 ('evento 2', 'Institucion 4', 'direccion 4'),
                                 ('evento 2', 'Institucion 5', 'direccion 5'),
                                 ('evento 3', 'Institucion 1', 'direccion 1'),
								 ('evento 3', 'Institucion 2', 'direccion 2'),
                                 ('evento 3', 'Institucion 3', 'direccion 3'),
                                 ('evento 3', 'Institucion 4', 'direccion 4'),
                                 ('evento 3', 'Institucion 5', 'direccion 5');
                                 
insert into instituciones values ('Evento de Prueba', 'Institucion 1', 'direccion 1'),
								 ('Evento de Prueba', 'Institucion 2', 'direccion 2'),
                                 ('Evento de Prueba', 'Institucion 3', 'direccion 3'),
                                 ('Evento de Prueba', 'Institucion 4', 'direccion 4'),
                                 ('Evento de Prueba', 'Institucion 5', 'direccion 5');
                                 
insert into asesores values ('evento 1', 'itcm', 'Elizabeth Cortez Razo', 'elizabeth.cr@cdmadero.tecnm.mx');
insert into asesores values ('evento 2', 'itcm', 'Federico Alonso Valadez', 'federico.av@cdmadero.tecnm.mx');							
insert into asesores values ('evento 1', 'Institucion 1', 'Pablo', 'pablo@gmail.com'),
						    ('evento 1', 'Institucion 2', 'Tyrone', 'tyrone@gmail.com'),
						    ('evento 1', 'Institucion 3', 'Tasha', 'tasha@gmail.com'),
						    ('evento 1', 'Institucion 4', 'Austin', 'austin@gmail.com'),
						    ('evento 1', 'Institucion 5', 'Uniqua', 'uniqua@gmail.com'),
						    ('evento 2', 'Institucion 1', 'Pablo', 'pablo@gmail.com'),
						    ('evento 2', 'Institucion 2', 'Tyrone', 'tyrone@gmail.com'),
						    ('evento 2', 'Institucion 3', 'Tasha', 'tasha@gmail.com'),
						    ('evento 2', 'Institucion 4', 'Austin', 'austin@gmail.com'),
						    ('evento 2', 'Institucion 5', 'Uniqua', 'uniqua@gmail.com'),
							('evento 3', 'Institucion 1', 'Pablo', 'pablo@gmail.com'),
						    ('evento 3', 'Institucion 2', 'Tyrone', 'tyrone@gmail.com'),
						    ('evento 3', 'Institucion 3', 'Tasha', 'tasha@gmail.com'),
						    ('evento 3', 'Institucion 4', 'Austin', 'austin@gmail.com'),
						    ('evento 3', 'Institucion 5', 'Uniqua', 'uniqua@gmail.com');
                            
insert into asesores values ('Evento de Prueba', 'Institucion 1', 'Pablo', 'pablo@gmail.com'),
						    ('Evento de Prueba', 'Institucion 2', 'Tyrone', 'tyrone@gmail.com'),
						    ('Evento de Prueba', 'Institucion 3', 'Tasha', 'tasha@gmail.com'),
						    ('Evento de Prueba', 'Institucion 4', 'Austin', 'austin@gmail.com'),
						    ('Evento de Prueba', 'Institucion 5', 'Uniqua', 'uniqua@gmail.com');

insert into equipos values  ('evento 1', 'itcm', 'DropDatabase', 'Elizabeth Cortez Razo', 'universidad');    
insert into equipos values  ('evento 2', 'itcm', 'Patitos', 'Federico Alonso Valadez', 'universidad');                        
insert into equipos values  ('evento 1', 'Institucion 1', 'Patitos', 'Pablo', 'secundaria'),
							('evento 1', 'Institucion 2', 'DropDatabase', 'Tyrone', 'secundaria'),
							('evento 1', 'Institucion 3', 'Supermanes', 'Tasha', 'bachillerato'),
							('evento 1', 'Institucion 4', 'Spidermanes', 'Austin', 'universidad'),
							('evento 1', 'Institucion 5', 'Pythons', 'Uniqua', 'universidad'),
							('evento 2', 'Institucion 1', 'Patitos', 'Pablo', 'primaria'),
							('evento 2', 'Institucion 2', 'DropDatabase', 'Tyrone', 'secundaria'),
							('evento 2', 'Institucion 3', 'Supermanes', 'Tasha', 'bachillerato'),
							('evento 2', 'Institucion 4', 'Spidermanes', 'Austin', 'universidad'),
							('evento 2', 'Institucion 5', 'Pythons', 'Uniqua', 'universidad'),
							('evento 3', 'Institucion 1', 'Patitos', 'Pablo', 'bachillerato'),
							('evento 3', 'Institucion 2', 'DropDatabase', 'Tyrone', 'bachillerato'),
							('evento 3', 'Institucion 3', 'Supermanes', 'Tasha', 'bachillerato'),
							('evento 3', 'Institucion 4', 'Spidermanes', 'Austin', 'universidad'),
							('evento 3', 'Institucion 5', 'Pythons', 'Uniqua', 'universidad');
                            
                            
insert into equipos values  ('Evento de Prueba', 'Institucion 1', 'Patitos', 'Pablo', 'secundaria'),
							('Evento de Prueba', 'Institucion 2', 'DropDatabase', 'Tyrone', 'secundaria'),
							('Evento de Prueba', 'Institucion 3', 'Supermanes', 'Tasha', 'bachillerato'),
							('Evento de Prueba', 'Institucion 4', 'Spidermanes', 'Austin', 'universidad'),
							('Evento de Prueba', 'Institucion 5', 'Pythons', 'Uniqua', 'universidad'),
                            ('Evento de Prueba', 'Institucion 1', 'Gorriones', 'Pablo', 'universidad'),
                            ('Evento de Prueba', 'Institucion 2', 'Garrafones', 'Tyrone', 'secundaria'),
                            ('Evento de Prueba', 'Institucion 3', 'Superhombres', 'Tasha', 'bachillerato'),
                            ('Evento de Prueba', 'Institucion 1', 'Patitos', 'Pablo', 'primaria'),
                            ('Evento de Prueba', 'Institucion 2', 'Guerreros', 'Tyrone', 'primaria'),
                            ('Evento de Prueba', 'Institucion 4', 'Aguilas', 'Austin', 'bachillerato');
insert into equipos values  ('Evento de Prueba', 'Institucion 3', 'Bolitas', 'Tasha', 'primaria');

truncate table integrantes;

insert into integrantes values('evento 1', 'itcm', 'DropDatabase', 'universidad', 'Luis', 19),
							  ('evento 1', 'itcm', 'DropDatabase', 'universidad', 'Manuel', 20),
                              ('evento 1', 'itcm', 'DropDatabase', 'universidad', 'Roberto', 20);
insert into integrantes values('evento 2', 'itcm', 'Patitos', 'universidad', 'Luis', 19),
							  ('evento 2', 'itcm', 'Patitos', 'universidad', 'Manuel', 20),
                              ('evento 2', 'itcm', 'Patitos', 'universidad', 'Marianita', 20);
insert into integrantes values
('evento 1', 'Institucion 1', 'Patitos', 'secundaria', 'Luis', 14),
('evento 1', 'Institucion 1', 'Patitos', 'secundaria', 'Manuel', 14),
('evento 1', 'Institucion 1', 'Patitos', 'secundaria', 'Mariana', 14),
('evento 1', 'Institucion 2', 'DropDatabase', 'secundaria', 'Publicho', 14),
('evento 1', 'Institucion 2', 'DropDatabase', 'secundaria', 'Pablacho', 14),
('evento 1', 'Institucion 2', 'DropDatabase', 'secundaria', 'Pochano', 14),
('evento 1', 'Institucion 3', 'Supermanes', 'bachillerato', 'Peter', 16),
('evento 1', 'Institucion 3', 'Supermanes', 'bachillerato', 'Clark', 16),
('evento 1', 'Institucion 3', 'Supermanes', 'bachillerato', 'Bruce', 16),
('evento 1', 'Institucion 4', 'Spidermanes', 'universidad', 'Pedro', 18),
('evento 1', 'Institucion 4', 'Spidermanes', 'universidad', 'Clarencio', 18),
('evento 1', 'Institucion 4', 'Spidermanes', 'universidad', 'Bruno', 18),
('evento 1', 'Institucion 5', 'Pythons', 'universidad', 'Wanda', 18),
('evento 1', 'Institucion 5', 'Pythons', 'universidad', 'Gwen', 18),
('evento 1', 'Institucion 5', 'Pythons', 'universidad', 'Gwendolin', 18),
('evento 2', 'Institucion 1', 'Patitos', 'primaria', 'Luis', 10),
('evento 2', 'Institucion 1', 'Patitos', 'primaria', 'Manuel', 10),
('evento 2', 'Institucion 1', 'Patitos', 'primaria', 'Mariana', 10),
('evento 2', 'Institucion 2', 'DropDatabase', 'secundaria', 'Publicho', 14),
('evento 2', 'Institucion 2', 'DropDatabase', 'secundaria', 'Pablacho', 14),
('evento 2', 'Institucion 2', 'DropDatabase', 'secundaria', 'Pochano', 14),
('evento 2', 'Institucion 3', 'Supermanes', 'bachillerato', 'Peter', 16),
('evento 2', 'Institucion 3', 'Supermanes', 'bachillerato', 'Clark', 16),
('evento 2', 'Institucion 3', 'Supermanes', 'bachillerato', 'Bruce', 16),
('evento 2', 'Institucion 4', 'Spidermanes', 'universidad', 'Pedro', 18),
('evento 2', 'Institucion 4', 'Spidermanes', 'universidad', 'Clarencio', 18),
('evento 2', 'Institucion 4', 'Spidermanes', 'universidad', 'Bruno', 18),
('evento 2', 'Institucion 5', 'Pythons', 'universidad', 'Wanda', 18),
('evento 2', 'Institucion 5', 'Pythons', 'universidad', 'Gwen', 18),
('evento 2', 'Institucion 5', 'Pythons', 'universidad', 'Gwendolin', 18),
('evento 3', 'Institucion 1', 'Patitos', 'bachillerato', 'Luis', 16),
('evento 3', 'Institucion 1', 'Patitos', 'bachillerato', 'Manuel', 16),
('evento 3', 'Institucion 1', 'Patitos', 'bachillerato', 'Mariana', 16),
('evento 3', 'Institucion 2', 'DropDatabase', 'bachillerato', 'Publicho', 16),
('evento 3', 'Institucion 2', 'DropDatabase', 'bachillerato', 'Pablacho', 16),
('evento 3', 'Institucion 2', 'DropDatabase', 'bachillerato', 'Pochano', 16),
('evento 3', 'Institucion 3', 'Supermanes', 'bachillerato', 'Peter', 16),
('evento 3', 'Institucion 3', 'Supermanes', 'bachillerato', 'Clark', 16),
('evento 3', 'Institucion 3', 'Supermanes', 'bachillerato', 'Bruce', 16),
('evento 3', 'Institucion 4', 'Spidermanes', 'universidad', 'Pedro', 16),
('evento 3', 'Institucion 4', 'Spidermanes', 'universidad', 'Clarencio', 16),
('evento 3', 'Institucion 4', 'Spidermanes', 'universidad', 'Bruno', 16),
('evento 3', 'Institucion 5', 'Pythons', 'universidad', 'Wanda', 16),
('evento 3', 'Institucion 5', 'Pythons', 'universidad', 'Gwen', 16),
('evento 3', 'Institucion 5', 'Pythons', 'universidad', 'Gwendolin', 16);

insert into integrantes values
('Evento de Prueba', 'Institucion 1', 'Patitos', 'primaria', 'Pato1', 10),
('Evento de Prueba', 'Institucion 1', 'Patitos', 'primaria', 'Pato2', 10),
('Evento de Prueba', 'Institucion 1', 'Patitos', 'primaria', 'Pato3', 10),
('Evento de Prueba', 'Institucion 2', 'Guerreros', 'primaria', 'Leonidas', 14),
('Evento de Prueba', 'Institucion 2', 'Guerreros', 'primaria', 'Napoleon', 14),
('Evento de Prueba', 'Institucion 2', 'Guerreros', 'primaria', 'Zeus', 14),
('Evento de Prueba', 'Institucion 3', 'Bolitas', 'primaria', 'Parker', 16),
('Evento de Prueba', 'Institucion 3', 'Bolitas', 'primaria', 'Reyes', 16),
('Evento de Prueba', 'Institucion 3', 'Bolitas', 'primaria', 'Gutierrez', 16),
('Evento de Prueba', 'Institucion 1', 'Patitos', 'secundaria', 'Luis', 14),
('Evento de Prueba', 'Institucion 1', 'Patitos', 'secundaria', 'Manuel', 14),
('Evento de Prueba', 'Institucion 1', 'Patitos', 'secundaria', 'Mariana', 14),
('Evento de Prueba', 'Institucion 2', 'DropDatabase', 'secundaria', 'Publicho', 14),
('Evento de Prueba', 'Institucion 2', 'DropDatabase', 'secundaria', 'Pablacho', 14),
('Evento de Prueba', 'Institucion 2', 'DropDatabase', 'secundaria', 'Pochano', 14),
('Evento de Prueba', 'Institucion 2', 'Garrafones', 'secundaria', 'Ponchinki', 14),
('Evento de Prueba', 'Institucion 2', 'Garrafones', 'secundaria', 'Pachinko', 14),
('Evento de Prueba', 'Institucion 2', 'Garrafones', 'secundaria', 'Pechanko', 14),
('Evento de Prueba', 'Institucion 3', 'Supermanes', 'bachillerato', 'Peter', 16),
('Evento de Prueba', 'Institucion 3', 'Supermanes', 'bachillerato', 'Clark', 16),
('Evento de Prueba', 'Institucion 3', 'Supermanes', 'bachillerato', 'Bruce', 16),
('Evento de Prueba', 'Institucion 3', 'Superhombres', 'bachillerato', 'Potasio', 16),
('Evento de Prueba', 'Institucion 3', 'Superhombres', 'bachillerato', 'Pancracio', 16),
('Evento de Prueba', 'Institucion 3', 'Superhombres', 'bachillerato', 'Brunacio', 16),
('Evento de Prueba', 'Institucion 4', 'Aguilas', 'bachillerato', 'Marianasa', 16),
('Evento de Prueba', 'Institucion 4', 'Aguilas', 'bachillerato', 'Maria', 16),
('Evento de Prueba', 'Institucion 4', 'Aguilas', 'bachillerato', 'Mora', 16),
('Evento de Prueba', 'Institucion 4', 'Spidermanes', 'universidad', 'Pedro', 18),
('Evento de Prueba', 'Institucion 4', 'Spidermanes', 'universidad', 'Clarencio', 18),
('Evento de Prueba', 'Institucion 4', 'Spidermanes', 'universidad', 'Bruno', 18),
('Evento de Prueba', 'Institucion 5', 'Pythons', 'universidad', 'Wanda', 18),
('Evento de Prueba', 'Institucion 5', 'Pythons', 'universidad', 'Gwen', 18),
('Evento de Prueba', 'Institucion 5', 'Pythons', 'universidad', 'Winona', 18),
('Evento de Prueba', 'Institucion 1', 'Gorriones', 'universidad', 'Pablito', 18),
('Evento de Prueba', 'Institucion 1', 'Gorriones', 'universidad', 'Marina', 18),
('Evento de Prueba', 'Institucion 1', 'Gorriones', 'universidad', 'Lupita', 18);
