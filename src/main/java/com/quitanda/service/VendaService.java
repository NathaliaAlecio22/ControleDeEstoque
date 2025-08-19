package com.quitanda.service;


import com.quitanda.dao.ProdutoDAO;
import com.quitanda.dao.VendaDAO;
import com.quitanda.model.ItemVenda;
import com.quitanda.model.Produto;
import com.quitanda.model.Venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendaService {
	private VendaDAO vendaDAO = new VendaDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
	
	/**
     * Realiza uma venda, incluindo cálculos, validações e registro.
     * Esta é a camada de serviço que orquestra a operação.
     *
     * @param itensParaVenda A lista de itens que o cliente quer comprar (com ID do produto e quantidade).
     * @param valorPago Pelo cliente.
     * @return O objeto Venda registrado ou null se a venda falhar.
     */
    
    public Venda realizarVenda(List<ItemVenda> itensParaVenda, BigDecimal valorPago) {
    	BigDecimal valorTotal = BigDecimal.ZERO;
    	List<ItemVenda> itensComDetalhes = new ArrayList<>();
    	
    	// 1. Validar e buscar detalhes de cada produto e calcular o valor total
    	for(ItemVenda item : itensParaVenda) {
    		Produto produto = produtoDAO.buscarProdutoPorID(item.getProdutoId());
    		
    		if(produto == null) {
    			System.err.println("Erro: Produto com ID" + item.getProdutoId());
    			return null;
    		}
    		if (produto.getQuantidadeEstoque()<item.getQuantidade()) {
    			System.err.println("Erro: Estoque insuficinete para o produto'" + produto.getNome() + "'. Disponível:" + produto.getQuantidadeEstoque());
    			return null;
    		}
    		
    		// Prepara o ItemVenda com o preço real de venda do produto no momento
    		item.setPrecoUnitarioVenda(produto.getPrecoVenda());
    		itensComDetalhes.add(item);
    		valorTotal = valorTotal.add(produto.getPrecoVenda().multiply(new BigDecimal(item.getQuantidade())));
    		
    	}
    	// 2. Calcular o troco
    	BigDecimal troco = valorPago.subtract(valorTotal);
    	if(troco.compareTo(BigDecimal.ZERO)<0) {
    		System.err.println("Erro: Valor pago insufiente. Valor total:" + valorTotal);
    		return null;
    	}
    	
    	// 3. Criar o objeto Venda
    	Venda novaVenda = new Venda(valorTotal, valorPago, troco, itensComDetalhes);
    	
    	// 4. Chamar o DAO para registrar a venda no banco de dados
    	vendaDAO.registrarVenda(novaVenda);
    	
    	// Note: O ID e a data serão gerados no DAO, então precisaremos de uma forma
        // de retornar a venda completa do DAO. Por enquanto, a lógica acima
        // já é suficiente para o teste.
        // Para um sistema real, o método registrarVenda no DAO retornaria o objeto Venda completo.
    	
    	return novaVenda;
    	
    	
    }
	//Método  main para testar a camada de serviço
    public static void main(String[] args) {
    	VendaService service = new VendaService();
    	ProdutoDAO produtoDAO = new ProdutoDAO();
    	
    	System.out.println("--- Teste do VendaService ---");
    	
    	// Simula uma lista de produtos que o cliente quer comprar
        List<ItemVenda> itensParaComprar = new ArrayList<>();
        // Venda de 1 unidade do produto com ID 1
        itensParaComprar.add(new ItemVenda(0, 1, 1, BigDecimal.ZERO)); // ID 1 é a Maçã
        // Venda de 1 unidade do produto com ID 3
        itensParaComprar.add(new ItemVenda(0, 3, 1, BigDecimal.ZERO)); // ID 3 é a Laranja
        
     // Simula o valor pago pelo cliente
        BigDecimal valorPago = new BigDecimal("10.00");
        
     // Tenta realizar a venda
        Venda vendaRealizada = service.realizarVenda(itensParaComprar, valorPago);
        
        
        if(vendaRealizada != null) {
        	System.out.println("\nVenda realizada com sucesso! Detalhes da Venda:");
            System.out.println("Valor Total: " + vendaRealizada.getValorTotal());
            System.out.println("Valor Pago: " + vendaRealizada.getValorPago());
            System.out.println("Troco: " + vendaRealizada.getTroco());
            System.out.println("Estoque de produtos após a venda:");
            produtoDAO.listarProduto().forEach(System.out::println);
        }else {
        	System.out.println("\nFalha ao realizar a venda. Verifique as mensagens de erro acima.");
        }
    	
    }
	
	
}
