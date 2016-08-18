package Testes;

import DAO.EstoqueDAO;
import DAO.ProdutoDAO;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testEstoque {
    
    public testEstoque() {
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
        ProdutoDAO daoChk = new ProdutoDAO();
        EstoqueDAO daoEst = new EstoqueDAO();
        
        assertEquals(daoEst.getEstoqueByProduto(daoChk.getProdutoById(1)).getQtd(), 200);
    }
    
    @Test
    public void checkExisteByProduto()throws SQLException{
        ProdutoDAO daoChk = new ProdutoDAO();
        EstoqueDAO daoEst = new EstoqueDAO();
        
        assertEquals(daoEst.checkExisteByProduto(daoChk.getProdutoById(1)), true);
    }
}
