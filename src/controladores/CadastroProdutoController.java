package controladores;

import definicoes.Produto;
import definicoes.Database;
import gestao.GestaoCadastroProduto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CadastroProdutoController{
    @FXML
    private TextField nomeField;
    @FXML
    private TextField descricaoField;
    @FXML
    private TextField quantidadeField;
    @FXML
    private TextField codigoBarrasField;
    @FXML
    private TextField dataValidadeField;
    @FXML
    private TextField valorProdutoField;
    @FXML
    private TextField loteField;
    @FXML
    private Button cancelarButton;
    @FXML
    public Button limparButton;
    @FXML
    public Button cadastrarButton;
    @FXML
    public Button deletarProdutoButton;
    @FXML
    private void handleCadastrarButtonAction(){
        try{
            Produto produto = criarProduto();
            gestaoCadastroProduto.cadastrarProduto(produto);
            showAlert(Alert.AlertType.INFORMATION, "Cadastro Bem-sucedido", "Produto cadastrado com sucesso.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Por favor, verifique os campos numéricos.");
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Data", "Por favor, insira uma data válida no formato AAAA-MM-DD.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao cadastrar o produto.");
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar produto", e);
        }
    }
    @FXML
    private void handleLimparButtonAction(){
        nomeField.clear();
        descricaoField.clear();
        quantidadeField.clear();
        codigoBarrasField.clear();
        dataValidadeField.clear();
        valorProdutoField.clear();
        loteField.clear();
    }
    @FXML
    private void handleCancelarButtonAction(){
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleDeletarProdutoButtonAction(){
        String codigoBarras = codigoBarrasField.getText();
        if (codigoBarras.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro ao deletar", "O campo de código de barras deve ser preenchido.");
            return;
        }
        if (!Database.productExists(codigoBarras)){
            showAlert(Alert.AlertType.ERROR, "Erro ao deletar", "Produto não encontrado.");
            return;
        }
        Database.deleteProduct(codigoBarras);
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Produto deletado com sucesso!");
    }

    public static final Logger LOGGER = Logger.getLogger(CadastroProdutoController.class.getName());
    
    private final GestaoCadastroProduto gestaoCadastroProduto;

    public CadastroProdutoController(){
        gestaoCadastroProduto = new GestaoCadastroProduto();
    }

    private Produto criarProduto(){
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        int quantidade = Integer.parseInt(quantidadeField.getText());
        String codigoBarras = codigoBarrasField.getText();
        LocalDate dataValidade = LocalDate.parse(dataValidadeField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
        double valorProduto = Double.parseDouble(valorProdutoField.getText().replace(",", "."));
        String lote = loteField.getText();
        return new Produto(nome, descricao, quantidade, codigoBarras, dataValidade, valorProduto, lote);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
