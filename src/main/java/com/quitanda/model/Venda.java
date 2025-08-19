package com.quitanda.model;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Importa para usar a data e hora modernas do Java 8+
import java.util.List; // Importa para ter uma lista de itens de venda

public class Venda {
	private int id;
	private LocalDateTime dataVenda;
	private BigDecimal valorTotal;
	private BigDecimal valorPago;
	private BigDecimal troco;
	private List<ItemVenda> itens;
	
	
	// --- Construtores ---

    // Construtor vazio
    public Venda() {
    }
    
    // Construtor para criar uma NOVA venda (sem ID e data, que serão gerados pelo banco)
    public Venda(BigDecimal valorTotal, BigDecimal valorPago, BigDecimal troco, List<ItemVenda> itens) {
        this.valorTotal = valorTotal;
        this.valorPago = valorPago;
        this.troco = troco;
        this.itens = itens;
    }
    
    
 // Construtor COMPLETO (para recuperar uma venda existente do banco de dados)
    public Venda(int id, LocalDateTime dataVenda, BigDecimal valorTotal, BigDecimal valorPago, BigDecimal troco, List<ItemVenda> itens) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.valorPago = valorPago;
        this.troco = troco;
        this.itens = itens;
    }
    
 // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    // --- Método toString() ---
    @Override
    public String toString() {
        return "Venda{" +
               "id=" + id +
               ", dataVenda=" + dataVenda +
               ", valorTotal=" + valorTotal +
               ", valorPago=" + valorPago +
               ", troco=" + troco +
               ", itens=" + (itens != null ? itens.size() : 0) + " itens" + // Mostra quantos itens estão associados
               '}';
    }
}
