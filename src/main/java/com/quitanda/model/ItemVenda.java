package com.quitanda.model;

import java.math.BigDecimal;

public class ItemVenda {
	private int id;
    private int vendaId;      // Chave estrangeira para a tabela 'vendas'
    private int produtoId;    // Chave estrangeira para a tabela 'produtos'
    private int quantidade;
    private BigDecimal precoUnitarioVenda;
    
    private Produto produto;
    
 // --- Construtores ---

    // Construtor vazio
    public ItemVenda() {
    }
    
    // Construtor para criar um NOVO item de venda (sem ID, que será gerado pelo banco)
    public ItemVenda(int vendaId, int produtoId, int quantidade, BigDecimal precoUnitarioVenda) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitarioVenda = precoUnitarioVenda;
    }
    
 // Construtor COMPLETO (para recuperar um item existente do banco de dados)
    public ItemVenda(int id, int vendaId, int produtoId, int quantidade, BigDecimal precoUnitarioVenda) {
        this.id = id;
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitarioVenda = precoUnitarioVenda;
    }
    
    // Construtor que inclui o objeto Produto (útil quando você carrega o item do banco e
    // já quer associar o objeto Produto completo a ele para fácil acesso).
    public ItemVenda(int id, int vendaId, int produtoId, int quantidade, BigDecimal precoUnitarioVenda, Produto produto) {
        this.id = id;
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitarioVenda = precoUnitarioVenda;
        this.produto = produto; // Atribui o objeto Produto
    }
    
 // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitarioVenda() {
        return precoUnitarioVenda;
    }

    public void setPrecoUnitarioVenda(BigDecimal precoUnitarioVenda) {
        this.precoUnitarioVenda = precoUnitarioVenda;
    }

    public Produto getProduto() { // Getter para o objeto Produto associado
        return produto;
    }

    public void setProduto(Produto produto) { // Setter para o objeto Produto associado
        this.produto = produto;
    }

    // --- Método toString() ---
    @Override
    public String toString() {
        return "ItemVenda{" +
               "id=" + id +
               ", vendaId=" + vendaId +
               ", produtoId=" + produtoId +
               ", quantidade=" + quantidade +
               ", precoUnitarioVenda=" + precoUnitarioVenda +
               ", produto=" + (produto != null ? produto.getNome() : "N/A") + // Se tiver um produto associado, mostra o nome
               '}';
    }
    	    
}
