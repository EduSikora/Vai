package DAO;

import Bean.Empresa;
import Bean.Servico;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {
    
    private final String stmtGetServicoById = "SELECT * FROM SERVICO WHERE ID_SERVICO = ?";
    private final String stmtGetServicoByNome = "SELECT * FROM SERVICO WHERE ST_NOME LIKE ? AND ST_DESC LIKE ? AND ID_EMPRESA = ?";
    private final String stmtGetServicoTodos = "SELECT * FROM SERVICO WHERE ID_EMPRESA = ?";
    private final String stmtAddServico = "INSERT INTO SERVICO (ID_EMPRESA, ST_NOME, ST_DESC) values (?,?,?)";
    private final String stmtDelServico = "DELETE FROM SERVICO WHERE ID_SERVICO = ?";
    private final String stmtUpdServico = "UPDATE SERVICO SET ST_NOME = ?, ST_DESC = ? WHERE ID_SERVICO = ?";
    private final String stmtCheckExisteByServico = "SELECT * FROM SERVICO WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    
    public Servico getServicoById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetServicoById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Servico serv = new Servico();
            EmpresaDAO daoEmp = new EmpresaDAO();
            serv.setIdServico(rs.getInt("ID_SERVICO"));
            serv.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            serv.setNome(rs.getString("ST_NOME"));
            serv.setDesc(rs.getString("ST_DESC"));
            return serv;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um serviço. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<Servico> getServicoByNomeOuDesc(String nome, String desc, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Servico> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetServicoByNome);
            stmt.setString(1, '%'+nome+'%');
            stmt.setString(2, '%'+desc+'%');
            stmt.setInt(3, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            while(rs.next()){
                Servico serv = new Servico();
                serv.setIdServico(rs.getInt("ID_SERVICO"));
                serv.setEmpresa(empresa);
                serv.setNome(rs.getString("ST_NOME"));
                serv.setDesc(rs.getString("ST_DESC"));
                
                lista.add(serv);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de serviços. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<Servico> getServicoTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Servico> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetServicoTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            while(rs.next()){
                Servico serv = new Servico();
                serv.setIdServico(rs.getInt("ID_SERVICO"));
                serv.setEmpresa(empresa);
                serv.setNome(rs.getString("ST_NOME"));
                serv.setDesc(rs.getString("ST_DESC"));
                
                lista.add(serv);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de serviços. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public int addServico(Servico serv) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddServico, PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, serv.getEmpresa().getIdEmpresa());
            stmt.setString(2, serv.getNome());
            stmt.setString(3, serv.getDesc());
            stmt.execute();
            con.commit();
            //Inclui cod
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            serv.setIdServico(rs.getInt(1));
            con.commit();
            return serv.getIdServico();
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um serviço. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void delServico(Servico serv) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelServico);
            stmt.setInt(1, serv.getIdServico());
            stmt.executeUpdate();
            con.commit();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }

    public void updServico(Servico serv) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtUpdServico);
            System.out.println(serv.getIdServico() + serv.getNome() + serv.getDesc());
            stmt.setString(1, serv.getNome());
            stmt.setString(2, serv.getDesc());
            stmt.setInt(3, serv.getIdServico());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }

    public boolean checkExisteByNome(String nome, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtCheckExisteByServico);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de serviços. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
}
