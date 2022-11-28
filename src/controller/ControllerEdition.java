package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import dao.AdresseDAO;
import dao.ClientDAO;
import dao.Connexion;
import dao.MedecinDAO;
import dao.MutuelleDAO;
import dao.SpecialisteDAO;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Adresse;
import model.Client;
import model.Medecin;
import model.Mutuelle;
import model.Specialiste;

/**
 * Controller de la fenetre d'affichage et d'edition d'un client
 * 
 * @author SRet
 *
 */
public class ControllerEdition extends Pane implements Initializable {

	@FXML
	private boolean editer = true;
	@FXML
	private Client choixCli;
	@FXML
	private TextField textNom = new TextField();
	@FXML
	private TextField textPrenom = new TextField();
	@FXML
	private TextField textNumRue = new TextField();
	@FXML
	private TextField textNomRue = new TextField();
	@FXML
	private TextField textCodePos = new TextField();
	@FXML
	private TextField textVille = new TextField();
	@FXML
	private TextField textTel = new TextField();
	@FXML
	private TextField textNumSecu = new TextField();
	@FXML
	private TextField textEmail = new TextField();
	@FXML
	private DatePicker choixDate = new DatePicker();
	@FXML
	private ChoiceBox<Medecin> boxMedecin = new ChoiceBox<>();
	@FXML
	private ChoiceBox<Mutuelle> boxMutuelle = new ChoiceBox<>();
	@FXML
	private ComboBox<Specialiste> boxSpecialiste = new ComboBox<>();
	@FXML
	private Button ajouterSpecialiste = new Button();
	@FXML
	private Button supprimerSpecialiste = new Button();
	@FXML
	private Button valider = new Button();
	@FXML
	private Text labelListe = new Text();
	@FXML
	private TextField dateNaissance = new TextField();
	@FXML
	private TableView<Specialiste> speTable = new TableView<>();
	@FXML
	private TableColumn<Specialiste, String> nomSpe = new TableColumn<>();
	@FXML
	private TableColumn<Specialiste, String> prenomSpe = new TableColumn<>();
	@FXML
	private TableColumn<Specialiste, String> specialiteSpe = new TableColumn<>();

	private ClientDAO clientDAO = new ClientDAO();
	private SpecialisteDAO speDAO = new SpecialisteDAO();
	private MedecinDAO medDao = new MedecinDAO();
	private MutuelleDAO mutDao = new MutuelleDAO();
	private AdresseDAO adresseDAO = new AdresseDAO();

	/**
	 * Setter pour recuperer le client a afficher
	 * 
	 * @param Cli : client a afficher
	 */
	public void setChoixCli(Client Cli) {
		this.choixCli = Cli;
	}

	/**
	 * Setter pour savoir si la fenetre est editable ou pas
	 * 
	 * @param choix : choix de la fenetre appelantre
	 */
	public void setEditer(boolean choix) {
		this.editer = choix;
	}

	/**
	 * Methode d'initialisation de la fenetre
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// association des colonnes du tableau au paramètres des objets du
		// tableau
		nomSpe.setCellValueFactory(new PropertyValueFactory<>("nom"));
		prenomSpe.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		specialiteSpe
				.setCellValueFactory(new PropertyValueFactory<>("specialite"));

		// ajout des specialistes de la base de donnees à la comboBox
		List<Specialiste> speList = new ArrayList<>();
		try {
			speList = speDAO.findAll();
		} catch (DAOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText(e.getMessage());
			error.show();
		}

		// paramétres de la fenêtre si elle est éditable
		textNom.setEditable(editer);
		textPrenom.setEditable(editer);
		textNumRue.setEditable(editer);
		textNomRue.setEditable(editer);
		textCodePos.setEditable(editer);
		textVille.setEditable(editer);
		textTel.setEditable(editer);
		textNumSecu.setEditable(editer);
		textEmail.setEditable(editer);
		choixDate.setEditable(editer);
		boxSpecialiste.setVisible(editer);
		ajouterSpecialiste.setVisible(editer);
		supprimerSpecialiste.setVisible(editer);
		valider.setVisible(editer);
		labelListe.setVisible(editer);
		choixDate.setVisible(editer);

		if (editer == true) {
			try {
				for (Medecin medecin : medDao.findAll()) {
					boxMedecin.getItems().add(medecin);
				}
			} catch (DAOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			}
			try {
				for (Mutuelle mutuelle : mutDao.findAll()) {
					boxMutuelle.getItems().add(mutuelle);
				}
			} catch (DAOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText(e.getMessage());
				error.show();
			}
			// paramétres de la fenêtre si elle n'est pas éditable
		} else {
			boxMutuelle.getItems().add(choixCli.getMutuelle());
			boxMedecin.getItems().add(choixCli.getMedecin());
			speTable.setLayoutX(154);
			dateNaissance.setVisible(true);
		}

		// paramétres à changer si un client est choisi
		if (choixCli != null) {
			valider.setText("Valider");
			textNom.setText(choixCli.getNom());
			textPrenom.setText(choixCli.getPrenom());
			textNumRue.setText(choixCli.getAdresse().getNumero());
			textNomRue.setText(choixCli.getAdresse().getRue());
			textCodePos.setText(choixCli.getAdresse().getCodePostal());
			textVille.setText(choixCli.getAdresse().getVille());
			textTel.setText(choixCli.getNumeroTelephone());
			textNumSecu.setText(choixCli.getSecuriteSociale());
			textEmail.setText(choixCli.getEmail());
			choixDate.setValue(choixCli.getDateNaissance());
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern("dd/MM/yyyy");
			dateNaissance.setText(choixDate.getValue().format(formatter));
			// ajout des specialistes du client à la table
			speTable.getItems().addAll(choixCli.getSpecialiste());
			// ajout des specialistes disponibles à la comboBox
			for (Specialiste specialiste : speList) {
				boolean trouve = false;
				for (Specialiste speClient : choixCli.getSpecialiste()) {
					if (specialiste.getId() == speClient.getId())
						trouve = true;
				}
				if (!trouve)
					boxSpecialiste.getItems().add(specialiste);
			}
			// choix de la comboBox médecin sur le médecin par défaut
			for (Medecin medecin : boxMedecin.getItems()) {
				if (medecin.getId() == choixCli.getMedecin().getId())
					boxMedecin.getSelectionModel().select(medecin);
			}
			// choix de la comboBox mutuelle sur la mutuelle par défaut
			for (Mutuelle mutuelle : boxMutuelle.getItems()) {
				if (mutuelle.getId() == choixCli.getMutuelle().getId())
					boxMutuelle.getSelectionModel().select(mutuelle);
			}
			// paramétres de la fenêtre si aucun client n'est choisi
		} else {
			boxSpecialiste.getItems().addAll(speList);
			boxMutuelle.getSelectionModel().selectFirst();
			boxMedecin.getSelectionModel().selectFirst();
		}

		boxSpecialiste.getSelectionModel().selectFirst();
		try {
			Main.getCon().setAutoCommit(false);
		} catch (SQLException e) {
			Alert erreur = new Alert(AlertType.ERROR);
			erreur.setHeaderText("Erreur serveur");
			erreur.setContentText(e.getMessage());
			erreur.show();
		}

	}

	/**
	 * Effet du bouton valider
	 * 
	 * @param event : Clic sur le bouton valider
	 */
	public void valider(ActionEvent event) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			// transformation des saisies clients en string
			String numRue = textNumRue.getText();
			String nomRue = textNomRue.getText();
			String codePostal = textCodePos.getText();
			String ville = textVille.getText();
			String nom = textNom.getText();
			String prenom = textPrenom.getText();
			String tel = textTel.getText();
			String numSecu = textNumSecu.getText();
			String email = textEmail.getText();
			String date = choixDate.getValue().format(formatter);
			// création du client avec les paramétres voulues
			Client cli = new Client(null, nom, prenom,
					new Adresse(null, numRue, nomRue, codePostal, ville), tel,
					numSecu, email, date, boxMedecin.getValue(),
					boxMutuelle.getValue());
			// action si création d'un client est choisi
			if (choixCli == null) {
				for (Specialiste specialiste : speTable.getItems()) {
					cli.setSpecialiste(specialiste);
				}
				adresseDAO.create(cli.getAdresse());
				if (adresseDAO.compare(cli.getAdresse()) != null) {
					cli.setAdresse(adresseDAO.compare(cli.getAdresse()));
				}
				// ajout du client à la base de données
				clientDAO.create(cli);
				// action si édition d'un client est choisi
			} else {
				cli.getAdresse().setId(choixCli.getAdresse().getId());
				cli.setId(choixCli.getId());
				clientDAO.update(cli);
			}
			Main.getCon().commit();
			Parent root = FXMLLoader
					.load(getClass().getResource("/view/Details.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene()
					.getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Détails");
			stage.show();
		} catch (DAOException | SQLException e) {
			Alert erreur = new Alert(AlertType.ERROR);
			erreur.setHeaderText("Erreur serveur");
			erreur.setContentText(e.getMessage());
			erreur.show();
		} catch (AppException e) {
			Alert erreur = new Alert(AlertType.WARNING);
			erreur.setHeaderText("Erreur lors de la création");
			erreur.setContentText(e.getMessage());
			erreur.show();
		} catch (NullPointerException e) {
			Alert erreur = new Alert(AlertType.WARNING);
			erreur.setHeaderText("Erreur lors de la création");
			erreur.setContentText("Date manquante");
			erreur.show();
		} catch (IOException e) {
			Alert erreur = new Alert(AlertType.ERROR);
			erreur.setHeaderText("Erreur affichage");
			erreur.setContentText("Veuillez contacter le SAV");
			erreur.show();
		}
	}

	/**
	 * Effet du bouton "ajout specialiste"
	 * 
	 * @param event : clic sur le bouton "ajout specialiste"
	 */
	public void ajoutSpecialiste(ActionEvent event) {
		// enregistrement du spécialiste dans la base de données du client
		if (choixCli != null)
			try {
				clientDAO.ajoutSpecialiste(choixCli, boxSpecialiste.getValue());
				// passage du spécialiste de la ComboBox à la table
				speTable.getItems().add(boxSpecialiste.getValue());
				boxSpecialiste.getItems().remove(boxSpecialiste.getValue());
				boxSpecialiste.getSelectionModel().selectFirst();
			} catch (DAOException e) {
				Alert erreur = new Alert(AlertType.ERROR);
				erreur.setHeaderText("Erreur serveur");
				erreur.setContentText(e.getMessage());
				erreur.show();
			}
		else {
			speTable.getItems().add(boxSpecialiste.getValue());
			boxSpecialiste.getItems().remove(boxSpecialiste.getValue());
			boxSpecialiste.getSelectionModel().selectFirst();
		}
	}

	/**
	 * Effet du bouton "Supprimer specialiste"
	 * 
	 * @param event : clic sur le bouton "Supprimer specialiste"
	 */
	public void effacerSpecialiste(ActionEvent event) {
		Specialiste choix = speTable.getSelectionModel().getSelectedItem();
		// enregistrement du spécialiste dans la base de données du client
		if (choixCli != null)
			try {
				clientDAO.supprimerSpecialiste(choixCli, choix);
				// passage du spécialiste de la table à la ComboBox
				speTable.getItems().remove(choix);
				boxSpecialiste.getItems().add(choix);
				boxSpecialiste.getSelectionModel().selectFirst();
			} catch (DAOException e) {
				Alert erreur = new Alert(AlertType.ERROR);
				erreur.setHeaderText("Erreur serveur");
				erreur.setContentText(e.getMessage());
				erreur.show();
			}
		else {
			speTable.getItems().remove(choix);
			boxSpecialiste.getItems().add(choix);
			boxSpecialiste.getSelectionModel().selectFirst();
		}
	}

	/**
	 * Effet du bouton "retour"
	 * 
	 * @param event : clic sur le bouton "retour"
	 */
	public void retour(ActionEvent event) {
		try {
			Main.getCon().rollback();
			Parent root = FXMLLoader
					.load(getClass().getResource("/view/Details.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene()
					.getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Détails");
			stage.show();
		} catch (SQLException e) {
			Alert erreur = new Alert(AlertType.ERROR);
			erreur.setHeaderText("Erreur serveur");
			erreur.setContentText(e.getMessage());
			erreur.show();
		} catch (IOException e) {
			Alert erreur = new Alert(AlertType.ERROR);
			erreur.setHeaderText("Erreur affichage");
			erreur.setContentText("Veuillez contacter le SAV");
			erreur.show();
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
