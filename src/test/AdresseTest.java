package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Adresse;

class AdresseTest {

	@Test
	void testSetNumero() {
		String numero = null;

		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, numero, "rue", "54000",
					"Nancy");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur adresse : Veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetNumero_erreursaisi() {
		String numero = "deux";

		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, numero, "rue", "54000",
					"Nancy");
		} catch (AppException e) {
			assert (e.getMessage().contains(
					"Format numéro de rue : xxx (x étant des chiffres)"));
		}
	}

	@Test
	void testSetNumero_nomvide() {
		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "", "rue", "54000", "Nancy");
		} catch (AppException e) {
			assert (e.getMessage().contains(
					"Erreur adresse : numero de l'adresse non renseigné"));
		}
	}

	@SuppressWarnings("unused")
	@Test
	void testSetRue() {
		String rue = null;

		try {
			Adresse adresse = new Adresse(null, "20", rue, "54000", "Nancy");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur adresse : Veuillez contacter le SAV"));
		}
	}

	@SuppressWarnings("unused")
	@Test
	void testSetRue_erreurNom() {
		String rue = "rué de Nancy";

		try {
			Adresse adresse = new Adresse(null, "20", rue, "54000", "Nancy");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur adresse : nom de rue incorrect"));
		}
	}

	@Test
	void testSetRue_nomvide() {
		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "20", "", "54000", "Nancy");
		} catch (AppException e) {
			assert (e.getMessage().contains(
					"Erreur adresse : rue de l'adresse non renseignée"));
		}
	}

	@Test
	void testSetCodePostal() {
		String codePostal = null;

		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "20", "rue de Nancy",
					codePostal, "Nancy");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur adresse : Veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetCodePostal_erreurSaisi() {
		String codePostal = "rué";

		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "20", "rue de Nancy",
					codePostal, "Nancy");
		} catch (AppException e) {
			assert (e.getMessage().contains("Format code postal : 5 chiffres"));
		}
	}

	@Test
	void testSetCodePostal_nomvide() {
		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "20", "rue de Nancy", "",
					"Nancy");
		} catch (AppException e) {
			assert (e.getMessage().contains(
					"Erreur adresse : code postal de l'adresse non renseigné"));
		}
	}

	@Test
	void testSetVille() {
		String ville = null;

		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "30", "rue de Nancy", "54000",
					ville);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur adresse : Veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetVille_nomville() {
		String ville = "635";

		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "30", "rue de", "54000", ville);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur adresse : nom de ville incorrect"));
		}
	}

	@Test
	void testSetVille_nomvide() {
		try {
			@SuppressWarnings("unused")
			Adresse adresse = new Adresse(null, "30", "rue de", "54000", "");
		} catch (AppException e) {
			assert (e.getMessage().contains(
					"Erreur adresse : ville de l'adresse non renseignée"));
		}
	}

	@Test
	void testAdresse() throws AppException {
		Adresse adresse = new Adresse(null, "30", "rue de Nancy", "54630",
				"Richardménil");
		assertTrue(adresse.getNumero().contains("30"));
		assertTrue(adresse.getRue().contains("rue de Nancy"));
		assertTrue(adresse.getCodePostal().contains("54630"));
		assertTrue(adresse.getVille().contains("Richardménil"));
		assertTrue(adresse.getId() == null);
	}

}
