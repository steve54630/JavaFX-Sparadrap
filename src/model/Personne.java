package model;

import java.util.regex.Pattern;

import exception.AppException;

/**
 * Modele abstrait pour la classe personne
 * 
 * @author SRet
 */
public abstract class Personne {

	/**
	 * identifiant pour "serialize" un objet
	 */
	private Integer id;
	/**
	 * nom d'une personne
	 */
	protected String nom;
	/**
	 * prenom d'une personne
	 */
	private String prenom;
	/**
	 * adresse d'une personne
	 */
	private Adresse adresse;
	/**
	 * numero de telephone d'une personne
	 */
	private String numeroTelephone;
	/**
	 * email d'une personne
	 */
	private String eMail;

	/**
	 * getter pour nom
	 * 
	 * @return le nom de la personne
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * setter pour nom
	 * 
	 * @param nom : nom de la personne
	 * @throws AppException : obligatoirement une majuscule et que des lettres
	 */
	public void setNom(String nom) throws AppException {
		if (nom == null)
			throw new AppException(
					"Erreur personne : veuillez contacter le SAV");
		if (nom.equals(""))
			throw new AppException("Erreur personne : nom non renseigné");
		if (!Pattern.matches("[A-Za-zâôéèçà]{3,15}", nom))
			throw new AppException("Format nom : seulement des lettres");
		char[] nomCorrige = nom.toCharArray();
		Character.toUpperCase(nomCorrige[0]);
		this.nom = new String(nomCorrige);
	}

	/**
	 * getter pour le prenom
	 * 
	 * @return prenom : prenom de la personne
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * setter pour le nom de la personne
	 * 
	 * @param prenom : prenom de la personne
	 * @throws AppException : obligatoirement une majuscule et que des lettres
	 */
	public void setPrenom(String prenom) throws AppException {
		if (prenom == null)
			throw new AppException(
					"Erreur personne : veuillez contacter le SAV");
		if (prenom.equals(""))
			throw new AppException("Erreur personne : prénom non renseigné");
		if (!Pattern.matches("[A-Za-zâôéèçà]{3,15}", prenom))
			throw new AppException("Format prénom : seulement des lettres");
		char[] prenomCorrige = prenom.toCharArray();
		Character.toUpperCase(prenomCorrige[0]);
		this.prenom = new String(prenomCorrige);
	}

	/**
	 * getter pour l'adresse d'une personne
	 * 
	 * @return {@link Adresse} d'une personne
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * setter pour l'adresse d'une personne
	 * 
	 * @param adresse : adresse d'une personne
	 * @throws AppException : aucune adresse n'est enregistre pour le client
	 */
	public void setAdresse(Adresse adresse) throws AppException {
		if (adresse == null)
			throw new AppException(
					"Erreur personne : veuillez contacter le SAV");
		this.adresse = adresse;
	}

	/**
	 * getter pour le numero de telephone
	 * 
	 * @return le numero de telephone en {@link String}
	 */
	public String getNumeroTelephone() {
		return numeroTelephone;
	}

	/**
	 * setter pour le numero de telephone
	 * 
	 * @param numeroTelephone : numero de telephone
	 * @throws AppException : format du numero demande xx.xx.xx.xx.xx
	 */
	public void setNumeroTelephone(String numeroTelephone) throws AppException {
		if (numeroTelephone == null)
			throw new AppException(
					"Erreur personne : veuillez contacter le SAV");
		if (numeroTelephone.equals(""))
			throw new AppException(
					"Erreur personne : numéro de téléphone non renseigné");
		if (!Pattern.matches(
				"[0][0-9][.][0-9]{2}[.][0-9]{2}[.][0-9]{2}[.][0-9]{2}",
				numeroTelephone))
			throw new AppException(
					"Format numéro téléphone : 0x.xx.xx.xx.xx (x étant des chiffres");
		this.numeroTelephone = numeroTelephone;
	}

	/**
	 * getter pour l'email d'une personne
	 * 
	 * @return l'email d'une personne en {@link String}
	 */
	public String getEmail() {
		return eMail;
	}

	/**
	 * setter pour l'email d'une personne
	 * 
	 * @param email : email d'une personne en {@link String}
	 * @throws AppException : format email "Un E-Mail s'ecrit au format
	 *                      abc@domaine.fr."
	 */
	public void setEmail(String email) throws AppException {
		if (email == null)
			throw new AppException(
					"Erreur personne : veuillez contacter le SAV");
		if (email.equals(""))
			throw new AppException("Erreur personne : email non renseigné");
		String regex = "[a-zA-Z0-9_.-]{1,}@{1}[a-zA-Z0-9.-]{2,}[.]{1}[a-z]{2,3}";
		if (!Pattern.matches(regex, email))
			throw new AppException("Format e-mail : abc@domaine.fr");
		this.eMail = email;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Constructeur abstrait de personne
	 * 
	 * @param id        	  :	id de la personne
	 * @param nom             : nom de la personne;
	 * @param prenom          : prenom de la personne;
	 * @param adresse         : adresse de la personne;
	 * @param numeroTelephone : numero de telephone de la personne;
	 * @param email           : email de la personne
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Personne(Integer id, String nom, String prenom, Adresse adresse,
			String numeroTelephone, String email) throws AppException {
		this.setId(id);
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setAdresse(adresse);
		this.setNumeroTelephone(numeroTelephone);
		this.setEmail(email);
	}

	/**
	 * methode abstraite pour transformer la personne en texte
	 */
	@Override
	public abstract String toString();

}
