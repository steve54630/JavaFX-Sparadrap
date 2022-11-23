package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Adresse;
import model.Mutuelle;

class MutuelleTest {

	private static Adresse adresse;
	private static Mutuelle mutuelle;

	@BeforeAll
	static void initialize() throws AppException {
		adresse = new Adresse(0, "9", "Rue Maurice Barres", "54000", "Nancy");
		mutuelle = new Mutuelle(0, "MGEN", adresse, "3976", "acceuil@mgen.fr",
				"Meurthe-et-Moselle", 80);
	}

	@Test
	void testSetNom_null() {
		try {
			mutuelle.setNom(null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur mutuelle : veuillez contacter le SAV"));
		}

	}

	@Test
	void testSetNom_erreurSaisi() {

		String[] test = { "65664", "546mdc", "a'b#", "ab" };

		for (String string : test) {

			try {
				mutuelle.setNom(string);
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Format nom : lettres, points et tirets (minimum 3 caractères)"));
			}
		}
	}

	@Test
	void testSetAdresse() {
		try {
			mutuelle.setAdresse(null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur mutuelle : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetTelephone_null() {
		try {
			mutuelle.setTelephone(null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur mutuelle : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetTelephone_erreurSaisi() {

		String[] test = { "397", "0681302976", "abc", "546def" };

		for (String string : test) {
			try {
				mutuelle.setTelephone(string);
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Format téléphone : 4 chiffres ou 0x.xx.xx.xx.xx (x étant un chiffre)"));
			}
		}
	}

	@Test
	void testSeteMail() {
		try {
			mutuelle.seteMail(null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur mutuelle : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSeteMail_erreurSaisi() {

		String[] test = { "@domaine.fr", "abcdomaine.fr", "abc@domaine",
				"abc@domaine.f", "abc@domaine.come", "a'b@domaine.fr",
				"ab@d'omaine.fr" };
		for (String string : test) {
			try {
				mutuelle.seteMail(string);
			} catch (AppException e) {
				assert (e.getMessage()
						.contains("Format E-Mail : abc@domaine.fr"));
			}
		}
	}

	@Test
	void testSetDepartement_erreurNull() {
		try {
			mutuelle.setDepartement(null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur mutuelle : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetDepartement_erreurSaisi() {

		String[] test = { "154", "abc564", "a'b#", "a" };

		for (String string : test) {
			try {
				mutuelle.setDepartement(string);
			} catch (AppException e) {
				assert (e.getMessage()
						.contains("Format département : lettres et tirets(-)"));
			}
		}
	}

	@Test
	void testSetRemboursement() {

		int[] test = { -5, 100 };

		for (int i : test) {
			try {
				mutuelle.setRemboursement(i);
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Erreur mutuelle : remboursement entre 1 et 99"));
			}
		}
	}

	@Test
	void testSetClients() {
		try {
			mutuelle.ajouterClients(null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur mutuelle : veuillez contacter le SAV"));
		}
	}

	@Test
	void testMutuelle() {
		assertEquals("MGEN", mutuelle.getNom());
		assertEquals(0, mutuelle.getId());
		assertEquals(adresse, mutuelle.getAdresse());
		assertEquals("3976", mutuelle.getTelephone());
		assertEquals("acceuil@mgen.fr", mutuelle.geteMail());
		assertEquals("Meurthe-et-Moselle", mutuelle.getDepartement());
		assertEquals(80, mutuelle.getRemboursement());
		try {
			mutuelle.setTelephone("06.85.65.24.23");
			assertEquals("06.85.65.24.23", mutuelle.getTelephone());
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
