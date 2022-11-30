package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import exception.DAOException;

/**
 * Singleton pour se connecter a mySQL
 * 
 * @author SRet
 *
 */
public class Connexion {

	private static final String PATHCONF = "mysql.properties";
	private static final Properties props = new Properties();
	private static Connection connection;

	private Connexion() throws DAOException {
		try {
			FileInputStream file = new FileInputStream(PATHCONF);

			props.load(file);
			props.setProperty("user", props.getProperty("user"));
			props.setProperty("password", props.getProperty("password"));

			connection = DriverManager.getConnection(props.getProperty("url"),
					props);
		} catch (IOException | SQLException e) {
			throw new DAOException("infos : " + e.getMessage());
		}
	}

	/**
	 * Methode pour recuperer la connexion
	 * 
	 * @return la connexion cree par le singleton
	 */
	public static Connection getInstanceDB() {
		if (connection == null) {
			try {
				new Connexion();
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	/**
	 * Methode pour couper la connection
	 */
	public static void closeInstanceDB() {
		try {
			Connexion.connection.close();
		} catch (SQLException sqle) {
			System.out.println("RelationWithDB erreur : " + sqle.getMessage()
					+ " [SQL error code : " + sqle.getSQLState() + " ]");
		}
	}


}
