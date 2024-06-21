package controladores;

import definicoes.Produto;
import gestao.GestaoCadastroProduto;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioEstoqueController{
    @FXML
    private TableView<Produto> tableView;
    @FXML
    private TableColumn<Produto, String> codigoBarrasColumn;
    @FXML
    private TableColumn<Produto, String> nomeColumn;
    @FXML
    private TableColumn<Produto, String> descricaoColumn;
    @FXML
    private TableColumn<Produto, Integer> quantidadeColumn;
    @FXML
    private TableColumn<Produto, String> dataValidadeColumn;
    @FXML
    private TableColumn<Produto, Double> valorProdutoColumn;
    @FXML
    private TableColumn<Produto, String> loteColumn;
    @FXML
    public void initialize(){
        codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        dataValidadeColumn.setCellValueFactory(new PropertyValueFactory<>("dataValidade"));
        valorProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("valorProduto"));
        loteColumn.setCellValueFactory(new PropertyValueFactory<>("lote"));
        ObservableList<Produto> produtos = gestaoCadastroProduto.consultarProdutos();
        tableView.setItems(produtos);
        List<Produto> produtosProximosDaValidade = produtos.stream()
                .filter(produto -> produto.getQuantidade() > 0 && produto.getDataValidade() != null && produto.getDataValidade().isAfter(LocalDate.now()) && produto.getDataValidade().isBefore(LocalDate.now().plusDays(30)))
                .collect(Collectors.toList());
        
        List<Produto> produtosVencidos = produtos.stream()
                .filter(produto -> produto.getQuantidade() > 0 && produto.getDataValidade() != null && produto.getDataValidade().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        if (!produtosProximosDaValidade.isEmpty()){
            Platform.runLater(() -> showAlert(produtosProximosDaValidade, "Produtos próximos da data de validade"));
        }

        if (!produtosVencidos.isEmpty()){
            Platform.runLater(() -> showAlert(produtosVencidos, "Produtos vencidos"));
        }
    }
    
    private void showAlert(List<Produto> produtos, String title){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/AvisoValidade.fxml"));
            Parent root = loader.load();
            AvisoValidadeController controller = loader.getController();
            controller.setProdutos(produtos);
            controller.setTitle(title);

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tableView.getScene().getWindow());

            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private final GestaoCadastroProduto gestaoCadastroProduto = new GestaoCadastroProduto();
    
    
}
