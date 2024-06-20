package gestao;

import static controladores.CadastroProdutoController.LOGGER;
import definicoes.Database;
import definicoes.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GestaoCadastroProduto{
    private final List<Produto> produtos = new ArrayList<>();
    public void cadastrarProduto(Produto produto){
        produtos.add(produto);
        salvarProdutoNoBanco(produto);
    }
    private void salvarProdutoNoBanco(Produto produto){
        String sql = "INSERT INTO Produto (codigoBarras, nome, descricao, quantidade, dataValidade, valorProduto, lote) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, produto.getCodigoBarras());
            pstmt.setString(2, produto.getNome());
            pstmt.setString(3, produto.getDescricao());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setDate(5, java.sql.Date.valueOf(produto.getDataValidade()));
            pstmt.setDouble(6, produto.getValorProduto());
            pstmt.setString(7, produto.getLote());
            pstmt.executeUpdate();
        } catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar produto", e);
        }
    }
    
    public ObservableList<Produto> consultarProdutos(){
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                String codigoBarras = rs.getString("codigoBarras");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int quantidade = rs.getInt("quantidade");
                java.sql.Date dataValidade = rs.getDate("dataValidade");
                double valorProduto = rs.getDouble("valorProduto");
                String lote = rs.getString("lote");

                Produto produto = new Produto(nome, descricao, quantidade, codigoBarras,
                        dataValidade != null ? dataValidade.toLocalDate() : null,
                        valorProduto, lote);
                produtos.add(produto);
            }
        } catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Erro ao consultar produtos", e);
        }
        return FXCollections.observableArrayList(produtos);
    }
}
