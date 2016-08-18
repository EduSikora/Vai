
package DAO;

import Bean.Empresa;
import bd.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpresaDAO {
    
    private final String stmtGetEmpresaById = "SELECT * FROM EMPRESA WHERE ID_EMPRESA = ?";
      
    public Empresa getEmpresaById(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetEmpresaById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            Empresa emp = new Empresa();
            emp.setIdEmpresa(rs.getInt("ID_EMPRESA"));
            emp.setNome(rs.getString("ST_NOMEEMP"));
            return emp;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma empresa. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
}
