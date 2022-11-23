package model;

import java.util.regex.Pattern;

import exception.AppException;

/**
 * Objet adresse de l'application
 * 
 * @author SRet
 */
public class Adresse {

	/**
	 * identifiant pour "serialize" un objet
	 */
	private Integer id;
	/**
	 * numero de la rue
	 */
	private String numero;
	/**
	 * nom de la rue
	 */
	private String rue;
	/**
	 * code postal de la ville
	 */
	private String codePostal;
	/**
	 * ville ou est localise l'adresse
	 */
	private String ville;

	/**
	 * getter pour le numero de la rue
	 * 
	 * @return le numero de la rue
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * setter pour le numero de la rue
	 * 
	 * @param numero : numero de la rue
	 * @throws AppException : erreur dans la saisi
	 */
	public void setNumero(String numero) throws AppException {
		if (numero == null)
			throw new AppException(
					"Erreur adresse : Veuillez contacter le SAV");
		if (numero.equals(""))
			throw new AppException(
					"Erreur adresse : numero de l'adresse non renseigné");
		if (!Pattern.matches("[0-9]{1,3}", numero))
			throw new AppException(
					"Format numéro de rue : xxx (x étant des chiffres)");
		this.numero = numero;
	}

	/**
	 * getter pour le nom de la rue
	 * 
	 * @return le nom de la rue en {@link String}
	 */
	public String getRue() {
		return rue;
	}

	/**
	 * setter pour le numero de la rue
	 * 
	 * @param rue : nom de la rue
	 * @throws AppException : si l'utilisateur n'a pas entre de nom de rue
	 */
	public void setRue(String rue) throws AppException {
		if (rue == null)
			throw new AppException(
					"Erreur adresse : Veuillez contacter le SAV");
		if (rue.equals(""))
			throw new AppException(
					"Erreur adresse : rue de l'adresse non renseignée");
		if (!Pattern.matches("[a-zA-Z]{1,}[ ][0-9A-zéèçàù[-]' ]{1,}", rue))
			throw new AppException("Erreur adresse : nom de rue incorrect");
		this.rue = rue;
	}

	/**
	 * getter pour le code postal
	 * 
	 * @return le code postal en {@link String}
	 */
	public String getCodePostal() {
		return codePostal;
	}

	/**
	 * setter pour le code postal
	 * 
	 * @param codePostal : code postal de l'adresse
	 * @throws AppException : si il n'y a pas 5 chiffres dans le code postal
	 */
	public void setCodePostal(String codePostal) throws AppException {
		if (codePostal == null)
			throw new AppException(
					"Erreur adresse : Veuillez contacter le SAV");
		if (codePostal.equals(""))
			throw new AppException(
					"Erreur adresse : code postal de l'adresse non renseigné");
		if (!Pattern.matches("[0-9]{5}", codePostal))
			throw new AppException("Format code postal : 5 chiffres");
		this.codePostal = codePostal;
	}

	/**
	 * getter pour la ville
	 * 
	 * @return le nom de la ville en {@link String}
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * setter pour la ville
	 * 
	 * @param ville : nom de la ville
	 * @throws AppException : si la ville ne commence par une majuscule ou n'est
	 *                      pas present
	 */
	public void setVille(String ville) throws AppException {
		if (ville == null)
			throw new AppException(
					"Erreur adresse : Veuillez contacter le SAV");
		if (ville.equals(""))
			throw new AppException(
					"Erreur adresse : ville de l'adresse non renseignée");
		if (!Pattern.matches("[a-zA-Zàâéèêîôùûç-]{1,}", ville))
			throw new AppException("Erreur adresse : nom de ville incorrect");
		char[] villeCorrige = ville.toCharArray();
		Character.toUpperCase(villeCorrige[0]);
		this.ville = new String(villeCorrige);
	}

	/**
	 * getter pour l'id de l'adresse dans la base de donnees
	 * 
	 * @return l'id de l'adresse
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * setter pour l'id de l'adresse
	 * 
	 * @param id : id de l'adresse
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * constructeur pour la ville
	 * 
	 * @param id         : identifiant adresse
	 * @param numero     : numero de la rue
	 * @param rue        : nom de la rue
	 * @param codePostal : code postal
	 * @param ville      : nom de la ville
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Adresse(Integer id, String numero, String rue, String codePostal,
			String ville) throws AppException {
		this.setId(id);
		this.setNumero(numero);
		this.setRue(rue);
		this.setCodePostal(codePostal);
		this.setVille(ville);
	}

	/**
	 * Transforme les valeur de l'objet courant en {@link String}
	 */
	@Override
	public String toString() {
		return "" + this.getNumero() + ", " + this.getRue() + ", "
				+ this.getCodePostal() + " " + this.getVille();
	}

}
