package controladores;

import definicoes.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class CadastroUsuarioController implements Initializable{
    @FXML
    private TextField cadastroLoginField;
    @FXML
    private PasswordField cadastroPassword2Field;
    @FXML
    private PasswordField cadastroPasswordField;
    @FXML
    private Button cancelarUsuarioButton;
    @FXML
    private ChoiceBox<String> tipoUsuarioChoiceBox;
    @FXML
    private Button deletarUsuarioButton;

    @FXML
    void getTipoUsuario() {
        tipoUsuarioChoiceBox.getSelectionModel().getSelectedItem();
    }

    @FXML
    void handleCadastrarButtonAction(){
        String login = cadastroLoginField.getText();
        String senha = cadastroPasswordField.getText();
        String confirmarSenha = cadastroPassword2Field.getText();
        String tipoUsuario = tipoUsuarioChoiceBox.getValue();
        if (login.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty() || tipoUsuario == null){
            showAlert("Erro de Cadastro", "Todos os campos devem ser preenchidos.");
            return;
        }
        if (!senha.equals(confirmarSenha)){
            showAlert("Erro de Cadastro", "As senhas não correspondem.");
            return;
        }
        Database.insertUser(login, senha, tipoUsuario);
        showAlert("Sucesso", "Usuário cadastrado com sucesso!");
    }

    @FXML
    void handleCancelarButtonAction(){
        Stage stage = (Stage) cancelarUsuarioButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleLimparButtonAction(){
        cadastroLoginField.clear();
        cadastroPasswordField.clear();
        cadastroPassword2Field.clear();
        tipoUsuarioChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    void handleDeletarUsuarioButtonAction() {
        String login = cadastroLoginField.getText();
        if (login.isEmpty()) {
            showAlert("Erro de Deleção", "O campo de login deve ser preenchido.");
            return;
        }
        if ("admin".equalsIgnoreCase(login)) {
            showAlert("Erro de Deleção", "Não é possível deletar o usuário admin.");
            return;
        }
        if (!Database.userExists(login)) {
            showAlert("Erro de Deleção", "Usuário não encontrado.");
            return;
        }
        Database.deleteUser(login);
        showAlert("Sucesso", "Usuário deletado com sucesso!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        tipoUsuarioChoiceBox.getItems().addAll("Admin", "Funcionario");
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
