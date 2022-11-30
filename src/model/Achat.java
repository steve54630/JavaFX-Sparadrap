package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import exception.AppException;

/**
 * Objet achat de la pharmacie
 * 
 * @author SRet
 */
public class Achat {

	private Integer id;
	private Client acheteur;
	private LocalDate date;
	private String type = getClass().getName().replaceAll("model.", "");
	private ArrayList<Medicament> medicaments = new ArrayList<>();

	/**
	 * setter pour celui qui realise l'achat
	 * 
	 * @param acheteur = celui qui realise l'achat
	 * @throws AppException : acheteur nul
	 */
	public void setAcheteur(Client acheteur) throws AppException {
		if (acheteur == null)
			throw new AppException("Erreur achat : veuillez contacter le SAV");
		this.acheteur = acheteur;
	}

	/**
	 * getter pour celui qui realise l'achat
	 * 
	 * @return le client qui realise l'achat
	 */
	public Client getAcheteur() {
		return acheteur;
	}

	/**
	 * setter pour ajouter un medicaments a l'achat
	 * 
	 * @param medicament : medicament choisi par l'acheteur
	 * @param quantite   : quantite du medicament voulu
	 * @throws AppException : medicament nul
	 */
	public void setMedicaments(Medicament medicament, int quantite)
			throws AppException {
		if (medicament == null)
			throw new AppException("Erreur achat : veuillez contacter le SAV");
		medicament.setQuantite(quantite);
		this.medicaments.add(medicament);
	}

	/**
	 * getter pour la liste des medicaments de l'achat
	 * 
	 * @return la liste des medicaments de l'achat
	 */
	public ArrayList<Medicament> getMedicaments() {
		return medicaments;
	}

	/**
	 * getter pour le prix total de l'achat
	 * 
	 * @return le prix total de l'achat
	 */
	public double getPrixTotal() {
		double prixTotal = 0;
		for (Medicament medicament : medicaments) {
			prixTotal = prixTotal
					+ (medicament.getQuantite() * medicament.getPrix());
		}
		return prixTotal;
	}

	/**
	 * setter pour l'id de l'achat en base de donnees
	 * 
	 * @param id : id de l'achat
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * getter pour l'id de l'achat en base de donnees
	 * 
	 * @return l'id de l'achat
	 */
	public int getId() {
		return id;
	}

	/**
	 * setter pour la date de l'achat
	 * 
	 * @param date : date a set
	 */
	public void setDate(String date) {
		this.date = LocalDate.parse(date);
	}

	/**
	 * getter pour la date de l'achat
	 * 
	 * @return la date de l'achat
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Methode pour recuperer une {@link LocalDate} au format "yyyy-MM-dd"
	 * 
	 * @return la date au format voulu
	 */
	public String dateToString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return this.getDate().format(formatter);
	}

	public String getType() {
		return type;
	}

	/**
	 * Constructeur de l'achat
	 * 
	 * @param id       : id de l'achat
	 * @param acheteur : celui qui realise l'achat
	 * @param Date     : date de l'achat
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Achat(Integer id, Client acheteur, String Date) throws AppException {
		this.setId(id);
		this.setAcheteur(acheteur);
		this.setDate(Date);
	}

	/**
	 * Transformer l'objet obtenu en {@link String} lisible par l'utilisateur
	 */
	@Override
	public String toString() {
		LocalDate today = LocalDate.now();
		return "Acheteur = " + this.acheteur.getNom() + " "
				+ this.acheteur.getPrenom() + "\n" + "Agé de "
				+ (today.getYear() - this.acheteur.getDateNaissance().getYear())
				+ " ans\n" + "\nDate = "
				+ this.dateToString() + "\nPrix total = " + this.getPrixTotal()
				+ " €";
	}

}
