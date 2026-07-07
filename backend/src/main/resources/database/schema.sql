CREATE TABLE IF NOT EXISTS usuarios (
    usuario_id      BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    correo          VARCHAR(150) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    rol             VARCHAR(20)  NOT NULL CHECK (rol IN ('productor','tecnico','administrador')),
    ubicacion       VARCHAR(200),
    fecha_registro  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS estanques (
    estanque_id      BIGSERIAL PRIMARY KEY,
    usuario_id       BIGINT       NOT NULL REFERENCES usuarios(usuario_id),
    nombre           VARCHAR(100) NOT NULL,
    tipo_agua        VARCHAR(10)  NOT NULL,
    capacidad_litros NUMERIC(12,2) NOT NULL,
    localizacion     VARCHAR(200),
    estado           VARCHAR(15)  NOT NULL DEFAULT 'disponible',
    fecha_creacion   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS lotes (
    lote_id         BIGSERIAL PRIMARY KEY,
    estanque_id     BIGINT        NOT NULL REFERENCES estanques(estanque_id) ON DELETE CASCADE,
    especie         VARCHAR(100)  NOT NULL,
    cantidad        INT           NOT NULL,
    fecha_siembra   DATE          NOT NULL,
    peso_inicial_g  NUMERIC(8,2)  NOT NULL,
    estado          VARCHAR(15)   NOT NULL DEFAULT 'activo',
    fecha_fin       DATE
);

CREATE TABLE IF NOT EXISTS parametros_agua (
    parametro_id   BIGSERIAL PRIMARY KEY,
    estanque_id    BIGINT       NOT NULL REFERENCES estanques(estanque_id) ON DELETE CASCADE,
    usuario_id     BIGINT       NOT NULL REFERENCES usuarios(usuario_id),
    ph             NUMERIC(4,2) NOT NULL,
    temperatura_c  NUMERIC(5,2) NOT NULL,
    oxigeno_mgl    NUMERIC(5,2) NOT NULL,
    amoniaco_mgl   NUMERIC(5,3) NOT NULL,
    fecha_registro TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS tareas (
    tarea_id    BIGSERIAL PRIMARY KEY,
    usuario_id  BIGINT       NOT NULL REFERENCES usuarios(usuario_id),
    estanque_id BIGINT       REFERENCES estanques(estanque_id) ON DELETE SET NULL,
    lote_id     BIGINT       REFERENCES lotes(lote_id),
    nombre      VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha_hora  TIMESTAMP    NOT NULL,
    estado      VARCHAR(15)  NOT NULL DEFAULT 'pendiente',
    notificado  BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS notificaciones (
    notificacion_id BIGSERIAL PRIMARY KEY,
    tarea_id        BIGINT    NOT NULL REFERENCES tareas(tarea_id),
    usuario_id      BIGINT    NOT NULL REFERENCES usuarios(usuario_id),
    tipo            VARCHAR(20)  NOT NULL,
    mensaje         TEXT         NOT NULL,
    leida           BOOLEAN      NOT NULL DEFAULT FALSE,
    fecha_envio     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS notas (
    nota_id    BIGSERIAL PRIMARY KEY,
    lote_id    BIGINT    NOT NULL REFERENCES lotes(lote_id) ON DELETE CASCADE,
    contenido  TEXT      NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS registros_crecimiento (
    registro_id     BIGSERIAL PRIMARY KEY,
    lote_id         BIGINT        NOT NULL REFERENCES lotes(lote_id) ON DELETE CASCADE,
    peso_promedio_g NUMERIC(8,2)  NOT NULL,
    talla_cm        NUMERIC(6,2),
    mortalidad      INT,
    fecha_muestreo  DATE          NOT NULL
);

CREATE TABLE IF NOT EXISTS bitacora (
    bitacora_id    BIGSERIAL PRIMARY KEY,
    usuario_id     BIGINT       NOT NULL REFERENCES usuarios(usuario_id),
    accion         VARCHAR(200) NOT NULL,
    fecha_registro TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS alertas (
    alerta_id      BIGSERIAL PRIMARY KEY,
    estanque_id    BIGINT       NOT NULL REFERENCES estanques(estanque_id) ON DELETE CASCADE,
    parametro_id   BIGINT       REFERENCES parametros_agua(parametro_id) ON DELETE CASCADE,
    mensaje        VARCHAR(200) NOT NULL,
    fecha_registro TIMESTAMP    NOT NULL DEFAULT NOW()
);