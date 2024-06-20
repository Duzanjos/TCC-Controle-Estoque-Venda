package controladores;

import definicoes.Database;
import definicoes.Produto;
import definicoes.Usuario;
import definicoes.Venda;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class RelatorioComprasController{
    @FXML
    private TableView<Venda> tableView;
    @FXML
    private TableColumn<Venda, String> codigoBarrasColumn;
    @FXML
    private TableColumn<Venda, String> nomeColumn;
    @FXML
    private TableColumn<Venda, String> descricaoColumn;
    @FXML
    private TableColumn<Venda, Integer> quantidadeColumn;
    @FXML
    private TableColumn<Venda, String> dataCompraColumn;
    @FXML
    private TableColumn<Venda, Double> valorTotalColumn;
    @FXML
    public void initialize(){
        codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        dataCompraColumn.setCellValueFactory(new PropertyValueFactory<>("dataCompra"));
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        quantidadeColumn.setCellFactory(column -> new TableCell<Venda, Integer>(){
            @Override
            protected void updateItem(Integer item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null || item == 0){
                    setText("");
                }
                else{
                    setText(item.toString());
                }
            }
        });
        carregarVendas();
    }

    private Usuario usuarioLogado;

    public void setUsuarioLogado(Usuario usuarioLogado){
        this.usuarioLogado = usuarioLogado;
        carregarVendas();
    }

    private void carregarVendas(){
        if (usuarioLogado != null){
            List<Venda> vendas = Database.buscarVendasPorUsuario(usuarioLogado.getLogin());
            codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
            nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
            descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
            quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
            dataCompraColumn.setCellValueFactory(new PropertyValueFactory<>("dataCompra"));
            valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
            vendas.removeIf(venda -> venda.getProduto() == null);
            tableView.getItems().setAll(vendas);
            double totalCompra = vendas.stream()
                    .mapToDouble(venda -> venda.getProduto() != null ? venda.getValorTotal() : 0)
                    .sum();
            Produto produtoFicticio = new Produto(null, null, 0, "Total Geral", null, 0.0, null);
            Venda totalVenda = new Venda(produtoFicticio, 0, null);
            totalVenda.setValorTotal(totalCompra);
            tableView.getItems().add(totalVenda);
        }
    }   
}
