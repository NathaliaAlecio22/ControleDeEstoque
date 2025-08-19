package com.quitanda.dao;

import com.quitanda.model.Produto;
import com.quitanda.util.ConnectionFactory;

import java.sql.Connection;        // Para a conexão com o banco
import java.sql.PreparedStatement; // Para executar queries SQL preparadas
import java.sql.ResultSet;         // Para armazenar os resultados das consultas
import java.sql.SQLException;      // Para tratar exceções de SQL
import java.util.ArrayList;        // Para listar produtos
import java.util.List;             // Para a interface List
import java.math.BigDecimal;       // Para valores monetários


public class ProdutoDAO {
	// Método para adicionar um novo produto no banco de dados
	public void adicionarProduto(Produto produto) {
		String sql = "INSERT INTO produtos (nome, preco_compra, preco_venda, quantidade_estoque) VALUES (?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(sql);
			
			// Define os valores para os placeholders (?) na query
			stmt.setString(1, produto.getNome());
			stmt.setBigDecimal(2, produto.getPrecoCompra());
			stmt.setBigDecimal(3, produto.getPrecoVenda());
			stmt.setInt(4, produto.getQuantidadeEstoque());
			
			stmt.executeUpdate();
			System.out.println("Produto '" + produto.getNome() + "' adicionado com sucesso!");
			
		}catch(SQLException e) {
			System.err.println("Erro ao adicionar produto: " + e.getMessage());
			e.printStackTrace();
		}finally {
			ConnectionFactory.closeConnection(conn);
			if(stmt != null) {
				try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			
		}
	}
	
	public List<Produto> listarProduto(){
		String sql = "SELECT id, nome, preco_compra, preco_venda, quantidade_estoque FROM produtos";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // ResultSet armazena os resultados da consulta
        List<Produto> produtos = new ArrayList<>();
        
        try {
        	conn = ConnectionFactory.getConnection();
        	stmt = conn.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	
        	while(rs.next()) {
        		Produto produto = new Produto();
        		// Obtém os dados de cada coluna e seta no objeto Produto
        		produto.setId(rs.getInt("id"));
        		produto.setNome(rs.getString("nome"));
        		produto.setPrecoCompra(rs.getBigDecimal("preco_compra"));
                produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                
                produtos.add(produto);
        	}
        }catch(SQLException e) {
        	System.err.println("Erro ao listar produtos: " + e.getMessage());
        	e.printStackTrace();
        }finally {
        	ConnectionFactory.closeConnection(conn);
        	if(rs != null) {
        		try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        	}
        	if(stmt != null) {
        		try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        	}
        }
        return produtos;
	}
	//Método para obter um produto pelo ID
	public Produto buscarProdutoPorID(int id) {
		String sql = "SELECT id, nome, preco_compra, preco_venda, quantidade_estoque FROM produtos WHERE id = ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Produto produto = null;
		
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				produto = new Produto();
				produto.setId(rs.getInt("id"));
				produto.setNome(rs.getString("nome"));
				produto.setPrecoCompra(rs.getBigDecimal("preco_compra"));
				produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
				produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
				
			}
		}catch(SQLException e) {
			System.err.println("Erro ao buscar produto por ID:" + e.getMessage());
			e.printStackTrace();
		}finally {
			ConnectionFactory.closeConnection(conn);
			if(rs != null) {
				try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if(stmt != null) {
				try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return produto;
	}
	
	//Metodo para atualizar um produto existente
	public void atualizarProduto(Produto produto) {
		String sql = "UPDATE produtos SET nome = ?, preco_compra = ?, preco_venda = ?, quantidade_estoque = ? WHERE id = ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(sql);
			
			//Define os novos valores
			stmt.setString(1, produto.getNome());
			stmt.setBigDecimal(2, produto.getPrecoCompra());
			stmt.setBigDecimal(3,  produto.getPrecoVenda());
			stmt.setInt(4, produto.getQuantidadeEstoque());
			stmt.setInt(5, produto.getId());
			
			stmt.executeUpdate();
			System.out.println("Produto '" + produto.getNome() + "' atualizado com sucesso!");
			
		}catch (SQLException e) {
			System.err.println("Erro ao atualizar produto: " + e.getMessage());
			e.printStackTrace();
		}finally {
			ConnectionFactory.closeConnection(conn);
			if(stmt != null) {
				try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
	}
	//Método para remover um produto pelo ID
	public void removerProduto(int id) {
		String sql = "DELETE FROM produtos WHERE id = ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Produto com ID" + id + " removido com sucesso!");
				
			}else {
				System.out.println("Nenhu, produto encontrado com ID " + id + "para remover.");
				
			}
		}catch (SQLException e ) {
			System.err.println("Erro ao remover produto: " + e.getMessage());
			e.printStackTrace();
		}finally {
			ConnectionFactory.closeConnection(conn);
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		
	}
	//Método main para testar as operações CRUD do ProdutoDAO
	public static void main(String[] args) {
		ProdutoDAO dao = new ProdutoDAO();
		
		// -- TESTE DE ADICIONAR PRODUTO ---
		System.out.println("\n---  Adiciona Produtos ---");
		Produto maca = new Produto("Maçã Gala", new BigDecimal("1.50"), new BigDecimal("2.50"), 100);
		dao.adicionarProduto(maca);
		
		Produto banana = new Produto("Banana Nanica", new BigDecimal("0.80"), new BigDecimal("1.80"),150);
		dao.adicionarProduto(banana);
		
		Produto laranja = new Produto("Laranja Lima", new BigDecimal("1.00"), new BigDecimal("2.00"), 80);
		dao.adicionarProduto(laranja);
		
		// --- TESTE DE LISTAR PRODUTOS ---
		System.out.println("\n--- Listando Todos os Produtos---");
		List<Produto> produtos = dao.listarProduto();
		for (Produto p : produtos) {
			System.out.println(p);
		}
		if (produtos.isEmpty()) {
			System.out.println("Nenhum produto encontrado.");
		}
		
		// --- TESTE DE BUSCAR PRODUTO POR ID ---
		System.out.println("\n--- Buscando Produto por ID ---");
        Produto produtoBuscado = dao.buscarProdutoPorID(1); // Tente buscar o primeiro produto adicionado
        if (produtoBuscado != null) {
            System.out.println("Produto encontrado por ID 1: " + produtoBuscado);
        } else {
            System.out.println("Produto com ID 1 não encontrado.");
        }
        
     // --- TESTE DE ATUALIZAR PRODUTO ---
        System.out.println("\n--- Atualizando Produto ---");
        if (produtoBuscado != null) {
            produtoBuscado.setPrecoVenda(new BigDecimal("2.75")); // Aumenta o preço da maçã
            produtoBuscado.setQuantidadeEstoque(produtoBuscado.getQuantidadeEstoque() + 50); // Adiciona mais estoque
            dao.atualizarProduto(produtoBuscado);
        }
        
     // Verifique a atualização listando novamente
        System.out.println("\n--- Listando Produtos Após Atualização ---");
        produtos = dao.listarProduto();
        for (Produto p : produtos) {
            System.out.println(p);
        }
        
     // --- TESTE DE REMOVER PRODUTO ---
        System.out.println("\n--- Removendo Produto ---");
        // Nota: Os IDs podem variar dependendo de como você inseriu.
        // Se você quiser remover um produto específico, verifique o ID no console de Listar Produtos.
        // Vou tentar remover um produto com um ID hipotético 2, que pode ser a banana
        dao.removerProduto(2); // Remover o produto com ID 2 (pode ser a banana, verifique no seu console)
        
        
        System.out.println("\n--- Listando Produtos Após Remoção ---");
        produtos = dao.listarProduto();
        for (Produto p : produtos) {
            System.out.println(p);
        }

		
		
		
	}
}
