USE pharmasteve;

/*insert adresse*/
INSERT INTO adresse (NUM_ADRESSE, RUE_ADRESSE, CODEPOSTAL_ADRESSE, VILLE_ADRESSE)
values ("37", "Rue Julie Daubié", "54000", "Nancy"),
("61", "rue Stanislas", "54000", "Nancy"),
("61", "avenue Foch", "54000", "Nancy"),
("30", "rue de Nancy", "54630", "Richardménil"),
("75", "Boulevard Maréchal Foch", "54520", "Laxou"),
("29", "Rue de la Commanderie", "54000", "Nancy"),
("20", "Rue de la mine", "54230", "Neuves-Maisons"),
("9", "Rue Maurice Barres", "54000", "Nancy"),
("19", "rue Saint-Jean", "54000","Nancy");

/*insert mutuelle*/
INSERT INTO mutuelle (ID_ADRESSE, NOM_MUTUELLE, TEL_MUTUELLE, EMAIL_MUTUELLE, DEPARTMENT_MUTUELLE, REBOURSEMENT)
values (8, "MGEN", "3976", "acceuil@mgen.fr", "Meurthe-et-Moselle", 80),
(9, "Harmonie Mutuelle",  "09.80.98.08.80", "contacter@harmoniemutuelle.fr","Meurthe-et-Moselle", 70);

/*insert personne*/
INSERT INTO personne (ID_ADRESSE, NOM_PERSONNE, PRENOM_PERSONNE, TELEPHONE_PERSONNE, EMAIL_PERSONNE)
values (1, "Titor", "John", "06.65.20.40.32", "j.titor@hotmail.fr"),
(1, "Tisserand", "Anne", "03.83.95.10.95", "a.tisserand@gmail.com"),
(2, "Thess", "François", "03.83.35.19.76", "t.francois@orange.fr"),
(6, "Chastagner", "Nathalie", "03.83.40.25.97", "c.nathalie@hotmail.fr"),
(7, "Baruffle", "Mireille", "03.83.50.19.01", "m.baruffaldi@gmail.com"),
(4, "Retournay", "Steve", "06.81.30.29.76", "steve54@wanadoo.fr"),
(5, "Sublion", "Julien", "06.25.64.13.20", "j.sublion@hotmail.fr"),
(3, "Henri", "Thomas", "06.25.99.11.30", "t.henri@orange.fr");

INSERT INTO medicament (NOM_MEDOC, CATEGORIE_MEDOC, PRIX_MEDOC, CIRCULATION_MEDOC, STOCK_MEDOC)
values ("Acuitel", "Antihypertenseur", 3.4, "1989-04-14", 50),
("Doliprane", "antalgique", 2.1 , "1984-01-01", 60),
("Oxybutine", "anticholinergique", 2.9, "2012-11-16", 40),
("Parrafin", "Emoliant", 10, "2017-07-19", 80),
("Amoxicilline", "Antibiotique", 1.5, "1953-05-02", 60);

/*create medecin*/
UPDATE personne
set NUMERO_MEDECIN ="1562038064121782" WHERE ID_PERSONNE = 4;

UPDATE personne
set NUMERO_MEDECIN = "4087203165981412" WHERE ID_PERSONNE = 5;

/*create specialiste*/
UPDATE personne
set NOM_SPECIALITE = "Urologie" WHERE ID_PERSONNE = 1;

UPDATE personne
set NOM_SPECIALITE = "Cardiologie" WHERE ID_PERSONNE = 2;

UPDATE personne
set NOM_SPECIALITE = "Dermatologie" WHERE ID_PERSONNE = 3;

/*create client*/

UPDATE personne
set ID_MEDECIN = "4", MUTUELLE_CLIENT = "1", 
SECSOCIALE_CLIENT = "1900175127030",
NAISSANCE_CLIENT = "1990-01-03"
WHERE ID_PERSONNE = "6";

UPDATE personne
set ID_MEDECIN = "5", MUTUELLE_CLIENT = "2", 
SECSOCIALE_CLIENT = "1860154117263",
NAISSANCE_CLIENT = "1986-08-14"
WHERE ID_PERSONNE = "7";

UPDATE personne
set ID_MEDECIN = "4", MUTUELLE_CLIENT = "1", 
SECSOCIALE_CLIENT = "1540288145236",
NAISSANCE_CLIENT = "1954-02-28"
WHERE ID_PERSONNE = "8";

/*create liste_specialiste*/

INSERT INTO liste_client_specialiste
VALUES (6, 1), (6, 2), (6, 3), (7, 1), (7, 2), (8, 3)