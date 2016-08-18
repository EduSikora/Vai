package DAO;

import Bean.Empresa;
import Bean.EstoqueHistorico;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueHistoricoDAO {
    private final String stmtGetEstoqueHistById = "SELECT * FROM ESTOQUE_HIST WHERE ID_ESTOQUE_HIST = ?";
    private final String stmtGetEstoqueHistTodos = "SELECT * FROM ESTOQUE_HIST WHERE ID_EMPRESA = ?";
    private final String stmtAddEstoqueHist = "INSERT INTO ESTOQUE_HIST (ID_EMPRESA, ID_PRODUTO, ID_FUNCIONARIO, ID_FORNECEDOR, ID_ACAO, NB_QTD, DT_DATA) values (?,?,?,?,?,?,NOW())";
     
    public EstoqueHistorico getEstoqueHistById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetEstoqueHistById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();

            EstoqueHistorico historico = new EstoqueHistorico();
            ProdutoDAO daoProd = new ProdutoDAO();
            FuncionarioDAO daoFuncionario = new FuncionarioDAO();
            EstoqueAcaoDAO daoEstoqueAcao = new EstoqueAcaoDAO();
            FornecedorDAO daoFornecedor = new FornecedorDAO();
            EmpresaDAO daoEmp = new EmpresaDAO();
            historico.setIdEstoqueHist(rs.getInt("ID_ESTOQUE_HIST"));
            historico.setProduto(daoProd.getProdutoById(rs.getInt("ID_PRODUTO")));
            historico.setFuncionario(daoFuncionario.getFuncionarioById(rs.getInt("ID_FUNCIONARIO")));
            historico.setFornecedor(daoFornecedor.getFornecedorById(rs.getInt("ID_FORNECEDOR")));
            historico.setAcao(daoEstoqueAcao.getAcaoById(rs.getInt("ID_ACAO")));
            historico.setQtd(rs.getInt("NB_QTD"));
            historico.setData(rs.getDate("DT_DATA"));
            historico.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            
            return historico;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um produto. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<EstoqueHistorico> getEstoqueHistTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EstoqueHistorico> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetEstoqueHistTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            ProdutoDAO daoProd = new ProdutoDAO();
            FuncionarioDAO daoFuncionario = new FuncionarioDAO();
            EstoqueAcaoDAO daoEstoqueAcao = new EstoqueAcaoDAO();
            FornecedorDAO daoFornecedor = new FornecedorDAO();
            EmpresaDAO daoEmp = new EmpresaDAO();
                
            while(rs.next()){
                EstoqueHistorico historico = new EstoqueHistorico();
                historico.setIdEstoqueHist(rs.getInt("ID_ESTOQUE_HIST"));
                historico.setProduto(daoProd.getProdutoById(rs.getInt("ID_PRODUTO")));
                historico.setFuncionario(daoFuncionario.getFuncionarioById(rs.getInt("ID_FUNCIONARIO")));
                historico.setFornecedor(daoFornecedor.getFornecedorById(rs.getInt("ID_FORNECEDOR")));
                historico.setAcao(daoEstoqueAcao.getAcaoById(rs.getInt("ID_ACAO")));
                historico.setQtd(rs.getInt("NB_QTD"));
                historico.setData(rs.getDate("DT_DATA"));
                historico.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
                lista.add(historico);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de produtos. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int addEstoqueHist(EstoqueHistorico historico) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddEstoqueHist, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, historico.getEmpresa().getIdEmpresa());
            stmt.setInt(2, historico.getProduto().getIdProduto());
            stmt.setInt(3, historico.getFuncionario().getIdFuncionario());
            stmt.setInt(4, historico.getFornecedor().getIdFornecedor());
            stmt.setInt(5, historico.getAcao().getIdAcao());
            stmt.setInt(6, historico.getQtd());
            //java.sql.Date dtt = new java.sql.Date(historico.getData().getTime() ); 
            //stmt.setDate(7, dtt);
            stmt.execute();
            con.commit();
            //Inclui cod
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            historico.setIdEstoqueHist(rs.getInt(1));
            
            //Atualiza Estoque     
//            Estoque estoque = new Estoque(0);
//            prod.setEstoque(estoque);
//            EstoqueDAO daoEstoque = new EstoqueDAO();
//            daoEstoque.addEstoque(prod);
//            con.commit();
            
            return historico.getIdEstoqueHist();
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um produto. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
}
