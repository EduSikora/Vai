package DAO;

import Bean.Fornecedor;
import Bean.Empresa;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {
    private final String stmtGetFornecedorById = "SELECT * FROM FORNECEDOR WHERE ID_FORNECEDOR = ?";
    private final String stmtGetFornecedorByNome = "SELECT * FROM FORNECEDOR WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtGetFornecedorTodos = "SELECT * FROM FORNECEDOR WHERE ID_EMPRESA = ?";
    private final String stmtAddFornecedor= "INSERT INTO FORNECEDOR (ID_EMPRESA, ST_NOME) VALUES (?, ?)";
    private final String stmtDelFornecedor = "DELETE FROM FORNECEDOR WHERE ID_FORNECEDOR = ?";
    private final String stmtUpdFornecedor = "UPDATE FORNECEDOR SET ST_NOME = ? WHERE ID_FORNECEDOR = ?";
    private final String stmtCheckExisteByFornecedor = "SELECT * FROM FORNECEDOR WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    
    public Fornecedor getFornecedorById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetFornecedorById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Fornecedor fornec = new Fornecedor();
            fornec.setIdFornecedor(rs.getInt("ID_FORNECEDOR"));
            fornec.setNome(rs.getString("ST_NOME"));
            EmpresaDAO daoEmp = new EmpresaDAO();
            fornec.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return fornec;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um fornecedor. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public Fornecedor getFornecedorByNome(String nome, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetFornecedorByNome);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            Fornecedor fornec = new Fornecedor();
            fornec.setIdFornecedor(rs.getInt("ID_FORNECEDOR"));
            fornec.setNome(rs.getString("ST_NOME"));
            EmpresaDAO daoEmp = new EmpresaDAO();
            fornec.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return fornec;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de fornecedores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
        
    public List<Fornecedor> getFornecedorTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Fornecedor> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetFornecedorTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Fornecedor fornec = new Fornecedor();
                EmpresaDAO daoEmp = new EmpresaDAO();
                fornec.setIdFornecedor(rs.getInt("ID_FORNECEDOR"));
                fornec.setNome(rs.getString("ST_NOME"));
                fornec.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
                lista.add(fornec);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de fornecedores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int addFornecedor(Fornecedor fornec) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddFornecedor, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, fornec.getEmpresa().getIdEmpresa());
            stmt.setString(2, fornec.getNome());
            stmt.execute();
            con.commit();
            
            //resgata o id
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um produto. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void delFornecedor(Fornecedor fornec) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelFornecedor);
            stmt.setInt(1, fornec.getIdFornecedor());
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
            stmt = con.prepareStatement(stmtCheckExisteByFornecedor);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de fornecedores. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public void updFornecedor(Fornecedor fornec) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtUpdFornecedor);
            stmt.setString(1, fornec.getNome());
            stmt.setInt(2, fornec.getIdFornecedor());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
}
