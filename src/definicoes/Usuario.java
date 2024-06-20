package definicoes;

import java.util.ArrayList;
import java.util.List;

public class Usuario{
    private final String login;
    private final String senha;
    private final String tipoUsuario;
    private final List<Venda> compras;

    public Usuario(String login, String senha, String tipoUsuario){
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.compras = new ArrayList<>();
    }
    public void adicionarCompra(Venda compra){
        this.compras.add(compra);
    }
    public String getLogin(){
        return login;
    }
    public String getSenha(){
        return senha;
    }
    public String getTipoUsuario(){
        return tipoUsuario;
    }
    public List<Venda> getCompras(){
        return compras;
    }
}