package DAO;

import Bean.Empresa;
import Bean.Categoria;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private final String stmtGetCategoriaById = "SELECT * FROM CATEGORIA WHERE ID_CATEGORIA = ?";
    private final String stmtGetCategoriaByNome = "SELECT * FROM CATEGORIA WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtGetCategoriaTodos = "SELECT * FROM CATEGORIA WHERE ID_EMPRESA = ?";
    private final String stmtAddCategoria= "INSERT INTO CATEGORIA (ID_EMPRESA, ST_NOME) values (?,?)";
    private final String stmtDelCategoria = "DELETE FROM CATEGORIA WHERE ID_CATEGORIA = ?";
    private final String stmtUpdCategoria = "UPDATE CATEGORIA SET ST_NOME = ? WHERE ID_CATEGORIA = ?";
    private final String stmtCheckExisteByCategoria = "SELECT * FROM CATEGORIA WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtCheckExisteServicoByCategoria = "SELECT * FROM SERVICO WHERE ID_CATEGORIA = ?";
    private final String stmtQtdServicosByCategoria = "SELECT COUNT(ID_SERVICO) as `qtd` FROM SERVICO WHERE ID_CATEGORIA = ? GROUP BY ID_CATEGORIA";
    
    public Categoria getCategoriaById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetCategoriaById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Categoria cat = new Categoria();
            cat.setIdCategoria(rs.getInt("ID_SETOR"));
            cat.setNome(rs.getString("ST_NOME"));
            EmpresaDAO daoEmp = new EmpresaDAO();
            cat.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return cat;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma categoria. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public Categoria getCategoriaByNome(String nome, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetCategoriaByNome);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            Categoria cat = new Categoria();
            cat.setIdCategoria(rs.getInt("ID_CATEGORIA"));
            cat.setNome(rs.getString("ST_NOME"));
            EmpresaDAO daoEmp = new EmpresaDAO();
            cat.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return cat;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de categorias. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
        
    public List<Categoria> getCategoriaTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Categoria> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetCategoriaTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Categoria cat = new Categoria();
                EmpresaDAO daoEmp = new EmpresaDAO();
                
                cat.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                cat.setNome(rs.getString("ST_NOME"));
                cat.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
                cat.setQtdServicos(getQtdServicosByCategoria(cat));
                lista.add(cat);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de categoriaes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int addCategoria(Categoria cat) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddCategoria, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, cat.getEmpresa().getIdEmpresa());
            stmt.setString(2, cat.getNome());
            stmt.execute();
            con.commit();
            //resgata o id
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir uma categoria. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void delCategoria(Categoria cat) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelCategoria);
            stmt.setInt(1, cat.getIdCategoria());
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
            stmt = con.prepareStatement(stmtCheckExisteByCategoria);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de categorias. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public void updCategoria(Categoria cat) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtUpdCategoria);
            stmt.setString(1, cat.getNome());
            stmt.setInt(2, cat.getIdCategoria());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public boolean checkExisteServicoByCategoria(Categoria cat) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtCheckExisteServicoByCategoria);
            stmt.setInt(1, cat.getIdCategoria());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de categoriaes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int getQtdServicosByCategoria(Categoria cat) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtQtdServicosByCategoria);
            stmt.setInt(1, cat.getIdCategoria());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return rs.getInt("qtd");
            return 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar a lista de categorias. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
}

