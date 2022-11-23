package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.regex.Pattern;

import exception.AppException;

/**
 * Objet client de la pharmacie
 * 
 * @author SRet
 *
 */

public class Client extends Personne {

	/**
	 * date de naissance du client
	 */
	private LocalDate dateNaissance;
	/**
	 * mutuelle du client
	 */
	private Mutuelle mutuelle;
	/**
	 * medecin du client
	 */
	private Medecin medecin;
	/**
	 * liste des specialistes lies au client
	 */
	private ArrayList<Specialiste> specialiste = new ArrayList<>();;
	/**
	 * numero de securite sociale du client
	 */
	private String securiteSociale;

	/**
	 * getter pour le numero de securite sociale
	 * 
	 * @return le numero de securite sociale
	 */
	public String getSecuriteSociale() {
		return securiteSociale;
	}

	/**
	 * setter pour le numero de securite sociale
	 * 
	 * @param securiteSociale : numero de securite sociale
	 * @throws AppException : contient obligatoirement 15 chiffres
	 */
	public void setSecuriteSociale(String securiteSociale) throws AppException {
		if (securiteSociale == null)
			throw new AppException("Erreur client : veuillez contacter le SAV");
		if (securiteSociale.equals(""))
			throw new AppException(
					"Erreur client : numéro de sécurité sociale non renseigné");
		if (!Pattern.matches("[0-9]{13}", securiteSociale))
			throw new AppException(
					"Format numéro de sécurité sociale : 13 chiffres");
		this.securiteSociale = securiteSociale;
	}

	/**
	 * getter pour la date de naissance
	 * 
	 * @return la date de naissance au format {@link LocalDate}
	 */
	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	/**
	 * Methode pour recuperer une {@link LocalDate} au format "dd/MM/yyyy"
	 * 
	 * @return la date au format voulu
	 */
	public String dateToString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return this.getDateNaissance().format(formatter);
	}

	/**
	 * setter pour la date de naissance
	 * 
	 * @param date  : date de naissance
	 * @throws AppException  : impossible d'etre ne apres le jour actuel
	 */
	public void setDateDeNaissance(String date) throws AppException {
		LocalDate datechoisi = LocalDate.parse(date);
		if (datechoisi.compareTo(LocalDate.now()) > 0)
			throw new AppException(
					"Erreur date : impossible d'être né aprés aujourd'hui");
		this.dateNaissance = datechoisi;
	}

	/**
	 * getter pour la mutuelle
	 * 
	 * @return le parametre {@link Mutuelle} de la classe
	 */
	public Mutuelle getMutuelle() {
		return mutuelle;
	}

	/**
	 * setter pour la mutuelle
	 * 
	 * @param mutuelle : {@link Mutuelle} de la classe
	 * @throws AppException : aucune mutuelle est ajoute
	 */
	public void setMutuelle(Mutuelle mutuelle) throws AppException {
		if (mutuelle == null)
			throw new AppException("Erreur client : veuillez contacter le SAV");
		this.mutuelle = mutuelle;
	}

	/**
	 * getter pour le medecin du client
	 * 
	 * @return le {@link Medecin} du client
	 */
	public Medecin getMedecin() {
		return medecin;
	}

	/**
	 * setter pour le medecin du client
	 * 
	 * @param medecin : choix du medecin du client au format {@link Medecin}
	 * @throws AppException : erreur d'ajout
	 */
	public void setMedecin(Medecin medecin) throws AppException {
		if (medecin == null)
			throw new AppException("Erreur client : veuillez contacter le SAV");
		this.medecin = medecin;
	}

	/**
	 * getter pour la liste des specialistes lies au client
	 * 
	 * @return l'ArrayList des {@link Specialiste} du client
	 */
	public ArrayList<Specialiste> getSpecialiste() {
		return specialiste;
	}

	/**
	 * setter pour ajouter un specialiste a la liste du client
	 * 
	 * @param specialiste : {@link Specialiste} à ajouter au client
	 * @throws AppException : le specialiste existe deja dans les donnees du
	 *                      client
	 */
	public void setSpecialiste(Specialiste specialiste) throws AppException {
		if (specialiste == null)
			throw new AppException("Erreur client : veuillez contacter le SAV");
		if (this.getSpecialiste().contains(specialiste))
			throw new AppException(
					"Specialiste présent dans les données du client");
		this.specialiste.add(specialiste);
	}

	/**
	 * Constructeur du client
	 * 
	 * @param id 			  : id du client
	 * @param nom             : nom du client
	 * @param prenom          : prenom du client
	 * @param adresse         : adresse du client
	 * @param numeroTelephone : numero de telephone du client
	 * @param securiteSociale : numero de securite sociale du client
	 * @param email           : email du client
	 * @param date            : date de naissance du client
	 * @param medecin         : Medecin du client
	 * @param mutuelle        : mutuelle du client
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Client(Integer id, String nom, String prenom, Adresse adresse,
			String numeroTelephone, String securiteSociale, String email,
			String date, Medecin medecin, Mutuelle mutuelle)
			throws AppException {
		super(id, nom, prenom, adresse, numeroTelephone, email);
		// Constructeur de la classe client
		this.setDateDeNaissance(date);
		this.setSecuriteSociale(securiteSociale);
		this.setMutuelle(mutuelle);
		this.getMutuelle().ajouterClients(this);
		this.setMedecin(medecin);
		this.getMedecin().ajouterPatient(this);
	}

	/**
	 * Renvoie les donnees du client sous forme de {@link String}
	 */
	@Override
	public String toString() {

		return this.getNom() + " " + this.getPrenom();
	}
}
