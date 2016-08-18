package DAO;

import Bean.Empresa;
import Bean.Endereco;
import Bean.Funcionario;
import Bean.Login;
import Bean.Setor;
import bd.ConnectionFactory;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FuncionarioDAO {
    private final String stmtAddEnd = "INSERT INTO endereco (ST_ESTADO ,ST_CIDADE, ST_BAIRRO, ST_CEP, "
            + "ST_RUA, NB_NUMERO, ST_COMPLEMENTO) VALUES (?,?,?,?,?,?,?)";
    
    private final String stmtAddFunc = "INSERT INTO funcionario (ID_EMPRESA ,ST_NOME, "
            + "ST_CPF, ST_TELEFONE, ST_EMAIL, ST_SENHA, ID_ENDERECO, NB_ATIVO, ID_SETOR) VALUES (?,?,?,?,?,?,?,?,?)";
    
    private final String stmtUpdEnd = "UPDATE endereco SET ST_ESTADO=?, ST_CIDADE=?, ST_BAIRRO=?, ST_CEP=?, "
            + "ST_RUA=?, NB_NUMERO=?, ST_COMPLEMENTO=? WHERE ID_ENDERECO=?";
    
    private final String stmtUpdFunc = "UPDATE funcionario SET ST_NOME=?,"
            + "ST_CPF=?, ST_TELEFONE=?, NB_ATIVO=?, ID_SETOR=? WHERE ID_FUNCIONARIO=?";
    
    private final String stmtUpdFuncSenha = "UPDATE funcionario SET ST_NOME=?, "
            + "ST_CPF=?, ST_TELEFONE=?, NB_ATIVO=?, ID_SETOR=?, ST_SENHA=? WHERE ID_FUNCIONARIO=?";
    
    private final String stmtGetFuncByCod = "SELECT f.*,e.*,emp.*,s.* FROM funcionario f INNER JOIN endereco e "
            + "ON (f.ID_ENDERECO = e.ID_ENDERECO) INNER JOIN empresa emp ON (emp.ID_EMPRESA = f.ID_EMPRESA) "
            + "INNER JOIN setor s ON (f.ID_SETOR = s.ID_SETOR) WHERE f.ID_FUNCIONARIO = ?";
    
    private final String stmtGetFunc = "SELECT * FROM funcionario WHERE ST_NOME LIKE ? OR "
            + "ST_EMAIL LIKE ? OR ST_CPF LIKE ? AND ID_EMPRESA=?";
    
    private final String stmtGetLogin = "SELECT * FROM funcionario f WHERE ST_EMAIL=?";
    
    private final String stmtListaFunc = "SELECT * FROM funcionario f WHERE ID_EMPRESA =?";
    
    public void addFuncionario(Funcionario func) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Connection con = null;
        PreparedStatement stmt = null;
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte senhaCrypt[] = algorithm.digest(func.getSenha().getBytes("UTF-8"));
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtAddEnd, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, func.getEndereco().getEstado());
            stmt.setString(2, func.getEndereco().getCidade());
            stmt.setString(3, func.getEndereco().getBairro());
            stmt.setString(4, func.getEndereco().getCep());
            stmt.setString(5, func.getEndereco().getRua());
            stmt.setInt(6, func.getEndereco().getNumero());
            stmt.setString(7, func.getEndereco().getComplemento());
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int i = rs.getInt(1);
            func.getEndereco().setCodEndereco(i);
            
            stmt = con.prepareStatement(stmtAddFunc, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, func.getEmpresa().getIdEmpresa());
            stmt.setString(2, func.getNome());
            stmt.setString(3, func.getCpf());
            stmt.setString(4, func.getTelefone());
            stmt.setString(5, func.getEmail());
            stmt.setString(6, func.getSenha());
            stmt.setInt(7, func.getEndereco().getCodEndereco());            
            int ati=0;
            if(func.isAtivo()) ati=1;
            stmt.setInt(8, ati);
            stmt.setInt(9, func.getSetor().getIdSetor());
            stmt.execute();//grava no banco
            //Seta o id no cliente
            ResultSet rs2 = stmt.getGeneratedKeys();
            rs2.next();
            int j = rs2.getInt(1);
            func.setIdFuncionario(j);
            con.commit();
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){ex1.printStackTrace();  System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um cliente no banco de dados. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public void updFuncionario(Funcionario func) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtUpdEnd, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, func.getEndereco().getEstado());
            stmt.setString(2, func.getEndereco().getCidade());
            stmt.setString(3, func.getEndereco().getBairro());
            stmt.setString(4, func.getEndereco().getCep());
            stmt.setString(5, func.getEndereco().getRua());
            stmt.setInt(6, func.getEndereco().getNumero());
            stmt.setString(7, func.getEndereco().getComplemento());
            stmt.setInt(8, func.getEndereco().getCodEndereco());
            stmt.executeUpdate();
            
            if (func.getSenha() == null)
                stmt = con.prepareStatement(stmtUpdFunc, PreparedStatement.RETURN_GENERATED_KEYS);
            else
                stmt = con.prepareStatement(stmtUpdFuncSenha, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, func.getNome());
            stmt.setString(2, func.getCpf());
            stmt.setString(3, func.getTelefone());
            if(func.isAtivo()) stmt.setInt(4, 1);
            else stmt.setInt(4, 0);
            stmt.setInt(5,func.getSetor().getIdSetor());
            if (func.getSenha() == null)
                stmt.setInt(6, func.getIdFuncionario());
            else{
                stmt.setString(6, func.getSenha());
                stmt.setInt(7, func.getIdFuncionario());
            }
            stmt.executeUpdate();//grava no banco
            con.commit();
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){ex1.printStackTrace();  System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao inserir um cliente no banco de dados. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
    }
    
    public Funcionario getFuncionarioById(int cod) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {   
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetFuncByCod);
            stmt.setInt(1, cod);
            rs = stmt.executeQuery();
            rs.next();
            Funcionario f = new Funcionario();
            Endereco e = new Endereco();
            Empresa em = new Empresa();
            Setor set = new Setor();
            SetorDAO setdao = new SetorDAO();
            
            e.setCodEndereco(rs.getInt("ID_ENDERECO"));
            e.setEstado(rs.getString("ST_ESTADO"));
            e.setCidade(rs.getString("ST_CIDADE"));
            e.setBairro(rs.getString("ST_BAIRRO"));
            e.setCep(rs.getString("ST_CEP"));
            e.setRua(rs.getString("ST_RUA"));
            e.setNumero(rs.getInt("NB_NUMERO"));
            e.setComplemento(rs.getString("ST_COMPLEMENTO"));
            
            em.setIdEmpresa(rs.getInt("ID_EMPRESA"));
            em.setNome(rs.getString("ST_NOMEEMP"));
            em.setCnpj(rs.getString("ST_CNPJ"));
            
            set.setEmpresa(em);
            set.setIdSetor(rs.getInt("ID_SETOR"));
            set.setNome(rs.getString("s.ST_NOME"));
            set.setQtdFuncionarios(setdao.getQtdFuncionariosBySetor(set));
            
            
            f.setEmpresa(em);
            f.setEndereco(e);
            f.setSetor(set);
            f.setIdFuncionario(rs.getInt("ID_FUNCIONARIO"));
            f.setNome(rs.getString("ST_NOME"));
            f.setEmail(rs.getString("ST_EMAIL"));
            f.setCpf(rs.getString("ST_CPF"));
            f.setTelefone(rs.getString("ST_TELEFONE"));
            
   
            if (rs.getInt("NB_ATIVO")== 1) f.setAtivo(true);
            else f.setAtivo(false);

            return f;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de clientes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<Funcionario> getFuncuncionarioTodos(String busca,int empresaId) throws SQLException, ClassNotFoundException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Funcionario> lista = new ArrayList();
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/os", "root", "");
            // con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetFunc);
            stmt.setString(1, '%'+busca+'%');
            stmt.setString(2, '%'+busca+'%');
            stmt.setString(3, '%'+busca+'%');
            stmt.setInt(4, empresaId); // Esse empresa ID tem que vir da sessao de quem invocou esse metodo
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Funcionario f = new Funcionario();
                f.setIdFuncionario(rs.getInt("ID_FUNCIONARIO"));
                f.setNome(rs.getString("ST_NOME"));
                f.setEmail(rs.getString("ST_EMAIL"));
                f.setCpf(rs.getString("ST_CPF"));
                if (rs.getInt("NB_ATIVO") == 1)
                    f.setAtivo(true);
                else
                    f.setAtivo(false);
                lista.add(f);
            }
            
            return lista;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de clientes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public Login getLogin(Login login) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Login feito = new Login();
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtGetLogin);
            stmt.setString(1, login.getEmail());
            rs = stmt.executeQuery();
            rs.next();
            if (login.getSenha().equals(rs.getString("ST_SENHA"))){
                feito.setNome(rs.getString("ST_NOME"));
                feito.setEmail(rs.getString("ST_EMAIL"));
                feito.setIdFuncionario(rs.getInt("ID_FUNCIONARIO"));
                feito.setAtivo(rs.getInt("NB_ATIVO"));
            }
            return feito;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de clientes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
    
    public List<Funcionario> ListaFunc(int empresaId) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Funcionario> lista = new ArrayList();
        try {
            con = new ConnectionFactory().getConnection();
            stmt = con.prepareStatement(stmtListaFunc);
            stmt.setInt(1, empresaId); // Esse empresa ID tem que vir da sessao de quem invocou esse metodo
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Funcionario f = new Funcionario();
                f.setIdFuncionario(rs.getInt("ID_FUNCIONARIO"));
                f.setNome(rs.getString("ST_NOME"));
                f.setEmail(rs.getString("ST_EMAIL"));
                f.setCpf(rs.getString("ST_CPF"));
                if (rs.getInt("NB_ATIVO") == 1)
                    f.setAtivo(true);
                else
                    f.setAtivo(false);
                lista.add(f);
            }
            
            return lista;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao consultar uma lista de clientes. Origem="+ex.getMessage());
        }finally{
            try{rs.close();}catch(Exception ex){System.out.println("Erro ao fechar result set. Ex="+ex.getMessage());};
            try{stmt.close();}catch(Exception ex){System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};               
        }
    }
}
