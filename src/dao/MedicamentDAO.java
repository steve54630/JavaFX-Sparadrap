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
import model.Medicament;

/**
 * DAO pour les medicaments
 * 
 * @author SRet
 *
 */
public class MedicamentDAO implements DAO<Medicament> {

	/**
	 * Creation d'un medicament dans la base de donnees
	 * 
	 * @param med : medicament a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Medicament med) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "INSERT INTO medicament (NOM_MEDOC, CATEGORIE_MEDOC, "
					+ "PRIX_MEDOC, CIRCULATION_MEDOC, STOCK_MEDOC) "
					+ "VALUES (?, ?, ?, ?, ?) ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, med.getNom());
			stm.setString(2, med.getCategorie());
			stm.setDouble(3, med.getPrix());
			stm.setString(4, med.dateToString());
			stm.setInt(5, med.getStock());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Rechercher un medicament dans la base de donnees
	 * 
	 * @param id : id du medicament a rechercher
	 * @return medicament trouve
	 */
	@Override
	public Medicament read(int id) throws DAOException {
		Medicament med = null;
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM medicament WHERE ID_MEDOC = ? ");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				med = new Medicament(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getInt(4), rs.getInt(6),
						rs.getString(5));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return med;
	}

	/**
	 * Mettre a jour le medicament dans la base de donnees
	 * 
	 * @param med : medicament a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Medicament med) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "UPDATE medicament SET NOM_MEDOC = ?, CATEGORIE_MEDOC = ?, "
					+ "PRIX_MEDOC = ?, CIRCULATION_MEDOC = ?, STOCK_MEDOC = ? "
					+ "WHERE ID_MEDOC = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, med.getNom());
			stm.setString(2, med.getCategorie());
			stm.setDouble(3, med.getPrix());
			stm.setString(4, med.dateToString());
			stm.setInt(5, med.getStock());
			stm.setInt(6, med.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Supprimer un medicament dans la base de donnees
	 * 
	 * @param med : medicament a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 * @throws DAOException : erreur connexion base de donnees
	 */
	@Override
	public boolean delete(Medicament med) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "DELETE FROM medicament " + "WHERE ID_MEDICAMENT = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, med.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}
	
	/**
	 * Trouver tous les medicaments de la base de donnees
	 * 
	 * @return les medicaments de la base de donnees sous forme de Collection
	 *         java
	 */
	@Override
	public List<Medicament> findAll() throws DAOException {
		ArrayList<Medicament> medList = new ArrayList<>();
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM medicament");
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				medList.add(new Medicament(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getInt(4), rs.getInt(6),
						rs.getString(5)));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return medList;
	}

	public boolean updateStock (Medicament medoc) throws DAOException {
		boolean err = false;
		Connection con = Connexion.getInstanceDB();
		StringBuilder sql2 = new StringBuilder();
		sql2.append("UPDATE medicament ");
		sql2.append("SET STOCK_MEDOC = STOCK_MEDOC - ? ");
		sql2.append("WHERE ID_MEDOC = ?");
		try {
			PreparedStatement stm2 = con.prepareStatement(sql2.toString());
			stm2.setInt(2, medoc.getId());
			stm2.setInt(1, medoc.getQuantite());
			stm2.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}
	
}
