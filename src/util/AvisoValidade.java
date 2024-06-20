package util;

import definicoes.Produto;

import java.time.LocalDate;
import java.util.List;

public class AvisoValidade{
    public void verificarValidade(List<Produto> produtos){
        LocalDate hoje = LocalDate.now();
        for(Produto produto : produtos){
            if (produto.getDataValidade().isBefore(hoje.plusDays(30))){
                System.out.println(
                        "Aviso: O produto " + produto.getNome() +
                                " está próximo da data de validade.");
            }
        }
    }
}
