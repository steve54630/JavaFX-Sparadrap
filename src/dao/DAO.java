package dao;

import java.sql.SQLException;
import java.util.List;

import exception.AppException;
import exception.DAOException;

/**
 * Interface Data Access Objet(DAO) de l'application
 * 
 * @author SRet
 *
 * @param <T> objet voulu pour la DAO
 */
public interface DAO<T> {

	/**
	 * Methode pour creer un objet dans la base de donnees
	 * 
	 * @param t : type d'objet a creer
	 * @return mise a jour reussi ou non
	 * @throws DAOException : erreur a afficher
	 * @throws SQLException : erreur de la base de donnees
	 * @throws AppException : erreur de saisi des donnees
	 */
	boolean create(T t) throws DAOException, SQLException, AppException;

	/**
	 * Methode pour chercher un objet avec son id
	 * 
	 * @param id : identifiant de l'objet cherche
	 * @return l'objet cherche
	 * @throws DAOException : erreur a afficher
	 * @throws SQLException : erreur de la base de donnees
	 * @throws AppException : erreur de saisi des donnees
	 */
	T read(int id) throws DAOException, SQLException, AppException;

	/**
	 * Methode pour recuperer tous les objets de la base de donnees
	 * 
	 * @return Les objets sous forme de liste
	 * @throws DAOException : erreur a afficher
	 * @throws SQLException : erreur de la base de donnees
	 * @throws AppException : erreur de saisi des donnees
	 */
	List<T> findAll() throws DAOException, SQLException, AppException;

	/**
	 * Methode pour mettre a jour les donnees de la base en fonction d'un objet
	 * 
	 * @param t : objet a mettre a jour
	 * @return mise a jour reussi ou non
	 * @throws DAOException : erreur a afficher
	 * @throws SQLException : erreur de la base de donnees
	 * @throws AppException : erreur de saisi des donnees
	 */
	boolean update(T t) throws DAOException, SQLException, AppException;

	/**
	 * Methode pour supprimer un objet de la base
	 * 
	 * @param t : objet a supprimer de la base
	 * @return mise a jour reussi ou non
	 * @throws DAOException : erreur a afficher
	 * @throws SQLException : erreur de la base de donnees
	 * @throws AppException : erreur de saisi des donnees
	 */
	boolean delete(T t) throws DAOException, SQLException, AppException;

}
