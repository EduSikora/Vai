package Testes;

import Bean.Marca;
import DAO.EmpresaDAO;
import DAO.MarcaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testMarca {
    
    public testMarca() {
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
        MarcaDAO dao = new MarcaDAO();
        assertEquals(dao.getMarcaById(1).getNome(), "Marca01");
    }
    
    @Test
    public void testGetTodos() throws SQLException{
        MarcaDAO daoT = new MarcaDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        List<Marca> lista = new ArrayList();
        lista = daoT.getMarcaTodos(daoE.getEmpresaById(1));
        assertEquals(lista.isEmpty(), false);
    }
    
    @Test
    public void testAdd() throws SQLException{
        MarcaDAO dao = new MarcaDAO();
        Marca marc = new Marca();
        EmpresaDAO daoE = new EmpresaDAO();
        marc.setNome("marcaTestadd");
        marc.setEmpresa(daoE.getEmpresaById(1));
        
        Marca marcTest = dao.getMarcaById(dao.addMarca(marc));
        assertEquals(marcTest.getNome(), "marcaTestadd");
        assertEquals(dao.checkExisteByNome(marcTest.getNome(), marcTest.getEmpresa()), true);
        dao.delMarca(marcTest);
        assertEquals(dao.checkExisteByNome(marcTest.getNome(), marcTest.getEmpresa()), false);
    }

    @Test
    public void testGetMarcaByNome()throws SQLException{
        MarcaDAO daoNom = new MarcaDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Marca marc = new Marca();
        marc.setNome("marcaTestget");
        marc.setEmpresa(daoE.getEmpresaById(1));
        daoNom.addMarca(marc);
        assertEquals(daoNom.getMarcaByNome("marcaTestget", marc.getEmpresa()).getNome(), "marcaTestget");
        daoNom.delMarca(daoNom.getMarcaByNome("marcaTestget", marc.getEmpresa()));
    }

    @Test
    public void checkExisteByNome()throws SQLException{
        MarcaDAO daoChk = new MarcaDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Marca marc = new Marca();
        marc.setNome("marcaTestChk");
        marc.setEmpresa(daoE.getEmpresaById(1));
        daoChk.addMarca(marc);
        
        assertEquals(daoChk.checkExisteByNome("marcaTestChk", marc.getEmpresa()), true);
        daoChk.delMarca(daoChk.getMarcaByNome("marcaTestChk", marc.getEmpresa()));
        assertEquals(daoChk.checkExisteByNome("marcaTestChk", marc.getEmpresa()), false);
    }
    
    @Test
    public void testDel()throws SQLException{
        MarcaDAO daoDel = new MarcaDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        Marca marc = new Marca();
        marc.setNome("marcaTestDel");
        marc.setEmpresa(daoE.getEmpresaById(1));
        Marca marc2 = daoDel.getMarcaById(daoDel.addMarca(marc));

        assertEquals(marc2.getNome(), "marcaTestDel");
        daoDel.delMarca(marc2);
        assertEquals(daoDel.checkExisteByNome("marcaTestDel", marc2.getEmpresa()), false);
    }
    
    @Test
    public void updMarca_Nome() throws SQLException{
        MarcaDAO daoUpd = new MarcaDAO();
//        Marca marc = daoUpd.getMarcaById(1);
//        marc.setNome("testeMarca");
//        daoUpd.updMarca(marc);
        Marca marc = new Marca();
        marc.setNome("Marcaupd");
       
        EmpresaDAO daoE = new EmpresaDAO();
        marc.setEmpresa(daoE.getEmpresaById(1));
        
        daoUpd.addMarca(marc);
        assertEquals(daoUpd.checkExisteByNome("Marcaupd", marc.getEmpresa()), true);
        Marca marctest = daoUpd.getMarcaByNome("Marcaupd", marc.getEmpresa());
        marctest.setNome("Marcaupd2");
        daoUpd.updMarca(marctest);
        assertEquals(daoUpd.checkExisteByNome("Marcaupd2", marc.getEmpresa()), true);
        assertEquals(daoUpd.checkExisteByNome("Marcaupd", marc.getEmpresa()), false);
        daoUpd.delMarca(daoUpd.getMarcaByNome("Marcaupd2", marc.getEmpresa()));
        assertEquals(daoUpd.checkExisteByNome("Marcaupd2", marc.getEmpresa()), false);
    }
}
