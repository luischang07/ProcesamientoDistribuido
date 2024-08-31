create database Empresa
use empresa 
go
create table TICKETSH(
FOLIO INT NOT NULL,
FECHA DATE NOT NULL,
IDESTADO INT NOT NULL,
IDCIUDAD INT NOT NULL,
IDTIENDA INT NOT NULL,
IDEMPLEADO INT NOT NULL
)
GO
create table TICKETSD(
TICKET VARCHAR(8) NOT NULL,
IDPRODUCTO INT NOT NULL,
UNIDADES INT NOT NULL,
PRECIO INT NOT NULL
)
GO

alter table ticketsh add primary key(FOLIO)
alter table TICKETSD ADD PRIMARY KEY(TICKET, IDPRODUCTO)

select * from TICKETSD