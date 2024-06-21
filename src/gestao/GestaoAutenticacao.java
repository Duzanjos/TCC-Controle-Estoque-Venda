package gestao;

import static controladores.CadastroProdutoController.LOGGER;
import definicoes.Database;
import definicoes.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GestaoAutenticacao{
    private final List<Usuario> usuarios = new ArrayList<>();
    private final Connection connection;
    public GestaoAutenticacao() throws SQLException{
        this.connection = Database.connect();
        carregarUsuarios();
    }
    
    private void carregarUsuarios(){
        String sql = "SELECT login, senha, tipo_usuario FROM Usuario";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                String login = rs.getString("login");
                String senha = rs.getString("senha");
                String tipoUsuario = rs.getString("tipo_usuario");
                Usuario usuario = new Usuario(login, senha, tipoUsuario);
                usuarios.add(usuario);
            }
        } catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar produto", e);
        }
    }

    public Usuario logar(String login, String senha){
        for (Usuario usuario : usuarios){
            if (usuario.getLogin().equalsIgnoreCase(login) && usuario.getSenha().equals(senha)){
                return usuario;
            }
        }
        return null;
    }
}