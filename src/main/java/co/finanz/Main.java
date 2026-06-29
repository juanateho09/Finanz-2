package co.finanz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación Finanz.
 * Carga la vista principal (dashboard) al iniciar.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/co/finanz/fxml/dashboard.fxml")
        );
        Scene scene = new Scene(loader.load(), 1100, 700);
        scene.getStylesheets().add(
                getClass().getResource("/co/finanz/css/styles.css").toExternalForm()
        );
        primaryStage.setTitle("Finanz — Gestión Financiera");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
