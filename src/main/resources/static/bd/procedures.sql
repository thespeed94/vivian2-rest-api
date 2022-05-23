-- -----------------------------------------------------
-- ⚠ No ejecutar sin tener la base de datos!! ⚠
-- -----------------------------------------------------
use Vivian;

-- -----------------------------------------------------
## Procedimientos de Reservaciones
-- -----------------------------------------------------
Delimiter ||
drop procedure if exists Vivian.listarReservas ||
create procedure listarReservas(codigo int)
	begin
		select * from Reserva  where idUsuario = codigo;
    end ||

Delimiter ||
drop procedure if exists Vivian.mostrarReservas ||
create procedure mostrarReservas(thisUsuario int)
begin
	select r.idReserva, r.idUsuario, r.nMesa, r.piso, r.fechaReservacion, r.idTurno, t.descripcion as descripcion_turno from Reserva r
	join turno t on t.idTurno = r.idTurno
	where idUsuario = thisUsuario;
end ||


Delimiter ||
drop procedure if exists Vivian.verificarMesas ||
create procedure verificarMesas(thisNombreCompleto varchar(50))
	begin
		declare codigoUsuario int;
        set codigoUsuario = (select idUsuario from Usuario where concat(nombresUsuario, ' ', apellidosUsuario) = thisNombreCompleto);
        
        call mostrarReservas(codigoUsuario);
	end ||
    
Delimiter ||
drop procedure if exists Vivian.AgregarReservacion ||
create procedure AgregarReservacion(thisCliente int, thisNumeroMesa int, thisFecha varchar(20), thisTurno varchar(20))
	begin		
        declare piso2 int;
               
        set piso2 = (select piso from Mesa where nMesa = thisNumeroMesa group by piso);
        
		update Mesa set estado = 1 where nMesa = thisNumeroMesa;
        
		insert into Reserva(idReserva, idUsuario, nMesa, piso, fechaReservacion, idTurno) 
        values (null, thisCliente, thisNumeroMesa, piso2, thisFecha, thisTurno);
        
    end ||


-- Procedimientos del formulario / Verificar por casillero
# solo mesa
Delimiter ||
drop procedure if exists Vivian.modReserva_M ||
create procedure modReserva_M(thisReserva int, thisNumeroMesa int)
	begin
		declare piso2 int;
        set piso2 = (select piso from Mesa where nMesa = thisNumeroMesa group by piso);
        
        update Reserva set
			nMesa = thisNumeroMesa,
            piso = piso2
		where idReserva = thisReserva;
    end ||

# solo fecha
Delimiter ||
drop procedure if exists Vivian.modReserva_F ||
create procedure modReserva_F(thisReserva int, thisFecha varchar(20))
	begin        
        update Reserva set
			fechaReservacion = thisFecha
		where idReserva = thisReserva;
    end ||
    
# solo turno
Delimiter ||
drop procedure if exists Vivian.modReserva_T ||
create procedure modReserva_T(thisReserva int, thisTurno varchar(20))
	begin        
        update Reserva set
			idTurno = thisTurno
		where idReserva = thisReserva;
    end ||
    
# mesa y fecha
Delimiter ||
drop procedure if exists Vivian.modReserva_MF ||
create procedure modReserva_MF(thisReserva int, thisNumeroMesa int, thisFecha varchar(20))
	begin
		declare piso2 int;
        set piso2 = (select piso from Mesa where nMesa = thisNumeroMesa group by piso);
        
        update Reserva set
			nMesa = thisNumeroMesa,
            piso = piso2,
            fechaReservacion = thisFecha
		where idReserva = thisReserva;
    end ||

# fecha y turno
Delimiter ||
drop procedure if exists Vivian.modReserva_FT ||
create procedure modReserva_FT(thisReserva int, thisFecha varchar(20), thisTurno varchar(20))
	begin
		update Reserva set
			fechaReservacion = thisFecha,
            idTurno = thisturno
		where idReserva = thisReserva;
    end ||
    
# mesa y turno
Delimiter ||
drop procedure if exists Vivian.modReserva_MT ||
create procedure modReserva_MT(thisReserva int, thisNumeroMesa int, thisTurno varchar(20))
	begin
		declare piso2 int;
        set piso2 = (select piso from Mesa where nMesa = thisNumeroMesa group by piso);
        
        update Reserva set
			nMesa = thisNumeroMesa,
            piso = piso2,
            idTurno = thisTurno
		where idReserva = thisReserva;
    end ||

# Todos los casilleros marcados
Delimiter ||
drop procedure if exists Vivian.modificarReserva ||
create procedure modificarReserva(thisReserva int, thisNumeroMesa int, thisFecha varchar(20), thisTurno varchar(20))
	begin
		declare piso2 int;
        set piso2 = (select piso from Mesa where nMesa = thisNumeroMesa group by piso);
        
        update Reserva set
			nMesa = thisNumeroMesa,
            piso = piso2,
            fechaReservacion = thisFecha,
            idTurno = thisTurno
		where idReserva = thisReserva;
    end ||
    
Delimiter ||
drop procedure if exists Vivian.delReserva ||
create procedure delReserva(cod int)
	begin
		update mesa set estado = 0 where nmesa = (select nmesa from reserva where idReserva = cod);
		delete from Reserva where idReserva = cod;        
    end ||
-- -----------------------------------------------------
## Procedimientos de Usuario
-- -----------------------------------------------------
Delimiter %
drop procedure if exists Vivian.vUsuario %
create procedure vUsuario (thisEmail varchar(100), thisClave varchar(20))
begin
	select idUsuario, dni, nombresUsuario, apellidosUsuario, email, aes_decrypt(clave, 'rumble') clave, telefono, fechaRegistro, idTipo, estado 
    from Usuario where email = thisEmail and clave = aes_encrypt(thisClave, 'rumble');
end %

Delimiter %
drop procedure if exists Vivian.AgregarUsuario %
create procedure AgregarUsuario
	(thisDni char(8), thisNombres varchar(45), thisApellidos varchar(45), thisCorreo varchar(100), 
    thisClave varchar(30), thisTelefono char(9), thisIdTipo int, thisEstado int, out valido int)
begin
	insert into Usuario(idUsuario, dni, nombresUsuario, apellidosUsuario, email, clave, telefono, fechaRegistro, idTipo) values
    (null, thisDni, thisNombres, thisApellidos, thisCorreo, aes_encrypt(thisClave, 'rumble'), thisTelefono, curdate(), if(isnull(thisIdTipo), 1, thisIdTipo));
    set valido = 1;
    select valido;
end %

-- -----------------------------------------------------
Delimiter %
drop procedure if exists Vivian.ModificarUsuario %
create procedure ModificarUsuario
	(thisCodigo int, thisDni char(8), thisNombres varchar(45), thisApellidos varchar(45), 
	thisCorreo varchar(100), thisClave varchar(30), thisTelefono char(9))
begin
	update Usuario set
    dni = thisDni,
    nombresUsuario = thisNombres,
    apellidosUsuario = thisApellidos,
    email = thisCorreo,
    clave = aes_encrypt(thisClave, 'rumble'),
    telefono = thisTelefono
    where idUsuario = thisCodigo
    ;
end %

-- -----------------------------------------------------
Delimiter %
drop procedure if exists Vivian.EliminarUsuario %
create procedure EliminarUsuario(thisCodigo int)
begin
	delete from Usuario where idUsuario = thisCodigo;
end %

-- -----------------------------------------------------
## Procedimientos de Pedidos
-- -----------------------------------------------------
Delimiter !!
drop procedure if exists Vivian.AgregarPedido !!
create procedure AgregarPedido(idPedido int, thisIdUsuario int, thisIdPago int)
begin
	insert into Pedido(idPedido, idUsuario, idPago) values
    (thisIdPedido, thisIdUsuario, thisIdPago);
end !!

Delimiter !!
drop procedure if exists Vivian.EliminarPedido !!
create procedure EliminarPedido(thisCodigo int)
begin
	delete from Pedido where idPedido = thisCodigo;
end !!

-- -----------------------------------------------------
## Procedimientos de Productos
-- -----------------------------------------------------
Delimiter ||
drop procedure if exists Vivian.AgregarProducto ||
create procedure AgregarProducto(thisMoneda varchar(30), thisProducto varchar(40), thisPrecio double, thisCategoria int, thisStock int, thisReparto int)
begin
	insert into Producto(idProducto, moneda, nombreProducto, precio, idCategoria, stock, reparto) values
    (null, thisMoneda, thisProducto, thisPrecio, thisCategoria, thisStock, thisReparto);
end ||

Delimiter ||
drop procedure if exists Vivian.EliminarProducto ||
create procedure EliminarProducto(thisCodigo int)
begin
	delete from Producto where idProducto = thisCodigo;
end ||
-- -----------------------------------------------------
## Procedimientos de Boleta
-- -----------------------------------------------------
Delimiter /
drop procedure if exists Vivian.GenerarBoleta /
create procedure GenerarBoleta
	(thisIdPedido int, thisIdUsuario int, thisApellidos varchar(45), 
    thisIdProducto int, thisProducto varchar(45), thisIdPago int, thisCantidad int, thisMonto varchar(45))
begin
	insert into `Detalles Pedido`(idPedido, idUsuario, apellidosUsuario, idProducto, nombreProducto, idPago, cantidadTotal, monto) values
    (thisIdPedido, thisIdUsuario, thisApellidos, thisIdProducto, thisProducto, thisIdPago, thisCantidad, thisMonto);
end /

Delimiter /
drop procedure if exists Vivian.EliminarBoleta /
create procedure EliminarBoleta(thisCodigo int)
begin
	delete from `Detalles Pedido` where idPedido = thisCodigo;
end /

Delimiter /
drop procedure if exists Vivian.ListarUsuarios /
create procedure ListarUsuarios()
begin
	select idUsuario, dni, nombresUsuario, apellidosUsuario, email, clave, telefono, fechaRegistro, idTipo, estado from usuario;
end /

Delimiter %
drop procedure if exists Vivian.vUsuarioSpring %
create procedure vUsuarioSpring (thisEmail varchar(100), thisClave varchar(255))
begin
	select idUsuario, dni, nombresUsuario, apellidosUsuario, email, clave, telefono, fechaRegistro, idTipo, activo 
    from Usuario_spring where email = thisEmail and clave = thisClave;
end %

Delimiter %
drop procedure if exists Vivian.ModificarUsuarioSpring %
create procedure ModificarUsuarioSpring (thisId int, thisDni char(8), thisNombresUsuario varchar(45), thisApellidosUsuario varchar(45), thisUsername varchar(100), thisTelefono char(9), thisEstado int, out valido int)
begin
	update vivian.usuario_spring set dni = thisDni, nombresUsuario = thisNombresUsuario, apellidosUsuario = thisApellidosUsuario, 
    username = thisUsername, telefono = thisTelefono, estado = thisEstado
    where idUsuario = thisId;
    set valido = 1;
    select valido;
end %

Delimiter %
drop procedure if exists Vivian.ModificarUsuario %
create procedure ModificarUsuario (thisId int, thisDni char(8), thisNombresUsuario varchar(45), thisApellidosUsuario varchar(45), thisEmail varchar(100), thisTelefono char(9), thisEstado int, out valido int)
begin
	update vivian.usuario set dni = thisDni, nombresUsuario = thisNombresUsuario, apellidosUsuario = thisApellidosUsuario, 
    email = thisEmail, telefono = thisTelefono, estado = thisEstado
    where idUsuario = thisId;
    set valido = 1;
    select valido;
end %

Delimiter %
drop procedure if exists Vivian.ModificarResetPassword %
create procedure ModificarResetPassword (thisId int, thisFlg int, out valido int)
begin
	update vivian.usuario_spring set flgresetpassword = thisFlg 
    where idUsuario = thisId;
    set valido = 1;
    select valido;
end %

Delimiter %
drop procedure if exists Vivian.ModificarPasswordOlvidada %
create procedure ModificarPasswordOlvidada (thisId int, thisPass varchar(255), out valido int)
begin
	update vivian.usuario_spring set password = thisPass 
    where idUsuario = thisId;
    set valido = 1;
    select valido;
end %
-- -----------------------------------------------------
## Procedimientos de Reserva
-- -----------------------------------------------------

Delimiter ||
drop procedure if exists Vivian.listarMesasActivas ||
create procedure listarMesasActivas()
	begin
		select * from Mesa  where estado = 1;
    end ||
    
Delimiter ||
drop procedure if exists Vivian.ExisteCruceReserva ||
create procedure ExisteCruceReserva(inFechaReservacion date, inTurno varchar(20), inNMesa int, out existe boolean)
	begin
		DECLARE contador int;
		SET contador = (SELECT COUNT(idReserva) FROM vivian.reserva WHERE nMesa = inNMesa AND fechaReservacion=inFechaReservacion AND idTurno=inTurno);
        IF contador>0 THEN
				SET existe= true;
				SELECT 1;
        ELSE
				SET existe= false;
				SELECT 0;
		END IF;
    end ||


-- -----------------------------------------------------
## Procedimientos de Categoria
-- -----------------------------------------------------

Delimiter %
drop procedure if exists Vivian.ModificarCategoria %
create procedure ModificarCategoria
	(thisId integer,descripcion varchar(45), out valido int)
begin
	update vivian.categoria
		set descripcionCategoria = descripcion
    where idCategoria= thisId;
    set valido = 1;
    select valido;
end %


Delimiter %
drop procedure if exists Vivian.ModificarCategoria %
create procedure ModificarCategoria
	(thisId integer,descripcion varchar(45), out valido int)
begin
	update vivian.categoria
		set descripcionCategoria = descripcion
    where idCategoria= thisId;
    set valido = 1;
    select valido;
end %


Delimiter /
drop procedure if exists Vivian.EliminarCategoria /
create procedure EliminarCategoria(thisCodigo int, out valido int)
begin
	delete from  vivian.categoria
    where idCategoria = thisCodigo;
    set valido=1;
    select valido;
end /


-- -----------------------------------------------------
## Procedimientos de Turno
-- -----------------------------------------------------

Delimiter /
drop procedure if exists Vivian.AgregarTurno /
create procedure AgregarTurno(thisCodigo varchar(30),descripcion varchar(35), horario varchar(35),out valido int)
begin
	INSERT INTO Turno (idTurno,descripcion,horario) VALUES (thisCodigo,descripcion,horario);
    SET valido = 1;
    SELECT valido;
END /

Delimiter /
drop procedure if exists Vivian.ObtenerUltimoIdTurno /
create procedure ObtenerUltimoIdTurno(out id varchar(30))
begin
    SET id = (SELECT  idTurno+1 FROM Turno order by idTurno DESC LIMIT 1);
    SELECT id;
END /


Delimiter /
drop procedure if exists Vivian.EliminarTurnoPorId /
create procedure EliminarTurnoPorId(id int, out valido int)
begin
    DELETE FROM turno WHERE idTurno=id;
    set valido =1;
    SELECT valido;
END /



-- -----------------------------------------------------
## Procedimientos para Pedido
-- -----------------------------------------------------
Delimiter /
drop procedure if exists Vivian.AgregarPagoSpring /
create procedure AgregarPagoSpring(thisMonto varchar(45),out lastId int)
begin
	INSERT INTO Pago(monto) VALUES (thisMonto);
    SET lastId = last_insert_id();
    SELECT lastId;
END /


Delimiter /
drop procedure if exists Vivian.VerDetallePedido /
create procedure VerDetallePedido(thisId int)
begin
	select *  from detalles_pedido where idPedido = thisId;
END; /



-- -----------------------------------------------------
## Procedimientos para Reportes Productos
-- -----------------------------------------------------
Delimiter /
drop procedure if exists Vivian.ReporteCantidadProductosVendidosPorMeses /
create procedure ReporteCantidadProductosVendidosPorMeses()
begin
	select sum(dp.cantidad)
	from Pedido pe join detalles_pedido dp on pe.idPedido = dp.idPedido
	join Producto pro on pro.idProducto = dp.idProducto
	group by month(pe.fechaCompra)
	order by month(pe.fechaCompra);
END; /

Delimiter /
drop procedure if exists Vivian.ReporteTopProductosIngresos /
create procedure ReporteTopProductosIngresos()
begin
	select pro.nombreProducto, sum(dp.precioTotal)
from Pedido pe join detalles_pedido dp on pe.idPedido = dp.idPedido
join Producto pro on pro.idProducto = dp.idProducto
group by pro.idProducto
order by sum(dp.precioTotal) desc
limit 5;
END; /

Delimiter /
drop procedure if exists Vivian.ReporteTopProductosVendidosPorMes /
create procedure ReporteTopProductosVendidosPorMes(thisMes int)
begin
	select pro.nombreProducto, sum(dp.cantidad)
	from Pedido pe join detalles_pedido dp on pe.idPedido = dp.idPedido
	join Producto pro on pro.idProducto = dp.idProducto
	where month(pe.fechaCompra) = thisMes
	group by pro.idProducto
	order by sum(dp.cantidad) desc;
END; /


Delimiter /
drop procedure if exists Vivian.ReporteTopCategoriasVendidasPorMes /
create procedure ReporteTopCategoriasVendidasPorMes(thisMes int)
begin
	select ca.descripcionCategoria, sum(dp.cantidad)
	from Pedido pe join detalles_pedido dp on pe.idPedido = dp.idPedido
	join Producto pro on pro.idProducto = dp.idProducto
    join Categoria ca on ca.idCategoria = pro.idCategoria
	where month(pe.fechaCompra) = thisMes
	group by ca.descripcionCategoria
	order by sum(dp.cantidad) desc;
END; /


Delimiter /
drop procedure if exists Vivian.ReportesProductoGeneralSummary /
create procedure ReportesProductoGeneralSummary()
begin
	select pro.nombreProducto, pro.precio, sum(dp.precioTotal),
	(select date_format(ped.fechaCompra,'%d-%m-%Y') from pedido ped 
	join detalles_pedido dpd on ped.idPedido = dpd.idPedido 
	join producto prod on prod.idProducto = dpd.idProducto where dpd.idProducto = dp.idProducto
	order by ped.fechaCompra desc
	limit 1) as ultima_venta
	from Pedido pe join detalles_pedido dp on pe.idPedido = dp.idPedido
	join Producto pro on pro.idProducto = dp.idProducto
	group by 1
	order by sum(dp.cantidad) desc
	limit 2;
END; /
