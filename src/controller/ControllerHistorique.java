package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.AchatDAO;
import dao.MedecinDAO;
import dao.OrdonnanceDAO;
import exception.DAOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Achat;
import model.Client;
import model.Medecin;
import model.Ordonnance;

/**
 * Controller de l'historique du client
 * 
 * @author SRet
 *
 */
public class ControllerHistorique extends Pane implements Initializable {

	@FXML
	private TableView<Achat> achTable = new TableView<>();
	@FXML
	private TableColumn<Achat, Client> clientAchat = new TableColumn<>();
	@FXML
	private TableColumn<Achat, String> dateAchat = new TableColumn<>();
	@FXML
	private TableColumn<Achat, String> typeAchat = new TableColumn<>();
	@FXML
	private ComboBox<String> comboBoxAchat = new ComboBox<>();
	@FXML
	private ComboBox<Object> comboBoxMedecin = new ComboBox<>();
	@FXML
	private Text labelMedecin = new Text();

	private AchatDAO achatDao = new AchatDAO();
	private OrdonnanceDAO ordonnanceDAO = new OrdonnanceDAO();
	private MedecinDAO medDAO = new MedecinDAO();

	/**
	 * Initialisation de la fenêtre
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		clientAchat.setCellValueFactory(new PropertyValueFactory<>("acheteur"));
		dateAchat.setCellValueFactory(new PropertyValueFactory<>("date"));
		typeAchat.setCellValueFactory(new PropertyValueFactory<>("type"));

		// méthode de remplissage du tableau des achats
		ajouterAchatSans();
		ajouterOrdonnance();

		// méthode d'ajout à la comboBox de médecin
		comboBoxMedecin.getItems().add("Tous");
		try {
			comboBoxMedecin.getItems().addAll(medDAO.findAll());
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}

		// méthode d'ajout à la comboBox d'achat
		comboBoxAchat.getItems().add("Tous les achats");
		comboBoxAchat.getItems().add("Achats sans ordonnance");
		comboBoxAchat.getItems().add("Achats avec ordonnance");
	}

	/**
	 * Methode pour ajouter des achats sans ordonnance a partir de la DAO
	 */
	public void ajouterAchatSans() {
		try {
			for (Achat achat : achatDao.findAll()) {
				achTable.getItems().add(achat);
			}
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}
	}

	/**
	 * Methode pour ajouter des achats avec ordonnance a partir de la DAO
	 */
	public void ajouterOrdonnance() {
		try {
			for (Ordonnance ordonnance : ordonnanceDAO.findAll()) {
				achTable.getItems().add(ordonnance);
			}
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}
	}

	/**
	 * Methode pour filtrer les achats en fonction du medecin
	 * 
	 * @param event : effet de la comboBox medecin
	 */
	public void filtreMedecin(ActionEvent event) {
		achTable.getItems().clear();
		if (comboBoxMedecin.getValue() != "Tous") {
			Medecin medecin = (Medecin) comboBoxMedecin.getValue();
			// affichage en fonction du choix de l'utilisateur
			try {
				for (Ordonnance ord : ordonnanceDAO.findAll()) {
					if (ord.getMedecin().getId() == medecin.getId())
						achTable.getItems().add(ord);
				}
			} catch (DAOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			}
		} else {
			ajouterOrdonnance();
		}
	}

	/**
	 * Méthode pour filtrer les achats par type
	 * 
	 * @param event : effet de la comboBox achat
	 */
	public void choixTypeAchat(ActionEvent event) {
		String choix = comboBoxAchat.getValue();
		achTable.getItems().clear();
		// affichage en fonction du choix de l'utilisateur
		switch (choix) {
		case "Tous les achats":
			ajouterAchatSans();
			ajouterOrdonnance();
			comboBoxMedecin.setVisible(false);
			labelMedecin.setVisible(false);
			break;
		case "Achats sans ordonnance":
			ajouterAchatSans();
			comboBoxMedecin.setVisible(false);
			labelMedecin.setVisible(false);
			break;
		case "Achats avec ordonnance":
			ajouterOrdonnance();
			comboBoxMedecin.setVisible(true);
			labelMedecin.setVisible(true);
			break;
		}
	}

	/**
	 * Effet du menu contextuel "afficher achat"
	 * 
	 * @param event : clic sur l'option "afficher achat" du menu contextuel
	 */
	public void afficherAchat(ActionEvent event) {
		if (achTable.getSelectionModel().getSelectedItem() != null) {
			try {
				FXMLLoader fen = new FXMLLoader(
						getClass().getResource("/view/AfficherAchat.fxml"));
				ControllerAfficherAchat controller = new ControllerAfficherAchat();
				controller.setAchat(
						achTable.getSelectionModel().getSelectedItem());
				fen.setController(controller);
				Stage stage = (Stage) achTable.getScene().getWindow();
				stage.setScene(new Scene(fen.load()));
				stage.setTitle("Afficher client");
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText("Erreur affichage");
				error.show();
			}
		}
	}

	/** Effet du menu contextuel "supprimer achat"
	 * 
	 * @param event : clic sur le menu contextuel "supprimer achat"
	 */
	public void supprimerAchat(ActionEvent event) {
		if (achTable.getSelectionModel().getSelectedItem() != null) {
				Alert supprimer = new Alert(AlertType.CONFIRMATION);
				supprimer.setContentText(
						"Voulez-vous supprimer la ligne sélectionné?");
				if (supprimer.showAndWait().get() == ButtonType.OK) {
					/*
					 * suppression de l'objet de la base de données, puis du
					 * tableau
					 */
					try {
						achatDao.delete(
								achTable.getSelectionModel().getSelectedItem());
						achTable.getItems().remove(
								achTable.getSelectionModel().getSelectedItem());
						achTable.refresh();
					} catch (DAOException e) {
						Alert error = new Alert(AlertType.ERROR);
						error.setContentText(e.getMessage());
						error.show();
					}
				}
			}
		}

	/**
	 * Effet du bouton retour
	 * 
	 * @param event : clic sur le bouton retour
	 */
	public void retour(ActionEvent event) {
		try {
			Parent root = FXMLLoader
					.load(getClass().getResource("/view/MainFen.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene()
					.getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Menu principal");
			stage.show();
		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Effet du bouton quitter
	 */
	public void quitter() {

		Alert quitter = new Alert(AlertType.CONFIRMATION);
		quitter.setContentText("Voulez-vous quitter?");
		if (quitter.showAndWait().get() == ButtonType.OK) {
			Platform.exit();
		}
	}

}
