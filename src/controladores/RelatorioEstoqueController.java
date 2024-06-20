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

public class RelatorioEstoqueController {
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
    private final GestaoCadastroProduto gestaoCadastroProduto = new GestaoCadastroProduto();

    @FXML
    public void initialize() {
        codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        dataValidadeColumn.setCellValueFactory(new PropertyValueFactory<>("dataValidade"));
        valorProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("valorProduto"));
        loteColumn.setCellValueFactory(new PropertyValueFactory<>("lote"));
        ObservableList<Produto> produtos = gestaoCadastroProduto.consultarProdutos();
        tableView.setItems(produtos);

        // Verificar validade dos produtos e exibir avisos
        List<Produto> produtosProximosDaValidade = produtos.stream()
                .filter(produto -> produto.getDataValidade().isBefore(LocalDate.now().plusDays(30)))
                .collect(Collectors.toList());
        
        if (!produtosProximosDaValidade.isEmpty()) {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/AvisoValidade.fxml"));
                    Parent root = loader.load();
                    AvisoValidadeController controller = loader.getController();
                    controller.setProdutos(produtosProximosDaValidade);
                    
                    Stage stage = new Stage();
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(tableView.getScene().getWindow());  // Define a janela de relatório como o dono da nova janela
                    
                    stage.setScene(new Scene(root));
                    stage.setTitle("Aviso de Validade");
                    stage.showAndWait();  // Mostra a janela e aguarda até que seja fechada
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
