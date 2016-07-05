create database open_mtas;

select open_mtase;

create table subscriber(
public_id varchar(255) not null,
display_name varchar(255),
ip varchar(20),
port int,
registred boolean,
last_update date,
type int not null,
short_number varchar(45),
group_id int,
enabled boolean,
call_type int,
primary key (public_id)
);

create table conference(
public_id varchar(255) not null,
activated boolean,
max_number_of_parties int,
primary key (public_id),
foreign key (public_id) references subscriber(public_id) ON DELETE CASCADE ON UPDATE CASCADE
);



create table subscriber_configuration(
id int not null auto_increment,
name varchar(45),
`condition` varchar(45),
forward_to varchar(255),
notify_caller boolean,
subscriber varchar(255) not null,
primary key(id),
foreign key (subscriber) references subscriber(public_id) ON DELETE CASCADE  ON UPDATE CASCADE
);


create table abbrev_dialing_configuration(
id int not null auto_increment,
short_number char(2),
target  varchar(255),
subscriber varchar(255) not null,
primary key(id),
foreign key (subscriber) references subscriber(public_id) ON DELETE CASCADE  ON UPDATE CASCADE
);


create table _group(
id int not null auto_increment,
name varchar(255),
primary key(id)
);

create table vn_destination
(
virtual_number varchar(45),
destination varchar(45),
_order int,
foreign key (virtual_number) references subscriber(public_id) ON DELETE CASCADE  ON UPDATE CASCADE,
foreign key (destination) references subscriber(public_id) ON DELETE CASCADE  ON UPDATE CASCADE,
unique(virtual_number,destination)
);