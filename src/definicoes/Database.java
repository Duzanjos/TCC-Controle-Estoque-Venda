package definicoes;

import javafx.scene.control.Alert;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTables() {
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS Usuario (" +
                            "login TEXT PRIMARY KEY, " +
                            "senha TEXT NOT NULL, " +
                            "tipo_usuario TEXT NOT NULL)";
        
        String sqlProduto = "CREATE TABLE IF NOT EXISTS Produto (" +
                            "codigoBarras TEXT PRIMARY KEY, " +
                            "nome TEXT NOT NULL, " +
                            "descricao TEXT, " +
                            "quantidade INTEGER NOT NULL, " +
                            "dataValidade DATE, " +
                            "valorProduto REAL NOT NULL, " +
                            "lote TEXT)";
        
        String sqlVenda = "CREATE TABLE IF NOT EXISTS Venda (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                          "produto_codigoBarras TEXT NOT NULL, " +
                          "quantidade INTEGER NOT NULL, " +
                          "dataCompra DATE NOT NULL, " +
                          "usuario_login TEXT NOT NULL, " +
                          "FOREIGN KEY (produto_codigoBarras) REFERENCES Produto(codigoBarras), " +
                          "FOREIGN KEY (usuario_login) REFERENCES Usuario(login))";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuario);
            stmt.execute(sqlProduto);
            stmt.execute(sqlVenda);
            System.out.println("Tables created or verified.");

            createDefaultAdmin(conn);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createDefaultAdmin(Connection conn) {
        String sqlCheckAdmin = "SELECT COUNT(*) FROM Usuario WHERE login = 'admin'";
        String sqlInsertAdmin = "INSERT INTO Usuario (login, senha, tipo_usuario) VALUES ('admin', 'admin', 'admin')";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheckAdmin)) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    stmt.execute(sqlInsertAdmin);
                    System.out.println("Usuário padrão Admin criado.");
                } else {
                    System.out.println("Admin user already exists.");
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertUser(String login, String senha, String tipoUsuario) {
        String sql = "INSERT INTO Usuario (login, senha, tipo_usuario) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            pstmt.setString(3, tipoUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean userExists(String login) {
        String sql = "SELECT COUNT(*) FROM Usuario WHERE login = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void deleteUser(String login) {
        if ("admin".equalsIgnoreCase(login)) {
            System.out.println("Não é possível deletar o usuário admin.");
            return;
        }
        if (!userExists(login)) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        String sql = "DELETE FROM Usuario WHERE login = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.executeUpdate();
            System.out.println("Usuário deletado com sucesso.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertVenda(Venda venda, String compradorLogin) {
        String sqlSelectEstoque = "SELECT quantidade FROM Produto WHERE codigoBarras = ?";
        String sqlInsertVenda = "INSERT INTO Venda (produto_codigoBarras, quantidade, dataCompra, usuario_login) VALUES (?, ?, ?, ?)";
        String sqlUpdateEstoque = "UPDATE Produto SET quantidade = quantidade - ? WHERE codigoBarras = ?";

        try (Connection conn = connect()) {
            try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelectEstoque)) {
                pstmtSelect.setString(1, venda.getProduto().getCodigoBarras());
                ResultSet rs = pstmtSelect.executeQuery();

                if (rs.next()) {
                    int quantidadeDisponivel = rs.getInt("quantidade");
                    if (quantidadeDisponivel < venda.getQuantidade()) {
                        showAlert("Quantidade em estoque insuficiente para a venda.");
                        return;
                    }
                } else {
                    showAlert("Produto não encontrado no estoque.");
                    return;
                }
            }
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertVenda)) {
                pstmtInsert.setString(1, venda.getProduto().getCodigoBarras());
                pstmtInsert.setInt(2, venda.getQuantidade());
                pstmtInsert.setDate(3, java.sql.Date.valueOf(venda.getDataCompra()));
                pstmtInsert.setString(4, compradorLogin);
                pstmtInsert.executeUpdate();
            }
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateEstoque)) {
                pstmtUpdate.setInt(1, venda.getQuantidade());
                pstmtUpdate.setString(2, venda.getProduto().getCodigoBarras());
                pstmtUpdate.executeUpdate();
            }

        } catch (SQLException e) {
            showAlert("Erro ao processar a venda: " + e.getMessage());
        }
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean productExists(String codigoBarras) {
        String sql = "SELECT COUNT(*) FROM Produto WHERE codigoBarras = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoBarras);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void deleteProduct(String codigoBarras) {
        if (!productExists(codigoBarras)) {
            System.out.println("Produto não encontrado.");
            return;
        }
        String sql = "DELETE FROM Produto WHERE codigoBarras = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoBarras);
            pstmt.executeUpdate();
            System.out.println("Produto deletado com sucesso.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Produto buscarProdutoPorCodigo(String codigoBarras) {
        String sql = "SELECT * FROM Produto WHERE codigoBarras = ?";
        Produto produto = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoBarras);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int quantidade = rs.getInt("quantidade");
                String codigo = rs.getString("codigoBarras");
                LocalDate dataValidade = rs.getDate("dataValidade") != null ? rs.getDate("dataValidade").toLocalDate() : null;
                double valorProduto = rs.getDouble("valorProduto");
                String lote = rs.getString("lote");
                produto = new Produto(nome, descricao, quantidade, codigo, dataValidade, valorProduto, lote);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return produto;
    }

    public static List<Venda> buscarVendasPorUsuario(String usuarioLogin) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.produto_codigoBarras, v.quantidade, v.dataCompra, p.nome, p.descricao, p.valorProduto "
                + "FROM Venda v "
                + "JOIN Produto p ON v.produto_codigoBarras = p.codigoBarras "
                + "WHERE v.usuario_login = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuarioLogin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String codigoBarras = rs.getString("produto_codigoBarras");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int quantidade = rs.getInt("quantidade");
                Date dataCompra = rs.getDate("dataCompra");
                double valorProduto = rs.getDouble("valorProduto");
                Produto produto = new Produto(nome, descricao, quantidade, codigoBarras, null, valorProduto, null);
                Venda venda = new Venda(produto, quantidade, dataCompra.toLocalDate());
                vendas.add(venda);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return vendas;
    }
}
