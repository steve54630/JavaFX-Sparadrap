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
import model.Achat;
import model.Medicament;

/**
 * DAO pour les achats
 * 
 * @author SRet
 *
 */
public class AchatDAO implements DAO<Achat> {

	/**
	 * creer un nouvel achat dans la base de donnees
	 * 
	 * @param ach : ach a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Achat ach) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO achat (ID_CLIENT, DATE_ACHAT) ");
			sql.append("VALUES (?, ?)");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, ach.getAcheteur().getId());
			stm.setString(2, ach.dateToString());
			stm.executeUpdate();
			sql = new StringBuilder();
			sql.append("INSERT INTO panier ");
			sql.append("VALUES ((select max(ID_ACHAT) from achat), ?, ?)");
			stm = con.prepareStatement(sql.toString());
			for (Medicament medicament : ach.getMedicaments()) {
				stm.setInt(1, medicament.getId());
				stm.setInt(2, medicament.getQuantite());
				stm.executeUpdate();
				StringBuilder sql2 = new StringBuilder();
				sql2.append("UPDATE medicament ");
				sql2.append("SET STOCK_MEDOC = STOCK_MEDOC - ? ");
				sql2.append("WHERE ID_MEDOC = ?");
				PreparedStatement stm2 = con.prepareStatement(sql2.toString());
				stm2.setInt(2, medicament.getId());
				stm2.setInt(1, medicament.getQuantite());
				stm2.executeUpdate();
			}
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Rechercher un achat dans la base de donnees
	 * 
	 * @param id : id de l'objet a rechercher
	 * @return achat trouve
	 */
	@Override
	public Achat read(int id) throws DAOException {
		Achat ach = null;
		try {
			ClientDAO clientDAO = new ClientDAO();
			MedicamentDAO medDao = new MedicamentDAO();
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ID_ACHAT, ID_CLIENT, ");
			sql.append("DATE_ACHAT FROM achat ");
			sql.append("WHERE ID_ACHAT = ? ");
			sql.append("ORDER BY ID_ACHAT");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ach = new Achat(rs.getInt(1), clientDAO.read(rs.getInt(2)),
						rs.getString(3));
			}
			sql = new StringBuilder();
			sql.append("SELECT * FROM panier WHERE ID_ACHAT = ?");
			stm.setInt(1, id);
			rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				ach.setMedicaments(medDao.read(rs.getInt(2)), rs.getInt(3));
			}
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		} catch (AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return ach;
	}

	/**
	 * Trouver tous les achats de la base de donnees
	 * 
	 * @return les achats de la base de donnees sous forme de Collection java
	 */
	@Override
	public List<Achat> findAll() throws DAOException {
		ArrayList<Achat> achList = null;
		try {
			achList = new ArrayList<>();
			ClientDAO clientDAO = new ClientDAO();
			MedicamentDAO medDao = new MedicamentDAO();
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ID_ACHAT, ID_CLIENT, ");
			sql.append("DATE_ACHAT FROM achat ");
			sql.append("WHERE ID_MEDECIN is null ");
			sql.append("ORDER BY ID_ACHAT");
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				achList.add(new Achat(rs.getInt(1),
						clientDAO.read(rs.getInt(2)), rs.getString(3)));
			}
			sql = new StringBuilder();
			sql.append("SELECT * FROM panier");
			rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				for (Achat achat : achList) {
					if (achat.getId() == rs.getInt(1))
						achat.setMedicaments(medDao.read(rs.getInt(2)),
								rs.getInt(3));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		} catch (AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return achList;
	}

	/**
	 * Mettre a jour l'achat dans la base de donnees
	 * 
	 * @param ach : achat a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Achat ach) {
		boolean err = true;
		return err;
	}

	/**
	 * Supprimer un achat dans la base de donnees
	 * 
	 * @param ach : achat a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Achat ach) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM achat ");
			sql.append("WHERE ID_ACHAT = ? ");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, ach.getId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

}
