package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


/** Main de l'application
 * @author SRet
 *
 */
public class Main extends Application {

	/**
	 * Methode de lancement de l'application
	 * @param primaryStage : fenetre a afficher
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(
					Main.class.getResource("/view/MainFen.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root, 663, 558);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Menu principal");
			Image img = new Image(
					Main.class.getResourceAsStream("/icon.jpg"));
			primaryStage.getIcons().add(img);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Chemin d'entree dans l'application
	 * @param args : arguments de l'application
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
