package com.quitanda.model;

import java.math.BigDecimal;


public class Produto {
	// Atributos: correspondem às colunas da tabela 'produtos'
	private int id; //Corresponde à coluna 'id' no banco
	private String nome; //corresponde a coluna nome
	private BigDecimal precoCompra;
	private BigDecimal precoVenda;
	private int quantidadeEstoque;
	
	
	// -- Contrutores--
	
	public Produto() {	
	}
	
	public Produto (int id, String nome, BigDecimal precoCompra, BigDecimal precoVenda, int quantidadeEstoque) {
		this.id = id;
		this.nome = nome;
		this.precoCompra = precoCompra;
		this.precoVenda = precoVenda;
		this.quantidadeEstoque = quantidadeEstoque; 
	}
	
	public Produto (String nome, BigDecimal precoCompra, BigDecimal precoVenda, int quantidadeEstoque) {
		this.nome = nome;
		this.precoCompra = precoCompra;
		this.precoVenda = precoVenda;
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public BigDecimal getPrecoCompra() {
		return precoCompra;
	}
	
	public void setPrecoCompra(BigDecimal precoCompra) {
		this.precoCompra = precoCompra;
	}
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	
	public int getQuantidadeEstoque() {
		return quantidadeEstoque;
	}
	
	public void  setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
		
	}
	
	// -- Metodo toString()--
	//util para depuração
	
	@Override
    public String toString() {
        return "Produto{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", precoCompra=" + precoCompra +
               ", precoVenda=" + precoVenda +
               ", quantidadeEstoque=" + quantidadeEstoque +
               '}';
    }
}
