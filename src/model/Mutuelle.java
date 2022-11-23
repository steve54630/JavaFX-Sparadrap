package model;

import java.util.ArrayList;
import java.util.regex.Pattern;

import exception.AppException;

/**
 * Objet mutuelle de la pharmacie
 * 
 * @author SRet
 */
public class Mutuelle {

	private int id;
	/**
	 * nom de la mutuelle
	 */
	private String nom;
	/**
	 * adresse de la mutuelle
	 */
	private Adresse adresse;
	/**
	 * departement de la mutuelle
	 */
	private String telephone;
	/**
	 * numero de telephone de la mutuelle
	 */
	private String eMail;
	/**
	 * email de la mutuelle
	 */
	private String departement;
	/**
	 * departement de la mutuelle
	 */
	private int remboursement;
	/**
	 * liste des clients
	 */
	private ArrayList<Client> clients = new ArrayList<>();

	/**
	 * getter pour le nom de la mutuelle
	 * 
	 * @return nom de la mutuelle
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * setter pour le nom de la mutuelle
	 * 
	 * @param nom : nom de la mutuelle
	 * @throws AppException : erreur de frappe
	 */
	public void setNom(String nom) throws AppException {
		if (nom == null)
			throw new AppException(
					"Erreur mutuelle : veuillez contacter le SAV");
		if (!Pattern.matches("[A-Za-zéèàçùêëî. ]{3,}", nom))
			throw new AppException(
					"Format nom : lettres, points et tirets (minimum 3 caractères)");
		char[] nomCorrige = nom.toCharArray();
		Character.toUpperCase(nomCorrige[0]);
		this.nom = new String(nomCorrige);
	}

	/**
	 * getter pour l'adresse de la mutuelle
	 * 
	 * @return {@link Adresse} de la mutuelle
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * setter de la mutuelle
	 * 
	 * @param adresse : {@link Adresse} de la mutuelle
	 * @throws AppException : absence d'adresse
	 */
	public void setAdresse(Adresse adresse) throws AppException {
		if (adresse == null)
			throw new AppException(
					"Erreur mutuelle : veuillez contacter le SAV");
		this.adresse = adresse;
	}

	/**
	 * getter du numero de telephone de la mutuelle
	 * 
	 * @return le numero de telephone de la mutuelle en {@link String}
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * setter pour le numero de telephone de la mutuelle
	 * 
	 * @param telephone : numero de telephone en {@link String}
	 * @throws AppException : format telephone non respecte
	 */
	public void setTelephone(String telephone) throws AppException {
		if (telephone == null)
			throw new AppException(
					"Erreur mutuelle : veuillez contacter le SAV");
		if (!Pattern.matches("[0-9]{4}", telephone) && !Pattern.matches(
				"[0][0-9][.][0-9]{2}[.][0-9]{2}[.][0-9]{2}[.][0-9]{2}",
				telephone))
			throw new AppException(
					"Format téléphone : 4 chiffres ou 0x.xx.xx.xx.xx (x étant un chiffre)");
		this.telephone = telephone;
	}

	/**
	 * getter pour l'email de la mutuelle
	 * 
	 * @return l'email de la mutuelle
	 */
	public String geteMail() {
		return eMail;
	}

	/**
	 * setter pour l'email de la mutuelle
	 * 
	 * @param eMail : email de la mutuelle en {@link String}
	 * @throws AppException : erreur lors de l'entree de l'email
	 */
	public void seteMail(String eMail) throws AppException {
		if (eMail == null)
			throw new AppException(
					"Erreur mutuelle : veuillez contacter le SAV");
		if (!Pattern.matches(
				"[a-zA-Z0-9_.-]{1,}@{1}[a-zA-Z0-9.-]{2,}[.]{1}[a-z]{2,3}",
				eMail))
			throw new AppException("Format E-Mail : abc@domaine.fr");
		this.eMail = eMail;
	}

	/**
	 * getter pour le departement de la mutuelle
	 * 
	 * @return le departement de la mutuelle en {@link String}
	 */
	public String getDepartement() {
		return departement;
	}

	/**
	 * setter pour le departement de la mutuelle
	 * 
	 * @param departement : departement de la mutuelle en {@link String}
	 * @throws AppException : erreur dans la saisi du departement
	 */
	public void setDepartement(String departement) throws AppException {
		if (departement == null)
			throw new AppException(
					"Erreur mutuelle : veuillez contacter le SAV");
		if (!Pattern.matches("[A-Za-z -]{2,}", departement))
			throw new AppException("Format département : lettres et tirets(-)");
		char[] nomCorrige = departement.toCharArray();
		Character.toUpperCase(nomCorrige[0]);
		this.departement = new String(nomCorrige);
	}

	/**
	 * getter du taux de remboursement de la mutuelle
	 * 
	 * @return le taux de remboursement de la mutuelle en %
	 */
	public int getRemboursement() {
		return remboursement;
	}

	/**
	 * setter pour le taux de remboursement de la mutuelle
	 * 
	 * @param remboursement : taux de remboursement de la mutuelle
	 * @throws AppException : taux de remboursement non compris entre 0 et 100
	 */
	public void setRemboursement(int remboursement) throws AppException {
		if (1 > remboursement || remboursement > 99)
			throw new AppException(
					"Erreur mutuelle : remboursement entre 1 et 99");
		this.remboursement = remboursement;
	}

	/**
	 * getter pour la liste des clients de la mutuelle
	 * 
	 * @return la liste des clients de la mutuelle
	 */
	public ArrayList<Client> getClients() {
		return clients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Constructeur de l'Objet de la mutuelle
	 * 
	 * @param id            : id de la mutuelle
	 * @param nom           : nom de la mutuelle
	 * @param adresse       : adresse de la mutuelle
	 * @param telephone     : telephone de la mutuelle
	 * @param eMail         : email de la mutuelle
	 * @param departement   : departement de la mutuelle
	 * @param remboursement : remboursement de la mutuelle
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Mutuelle(int id, String nom, Adresse adresse, String telephone,
			String eMail, String departement, int remboursement)
			throws AppException {

		this.setId(id);
		this.setNom(nom);
		this.setAdresse(adresse);
		this.setTelephone(telephone);
		this.seteMail(eMail);
		this.setDepartement(departement);
		this.setRemboursement(remboursement);
	}

	/**
	 * ajout d'un client a la mutuelle
	 * 
	 * @param client : {@link Client} de la mutuelle
	 * @throws AppException : aucun client selectionne
	 */
	public void ajouterClients(Client client) throws AppException {
		if (client == null)
			throw new AppException(
					"Erreur mutuelle : veuillez contacter le SAV");
		this.clients.add(client);
	}

	/**
	 * Outil pour recuperer le nom de la mutuelle
	 */
	@Override
	public String toString() {
		return nom;
	}

}
