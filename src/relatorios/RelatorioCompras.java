package relatorios;

import definicoes.Produto;
import definicoes.Usuario;
import definicoes.Venda;
import java.util.List;

public class RelatorioCompras{
    public void gerarRelatorioUsuarios(List<Usuario> usuarios){
        for (Usuario usuario : usuarios){
            System.out.println("Relatório de Compras - Usuário: "+usuario.getLogin());
            double totalGeral = 0;
            for (Venda compra : usuario.getCompras()){
                Produto produto = compra.getProduto();
                int quantidade = compra.getQuantidade();
                double totalProduto = quantidade * produto.getValorProduto();
                totalGeral += totalProduto;
                System.out.println("Data: " + compra.getDataCompra() +
                        ", Produto: " + produto.getNome() +
                        ", Quantidade: " + quantidade +
                        ", Total: " + totalProduto);
            }
            System.out.println("Total Geral: "+ totalGeral);
        }
    }
}