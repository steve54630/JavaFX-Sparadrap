package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.ClientDAO;
import dao.Connexion;
import dao.MedecinDAO;
import dao.MutuelleDAO;
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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Client;
import model.Medecin;
import model.Mutuelle;

/**
 * Controller de la fenetre de detail des clients
 * 
 * @author SRet
 *
 */
public class ControllerDetailClient extends Pane implements Initializable {

	@FXML
	private TableView<Client> cliTable = new TableView<>();
	@FXML
	private TableColumn<Client, String> nom = new TableColumn<>();
	@FXML
	private TableColumn<Client, String> prenom = new TableColumn<>();
	@FXML
	private TableColumn<Client, String> telephone = new TableColumn<>();
	@FXML
	private TableColumn<Client, String> SecSociale = new TableColumn<>();
	@FXML
	private TableColumn<Client, Medecin> medecin = new TableColumn<>();
	@FXML
	private TableColumn<Client, Mutuelle> mutuelle = new TableColumn<>();
	@FXML
	private ContextMenu menu = new ContextMenu();

	private ClientDAO cliDao = new ClientDAO();
	private MutuelleDAO mutDao = new MutuelleDAO();
	private MedecinDAO medDao = new MedecinDAO();

	/**
	 * Initialisation de la fenêtre de la liste des clients
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		telephone.setCellValueFactory(
				new PropertyValueFactory<>("numeroTelephone"));
		SecSociale.setCellValueFactory(
				new PropertyValueFactory<>("securiteSociale"));
		medecin.setCellValueFactory(new PropertyValueFactory<>("medecin"));
		mutuelle.setCellValueFactory(new PropertyValueFactory<>("mutuelle"));
		cliTable.setContextMenu(menu);
		afficherTous();
	}

	/**
	 * Effet du filtre nom
	 * 
	 * @param event : clic sur le bouton nom
	 */
	public void rechercheNom(ActionEvent event) {
		// demande d'une saisie à l'utilisateur
		TextInputDialog recherche = new TextInputDialog();
		recherche.setTitle("Recherche par nom");
		recherche.setHeaderText("Confirmation");
		recherche.setContentText("Saisit le nom à rechercher");
		Optional<String> result = recherche.showAndWait();
		if (result.isPresent()) {
			// recherche en fonction de la saisi demandé
			cliTable.getItems().clear();
			try {
				for (Client client : cliDao.findAll()) {
					if (client.getNom().contains(result.get())) {
						cliTable.getItems().add(client);
					}
				}
			} catch (DAOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			}
		}
	}

	/**
	 * effet du bouton medecin
	 */
	public void rechercheMedecin() {
		ArrayList<Medecin> medList = new ArrayList<>();
		// ajout des medecins de la base de données à la liste
		try {
			for (Medecin med : medDao.findAll()) {
				medList.add(med);
			}
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}

		// demande d'un choix à l'utilisateur
		ChoiceDialog<Medecin> choix = new ChoiceDialog<>();
		choix.getItems().addAll(medList);
		choix.setTitle("Recherche par mutuelle");
		choix.setHeaderText("Confirmation");
		choix.setContentText("Choix médecin");
		choix.setSelectedItem(medList.get(0));

		// recherche en fonction du choix demandé
		Optional<Medecin> result = choix.showAndWait();
		if (result.isPresent()) {
			cliTable.getItems().clear();
			try {
				for (Client client : cliDao.findAll()) {
					if (client.getMedecin().getId() == result.get().getId()) {
						cliTable.getItems().add(client);
					}
				}
			} catch (DAOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			}
		}
	}

	/**
	 * Effet du bouton "mutuelle"
	 * 
	 * @param event : clic sur le bouton mutuelle
	 */
	public void rechercheMutuelle(ActionEvent event) {

		// ajout des mutuelles de la base de données à la liste
		ArrayList<Mutuelle> mutList = new ArrayList<>();
		try {
			for (Mutuelle mutuelle : mutDao.findAll()) {
				mutList.add(mutuelle);
			}
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}

		// demande d'un choix à l'utilisateur
		ChoiceDialog<Mutuelle> choix = new ChoiceDialog<>();
		choix.getItems().addAll(mutList);
		choix.setTitle("Recherche par mutuelle");
		choix.setHeaderText("Confirmation");
		choix.setContentText("Choix mutuelle");
		choix.setSelectedItem(mutList.get(0));

		// recherche en fonction du choix demandé
		Optional<Mutuelle> result = choix.showAndWait();
		if (result.isPresent()) {
			cliTable.getItems().clear();
			try {
				for (Client client : cliDao.findAll()) {
					if (client.getMutuelle().getId() == result.get().getId()) {
						cliTable.getItems().add(client);
					}
				}
			} catch (DAOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			}
		}
	}

	/**
	 * Effet du bouton "Afficher tous"
	 * 
	 * @param event : clic sur le bouton "afficher tous"
	 */
	public void refreshTable(ActionEvent event) {
		cliTable.getItems().clear();
		cliTable.refresh();
		afficherTous();
	}

	/**
	 * Methode pour recuperer tous les clients de la base de donnees
	 */
	public void afficherTous() {
		try {
			cliTable.getItems().addAll(cliDao.findAll());
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}
	}

	/**
	 * Ouverture du menu du client
	 * 
	 * @param choix : mode modification ou non
	 * @param client : client de la fenetre
	 * @throws IOException : erreur affichage
	 */
	public void menuClient(boolean choix, Client client) throws IOException {
		FXMLLoader fen = new FXMLLoader(
				getClass().getResource("/view/EditClient.fxml"));
		ControllerEdition controller = new ControllerEdition();
		/*
		 * passage des paramétres au contrôleur pour une fenêtre
		 * "modifier un client"
		 */
		controller.setEditer(choix);
		controller.setChoixCli(client);
		fen.setController(controller);
		Stage stage = (Stage) cliTable.getScene().getWindow();
		stage.setScene(new Scene(fen.load()));
		stage.setTitle("Afficher client");
		stage.show();
	}

	/**
	 * Effet du bouton "ajouter un client"
	 * 
	 * @param event : clic sur le bouton ajouter un client
	 */
	public void ajoutClient(ActionEvent event) {
		try {
			menuClient(true, null);
		} catch (IOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Effet du menu contextuelle "Afficher un client"
	 * 
	 * @param event : clic sur l'option "Afficher un client"
	 */
	public void afficherClient(ActionEvent event) {
		if (cliTable.getSelectionModel().getSelectedItem() != null) {
			try {
				menuClient(false,
						cliTable.getSelectionModel().getSelectedItem());
			} catch (IOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText("Erreur affichage");
				error.show();
			}
		}
	}

	/**
	 * Effet du menu contextuelle "Modifier un client"
	 * 
	 * @param event : clic sur l'option "Modifier un client"
	 */
	public void modifierClient(ActionEvent event) {
		if (cliTable.getSelectionModel().getSelectedItem() != null) {
			try {
				menuClient(true,
						cliTable.getSelectionModel().getSelectedItem());
			} catch (IOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText("Erreur affichage");
				error.show();
			}
		}
	}

	/**
	 * Effet du menu contextuelle "Supprimer un client"
	 * 
	 * @param event : clic sur l'option "Supprimer un client"
	 */
	public void suppressionClient(ActionEvent event) {
		if (cliTable.getSelectionModel().getSelectedItem() != null) {
			Alert supprimer = new Alert(AlertType.CONFIRMATION);
			supprimer.setContentText("Voulez-vous supprimer la ligne "
					+ cliTable.getSelectionModel().getSelectedItem() + "?");
			// méthode de suppression du client
			if (supprimer.showAndWait().get() == ButtonType.OK) {
				try {
					cliDao.delete(
							cliTable.getSelectionModel().getSelectedItem());
				} catch (DAOException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setContentText(e.getMessage());
					error.show();
				}
				cliTable.getItems()
						.remove(cliTable.getSelectionModel().getSelectedItem());
			}
		}
	}

	/**
	 * Effet du bouton "retour"
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
			stage.show();
		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("Erreur affichage");
			error.show();
		}
	}

	/**
	 * Effet du bouton "quitter"
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
