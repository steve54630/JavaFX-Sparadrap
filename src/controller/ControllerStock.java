package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.Connexion;
import dao.MedicamentDAO;
import exception.AppException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Medicament;

/**
 * Controller de la gestion du stock des medicaments
 * 
 * @author SRet
 *
 */
public class ControllerStock extends Pane implements Initializable {

	@FXML
	private TableView<Medicament> medTable = new TableView<>();
	@FXML
	private TableColumn<Medicament, String> nomMedoc = new TableColumn<>();
	@FXML
	private TableColumn<Medicament, String> categorieMedoc = new TableColumn<>();
	@FXML
	private TableColumn<Medicament, Double> prixMedoc = new TableColumn<>();
	@FXML
	private TableColumn<Medicament, Integer> stockMedoc = new TableColumn<>();

	private MedicamentDAO medDAO = new MedicamentDAO();

	/**
	 * Initialisation de la fenetre
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// définition des colonnes du tableau
		nomMedoc.setCellValueFactory(new PropertyValueFactory<>("nom"));
		categorieMedoc
				.setCellValueFactory(new PropertyValueFactory<>("categorie"));
		prixMedoc.setCellValueFactory(new PropertyValueFactory<>("prix"));
		stockMedoc.setCellValueFactory(new PropertyValueFactory<>("stock"));
		// ajout des medicaments a partir de la DAO
		try {
			medTable.getItems().addAll(medDAO.findAll());
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}
	}

	/**
	 * effet du clic droit sur un medicament
	 * 
	 * @param event : c
	 */
	public void gestionStock(ActionEvent event) {
		if (medTable.getSelectionModel().getSelectedItem() != null) {
			TextInputDialog gestion = new TextInputDialog();
			gestion.setContentText("Veuillez saisir le nouveau stock");
			gestion.setTitle("Gestion du stock");

			// modification en fonction du choix du client
			Optional<String> result = gestion.showAndWait();
			if (result.isPresent()) {
				Medicament choix = medTable.getSelectionModel()
						.getSelectedItem();
				try {
					int stock = Integer.parseInt(result.get());
					choix.setQuantite(stock);
					medDAO.update(choix);
				} catch (DAOException | AppException e) {
					Alert erreur = new Alert(AlertType.ERROR);
					erreur.setContentText(e.getMessage());
				} catch (RuntimeException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(
							"Changement impossible : veuillez saisir un nombre");
					error.show();
				}
				medTable.refresh();
			}
		}
	}

	/**
	 * Bouton retour au menu principal
	 * 
	 * @param event : Clic sur le bouton retour
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
		Connexion.closeInstanceDB();
		Alert quitter = new Alert(AlertType.CONFIRMATION);
		quitter.setContentText("Voulez-vous quitter?");
		if (quitter.showAndWait().get() == ButtonType.OK) {
			Platform.exit();
		}
	}

}
