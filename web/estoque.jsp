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
                    <li class="selected"><a href="estoque.jsp">Estoque</a></li>
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
                <h1>Estoque</h1>
                <br /><br />
                    <table class="ge_re">
                        <tr>
                            <form action="EstoqueServlet?action=saveEstoqueHist" method="POST" accept-charset="utf-8">
                                <td class="esquerda">Marca:</td>
                                <td><select name="cmbMarca" size="1">
                                    <c:forEach var="item" items="${cmbMarcas}">
                                        <option value="${item.idMarca}">${item.nome}</option>
                                    </c:forEach>
                                    </select></td>
                                <td class="esquerda">Produto:</td>  
                                <td><select name="cmbProduto" size="1">
                                    <c:forEach var="item" items="${cmbProdutos}">
                                        <option value="${item.idProduto}">${item.nome}</option>
                                    </c:forEach>
                                    </select></td>
                                <td class="esquerda">Fornecedor:</td>
                                <td><select name="cmbFornecedor" size="1">
                                    <c:forEach var="item" items="${cmbFornecedores}">
                                        <option value="${item.idFornecedor}">${item.nome}</option>
                                    </c:forEach>
                                    </select></td>
                                <td class="esquerda">Ação:</td>
                                <td><select name="cmbAcao" size="1">
                                    <c:forEach var="item" items="${cmbAcoes}">
                                        <option value="${item.idAcao}">${item.acao}</option>
                                    </c:forEach>
                                    </select></td>
                                <td class="esquerda">Qtd: </td>
                                <td><input type="text" name="historicoParamQtd" size="10" value="0"></input></td>
                                <c:choose>
                                    <c:when test="${sessionScope.modoEstoqueHist == 'inclusao'}">
                                        <td>
                                            <button type="submit" name="addSave" value="salvar"><img class="icone" src="images/save.png" alt="" /></button>
                                        </td>
                                    </c:when>
                                </c:choose>
                            </form>
                            <td>
                                <form action="EstoqueServlet?action=listEstoqueHist" method="POST" accept-charset="utf-8">
                                    <button type="submit"><img class="icone" src="images/search.png" alt="" /></button>
                                </form>
                            </td>
                        </tr>
                    </table>

                <c:choose>
                    <c:when test="${sessionScope.modoEstoqueHist == 'inclusao'}">
                        <form action="EstoqueServlet?action=updEstoqueHist" method="POST" accept-charset="utf-8">
                            <table cellspacing="0">
                                <tr>
                                    <th class="thgrid">Marca</th>
                                    <th class="thgrid">Produto</th>
                                    <th class="thgrid">Fornecedor</th>
                                    <th class="thgrid">Acao</th>
                                    <th class="thgrid">Data</th>
                                    <th class="thgrid">Qtd</th>
                                    <th class="thgrid">Funcionario</th>
                                    <th class="thgrid">Reverter</th>
                                </tr>
                                <c:forEach var="item" items="${EstoqueHist}" varStatus="id">
                                <tr class="${id.count%2==0 ? 'tbgrid': 'grid2'}" >
                                    <td>${item.produto.marca.nome}</td>
                                    <td>${item.produto.nome}</td>
                                    <td>${item.fornecedor.nome}</td>
                                    <td>${item.acao.acao}</td>
                                    <td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm" value="${item.data}" /></td>
                                    <td>${item.qtd}</td>
                                    <td>${item.funcionario.nome}</td>
                                    <td class="centro"><button type="submit" name="del" value="${item.idEstoqueHist}"><img class="iconetb" src="images/cancel.png" alt="" /></button></td>
                                </tr>
                                </c:forEach>
                            </table>
                        </form>
                    </c:when>
                </c:choose>
                <br/><br/>
                <p></p>
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