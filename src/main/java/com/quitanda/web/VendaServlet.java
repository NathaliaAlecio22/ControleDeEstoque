package com.quitanda.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quitanda.dao.VendaDAO; 
import com.quitanda.model.ItemVenda;
import com.quitanda.model.Venda;
import com.quitanda.service.VendaService;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet("/vendas/*")
public class VendaServlet extends HttpServlet {

    private VendaService vendaService;
    private VendaDAO vendaDAO; 

    @Override
    public void init() throws ServletException {
        super.init();
        this.vendaService = new VendaService();
        this.vendaDAO = new VendaDAO(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        
        List<Venda> vendas = vendaDAO.listarVendas();

        String vendasJson = new Gson().toJson(vendas);

        PrintWriter out = response.getWriter();
        out.print(vendasJson);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        
        String requestBody = request.getReader().lines().collect(java.util.stream.Collectors.joining());

        Type listType = new TypeToken<List<ItemVenda>>(){}.getType();
        List<ItemVenda> itensParaVenda = new Gson().fromJson(requestBody, listType);

        
        BigDecimal valorPago = new BigDecimal(request.getParameter("valorPago"));

        Venda vendaRegistrada = vendaService.realizarVenda(itensParaVenda, valorPago);

        if (vendaRegistrada != null) {
            
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            PrintWriter out = response.getWriter();
            String jsonResponse = "{ \"message\": \"Venda registrada com sucesso!\", \"vendaId\": " + vendaRegistrada.getId() + " }";
            out.print(jsonResponse);
            out.flush();
        } else {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            PrintWriter out = response.getWriter();
            out.print("{ \"message\": \"Falha ao registrar a venda. Verifique os dados.\" }");
            out.flush();
        }
    }
}