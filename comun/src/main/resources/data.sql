-- data.sql Modificado

-- Administradores
INSERT INTO administrador (id, usuario, clave, num_accesos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a201', '-', '')), 'admin1@gmail.com', 'admin123', 0);
INSERT INTO administrador (id, usuario, clave, num_accesos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a202', '-', '')), 'admin2@gmail.com', 'admin123', 0);

-- Usuarios sin claves cifradas (Estos parecen ser de prueba y no estar asociados directamente a los empleados de abajo, los mantengo como estaban pero añadiendo los campos a 0)
INSERT INTO usuario_empleado (id, usuario, clave, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a203', '-', '')), 'emp1@gmail.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a204', '-', '')), 'emp2@gmail.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', 0, 0);

-- CONCEPTO
INSERT INTO concepto (id, nombre, tipo)
VALUES (UNHEX(REPLACE('00000000-00000000-00000000-00000000', '-', '')), 'Salario base', 'INGRESO'),
       (UNHEX(REPLACE('00000000-00000000-00000000-00000001', '-', '')), 'Horas extras', 'INGRESO'),
       (UNHEX(REPLACE('00000000-00000000-00000000-00000002', '-', '')), 'Bonus productividad', 'INGRESO'),
       (UNHEX(REPLACE('00000000-00000000-00000000-00000003', '-', '')), 'IRPF', 'DEDUCCION'),
       (UNHEX(REPLACE('00000000-00000000-00000000-00000004', '-', '')), 'Seguridad Social', 'DEDUCCION'),
       (UNHEX(REPLACE('00000000-00000000-00000000-00000005', '-', '')), 'Descuento transporte', 'DEDUCCION');

-- Géneros
INSERT INTO genero (id, genero)
VALUES (1, 'Masculino');
INSERT INTO genero (id, genero)
VALUES (2, 'Femenino');
INSERT INTO genero (id, genero)
VALUES (3, 'Otro');

-- Países
INSERT INTO pais (id, pais, prefijo, codigo)
VALUES (1, 'España', '+34', 'ES');
INSERT INTO pais (id, pais, prefijo, codigo)
VALUES (2, 'Francia', '+33', 'FR');
INSERT INTO pais (id, pais, prefijo, codigo)
VALUES (3, 'Alemania', '+49', 'DE');
INSERT INTO pais (id, pais, prefijo, codigo)
VALUES (4, 'Italia', '+39', 'IT');
INSERT INTO pais (id, pais, prefijo, codigo)
VALUES (5, 'Rumanía', '+40', 'RO');

-- Tipos de vía
INSERT INTO tipo_via (id, tipo_via)
VALUES (1, 'Calle');
INSERT INTO tipo_via (id, tipo_via)
VALUES (2, 'Avenida');
INSERT INTO tipo_via (id, tipo_via)
VALUES (3, 'Paseo');
INSERT INTO tipo_via (id, tipo_via)
VALUES (4, 'Carretera');
INSERT INTO tipo_via (id, tipo_via)
VALUES (5, 'Camino');
INSERT INTO tipo_via (id, tipo_via)
VALUES (6, 'Plaza');
INSERT INTO tipo_via (id, tipo_via)
VALUES (7, 'Ronda');
INSERT INTO tipo_via (id, tipo_via)
VALUES (8, 'Travesía');
INSERT INTO tipo_via (id, tipo_via)
VALUES (9, 'Glorieta');
INSERT INTO tipo_via (id, tipo_via)
VALUES (10, 'Vía');

--Tipo de documento
INSERT INTO tipo_documento (id, documento)
VALUES (1, 'DNI');
INSERT INTO tipo_documento (id, documento)
VALUES (2, 'NIE');
INSERT INTO tipo_documento (id, documento)
VALUES (3, 'Pasaporte');

-- Departamentos
INSERT INTO departamento (id, nombre, codigo, localidad)
VALUES (UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')), 'Recursos Humanos', 'RRHH', 'Madrid');
INSERT INTO departamento (id, nombre, codigo, localidad)
VALUES (UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')), 'Tecnología', 'TEC', 'Barcelona');
INSERT INTO departamento (id, nombre, codigo, localidad)
VALUES (UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')), 'Ventas', 'VNT', 'Valencia');
INSERT INTO departamento (id, nombre, codigo, localidad)
VALUES (UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')), 'Marketing', 'MKT', 'Sevilla');
INSERT INTO departamento (id, nombre, codigo, localidad)
VALUES (UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')), 'Finanzas', 'FIN', 'Bilbao');

-- Especialidades
INSERT INTO especialidad (id, codigo, nombre)
VALUES (UNHEX(REPLACE('a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d', '-', '')), '1', 'Programación Java');
INSERT INTO especialidad (id, codigo, nombre)
VALUES (UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')), '2', 'Diseño UX/UI');
INSERT INTO especialidad (id, codigo, nombre)
VALUES (UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')), '3', 'Marketing Digital');
INSERT INTO especialidad (id, codigo, nombre)
VALUES (UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')), '4', 'Gestión de Proyectos');
INSERT INTO especialidad (id, codigo, nombre)
VALUES (UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')), '5', 'Administración de Bases de Datos');

-- Entidades Bancarias
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), '0182',
        'Banco Bilbao Vizcaya Argentaria (BBVA)', 1);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), '0049', 'Banco Santander', 1);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), '2100', 'CaixaBank', 1);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), '0081', 'Banco Sabadell', 1);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), '30004', 'BNP Paribas', 2);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', '')), '0019', 'Deutsche Bank', 3);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('44444444-4444-4444-4444-444444444444', '-', '')), '02008', 'UniCredit', 4);
INSERT INTO entidad_bancaria (id, codigo, nombre, id_pais)
VALUES (UNHEX(REPLACE('55555555-5555-5555-5555-555555555555', '-', '')), '03002', 'Banca Transilvania', 5);

-- Tipos de Tarjetas de Crédito
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (1, 'Visa Classic');
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (2, 'Mastercard Gold');
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (3, 'American Express');
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (4, 'Visa Platinum');

-- USUARIOS EMPLEADOS (El ID ahora es el mismo que el del empleado asociado)
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 'glauco.perello@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2025-04-16 10:12:45', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 'moises.villalba@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-09-17 05:25:30', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 'ester.somoza@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-08-30 09:34:51', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 'samu.ortuno@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-12-28 03:16:37', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 'manu.roma@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-07-17 05:57:15', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 'pia.lago@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-07-21 09:20:18', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 'rosalia.cabanas@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-05-22 06:02:09', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 'maribel.malo@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2025-03-14 15:30:20', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 'marisol.valenciano@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-05-04 04:38:05', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 'vicente.palma@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2025-02-04 03:22:58', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 'paz.mur@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2025-01-27 07:05:40', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 'alejandro.cerro@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-07-18 15:42:41', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 'aaron.pintor@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-06-04 09:11:57', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 'wilfredo.cozar@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-12-02 18:40:01', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 'mercedes.gisbert@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-06-13 06:13:35', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 'veronica.blanca@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-05-14 07:41:15', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 'lilia.rivas@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2025-01-16 17:20:08', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 'pastora.cabezas@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-05-11 17:11:30', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 'mirta.molina@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-11-27 09:00:46', true, 0, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos, num_accesos)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 'raul.requena@empresa.com',
        '$2a$12$vSD6k7P81SrWI6Ivim0SaOkZjEPY3CEX12wwAxY49nGw3YMdQ/ba2', '2024-05-07 18:19:46', true, 0, 0);


-- EMPLEADOS (id_usuario ahora referencia el ID del empleado)
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 'Glauco', 'Perelló', 'DNI', '36468518V',
        '1972-05-26', 53, 'España', 3, 'Paseo', 'Acceso de Isa Reguera', '789', '5', '5', 'C', 'Cáceres', '14680',
        'Málaga', 'Network engineer', NULL, '2017-05-18', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')),
        UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 'Moisés', 'Villalba', 'DNI', '28684212S',
        '1996-11-27', 29, 'España', 2, 'Paseo', 'Plaza Sandalio Cabrero', '48', '5', '1', 'C', 'Sevilla', '90441',
        'Santa Cruz de Tenerife', 'Production assistant, radio', NULL, '2015-11-02', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 'Ester', 'Somoza', 'DNI', '14999538L',
        '1984-06-15', 41, 'España', 2, 'Paseo', 'Via de Julián Peña', '41', '2', '1', 'B', 'Burgos', '74207', 'Córdoba',
        'Therapist, nutritional', NULL, '2023-11-07', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 'Samu', 'Ortuño', 'DNI', '97693573S',
        '1991-01-03', 34, 'España', 3, 'Avenida', 'Paseo de Ismael Beltrán', '760', '5', '1', 'A', 'Albacete', '47085',
        'Álava', 'Customer service manager', NULL, '2015-12-25', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 'Manu', 'Roma', 'DNI', '43522188R',
        '1986-09-18', 39, 'España', 3, 'Avenida', 'Camino de Alex Córdoba', '77', '2', '4', 'B', 'Navarra', '58257',
        'Soria', 'Learning mentor', NULL, '2020-01-19', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 'Pía', 'Lago', 'DNI', '39996972H',
        '1999-06-19', 26, 'España', 2, 'Calle', 'Acceso Emiliano Pi', '71', '1', '2', 'B', 'Baleares', '76565', 'Álava',
        'Nurse, adult', NULL, '2023-07-07', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')),
        UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 'Rosalía', 'Cabañas', 'DNI', '10344806G',
        '1996-10-07', 29, 'España', 2, 'Paseo', 'C. de Sosimo Hoyos', '61', '1', '6', 'C', 'Baleares', '22339', 'Lugo',
        'Therapist, drama', NULL, '2020-11-04', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 'Maribel', 'Malo', 'DNI', '54261804C',
        '1994-09-25', 31, 'España', 3, 'Calle', 'Alameda de Odalis Godoy', '3', '4', '4', 'B', 'Baleares', '91371',
        'Sevilla', 'Art gallery manager', NULL, '2018-09-27', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 'Marisol', 'Valenciano', 'DNI', '81039731J',
        '1979-11-17', 46, 'España', 3, 'Avenida', 'Vial Fernando Torralba', '68', '5', '5', 'C', 'Baleares', '78632',
        'Girona', 'Insurance account manager', NULL, '2025-04-09', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 'Vicente', 'Palma', 'DNI', '19568005L',
        '1974-10-29', 51, 'España', 1, 'Avenida', 'Cañada de Susanita Cabrera', '5', '5', '4', 'C', 'Salamanca',
        '16402', 'Ávila', 'Chief Executive Officer', NULL, '2017-09-26', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')),
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 'Paz', 'Mur', 'DNI', '76701504T', '1981-12-22',
        44, 'España', 1, 'Paseo', 'Glorieta Cruz Zaragoza', '2', '5', '6', 'A', 'Palencia', '59607', 'Jaén',
        'Engineer, electrical', NULL, '2022-11-17', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 'Alejandro', 'Cerro', 'DNI', '94643633J',
        '1999-06-07', 26, 'España', 3, 'Avenida', 'C. de Emilia Palomares', '109', '1', '6', 'B', 'Palencia', '69962',
        'Málaga', 'Optometrist', NULL, '2024-06-04', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 'Aarón', 'Pintor', 'DNI', '93827315B',
        '1979-06-24', 46, 'España', 3, 'Calle', 'Urbanización Pía Ramos', '29', '1', '6', 'B', 'Cáceres', '30354',
        'Albacete', 'Health promotion specialist', NULL, '2022-12-10', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 'Wilfredo', 'Cózar', 'DNI', '82329040B',
        '1964-10-11', 61, 'España', 3, 'Calle', 'Paseo Olivia Tormo', '570', '1', '1', 'B', 'Cantabria', '47900',
        'Zamora', 'Occupational therapist', NULL, '2022-05-27', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 'Mercedes', 'Gisbert', 'DNI', '67419594Q',
        '1990-08-05', 35, 'España', 2, 'Avenida', 'Urbanización de Sergio Ruiz', '42', '2', '6', 'C', 'León', '73369',
        'Córdoba', 'Illustrator', NULL, '2015-09-29', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 'Verónica', 'Blanca', 'DNI', '80597763J',
        '1983-03-13', 42, 'España', 3, 'Avenida', 'Rambla de Hermenegildo Donaire', '8', '4', '4', 'B', 'Tarragona',
        '55227', 'La Coruña', 'Oncologist', NULL, '2015-07-15', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')),
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 'Lilia', 'Rivas', 'DNI', '17004331V',
        '1977-05-29', 48, 'España', 3, 'Calle', 'Callejón Ángela Llano', '79', '3', '1', 'A', 'Alicante', '64345',
        'Vizcaya', 'Land/geomatics surveyor', NULL, '2020-10-08', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 'Pastora', 'Cabezas', 'DNI', '48612915T',
        '1985-02-13', 40, 'España', 2, 'Paseo', 'C. Lázaro Calvet', '211', '5', '2', 'C', 'Jaén', '89104', 'Burgos',
        'Veterinary surgeon', NULL, '2022-01-18', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 'Mirta', 'Molina', 'DNI', '46424793Y',
        '1982-07-15', 43, 'España', 1, 'Paseo', 'Alameda Susana Alcalá', '46', '4', '6', 'A', 'Lleida', '52803',
        'Badajoz', 'Multimedia programmer', NULL, '2019-06-10', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id, tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 'Raúl', 'Requena', 'DNI', '85898590F',
        '1997-03-29', 28, 'España', 3, 'Avenida', 'Via Gonzalo Melero', '44', '3', '4', 'B', 'Álava', '54811', 'Burgos',
        'Waste management officer', NULL, '2023-11-17', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')),
        UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')));

-- Información Económica
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 87019.82, 14660.3, 'ES8664246703052344422926',
        UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), 4, '372596233604526', '12', '2029', '335');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 94898.97, 10275.35, 'ES3745781255629297379479',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 2, '375857670110535', '03', '2028', '152');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 111793.84, 17799.03,
        'ES2675791761561383354933', UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 3,
        '4774415655693948685', '12', '2027', '825');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 77066.17, 19869.27, 'ES8091072352244665376399',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 2, '4699988983989782469', '09', '2025', '439');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 118332.43, 14270.23,
        'ES5938690295472018916532', UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 4, '4325987117410',
        '05', '2029', '758');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 119798.7, 17122.35, 'ES0993423471739576659875',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 4, '5107158973908285', '02', '2025', '826');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 77621.49, 6017.23, 'ES6361277010102780882315',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 3, '3546570312135780', '06', '2030', '131');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 66787.19, 4747.48, 'ES5575752961307291267561',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 1, '30574110188172', '04', '2026', '709');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 63339.56, 18509.99, 'ES0734627176473207288353',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 4, '4759059056486369', '03', '2029', '446');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 99182.52, 18378.43, 'ES6596896828624237011631',
        UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), 2, '503876466528', '09', '2025', '300');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 95020.1, 3310.83, 'ES5311679884191806165610',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 2, '2227322937001981', '02', '2025', '422');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 100743.43, 11325.09,
        'ES9015553119672875027719', UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 1,
        '30350229174845', '06', '2026', '930');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 48136.0, 15126.94, 'ES0724353030347230794723',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 1, '4967466016318891891', '12', '2028', '288');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 107250.04, 22836.05,
        'ES7979888574370079186391', UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 2,
        '4569310867385607251', '03', '2030', '497');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 111582.39, 9999.16, 'ES7583903548414104812442',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 3, '3545626170408551', '04', '2026', '808');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 39531.76, 13685.05, 'ES3208036225068058672431',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 2, '30352499944816', '09', '2026', '856');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 71296.67, 16873.78, 'ES4415481759216068926570',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 2, '30175791205242', '08', '2026', '706');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 50107.19, 22721.68, 'ES1673846676222302953361',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 3, '639046270687', '08', '2025', '183');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 53227.01, 17164.15, 'ES5345310524898726147957',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 2, '501803192548', '05', '2027', '608');
INSERT INTO informacion_economica (id_empleado, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero_tarjeta, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 81981.97, 2447.05, 'ES3183510830919004418423',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 2, '3532211105253250', '02', '2029', '420');
-- Relaciones empleado-especialidad
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')),
        UNHEX(REPLACE('a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')),
        UNHEX(REPLACE('a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')),
        UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')),
        UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')),
        UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')));
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')),
        UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));

--NOMINA

INSERT INTO nomina (id_nomina, id_empleado, mes, anio, total_liquido)
VALUES (UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')),
        UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 10, 2025, 1100);

INSERT INTO nomina (id_nomina, id_empleado, mes, anio, total_liquido)
VALUES (UNHEX(REPLACE('a1b2c3d4-e5f6-789a-bcde-0123456789ab', '-', '')),
        UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 12, 2025, 2500.00),
       (UNHEX(REPLACE('b2c3d4e5-f678-9abc-de01-23456789abcd', '-', '')),
        UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 2, 2025, 2600.00),
       (UNHEX(REPLACE('c3d4e5f6-789a-bcde-0123-456789abcdef', '-', '')),
        UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 1, 2025, 2400.00),

       (UNHEX(REPLACE('d4e5f678-9abc-de01-2345-6789abcdef01', '-', '')),
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 3, 2024, 2300.00),
       (UNHEX(REPLACE('e5f6789a-bcde-0123-4567-89abcdef0123', '-', '')),
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 9, 2024, 2350.00),
       (UNHEX(REPLACE('f6789abc-de01-2345-6789-abcdef012345', '-', '')),
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 1, 2024, 2400.00),

       (UNHEX(REPLACE('a789bcde-0123-4567-89ab-cdef01234567', '-', '')),
        UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 3, 2024, 2550.00),
       (UNHEX(REPLACE('b89cdef0-1234-5678-9abc-def012345678', '-', '')),
        UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 5, 2024, 2600.00),
       (UNHEX(REPLACE('c9def012-3456-789a-bcde-f0123456789a', '-', '')),
        UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 1, 2024, 2450.00),

       (UNHEX(REPLACE('d0123456-789a-bcde-f012-3456789abcde', '-', '')),
        UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 6, 2024, 2750.00),
       (UNHEX(REPLACE('e1234567-89ab-cdef-0123-456789abcdef', '-', '')),
        UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 2, 2024, 2800.00),
       (UNHEX(REPLACE('f2345678-9abc-def0-1234-56789abcdef0', '-', '')),
        UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 1, 2024, 2650.00),

       (UNHEX(REPLACE('a3456789-bcde-f012-3456-789abcdef012', '-', '')),
        UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 7, 2024, 2450.00),
       (UNHEX(REPLACE('b456789a-cdef-0123-4567-89abcdef0123', '-', '')),
        UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 2, 2024, 2500.00),
       (UNHEX(REPLACE('c56789ab-def0-1234-5678-9abcdef01234', '-', '')),
        UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 1, 2024, 2350.00);

INSERT INTO nomina (id_nomina, id_empleado, mes, anio, total_liquido)
VALUES (UNHEX(REPLACE('d6789abc-ef01-2345-6789-abcdef012345', '-', '')),
        UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 3, 2024, 2650.00),
       (UNHEX(REPLACE('e789abcd-f012-3456-789a-bcdef0123456', '-', '')),
        UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 8, 2024, 2700.00),
       (UNHEX(REPLACE('f890bcde-1234-5678-9abc-def012345678', '-', '')),
        UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 1, 2024, 2600.00),

       (UNHEX(REPLACE('a901cdef-2345-6789-abcd-ef0123456789', '-', '')),
        UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 3, 2024, 2550.00),
       (UNHEX(REPLACE('b012def0-3456-789a-bcde-f0123456789a', '-', '')),
        UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 2, 2024, 2600.00),
       (UNHEX(REPLACE('c123ef01-4567-89ab-cdef-0123456789ab', '-', '')),
        UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 4, 2024, 2450.00),

       (UNHEX(REPLACE('d234f012-5678-9abc-def0-123456789abc', '-', '')),
        UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 6, 2025, 2750.00),
       (UNHEX(REPLACE('e3450123-6789-abcd-ef01-23456789abcd', '-', '')),
        UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 2, 2025, 2800.00),
       (UNHEX(REPLACE('f4561234-789a-bcde-f012-3456789abcde', '-', '')),
        UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 1, 2025, 2650.00),

       (UNHEX(REPLACE('a5672345-89ab-cdef-0123-456789abcdef', '-', '')),
        UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 5, 2024, 2450.00),
       (UNHEX(REPLACE('b6783456-9abc-def0-1234-56789abcdef0', '-', '')),
        UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 6, 2024, 2500.00),
       (UNHEX(REPLACE('c7894567-abcd-ef01-2345-6789abcdef01', '-', '')),
        UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 7, 2024, 2350.00),

       (UNHEX(REPLACE('d8905678-bcde-f012-3456-789abcdef012', '-', '')),
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 9, 2025, 2650.00),
       (UNHEX(REPLACE('e9016789-cdef-0123-4567-89abcdef0123', '-', '')),
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 2, 2025, 2700.00),
       (UNHEX(REPLACE('f012789a-def0-1234-5678-9abcdef01234', '-', '')),
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 1, 2025, 2600.00),

       (UNHEX(REPLACE('a123890b-ef01-2345-6789-abcdef012345', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 6, 2024, 2750.00),
       (UNHEX(REPLACE('b234901c-f012-3456-789a-bcdef0123456', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 2, 2024, 2800.00),
       (UNHEX(REPLACE('c345012d-1234-5678-9abc-def012345678', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 1, 2024, 2650.00);

INSERT INTO nomina (id_nomina, id_empleado, mes, anio, total_liquido)
VALUES (UNHEX(REPLACE('d9012345-6789-abcd-ef01-23456789abcd', '-', '')),
        UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 5, 2025, 2700.00),
       (UNHEX(REPLACE('e0123456-789a-bcde-f012-3456789abcde', '-', '')),
        UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 2, 2025, 2750.00),
       (UNHEX(REPLACE('f1234567-89ab-cdef-0123-456789abcdef', '-', '')),
        UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 1, 2025, 2600.00),

       (UNHEX(REPLACE('a2345678-9abc-def0-1234-56789abcdef0', '-', '')),
        UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 6, 2024, 2500.00),
       (UNHEX(REPLACE('b3456789-abcd-ef01-2345-6789abcdef01', '-', '')),
        UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 2, 2024, 2550.00),
       (UNHEX(REPLACE('c456789a-bcde-f012-3456-789abcdef012', '-', '')),
        UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 1, 2024, 2450.00),

       (UNHEX(REPLACE('d56789ab-cdef-0123-4567-89abcdef0123', '-', '')),
        UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 10, 2024, 2750.00),
       (UNHEX(REPLACE('e6789abc-def0-1234-5678-9abcdef01234', '-', '')),
        UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 2, 2024, 2800.00),
       (UNHEX(REPLACE('f789abcd-ef01-2345-6789-abcdef012345', '-', '')),
        UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 1, 2024, 2650.00),

       (UNHEX(REPLACE('a890bcde-0123-4567-89ab-cdef01234567', '-', '')),
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 3, 2024, 2500.00),
       (UNHEX(REPLACE('b901cdef-1234-5678-9abc-def012345678', '-', '')),
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 2, 2024, 2550.00),
       (UNHEX(REPLACE('c012def0-3456-789a-bcde-f0123456789a', '-', '')),
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 1, 2024, 2450.00),

       (UNHEX(REPLACE('d123ef01-4567-89ab-cdef-0123456789ab', '-', '')),
        UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 3, 2024, 2600.00),
       (UNHEX(REPLACE('e234f012-5678-9abc-def0-123456789abc', '-', '')),
        UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 2, 2024, 2650.00),
       (UNHEX(REPLACE('f3450123-6789-abcd-ef01-23456789abcd', '-', '')),
        UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 10, 2024, 2550.00);

INSERT INTO nomina (id_nomina, id_empleado, mes, anio, total_liquido)
VALUES (UNHEX(REPLACE('a456789b-cdef-0123-4567-89abcdef0123', '-', '')),
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 3, 2024, 2750.00),
       (UNHEX(REPLACE('b56789cd-ef01-2345-6789-abcdef012345', '-', '')),
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 2, 2024, 2800.00),
       (UNHEX(REPLACE('c6789def-0123-4567-89ab-cdef01234567', '-', '')),
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 11, 2024, 2650.00),

       (UNHEX(REPLACE('d789abc0-1234-5678-9abc-def012345678', '-', '')),
        UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 6, 2025, 2500.00),
       (UNHEX(REPLACE('e890bcd1-2345-6789-abcd-ef0123456789', '-', '')),
        UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 2, 2025, 2550.00),
       (UNHEX(REPLACE('f901cde2-3456-789a-bcde-f0123456789a', '-', '')),
        UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 1, 2025, 2450.00),

       (UNHEX(REPLACE('a0123456-789a-bcde-f012-3456789abcde', '-', '')),
        UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 5, 2024, 2600.00),
       (UNHEX(REPLACE('b1234567-89ab-cdef-0123-456789abcdef', '-', '')),
        UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 2, 2024, 2650.00),
       (UNHEX(REPLACE('c2345678-9abc-def0-1234-56789abcdef0', '-', '')),
        UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 1, 2024, 2550.00),

       (UNHEX(REPLACE('d3456789-abcd-ef01-2345-6789abcdef01', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 7, 2024, 2750.00),
       (UNHEX(REPLACE('e456789a-bcde-f012-3456-789abcdef012', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 2, 2024, 2800.00),
       (UNHEX(REPLACE('f56789ab-cdef-0123-4567-89abcdef0123', '-', '')),
        UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 1, 2024, 2650.00),

       (UNHEX(REPLACE('a6789abc-def0-1234-5678-9abcdef01234', '-', '')),
        UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 9, 2024, 2500.00),
       (UNHEX(REPLACE('b789abcd-ef01-2345-6789-abcdef012345', '-', '')),
        UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 2, 2024, 2550.00),
       (UNHEX(REPLACE('c890bcde-0123-4567-89ab-cdef01234567', '-', '')),
        UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 1, 2024, 2450.00);


--LINEA DE NOMINA

-- INSERT INTO LINEA_NOMINA (id, id_concepto, id_nomina, cantidad)
-- VALUES (UNHEX(REPLACE('00000000-0000-0000-0000-000000000000', '-', '')),
--         UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a105', '-', '')),
--         UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')), 150.00),
--        (UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
--         UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a106', '-', '')),
--         UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')), 25.00),
--        (UNHEX(REPLACE('00000000-0000-0000-0000-000000000002', '-', '')),
--         UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a102', '-', '')),
--         UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')), 50.00),
--        (UNHEX(REPLACE('00000000-0000-0000-0000-000000000003', '-', '')),
--         UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a104', '-', '')),
--         UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')), 500.00),
--        (UNHEX(REPLACE('00000000-0000-0000-0000-000000000004', '-', '')),
--         UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a103', '-', '')),
--         UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')), 60.00),
--        (UNHEX(REPLACE('00000000-0000-0000-0000-000000000005', '-', '')),
--         UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a101', '-', '')),
--         UNHEX(REPLACE('eb661f36-f938-4388-9039-c89e262e52fd', '-', '')), 1500.00);

-- INSERT INTO LINEA_NOMINA (id, id_concepto, id_nomina, cantidad) VALUES
--     (UNHEX(REPLACE('11111111-00000000-00000000-00000000', '-', '')),
--      UNHEX(REPLACE('00000000-00000000-00000000-00000000', '-', '')),
--      UNHEX(REPLACE('a1b2c3d4e5f6789a-bcde-0123456789abcd', '-', '')),
--      2800.00);
INSERT INTO LINEA_NOMINA (id, id_concepto, id_nomina, cantidad)
VALUES (UNHEX(REPLACE('a1f2b3c4-5678-9abc-def0-123456789abc', '-', '')),
        UNHEX(REPLACE('00000000-00000000-00000000-00000000', '-', '')),
        UNHEX(REPLACE('a1b2c3d4-e5f6-789a-bcde-0123456789ab', '-', '')), 2800.00),
       (UNHEX(REPLACE('b2f3c4d5-6789-abcd-ef01-23456789abcd', '-', '')),
        UNHEX(REPLACE('00000000-00000000-00000000-00000002', '-', '')),
        UNHEX(REPLACE('a1b2c3d4-e5f6-789a-bcde-0123456789ab', '-', '')), 300.00),
       (UNHEX(REPLACE('c3f4d5e6-789a-bcde-0123-456789abcdef', '-', '')),
        UNHEX(REPLACE('00000000-00000000-00000000-00000003', '-', '')),
        UNHEX(REPLACE('a1b2c3d4-e5f6-789a-bcde-0123456789ab', '-', '')), 400.00),
       (UNHEX(REPLACE('d4f5e678-9abc-de01-2345-6789abcdef01', '-', '')),
        UNHEX(REPLACE('00000000-00000000-00000000-00000004', '-', '')),
        UNHEX(REPLACE('a1b2c3d4-e5f6-789a-bcde-0123456789ab', '-', '')), 200.00);

--        (UNHEX(REPLACE('e5f6g789-abcd-ef01-2345-6789abcdef01', '-', '')),
--         UNHEX(REPLACE('00000000-00000000-00000000-00000000', '-', '')),
--         UNHEX(REPLACE('b2c3d4e5-f678-9abc-de01-23456789abcd', '-', '')), 3000.00)
--         ,
--        (UNHEX(REPLACE('f6g789a0-1234-5678-9abc-def012345678', '-', '')),
--         UNHEX(REPLACE('00000000-00000000-00000000-00000001', '-', '')),
--         UNHEX(REPLACE('b2c3d4e5-f678-9abc-de01-23456789abcd', '-', '')), 200.00),
--        (UNHEX(REPLACE('a7g890b1-2345-6789-abcd-ef0123456789', '-', '')),
--         UNHEX(REPLACE('00000000-00000000-00000000-00000003', '-', '')),
--         UNHEX(REPLACE('b2c3d4e5-f678-9abc-de01-23456789abcd', '-', '')), 500.00),
--        (UNHEX(REPLACE('b8g901c2-3456-789a-bcde-f0123456789a', '-', '')),
--         UNHEX(REPLACE('00000000-00000000-00000000-00000004', '-', '')),
--         UNHEX(REPLACE('b2c3d4e5-f678-9abc-de01-23456789abcd', '-', '')), 250.00),
--        (UNHEX(REPLACE('c9g012d3-4567-89ab-cdef-0123456789ab', '-', '')),
--         UNHEX(REPLACE('00000000-00000000-00000000-00000005', '-', '')),
--         UNHEX(REPLACE('b2c3d4e5-f678-9abc-de01-23456789abcd', '-', '')), 150.00);



-- Establecimiento de la estructura jerárquica de jefes (No requiere cambios en la lógica, ya que usa los IDs de empleado)
-- Establecer jefes de departamento que reportan al CEO (Vicente Palma)
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', ''))
WHERE id IN (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')),
             UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')),
             UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')),
             UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')),
             UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')));
-- Empleados de RRHH reportando a Moisés Villalba
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', ''))
  AND id != UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', ''));
-- Empleados de Tecnología reportando a Verónica Blanca
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', ''))
  AND id != UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')) AND id != UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', ''));
-- Empleados de Ventas reportando a Marisol Valenciano
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', ''))
  AND id != UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', ''));
-- Empleados de Marketing reportando a Wilfredo Cózar
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', ''))
  AND id != UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', ''));
-- Empleados de Finanzas reportando a Glauco Perelló
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', ''))
  AND id != UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', ''));
-- Creación de mandos intermedios
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', ''))
WHERE id IN (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')),
             UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')));
-- Paz Mur como responsable del equipo de ingenieros eléctricos
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', ''))
WHERE id = UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', ''));

-- Etiquetas creadas por Vicente Palma (CEO)
INSERT INTO etiqueta (id, nombre, creador_id, fecha_creacion)
VALUES (UNHEX(REPLACE('e1e1e1e1-1111-4a4a-aaaa-111111111111', '-', '')), 'Alto Potencial',
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), NOW()),
       (UNHEX(REPLACE('e1e1e1e1-2222-4a4a-aaaa-222222222222', '-', '')), 'Necesita Seguimiento',
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), NOW()),
       (UNHEX(REPLACE('e1e1e1e1-3333-4a4a-aaaa-333333333333', '-', '')), 'Liderazgo',
        UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), NOW());

-- Etiquetas creadas por Moisés Villalba (RRHH)
INSERT INTO etiqueta (id, nombre, creador_id, fecha_creacion)
VALUES (UNHEX(REPLACE('e2e2e2e2-1111-4b4b-bbbb-111111111111', '-', '')), 'Comunicación Excelente',
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), NOW()),
       (UNHEX(REPLACE('e2e2e2e2-2222-4b4b-bbbb-222222222222', '-', '')), 'Trabajo en Equipo',
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), NOW()),
       (UNHEX(REPLACE('e2e2e2e2-3333-4b4b-bbbb-333333333333', '-', '')), 'Resolutivo',
        UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), NOW());

-- Etiquetas creadas por Glauco Perelló (Finanzas)
INSERT INTO etiqueta (id, nombre, creador_id, fecha_creacion)
VALUES (UNHEX(REPLACE('e3e3e3e3-1111-4c4c-cccc-111111111111', '-', '')), 'Meticuloso/a',
        UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), NOW()),
       (UNHEX(REPLACE('e3e3e3e3-2222-4c4c-cccc-222222222222', '-', '')), 'Analítico',
        UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), NOW());

-- Etiquetas creadas por Verónica Blanca (Tecnología)
INSERT INTO etiqueta (id, nombre, creador_id, fecha_creacion)
VALUES (UNHEX(REPLACE('e4e4e4e4-1111-4d4d-dddd-111111111111', '-', '')), 'Innovador/a',
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), NOW()),
       (UNHEX(REPLACE('e4e4e4e4-2222-4d4d-dddd-222222222222', '-', '')), 'Experto Técnico',
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), NOW()),
       (UNHEX(REPLACE('e4e4e4e4-3333-4d4d-dddd-333333333333', '-', '')), 'Proactivo',
        UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), NOW());

-- Etiquetas creadas por Marisol Valenciano (Ventas)
INSERT INTO etiqueta (id, nombre, creador_id, fecha_creacion)
VALUES (UNHEX(REPLACE('e5e5e5e5-1111-4e4e-eeee-111111111111', '-', '')), 'Orientado a Resultados',
        UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), NOW()),
       (UNHEX(REPLACE('e5e5e5e5-2222-4e4e-eeee-222222222222', '-', '')), 'Habilidades de Negociación',
        UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), NOW());

-- Etiquetas creadas por Wilfredo Cózar (Marketing)
INSERT INTO etiqueta (id, nombre, creador_id, fecha_creacion)
VALUES (UNHEX(REPLACE('e6e6e6e6-1111-4f4f-ffff-111111111111', '-', '')), 'Creativo/a',
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), NOW()),
       (UNHEX(REPLACE('e6e6e6e6-2222-4f4f-ffff-222222222222', '-', '')), 'Visión Estratégica',
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), NOW()),
       (UNHEX(REPLACE('e6e6e6e6-3333-4f4f-ffff-333333333333', '-', '')), 'Adaptable',
        UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), NOW());


-- Insertar categorías (IDs autoincrementales)
INSERT INTO categoria (nombre)
VALUES ('Libros'),
       ('Electrónicos'),
       ('Ropa');

-- Obtener IDs de categorías recién insertadas
SET
@libro_cat_id = (SELECT id FROM categoria WHERE nombre = 'Libros');
SET
@electronico_cat_id = (SELECT id FROM categoria WHERE nombre = 'Electrónicos');
SET
@ropa_cat_id = (SELECT id FROM categoria WHERE nombre = 'Ropa');

-- Libro 1
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid,
        'Matar un ruiseñor',
        18.50,
        'Clásico sobre justicia y prejuicio racial',
        @libro_cat_id,
        'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid,
        'Harper Lee',
        'Vintage Español',
        384);
-- Libro 2
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid,
        '1984',
        19.95,
        'Novela distópica clásica',
        @libro_cat_id,
        'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid,
        'George Orwell',
        'Secker & Warburg',
        328);

-- Electrónico 1
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid,
        'Smartphone X',
        799.99,
        'Teléfono flagship 2025',
        @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid,
        'TechCorp',
        'X-2025',
        '2 años');

-- Electrónico 2
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid,
        'Tablet Pro',
        349.99,
        'Tablet profesional 12 pulgadas',
        @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid,
        'DigitalWorks',
        'T-Pro-12',
        '3 años');

-- Ropa 1
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid,
        'Camisa Oxford',
        59.95,
        'Camisa formal 100% algodón',
        @ropa_cat_id,
        'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid,
        'M',
        'Azul marino',
        'Algodón');

-- Ropa 2
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid,
        'Jeans Slim Fit',
        89.99,
        'Vaqueros ajustados',
        @ropa_cat_id,
        'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid,
        '32',
        'Azul oscuro',
        'Denim');
-- Libro 3
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'El Hobbit', 18.50, 'Precuela de El Señor de los Anillos', @libro_cat_id, 'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid, 'J.R.R. Tolkien', 'Minotauro', 310);

-- Libro 4
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Matar un ruiseñor', 17.99, 'Clásico sobre justicia y racismo', @libro_cat_id, 'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid, 'Harper Lee', 'HarperCollins', 281);

-- Libro 5
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Donde viven los monstruos', 14.50, 'Libro infantil ilustrado', @libro_cat_id, 'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid, 'Maurice Sendak', 'Kalandraka', 48);

-- Libro 6
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Fahrenheit 451', 16.75, 'Distopía sobre la quema de libros', @libro_cat_id, 'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid, 'Ray Bradbury', 'Debolsillo', 158);

-- Libro 7
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Orgullo y Prejuicio', 15.00, 'Clásico de la literatura inglesa', @libro_cat_id, 'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid, 'Jane Austen', 'Penguin Clásicos', 480);

-- === Electrónicos Adicionales ===

-- Electrónico 3
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Auriculares NoiseFree', 129.99, 'Cancelación activa de ruido premium', @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid, 'SoundWave', 'NF-3000', '1 año');

-- Electrónico 4
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Monitor Curvo 34"', 450.00, 'Monitor ultrapanorámico para gaming', @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid, 'ViewMax', 'VM-C34WQ', '3 años');

-- Electrónico 5
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Teclado Mecánico RGB', 85.50, 'Teclado gaming retroiluminado', @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid, 'ClickKeys', 'MK-RGB-Pro', '2 años');

-- Electrónico 6
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Webcam ProStream', 65.00, 'Cámara web Full HD para streaming', @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid, 'CamLink', 'StreamerX', '1 año');

-- Electrónico 7
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Disco Duro Externo 2TB', 75.99, 'Almacenamiento portátil USB 3.0', @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid, 'DataStore', 'PortableHD-2T', '2 años');

-- === Ropa Adicional ===

-- Ropa 3
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Polo Clásico Piqué', 34.99, 'Polo de algodón piqué', @ropa_cat_id, 'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid, 'L', 'Blanco', 'Algodón Piqué');

-- Ropa 4
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Bermudas Cargo', 45.00, 'Pantalón corto con bolsillos laterales', @ropa_cat_id, 'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid, 'M', 'Beige', 'Algodón Ripstop');

-- Ropa 5
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Vestido de Lino', 69.90, 'Vestido fresco de verano', @ropa_cat_id, 'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid, 'S', 'Verde Oliva', 'Lino');

-- Ropa 6
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Chaqueta Bomber', 79.99, 'Chaqueta ligera estilo bomber', @ropa_cat_id, 'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid, 'XL', 'Negro', 'Nylon');

-- Ropa 7
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Jersey Cuello Pico Lana', 95.00, 'Jersey fino de lana merino', @ropa_cat_id, 'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid, 'M', 'Gris Jaspeado', 'Lana Merino');

-- === Mixto Adicional ===

-- Libro 8
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Crónica de una muerte anunciada', 14.00, 'Novela corta sobre un asesinato inevitable',
        @libro_cat_id, 'LIBRO');
INSERT INTO libro (id, autor, editorial, num_paginas)
VALUES (@producto_uuid, 'Gabriel García Márquez', 'Bruguera', 128);

-- Electrónico 8
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Ratón Inalámbrico Ergo', 35.50, 'Ratón ergonómico para oficina', @electronico_cat_id,
        'ELECTRONICO');
INSERT INTO electronico (id, marca, modelo, garantia)
VALUES (@producto_uuid, 'ClickWell', 'ErgoMouse-W', '18 meses');

-- Ropa 8
SET
@producto_uuid = UNHEX(REPLACE(UUID(), '-', ''));
INSERT INTO producto (id, nombre, precio, descripcion, id_categoria, tipo_producto)
VALUES (@producto_uuid, 'Calcetines Deportivos (Pack 3)', 12.95, 'Pack de 3 pares de calcetines técnicos', @ropa_cat_id,
        'ROPA');
INSERT INTO ropa (id, talla, color, material)
VALUES (@producto_uuid, '42-45', 'Multicolor', 'Poliéster/Elastano');