package application;

import java.io.IOException;

import dao.Connexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main de l'application
 * 
 * @author SRet
 *
 */
public class Main extends Application {

	/**
	 * Methode de lancement de l'application
	 * 
	 * @param primaryStage : fenetre a afficher
	 */
	@Override
	public void start(Stage primaryStage) {

//		// Creer le dialog
//		Dialog<Pair<String, String>> dialog = new Dialog<>();
//		dialog.setTitle("Login à la base de données");
//		dialog.setHeaderText("Fenêtre de login");
//
//		// Creer les boutons
//		ButtonType loginButtonType = new ButtonType("Login",
//				ButtonData.OK_DONE);
//		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType,
//				ButtonType.CANCEL);
//
//		// Create le champ
//		GridPane grid = new GridPane();
//		grid.setHgap(10);
//		grid.setVgap(10);
//		grid.setPadding(new Insets(20, 150, 10, 10));
//
//		TextField username = new TextField();
//		username.setPromptText("Nom");
//		PasswordField password = new PasswordField();
//		password.setPromptText("Mot de passe");
//
//		grid.add(new Label("Nom:"), 0, 0);
//		grid.add(username, 1, 0);
//		grid.add(new Label("Mot de passe:"), 0, 1);
//		grid.add(password, 1, 1);
//
//		// Active la zone de texte si un nom est présent
//		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
//		loginButton.setDisable(true);
//
//		username.textProperty()
//				.addListener((observable, oldValue, newValue) -> {
//					loginButton.setDisable(newValue.trim().isEmpty());
//				});
//
//		dialog.getDialogPane().setContent(grid);
//
//		// Request focus on the username field by default.
//		Platform.runLater(() -> username.requestFocus());
//
//		// Convert the result to a username-password-pair when the login button
//		// is clicked.
//		dialog.setResultConverter(dialogButton -> {
//			if (dialogButton == loginButtonType) {
//				return new Pair<>(username.getText(), password.getText());
//			}
//			return null;
//		});
//
//		Optional<Pair<String, String>> result = dialog.showAndWait();
//		try {
//			Properties props = new Properties();
//			props.setProperty("url", "jdbc:mysql://localhost:3306/pharmasteve");
//			props.setProperty("user", result.get().getKey());
//			props.setProperty("password", result.get().getValue());
//			props.store(new FileWriter("mysql.properties"), "test");
//		} catch (IOException e) {
//			Alert error = new Alert(AlertType.ERROR);
//			error.setContentText(
//					"Erreur connexion : veuillez contacter le SAV");
//			error.show();
//		} catch (NoSuchElementException e) {
//			Connexion.closeInstanceDB();
//		}

//		if (Connexion.getInstanceDB() == null && result.isPresent()) {
//			Alert error = new Alert(AlertType.ERROR);
//			error.setContentText("Login incorrect");
//			error.show();
//		}
		if (Connexion.getInstanceDB() != null) {
			try {
				// Methode de lancement JavaFX
				FXMLLoader loader = new FXMLLoader(
						Main.class.getResource("/view/MainFen.fxml"));
				Parent root = (Parent) loader.load();
				Scene scene = new Scene(root, 663, 558);
				scene.getStylesheets().add(getClass()
						.getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("Menu principal");
				Image img = new Image(
						Main.class.getResourceAsStream("/icon.jpg"));
				primaryStage.getIcons().add(img);
				primaryStage.setResizable(false);
				primaryStage.show();
			} catch (IOException e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText("Erreur affichage");
				error.show();
			}
		}
	}

	/**
	 * Chemin d'entree dans l'application
	 * 
	 * @param args : arguments de l'application
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
