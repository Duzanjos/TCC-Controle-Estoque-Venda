package definicoes;

import java.time.LocalDate;

public class Produto{
    private final String nome;
    private final String descricao;
    private int quantidade;
    private final String codigoBarras;
    private final LocalDate dataValidade;
    private final double valorProduto;
    private final String lote;

    public Produto(String nome, String descricao, int quantidade, String codigoBarras, LocalDate dataValidade, double valorProduto, String lote){
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.codigoBarras = codigoBarras;
        this.dataValidade = dataValidade;
        this.valorProduto = valorProduto;
        this.lote = lote;
    }
    public String getNome(){
        return nome;
    }
    public String getDescricao(){
        return descricao;
    }
    public int getQuantidade(){
        return quantidade;
    }
    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
    public String getCodigoBarras(){
        return codigoBarras;
    }
    public LocalDate getDataValidade(){
        return dataValidade;
    }
    public double getValorProduto(){
        return valorProduto;
    }
    public String getLote(){
        return lote;
    }
}
