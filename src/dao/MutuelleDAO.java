package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exception.AppException;
import exception.DAOException;
import model.Mutuelle;

/**
 * DAO des mutuelles
 * 
 * @author SRet
 *
 */
public class MutuelleDAO implements DAO<Mutuelle> {

	/**
	 * Creation d'une mutuelle dans la base de donnees
	 * 
	 * @param mut : mutuelle a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Mutuelle mut) throws DAOException {

		boolean err = true;
		try {
			String sql = "INSERT INTO mutuelle (ID_ADRESSE, NOM_MUTUELLE, "
					+ "TEL_MUTUELLE, EMAIL_MUTUELLE, "
					+ "DEPARTEMENT_MUTUELLE, REBOURSEMENT) "
					+ "VALUES (?, ?, ?, ?, ?, ?) ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, mut.getAdresse().getId());
			stm.setString(2, mut.getNom());
			stm.setString(3, mut.getTelephone());
			stm.setString(4, mut.geteMail());
			stm.setString(5, mut.getDepartement());
			stm.setInt(6, mut.getRemboursement());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

	/**
	 * Rechercher une mutuelle dans la base de donnees
	 * 
	 * @param id : id de la mutuelle a rechercher
	 * @return mutuelle trouve
	 */
	@Override
	public Mutuelle read(int id) throws DAOException {
		Mutuelle mut = null;
		AdresseDAO list_adresse = new AdresseDAO();
		try {
			String sql = "SELECT * FROM mutuelle WHERE ID_MUTUELLE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				mut = new Mutuelle(rs.getInt(1), rs.getString(3),
						list_adresse.read(rs.getInt(2)), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return mut;
	}

	/**
	 * Trouver tous les mutuelles de la base de donnees
	 * 
	 * @return les mutuelles de la base de donnees sous forme de Collection java
	 */
	@Override
	public List<Mutuelle> findAll() throws DAOException {
		AdresseDAO list_adresse = new AdresseDAO();
		ArrayList<Mutuelle> MutList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM MUTUELLE";
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				MutList.add(new Mutuelle(rs.getInt(1), rs.getString(3),
						list_adresse.read(rs.getInt(2)), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getInt(7)));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return MutList;
	}

	/**
	 * Mettre a jour la mutuelle dans la base de donnees
	 * 
	 * @param mut : mutuelle a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Mutuelle mut) throws DAOException {
		boolean err = true;
		try {
			String sql = "UPDATE mutuelle " + "SET ID_ADRESSE = ?, "
					+ "NOM_MUTUELLE = ?, " + "TEL_MUTUELLE ?, "
					+ "EMAIL_MUTUELLE ?, " + "DEPARTEMENT_MUTUELLE ?, "
					+ "REBOURSEMENT = ? " + "WHERE ID_MUTUELLE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, mut.getAdresse().getId());
			stm.setString(2, mut.getNom());
			stm.setString(3, mut.getTelephone());
			stm.setString(4, mut.geteMail());
			stm.setString(5, mut.getDepartement());
			stm.setInt(6, mut.getRemboursement());
			stm.setInt(7, mut.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

	/**
	 * Supprimer une mutuelle dans la base de donnees
	 * 
	 * @param mut : mutuelle a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Mutuelle mut) throws DAOException {
		boolean err = true;
		try {
			String sql = "DELETE FROM mutuelle " + "WHERE ID_MUTUELLE = ? ";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, mut.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

}
