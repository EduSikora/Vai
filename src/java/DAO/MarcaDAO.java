package DAO;

import Bean.Empresa;
import Bean.Marca;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {
    private final String stmtGetMarcaById = "SELECT * FROM MARCA WHERE ID_MARCA = ?";
    private final String stmtGetMarcaByNome = "SELECT * FROM MARCA WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtGetMarcaTodos = "SELECT * FROM MARCA WHERE ID_EMPRESA = ?";
    private final String stmtAddMarca= "INSERT INTO MARCA (ID_EMPRESA, ST_NOME) values (?,?)";
    private final String stmtDelMarca = "DELETE FROM MARCA WHERE ID_MARCA = ?";
    private final String stmtUpdMarca = "UPDATE MARCA SET ST_NOME = ? WHERE ID_MARCA = ?";
    private final String stmtCheckExisteByMarca = "SELECT * FROM MARCA WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtCheckExisteProdutoByMarca = "SELECT * FROM produto WHERE ID_MARCA = ?";
    private final String stmtQtdProdutosByMarca = "SELECT COUNT(ID_PRODUTO) as `qtd` FROM PRODUTO WHERE ID_MARCA = ? GROUP BY ID_MARCA";
    
    public Marca getMarcaById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetMarcaById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Marca marc = new Marca();
            marc.setIdMarca(rs.getInt("ID_MARCA"));
            marc.setNome(rs.getString("ST_NOME"));
            marc.setQtdProdutos(getQtdProdutosByMarca(marc));
            EmpresaDAO daoEmp = new EmpresaDAO();
            marc.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return marc;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um marca. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public Marca getMarcaByNome(String nome, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetMarcaByNome);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            Marca marcRet = new Marca();
            marcRet.setIdMarca(rs.getInt("ID_MARCA"));
            marcRet.setNome(rs.getString("ST_NOME"));
            marcRet.setQtdProdutos(getQtdProdutosByMarca(marcRet));
            EmpresaDAO daoEmp = new EmpresaDAO();
            marcRet.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            return marcRet;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de marcas. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
        
    public List<Marca> getMarcaTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Marca> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetMarcaTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Marca marc = new Marca();
                EmpresaDAO daoEmp = new EmpresaDAO();
                marc.setIdMarca(rs.getInt("ID_MARCA"));
                marc.setNome(rs.getString("ST_NOME"));
                marc.setQtdProdutos(getQtdProdutosByMarca(marc));
                marc.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
                lista.add(marc);
            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de marcaes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int addMarca(Marca marc) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddMarca, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, marc.getEmpresa().getIdEmpresa());
            stmt.setString(2, marc.getNome());
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
    
    public void delMarca(Marca marc) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelMarca);
            stmt.setInt(1, marc.getIdMarca());
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
            stmt = con.prepareStatement(stmtCheckExisteByMarca);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de marcas. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public void updMarca(Marca marc) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtUpdMarca);
            stmt.setString(1, marc.getNome());
            stmt.setInt(2, marc.getIdMarca());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public boolean checkExisteProdutoByMarca(Marca marc) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtCheckExisteProdutoByMarca);
            stmt.setInt(1, marc.getIdMarca());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de marcas. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public int getQtdProdutosByMarca(Marca marc) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtQtdProdutosByMarca);
            stmt.setInt(1, marc.getIdMarca());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return rs.getInt("qtd");
            return 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar a lista de produtos. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
}

