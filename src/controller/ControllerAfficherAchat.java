package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import dao.Connexion;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Achat;
import model.Medicament;
import model.Ordonnance;

/**
 * Controller de la fenetre afficher achat
 * 
 * @author SRet
 *
 */
public class ControllerAfficherAchat extends Pane implements Initializable {

	private Achat achat;

	@FXML
	private TableView<Medicament> medicTable = new TableView<>();
	@FXML
	private TableColumn<Medicament, String> nomMedoc = new TableColumn<>();
	@FXML
	private TableColumn<Medicament, String> categorie = new TableColumn<>();
	@FXML
	private TableColumn<Medicament, Double> prix = new TableColumn<>();
	@FXML
	private TableColumn<Medicament, Integer> stock = new TableColumn<>();
	@FXML
	private Text textAchat = new Text();
	@FXML
	private Text textClient = new Text();
	@FXML
	private Text textSpecialiste = new Text();
	@FXML
	private Text labelSpecialiste = new Text();
	@FXML
	private TextField medecin = new TextField();
	@FXML
	private TextField mutuelle = new TextField();
	@FXML
	private TextField textPrixTotal;
	@FXML
	private Button imprimer = new Button();
	@FXML
	private Button retour = new Button();
	@FXML
	private Button quitter = new Button();

	/**
	 * Methode pour cibler un achat a la creation de la fenetre
	 * 
	 * @param achat : achat a ajouter
	 */
	public void setAchat(Achat achat) {
		this.achat = achat;
	}

	/**
	 * Methode d'initialisation de la fenetre
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// initialisation des colonnes aux parametres de l'objet voulu
		nomMedoc.setCellValueFactory(new PropertyValueFactory<>("nom"));
		categorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
		stock.setCellValueFactory(new PropertyValueFactory<>("quantite"));

		// affichage du client de l'achat
		textClient.setText(achat.getAcheteur().toString());

		// calcul du prix total
		DecimalFormat format = new DecimalFormat("0.00");
		String total = format.format(achat.getPrixTotal());
		textPrixTotal.setText("Prix total = " + total + " €");

		// methode de creation si un Achat est ciblé
		if (achat.getType().contains("Ordonnance")) {
			// recuperation du prix reduit
			prix.setCellValueFactory(new PropertyValueFactory<>("prixReduit"));
			for (Medicament medicament : achat.getMedicaments()) {
				medicament.setPrixReduit(achat.getAcheteur().getMutuelle());
				// ajout des médicaments à la table
				medicTable.getItems().add(medicament);
			}
			// recupération du medecin de l'achat à afficher
			medecin.setText("Medecin = " + ((Ordonnance) achat).getMedecin());
			// recupération de la mutuelle du client à afficher
			mutuelle.setText("Mutuelle = " + achat.getAcheteur().getMutuelle());
			// recupération du spécialiste s'étant occupé de l'achat
			if (((Ordonnance) achat).getSpecialiste() == null) {
				textSpecialiste.setText("Aucun");
			} else {
				textSpecialiste.setText(
						((Ordonnance) achat).getSpecialiste().toString());
			}
			// affichage du type d'achat
			textAchat.setText("Achat avec Ordonnance");
		}
		// méthode de création si une ordonnance est ciblé
		else {
			// recupération du prix de base du médicament
			prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
			// disparition des affichages inutiles
			medecin.setVisible(false);
			mutuelle.setVisible(false);
			textSpecialiste.setVisible(false);
			labelSpecialiste.setVisible(false);
			// ajustement de l'affichage du tableau
			medicTable.setLayoutX(33);
			medicTable.setLayoutY(50);
			medicTable.setPrefHeight(388);
			medicTable.setPrefWidth(736);
			// affichage du type d'achat
			textAchat.setText("Achat sans Ordonnance");
			// ajout des médicaments à la table
			medicTable.getItems().addAll(achat.getMedicaments());
		}
	}

	public void imprimer(ActionEvent event) {
		imprimer.setVisible(false);
		quitter.setVisible(false);
		retour.setVisible(false);
		textPrixTotal.setLayoutX(248);
		Scene scene = ((Node) event.getSource()).getScene();
		WritableImage img = scene.snapshot(null);
		try {
			File file = new File(System.getProperty("user.dir") + "\\Tickets\\"
					+ achat.getType() + "\\" + achat.getAcheteur() + "\\"
					+ achat.getType() + "_" + achat.getDate().toString() + "-"
					+ achat.getId() + ".png");
			file.mkdirs();
			ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
		} catch (IOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur impression");
			error.show();
		}
		Alert conf = new Alert(AlertType.INFORMATION);
		conf.setHeaderText("Impression du ticket réussi");
		conf.setContentText("Localisation : " + System.getProperty("user.dir")
				+ "\\Tickets\\" + achat.getType() + "\\" + achat.getAcheteur());
		conf.show();
		imprimer.setVisible(true);
		quitter.setVisible(true);
		retour.setVisible(true);
		textPrixTotal.setLayoutX(37);
	}

	/**
	 * Effet du bouton retour
	 * 
	 * @param event : clic sur le bouton retour
	 */
	public void retour(ActionEvent event) {
		try {
			Parent root = FXMLLoader
					.load(getClass().getResource("/view/Historique.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene()
					.getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass()
					.getResource("/application/application.css").toExternalForm());
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
	 * Effet du bouton quitter
	 */
	public void quitter() {
		Alert quitter = new Alert(AlertType.CONFIRMATION);
		quitter.setContentText("Voulez-vous quitter?");
		if (quitter.showAndWait().get() == ButtonType.OK) {
			Connexion.closeInstanceDB();
			Platform.exit();
		}
	}
}
