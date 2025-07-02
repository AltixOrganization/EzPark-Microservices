#!/bin/bash
set -e # Detiene el script si un comando falla

# La función 'psql' ejecutará un comando SQL.
# -v ON_ERROR_STOP=1 : asegura que el script falle si un comando SQL falla.
# "$POSTGRES_USER" : se conecta como el superusuario de postgres.
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Creación de las bases de datos para cada microservicio
    -- El comando CREATE DATABASE no se puede ejecutar en un bloque de transacción,
    -- por eso usamos \gexec para ejecutarlo por cada fila.
    SELECT 'CREATE DATABASE ' || datname FROM (
    VALUES
        ('ezpark_iam_db'),
        ('ezpark_profiles_db'),
        ('ezpark_vehicles_db'),
        ('ezpark_parkings_db'),
        ('ezpark_reservations_db'),
        ('ezpark_payments_db'),
        ('ezpark_reviews_db'),
        ('ezpark_notifications_db')
        -- Añade más bases de datos aquí si las necesitas
    ) AS d(datname)
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = d.datname)
    \gexec

    -- Opcional: Otorgar privilegios al usuario principal en las nuevas bases de datos
    -- (generalmente no es necesario si el mismo usuario es el dueño)
EOSQL