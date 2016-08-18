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
<c:if test="${(empty cmbMarcas)}"><c:redirect url="ProdutoServlet?action=listProdutos" /></c:if>
<c:if test="${msgErro=='produtoJaExiste'}"><script>alert('Produto já existe!');</script></c:if>
<c:if test="${msgErro=='produtoEMarcaJaExiste'}"><script>alert('Produto + Marca já existe!');</script></c:if>
<c:if test="${msgErro=='produtoPossuiEstoque'}"><script>alert('Produto possui estoque!');</script></c:if>
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
                    <li class="selected"><a href="produto.jsp">Produto</a></li>
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
                <h1>Produtos</h1>
                <br /><br />
                    <table class="ge_re">
                        <tr>
                            <form action="ProdutoServlet?action=saveProduto" method="POST" accept-charset="utf-8">
                                <td class="esquerda">Produto:</td>
                                <td><input type="text" name="produtoParamNome" size="25" value="<c:if test="${sessionScope.modoProduto == 'edicao'}">${Produto.nome}</c:if>"></input></td>
                                <td class="esquerda">Valor:</td>
                                <td><input type="text" name="produtoParamValor" size="10" value="<c:if test="${sessionScope.modoProduto == 'edicao'}">${Produto.valor}</c:if>"></input></td>
                                <td class="esquerda">Marca</td>    
                                <td><select name="cmbMarca" size="1">
                                    <c:forEach var="item" items="${cmbMarcas}">
                                        <option value="${item.idMarca}"<c:if test="${item.nome == Produto.marca.nome}"><c:out value="selected='selected'" /></c:if>>${item.nome}</option>
                                    </c:forEach>
                                    </select></td>

                                <c:choose>
                                        <c:when test="${sessionScope.modoProduto == 'inclusao'}">
                                            <td>
                                                <button type="submit" name="addSave" value="salvar"><img class="icone" src="images/save.png" alt="" /></button>
                                            </td>
                                        </c:when>
                                        <c:when test="${sessionScope.modoProduto == 'edicao'}">
                                            <td>
                                                <button type="submit" name="updSave" value="${Produto.idProduto}"><img class="icone" src="images/editar.png" alt="" /></button>
                                            </td>
                                        </c:when>
                                    </c:choose>
                            </form>
                            <td>
                                <form action="ProdutoServlet?action=listProdutos" method="POST" accept-charset="utf-8">
                                    <button type="submit"><img class="icone" src="images/search.png" alt="" /></button>
                                </form>
                            </td>
                        </tr>
                    </table>


                <c:choose>
                    <c:when test="${sessionScope.modoProduto == 'inclusao'}">
                        <form action="ProdutoServlet?action=updProduto" method="POST" accept-charset="utf-8">
                            <table cellspacing="0">
                                <tr>
                                    <th class="thgrid">Produto</th>
                                    <th class="thgrid">Marca</th>
                                    <th class="thgrid">Valor</th>
                                    <th class="thgrid">Estoque</th>
                                    <th class="thgrid">Excluir</th>
                                    <th class="thgrid">Editar</th>
                                </tr>
                                <c:forEach var="item" items="${Produtos}" varStatus="id">
                                <tr class="${id.count%2==0 ? 'tbgrid': 'grid2'}" >
                                    <td>${item.nome}</td>
                                    <td>${item.marca.nome}</td>
                                    <td class="centro"><fmt:formatNumber value="${item.valor}" type="currency" /></td>
                                    <td class="centro">${item.estoque.qtd}</td>
                                    <td class="centro"><button type="submit" name="del" value="${item.idProduto}"><img class="iconetb" src="images/cancel.png" alt="" /></button></td>
                                    <td class="centro"><button type="submit" name="edit" value="${item.idProduto}"><img class="iconetb" src="images/editar.png" alt="" /></button></td>
                                </tr>
                                </c:forEach>
                            </table>
                        </form>
                    </c:when>
                    <c:when test="${sessionScope.modoProduto == 'edicao'}">
                        <table cellspacing="0">
                            <tr>
                                <th class="thgrid">Produto</th>
                                <th class="thgrid">Marca</th>
                                <th class="thgrid">Valor</th>
                                <th class="thgrid">Estoque</th>
                            </tr>
                            <tr class="tbgrid" >
                                <td>${Produto.nome}</td>
                                <td>${Produto.marca.nome}</td>
                                <td class="centro"><fmt:formatNumber value="${Produto.valor}" type="currency" /></td>
                                <td class="centro">${Produto.estoque.qtd}</td>
                            </tr>
                        </table>
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