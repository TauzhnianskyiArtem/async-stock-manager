--liquibase formatted sql

--changeset tauzhnianskyi:1
ALTER TABLE company ADD CONSTRAINT symbol_company UNIQUE (symbol);

--changeset tauzhnianskyi:2
ALTER TABLE stock ADD CONSTRAINT symbol_stock UNIQUE (symbol);