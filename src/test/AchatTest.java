package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Achat;
import model.Adresse;
import model.Client;
import model.Medecin;
import model.Medicament;
import model.Mutuelle;

class AchatTest {

	@SuppressWarnings("unused")
	@Test
	void testSetAcheteur() throws AppException {
		try {
			Achat achat = new Achat(null, null, "2022-11-16");
		} catch (AppException ae) {
			assert (ae.getMessage()
					.contains("Erreur achat : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetMedicaments() throws AppException {
		Adresse adresse = new Adresse(0, "15", "Rue des Ponts", "54000",
				"Nancy");
		Adresse adresse2 = new Adresse(0, "30", "rue de Nancy", "54630",
				"Richardmenil");
		Adresse adresse3 = new Adresse(0, "9", "Rue Maurice Barres", "54000",
				"Nancy");
		Mutuelle mutuelle1 = new Mutuelle(0, "MGEN", adresse3, "3976",
				"acceuil@mgen.fr", "Meurthe-et-Moselle", 80);
		Medecin medecin1 = new Medecin(0, "Chastagner", "Nathalie", adresse,
				"03.83.40.25.97", "c.nathalie@hotmail.fr", "1562038064121782");
		Client client1 = new Client(0, "Retournay", "Steve", adresse2,
				"06.81.30.29.76", "1900175127030", "steve54@wanadoo.fr",
				"1990-01-03", medecin1, mutuelle1);
		Achat achat = new Achat(null, client1, "2022-11-16");
		try {
			achat.setMedicaments(null, 0);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur achat : veuillez contacter le SAV"));
		}
	}

		@Test
	void testAchat() throws AppException {
		Adresse adresse = new Adresse(0, "15", "Rue des Ponts", "54000",
				"Nancy");
		Adresse adresse2 = new Adresse(0, "30", "rue de Nancy", "54630",
				"Richardmenil");
		Adresse adresse3 = new Adresse(0, "9", "Rue Maurice Barres", "54000",
				"Nancy");
		Mutuelle mutuelle1 = new Mutuelle(0, "MGEN", adresse3, "3976",
				"acceuil@mgen.fr", "Meurthe-et-Moselle", 80);
		Medecin medecin1 = new Medecin(0, "Chastagner", "Nathalie", adresse,
				"03.83.40.25.97", "c.nathalie@hotmail.fr", "1562038064121782");
		Client client1 = new Client(0, "Retournay", "Steve", adresse2,
				"06.81.30.29.76", "1900175127030", "steve54@wanadoo.fr",
				"1990-01-03", medecin1, mutuelle1);
		Medicament medicament = new Medicament(0, "Amoxicilline",
				"Antibiotique", 1, 60, "1953-05-02");
		Achat achat = new Achat(0, client1, "2022-11-07");
		achat.setMedicaments(medicament, 3);
		assertTrue(achat.getMedicaments().get(0).getNom()
				.contains("Amoxicilline"));
		assertEquals(0, achat.getId());
		assertEquals(3.0, achat.getPrixTotal());
		assertEquals(client1, achat.getAcheteur());
		assertEquals("2022-11-07", achat.dateToString());
		assertEquals("Achat", achat.getType());
	}

}
