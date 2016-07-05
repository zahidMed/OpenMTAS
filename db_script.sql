create database open_msdp;
use open_msdp;

create table subscriber(
eui varchar(20) primary key,
uuid varchar(36),
profile ENUM('Prepaid', 'Postpaid')
);

create table subscriber_service(
id bigint primary key auto_increment,
name varchar(45),
enabled boolean,
reference varchar(45),
subscriber_eui varchar(20),
foreign key (subscriber_eui) references subscriber(eui)
);

create table charging_transaction(
id bigint primary key auto_increment,
billing_text varchar(255),
currancy char(3),
reference_code varchar(45),
client_correlator varchar(45),
on_behalf_of varchar(45),
purchase_category_code varchar(45),
channel ENUM('SMS', 'MMS','Wap','Web'),
product_id varchar(45),
subscriber_eui varchar(20) not null,
type int,
amount  DECIMAL,
tax_amount  DECIMAL,
volume bigint,
foreign key (subscriber_eui) references subscriber(eui)
);