package controladores;

import static controladores.CadastroProdutoController.LOGGER;
import definicoes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class MenuController{
    @FXML
    private Button cadastrarUsuarioButton;
    @FXML
    private Button cadastrarProdutoButton;
    @FXML
    private Button registrarVendaButton;
    @FXML
    private Button relatorioEstoqueButton;
    @FXML
    private Button relatorioComprasButton;
    @FXML
    void handleCadastrarUsuario(){
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/telas/CadastroUsuario.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Usuário");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Erro ao consultar produtos", e);
        }
    }
    @FXML
    private void handleCadastrarProduto(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/CadastroProduto.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cadastro de Produto");
            stage.show();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Erro ao consultar produtos", e);
        }
    }
    @FXML
    private void handleRegistrarVenda(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/RegistroVenda.fxml"));
            Parent root = loader.load();
            RegistroVendaController controller = loader.getController();
            controller.setUsuarioLogado(usuarioLogado);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro de Venda");
            stage.show();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Erro ao consultar produtos", e);
        }
    }
    @FXML
    private void handleRelatorioEstoque(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/RelatorioEstoque.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Relatório de Estoque");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "Erro ao consultar produtos", e);
        }
    }
    @FXML
    private void handleRelatorioCompras(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/telas/RelatorioCompras.fxml"));
            Parent root = loader.load();
            RelatorioComprasController controller = loader.getController();
            controller.setUsuarioLogado(usuarioLogado);
            Stage stage = new Stage();
            stage.setTitle("Relatório de Compra");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "Erro ao abrir o relatório de compras", e);
        }
    }
    @FXML
    void handleSair(ActionEvent event){
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }
    
    private Usuario usuarioLogado;
    
    private String tipoUsuario;

    public void setUsuarioLogado(Usuario usuarioLogado){
        this.usuarioLogado = usuarioLogado;
        if (usuarioLogado != null){
            setTipoUsuario(usuarioLogado.getTipoUsuario());
        }
    }

    private void setTipoUsuario(String tipoUsuario){
        this.tipoUsuario = tipoUsuario;
        ajustarMenu();
    }

    private void ajustarMenu(){
        if ("Funcionario".equals(tipoUsuario)) {
            cadastrarUsuarioButton.setVisible(false);
            cadastrarProdutoButton.setVisible(false);
            relatorioEstoqueButton.setVisible(false);
        } 
        else if ("Admin".equals(tipoUsuario)){
            cadastrarUsuarioButton.setVisible(true);
            cadastrarProdutoButton.setVisible(true);
            registrarVendaButton.setVisible(true);
            relatorioEstoqueButton.setVisible(true);
            relatorioComprasButton.setVisible(true);
        }
    }
}
