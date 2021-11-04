insert into guest(id, name) values(null, 'Roger Federer');
insert into guest(id, name) values(null, 'Rafael Nadal');
insert into guest(id, name) values(null, 'Marcelo Giatti');


insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Chatrier');
insert into tennis_court(id, name) values(null, 'My private Court');

insert into schedule(id, start_date_time, end_date_time, tennis_court_id) values(null, '2021-11-05T20:00:00.0', '2021-11-05T21:00:00.0', 1);

insert into schedule(id, start_date_time, end_date_time, tennis_court_id) values(null, '2021-11-05T10:00:00.0', '2021-11-05T11:00:00.0', 2);

insert into schedule(id, start_date_time, end_date_time, tennis_court_id) values(null, '2021-11-04T20:00:00.0', '2021-11-04T21:00:00.0', 2);

