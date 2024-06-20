package relatorios;

import definicoes.Produto;
import java.util.List;

public class RelatorioEstoque{
    public void gerarRelatorio(List<Produto> produtos){
        System.out.println("Relatório de Estoque:");
        for (Produto produto : produtos){
            System.out.println("Nome: " + produto.getNome() +
                    ", Descrição: " + produto.getDescricao() +
                    ", Quantidade: " + produto.getQuantidade() +
                    ", Código de Barras: " + produto.getCodigoBarras() +
                    ", Data de Validade: " + produto.getDataValidade() +
                    ", Preço: R$ "+ produto.getValorProduto() +
                    ", Lote: "+ produto.getLote());
        }
    }
}
