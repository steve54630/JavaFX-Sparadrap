package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import exception.AppException;

/**
 * Objet medicament de la pharmacie
 * 
 * @author SRet
 */
public class Medicament {

	private Integer id;
	private String nom;
	private String categorie;
	private double prix;
	private double prixReduit;
	private LocalDate dateCirculation;
	private int quantite;
	private int stock;
	/**
	 * setter pour le nom du medicament
	 * 
	 * @param nom : nom du medicament
	 * @throws AppException : erreur dans la saisi du nom
	 */
	public void setNom(String nom) throws AppException {
		if (nom == null)
			throw new AppException(
					"Erreur médicament : veuillez contacter le SAV");
		if (!Pattern.matches("[A-Za-zéèçâäëêù ]{3,}", nom))
			throw new AppException(
					"Format nom : lettres seulement (3 minimum)");
		this.nom = nom;
	}

	/**
	 * getter pour le nom du medicament
	 * 
	 * @return le nom du medicament en {@link String}
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * setter pour la categorie du medicament
	 * 
	 * @param categorie : categorie du medicament en {@link String}
	 * @throws AppException : erreur dans la saisie de la categorie
	 */
	public void setCategorie(String categorie) throws AppException {
		if (categorie == null)
			throw new AppException(
					"Erreur médicament : veuillez contacter le SAV");
		if (!Pattern.matches("[A-Za-zéèçâäëêù ]{3,}", categorie))
			throw new AppException(
					"Format catégorie : lettres seulement (3 minimum)");
		this.categorie = categorie;
	}

	/**
	 * getter pour la categorie du medicament
	 * 
	 * @return la categorie du medicament en {@link String}
	 */
	public String getCategorie() {
		return categorie;
	}

	/**
	 * setter pour le prix du medicament
	 * 
	 * @param prix : prix du medicament
	 * @throws AppException : refus d'un prix inferieur ou egal a 0
	 */
	public void setPrix(double prix) throws AppException {
		if (prix <= 0)
			throw new AppException("Erreur médicament : prix négatif ou null");
		this.prix = prix;
	}

	/**
	 * getter pour le prix du medicament
	 * 
	 * @return le prix du medicament
	 */
	public double getPrix() {
		return prix;
	}

	/**
	 * getter pour recuperer le prix avec la reduction mutuelle
	 * 
	 * @return prix avec la reduction
	 */
	public double getPrixReduit() {
		return prixReduit;
	}

	/** setter pour le prix avec la reduction d'une mutuelle
	 * 
	 * @param mutuelle : mutuelle a prendre en compte pour la reduction 
	 * @throws AppException : refus d'un prix inferieur ou egal a 0
	 */
	public void setPrixReduit(Mutuelle mutuelle) throws AppException {
		double nouveauPrix = ((100 - mutuelle.getRemboursement())
				* this.getPrix()) / 100;
		if (nouveauPrix <= 0)
			throw new AppException("Erreur médicament : prix négatif ou null");
		this.prixReduit = nouveauPrix;
	}

	/**
	 * setter pour la date de mise en circulation
	 * 
	 * @param date  : date de mise en circulation
	 * @throws AppException : erreur dans la saisi de la date
	 */
	public void setDateCirculation(String date)
			throws AppException, DateTimeParseException {
		this.dateCirculation = LocalDate.parse(date);
	}

	/**
	 * getter pour la date de mise en circulation
	 * 
	 * @return date de mise en circulation au format {@link GregorianCalendar}
	 */
	public LocalDate getDateCirculation() {
		return dateCirculation;
	}

	/**
	 * Methode pour recuperer une {@link LocalDate} au format "yyyy-MM-dd"
	 * 
	 * @return la date au format voulu
	 */
	public String dateToString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return this.getDateCirculation().format(formatter);
	}

	/**
	 * getter pour le stock de medicaments de la pharmacie
	 * 
	 * @return le stock de medicaments dans la pharmacie
	 */
	public int getQuantite() {
		return quantite;
	}

	/**
	 * setter pour le stock de medicaments de la pharmacie
	 * 
	 * @param quantite : stock de medicaments de la pharmacie
	 * @throws AppException : un stock ne peut pas etre negatif
	 */
	public void setQuantite(int quantite) throws AppException {
		if (quantite < 0)
			throw new AppException("Erreur médicament : quantité négative");
		this.quantite = quantite;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) throws AppException {
		if (stock < 0)
			throw new AppException("Erreur médicament : stock négatif");
		this.stock = stock;
	}

	/**
	 * getter pour l'id du medicament dans la base de donnees
	 * 
	 * @return l'id du medicament
	 */
	public int getId() {
		return id;
	}

	/**
	 * setter pour l'id du medicament
	 * 
	 * @param id : id du medicament
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Constructeur pour le medicament
	 * 
	 * @param id        : id du medicament
	 * @param nom       : nom du medicament
	 * @param categorie : categorie du medicament
	 * @param prix      : prix du medicament
	 * @param stock     : stock du medicament
	 * @param date      :  date de mise en circulation
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Medicament(Integer id, String nom, String categorie, double prix,
			int stock, String date) throws AppException {
		this.setId(id);
		this.setNom(nom);
		this.setCategorie(categorie);
		this.setPrix(prix);
		this.setDateCirculation(date);
		this.setStock(stock);
	}

	/**
	 * Recuperer les donnees du medicament qui nous interessent
	 */
	@Override
	public String toString() {
		return this.getNom() + ", " + this.getCategorie() + ", Prix= "
				+ this.getPrix() + "€";
	}

}
