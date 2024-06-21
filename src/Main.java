import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import definicoes.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/telas/Login.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Estoque e Vendas - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Erro ao carregar o FXML", e);
        }
    }
    public static void main(String[] args){
        Database.createTables();
        launch(args);
    }
}