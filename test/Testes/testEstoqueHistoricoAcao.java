package Testes;

import Bean.EstoqueAcao;
import DAO.EmpresaDAO;
import DAO.EstoqueAcaoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testEstoqueHistoricoAcao {
    
    public testEstoqueHistoricoAcao() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getEstoqueByProduto()throws SQLException{
        EstoqueAcaoDAO dao = new EstoqueAcaoDAO();
        EstoqueAcao acao = dao.getAcaoById(1);
        assertEquals(acao.getAcao(), "INCLUSAO");
    }
    
    @Test
    public void getEstoqueByNome()throws SQLException{
        EstoqueAcaoDAO dao = new EstoqueAcaoDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        EstoqueAcao acao = dao.getAcaoByNome("INCLUSAO", daoE.getEmpresaById(1));
        assertEquals(acao.getIdAcao(), 1);
    }
    
    @Test
    public void testGetTodos() throws SQLException{
        EstoqueAcaoDAO dao = new EstoqueAcaoDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        List<EstoqueAcao> lista = new ArrayList();
        lista = dao.getAcaoTodos(daoE.getEmpresaById(1));
        assertEquals(lista.isEmpty(), false);
    }
}
