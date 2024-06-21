package controladores;

import static controladores.CadastroProdutoController.LOGGER;
import definicoes.Usuario;
import gestao.GestaoAutenticacao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.logging.Level;

public class LoginController{
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private void handleLoginButtonAction(){
        String login = loginField.getText();
        String password = passwordField.getText();
        Usuario usuarioLogado = auth.logar(login, password);

        if (usuarioLogado != null){
            showAlert(Alert.AlertType.INFORMATION, "Login Bem-sucedido", "Bem-vindo, " + usuarioLogado.getLogin() + "!");
            abrirMenuPrincipal(usuarioLogado);
        } else{
            showAlert(Alert.AlertType.ERROR, "Login Falhou", "Login ou senha incorretos.");
        }
    }
    
    private final GestaoAutenticacao auth;

    public LoginController() throws SQLException{
        auth = new GestaoAutenticacao();
    }
    
    private void abrirMenuPrincipal(Usuario setTipoUsuario){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/Menu.fxml"));
            Parent root = loader.load();
            MenuController menuController = loader.getController();
            menuController.setUsuarioLogado(setTipoUsuario);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao cadastrar o produto.");
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar produto", e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
