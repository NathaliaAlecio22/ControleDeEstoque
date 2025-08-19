package com.quitanda.util;

import java.io.IOException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionFactory {
	// --- Configurações de Conexão com o Banco de Dados ---
    // Estas são as informações que o JDBC usará para se conectar ao seu MySQL.

    // URL de conexão:
    // - jdbc:mysql:// indica o tipo de banco de dados e driver
    // - localhost:3306 é o endereço do seu servidor MySQL (onde ele está rodando e a porta padrão)
    // - /quitanda_db é o nome do banco de dados que criamos no MySQL Workbench
    // - ?useTimezone=true&serverTimezone=UTC são parâmetros importantes para evitar problemas com fuso horário
	
	private static Properties props;
	
	static {
		props = new Properties();
		try(InputStream  input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")){
			if(input == null) {
				System.err.println("Desculpe, o arquivo db.properties não foi encontrado.");
				System.exit(1);
				
			}
			props.load(input);
		}catch(IOException ex) {
			System.err.println("Erro ao carregar o arquivo de propriedades: " + ex.getMessage());
			System.exit(1);
		}
		
	}
	
	/**
     * Método estático para obter uma nova conexão com o banco de dados.
     * Métodos estáticos podem ser chamados diretamente pela classe (ex: ConnectionFactory.getConnection()).
     *
     * @return Um objeto Connection, que representa a conexão ativa com o banco.
     * @throws SQLException Se ocorrer qualquer erro durante o processo de conexão (ex: credenciais erradas, banco não encontrado).
     */
	
	public static Connection getConnection() throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver JDBC carregado com sucesso!");
				
		}catch(ClassNotFoundException e) {
			System.err.println("Erro ao carregar o driver JDBC: " + e.getMessage());
			throw new SQLException("Driver JDBC não encontrado. Verifique se o JAR está no Build Path.", e);
		}
		Connection conn = DriverManager.getConnection(
					props.getProperty("db.url"),
					props.getProperty("db.user"),
					props.getProperty("db.password")
				);
		
		
				return conn;
	}
	 /**
     * Método estático para fechar uma conexão com o banco de dados de forma segura.
     * É crucial fechar as conexões para liberar recursos do banco.
     *
     * @param conn O objeto Connection a ser fechado.
     */
	
	public static void closeConnection(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
				System.out.println("Conexão com o banco de dados fechada com sucesso!");
				
			}catch(SQLException e) {
				// Captura e imprime erros que podem ocorrer ao tentar fechar a conexão
				System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());	
			}
		}
	}
	/**
     * Método main para testar a conexão.
     * Este é um método de teste rápido. Você pode executá-lo diretamente no Eclipse.
     * Clique com o botão direito no arquivo 'ConnectionFactory.java' no Package Explorer,
     * selecione 'Run As' > 'Java Application'.
     * Se tudo estiver configurado corretamente, você verá as mensagens de sucesso no console.
     */
	public static void main(String[] args) {
        Connection conn = null; // Declara a conexão fora do bloco try para poder fechar no finally
        try {
            conn = getConnection(); // Tenta obter a conexão
            // Se chegou aqui, a conexão foi bem-sucedida.
            System.out.println("Teste de conexão concluído. Se esta mensagem apareceu, está OK!");
        } catch (SQLException e) {
            // Se houver uma SQLException, imprime a mensagem de erro
            System.err.println("Falha ao testar a conexão: " + e.getMessage());
            System.err.println("Verifique:");
            System.err.println("1. Se o MySQL Server está rodando.");
            System.err.println("2. Se o arquivo db.properties está na pasta src/main/resources.");
            System.err.println("3. Se as credenciais no arquivo db.properties estão corretas.");
        } finally {
            closeConnection(conn); // Garante que a conexão seja fechada, mesmo se ocorrer um erro
        }
    }
}
