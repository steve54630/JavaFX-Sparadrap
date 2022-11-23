package model;

import java.util.regex.Pattern;

import exception.AppException;

/**
 * Objet specialiste de la pharmacie
 * 
 * @author SRet
 */
public class Specialiste extends Personne {

	/**
	 * Specialite du specialiste
	 */
	private String specialite;

	/**
	 * getter pour la specialite du specialiste
	 * 
	 * @return Specialite du Specialiste
	 */
	public String getSpecialite() {
		return specialite;
	}

	/**
	 * setter pour la specialite du specialiste
	 * 
	 * @param specialite : specialite du specialiste
	 * @throws AppException : saisi incorrecte
	 */
	public void setSpecialite(String specialite) throws AppException {
		if (specialite == null)
			throw new AppException(
					"Erreur specialiste : veuillez contacter le SAV");
		if (!Pattern.matches("[A-Za-zéèàçùêëî. ]{3,}", specialite))
			throw new AppException("Format spécialité : lettres uniquement");
		this.specialite = specialite;
	}

	/**
	 * Constructeur du specialiste
	 * 
	 * @param id              : id du specialiste
	 * @param nom             : nom du specialiste
	 * @param prenom          : prenom du specialiste
	 * @param adresse         : adresse du specialiste
	 * @param numeroTelephone : numero de telephone du specialiste
	 * @param email           : E-mail du specialiste
	 * @param specialite      : specialite du specialiste
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Specialiste(int id, String nom, String prenom, Adresse adresse,
			String numeroTelephone, String email, String specialite)
			throws AppException {
		super(id, nom, prenom, adresse, numeroTelephone, email);
		this.setSpecialite(specialite);
	}

	/**
	 * Permet de recuperer le nom, prenom et specialite du specialiste
	 */
	@Override
	public String toString() {
		return "Docteur " + this.getNom() + " " + this.getPrenom() + "" + " - "
				+ this.getSpecialite();
	}

}
