-- Création de la base de données SecondeDB
CREATE DATABASE SecondeDB WITH
	OWNER = postgres
	ENCODING = 'UTF8';


-- Création de la table Client
CREATE TABLE Client
(
    Code_Client     INTEGER,
    Nom             CHARACTER VARYING(32)   NOT NULL,
    Prenom          CHARACTER VARYING(32)   NOT NULL,
    Adresse         CHARACTER VARYING(64)   NOT NULL,
    Code_Postal     CHARACTER(5)            NOT NULL,
    Ville           CHARACTER VARYING(32)   NOT NULL,
    Email           CHARACTER VARYING(64)       NULL,
    PRIMARY KEY(Code_Client));


-- Gestion des droits pour les utilisateurs lambda et postgres
-- lambda : droits en lecture et ecriture (sans pouvoir, par ex., créer un user)
-- postgres : tous les droits
-- pour les reste : aucun droits

REVOKE ALL ON Client        FROM PUBLIC;
GRANT SELECT ON Client      TO lambda;
GRANT INSERT ON Client      TO lambda;
GRANT UPDATE ON Client      TO lambda;
GRANT DELETE ON Client      TO lambda;
GRANT ALL ON Client         TO postgres;
