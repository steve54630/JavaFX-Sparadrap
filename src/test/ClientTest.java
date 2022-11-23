package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exception.AppException;
import model.Adresse;
import model.Client;
import model.Medecin;
import model.Mutuelle;
import model.Specialiste;

class ClientTest {

	private static Adresse adresse;
	private static  Adresse adresse2;
	private static Adresse adresse3;
	private static Mutuelle mutuelle;
	private static Medecin medecin;
	private static Client clientTest;
	private static Specialiste specialiste;

	@BeforeAll
	static void initialize() throws AppException {
		adresse = new Adresse(0, "15", "Rue des Ponts", "54000", "Nancy");
		adresse2 = new Adresse(0, "30", "rue de Nancy", "54630",
				"Richardmenil");
		adresse3 = new Adresse(0, "9", "Rue Maurice Barres", "54000", "Nancy");
		mutuelle = new Mutuelle(0, "MGEN", adresse3, "3976", "acceuil@mgen.fr",
				"Meurthe-et-Moselle", 80);
		medecin = new Medecin(0, "Chastagner", "Nathalie", adresse,
				"03.83.40.25.97", "c.nathalie@hotmail.fr", "1562038064121782");
		specialiste = new Specialiste(0, "Thess", "François",
				adresse2, "03.83.35.19.76", "t.francois@orange.fr",
				"Dermatologie");
	}

	@Test
	void testSetDateDeNaissance() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", null, "steve54@wanadoo.fr", "2023-01-03",
					medecin, mutuelle);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur date : impossible d'être né aprés aujourd'hui"));
		}
	}
	
	@Test
	void testSetSecuriteSociale_secunull() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", null, "steve54@wanadoo.fr", "1990-01-03",
					medecin, mutuelle);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur client : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetSecuriteSociale_secerreur() {

		String[] test = { "16542", "abcd", "dkf654" };

		for (String string : test) {
			try {
				clientTest = new Client(0, "Retournay", "Steve", adresse2,
						"06.81.30.29.76", string, "steve54@wanadoo.fr",
						"1990-01-03", medecin, mutuelle);
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Format numéro de sécurité sociale : 13 chiffres"));
			}
		}
	}
	
	@Test
	void testSetSecuriteSociale_numsecuvide() {
		
			try {
				clientTest = new Client(0, "Retournay", "Steve", adresse2,
						"06.81.30.29.76", "", "steve54@wanadoo.fr",
						"1990-01-03", medecin, mutuelle);
			} catch (AppException e) {
				assert (e.getMessage().contains(
						"Erreur client : numéro de sécurité sociale non renseigné"));
			}
	}

	@Test
	void testSetMutuelle() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", "1900175127030", "steve54@wanadoo.fr",
					"1990-01-03", medecin, null);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur client : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetMedecin() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", "1900175127030", "steve54@wanadoo.fr",
					"1990-01-03", null, mutuelle);
		} catch (AppException e) {
			assert (e.getMessage()
					.contains("Erreur client : veuillez contacter le SAV"));
		}
	}

	@Test
	void testSetSpecialiste_ajoutmemespecialiste() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", "1900175119025", "steve54@wanadoo.fr",
					"1990-01-03", medecin, mutuelle);
			clientTest.setSpecialiste(specialiste);
			clientTest.setSpecialiste(specialiste);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			assert (e.getMessage().contains(
					"Specialiste présent dans les données du client"));
		}
	}
	
	@Test
	void testSetSpecialiste_ajoutspecialistenull() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", "1900175119025", "steve54@wanadoo.fr",
					"1990-01-03", medecin, mutuelle);
			clientTest.setSpecialiste(null);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			assert (e.getMessage().contains(
					"Erreur client : veuillez contacter le SAV"));
		}
	}

	@Test
	void testClient() {
		try {
			clientTest = new Client(0, "Retournay", "Steve", adresse2,
					"06.81.30.29.76", "1900175119025", "steve54@wanadoo.fr",
					"1990-01-03", medecin, mutuelle);
			clientTest.setSpecialiste(specialiste);
		} catch (AppException e) {
			e.printStackTrace();
		}
		assertEquals("Retournay", clientTest.getNom());
		assertEquals("Steve", clientTest.getPrenom());
		assertEquals(adresse2, clientTest.getAdresse());
		assertEquals("06.81.30.29.76", clientTest.getNumeroTelephone());
		assertEquals("1900175119025", clientTest.getSecuriteSociale());
		assertEquals("steve54@wanadoo.fr", clientTest.getEmail());
		assertEquals("1990-01-03", clientTest.dateToString());
		assertEquals(medecin, clientTest.getMedecin());
		assertEquals(mutuelle, clientTest.getMutuelle());
		assertTrue(clientTest.getSpecialiste().contains(specialiste));
		assertTrue(mutuelle.getClients().contains(clientTest));
		assertTrue(medecin.getPatients().contains(clientTest));
	}

}
