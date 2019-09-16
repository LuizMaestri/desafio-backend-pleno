create table if not exists habitant (
  id bigint auto_increment primary key,
  code varchar(50) not null ,
  name varchar(255) not null
);

create table if not exists address (
  id bigint auto_increment primary key,
  postal_code char(8) not null,
  street varchar(255),
  neighborhood varchar(255),
  number int,
  complement varchar(255),
  city varchar(255),
  state varchar(255),
  habitant_code varchar(50) not null,
  foreign key (habitant_code) references habitant(code)
);