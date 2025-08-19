package com.quitanda.web;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.quitanda.dao.ProdutoDAO;
import com.quitanda.model.Produto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/produtos/*")
public class ProdutoServlet extends HttpServlet {

    private ProdutoDAO produtoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.produtoDAO = new ProdutoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configura o tipo de conteúdo da resposta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Obtém a lista de produtos do banco de dados usando o DAO
        List<Produto> produtos = produtoDAO.listarProduto();

        // Converte a lista de objetos Java para uma string JSON
        String produtosJson = new Gson().toJson(produtos);

        // Obtém o "escritor" da resposta e escreve a string JSON
        PrintWriter out = response.getWriter();
        out.print(produtosJson);
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	
    	//O corpo da requisição POST contém o JSON do novo produto
    	String requestBody = request.getReader().lines().collect(java.util.stream.Collectors.joining());
    	
    	// Converte a string JSON para um objeto Produto
    	Produto novoProduto = new Gson().fromJson(requestBody, Produto.class);
    	
    	
    	produtoDAO.adicionarProduto(novoProduto);
    	
    	// Retorna o produto adicionado como resposta (opcional, mas boa prática)
        // Por enquanto, vamos apenas enviar uma mensagem de sucesso
    	
    	
    	PrintWriter out = response.getWriter();
    	out.print("{ \"message\": \"Produto adicionado com sucesso!\" }");
    	out.flush();
    	
    }
    
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	
    	// O corpo da requisição PUT contém o JSON do produto a ser atualizado
    	String requestBody = request.getReader().lines().collect(java.util.stream.Collectors.joining());
    	
    	
    	//Converte a string JSON para um objeto Produto
    	Produto produtoAtualizado = new Gson().fromJson(requestBody, Produto.class);
    	
    	// Atualiza o produto no banco de dados usando o DAO
        produtoDAO.atualizarProduto(produtoAtualizado);
        
        PrintWriter out = response.getWriter();
        out.print("{ \"message\": \"Produto com ID " + produtoAtualizado.getId() + " atualizado com sucesso!\" }");
        out.flush();
    }
    
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Obtenha o ID do produto a ser deletado a partir da URL.
        // Por exemplo, uma requisição para /produtos/10
        String pathInfo = request.getPathInfo(); // Retorna "/10"
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                // Extrai o ID da URL (ex: remove a barra "/" e converte para int)
                int id = Integer.parseInt(pathInfo.substring(1));

                // Remove o produto do banco de dados usando o DAO
                produtoDAO.removerProduto(id);

                PrintWriter out = response.getWriter();
                out.print("{ \"message\": \"Produto com ID " + id + " removido com sucesso!\" }");
                out.flush();
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                PrintWriter out = response.getWriter();
                out.print("{ \"message\": \"ID inválido na URL.\" }");
                out.flush();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            PrintWriter out = response.getWriter();
            out.print("{ \"message\": \"Faltando o ID do produto na URL.\" }");
            out.flush();
        }
    }
    
}