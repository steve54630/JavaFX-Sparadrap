package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import exception.AppException;
import exception.DAOException;
import model.Achat;
import model.Medicament;
import model.Ordonnance;

/**
 * DAO des ordonnances
 * 
 * @author SRet
 *
 */
public class OrdonnanceDAO implements DAO<Ordonnance> {

	/**
	 * Creation d'une ordonnance dans la base de donnees
	 * 
	 * @param ach : ordonnance a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Ordonnance ach) throws DAOException, SQLException {
		boolean err = true;
		try {
			con.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append(
					"INSERT INTO achat (ID_CLIENT, ID_MEDECIN, ID_SPECIALISTE, DATE_ACHAT) ");
			sql.append("VALUES (?, ?, ?, ?)");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, ach.getAcheteur().getId());
			stm.setInt(2, ach.getMedecin().getId());
			if (ach.getSpecialiste() != null) {
				stm.setInt(3, ach.getSpecialiste().getId());
			} else {
				stm.setNull(3, Types.INTEGER);
			}
			stm.setString(4, ach.dateToString());
			stm.executeUpdate();
			StringBuilder sql2 = new StringBuilder();
			sql2.append("INSERT INTO panier ");
			sql2.append("VALUES ((select max(ID_ACHAT) from achat), ?, ?)");
			PreparedStatement stm2 = con.prepareStatement(sql2.toString());
			for (Medicament medicament : ach.getMedicaments()) {
				stm2.setInt(1, medicament.getId());
				stm2.setInt(2, medicament.getQuantite());
				stm2.executeUpdate();
			}
			con.commit();
			err = false;
		} catch (SQLException e) {
			con.rollback();
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

	/**
	 * Rechercher une ordonnance dans la base de donnees
	 * 
	 * @param id : id de l'ordonnance a rechercher
	 * @return ordonnance trouve
	 */
	@Override
	public Ordonnance read(int id) throws DAOException {
		Ordonnance ord = null;
		try {
			MedecinDAO medDao = new MedecinDAO();
			ClientDAO clientDAO = new ClientDAO();
			SpecialisteDAO speDao = new SpecialisteDAO();
			MedicamentDAO medocDao = new MedicamentDAO();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM achat a ");
			sql.append("WHERE ID_ACHAT = ? ");
			sql.append("ORDER BY ID_ACHAT ");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ord = new Ordonnance(rs.getInt(1), clientDAO.read(rs.getInt(2)),
						medDao.read(rs.getInt(4)), rs.getString(5));
				ord.setSpecialiste(speDao.read(rs.getInt(3)));
			}
			sql = new StringBuilder();
			sql.append("SELECT * FROM panier WHERE ID_ACHAT = ? ");
			stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			rs = stm.executeQuery();
			while (rs.next()) {
				ord.setMedicaments(medocDao.read(rs.getInt(2)), rs.getInt(3));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return ord;
	}

	/**
	 * Trouver toutes les ordonnances de la base de donnees
	 * 
	 * @return les ordonnances de la base de donnees sous forme de Collection
	 *         java
	 */
	@Override
	public List<Ordonnance> findAll() throws DAOException {
		ArrayList<Ordonnance> achList = new ArrayList<>();
		try {
			MedecinDAO medDao = new MedecinDAO();
			ClientDAO clientDAO = new ClientDAO();
			SpecialisteDAO speDao = new SpecialisteDAO();
			MedicamentDAO medocDao = new MedicamentDAO();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM achat WHERE ID_MEDECIN is not null ");
			sql.append("ORDER BY ID_ACHAT");
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				Ordonnance o = new Ordonnance(rs.getInt(1),
						clientDAO.read(rs.getInt(2)), medDao.read(rs.getInt(4)),
						rs.getString(5));
				achList.add(o);
				o.setSpecialiste(speDao.read(rs.getInt(3)));
			}
			sql = new StringBuilder();
			sql.append("SELECT * FROM panier");
			stm = con.createStatement();
			rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				for (Achat achat : achList) {
					if (achat.getId() == rs.getInt(1))
						achat.setMedicaments(medocDao.read(rs.getInt(2)),
								rs.getInt(3));
				}
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return achList;
	}

	/**
	 * Mettre a jour l'ordonnance dans la base de donnees
	 * 
	 * @param ach : ordonnance a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Ordonnance ach) {
		return false;
	}

	/**
	 * Supprimer une ordonnance dans la base de donnees
	 * 
	 * @param ach : ordonnance a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Ordonnance ach) throws DAOException {
		boolean err = true;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM achat ");
			sql.append("WHERE ID_ACHAT = ? ");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, ach.getId());
			stm.executeUpdate();
			this.findAll();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connexion base de données");
		}
		return err;
	}

}
