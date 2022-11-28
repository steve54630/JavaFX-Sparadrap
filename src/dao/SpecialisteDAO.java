package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exception.AppException;
import exception.DAOException;
import model.Specialiste;

/**
 * DAO pour les specialistes
 * 
 * @author SRet
 *
 */
public class SpecialisteDAO implements DAO<Specialiste> {

	/**
	 * Creation d'un specialiste dans la base de donnees
	 * 
	 * @param spe : specialiste a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Specialiste spe) throws DAOException {
		boolean err = true;
		try {
			String sql = "INSERT INTO personne (ID_ADRESSE, NOM_PERSONNE, "
					+ "PRENOM_PERSONNE, TELEPHONE_PERSONNE, "
					+ "EMAIL_PERSONNE, NOM_SPECIALITE) "
					+ "VALUES (?, ?, ?, ?, ?, ?) ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, spe.getAdresse().getId());
			stm.setString(2, spe.getNom());
			stm.setString(3, spe.getPrenom());
			stm.setString(4, spe.getNumeroTelephone());
			stm.setString(5, spe.getEmail());
			stm.setString(6, spe.getSpecialite());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

	/**
	 * Rechercher un specialiste dans la base de donnees
	 * 
	 * @param id : id du specialiste a rechercher
	 * @return specialiste trouve
	 */
	@Override
	public Specialiste read(int id) throws DAOException {
		Specialiste spe = null;
		AdresseDAO list_adresse = new AdresseDAO();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select ID_PERSONNE, NOM_PERSONNE, PRENOM_PERSONNE, ");
			sql.append("TELEPHONE_PERSONNE, EMAIL_PERSONNE, ");
			sql.append("NOM_SPECIALITE, ID_ADRESSE ");
			sql.append("from personne ");
			sql.append("WHERE NOM_SPECIALITE is not NULL and ID_PERSONNE = ?");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				spe = new Specialiste(rs.getInt(1), rs.getString(2),
						rs.getString(3), list_adresse.read(rs.getInt(7)),
						rs.getString(4), rs.getString(5), rs.getString(6));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return spe;
	}

	/**
	 * Trouver toutes les specialistes de la base de donnees
	 * 
	 * @return les specialistes de la base de donnees sous forme de Collection
	 *         java
	 */
	@Override
	public List<Specialiste> findAll() throws DAOException {
		ArrayList<Specialiste> speList = new ArrayList<>();
		AdresseDAO list_adresse = new AdresseDAO();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select ID_PERSONNE, NOM_PERSONNE, PRENOM_PERSONNE, ");
			sql.append("TELEPHONE_PERSONNE, EMAIL_PERSONNE, ");
			sql.append("NOM_SPECIALITE, ID_ADRESSE ");
			sql.append("from personne ");
			sql.append("WHERE NOM_SPECIALITE is not NULL");
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				speList.add(new Specialiste(rs.getInt(1), rs.getString(2),
						rs.getString(3), list_adresse.read(rs.getInt(7)),
						rs.getString(4), rs.getString(5), rs.getString(6)));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return speList;
	}

	/**
	 * Mettre a jour les specialistes dans la base de donnees
	 * 
	 * @param spe : specialiste a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Specialiste spe) throws DAOException {
		boolean err = true;
		try {
			String sql = "UPDATE personne " + "SET NOM_PERSONNE = ?, "
					+ "PRENOM_PERSONNE = ?, " + "TELEPHONE_PERSONNE = ?, "
					+ "EMAIL_PERSONNE = ?, " + "NOM_SPECIALITE = ?, "
					+ "ID_ADRESSE = ? " + "WHERE ID_PERSONNE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, spe.getNom());
			stm.setString(2, spe.getPrenom());
			stm.setString(3, spe.getNumeroTelephone());
			stm.setString(4, spe.getEmail());
			stm.setString(5, spe.getSpecialite());
			stm.setInt(6, spe.getAdresse().getId());
			stm.setInt(7, spe.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

	/**
	 * Supprimer un specialiste dans la base de donnees
	 * 
	 * @param spe : specialiste a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Specialiste spe) throws DAOException {
		boolean err = true;
		try {
			String sql = "DELETE FROM personne " + "WHERE ID_PERSONNE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, spe.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

}
