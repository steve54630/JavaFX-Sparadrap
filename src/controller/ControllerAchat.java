package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import dao.AchatDAO;
import dao.ClientDAO;
import dao.Connexion;
import dao.MedicamentDAO;
import dao.OrdonnanceDAO;
import exception.AppException;
import exception.DAOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Achat;
import model.Client;
import model.Medicament;
import model.Ordonnance;
import model.Specialiste;

public class ControllerAchat extends Pane implements Initializable {

	private Object root;
	private Stage stage;
	private Scene scene;

	@FXML
	private ComboBox<Client> boxClient = new ComboBox<>();
	@FXML
	private ComboBox<String> boxAchat = new ComboBox<>();
	@FXML
	private ComboBox<Medicament> boxMedicament = new ComboBox<>();
	@FXML
	private ComboBox<Specialiste> boxSpecialiste = new ComboBox<>();
	@FXML
	private Spinner<Integer> values = new Spinner<>();
	@FXML
	private TextField mutuelle = new TextField();
	@FXML
	private TextField medecin = new TextField();
	@FXML
	private Text labelSpecialiste = new Text();
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
	private TableColumn<Medicament, Integer> quantite = new TableColumn<>();
	@FXML
	private Button retour = new Button();
	@FXML
	private ContextMenu menu = new ContextMenu();

	private ClientDAO cliDao = new ClientDAO();
	private MedicamentDAO medDao = new MedicamentDAO();
	private AchatDAO achDAO = new AchatDAO();
	private OrdonnanceDAO ordDao = new OrdonnanceDAO();

	/**
	 * Initialisation de la fenetre
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// ajout des clients de la BDD à la ComboBox de clients
		try {
			for (Client client : cliDao.findAll()) {
				boxClient.getItems().add(client);
			}
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}

		// ajout des medicaments de la BDD à la ComboBox de clients
		try {
			for (Medicament medicament : medDao.findAll()) {
				boxMedicament.getItems().add(medicament);
			}
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}

		// association des colonnes du tableau au paramètres des objets du
		// tableau
		nomMedoc.setCellValueFactory(new PropertyValueFactory<>("nom"));
		categorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
		prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
		stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));

		values.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
		values.setEditable(true);

		// remplissage de la comboBox achat
		boxAchat.getItems().add("Achat sans ordonnance");
		boxAchat.getItems().add("Achat avec ordonnance");

		// sélection par défaut des comboBox achat et client
		boxClient.getSelectionModel().selectFirst();
		boxAchat.getSelectionModel().selectFirst();
		Client client = boxClient.getValue();

		// ajout des spécialistes du client par défaut à la comboBox
		for (Specialiste spe : client.getSpecialiste()) {
			boxSpecialiste.getItems().add(spe);
		}
		mutuelle.setVisible(false);
		medecin.setVisible(false);
		boxMedicament.getSelectionModel().selectFirst();
		for (MenuItem object : menu.getItems()) {
			object.setVisible(false);
		}
		try {
			Main.getCon().setAutoCommit(false);
		} catch (SQLException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(
					"Erreur connexion BDD : veuillez contacter le SAV");
			error.show();
		}
	}

	/**
	 * Passage en mode achat sans ordonnances
	 */
	public void modeAchat() {
		medecin.setVisible(false);
		mutuelle.setVisible(false);
		labelSpecialiste.setVisible(false);
		boxSpecialiste.setVisible(false);
		prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
		medicTable.refresh();
	}

	/**
	 * Passage en mode avec ordonnance
	 */
	public void modeOrdonnance() {
		medecin.setVisible(true);
		mutuelle.setVisible(true);
		boxSpecialiste.setVisible(true);
		labelSpecialiste.setVisible(true);
		medecin.setText("Médecin traitant : "
				+ boxClient.getValue().getMedecin().toString());
		mutuelle.setText(
				"Mutuelle : " + boxClient.getValue().getMutuelle().getNom());
		prix.setCellValueFactory(new PropertyValueFactory<>("prixReduit"));
		medicTable.refresh();
	}

	/**
	 * Choix du type d'achat
	 * 
	 * @param event : clic sur la comboBox achat
	 */
	public void choixAchat(ActionEvent event) {
		if (boxAchat.getValue() == "Achat avec ordonnance") {
			modeOrdonnance();
		}
		if (boxAchat.getValue() == "Achat sans ordonnance") {
			modeAchat();
		}
	}

	/**
	 * Choix du client
	 * 
	 * @param event : clic sur la comboBox client
	 */
	public void choixClient(ActionEvent event) {
		Client client = boxClient.getValue();
		medecin.setText("Médecin traitant : " + client.getMedecin().toString());
		mutuelle.setText("Mutuelle : " + client.getMutuelle().getNom());
		boxSpecialiste.getItems().clear();
		// ajout des spécialistes du client à la comboBox
		for (Specialiste spe : client.getSpecialiste()) {
			boxSpecialiste.getItems().add(spe);
		}
		for (Medicament medoc : medicTable.getItems()) {
			medoc.setPrixReduit(client.getMutuelle());
		}
		medicTable.refresh();
	}

	/**
	 * Fonction du bouton ajouter medicament
	 * 
	 * @param event : clic sur le bouton ajouter medicament
	 */
	public void ajouterMedicament(ActionEvent event) {
		int quantite = values.getValue();
		Medicament medoc = boxMedicament.getValue();
		Client client = boxClient.getValue();
		try {
			// modification de la quantite pour correspondre à la quantité
			// voulue
			medoc.setQuantite(quantite);
			/*
			 * ajustement du prix reduit du medicament en fonction de la
			 * mutuelle du client choisi
			 */
			medoc.setPrixReduit(client.getMutuelle());
			// ajout du médicament à la liste des médicaments achetés
			medicTable.getItems().add(medoc);
			// suppresion du médicament de la liste des choix
			boxMedicament.getItems().remove(boxMedicament.getValue());
			// remise par défaut de la comboBox de médicament
			boxMedicament.getSelectionModel().selectFirst();
		} catch (AppException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}
		if (medicTable.getItems().size() == 1) {
			for (MenuItem object : menu.getItems()) {
				object.setVisible(true);
			}
		}
	}

	/**
	 * Effet du sous-menu supprimer un medicament
	 * 
	 * @param event : clic sur le sous-menu supprimer un medicament
	 */
	public void supprimerMedicament(ActionEvent event) {
		Alert effacer = new Alert(AlertType.CONFIRMATION);
		effacer.setContentText("Voulez-vous supprimer le médicament?");
		if (effacer.showAndWait().get() == ButtonType.OK) {
			// ajout du medicament à la comboBox de choix
			boxMedicament.getItems()
					.add(medicTable.getSelectionModel().getSelectedItem());
			// suppression du médicament de la table
			medicTable.getItems()
					.remove(medicTable.getSelectionModel().getSelectedItem());
			boxMedicament.getSelectionModel().selectFirst();
			if (medicTable.getItems().isEmpty()) {
				for (MenuItem object : menu.getItems()) {
					object.setVisible(false);
				}
			}
		}
	}

	/**
	 * Effet du menu contextuel "changer médicament"
	 * 
	 * @param event : clic sur le menu
	 */
	public void changerQuantite(ActionEvent event) {
		Medicament choix = medicTable.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog("" + choix.getQuantite());
		dialog.setTitle("Changer stock");
		dialog.setHeaderText("Médicament choisi : " + choix.getNom());
		dialog.setContentText("Entre une nouvelle valeur:");

		// Utilisation du choix
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			try {
				int quantite = Integer.parseInt(result.get());
				choix.setQuantite(quantite);
				medicTable.refresh();
			} catch (AppException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			} catch (RuntimeException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(
						"Changement impossible : veuillez saisir un nombre");
				error.show();
			}
		}
	}

	/**
	 * Effet du bouton valider
	 * 
	 * @param event : clic sur le bouton valider
	 */
	public void validerAchat(ActionEvent event) {
		Alert valider = new Alert(AlertType.CONFIRMATION);
		valider.setContentText("Voulez-vous valider l'achat?");
		if (valider.showAndWait().get() == ButtonType.OK) {
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern("yyyy-MM-dd");
			LocalDate achatDate = LocalDate.now();
			String date = achatDate.format(formatter);
			// Effet du bouton si "Achat sans ordonnance" est sélectionné
			if (boxAchat.getValue().equals("Achat sans ordonnance")) {
				try {
					// création d'un achat avec les paramètres voulus
					Achat achat = new Achat(null, boxClient.getValue(), date);
					// ajout des médicaments à cet achat
					for (Medicament medicament : medicTable.getItems()) {
						achat.setMedicaments(medicament,
								medicament.getQuantite());
						medDao.updateStock(medicament);
					}
					// ajout de l'achat à la base de données
					achDAO.create(achat);
					// retour au menu principal
					menuPrincipal();
				} catch (AppException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(e.getMessage());
					error.show();
				} catch (NullPointerException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText("Aucune date saisie");
					error.show();
				} catch (DAOException | SQLException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(e.getMessage());
					error.show();
				}
			} else {
				try {
					// création d'une ordonnance avec les paramétres
					// sélectionnés
					Ordonnance ordonnance = new Ordonnance(null,
							boxClient.getValue(),
							boxClient.getValue().getMedecin(), date);
					// ajout des médicaments à cet ordonnance
					for (Medicament medicament : medicTable.getItems()) {
						ordonnance.setMedicaments(medicament,
								medicament.getQuantite());
						medDao.updateStock(medicament);
					}
					// ajout du spécialiste si choisi à cette ordonnance
					if (boxSpecialiste.getValue() != null) {
						ordonnance.setSpecialiste(boxSpecialiste.getValue());
					}
					// ajout de l'ordonnance à la base de données
					ordDao.create(ordonnance);
					// retour au menu principal
					menuPrincipal();
				} catch (AppException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(e.getMessage());
					error.show();
				} catch (DAOException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(e.getMessage());
					error.show();
				} catch (SQLException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(
							"Erreur connexion BDD : veuillez contacter le SAV");
					error.show();
				}
			}
		}
	}

	/**
	 * Fonction pour retourner au menu principal
	 */
	public void menuPrincipal() {
		try {
			root = FXMLLoader
					.load(getClass().getResource("/view/MainFen.fxml"));
			stage = (Stage) retour.getScene().getWindow();
			scene = new Scene((Parent) root);
			stage.setScene(scene);
			stage.setTitle("Menu principal");
			stage.show();
		} catch (IOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Effet du bouton retour
	 * 
	 * @param event : clic sur le bouton retour
	 */
	public void retour(ActionEvent event) {
		try {
			Main.getCon().rollback();
			menuPrincipal();
		} catch (SQLException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(
					"Erreur connexion BDD : veuillez contacter le SAV");
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
