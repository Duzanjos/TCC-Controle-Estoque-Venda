package definicoes;

import java.time.LocalDate;

public class Venda{

    private Produto produto;
    private int quantidade;
    private final LocalDate dataCompra;
    private double valorTotal;

    public Venda(Produto produto, int quantidade, LocalDate dataCompra){
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataCompra = dataCompra;
        calcularValorTotal();
    }
    public Venda(int quantidade, LocalDate dataCompra){
        this(null, quantidade, dataCompra);
    }
    private void calcularValorTotal(){
        if (produto != null){
            this.valorTotal = produto.getValorProduto() * quantidade;
        } else{
            this.valorTotal = 0;
        }
    }
    public String getCodigoBarras(){
        return produto != null ? produto.getCodigoBarras() : "N/A";
    }
    public String getNome(){
        return produto != null ? produto.getNome() : "Produto desconhecido";
    }
    public String getDescricao(){
        return produto != null ? produto.getDescricao() : "Descrição indisponível";
    }
    public double getValorTotal(){
        return valorTotal;
    }
    public void setValorTotal(double valorTotal){
        this.valorTotal = valorTotal;
    }
    public Produto getProduto(){
        return produto;
    }
    public void setProduto(Produto produto){
        this.produto = produto;
        calcularValorTotal();
    }
    public int getQuantidade(){
        return quantidade;
    }
    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
        calcularValorTotal();
    }
    public LocalDate getDataCompra(){
        return dataCompra;
    }
}
