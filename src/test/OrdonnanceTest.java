package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Adresse;
import model.Client;
import model.Medecin;
import model.Medicament;
import model.Mutuelle;
import model.Ordonnance;
import model.Specialiste;

class OrdonnanceTest {

	private static Adresse adresse;
	private static Adresse adresse2;
	private static Adresse adresse3;
	private static Mutuelle mutuelle1;
	private static Medecin medecin1;
	private static Client client1;
	private static Medicament medicament;
	private static Ordonnance achat;
	private static Specialiste specialiste;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		adresse = new Adresse(0, "15", "Rue des Ponts", "54000", "Nancy");
		adresse2 = new Adresse(0, "30", "rue de Nancy", "54630",
				"Richardmenil");
		adresse3 = new Adresse(0, "9", "Rue Maurice Barres", "54000", "Nancy");
		mutuelle1 = new Mutuelle(0, "MGEN", adresse3, "3976", "acceuil@mgen.fr",
				"Meurthe-et-Moselle", 80);
		medecin1 = new Medecin(0, "Chastagner", "Nathalie", adresse,
				"03.83.40.25.97", "c.nathalie@hotmail.fr", "1562038064121782");
		client1 = new Client(0, "Retournay", "Steve", adresse2,
				"06.81.30.29.76", "1900175127030", "steve54@wanadoo.fr",
				"1990-01-03", medecin1, mutuelle1);
		medicament = new Medicament(0, "Amoxicilline", "Antibiotique", 1, 60,
				"1953-05-02");
		achat = new Ordonnance(0, client1, medecin1, "2022-11-07");
		specialiste = new Specialiste(0, "Titor", "John", adresse2,
				"06.65.20.40.32", "j.titor@hotmail.fr", "Urologie");
	}

	@Test
	void testSetMedicaments() {
		try {
			achat.setMedicaments(null, 0);
		} catch (Exception e) {
			assert (e.getMessage()
					.contains("Erreur ordonnance : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetMedecin() {
		try {
			achat.setMedecin(null);
		} catch (Exception e) {
			assert (e.getMessage()
					.contains("Erreur ordonnance : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetSpecialiste() {
		try {
			achat.setSpecialiste(null);
		} catch (Exception e) {
			assert (e.getMessage()
					.contains("Erreur ordonnance : veuillez contacter le SAV"));
		}
	}

	@Test
	void testOrdonnance() {
		try {
			achat.setMedicaments(medicament, 1);
			achat.setSpecialiste(specialiste);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		assertEquals(0, achat.getId());
		assertEquals(client1, achat.getAcheteur());
		assertEquals(medecin1, achat.getMedecin());
		assertEquals("2022-11-07", achat.dateToString());
		assertTrue(achat.getMedicaments().get(0).getId() == medicament.getId());
		assertEquals(0.2,achat.getPrixTotal());
		assertTrue(achat.getSpecialiste().equals(specialiste));
		assertTrue(medecin1.getOrdonnancesPrescrites().contains(achat));
	}

}
