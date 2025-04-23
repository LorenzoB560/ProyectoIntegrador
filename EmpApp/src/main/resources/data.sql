-- Géneros
INSERT INTO genero (id, genero) VALUES (1, 'Masculino');
INSERT INTO genero (id, genero) VALUES (2, 'Femenino');
INSERT INTO genero (id, genero) VALUES (3, 'No binario');

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

-- Motivos de Bloqueo
-- INSERT INTO motivo_bloqueo (id, descripcion)
-- VALUES (1, 'Múltiples intentos fallidos');
-- INSERT INTO motivo_bloqueo (id, descripcion)
-- VALUES (2, 'Inactividad prolongada');
-- INSERT INTO motivo_bloqueo (id, descripcion)
-- VALUES (3, 'Solicitud del usuario');
-- INSERT INTO motivo_bloqueo (id, descripcion)
-- VALUES (4, 'Razones de seguridad');

-- PRIMER EMPLEADO: Inserción en tabla principal
INSERT INTO empleado (id, nombre, apellido, tipo_documento, documento, fecha_nacimiento, edad, pais_nacimiento, genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '-', '')), 'Carlos', 'Rodríguez', 'DNI', '12345678A', '1975-05-15', 50, 'España', 1,
        'Calle', 'Gran Vía', '28', 'A', '5', 'B', 'Madrid', '28013', 'Madrid',
        'Director General con amplia experiencia en gestión empresarial', NULL, '2010-01-15', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d479', '-', '')));

-- PRIMER EMPLEADO: Inserción en tabla secundaria
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '-', '')), 120000.00, 25000.00, 'ES7921000813610123456789',
        UNHEX(REPLACE('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d', '-', '')), 4,
        '5412 7534 8901 2345', '12', '2027', '123');

-- SEGUNDO EMPLEADO: Inserción en tabla principal
INSERT INTO empleado (id, nombre, apellido, tipo_documento, documento, fecha_nacimiento, edad, pais_nacimiento, genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), 'Laura', 'Martínez', 'DNI', '23456789B', '1980-08-22', 45, 'España', 2,
        'Avenida', 'Diagonal', '532', '2', '4', 'A', 'Barcelona', '08006', 'Cataluña',
        'Directora de Tecnología especializada en transformación digital', UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '-', '')), '2012-04-10', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')));

-- SEGUNDO EMPLEADO: Inserción en tabla secundaria
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), 95000.00, 15000.00, 'ES8920807698623456789012',
        UNHEX(REPLACE('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '-', '')), 2,
        '4123 5678 9012 3456', '03', '2026', '456');

-- TERCER EMPLEADO: Inserción en tabla principal
INSERT INTO empleado (id, nombre, apellido, tipo_documento, documento, fecha_nacimiento, edad, pais_nacimiento, genero_id,
                      tipo_via, via, numero, portal, planta, puerta, localidad, codigo_postal, region,
                      comentarios, id_jefe, fecha_contratacion, fecha_cese, activo, id_departamento)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '-', '')), 'Miguel', 'García', 'DNI', '34567890C', '1988-11-30', 37, 'España', 1,
        'Calle', 'Provenza', '123', '4', '2', 'C', 'Barcelona', '08008', 'Cataluña',
        'Desarrollador Java con 10 años de experiencia en aplicaciones empresariales', UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), '2015-07-05', NULL, true, UNHEX(REPLACE('f47ac10b-58cc-4372-a567-0e02b2c3d480', '-', '')));

-- TERCER EMPLEADO: Inserción en tabla secundaria
INSERT INTO informacion_economica (id, salario, comision, IBAN, id_entidad_bancaria, id_tipo_tarjeta,
                                   numero, mes_caducidad, anio_caducidad, CVC)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '-', '')), 65000.00, 5000.00, 'ES7100302053091234567895',
        UNHEX(REPLACE('3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f', '-', '')), 1,
        '5678 1234 5678 9012', '09', '2025', '789');

-- Usuarios
INSERT INTO usuario_empleado (id, correo, clave, id_empleado, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '-', '')), 'carlos.rodriguez@empresa.com', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '-', '')), '2023-04-20 10:30:00', true, 0);

INSERT INTO usuario_empleado (id, correo, clave, id_empleado, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), 'laura.martinez@empresa.com', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), '2023-04-22 16:45:00', true, 0);

INSERT INTO usuario_empleado (id, correo, clave, id_empleado, ultima_conexion, activo, intentos_sesion_fallidos)
VALUES (UNHEX(REPLACE('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '-', '')), 'miguel.garcia@empresa.com', '$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7aOHnWSPo36',
        UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '-', '')), '2023-04-23 09:15:00', true, 0);

-- Relaciones muchos a muchos (empleado_especialidad)
INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), UNHEX(REPLACE('a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d', '-', '')));

INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '-', '')), UNHEX(REPLACE('d4e5f6a7-b8c9-7d0e-1f2a-3b4c5d6e7f8a', '-', '')));

INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '-', '')), UNHEX(REPLACE('a1b2c3d4-e5f6-4a5b-8c7d-9e8f7a6b5c4d', '-', '')));

INSERT INTO empleado_especialidad (id_empleado, id_especialidad)
VALUES (UNHEX(REPLACE('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '-', '')), UNHEX(REPLACE('e5f6a7b8-c9d0-8e1f-2a3b-4c5d6e7f8a9b', '-', '')));
