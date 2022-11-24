package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exception.AppException;
import exception.DAOException;
import model.Medecin;

/**
 * DAO pour les medecins
 * 
 * @author SRet
 *
 */
public class MedecinDAO implements DAO<Medecin> {

	/**
	 * Creation d'un medecin dans la base de donnees
	 * 
	 * @param med : medecin a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Medecin med) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "INSERT INTO personne (ID_ADRESSE, NOM_PERSONNE, "
					+ "PRENOM_PERSONNE, TELEPHONE_PERSONNE, "
					+ "EMAIL_PERSONNE, NUMERO_MEDECIN) "
					+ "VALUES (?, ?, ?, ?, ?, ?) ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, med.getAdresse().getId());
			stm.setString(2, med.getNom());
			stm.setString(3, med.getPrenom());
			stm.setString(4, med.getNumeroTelephone());
			stm.setString(5, med.getEmail());
			stm.setString(6, med.getNumeroAggrement());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Rechercher un medecin dans la base de donnees
	 * 
	 * @param id : id du medecin a rechercher
	 * @return adresse trouve
	 */
	@Override
	public Medecin read(int id) throws DAOException {
		Medecin med = null;
		AdresseDAO list_adresse = new AdresseDAO();
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append(
					"select ID_PERSONNE, NOM_PERSONNE, PRENOM_PERSONNE, TELEPHONE_PERSONNE, ");
			sql.append(
					"EMAIL_PERSONNE, NUMERO_MEDECIN, ID_ADRESSE from personne p ");
			sql.append("WHERE ID_PERSONNE = ?");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				med = new Medecin(rs.getInt(1), rs.getString(2),
						rs.getString(3), list_adresse.read(rs.getInt(7)),
						rs.getString(4), rs.getString(5), rs.getString(6));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return med;
	}

	/**
	 * Trouver tous les medecins de la base de donnees
	 * 
	 * @return les medecins de la base de donnees sous forme de Collection java
	 */
	@Override
	public List<Medecin> findAll() throws DAOException {
		ArrayList<Medecin> MedList = new ArrayList<>();
		AdresseDAO list_adresse = new AdresseDAO();
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append(
					"select ID_PERSONNE, NOM_PERSONNE, PRENOM_PERSONNE, TELEPHONE_PERSONNE, ");
			sql.append(
					"EMAIL_PERSONNE, NUMERO_MEDECIN, ID_ADRESSE from personne p ");
			sql.append("WHERE NUMERO_MEDECIN is not null");
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				MedList.add(new Medecin(rs.getInt(1), rs.getString(2),
						rs.getString(3), list_adresse.read(rs.getInt(7)),
						rs.getString(4), rs.getString(5), rs.getString(6)));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return MedList;
	}

	/**
	 * Mettre a jour le medecin dans la base de donnees
	 * 
	 * @param med : medecin a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Medecin med) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "UPDATE personne " + "SET NOM_PERSONNE = ?, "
					+ "PRENOM_PERSONNE = ?, " + "TELEPHONE_PERSONNE = ?, "
					+ "EMAIL_PERSONNE = ?, " + "NUMERO_MEDECIN = ?, "
					+ "ID_ADRESSE = ? " + "WHERE ID_PERSONNE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, med.getNom());
			stm.setString(2, med.getPrenom());
			stm.setString(3, med.getNumeroTelephone());
			stm.setString(4, med.getEmail());
			stm.setString(5, med.getNumeroAggrement());
			stm.setInt(6, med.getAdresse().getId());
			stm.setInt(7, med.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Supprimer un medecin dans la base de donnees
	 * 
	 * @param med : medecin a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Medecin med) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "DELETE FROM personne " + "WHERE ID_PERSONNE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, med.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

}
