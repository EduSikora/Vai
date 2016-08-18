package DAO;

import Bean.Empresa;
import Bean.EstoqueAcao;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueAcaoDAO {
    private final String stmtGetAcaoById = "SELECT * FROM ESTOQUE_ACAO WHERE ID_ACAO = ?";
    private final String stmtGetAcaoByNome = "SELECT * FROM ESTOQUE_ACAO WHERE ST_ACAO = ? AND ID_EMPRESA = ?";
    private final String stmtGetAcaoTodos = "SELECT * FROM ESTOQUE_ACAO WHERE ID_EMPRESA = ?";
    
    public EstoqueAcao getAcaoById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetAcaoById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            EmpresaDAO daoEmp = new EmpresaDAO();
            EstoqueAcao acao = new EstoqueAcao(rs.getInt("ID_ACAO"), rs.getString("ST_ACAO"), daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return acao;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um acao. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public EstoqueAcao getAcaoByNome(String acao, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetAcaoByNome);
            stmt.setString(1, acao);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            EmpresaDAO daoEmp = new EmpresaDAO();
            EstoqueAcao acaoRet = new EstoqueAcao(rs.getInt("ID_ACAO"), rs.getString("ST_ACAO"), daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return acaoRet;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um acao. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<EstoqueAcao> getAcaoTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EstoqueAcao> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetAcaoTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            while(rs.next()){
                EstoqueAcao acao = new EstoqueAcao();
                EmpresaDAO daoEmp = new EmpresaDAO();
                acao.setIdAcao(rs.getInt("ID_ACAO"));
                acao.setAcao(rs.getString("ST_ACAO"));
                acao.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
                lista.add(acao);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de acoes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

}
