package DAO;

import Bean.Estoque;
import Bean.EstoqueHistorico;
import Bean.Produto;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstoqueDAO {
    
    private final String stmtGetQtdByProduto = "SELECT NB_QTD FROM ESTOQUE WHERE ID_PRODUTO = ?";
    private final String stmtCheckExisteByProduto = "SELECT NB_QTD FROM ESTOQUE WHERE ID_PRODUTO = ?";
    private final String stmtAddEstoque = "INSERT INTO ESTOQUE (ID_PRODUTO, NB_QTD) VALUES (?,?)";
    private final String stmtDelEstoque = "DELETE FROM ESTOQUE WHERE ID_PRODUTO = ?";
    private final String stmtEstoque = "UPDATE ESTOQUE SET NB_QTD = ? WHERE ID_PRODUTO = ?";
    
    public Estoque getEstoqueByProduto(Produto prod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetQtdByProduto);
            stmt.setInt(1, prod.getIdProduto());
            rs = stmt.executeQuery();
            rs.next();
            return new Estoque(rs.getInt("NB_QTD"));
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar um estoque. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public boolean checkExisteByProduto(Produto prod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtCheckExisteByProduto);
            stmt.setInt(1, prod.getIdProduto());
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
    
    public void addEstoque(Produto prod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddEstoque, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, prod.getIdProduto());
            stmt.setInt(2, prod.getEstoque().getQtd());
            stmt.execute();
            con.commit();
            
//            //resgata o id
//            ResultSet rs = stmt.getGeneratedKeys();
//            rs.next();
//            int retId = rs.getInt(1);
//            return retId;
            
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um produto. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }

    public void delEstoqueByProduto(Produto prod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtDelEstoque);
            stmt.setInt(1, prod.getIdProduto());
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void updEstoqueByHistorico(EstoqueHistorico historico) throws SQLException{
        Connection con=null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtEstoque);
            if (historico.getAcao().getAcao().equals("INCLUSAO")){
                stmt.setInt(1, historico.getProduto().getEstoque().getQtd() + historico.getQtd());
            }else{
                stmt.setInt(1, historico.getProduto().getEstoque().getQtd() - historico.getQtd());
            }            
            stmt.setInt(2, historico.getProduto().getIdProduto());
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
