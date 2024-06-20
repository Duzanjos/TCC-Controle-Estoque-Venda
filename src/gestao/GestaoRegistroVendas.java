package gestao;

import definicoes.Produto;
import definicoes.Usuario;
import definicoes.Venda;
import java.time.LocalDate;

public class GestaoRegistroVendas{
    public void registrarVenda(Usuario comprador, Produto produto, int quantidadeVendida){
        if (produto.getQuantidade() >= quantidadeVendida){
            produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);
            Venda compra = new Venda(produto, quantidadeVendida, LocalDate.now());
            comprador.adicionarCompra(compra);
            System.out.println("Venda registrada: Produto " + produto.getNome() + ", Quantidade: " +
                    quantidadeVendida + ", Valor: R$ " + (produto.getValorProduto() * quantidadeVendida));
        }
        else{
            System.out.println("Quantidade insuficiente em estoque para " + produto.getNome());
        }
    }
}