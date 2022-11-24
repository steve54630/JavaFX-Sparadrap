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
import model.Adresse;

/**
 * DAO pour les adresses
 * 
 * @author SRet
 *
 */
public class AdresseDAO implements DAO<Adresse> {

	/**
	 * Creation d'une adresse dans la base de donnees
	 * 
	 * @param adr : adresse a ajouter dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean create(Adresse adr) throws DAOException {

		boolean err = true;

		try {
			Connection con = Connexion.getInstanceDB();
			if (this.compare(adr) == null) {
				String sql = "INSERT INTO adresse (NUM_ADRESSE, RUE_ADRESSE, "
						+ "CODEPOSTAL_ADRESSE, VILLE_ADRESSE) "
						+ "VALUES ( ?, ?, ?, ?)";
				PreparedStatement stm = con.prepareStatement(sql);
				stm.setString(1, adr.getNumero());
				stm.setString(2, adr.getRue());
				stm.setString(3, adr.getCodePostal());
				stm.setString(4, adr.getVille());
				stm.executeUpdate();
				err = false;
			}
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Rechercher une adresse dans la base de donnees
	 * 
	 * @param id : id de l'adresse a rechercher
	 * @return adresse trouve
	 */
	@Override
	public Adresse read(int id) throws DAOException {
		Adresse adr = null;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "SELECT * FROM adresse " + "WHERE ID_ADRESSE = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				adr = new Adresse(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return adr;
	}

	/**
	 * Trouver toutes les adresses de la base de donnees
	 * 
	 * @return les adresses de la base de donnees sous forme de Collection java
	 */
	@Override
	public List<Adresse> findAll() throws DAOException {
		List<Adresse> AdrList = new ArrayList<>();
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "SELECT * FROM adresse";
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				AdrList.add(new Adresse(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return AdrList;
	}

	/**
	 * Mettre a jour l'adresse dans la base de donnees
	 * 
	 * @param adr : adresse a mofifier dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean update(Adresse adr) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "UPDATE adresse " + "SET 'NUM_ADRESSE' = ? "
					+ "'RUE_ADRESSE' = ? " + "'CODEPOSTAL_ADRESSE' = ? "
					+ "SET 'VILLE_ADRESSE = ? " + "WHERE 'ID_ADRESSE' = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, adr.getNumero());
			stm.setString(2, adr.getRue());
			stm.setString(3, adr.getCodePostal());
			stm.setString(4, adr.getVille());
			stm.setInt(5, adr.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/**
	 * Supprimer une adresse dans la base de donnees
	 * 
	 * @param adr : adresse a supprimer dans la base de donnees
	 * @return mise a jour reussi ou non
	 */
	@Override
	public boolean delete(Adresse adr) throws DAOException {
		boolean err = true;
		try {
			Connection con = Connexion.getInstanceDB();
			String sql = "DELETE FROM adresse " + "WHERE 'ID_ADRESSE' = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, adr.getId());
			stm.executeUpdate();
			err = false;
		} catch (SQLException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return err;
	}

	/** Rechercher la derniere adresse insere dans la base de donnees
	 * @return la derniere adresse cree
	 * @throws DAOException : erreur de connexion a la base de donnees
	 */
	public Adresse findLast() throws DAOException {

		Adresse adr = null;
		try {
			Connection con = Connexion.getInstanceDB();
			StringBuilder sql = new StringBuilder("SELECT * FROM adresse ");
			sql.append("WHERE ID_ADRESSE IN ");
			sql.append("(SELECT max(ID_ADRESSE) from adresse)");

			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			while (rs.next()) {
				adr = new Adresse(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5));
			}
		} catch (SQLException | AppException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return adr;
	}

	/** Verifie si l'adresse existe dans la base de donnees
	 * @param adr : adresse a verifier
	 * @return l'adresse existe ou n'existe pas
	 * @throws DAOException  : problème connexion
	 */
	public Adresse compare(Adresse adr) throws DAOException {
		Adresse adrTrouve = null;
		try {
			for (Adresse adresse : this.findAll()) {
				if (adr.getNumero().equals(adresse.getNumero())
						&& adr.getRue().equals(adresse.getRue())
						&& adr.getCodePostal().equals(adresse.getCodePostal())
						&& adr.getVille().equals(adresse.getVille())) {
					adrTrouve = adresse;
				}
			}
		} catch (DAOException e) {
			throw new DAOException("Erreur connection base de données");
		}
		return adrTrouve;
	}

}
