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

CREATE TABLE IF NOT EXISTS guias (
    guia_id    BIGSERIAL PRIMARY KEY,
    especie    VARCHAR(100) NOT NULL,
    categoria  VARCHAR(20)  NOT NULL CHECK (categoria IN ('sanidad','alimentacion')),
    parametro  VARCHAR(20)  CHECK (parametro IN ('ph','temperatura','oxigeno','amoniaco')),
    titulo     VARCHAR(150) NOT NULL,
    contenido  TEXT         NOT NULL,
    UNIQUE (especie, categoria, titulo)
);

CREATE TABLE IF NOT EXISTS consultas_guia (
    consulta_id    BIGSERIAL PRIMARY KEY,
    guia_id        BIGINT    NOT NULL REFERENCES guias(guia_id) ON DELETE CASCADE,
    lote_id        BIGINT    NOT NULL REFERENCES lotes(lote_id) ON DELETE CASCADE,
    fecha_consulta TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE guias ADD COLUMN IF NOT EXISTS parametro VARCHAR(20);

INSERT INTO guias (especie, categoria, titulo, contenido) VALUES
('trucha', 'sanidad',
 'Prevención de saprolegniasis (hongos) en trucha',
 'Mantén el oxígeno disuelto sobre 6 mg/L y evita el hacinamiento: el estrés y las heridas por manipulación son la principal puerta de entrada del hongo. Revisa a diario que no haya peces con manchas algodonosas blanquecinas en piel o branquias y sepáralos de inmediato del resto del lote.'),
('trucha', 'alimentacion',
 'Plan de alimentación para trucha arcoíris',
 'Alimenta entre 2 y 4 veces al día con una ración diaria de 1.5 a 3% de la biomasa total, ajustando a la baja cuando la temperatura del agua supere los 18°C. Evita el exceso de alimento no consumido, ya que se descompone y eleva el amoníaco del estanque.'),
('tilapia', 'sanidad',
 'Control de parásitos y bacterias en tilapia',
 'Realiza recambios parciales de agua (10-15%) cada semana para reducir la carga de patógenos, y desinfecta las mallas y equipos entre siembras. Ante nado errático o pérdida de apetito, revisa branquias en busca de puntos blancos característicos de ictioftiriasis.'),
('tilapia', 'alimentacion',
 'Dosificación de alimento balanceado para tilapia',
 'La tilapia tolera raciones de 3 a 5% de su biomasa en etapas juveniles, reduciendo progresivamente al 2% en peces de engorde. Distribuye el alimento en al menos 3 tomas diarias para mejorar la conversión alimenticia y reducir el desperdicio.')
ON CONFLICT (especie, categoria, titulo) DO NOTHING;

INSERT INTO guias (especie, categoria, parametro, titulo, contenido) VALUES
('trucha', 'sanidad', 'ph',
 'Corrección de pH fuera de rango en trucha',
 'El pH ideal para trucha es 6.5-8.0. Valores fuera de este rango dañan las branquias y la capa de mucus protector, aumentando la susceptibilidad a infecciones. Si el agua está ácida, aplica cal agrícola en dosis bajas; si está muy alcalina, revisa la fuente de agua y evita acumular materia orgánica en descomposición en el fondo.'),
('trucha', 'sanidad', 'temperatura',
 'Manejo de estrés térmico en trucha',
 'La trucha tolera 10-18°C; fuera de este rango entra en estrés metabólico y baja sus defensas naturales. Con agua fría, reduce la alimentación y evita manipular a los peces; con agua caliente, aumenta la aireación y busca mayor profundidad o sombra en el estanque.'),
('trucha', 'sanidad', 'oxigeno',
 'Manejo de bajo oxígeno disuelto en trucha',
 'La trucha necesita al menos 6 mg/L de oxígeno disuelto; por debajo de ese nivel se estresa y queda más expuesta a hongos e infecciones. Aumenta la aireación de inmediato, reduce la densidad de siembra si es posible, y evita alimentar hasta que el nivel se normalice.'),
('trucha', 'sanidad', 'amoniaco',
 'Control de amoníaco en trucha',
 'El amoníaco no ionizado daña las branquias incluso en concentraciones bajas, y la trucha es particularmente sensible. Ante niveles elevados, realiza un recambio parcial de agua de inmediato, reduce la ración de alimento por 24-48 horas y retira el alimento no consumido del fondo.'),
('tilapia', 'sanidad', 'ph',
 'Corrección de pH fuera de rango en tilapia',
 'La tilapia tolera un rango más amplio (6.0-9.0), pero fuera de él también sufre estrés osmótico. Corrige gradualmente con cal agrícola o renovación parcial de agua, evitando cambios bruscos que generen un shock adicional en el lote.'),
('tilapia', 'sanidad', 'temperatura',
 'Manejo de estrés térmico en tilapia',
 'La tilapia es una especie tropical con rango ideal de 25-32°C; por debajo de 20°C deja de alimentarse y por debajo de 12°C el riesgo de mortalidad es alto. Ante temperaturas bajas, reduce la alimentación y considera cubrir o profundizar el estanque para conservar calor.'),
('tilapia', 'sanidad', 'oxigeno',
 'Manejo de bajo oxígeno disuelto en tilapia',
 'Aunque la tilapia tolera niveles de oxígeno más bajos que otras especies, por debajo de 4 mg/L reduce su crecimiento y queda más vulnerable a enfermedades. Aumenta la aireación, especialmente en horas de la madrugada, y evita la sobrepoblación del estanque.'),
('tilapia', 'sanidad', 'amoniaco',
 'Control de amoníaco en tilapia',
 'Aunque la tilapia es más tolerante al amoníaco que otras especies, niveles sostenidos por encima de lo seguro afectan el crecimiento y dañan las branquias. Realiza recambios parciales de agua y evita el exceso de alimento no consumido en el fondo.')
ON CONFLICT (especie, categoria, titulo) DO NOTHING;