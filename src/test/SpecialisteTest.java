package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Adresse;
import model.Specialiste;

class SpecialisteTest {

	private static Adresse adresse;
	private static Specialiste specialiste;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		adresse = new Adresse(0, "30", "rue de Nancy", "54630", "Richardmenil");
		specialiste = new Specialiste(0, "Titor", "John", adresse,
				"06.65.20.40.32", "j.titor@hotmail.fr", "Urologie");
	}

	@Test
	void testSetSpecialite_erreurNull() {
		try {
			specialiste.setSpecialite(null);
		} catch (Exception e) {
			assert (e.getMessage().contains(
					"Erreur specialiste : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetSpecialite_erreurNom() {

		String[] test = { "123", "abc123", "a'b" };

		for (String string : test) {

			try {
				specialiste.setSpecialite(string);
			} catch (Exception e) {
				assert (e.getMessage()
						.contains("Format spécialité : lettres uniquement"));
			}
		}

	}

	@Test
	void testSpecialiste() {
		assertEquals("Urologie", specialiste.getSpecialite());
	}

}
