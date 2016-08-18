package DAO;

import Bean.Empresa;
import Bean.Estoque;
import Bean.Produto;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProdutoDAO {
    
    private final String stmtGetProdutoById = "SELECT * FROM PRODUTO WHERE ID_PRODUTO = ?";
//    private final String stmtGetProdutoByNomeEFornec = "SELECT * FROM PRODUTO WHERE ST_NOME = ? AND ID_MARCA = ?";
    private final String stmtGetProdutoTodos = "SELECT * FROM PRODUTO WHERE ID_EMPRESA = ?";
    private final String stmtAddProduto = "INSERT INTO PRODUTO (ID_EMPRESA, ST_NOME, NB_VALOR, ID_MARCA) values (?,?,?,?)";
    private final String stmtDelProduto = "DELETE FROM PRODUTO WHERE ID_PRODUTO = ?";
    private final String stmtUpdProduto = "UPDATE PRODUTO SET ST_NOME = ?, NB_VALOR = ?, ID_MARCA = ? WHERE ID_PRODUTO = ?";
    private final String stmtCheckExisteByNome = "SELECT * FROM PRODUTO WHERE ST_NOME = ? AND ID_EMPRESA = ?";
    private final String stmtCheckExisteByNomeEMarca = "SELECT * FROM PRODUTO WHERE ST_NOME = ? AND ID_MARCA = ? AND ID_EMPRESA = ?";

    public Produto getProdutoById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetProdutoById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Produto prod = new Produto();
            prod.setIdProduto(rs.getInt("ID_PRODUTO"));
            prod.setNome(rs.getString("ST_NOME"));
            prod.setValor(rs.getDouble("NB_VALOR"));
            
            //Inclui o marca
            MarcaDAO daoFornec = new MarcaDAO();
            prod.setMarca(daoFornec.getMarcaById(rs.getInt("ID_MARCA")));
            
            EstoqueDAO daoEstoque = new EstoqueDAO();
            prod.setEstoque(daoEstoque.getEstoqueByProduto(prod));
            
            EmpresaDAO daoEmp = new EmpresaDAO();
            prod.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            
            return prod;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um produto. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<Produto> getProdutoTodos(Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> lista = new ArrayList();
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetProdutoTodos);
            stmt.setInt(1, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            while(rs.next()){
                Produto prod = new Produto();
                prod.setIdProduto(rs.getInt("ID_PRODUTO"));
                prod.setNome(rs.getString("ST_NOME"));
                prod.setValor(rs.getDouble("NB_VALOR"));
                
                //Inclui o marca
                MarcaDAO daoFornec = new MarcaDAO();
                prod.setMarca(daoFornec.getMarcaById(rs.getInt("ID_MARCA")));
            
                //Inclui o estoque
                EstoqueDAO daoEstoque = new EstoqueDAO();
                prod.setEstoque(daoEstoque.getEstoqueByProduto(prod));

                EmpresaDAO daoEmp = new EmpresaDAO();
                prod.setEmpresa(daoEmp.getEmpresaById(rs.getInt("ID_EMPRESA")));
            
                lista.add(prod);
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
    
    public int addProduto(Produto prod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddProduto, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, prod.getEmpresa().getIdEmpresa());
            stmt.setString(2, prod.getNome());
            stmt.setDouble(3, prod.getValor());
            stmt.setInt(4, prod.getMarca().getIdMarca());
            stmt.execute();
            con.commit();
            //Inclui cod
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            prod.setIdProduto(rs.getInt(1));
            
            //Inclui Estoque     
            Estoque estoque = new Estoque(0);
            prod.setEstoque(estoque);
            EstoqueDAO daoEstoque = new EstoqueDAO();
            daoEstoque.addEstoque(prod);
            con.commit();
            
            return prod.getIdProduto();
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um produto. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void delProduto(Produto prod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            //Deleta Estoque
            EstoqueDAO daoEst = new EstoqueDAO();
            daoEst.delEstoqueByProduto(prod);
            
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelProduto);
            stmt.setInt(1, prod.getIdProduto());
            stmt.executeUpdate();
            con.commit();
            
            EstoqueDAO daoEstoque = new EstoqueDAO();
            daoEstoque.delEstoqueByProduto(prod);
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
            stmt = con.prepareStatement(stmtCheckExisteByNome);
            stmt.setString(1, nome);
            stmt.setInt(2, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de produtos. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public boolean checkExisteByNomeEMarca(Produto prod, Empresa empresa) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtCheckExisteByNomeEMarca);
            stmt.setString(1, prod.getNome());
            stmt.setInt(2, prod.getMarca().getIdMarca());
            stmt.setInt(3, empresa.getIdEmpresa());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow()>0)
                return true;
            return false;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de produtos. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }

    public void updProduto(Produto prod) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtUpdProduto);
            stmt.setString(1, prod.getNome());
            stmt.setDouble(2, prod.getValor());
            stmt.setInt(3, prod.getMarca().getIdMarca());
            stmt.setInt(4, prod.getIdProduto());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
//    public Produto getProdutoByNomeEFornec(String nome, String nomeMarca) throws SQLException{
//        Connection con = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        try{
//            con = new ConnectionFactory().getConnection();
//            stmt = con.prepareStatement(stmtGetProdutoByNomeEFornec);
//            stmt.setString(1, nome);
//            stmt.setString(2, nomeMarca);
//            rs = stmt.executeQuery();
//            rs.next();
//            Produto prod = new Produto();
//            prod.setCodProduto(rs.getInt("ID_PRODUTO"));
//            prod.setNome(rs.getString("nome"));
//            prod.setValor(rs.getDouble("NB_VALOR"));
//            
//            //Inclui o fornecedor
//            MarcaDAO daoFornec = new MarcaDAO();
//            prod.setMarca(daoFornec.getMarcaById(rs.getInt("ID_MARCA")));
//            
//            return prod;
//        } catch (SQLException ex) {
//            throw new RuntimeException("Erro ao consultar uma lista de produtos. Origem="+ex.getMessage());
//        }finally{
//            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
//            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
//            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
//        }
//    }
//    
}