# Controle de Estoque

## 📝 Descrição do Projeto

Este é um projeto desenvolvido com o objetivo de criar um sistema completo de controle de estoque e caixa para uma pequena quitanda. A aplicação é construída com uma arquitetura cliente-servidor, onde o backend em Java fornece uma API RESTful para o frontend, construído com HTML, CSS e JavaScript.

O projeto demonstra a integração de diferentes tecnologias e a aplicação de boas práticas de arquitetura de software para criar uma solução funcional e robusta.

## 🚀 Funcionalidades Principais

* **Controle de Produtos:**
    * Listagem de todos os produtos do estoque.
    * Adição de novos produtos.
    * Edição de informações de produtos existentes.
    * Exclusão de produtos.
* **Controle de Caixa:**
    * Registro de vendas com múltiplos itens.
    * Cálculo automático de valor total e troco.
    * Atualização automática do estoque após cada venda.
* **Segurança:**
    * Uso de um arquivo de configuração externo para armazenar as credenciais do banco de dados, garantindo que não sejam expostas publicamente.

## 💻 Tecnologias Utilizadas

### Backend
* **Java (Servlets):** Lógica de negócio e API RESTful.
* **MySQL:** Sistema de gerenciamento de banco de dados.
* **JDBC:** Conectividade Java com o banco de dados.
* **Apache Tomcat:** Servidor de aplicações para rodar o backend.
* **Gson:** Biblioteca para conversão de objetos Java para JSON e vice-versa.

### Frontend
* **HTML5:** Estrutura das páginas da web.
* **CSS3:** Estilização e layout.
* **JavaScript:** Lógica do lado do cliente e comunicação com a API (usando `fetch`).

## 🧱 Arquitetura do Projeto

O projeto segue um design de arquitetura em camadas para separar as responsabilidades e facilitar a manutenção:

* **Camada de Modelo (`com.quitanda.model`):** Contém as classes que representam os objetos de negócio (`Produto`, `Venda`, etc.).
* **Camada de Acesso a Dados (`com.quitanda.dao`):** Responsável por todas as operações de banco de dados (CRUD).
* **Camada de Serviço (`com.quitanda.service`):** Implementa a lógica de negócio e orquestra as operações entre os DAOs.
* **Camada de Web (`com.quitanda.web`):** Contém os Servlets que atuam como controladores, recebendo as requisições HTTP e interagindo com as camadas de serviço e DAO.

## 🛠️ Como Executar o Projeto

1.  **Pré-requisitos:** Certifique-se de ter o **JDK 11+**, o **Apache Tomcat 10.x** e o **MySQL Server** instalados.

2.  **Configuração do Banco de Dados:**
    * Crie um banco de dados chamado `quitanda_db` no MySQL.
    * Execute os scripts de criação de tabelas (`produtos`, `vendas`, `itens_venda`).
    * Abra o arquivo `src/main/resources/db.properties` e insira suas credenciais de acesso ao MySQL.

3.  **Configuração no Eclipse:**
    * Importe o projeto para o Eclipse.
    * Adicione os arquivos JAR do **MySQL Connector/J** e **Gson** ao `Build Path` do projeto.
    * Configure o **Apache Tomcat** como servidor no Eclipse.
    * Configure o `Deployment Assembly` do projeto para que a pasta `src/main/resources` seja copiada para `/WEB-INF/classes`.

4.  **Execução:**
    * Inicie o servidor Tomcat no Eclipse.
    * Acesse a aplicação no navegador pela URL `http://localhost:8080/QuitandaBackend/`.

## 👩‍💻 Autor

* **Nathalia Alecio** - https://www.linkedin.com/in/nathalia-alecio-098271252/
