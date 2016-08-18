package DAO;

import Bean.Empresa;
import Bean.Ordem;
import Bean.OrdemProduto;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdemDAO {
   
    private final String stmtGetProdutosByIdOrdem = "SELECT * FROM ORDEM_PRODUTO WHERE ID_ORDEM = ?";
    private final String stmtGetOrdemTodas = "SELECT * FROM ORDEM WHERE ID_EMPRESA = ?";
    private final String stmtGetOrdemById = "SELECT * FROM ORDEM WHERE ID_ORDEM = ?";
    
    public List<OrdemProduto> getProdutosByIdOrdem(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OrdemProduto> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetProdutosByIdOrdem);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            ProdutoDAO dao = new ProdutoDAO();
            while(rs.next()){
                OrdemProduto op = new OrdemProduto();
                op.setProduto(dao.getProdutoById(rs.getInt("ID_PRODUTO")));
                op.setQtd(rs.getInt("NB_QTD"));
                lista.add(op);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de produtos e ordens. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
     
    public Ordem getOrdemById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetOrdemById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Ordem ordem = new Ordem();
            ordem.setIdOrdem(rs.getInt("ID_ORDEM"));
            
            ordem.setListProdutos(getProdutosByIdOrdem(id));
            
            EmpresaDAO daoE = new EmpresaDAO();
            ordem.setEmpresa(daoE.getEmpresaById(rs.getInt("ID_EMPRESA")));
            
            FuncionarioDAO daoF = new FuncionarioDAO();
            ordem.setFuncionario(daoF.getFuncionarioById(rs.getInt("ID_FUNCIONARIO")));
            
            return ordem;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma ordem. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<Ordem> getOrdemTodas(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Ordem> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetOrdemTodas);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            while(rs.next()){
                Ordem ordem = new Ordem();
                ordem = this.getOrdemById(rs.getInt("ID_ORDEM"));
                lista.add(ordem);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de ordens. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
}
