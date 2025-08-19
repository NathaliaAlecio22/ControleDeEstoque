package com.quitanda.dao;


import com.quitanda.model.ItemVenda;
import com.quitanda.model.Produto;
import com.quitanda.model.Venda;
import com.quitanda.util.ConnectionFactory;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class VendaDAO {
	 /**
     * Registra uma nova venda no banco de dados de forma transacional.
     * Garante que a venda, seus itens e a atualização do estoque sejam consistentes.
     *
     * @param venda O objeto Venda a ser registrado.
     */
	
	public void registrarVenda(Venda venda) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia a transação. Desliga o commit automático.

            // 1. Inserir a venda na tabela 'vendas' e obter o ID gerado
            int vendaId = inserirVenda(conn, venda);

            // 2. Inserir cada item da venda na tabela 'itens_venda' e atualizar o estoque
            for (ItemVenda item : venda.getItens()) {
                inserirItemVenda(conn, vendaId, item);
                atualizarEstoque(conn, item);
            }

            conn.commit(); // Se tudo deu certo, confirma todas as operações
            System.out.println("Venda registrada com sucesso! ID da Venda: " + vendaId);

        } catch (SQLException e) {
            // Se algo der errado, desfaz tudo
            System.err.println("Erro ao registrar a venda. Desfazendo operações...");
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erro ao reverter a transação: " + rollbackEx.getMessage());
            }
            e.printStackTrace();
        } finally {
            // Fecha a conexão
            ConnectionFactory.closeConnection(conn);
        }
    }
	
	private int inserirVenda(Connection conn, Venda venda) throws SQLException{
		String sql = "INSERT INTO vendas (valor_total, valor_pago, troco) VALUES (?, ?, ?)";
		PreparedStatement stmt = null;
        ResultSet rs = null;
        int vendaId = -1;
        
        try {
        	stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        	stmt.setBigDecimal(1, venda.getValorTotal());
        	stmt.setBigDecimal(2, venda.getValorPago());
        	stmt.setBigDecimal(3, venda.getTroco());
        	stmt.executeUpdate();
        	
        	rs = stmt.getGeneratedKeys();
        	if (rs.next()) {
        		vendaId = rs.getInt(1);
        	}
        }finally {
        	if(rs != null) rs.close();
        	if(stmt != null) stmt.close();
        }
        return vendaId;
	}
	
	// Método auxiliar para inserir cada item da venda
	private void inserirItemVenda(Connection conn, int vendaId, ItemVenda item) throws SQLException{
		String sql = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario_venda) VALUES (?, ?, ?, ?)";
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, vendaId);
			stmt.setInt(2, item.getProdutoId());
			stmt.setInt(3, item.getQuantidade());
			stmt.setBigDecimal(4, item.getPrecoUnitarioVenda());
			stmt.executeUpdate();
		}finally {
			if(stmt != null) stmt.close();
		}
		
	}
	
	
	private void atualizarEstoque(Connection conn, ItemVenda item) throws SQLException{
		String sql = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        PreparedStatement stmt = null;
        
        try {
        	stmt = conn.prepareStatement(sql);
            stmt.setInt(1, item.getQuantidade());
            stmt.setInt(2, item.getProdutoId());
            stmt.executeUpdate();
        }finally {
        	if(stmt != null) stmt.close();
        }
	}
	
	public List<Venda> listarVendas(){
		String sql = "SELECT id, data_venda, valor_total, valor_pago, troco FROM vendas ORDER BY data_venda DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Venda> vendas = new ArrayList<>(); 
        
        try {
        	conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while(rs.next()) {
            	Venda venda = new Venda();
                venda.setId(rs.getInt("id"));
                // Converte de java.sql.Timestamp para java.time.LocalDateTime
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setValorTotal(rs.getBigDecimal("valor_total"));
                venda.setValorPago(rs.getBigDecimal("valor_pago"));
                venda.setTroco(rs.getBigDecimal("troco"));
                vendas.add(venda);
            }
        }catch(SQLException e) {
        	System.err.println("Erro ao listar vendas: " + e.getMessage());
            e.printStackTrace();
        }finally {
        	ConnectionFactory.closeConnection(conn);
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return vendas;
	}
	
	 // Método main para testar o registro de vendas
    public static void main(String[] args) {
        VendaDAO dao = new VendaDAO();
        ProdutoDAO produtoDAO = new ProdutoDAO();

        System.out.println("--- Teste de Registro de Venda ---");

        // Passo 1: Obter produtos do estoque para simular a venda
        List<Produto> produtosEmEstoque = produtoDAO.listarProduto();
        if (produtosEmEstoque.size() < 2) {
            System.err.println("Por favor, adicione pelo menos 2 produtos no estoque antes de rodar este teste.");
            return;
        }

        // Simula a compra de 2 produtos
        Produto produto1 = produtosEmEstoque.get(0);
        Produto produto2 = produtosEmEstoque.get(1);

        List<ItemVenda> itens = new ArrayList<>();
        ItemVenda item1 = new ItemVenda(0, produto1.getId(), 2, produto1.getPrecoVenda()); // Vende 2 unidades do primeiro produto
        ItemVenda item2 = new ItemVenda(0, produto2.getId(), 1, produto2.getPrecoVenda()); // Vende 1 unidade do segundo produto
        itens.add(item1);
        itens.add(item2);

        // Calcula o valor total da venda
        BigDecimal valorTotal = item1.getPrecoUnitarioVenda().multiply(new BigDecimal(item1.getQuantidade()))
                                     .add(item2.getPrecoUnitarioVenda().multiply(new BigDecimal(item2.getQuantidade())));
        BigDecimal valorPago = new BigDecimal("50.00"); // Supondo que o cliente pagou R$ 50,00
        BigDecimal troco = valorPago.subtract(valorTotal);

        Venda novaVenda = new Venda(valorTotal, valorPago, troco, itens);

        // Passo 2: Registrar a venda
        dao.registrarVenda(novaVenda);

        // Passo 3: Listar as vendas para verificar se a nova foi adicionada
        System.out.println("\n--- Listando Vendas após o Registro ---");
        List<Venda> vendas = dao.listarVendas();
        for (Venda v : vendas) {
            System.out.println(v);
        }

        // Passo 4: Listar os produtos novamente para verificar se o estoque foi atualizado
        System.out.println("\n--- Estoque de Produtos após a Venda ---");
        List<Produto> produtosAtualizados = produtoDAO.listarProduto();
        for (Produto p : produtosAtualizados) {
            System.out.println(p);
        }
    }
}
