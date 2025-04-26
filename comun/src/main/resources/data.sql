-- Administradores
INSERT INTO administrador (id, usuario, clave, num_accesos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a201', '-', '')), 'admin1@gmail.com', 'admin123', 0);
INSERT INTO administrador (id, usuario, clave, num_accesos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a202', '-', '')), 'admin2@gmail.com', 'admin123', 0);

-- Usuarios sin claves cifradas

INSERT INTO usuario_empleado (id, usuario, clave)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a203', '-', '')), 'emp1@gmail.com', 'empleado');
INSERT INTO usuario_empleado (id, usuario, clave)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a204', '-', '')), 'emp2@gmail.com', 'empleado');


-- Géneros
INSERT INTO genero (id, genero)
VALUES (1, 'Masculino');
INSERT INTO genero (id, genero)
VALUES (2, 'Femenino');
INSERT INTO genero (id, genero)
VALUES (3, 'Otro');

-- Países

INSERT INTO pais (id, pais, prefijo)
VALUES (1, 'España', '+34');
INSERT INTO pais (id, pais, prefijo)
VALUES (2, 'Francia', '+33');
INSERT INTO pais (id, pais, prefijo)
VALUES (3, 'Alemania', '+49');
INSERT INTO pais (id, pais, prefijo)
VALUES (4, 'Italia', '+39');
INSERT INTO pais (id, pais, prefijo)
VALUES (5, 'Rumanía', '+40');

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

INSERT INTO tipo_documento (id, documento) VALUES (1, 'DNI');
INSERT INTO tipo_documento (id, documento) VALUES (2, 'NIE');
INSERT INTO tipo_documento (id, documento) VALUES (3, 'Pasaporte');


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
INSERT INTO especialidad (id, nombre)
VALUES (UNHEX(REPLACE('a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d', '-', '')), 'Programación Java');
INSERT INTO especialidad (id, nombre)
VALUES (UNHEX(REPLACE('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', '-', '')), 'Diseño UX/UI');
INSERT INTO especialidad (id, nombre)
VALUES (UNHEX(REPLACE('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', '-', '')), 'Marketing Digital');
INSERT INTO especialidad (id, nombre)
VALUES (UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')), 'Gestión de Proyectos');
INSERT INTO especialidad (id, nombre)
VALUES (UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')), 'Administración de Bases de Datos');

-- Entidades Bancarias
INSERT INTO entidad_bancaria (id, codigo, nombre)
VALUES (UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 'BBVA', 'Banco Bilbao Vizcaya Argentaria');
INSERT INTO entidad_bancaria (id, codigo, nombre)
VALUES (UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), 'SANT', 'Banco Santander');
INSERT INTO entidad_bancaria (id, codigo, nombre)
VALUES (UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 'CAIXA', 'CaixaBank');
INSERT INTO entidad_bancaria (id, codigo, nombre)
VALUES (UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 'SABD', 'Banco Sabadell');

-- Tipos de Tarjetas de Crédito
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (1, 'Visa Classic');
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (2, 'Mastercard Gold');
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (3, 'American Express');
INSERT INTO tipo_tarjeta_credito (id, tipo_tarjeta)
VALUES (4, 'Visa Platinum');

-- USUARIOS EMPLEADOS (ahora van primero porque empleado depende de ellos)
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a207', '-', '')), 'glauco.perelló@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2025-04-16 10:12:45', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('95a974df-a636-46ba-b799-55f40c273b1a', '-', '')), 'moisés.villalba@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-09-17 05:25:30', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('93a19953-fcd9-4527-9025-806cf8eb8654', '-', '')), 'ester.somoza@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-08-30 09:34:51', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('29e1cc76-a129-425f-9bf1-5e3d810c10d7', '-', '')), 'samu.ortuño@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-12-28 03:16:37', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('f4b53a22-07eb-4282-bea3-07757a9f54e9', '-', '')), 'manu.roma@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-07-17 05:57:15', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('4c2dc710-fc28-411e-8078-867b1be4c677', '-', '')), 'pía.lago@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-07-21 09:20:18', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('5a435ce7-0880-4ed2-8d1a-c026347bab3f', '-', '')), 'rosalía.cabañas@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-05-22 06:02:09', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('5222da24-bc8d-4976-be89-399256d838f5', '-', '')), 'maribel.malo@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2025-03-14 15:30:20', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('bf735c4b-1a7f-4134-b2f6-27d7b6b62f35', '-', '')), 'marisol.valenciano@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-05-04 04:38:05', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('56e92970-d090-4349-b10f-b24f9c54d93a', '-', '')), 'vicente.palma@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2025-02-04 03:22:58', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('f1ca5a34-f500-46c4-815e-b5633ed05502', '-', '')), 'paz.mur@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2025-01-27 07:05:40', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('09544433-1feb-4d64-8d4d-8ba62a539a07', '-', '')), 'alejandro.cerro@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-07-18 15:42:41', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('2d7ad5bd-778f-4436-9aee-6d097783fdb2', '-', '')), 'aarón.pintor@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-06-04 09:11:57', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('7618bea4-1357-4434-8351-aa2750ef3a32', '-', '')), 'wilfredo.cózar@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-12-02 18:40:01', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('ec15ed1f-ab85-4d43-8293-36c914a09a10', '-', '')), 'mercedes.gisbert@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-06-13 06:13:35', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('a0d521f3-6d43-4754-99fc-1587fec7121d', '-', '')), 'verónica.blanca@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-05-14 07:41:15', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('c22711f9-6c6e-43cb-bf66-40b1636e5523', '-', '')), 'lilia.rivas@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2025-01-16 17:20:08', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('8385d434-a40c-4357-8eb9-8379d461bf40', '-', '')), 'pastora.cabezas@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-05-11 17:11:30', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('ef4680f5-91a7-4407-82aa-35d9d355fa70', '-', '')), 'mirta.molina@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-11-27 09:00:46', true, 0);
INSERT INTO usuario_empleado (id, usuario, clave, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('a21a03aa-d844-47df-a834-7dbebac182dc', '-', '')), 'raúl.requena@empresa.com',
        '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        '2024-05-07 18:19:46', true, 0);

-- EMPLEADOS (ahora con id_usuario en lugar de que usuario tenga id_empleado)
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 'Glauco', 'Perelló', 'DNI', '36468518V',
        '1972-05-26', 53, 'España', 3,
        'Paseo', 'Acceso de Isa Reguera', '789', '5', '5', 'C', 'Cáceres', '14680', 'Málaga',
        'Network engineer', NULL, '2017-05-18', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')),
        UNHEX(REPLACE('ea27576c-7bb1-493c-8397-0a727ec7a207', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 'Moisés', 'Villalba', 'DNI', '28684212S',
        '1996-11-27', 29, 'España', 2,
        'Paseo', 'Plaza Sandalio Cabrero', '48', '5', '1', 'C', 'Sevilla', '90441', 'Santa Cruz de Tenerife',
        'Production assistant, radio', NULL, '2015-11-02', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('95a974df-a636-46ba-b799-55f40c273b1a', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 'Ester', 'Somoza', 'DNI', '14999538L',
        '1984-06-15', 41, 'España', 2,
        'Paseo', 'Via de Julián Peña', '41', '2', '1', 'B', 'Burgos', '74207', 'Córdoba',
        'Therapist, nutritional', NULL, '2023-11-07', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('93a19953-fcd9-4527-9025-806cf8eb8654', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 'Samu', 'Ortuño', 'DNI', '97693573S',
        '1991-01-03', 34, 'España', 3,
        'Avenida', 'Paseo de Ismael Beltrán', '760', '5', '1', 'A', 'Albacete', '47085', 'Álava',
        'Customer service manager', NULL, '2015-12-25', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('29e1cc76-a129-425f-9bf1-5e3d810c10d7', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 'Manu', 'Roma', 'DNI', '43522188R',
        '1986-09-18', 39, 'España', 3,
        'Avenida', 'Camino de Alex Córdoba', '77', '2', '4', 'B', 'Navarra', '58257', 'Soria',
        'Learning mentor', NULL, '2020-01-19', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('f4b53a22-07eb-4282-bea3-07757a9f54e9', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 'Pía', 'Lago', 'DNI', '39996972H',
        '1999-06-19', 26, 'España', 2,
        'Calle', 'Acceso Emiliano Pi', '71', '1', '2', 'B', 'Baleares', '76565', 'Álava',
        'Nurse, adult', NULL, '2023-07-07', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')),
        UNHEX(REPLACE('4c2dc710-fc28-411e-8078-867b1be4c677', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 'Rosalía', 'Cabañas', 'DNI', '10344806G',
        '1996-10-07', 29, 'España', 2,
        'Paseo', 'C. de Sosimo Hoyos', '61', '1', '6', 'C', 'Baleares', '22339', 'Lugo',
        'Therapist, drama', NULL, '2020-11-04', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('5a435ce7-0880-4ed2-8d1a-c026347bab3f', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 'Maribel', 'Malo', 'DNI', '54261804C',
        '1994-09-25', 31, 'España', 3,
        'Calle', 'Alameda de Odalis Godoy', '3', '4', '4', 'B', 'Baleares', '91371', 'Sevilla',
        'Art gallery manager', NULL, '2018-09-27', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('5222da24-bc8d-4976-be89-399256d838f5', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 'Marisol', 'Valenciano', 'DNI', '81039731J',
        '1979-11-17', 46, 'España', 3,
        'Avenida', 'Vial Fernando Torralba', '68', '5', '5', 'C', 'Baleares', '78632', 'Girona',
        'Insurance account manager', NULL, '2025-04-09', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('bf735c4b-1a7f-4134-b2f6-27d7b6b62f35', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 'Vicente', 'Palma', 'DNI', '19568005L',
        '1974-10-29', 51, 'España', 1,
        'Avenida', 'Cañada de Susanita Cabrera', '5', '5', '4', 'C', 'Salamanca', '16402', 'Ávila',
        'Chief Executive Officer', NULL, '2017-09-26', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')),
        UNHEX(REPLACE('56e92970-d090-4349-b10f-b24f9c54d93a', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 'Paz', 'Mur', 'DNI', '76701504T', '1981-12-22',
        44, 'España', 1,
        'Paseo', 'Glorieta Cruz Zaragoza', '2', '5', '6', 'A', 'Palencia', '59607', 'Jaén',
        'Engineer, electrical', NULL, '2022-11-17', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('f1ca5a34-f500-46c4-815e-b5633ed05502', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 'Alejandro', 'Cerro', 'DNI', '94643633J',
        '1999-06-07', 26, 'España', 3,
        'Avenida', 'C. de Emilia Palomares', '109', '1', '6', 'B', 'Palencia', '69962', 'Málaga',
        'Optometrist', NULL, '2024-06-04', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('09544433-1feb-4d64-8d4d-8ba62a539a07', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 'Aarón', 'Pintor', 'DNI', '93827315B',
        '1979-06-24', 46, 'España', 3,
        'Calle', 'Urbanización Pía Ramos', '29', '1', '6', 'B', 'Cáceres', '30354', 'Albacete',
        'Health promotion specialist', NULL, '2022-12-10', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('2d7ad5bd-778f-4436-9aee-6d097783fdb2', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 'Wilfredo', 'Cózar', 'DNI', '82329040B',
        '1964-10-11', 61, 'España', 3,
        'Calle', 'Paseo Olivia Tormo', '570', '1', '1', 'B', 'Cantabria', '47900', 'Zamora',
        'Occupational therapist', NULL, '2022-05-27', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('7618bea4-1357-4434-8351-aa2750ef3a32', '-', '')));
INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 'Mercedes', 'Gisbert', 'DNI', '67419594Q',
        '1990-08-05', 35, 'España', 2,
        'Avenida', 'Urbanización de Sergio Ruiz', '42', '2', '6', 'C', 'León', '73369', 'Córdoba',
        'Illustrator', NULL, '2015-09-29', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')),
        UNHEX(REPLACE('ec15ed1f-ab85-4d43-8293-36c914a09a10', '-', '')));

INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 'Verónica', 'Blanca', 'DNI', '80597763J',
        '1983-03-13', 42, 'España', 3,
        'Avenida', 'Rambla de Hermenegildo Donaire', '8', '4', '4', 'B', 'Tarragona', '55227', 'La Coruña',
        'Oncologist', NULL, '2015-07-15', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')),
        UNHEX(REPLACE('a0d521f3-6d43-4754-99fc-1587fec7121d', '-', '')));

INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 'Lilia', 'Rivas', 'DNI', '17004331V',
        '1977-05-29', 48, 'España', 3,
        'Calle', 'Callejón Ángela Llano', '79', '3', '1', 'A', 'Alicante', '64345', 'Vizcaya',
        'Land/geomatics surveyor', NULL, '2020-10-08', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('c22711f9-6c6e-43cb-bf66-40b1636e5523', '-', '')));

INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 'Pastora', 'Cabezas', 'DNI', '48612915T',
        '1985-02-13', 40, 'España', 2,
        'Paseo', 'C. Lázaro Calvet', '211', '5', '2', 'C', 'Jaén', '89104', 'Burgos',
        'Veterinary surgeon', NULL, '2022-01-18', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d481', '-', '')),
        UNHEX(REPLACE('8385d434-a40c-4357-8eb9-8379d461bf40', '-', '')));

INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 'Mirta', 'Molina', 'DNI', '46424793Y',
        '1982-07-15', 43, 'España', 1,
        'Paseo', 'Alameda Susana Alcalá', '46', '4', '6', 'A', 'Lleida', '52803', 'Badajoz',
        'Multimedia programmer', NULL, '2019-06-10', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d482', '-', '')),
        UNHEX(REPLACE('ef4680f5-91a7-4407-82aa-35d9d355fa70', '-', '')));

INSERT INTO empleado (id, nombre, apellido, tipo_documento, num_documento, fecha_nacimiento, edad, pais_nacimiento,
                      genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento, id_usuario)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 'Raúl', 'Requena', 'DNI', '85898590F',
        '1997-03-29', 28, 'España', 3,
        'Avenida', 'Via Gonzalo Melero', '44', '3', '4', 'B', 'Álava', '54811', 'Burgos',
        'Waste management officer', NULL, '2023-11-17', NULL, true,
        UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d483', '-', '')),
        UNHEX(REPLACE('a21a03aa-d844-47df-a834-7dbebac182dc', '-', '')));

-- Información Económica
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')), 87019.82, 14660.3, 'ES8664246703052344422926',
        UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), 4,
        '372596233604526', '12', '2029', '335');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), 94898.97, 10275.35, 'ES3745781255629297379479',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 2,
        '375857670110535', '03', '2028', '152');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('4280629d-9787-4ec6-bad3-6b83db92f958', '-', '')), 111793.84, 17799.03,
        'ES2675791761561383354933',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 3,
        '4774415655693948685', '12', '2027', '825');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a288206c-1e7d-4f20-a9c3-46a0b215b6cf', '-', '')), 77066.17, 19869.27, 'ES8091072352244665376399',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 2,
        '4699988983989782469', '09', '2025', '439');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('d316af5b-7c1c-4f1a-a6af-09ec51c192f1', '-', '')), 118332.43, 14270.23,
        'ES5938690295472018916532',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 4,
        '4325987117410', '05', '2029', '758');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a571ab15-ffd5-4cad-9c9a-4200a974df6a', '-', '')), 119798.7, 17122.35, 'ES0993423471739576659875',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 4,
        '5107158973908285', '02', '2025', '826');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), 77621.49, 6017.23, 'ES6361277010102780882315',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 3,
        '3546570312135780', '06', '2030', '131');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')), 66787.19, 4747.48, 'ES5575752961307291267561',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 1,
        '30574110188172', '04', '2026', '709');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), 63339.56, 18509.99, 'ES0734627176473207288353',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 4,
        '4759059056486369', '03', '2029', '446');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', '')), 99182.52, 18378.43, 'ES6596896828624237011631',
        UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), 2,
        '503876466528', '09', '2025', '300');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', '')), 95020.1, 3310.83, 'ES5311679884191806165610',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 2,
        '2227322937001981', '02', '2025', '422');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('1e174207-06fb-4a8c-a66b-c1ae4b9672d1', '-', '')), 100743.43, 11325.09,
        'ES9015553119672875027719',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 1,
        '30350229174845', '06', '2026', '930');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('d27a9cc2-5743-40a1-b6e3-fdc97a6c4351', '-', '')), 48136.0, 15126.94, 'ES0724353030347230794723',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 1,
        '4967466016318891891', '12', '2028', '288');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), 107250.04, 22836.05,
        'ES7979888574370079186391',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 2,
        '4569310867385607251', '03', '2030', '497');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')), 111582.39, 9999.16, 'ES7583903548414104812442',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 3,
        '3545626170408551', '04', '2026', '808');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), 39531.76, 13685.05, 'ES3208036225068058672431',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 2,
        '30352499944816', '09', '2026', '856');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')), 71296.67, 16873.78, 'ES4415481759216068926570',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 2,
        '30175791205242', '08', '2026', '706');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('e0a54e04-4851-45ee-8ac6-9e6db782002d', '-', '')), 50107.19, 22721.68, 'ES1673846676222302953361',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 3,
        '639046270687', '08', '2025', '183');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('cdd14828-85c9-45ac-8b05-61491ee2514e', '-', '')), 53227.01, 17164.15, 'ES5345310524898726147957',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 2,
        '501803192548', '05', '2027', '608');
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('cda9b784-9da6-4b17-93f1-7181b9609c4a', '-', '')), 81981.97, 2447.05, 'ES3183510830919004418423',
        UNHEX(REPLACE('4d5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '-', '')), 2,
        '3532211105253250', '02', '2029', '420');

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

-- ---------------------------------------------------------
-- Establecimiento de la estructura jerárquica de jefes
-- ---------------------------------------------------------

-- Establecer jefes de departamento que reportan al CEO (Vicente Palma)
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', ''))
WHERE id IN (
             UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', '')), -- Moisés Villalba (RRHH)
             UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', '')), -- Verónica Blanca (TEC)
             UNHEX(REPLACE('f16de5d1-0c78-4616-87d5-3045ff2fe8b0', '-', '')), -- Marisol Valenciano (VNT)
             UNHEX(REPLACE('8d8a9bc1-6068-4da6-bada-176c40e1bea2', '-', '')), -- Wilfredo Cózar (MKT)
             UNHEX(REPLACE('f63e2354-18ea-4951-be31-06d11e9d5b87', '-', '')) -- Glauco Perelló (FIN)
    );

-- Empleados de RRHH reportando a Moisés Villalba
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', ''))
  AND id != UNHEX(REPLACE('88ad5d0c-7919-4edb-a7f5-fd07cb8ba81e', '-', ''));

-- Empleados de Tecnología reportando a Verónica Blanca
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', ''))
WHERE id_departamento = UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', ''))
  AND id != UNHEX(REPLACE('2cfbb0e5-7923-476d-bd6d-4d750e5a87ab', '-', ''))
AND id != UNHEX(REPLACE('53cddfc5-96ab-4a2e-9f3d-8208f9cee76a', '-', ''));
-- Excepto el CEO

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
SET id_jefe = UNHEX(REPLACE('fbfb87a3-7ccb-47d1-a259-f35b9d66a829', '-', '')) -- Mercedes Gisbert
WHERE id IN (
             UNHEX(REPLACE('d7297cda-a3c0-4fb5-8e22-51629d90c332', '-', '')), -- Rosalía Cabañas
             UNHEX(REPLACE('8a5826f4-87c5-46ad-94e4-1806c79635f3', '-', '')) -- Maribel Malo
    );

-- Paz Mur como responsable del equipo de ingenieros eléctricos
UPDATE empleado
SET id_jefe = UNHEX(REPLACE('1cb5803b-0b28-4a91-ac42-64dd85ff9d87', '-', ''))
WHERE id = UNHEX(REPLACE('65b60a24-c9b2-418f-b57b-a01fe0b11a69', '-', '')); -- Lilia Rivas
