-- initialize the schema and tables with basic stuff..

-- DROP DATABASE simple;
CREATE DATABASE simple;

--below only work if supplying this script to psql directly, comment out otherwise.
-- \g
-- \c simple

--select the simple db for operations after this.

-- DROP SCHEMA webapp;
CREATE SCHEMA IF NOT EXISTS webapp;

--User table
CREATE TABLE IF NOT EXISTS  webapp.users (
  uid UUID,
  first_name varchar(160),
  last_name varchar(160),
  email varchar(256),
  pass_hash varchar(256),
  pass_salt varchar(256),
  created timestamp,
  updated timestamp,
  PRIMARY KEY(uid)
);

--Document table
CREATE OR REPLACE TABLE webapp.documents (
  docid UUID,
  doc_url varchar(256),
  filename varchar(500),
  created timestamp,
  updated timestamp,
  PRIMARY KEY(docid)
);

--TODO: initialize with some basic values
