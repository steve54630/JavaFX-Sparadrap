package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Adresse;
import model.Medecin;

class MedecinTest {

	private static Adresse adresse;
	private static Medecin medecin;

	@BeforeAll
	static void initialize() throws AppException {
		adresse = new Adresse(0, "15", "Rue des Ponts", "54000", "Nancy");
	}

	@Test
	void testSetNumeroAggrement_erreurnull() {
		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr", null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur medecin = veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetNumeroAggrement_erreurNumero() {

		String[] test = { "1562038064121782l", "abc", "1564", "d5l6" };

		for (String string : test) {
			try {
				medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
						"03.83.40.25.97", "c.nathalie@hotmail.fr", string);
			} catch (AppException e) {
				assert (e.getMessage()
						.contains("Format numero d'agréément : 16 chiffres"));
			}
		}
	}

	@Test
	void testSetNom_null() {

		try {
			medecin = new Medecin(0, null, "Nathalie", adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : veuillez contacter le SAV"));
		}

	}

	@Test
	void testSetNom_erreurNom() {

		String[] test = { "652", "54&dh", "Sa", "anticonstitutuionellement" };

		for (String string : test) {
			try {
				medecin = new Medecin(0, string, "Nathalie", adresse,
						"03.83.40.25.97", "c.nathalie@hotmail.fr",
						"1562038064121782");
			} catch (AppException e) {
				assert (e.getMessage()
						.contains("Format nom : seulement des lettres"));
			}
		}}
	
	@Test
	void testSetNom_nomvide() {

		try {
			medecin = new Medecin(0, "", "Nathalie", adresse, "03.83.40.25.97",
					"c.nathalie@hotmail.fr", "1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : nom non renseigné"));
		}

	}

	@Test
	void testSetPrenom_null() {

		try {
			medecin = new Medecin(0, "Chastagner", null, adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : veuillez contacter le SAV"));
		}

	}

	@Test
	void testSetPrenom_erreurPrenom() {

		String[] test = { "652", "54&dh", "Sa", "anticonstitutuionellement" };

		for (String string : test) {
			try {
				medecin = new Medecin(0, "Chastagner", string, adresse,
						"03.83.40.25.97", "c.nathalie@hotmail.fr",
						"1562038064121782");
			} catch (AppException e) {
				assert (e.getMessage()
						.contains("Format prénom : seulement des lettres"));
			}
		}
	}

	@Test
	void testSetPrenom_prenomvide() {
		try {
			medecin = new Medecin(0, "Chastagner", "", adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : prénom non renseigné"));
		}

	}

	@Test
	void testSetAdresse() {

		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", null,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : veuillez contacter le SAV"));
		}

	}

	@Test
	void testSetNumeroTelephone_erreurNull() {

		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse, null,
					"c.nathalie@hotmail.fr", "1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : veuillez contacter le SAV"));

		}
	}

	@Test
	void testSetNumeroTelephone_erreurSaisi() {

		String[] test = { "0681302976", "0b.cd.ef.gh.ij", "3946",
				"06-81-30-29-76" };

		for (String string : test) {
			try {
				medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
						string, "c.nathalie@hotmail.fr", "1562038064121782");
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Format numéro téléphone : 0x.xx.xx.xx.xx (x étant des chiffres"));
			}
		}
	}

	@Test
	void testSetNumeroTelephone_telvide() {
		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse, "",
					"c.nathalie@hotmail.fr", "1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage().contains(
					"Erreur personne : numéro de téléphone non renseigné"));
		}

	}

	@Test
	void testSetEmail_erreurNull() {
		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
					"03.83.40.25.97", null, "1562038064121782");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur personne : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetEmail_erreurSaisi() {

		String[] test = { "@domaine.fr", "abcdomaine.fr", "abc@domaine",
				"abc@domaine.f", "abc@domaine.come", "a'b@domaine.fr",
				"ab@d'omaine.fr" };

		for (String string : test) {
			try {
				medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
						"03.83.40.25.97", string, "1562038064121782");
			} catch (Exception e) {
				assert (e.getMessage()
						.contains("Format e-mail : abc@domaine.fr"));
			}
		}
	}

	@Test
	void testSetEmail_emailvide() {
		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
					"03.83.40.25.97", "", "1562038064121782");
		} catch (Exception e) {
			assert (e.getMessage()
					.contains("Erreur personne : email non renseigné"));
		}

	}

	@Test
	void testPatient() {
		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
			medecin.ajouterPatient(null);
		} catch (Exception e) {
			assert (e.getMessage()
					.contains("Erreur medecin = veuillez contacter le SAV"));
		}
	}

	@Test
	void testOrdonnance() {
		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
			medecin.ajouterOrdonnance(null);
		} catch (Exception e) {
			assert (e.getMessage()
					.contains("Erreur medecin = veuillez contacter le SAV"));
		}
	}

	@Test
	void testMedecin() {

		try {
			medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
					"03.83.40.25.97", "c.nathalie@hotmail.fr",
					"1562038064121782");
		} catch (AppException e) {
			e.printStackTrace();
		}

		assertEquals(0, medecin.getId());
		assertEquals("1562038064121782", medecin.getNumeroAggrement());

	}

}
