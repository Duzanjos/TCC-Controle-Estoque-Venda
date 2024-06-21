package controladores;

import definicoes.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;

public class AvisoValidadeController{
    @FXML
    private TableView<Produto> tableView;
    @FXML
    private TableColumn<Produto, String> codigoBarrasColumn;
    @FXML
    private TableColumn<Produto, String> nomeColumn;
    @FXML
    private TableColumn<Produto, LocalDate> dataValidadeColumn;
    @FXML
    private TableColumn<Produto, String> loteColumn;
    @FXML
    public void initialize(){
        codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        dataValidadeColumn.setCellValueFactory(new PropertyValueFactory<>("dataValidade"));
        loteColumn.setCellValueFactory(new PropertyValueFactory<>("lote"));
        tableView.setItems(produtos);
    }
    
    private ObservableList<Produto> produtos = FXCollections.observableArrayList();
    
    public void setProdutos(List<Produto> produtos){
        this.produtos.setAll(produtos);
    }
}
