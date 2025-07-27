/*
 * This file defines the basic security tables and views required by the applications
 * using app.base. It is only necessary to run this file to create the security 
 * database and tables if your application requires security as specified in the
 * specification within ApplicationDefinition for your application.
 * 
 * Each application has further table requirements if you are going to store data in
 * a SQL database.
 */

-- Change database_name to match the one that is specified in the file security.properties
CREATE DATABASE IF NOT EXISTS database_name;

-- Change database_name to match the one that is specified in the file security.properties
use database_name;

DROP TABLE IF EXISTS userapplications;
DROP TABLE IF EXISTS applicationtables;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS tables;

DROP VIEW IF EXISTS registrations;
DROP VIEW IF EXISTS dependencies;

CREATE TABLE IF NOT EXISTS users (
    idusers int AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(45),
    password VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS applications (
    idapps INT AUTO_INCREMENT PRIMARY KEY,
    appname VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS userapplications (
    user_id INT NOT NULL,
    app_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(idusers),
    FOREIGN KEY (app_id) REFERENCES applications(idapps),
    PRIMARY KEY (user_id, app_id)
);

CREATE TABLE IF NOT EXISTS tables (
    idtables INT AUTO_INCREMENT PRIMARY KEY,
    tablename VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS applicationtables (
    app_id INT NOT NULL,
    table_id INT NOT NULL,
    FOREIGN KEY (app_id) REFERENCES applications(idapps),
    FOREIGN KEY (table_id) REFERENCES tables(idtables),
    PRIMARY KEY (app_id, table_id)
);

CREATE VIEW registrations AS
SELECT username, appname FROM (users,applications,userapplications) 
WHERE users.idusers=userapplications.user_id AND applications.idapps=userapplications.app_id;

CREATE VIEW dependencies AS
SELECT appname, tablename FROM (applications, tables, applicationtables)
WHERE applications.idapps=applicationtables.app_id AND tables.idtables=applicationtables.table_id;

INSERT INTO applications(appname) VALUE
    ('bank.application'),
    ('property.application'),
    ('slideshow.application'),
    ('weight.monitor.application');
    
