package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Adresse;
import model.Medicament;
import model.Mutuelle;

class MedicamentTest {

	private Medicament medicament;

	@Test
	void testSetNom_erreurNull() {
		try {
			medicament = new Medicament(0, null, "Antibiotique", 1, 60,
					"1953-05-02");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur médicament : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetNom_erreurNom() {

		String[] test = { "123", "abc,", "teg154", "ab" };

		for (String string : test) {
			try {
				medicament = new Medicament(0, string, "Antibiotique", 1, 60,
						"1953-05-02");
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Format nom : lettres seulement (3 minimum)"));
			}
		}
	}

	@Test
	void testSetCategorie_erreurNull() {
		try {
			medicament = new Medicament(0, "Amoxilline", null, 1, 60,
					"1953-05-02");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur médicament : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetCategorie_erreurNom() {
		String[] test = { "123", "abc,", "teg154", "ab" };

		for (String string : test) {
			try {
				medicament = new Medicament(0, "Amoxilline", string, 1, 60,
						"1953-05-02");
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Format catégorie : lettres seulement (3 minimum)"));
			}
		}
	}

	@Test
	void testSetPrix() {

		int[] test = { -1, 0 };
		for (int prix : test) {
			try {
				medicament = new Medicament(0, "Amoxilline", "Antibiotique",
						prix, 60, "1953-05-02");
			} catch (AppException e) {
				assert (e.getMessage()
						.contains("Erreur médicament : prix négatif ou null"));
			}
		}
	}

	@Test
	void testSetStock() {
		try {
			medicament = new Medicament(0, "Amoxilline", "Antibiotique", 1, -5,
					"1953-05-02");
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur médicament : stock négatif"));
		}
	}

	@Test
	void testMedicament() {
		
		Mutuelle mutuelle;
		Adresse adresse;
		try {
			medicament = new Medicament(0, "Amoxicilline",
					"Antibiotique", 1, 5, "1953-05-02");
			adresse = new Adresse(0, "9", "Rue Maurice Barres", "54000", "Nancy");
			mutuelle = new Mutuelle(0, "MGEN", adresse, "3976", "acceuil@mgen.fr",
					"Meurthe-et-Moselle", 80);
			medicament.setPrixReduit(mutuelle);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(0, medicament.getId());
		assertEquals("Amoxicilline", medicament.getNom());
		assertEquals("Antibiotique", medicament.getCategorie());
		assertEquals(1, medicament.getPrix());
		assertEquals(5, medicament.getQuantite());
		assertEquals("1953-05-02", medicament.dateToString());
		assertEquals(0.2,medicament.getPrixReduit());
	}

}
