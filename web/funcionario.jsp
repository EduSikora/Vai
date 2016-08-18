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
                    <li><a href="produto.jsp">Produto</a></li>
                    <li><a href="marca.jsp">Marca</a></li>
                    <li><a href="fornecedor.jsp">Fornecedor</a></li>
                    <li><a href="estoque.jsp">Estoque</a></li>
                    <li><a href="ordem.jsp">Ordem</a></li>
                    <li class="selected"><a href="funcionario.jsp">Funcionario</a></li>
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
                <h1>Cadastro de Funcionarios</h1>
                <br /><br />
               	<table class="ge_re">
                    <tr>
                        <form action="FuncionarioServlet?action=buscafuncionario" method="POST" accept-charset="utf-8">
                            <td class="esquerda">Pesquisar:</td>
                            <td><input type="text" name="buscafunc" size="40" /></td>
                            <td><button type="submit"><img class="icone" src="images/search.png" alt=""></button></td>
                        </form>
                    </tr>
                </table>
                <c:if test="${listafunc != null}">
                    <br /><br />
                    <table cellspacing="0">
                        <tr>
                            <th class="thgrid">Nome</th>
                            <th class="thgrid">CPF</th>
                            <th class="thgrid">E-mail</th>
                            <th class="thgrid">Ativo</th>
                            <th class="thgrid">Editar</th>
                        </tr>
                        <c:forEach var="itemfunc" items="${listafunc}" varStatus="id">
                            <form action="FuncionarioServlet?action=editafuncionario" method="POST" accept-charset="utf-8">
                                <tr class="${id.count%2==0 ? 'tbgrid' : 'grid2'}">
                                    <input type="hidden" name="idFuncionario" value="${itemfunc.idFuncionario}" />
                                    <td>${itemfunc.nome}</td>
                                    <td class="centro">${itemfunc.cpf}</td>
                                    <td class="centro">${itemfunc.email}</td>
                                    <td class="centro">${itemfunc.ativo==true ? 'Ativo' : 'Inativo'}</td>
                                    <td class="centro"><button type="submit"><img class="iconetb" src="images/editar.png" alt=""></button></td>
                                </tr>
                            </form>
                        </c:forEach>
                    </table>
                </c:if>
                <br/><br/>
                <form action="FuncionarioServlet?action=salvafuncionario" method="POST" accept-charset="utf-8">
                    <input type="hidden" name="idFuncionario" value="${func.idFuncionario}" />
                    <input type="hidden" name="codEndereco" value="${func.endereco.codEndereco}" />
                    <table cellspacing="0">
                        <tr>
                            <td class="esquerda">Status do funcionário:</td>
                            <td><select name="ativo" size="1">
                                <option value="1" <c:if test="${func.ativo==true}"><c:out value="selected=selected" /></c:if>>Ativo</option>
                                <option value="0" <c:if test="${func.ativo==false}"><c:out value="selected=selected" /></c:if>>Inativo</option>
                            </select></td>
                        </tr>
                        <tr>
                            <td class="esquerda">Nome:</td>
                            <td><input name="nome" type="text" size="40" value="${func.nome}" /></td>
                        </tr>
                        <tr>
                            <td class="esquerda">CPF:</td>
                            <td><input name="cpf" type="text" size="15" id="cpf" value="${func.cpf}" /> <i>(Somente números)</i></td>
                        </tr>
                        <tr>
                            <td class="esquerda">Telefone:</td>
                            <td><input name="telefone" type="text" size="15" id="telefone" value="${func.telefone}" /> <i>(Somente números)</i></td>
                        </tr>
                        <tr>
                            <td class="esquerda">E-mail (login):</td>
                            <td><input name="email" type="text" size="25" value="${func.email}" 
                                       ${func.idFuncionario>0 ? 'disabled' : ''} /></td>
                        </tr>
                        <tr>
                            <td class="esquerda">Senha:</td>
                            <td><input name="senha" type="text" size="10" /></td>
                        </tr>
                        <tr>
                            <td class="esquerda">CEP:</td>
                            <td><input name="cep" type="text" size="10" id="cep" value="${func.endereco.cep}" onkeypress="mascaracep(this)" /> <i>(Somente números)</i></td>
                        </tr>
                        <tr>
                            <td class="esquerda">UF:</td>
                            <td><select name="estado" size="1" id="estado">
                                <option value="AC" <c:if test="${func.endereco.estado=='AC'}"><c:out value="selected='selected'" /></c:if>>AC</option>
                                <option value="AL" <c:if test="${func.endereco.estado=='AL'}"><c:out value="selected='selected'" /></c:if>>AL</option>
                                <option value="AP" <c:if test="${func.endereco.estado=='AP'}"><c:out value="selected='selected'" /></c:if>>AP</option>
                                <option value="AM" <c:if test="${func.endereco.estado=='AM'}"><c:out value="selected='selected'" /></c:if>>AM</option>
                                <option value="BA" <c:if test="${func.endereco.estado=='BA'}"><c:out value="selected='selected'" /></c:if>>BA</option>
                                <option value="CE" <c:if test="${func.endereco.estado=='CE'}"><c:out value="selected='selected'" /></c:if>>CE</option>
                                <option value="DF" <c:if test="${func.endereco.estado=='DF'}"><c:out value="selected='selected'" /></c:if>>DF</option>
                                <option value="ES" <c:if test="${func.endereco.estado=='ES'}"><c:out value="selected='selected'" /></c:if>>ES</option>
                                <option value="GO" <c:if test="${func.endereco.estado=='GO'}"><c:out value="selected='selected'" /></c:if>>GO</option>
                                <option value="MA" <c:if test="${func.endereco.estado=='MA'}"><c:out value="selected='selected'" /></c:if>>MA</option>
                                <option value="MT" <c:if test="${func.endereco.estado=='MT'}"><c:out value="selected='selected'" /></c:if>>MT</option>
                                <option value="MS" <c:if test="${func.endereco.estado=='MS'}"><c:out value="selected='selected'" /></c:if>>MS</option>
                                <option value="MG" <c:if test="${func.endereco.estado=='MG'}"><c:out value="selected='selected'" /></c:if>>MG</option>
                                <option value="PA" <c:if test="${func.endereco.estado=='PA'}"><c:out value="selected='selected'" /></c:if>>PA</option>
                                <option value="PB" <c:if test="${func.endereco.estado=='PB'}"><c:out value="selected='selected'" /></c:if>>PB</option>
                                <option value="PR" <c:if test="${func.endereco.estado=='PR'}"><c:out value="selected='selected'" /></c:if>>PR</option>
                                <option value="PE" <c:if test="${func.endereco.estado=='PE'}"><c:out value="selected='selected'" /></c:if>>PE</option>
                                <option value="PI" <c:if test="${func.endereco.estado=='PI'}"><c:out value="selected='selected'" /></c:if>>PI</option>
                                <option value="RJ" <c:if test="${func.endereco.estado=='RJ'}"><c:out value="selected='selected'" /></c:if>>RJ</option>
                                <option value="RN" <c:if test="${func.endereco.estado=='RN'}"><c:out value="selected='selected'" /></c:if>>RN</option>
                                <option value="RS" <c:if test="${func.endereco.estado=='RS'}"><c:out value="selected='selected'" /></c:if>>RS</option>
                                <option value="RO" <c:if test="${func.endereco.estado=='RO'}"><c:out value="selected='selected'" /></c:if>>RO</option>
                                <option value="RR" <c:if test="${func.endereco.estado=='RR'}"><c:out value="selected='selected'" /></c:if>>RR</option>
                                <option value="SC" <c:if test="${func.endereco.estado=='SC'}"><c:out value="selected='selected'" /></c:if>>SC</option>
                                <option value="SP" <c:if test="${func.endereco.estado=='SP'}"><c:out value="selected='selected'" /></c:if>>SP</option>
                                <option value="SE" <c:if test="${func.endereco.estado=='SE'}"><c:out value="selected='selected'" /></c:if>>SE</option>
                                <option value="TO" <c:if test="${func.endereco.estado=='TO'}"><c:out value="selected='selected'" /></c:if>>TO</option>
                            </select></td>
                        </tr>
                        <tr>
                            <td class="esquerda">Cidade:</td>
                            <td><input name="cidade" type="text" id="cidade" value="${func.endereco.cidade}" /></td>
                        </tr>
                        <tr>	
                            <td class="esquerda">Bairro:</td>
                            <td><input name="bairro" type="text" id="bairro" value="${func.endereco.bairro}" /></td>
                        </tr>
                        <tr>
                            <td class="esquerda">Rua:</td>
                            <td ><input name="rua" type="text" size="35" id="rua" value="${func.endereco.rua}" /></td>
                        </tr>
                        <tr>					
                            <td class="esquerda">Numero:</td>
                            <td><input name="numero" type="text" size="5" value="${func.endereco.numero}" /></td>
                        </tr>
                        <tr>					
                            <td class="esquerda">Complemento:</td>
                            <td><input name="complemento" type="text" size="10" value="${func.endereco.complemento}" /></td>
                        </tr>
                        <tr>
                            <td class="esquerda">Setor:</td>
                            <td><select name="cmbSetor" size="1" value="${func.setor.idSetor}">
                                 <c:forEach var="item" items="${cmbSetores}">
                                        <option value="${item.idSetor}">${item.nome}</option>
                                    </c:forEach>
                                    </select></td>
                        </tr>
                        <tr>
                            <td class="esquerda"><input type="reset" name="limpa" value="Limpar" /></td>
                            <td><input type="submit" name="salva" value="Salvar" /></td>
                        </tr>
                    </table>
                </form>
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
<script>
    $("#cep").blur(function(){
    var cep = this.value.replace(/[^0-9]/, "");
    if(cep.length!=8){
        return false;}
    var url = "http://viacep.com.br/ws/"+cep+"/json/";
    $.getJSON(url, function(dadosRetorno){
    try{
        $("#rua").val(dadosRetorno.logradouro);
        $("#bairro").val(dadosRetorno.bairro);
        $("#cidade").val(dadosRetorno.localidade);
        $("#estado").val(dadosRetorno.uf);
    } catch(ex){}
    });
});
</script>
<c:remove var="func" />
</body>
</html>
