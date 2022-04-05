DROP DATABASE if exists Vivian;
create database Vivian default character set utf8mb4;
use Vivian;

-- -----------------------------------------------------
## Tabla Tipo
-- -----------------------------------------------------
create table if not exists Tipo (
	idTipo int not null,
    descripcion varchar(60) null
) engine = InnoDB default charset = utf8mb4;

alter table tipo add constraint pk_tipo primary key(idTipo);
alter table Tipo modify column `idTipo` int not null auto_increment;

insert into Tipo(idTipo, descripcion) values
	(1, 'Normal'),
    (2, 'Mantenimiento'),
    (3, 'Administrador')
;

-- -----------------------------------------------------
## Tabla Usuario
-- -----------------------------------------------------
create table if not exists Usuario (
	idUsuario int not null,
    dni char(8) not null, -- unique
    nombresUsuario varchar(45) not null,
    apellidosUsuario varchar(45) not null,
    email varchar(100) not null, -- unique
    clave blob not null,
    telefono char(9) not null,
    fechaRegistro date not null,
    idTipo int default 1 not null, -- tipo
    estado bit default 1 not null
) engine = InnoDB  default charset = utf8mb4 auto_increment 1;

alter table Usuario add constraint pk_usuario primary key(idUsuario);
alter table Usuario modify column `idUsuario` int auto_increment;
alter table Usuario add constraint uq_dni unique(dni);
alter table Usuario add constraint uq_email unique(email);

-- fk_tipo
alter table Usuario add constraint fk_Tipo
foreign key(idTipo) references Tipo(idTipo)
on delete cascade on update cascade;

insert into Usuario(idUsuario, dni, nombresUsuario, apellidosUsuario, email, clave, telefono, fechaRegistro, idTipo, estado) values
	(1, 12312312, 'Donatto', 'Minaya', 'ottanod22@gmail.com', aes_encrypt('donatto22', 'rumble'), 913242570, '2021-04-01', default,default),
    (2, 93766295, 'Gerson', 'Murguia', 'gerson@gmail.com', aes_encrypt('murguiaGerson', 'rumble'), 926537582, '2021-04-02', default,default),
	(3, 12345678, 'Miriam', 'Lorem', 'miriam123@gmail.com', aes_encrypt('miriamL12345', 'rumble'), 987654321, '2021-05-10', default,default),
    (4, 23456789, 'Lidia', 'Ramirez', 'lidia_ram@gmail.com', aes_encrypt('lidia25', 'rumble'), 912345678, '2021-05-13', default,default),
    (5, 96817492, 'Juan', 'Escobar', 'e_juan@gmail.com', aes_encrypt('JuanEscobar', 'rumble'), 927811323, '2021-04-10', default,default)
;

-- -----------------------------------------------------
## Tabla Turno
-- -----------------------------------------------------
create table if not exists usuario_spring (
	idUsuario int not null,
    dni char(8) not null, -- unique
    nombresUsuario varchar(45) not null,
    apellidosUsuario varchar(45) not null,
    username varchar(100) not null, -- unique
    password varchar(255) not null,
    telefono char(9) not null,
    fechaRegistro datetime default now(),
    idTipo int default 3, -- tipo
    estado int default 1,
    flgresetpassword int
) engine = InnoDB  default charset = utf8mb4 auto_increment 1;

alter table usuario_spring add constraint pk_usuario_spring primary key(idUsuario);
alter table usuario_spring modify column `idUsuario` int auto_increment;
alter table usuario_spring add constraint uq_dni_spring unique(dni);
alter table usuario_spring add constraint uq_username_spring unique(username);

-- fk_tipo
alter table usuario_spring add constraint fk_Tipo_spring
foreign key(idTipo) references Tipo(idTipo)
on delete cascade on update cascade;

insert into usuario_spring(idUsuario, dni, nombresUsuario, apellidosUsuario, username, password, telefono, fechaRegistro, idTipo, estado, flgresetpassword) values
	(1, 12312312, 'Donatto', 'Minaya', 'ottanod22@gmail.com', '$2a$10$vOuBScNDn3XXyBMQO30FZO50Rg/UPuoetvd.XW1iQ47nub9is7dpG', 913242570, '2021-04-01 10:40:00', 3,default,1);


-- -----------------------------------------------------
## Tabla Turno
-- -----------------------------------------------------
create table if not exists Turno (
	idTurno varchar(30) not null,
    descripcion varchar(35) not null,
    horario varchar(35) not null
) engine = InnoDB default charset = utf8mb4;

alter table Turno add constraint pk_turno primary key(idTurno);
alter table Turno add constraint uq_descripcion unique(descripcion);

insert into Turno (idTurno, descripcion, horario) values
	(1,'Mañana', '07:30am - 11:30am'),
    (2,'Tarde', '12:00pm - 6:30pm'),
    (3,'Noche', '7:00pm - 11:30pm')
;

-- -----------------------------------------------------
## Tabla Mesa
-- -----------------------------------------------------
create table if not exists Mesa (
	nMesa int not null,
    piso char(1) not null,
    capacidadPersonas int not null,
    estado int
) engine = InnoDB default charset = utf8mb4;

alter table Mesa add constraint pk_mesa primary key(nMesa);
alter table Mesa modify column `nMesa` int not null auto_increment;

insert into Mesa(nMesa, piso, capacidadPersonas, estado) values
	(1, 1, 5, 1),
    (2, 1, 5, 1),
    (3, 1, 4, 1),
	(4, 1, 2, 1),
    (5, 1, 6, 1),
    (6, 2, 2, 1),
    (7, 2, 5, 1),
    (8, 2, 3, 1),
    (9, 2, 3, 1)
;

-- -----------------------------------------------------
## Tabla Categoria
-- -----------------------------------------------------
create table if not exists Categoria (
  idCategoria int not null,
  descripcionCategoria varchar(45) not null,
  estado bit default 1 not null
) engine = InnoDB default charset = utf8mb4;

alter table categoria add constraint pk_categoria primary key(idCategoria);
alter table Categoria modify column `idCategoria` int not null auto_increment;

insert into Categoria(idCategoria, descripcionCategoria, estado) values 
	(1, 'Especial',1),
    (2, 'Bebida',1),
    (3, 'Postre',1),
	(4, 'Ensalada',1),
    (5, 'Bocadillo',1),
    (6, 'Sandwich',1),
    (7, 'Normal',1)
;

-- -----------------------------------------------------
## Tabla Reserva
-- -----------------------------------------------------
create table if not exists Reserva (
	idReserva int not null,
    idUsuario int not null, -- Usuario
    nMesa int not null, -- Mesa
    piso char(1) not null, -- Mesa
    fechaReservacion date not null,
    idTurno varchar(20) not null -- Turno
) engine = InnoDB default charset = utf8mb4;

alter table Reserva add constraint pk_reserva primary key(idReserva);
alter table Reserva modify column `idReserva` int not null auto_increment;

-- fk_usuario
alter table Reserva add constraint fk_usuario
foreign key(idUsuario) references Usuario(idUsuario)
on delete cascade on update cascade;

-- fk_mesa
alter table Reserva add constraint fk_mesa 
foreign key(nMesa) references Mesa(nMesa)
on delete cascade on update cascade;

-- fk_turno
alter table Reserva add constraint fk_turno 
foreign key(idTurno) references Turno(idTurno)
on delete cascade on update cascade;

insert into Reserva(idReserva, idUsuario, nMesa, piso, fechaReservacion, idTurno) values
	(1, 3, 5, 1, '2021-06-10', 2),
    (2, 3, 3, 1, '2021-06-15', 3),
    (3, 5, 6, 2, '2022-01-01', 1),
    (4, 4, 1, 1, '2021-12-25', 3)
;

-- -----------------------------------------------------
## Tabla Pago
-- -----------------------------------------------------
CREATE TABLE `pago` (
  `idPago` int NOT NULL AUTO_INCREMENT,
  `monto` varchar(40) NOT NULL,
  PRIMARY KEY (`idPago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
## Tabla Moneda
-- -----------------------------------------------------
create table if not exists Moneda (
	moneda varchar(30) not null,
    descripcion varchar(50) not null
)engine = InnoDB default charset = utf8mb4;

alter table Moneda add constraint pk_moneda primary key(moneda);

insert into Moneda(moneda, descripcion) values
	('Euro', 'Euro -> Moneda Global'),
	('Dolar', 'Dolar -> Moneda Global'),
    ('Soles', 'Soles -> Moneda Peruana'),
    ('Yen', 'Yen -> Moneda Japonesa')
;

-- -----------------------------------------------------
## Tabla Producto
-- -----------------------------------------------------
create table if not exists Producto (
	idProducto int not null,
    moneda varchar(30), -- Moneda
    nombreProducto varchar(40) not null, 
    precio double not null,
    idCategoria int not null, -- categoria
    stock int null,
    reparto int not null,
    estado int
) engine = InnoDB default charset = utf8mb4;

-- pk_producto
alter table Producto add constraint pk_producto primary key(idProducto);
alter table Producto modify column idProducto int not null auto_increment;

-- fk_categoria
alter table Producto add constraint fk_categoria 
foreign key(idCategoria) references Categoria(idCategoria)
on delete cascade on update cascade;

-- fk_moneda
alter table Producto add constraint fk_moneda 
foreign key(moneda) references Moneda(moneda)
on delete cascade on update cascade;

insert into Producto(idProducto, moneda, nombreProducto, precio, idCategoria, stock, reparto, estado) values 
/*
	(1, 'Especial')
    (2, 'Bebida')
    (3, 'Postre')
	(4, 'Ensalada')
    (5, 'Bocadillo')
    (7, 'Sandwich')
    (8, 'Normal')
*/
	(1, 'Euro' ,'Serendipity Sundae', 25000, 1, 2, 1, 1),
    (2, 'Soles', 'Pollo a la Brasa', 62, 1, 13, 4, 1),
    (3, 'Euro', 'Zillion Dollar Frittata', 1000, 1, 5, 2, 1),
    (4, 'Yen', 'Ramen', 655, 7, 10, 1, 1),
    (5, 'Soles', 'Ceviche', 9, 7, 20, 5, 1),
    (6, 'Euro', 'Sandía negra Denzuke', 5600, 1, 1, 1, 1),
    (7, 'Soles', 'Ensalada Popeye', 20, 4, 20, 2, 1),
    (8, 'Soles', 'Hamburguesa Royal Chesse', 12, 7, 15, 1, 1),
    (9, 'Dolar', 'Cerveza 7 Vidas', 2, 2, 34, 1, 1),
    (10, 'Soles', 'Spagethi Carbonara', 45, 7, 7, 1, 1),
    (11, 'Euro', 'Cocktel 27', 3200, 1, 3, 5, 1),
    (12, 'Dolar', 'Sopa de Soja', 10, 7, 12, 1, 1),
    (13, 'Soles', 'Pollo Tandoori', 27, 7, 4, 1, 1),
    (14, 'Dolar', 'Azafrán', 2700, 1, 3, 1, 1),
    (15, 'Euro', 'Kellab de Resaca', 1200, 1, 6, 1, 1),
    (16, 'Soles', 'Langosgta al Ajillo', 129, 1, 3, 1, 1),
    (17, 'Soles', 'Enchilada', 15, 7, 13, 1, 1),
    (18, 'Dolar', 'Whisky Macallan 750ml', 230, 2, 7, 5, 1)
;

-- -----------------------------------------------------
## Tabla pedido
-- -----------------------------------------------------
create table `pedido` (
  `idPedido` int not null auto_increment,
  `idUsuario` int not null,
  `idPago` int not null,
  `fechaCompra` date not null,
  `monto` decimal(10,0) not null,
  `estado` varchar(45) default 'En proceso',
  primary key (`idPedido`),
  key `fk_usuario2` (`idUsuario`),
  key `fk_pedido_pago_idx` (`idPago`),
  constraint `fk_pedido_pago` foreign key (`idPago`) references `pago` (`idPago`),
  constraint `fk_usuario2` foreign key (`idUsuario`) references `usuario` (`idUsuario`) on delete cascade on update cascade
) engine = InnoDB default charset = utf8mb4 collate = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
## Tabla detalles_pedido
-- -----------------------------------------------------
create table `detalles_pedido` (
  `idDetalle` int not null auto_increment,
  `idProducto` int not null,
  `idPedido` int not null,
  `cantidad` int not null,
  `precio` decimal(10,0) not null,
  `precioTotal` decimal(10,0) not null,
  primary key (`idDetalle`),
  key `fk_pedido` (`idPedido`),
  key `fk_producto` (`idProducto`),
  constraint `fk_pedido` foreign key (`idPedido`) references `pedido` (`idPedido`) on delete cascade on update cascade,
  constraint `fk_producto` foreign key (`idProducto`) references `producto` (`idProducto`) on delete cascade on update cascade
) engine = InnoDB default charset = utf8mb4;