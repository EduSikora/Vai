package Testes;

import Bean.Fornecedor;
import Bean.Produto;
import DAO.EmpresaDAO;
import DAO.FornecedorDAO;
import DAO.ProdutoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testFornecedor {
    
    public testFornecedor() {
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
    public void testgetById() throws SQLException{
        FornecedorDAO dao = new FornecedorDAO();
        assertEquals(dao.getFornecedorById(1).getNome(), "Fornecedor01");
    }
    
    @Test
    public void testGetTodos() throws SQLException{
        FornecedorDAO daoT = new FornecedorDAO();
        List<Fornecedor> lista = new ArrayList();
        EmpresaDAO daoE = new EmpresaDAO();
        lista = daoT.getFornecedorTodos(daoE.getEmpresaById(1));
        assertEquals(lista.isEmpty(), false);
    }
    
    @Test
    public void testAdd() throws SQLException{
        FornecedorDAO dao = new FornecedorDAO();
        Fornecedor fornec = new Fornecedor();
        EmpresaDAO daoE = new EmpresaDAO();
        fornec.setNome("fornecedorTestadd");
        fornec.setEmpresa(daoE.getEmpresaById(1));
        dao.addFornecedor(fornec);
        
        assertEquals(dao.getFornecedorByNome("fornecedorTestadd", fornec.getEmpresa()).getNome(), "fornecedorTestadd");
        dao.delFornecedor(dao.getFornecedorByNome("fornecedorTestadd", fornec.getEmpresa()));
    }

    @Test
    public void testGetFornecedorByNome()throws SQLException{
        FornecedorDAO daoNom = new FornecedorDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Fornecedor fornec = new Fornecedor();
        fornec.setNome("fornecedorTestget");
        fornec.setEmpresa(daoE.getEmpresaById(1));
        daoNom.addFornecedor(fornec);
        
        assertEquals(daoNom.getFornecedorByNome("fornecedorTestget", fornec.getEmpresa()).getNome(), "fornecedorTestget");
        daoNom.delFornecedor(daoNom.getFornecedorByNome("fornecedorTestget", fornec.getEmpresa()));
    }

    @Test
    public void checkExisteByNome()throws SQLException{
        FornecedorDAO daoChk = new FornecedorDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Fornecedor fornec = new Fornecedor();
        fornec.setNome("fornecedorTestChk");
        fornec.setEmpresa(daoE.getEmpresaById(1));
        daoChk.addFornecedor(fornec);
        
        assertEquals(daoChk.checkExisteByNome("fornecedorTestChk",fornec.getEmpresa()), true);
        daoChk.delFornecedor(daoChk.getFornecedorByNome("fornecedorTestChk", fornec.getEmpresa()));
        assertEquals(daoChk.checkExisteByNome("fornecedorTestChk", fornec.getEmpresa()), false);
    }
    
    @Test
    public void testDel()throws SQLException{
        FornecedorDAO daoDel = new FornecedorDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Fornecedor fornec = new Fornecedor();
        fornec.setNome("fornecedorTestDel");
        fornec.setEmpresa(daoE.getEmpresaById(1));
        daoDel.addFornecedor(fornec);
        
        Fornecedor fornec2 = daoDel.getFornecedorByNome("fornecedorTestDel", fornec.getEmpresa());
        assertEquals(fornec2.getNome(), "fornecedorTestDel");
        daoDel.delFornecedor(fornec2);
        assertEquals(daoDel.checkExisteByNome("fornecedorTestDel", fornec.getEmpresa()), false);
    }
    
    @Test
    public void updFornecedor_Nome() throws SQLException{
        FornecedorDAO daoUpd = new FornecedorDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Fornecedor fornec = new Fornecedor();
        fornec.setNome("Fornecedorupd");
        fornec.setEmpresa(daoE.getEmpresaById(1));
        
        daoUpd.addFornecedor(fornec);
        assertEquals(daoUpd.checkExisteByNome("Fornecedorupd", fornec.getEmpresa()), true);
        Fornecedor fornectest = daoUpd.getFornecedorByNome("Fornecedorupd", fornec.getEmpresa());
        fornectest.setNome("Fornecedorupd2");
        daoUpd.updFornecedor(fornectest);
        assertEquals(daoUpd.checkExisteByNome("Fornecedorupd2", fornec.getEmpresa()), true);
        assertEquals(daoUpd.checkExisteByNome("Fornecedorupd", fornec.getEmpresa()), false);
        daoUpd.delFornecedor(daoUpd.getFornecedorByNome("Fornecedorupd2", fornec.getEmpresa()));
        assertEquals(daoUpd.checkExisteByNome("Fornecedorupd2", fornec.getEmpresa()), false);
    }
}
