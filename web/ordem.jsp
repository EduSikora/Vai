<%@ page contentType="text/HTML" language="java" pageEncoding="UTF-8" import="java.sql.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>OS</title>
    <link rel="stylesheet" href="styles.css" type="text/css" />
</head>
<body>

<div class="topo">
    <div id="containertopo">
        Incluir login!
    </div>
</div>
<div class="wrapper">
    <div id="container">
        <div id="titulo">
            <div id="header">
                <h1><a href="/">OS</a></h1>
                <h2>Empresa abcd</h2>
            </div>
            <div id="nav"> 
                <ul>
                    <li><a href="produto.jsp">Produto</a></li>
                    <li><a href="marca.jsp">Marca</a></li>
                    <li><a href="fornecedor.jsp">Fornecedor</a></li>
                    <li><a href="estoque.jsp">Estoque</a></li>
                    <li><a href="ordem.jsp">Ordem</a></li>
                    <li><a href="funcionario.jsp">Funcionario</a></li>
                    <li><a href="setor.jsp">Setor</a></li>
                    <li><a href="categoria.jsp">Categoria</a></li>
                    <li><a href="servico.jsp">Serviço</a></li>
                </ul>
            </div>
        </div>
    </div>

    <div id="containercorpo">
        <div id="body">
            <div id="content">
                <h1>Ordem</h1>
                <br /><br />
                <form action="OrdemServlet?action=getOrdem" method="POST" accept-charset="utf-8">
                    <table class="ge_re">    
                        <tr>
                            <th class="thgrid">Ordem</th>
                            <th class="thgrid">Funcionario</th>
                            <th class="thgrid">Editar</th>
                        </tr>
                        <c:forEach var="item" items="${Ordens}" varStatus="id">
                        <tr class="${id.count%2==0 ? 'tbgrid': 'grid2'}" >
                            <td class="centro">${item.idOrdem}</td>
                            <td class="centro">${item.funcionario.nome}</td>
                            <td class="centro"><button type="submit" name="ororororo" value="${item.idOrdem}"><img class="iconetb" src="images/edit.png" alt="" /></button></td>
                        </tr>
                        </c:forEach>
                    </table>
                    <br /><br />
                    
                    <table class="ge_re">
                        <tr>
                            <td class="esquerda">Ordem:</td>
                            <td><input type="text" name="ordemParamId" size="10" value="0"></input></td>
                            <td><button type="submit" name="getOrdem"><img class="icone" src="images/search.png" alt="" /></button></td>
                        </tr>
                    </table>
                    <br /><br />
                    
                    <table class="ge_re">    
                        <tr>
                            <th class="thgrid">Produto</th>
                            <th class="thgrid">Marca</th>
                            <th class="thgrid">Qtd</th>
                            <th class="thgrid">Excluir</th>
                        </tr>
                        <c:forEach var="item" items="${OrdemProduto}" varStatus="id">
                        <tr class="${id.count%2==0 ? 'tbgrid': 'grid2'}" >
                            <td class="centro">${item.produto.nome}</td>
                            <td class="centro">${item.produto.marca.nome}</td>
                            <td class="centro">${item.qtd}</td>
                            <td class="centro"><button type="submit" name="delProduto" value="${item.produto.idProduto}"><img class="iconetb" src="images/cancel.png" alt="" /></button></td>
                        </tr>
                        </c:forEach>
                    </table>
                    <br /><br />
                    <form action="OrdemServlet?action=addProduto" method="POST" accept-charset="utf-8">
                    <table class="ge_re">
                        <tr>
                            <th class="thgrid">Produto</th>
                            <th class="thgrid">Marca</th>
                            <th class="thgrid">Valor</th>
                            <th class="thgrid">Estoque</th>
                            <th class="thgrid">Adicionar</th>
                        </tr>
                        <c:forEach var="item" items="${Produtos}" varStatus="id">
                        <tr class="${id.count%2==0 ? 'tbgrid': 'grid2'}" >
                            <td>${item.nome}</td>
                            <td>${item.marca.nome}</td>
                            <td class="centro"><fmt:formatNumber value="${item.valor}" type="currency" /></td>
                            <td class="centro">${item.estoque.qtd}</td>
                            <td class="centro"><button type="submit" name="addProduto" value="${item.idProduto}"><img class="iconetb" src="images/add.png" alt="" /></button></td>
                        </tr>
                        </c:forEach>
                    </table>
                </form>
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <div id="footer">
        <div id="footer-links">
            <p>&copy; OS - Definir descricao. Por 
            <a href="mailto:cleversonianke@gmail.com">Cleverson</a>, 
            <a href="mailto:danielguedesleite@gmail.com">Daniel</a>,
            <a href="mailto:edusikora@yahoo.com.br">Eduardo</a> e 
            <a href="mailto:rodrigolazoski@hotmail.com">Rodrigo</a>.</p>
        </div>  
    </div>
</div>
</body>
</html>