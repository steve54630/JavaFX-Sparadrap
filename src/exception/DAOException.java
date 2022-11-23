package exception;

/**
 * Classe d'exception pour les erreurs de connexions
 * 
 * @author SRet
 */
public class DAOException extends Exception {

	/**
	 * identifiant pour "serialize" un objet
	 */
	private static final long serialVersionUID = 3242927487598368595L;

	/**
	 * Message a afficher si l'exception renvoit une erreur
	 * 
	 * @param message : message à afficher
	 */
	public DAOException(String message) {
		super(message);
	}

}
