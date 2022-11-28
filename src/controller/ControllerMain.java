package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.Connexion;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller de l'application
 * 
 * @author SRet
 *
 */
public class ControllerMain extends Pane implements Initializable {

	@FXML
	private Button achat = new Button();
	@FXML
	private Button historique = new Button();
	@FXML
	private Button detail_field = new Button();
	@FXML
	private Button quitter = new Button();

	private Parent root;
	private Stage stage;
	private Scene scene;

	/**
	 * Methode d'initialisation de la fenetre
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Ouverture du menu achat
	 * 
	 * @param event : clic sur le bouton achat
	 */
	public void achat(ActionEvent event) {
		try {
			root = FXMLLoader
					.load(ControllerMain.class.getResource("/view/Achat.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setTitle("Menu Achat");
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Ouverture du menu historique
	 * 
	 * @param event : clic sur le boutton historique
	 */
	public void historique(ActionEvent event) {
		try {
			root = FXMLLoader
					.load(getClass().getResource("/view/Historique.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Historique");
			stage.show();
		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Ouverture du menu stock
	 * 
	 * @param event : clic sur le bouton stock
	 */
	public void stock(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/view/Stock.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Stock");
			stage.show();
		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Ouverture du menu details des clients
	 * 
	 * @param event : clic sur le bouton details des clients
	 */
	public void detail_field(ActionEvent event) {
		try {
			root = FXMLLoader
					.load(getClass().getResource("/view/Details.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Détails");
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
	public void exit() {
		Connexion.closeInstanceDB();
		Alert quitter = new Alert(AlertType.CONFIRMATION);
		quitter.setContentText("Voulez-vous quitter?");
		if (quitter.showAndWait().get() == ButtonType.OK) {
			Platform.exit();
		}
	}

}
