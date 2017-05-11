FROM postgres:latest

ENV POSTGRES_DB postgresdb
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres

ADD sql/*.sql /docker-entrypoint-initdb.d/
