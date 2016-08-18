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
<c:if test="${msgErro=='fornecedorJaExiste'}"><script>alert('Fornecedor já existe!');</script></c:if>
<c:if test="${msgErro=='fornecedorEstaEmEstoque'}"><script>alert('Fornecedor esta no estoque!');</script></c:if>

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
                    <li class="selected"><a href="fornecedor.jsp">Fornecedor</a></li>
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
                <h1>Fornecedor</h1>
                <br /><br />
                    <table class="ge_re">
                        <tr>
                            <form action="FornecedorServlet?action=saveFornecedor" method="POST" accept-charset="utf-8">
                                <td class="esquerda">Fornecedor</td>
                                <td><input type="text" name="fornecedorParamNome" size="25" value="<c:if test="${sessionScope.modoFornecedor == 'edicao'}">${Fornecedor.nome}</c:if>"></input></td>
                                
                                <c:choose>
                                        <c:when test="${sessionScope.modoFornecedor == 'inclusao'}">
                                            <td>
                                                <button type="submit" name="addSave" value="salvar"><img class="icone" src="images/save.png" alt="" /></button>
                                            </td>
                                        </c:when>
                                        <c:when test="${sessionScope.modoFornecedor == 'edicao'}">
                                            <td>
                                                <button type="submit" name="updSave" value="${Fornecedor.idFornecedor}"><img class="icone" src="images/editar.png" alt="" /></button>
                                            </td>
                                        </c:when>
                                    </c:choose>
                            </form>
                            <td>
                                <form action="FornecedorServlet?action=listFornecedores" method="POST" accept-charset="utf-8">
                                    <button type="submit"><img class="icone" src="images/search.png" alt="" /></button>
                                </form>
                            </td>
                        </tr>
                    </table>


                <c:choose>
                    <c:when test="${sessionScope.modoFornecedor == 'inclusao'}">
                        <form action="FornecedorServlet?action=updFornecedor" method="POST" accept-charset="utf-8">
                            <table cellspacing="0">
                                <tr>
                                    <th class="thgrid">Fornecedor</th>
                                    <th class="thgrid">Excluir</th>
                                    <th class="thgrid">Editar</th>
                                </tr>
                                <c:forEach var="item" items="${Fornecedores}" varStatus="id">
                                <tr class="${id.count%2==0 ? 'tbgrid': 'grid2'}" >
                                    <td>${item.nome}</td>
                                    <td class="centro"><button type="submit" name="del" value="${item.idFornecedor}"><img class="iconetb" src="images/cancel.png" alt="" /></button></td>
                                    <td class="centro"><button type="submit" name="edit" value="${item.idFornecedor}"><img class="iconetb" src="images/editar.png" alt="" /></button></td>
                                </tr>
                                </c:forEach>
                            </table>
                        </form>
                    </c:when>
                    <c:when test="${sessionScope.modoFornecedor == 'edicao'}">
                        <table cellspacing="0">
                            <tr>
                                <th class="thgrid">Fornecedor</th>
                            </tr>
                            <tr class="tbgrid" >
                                <td>${Fornecedor.nome}</td>
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