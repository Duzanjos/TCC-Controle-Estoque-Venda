package util;

import definicoes.Produto;

import java.util.List;

public class Estoque{
    public void visualizarEstoque(List<Produto> produtos){
        System.out.println("Estoque Atual: ");
        for(Produto produto : produtos){
            System.out.println(
                    "Nome: " + produto.getNome() + ", Quantidade: " + produto.getQuantidade());
        }
    }
}
