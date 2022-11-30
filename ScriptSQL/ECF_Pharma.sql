/*==============================================================*/
/* Nom de SGBD :  MySQL	ALTER                                   */
/* Date de création :  03/11/2022 09:40:26     					*/
/* Auteur : Steve Retournay						                */
/*==============================================================*/

DROP SCHEMA IF EXISTS pharmasteve;
CREATE SCHEMA pharmasteve;
USE pharmasteve;

/*==============================================================*/
/* Table : ADRESSE                                              */
/*==============================================================*/
create table ADRESSE
(
   ID_ADRESSE           int not null auto_increment,
   NUM_ADRESSE          char(7) not null,
   RUE_ADRESSE          char(50) not null,
   CODEPOSTAL_ADRESSE   char(5) not null,
   VILLE_ADRESSE        char(20) not null,
   primary key (ID_ADRESSE)
);

/*==============================================================*/
/* Table : MUTUELLE                                             */
/*==============================================================*/
create table MUTUELLE
(
   ID_MUTUELLE          int not null auto_increment,
   ID_ADRESSE           int not null,
   NOM_MUTUELLE         char(50) not null,
   TEL_MUTUELLE         char(14) not null,
   EMAIL_MUTUELLE       char(30) not null,
   DEPARTMENT_MUTUELLE  char(20) not null,
   REBOURSEMENT         smallint not null,
   primary key (ID_MUTUELLE),
   constraint FK_SE_SITUE foreign key (ID_ADRESSE)
      references ADRESSE (ID_ADRESSE) on delete restrict on update restrict
);

/*==============================================================*/
/* Table : PERSONNE                                             */
/*==============================================================*/
create table PERSONNE
(
   ID_PERSONNE          int not null auto_increment,
   MUTUELLE_CLIENT      int,
   NOM_SPECIALITE       VARCHAR(20),
   ID_ADRESSE           int not null,
   ID_MEDECIN           int,
   NOM_PERSONNE         char(50) not null,
   PRENOM_PERSONNE      char(50) not null,
   TELEPHONE_PERSONNE   char(14) not null,
   EMAIL_PERSONNE       char(50) not null,
   NUMERO_MEDECIN       char(16),
   SECSOCIALE_CLIENT    char(13) unique,
   NAISSANCE_CLIENT     date,
   primary key (ID_PERSONNE),
   constraint FK_HABITE foreign key (ID_ADRESSE)
      references ADRESSE (ID_ADRESSE) on delete restrict on update restrict,
   constraint FK_MEDECIN_TRAITANT foreign key (ID_MEDECIN)
      references PERSONNE (ID_PERSONNE) on delete restrict on update restrict,
   constraint FK_ADHERE foreign key (MUTUELLE_CLIENT)
      references MUTUELLE (ID_MUTUELLE) on delete restrict on update restrict);

/*==============================================================*/
/* Table : ACHAT                                                */
/*==============================================================*/
create table ACHAT
(
   ID_ACHAT             int not null auto_increment,
   ID_CLIENT            int not null,
   ID_SPECIALISTE       int,
   ID_MEDECIN           int,
   DATE_ACHAT           date not null,
   primary key (ID_ACHAT),
   constraint FK_COMPLEMENTE foreign key (ID_SPECIALISTE)
      references PERSONNE (ID_PERSONNE) on delete restrict on update restrict,
   constraint FK_REALISE foreign key (ID_CLIENT)
      references PERSONNE (ID_PERSONNE) on delete restrict on update restrict,
   constraint FK_REDIGE foreign key (ID_MEDECIN)
      references PERSONNE (ID_PERSONNE) on delete restrict on update restrict
);

/*==============================================================*/
/* Table : LISTE_CLIENT_SPECIALISTE                             */
/*==============================================================*/
create table LISTE_CLIENT_SPECIALISTE
(
   ID_CLIENT            int not null,
   ID_SPECIALISTE       int not null,
   primary key (ID_SPECIALISTE, ID_CLIENT),
   constraint FK_LISTE_CLIENT_SEPCIALISTE2 foreign key (ID_SPECIALISTE)
      references PERSONNE (ID_PERSONNE) on delete restrict on update restrict,
   constraint FK_LISTE_CLIENT_SEPCIALISTE foreign key (ID_CLIENT)
      references PERSONNE (ID_PERSONNE) on delete restrict on update restrict
);

/*==============================================================*/
/* Table : MEDICAMENT                                           */
/*==============================================================*/
create table MEDICAMENT
(
   ID_MEDOC             int not null auto_increment,
   NOM_MEDOC            char(50) not null,
   CATEGORIE_MEDOC      char(50) not null,
   PRIX_MEDOC           float(3,0) not null,
   CIRCULATION_MEDOC    date not null,
   STOCK_MEDOC          int not null,
   primary key (ID_MEDOC)
);

/*==============================================================*/
/* Table : PANIER                                               */
/*==============================================================*/
create table PANIER
(
   ID_ACHAT             int not null,
   ID_MEDOC             int not null,
   QUANTITE             int not null,
   primary key (ID_ACHAT, ID_MEDOC),
   constraint FK_PANIER2 foreign key (ID_ACHAT)
      references ACHAT (ID_ACHAT) on delete restrict on update restrict,
   constraint FK_PANIER foreign key (ID_MEDOC)
      references MEDICAMENT (ID_MEDOC) on delete restrict on update restrict
);

DELIMITER |

/*==============================================================*/
/* Création de procédures stockées		                        */
/*==============================================================*/

CREATE PROCEDURE est_medecin (IN id int)
BEGIN
IF id not in (SELECT ID_PERSONNE FROM personne WHERE NUMERO_MEDECIN is not null)
THEN SIGNAL SQLSTATE VALUE "45000" SET MESSAGE_TEXT = "Un medecin a un numéro d'aggrément";
END IF;
END |

CREATE PROCEDURE est_specialiste (IN id int)
BEGIN
IF id not in (SELECT ID_PERSONNE FROM personne WHERE NOM_SPECIALITE is not null)
THEN SIGNAL SQLSTATE VALUE "45000" SET MESSAGE_TEXT = "Cette personne n'est pas un spécialiste";
END IF;
END |

CREATE PROCEDURE est_client (IN id int)
BEGIN
IF id not in (SELECT ID_PERSONNE FROM personne WHERE ID_MEDECIN is not null and MUTUELLE_CLIENT is not null
and SECSOCIALE_CLIENT IS NOT NULL AND NAISSANCE_CLIENT IS NOT NULL)
THEN SIGNAL SQLSTATE VALUE "45000" SET MESSAGE_TEXT = "Cette personne n'est pas un client";
END IF;
END |

/*==============================================================*/
/* Triggers	: Personne					                        */
/*==============================================================*/

CREATE TRIGGER verif_medecin_personne_in
BEFORE INSERT ON personne FOR EACH ROW
BEGIN
IF new.ID_MEDECIN IS NOT NULL
THEN CALL est_medecin(new.ID_MEDECIN);
ELSEIF new.ID_PERSONNE = NEW.ID_MEDECIN
THEN SIGNAL SQLSTATE "45000" SET MESSAGE_TEXT = "Une personne ne peut pas être son propre médecin traitant";
END IF;
END |

CREATE TRIGGER verif_medecin_personne_up
BEFORE UPDATE ON personne FOR EACH ROW
BEGIN
IF new.ID_MEDECIN IS NOT NULL
THEN CALL est_medecin(new.ID_MEDECIN);
ELSEIF new.ID_PERSONNE = NEW.ID_MEDECIN
THEN SIGNAL SQLSTATE "45000" SET MESSAGE_TEXT = "Une personne ne peut pas être son propre médecin traitant";
END IF;
END |

/*==============================================================*/
/* Triggers	: achat						                        */
/*==============================================================*/

CREATE TRIGGER ajout_achat
BEFORE INSERT ON achat FOR EACH ROW
BEGIN
    IF new.ID_CLIENT = NEW.ID_MEDECIN
	THEN SIGNAL SQLSTATE "45000" SET MESSAGE_TEXT = "Une personne ne peut pas écrire une ordonnance pour lui-même";
	ELSEIF new.ID_SPECIALISTE is not null
	THEN CALL est_specialiste(new.ID_SPECIALISTE);
    ELSEIF new.ID_CLIENT = NEW.ID_SPECIALISTE
	THEN SIGNAL SQLSTATE "45000" SET MESSAGE_TEXT = "Une personne ne peut pas écrire une ordonnance pour lui-même";
    ELSEIF new.ID_MEDECIN is NOT NULL
    THEN CALL est_medecin(new.ID_MEDECIN);
    END IF;
    CALL est_client(new.ID_CLIENT);
END |

CREATE TRIGGER maj_achat
BEFORE UPDATE ON achat FOR EACH ROW
BEGIN
	CALL est_client(new.ID_CLIENT);
    IF new.ID_MEDECIN is NOT NULL
    THEN CALL est_medecin(new.ID_MEDECIN);
	ELSEIF new.ID_CLIENT = NEW.ID_MEDECIN
	THEN SIGNAL SQLSTATE "45000" SET MESSAGE_TEXT = "Une personne ne peut pas écrire une ordonnance pour lui-même";
	ELSEIF new.ID_SPECIALISTE is not null
	THEN CALL est_specialiste(new.ID_SPECIALISTE);
    ELSEIF new.ID_CLIENT = NEW.ID_SPECIALISTE
	THEN SIGNAL SQLSTATE "45000" SET MESSAGE_TEXT = "Une personne ne peut pas écrire une ordonnance pour lui-même";
    END IF;
END |

CREATE TRIGGER suppression
BEFORE DELETE ON achat FOR EACH ROW
BEGIN
	DELETE FROM panier WHERE ID_ACHAT = old.ID_ACHAT;
END |


/*==============================================================*/
/* Triggers	: Liste client specialiste	                        */
/*==============================================================*/

CREATE TRIGGER ajout_specialiste
BEFORE INSERT ON liste_client_specialiste FOR EACH ROW
BEGIN
	CALL est_specialiste(new.ID_SPECIALISTE);
    CALL est_client(new.ID_CLIENT);
END |

CREATE TRIGGER modifier_specialiste
BEFORE UPDATE ON liste_client_specialiste FOR EACH ROW
BEGIN
    CALL est_specialiste(new.ID_SPECIALISTE);
    CALL est_client(new.ID_CLIENT);
END |

DELIMITER ;
