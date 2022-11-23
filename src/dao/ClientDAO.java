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
import model.Client;
import model.Specialiste;

/**
 * DAO pour la creation d'un client
 * 
 * @author SRet
 */
public class ClientDAO implements DAO<Client> {

	/**
	 * Creation d'un client dans la base de donnees
	 * 
	 * @param cli : client a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Client cli) throws SQLException, DAOException {
		boolean err = true;
		Connection con = Connexion.getInstanceDB();
		if (!comparer(cli)) {
			try {
				con.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO personne ");
				sql.append(
						"(MUTUELLE_CLIENT, ID_ADRESSE, ID_MEDECIN, NOM_PERSONNE, ");
				sql.append(
						"PRENOM_PERSONNE, TELEPHONE_PERSONNE, EMAIL_PERSONNE, ");
				sql.append("SECSOCIALE_CLIENT, NAISSANCE_CLIENT)");
				sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				PreparedStatement stm = con.prepareStatement(sql.toString());
				stm.setInt(1, cli.getMutuelle().getId());
				stm.setInt(2, cli.getAdresse().getId());
				stm.setInt(3, cli.getMedecin().getId());
				stm.setString(4, cli.getNom());
				stm.setString(5, cli.getPrenom());
				stm.setString(6, cli.getNumeroTelephone());
				stm.setString(7, cli.getEmail());
				stm.setString(8, cli.getSecuriteSociale());
				stm.setString(9, cli.dateToString());
				stm.executeUpdate();
				sql = new StringBuilder(
						"INSERT INTO liste_client_specialiste ");
				sql.append(
						"VALUES ((SELECT max(ID_PERSONNE) from personne), ?)");
				stm = con.prepareStatement(sql.toString());
				for (Specialiste specialiste : cli.getSpecialiste()) {
					stm.setInt(1, specialiste.getId());
					stm.executeUpdate();
				}
				con.commit();
				err = false;
			} catch (SQLException e) {
				con.rollback();
				throw new DAOException("Erreur connection base de données");
			}
			return err;
		} else {
			throw new DAOException(
					"Erreur Base de données : Numéro de sécurité sociale présent");
		}
	}

	/**
	 * Rechercher un client dans la base de donnees
	 * 
	 * @param id : id de l'objet a rechercher
	 * @return client trouve
	 */
	@Override
	public Client read(int id) throws DAOException, AppException {
		Client cli = null;
		try {
			AdresseDAO adrDao = new AdresseDAO();
			MedecinDAO medDao = new MedecinDAO();
			MutuelleDAO mutDao = new MutuelleDAO();
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append("select ID_PERSONNE, NOM_PERSONNE, PRENOM_PERSONNE, ");
			sql.append("TELEPHONE_PERSONNE, ");
			sql.append("EMAIL_PERSONNE, SECSOCIALE_CLIENT, ");
			sql.append("NAISSANCE_CLIENT, MUTUELLE_CLIENT, ");
			sql.append("ID_ADRESSE, ID_MEDECIN ");
			sql.append("from personne p WHERE ID_PERSONNE = ? ");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				cli = new Client(rs.getInt(1), rs.getString(2), rs.getString(3),
						adrDao.read(rs.getInt(9)), rs.getString(4),
						rs.getString(6), rs.getString(5), rs.getString(7),
						medDao.read(rs.getInt(10)), mutDao.read(rs.getInt(8)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur connection base de données");
		}
		return cli;
	}

	/**
	 * Trouver tous les clients de la base de donnees
	 * 
	 * @return les clients de la base de donnees sous forme de Collection java
	 */
	@Override
	public List<Client> findAll() throws DAOException {
		ArrayList<Client> CliList = new ArrayList<>();
		try {
			AdresseDAO adrDao = new AdresseDAO();
			MedecinDAO medDao = new MedecinDAO();
			MutuelleDAO mutDao = new MutuelleDAO();
			SpecialisteDAO speDAO = new SpecialisteDAO();
			ArrayList<Specialiste> speList = (ArrayList<Specialiste>) speDAO
					.findAll();
			Connection con = Connexion.getInstanceDB();
			String sql = "select ID_PERSONNE, NOM_PERSONNE, PRENOM_PERSONNE, "
					+ "TELEPHONE_PERSONNE, "
					+ "EMAIL_PERSONNE, SECSOCIALE_CLIENT, "
					+ "NAISSANCE_CLIENT, MUTUELLE_CLIENT , "
					+ "ID_ADRESSE, ID_MEDECIN "
					+ "from personne where ID_MEDECIN is not null ";
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				Client cli = new Client(rs.getInt(1), rs.getString(2),
						rs.getString(3), adrDao.read(rs.getInt(9)),
						rs.getString(4), rs.getString(6), rs.getString(5),
						rs.getString(7), medDao.read(rs.getInt(10)),
						mutDao.read(rs.getInt(8)));
				CliList.add(cli);
			}
			for (Client client : CliList) {
				String sql2 = "SELECT ID_SPECIALISTE FROM liste_client_specialiste "
						+ "WHERE ID_CLIENT = ?";
				PreparedStatement stm2 = con.prepareStatement(sql2);
				stm2.setInt(1, client.getId());
				ResultSet rs2 = stm2.executeQuery();
				while (rs2.next()) {
					for (Specialiste spe : speList) {
						if (spe.getId() == rs2.getInt(1))
							client.setSpecialiste(spe);
					}
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur connection base de données");
		}
		return CliList;
	}

	/**
	 * Trouver tous les clients de la base de donnees
	 * 
	 * @return les clients de la base de donnees sous forme de Collection java
	 */
	@Override
	public boolean update(Client cli) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE personne ");
			sql.append("SET MUTUELLE_CLIENT = ?, ");
			sql.append("ID_ADRESSE = ?, ");
			sql.append("ID_MEDECIN = ?, ");
			sql.append("NOM_PERSONNE = ?, ");
			sql.append("PRENOM_PERSONNE = ?, ");
			sql.append("TELEPHONE_PERSONNE = ?, ");
			sql.append("EMAIL_PERSONNE = ?, ");
			sql.append("SECSOCIALE_CLIENT = ?, ");
			sql.append("NAISSANCE_CLIENT = ? ");
			sql.append("WHERE ID_PERSONNE = ?");
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, cli.getMutuelle().getId());
			stm.setInt(2, cli.getAdresse().getId());
			stm.setInt(3, cli.getMedecin().getId());
			stm.setString(4, cli.getNom());
			stm.setString(5, cli.getPrenom());
			stm.setString(6, cli.getNumeroTelephone());
			stm.setString(7, cli.getEmail());
			stm.setString(8, cli.getSecuriteSociale());
			stm.setString(9, cli.dateToString());
			stm.setInt(10, cli.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Supprimer un client dans la base de donnees
	 * 
	 * @param cli : client a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Client cli) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "DELETE FROM personne WHERE ID_PERSONNE = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, cli.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Methode pour ajouter des specialistes a un client dans la base de donnees
	 * 
	 * @param cli : client a modifier
	 * @param spe : specialiste a ajouter
	 * @return mise a jour reussi ou non
	 * @throws DAOException : erreur de connexion a la base de donnees
	 */
	public boolean ajoutSpecialiste(Client cli, Specialiste spe)
			throws DAOException {
		boolean err = true;
		Connection con = Connexion.getInstanceDB();
		StringBuilder sql = new StringBuilder(
				"INSERT INTO liste_client_specialiste ");
		sql.append("VALUES (?, ?) ");
		try {
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, cli.getId());
			stm.setInt(2, spe.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Methode pour supprimer des specialistes a un client dans la base de
	 * donnees
	 * 
	 * @param cli : client a modifier
	 * @param spe : specialiste a supprimer
	 * @return mise a jour reussi ou non
	 * @throws DAOException : erreur de connexion a la base de donnees
	 */
	public boolean supprimerSpecialiste(Client cli, Specialiste spe)
			throws DAOException {
		boolean err = true;
		Connection con = Connexion.getInstanceDB();
		StringBuilder sql = new StringBuilder(
				"DELETE FROM liste_client_specialiste ");
		sql.append("WHERE ID_CLIENT = ? AND ID_SPECIALISTE = ?");
		try {
			PreparedStatement stm = con.prepareStatement(sql.toString());
			stm.setInt(1, cli.getId());
			stm.setInt(2, spe.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Verifie si le numero de secu existe dans la base de donnees
	 * 
	 * @param cli : client dont le numero de secu est a verifier
	 * @return le numero de secu existe ou n'existe pas
	 */
	public boolean comparer(Client cli) {

		boolean trouve = false;
		List<Client> cliList = new ArrayList<>();

		try {
			cliList = this.findAll();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		for (Client client : cliList) {
			if (client.getSecuriteSociale().equals(cli.getSecuriteSociale()))
				trouve = true;
		}

		return trouve;
	}

}
