package controladores;

import definicoes.Database;
import definicoes.Produto;
import definicoes.Usuario;
import definicoes.Venda;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RegistroVendaController{
    @FXML
    private Button cancelarVendaButton;
    @FXML
    private TextField codigoBarrasTextField;
    @FXML
    private TextField precoTextField;
    @FXML
    private TextField valorTotalTextField;
    @FXML
    private TextField nomeProdutoField;
    @FXML
    private Spinner<Integer> quantidadeSpinner;
    @FXML
    void handleCadastrarButtonAction(){
        if (produtoAtual == null){
            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum produto selecionado.");
            return;
        }

        if (usuarioLogado == null){
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não logado.");
            return;
        }

        Integer quantidade = quantidadeSpinner.getValue();
        double precoUnitario = produtoAtual.getValorProduto();
        double valorTotal = precoUnitario * quantidade;
        valorTotalTextField.setText("R$ " + String.format("%.2f", valorTotal));

        Produto produto = Database.buscarProdutoPorCodigo(produtoAtual.getCodigoBarras());
        if (produto != null && produto.getQuantidade() < quantidade){
            showAlert(Alert.AlertType.ERROR, "Erro", "Quantidade em estoque insuficiente para a venda.");
            return;
        }
        Venda venda = new Venda(produtoAtual, quantidade, java.time.LocalDate.now());
        Database.insertVenda(venda, usuarioLogado.getLogin());
        showAlert(Alert.AlertType.INFORMATION, "Venda Registrada", "Venda registrada com sucesso!");
        limparCampos();
    }
    @FXML
    void handleCancelarButtonAction(){
        Stage stage = (Stage) cancelarVendaButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    void handleLimparButtonAction(){
        limparCampos();
    }
    @FXML
    void initialize(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantidadeSpinner.setValueFactory(valueFactory);
        codigoBarrasTextField.setOnKeyPressed(this::handleCodigoBarrasKeyPressed);
        quantidadeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> atualizarValorTotal());
    }
    private Usuario usuarioLogado;
    
    private Produto produtoAtual;

    public void setUsuarioLogado(Usuario usuario){
        this.usuarioLogado = usuario;
    }
    
    private void handleCodigoBarrasKeyPressed(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
            String codigoBarras = codigoBarrasTextField.getText().trim();
            if (codigoBarras.isEmpty()){
                showAlert(Alert.AlertType.ERROR, "Erro", "O campo de código de barras não pode estar vazio.");
                return;
            }

            Produto produto = Database.buscarProdutoPorCodigo(codigoBarras);
            if (produto == null){
                showAlert(Alert.AlertType.ERROR, "Erro", "Produto não encontrado.");
                return;
            }

            produtoAtual = produto;
            nomeProdutoField.setText(produto.getNome());
            precoTextField.setText(String.format("R$ %.2f", produto.getValorProduto()));
            atualizarValorTotal();
        }
    }
    
    private void atualizarValorTotal(){
        if (produtoAtual != null){
            double precoUnitario = produtoAtual.getValorProduto();
            int quantidade = quantidadeSpinner.getValue();
            double valorTotal = precoUnitario * quantidade;
            valorTotalTextField.setText(String.format("R$ %.2f", valorTotal));
        }
    }
    
    private void limparCampos(){
        codigoBarrasTextField.clear();
        precoTextField.clear();
        valorTotalTextField.setText("");
        nomeProdutoField.clear();
        quantidadeSpinner.getValueFactory().setValue(1);
        produtoAtual = null;
        atualizarValorTotal();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
