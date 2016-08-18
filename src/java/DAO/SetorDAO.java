package DAO;

import Bean.Empresa;
import Bean.Setor;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetorDAO {
    private final String stmtGetSetorById = "SELECT * FROM SETOR WHERE ID_SETOR = ?";
    private final String stmtGetSetorByNome = "SELECT * FROM SETOR WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtGetSetorTodos = "SELECT * FROM SETOR WHERE ID_EMPRESA = ?";
    private final String stmtAddSetor= "INSERT INTO SETOR (ID_EMPRESA, ST_NOME) values (?,?)";
    private final String stmtDelSetor = "DELETE FROM SETOR WHERE ID_SETOR = ?";
    private final String stmtUpdSetor = "UPDATE SETOR SET ST_NOME = ? WHERE ID_SETOR = ?";
    private final String stmtCheckExisteBySetor = "SELECT * FROM SETOR WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtCheckExisteFuncionarioBySetor = "SELECT * FROM funcionario WHERE ID_SETOR = ?";
    private final String stmtQtdFuncionariosBySetor = "SELECT COUNT(ID_FUNCIONARIO) as `qtd` FROM FUNCIONARIO WHERE ID_SETOR = ? GROUP BY ID_SETOR";
    
    public Setor getSetorById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetSetorById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Setor set = new Setor();
            set.setIdSetor(rs.getInt("ID_SETOR"));
            set.setNome(rs.getString("ST_NOME"));
            EmpresaDAO daoEmp = new EmpresaDAO();
            set.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return set;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um setor. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public Setor getSetorByNome(String nome, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetSetorByNome);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            Setor setRet = new Setor();
            setRet.setIdSetor(rs.getInt("ID_SETOR"));
            setRet.setNome(rs.getString("ST_NOME"));
            EmpresaDAO daoEmp = new EmpresaDAO();
            setRet.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return setRet;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de setores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
        
    public List<Setor> getSetorTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Setor> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetSetorTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Setor set = new Setor();
                EmpresaDAO daoEmp = new EmpresaDAO();
                
                set.setIdSetor(rs.getInt("ID_SETOR"));
                set.setNome(rs.getString("ST_NOME"));
                set.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
                set.setQtdFuncionarios(getQtdFuncionariosBySetor(set));
                lista.add(set);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de setores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int addSetor(Setor set) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddSetor, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, set.getEmpresa().getIdEmpresa());
            stmt.setString(2, set.getNome());
            stmt.execute();
            con.commit();
            //resgata o id
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um setor. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void delSetor(Setor set) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelSetor);
            stmt.setInt(1, set.getIdSetor());
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
            stmt = con.prepareStatement(stmtCheckExisteBySetor);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de setores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public void updSetor(Setor set) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtUpdSetor);
            stmt.setString(1, set.getNome());
            stmt.setInt(2, set.getIdSetor());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public boolean checkExisteFuncionarioBySetor(Setor set) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtCheckExisteFuncionarioBySetor);
            stmt.setInt(1, set.getIdSetor());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de setores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int getQtdFuncionariosBySetor(Setor set) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtQtdFuncionariosBySetor);
            stmt.setInt(1, set.getIdSetor());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return rs.getInt("qtd");
            return 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar a lista de setores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
}

