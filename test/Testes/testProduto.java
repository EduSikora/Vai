package Testes;

import Bean.Marca;
import Bean.Produto;
import DAO.EmpresaDAO;
import DAO.EstoqueDAO;
import DAO.MarcaDAO;
import DAO.ProdutoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testProduto {
    
    public testProduto() {
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
    public void testgetById_retNome() throws SQLException{
        ProdutoDAO dao = new ProdutoDAO();
        Produto prodI = dao.getProdutoById(1);
        assertEquals(prodI.getNome(), "Produto01");
    }
    
    @Test
    public void testgetById_retValor() throws SQLException{
        ProdutoDAO dao = new ProdutoDAO();
        Produto prodI = dao.getProdutoById(1);
        double valorTest = 10.5;
        assertEquals(prodI.getValor(), valorTest,0);
    }
    
    @Test
    public void testgetById_retMarca() throws SQLException{
        ProdutoDAO dao = new ProdutoDAO();
        Produto prodI = dao.getProdutoById(1);
        assertEquals(prodI.getMarca().getNome(),"Marca01");
    }
    
    @Test
    public void testgetById_retEstoque() throws SQLException{
        ProdutoDAO dao = new ProdutoDAO();
        Produto prodI = dao.getProdutoById(1);
        assertEquals(prodI.getEstoque().getQtd(),200);
    }
    
    @Test
    public void testgetTodos() throws SQLException{
        ProdutoDAO daoT = new ProdutoDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        List<Produto> lista = new ArrayList();
        lista = daoT.getProdutoTodos(daoE.getEmpresaById(1));
        assertEquals(lista.isEmpty(), false);
    }

    @Test
    public void testAdd() throws SQLException{
        ProdutoDAO dao = new ProdutoDAO();
        MarcaDAO daoMarca = new MarcaDAO();
        EstoqueDAO daoEstoque = new EstoqueDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        
        Marca marca = daoMarca.getMarcaById(8);
                
        Produto prod = new Produto();
        
        prod.setNome("ProdutoTesteAdd");
        prod.setValor(12.55);
        prod.setMarca(marca);
        prod.setEmpresa(daoE.getEmpresaById(1));

        Produto prodTeste = dao.getProdutoById(dao.addProduto(prod));
        
        assertEquals(dao.checkExisteByNome("ProdutoTesteAdd", prodTeste.getEmpresa()), true);
        assertEquals(dao.checkExisteByNomeEMarca(prodTeste, prodTeste.getEmpresa()), true);
        assertEquals(daoEstoque.checkExisteByProduto(prodTeste), true);
        dao.delProduto(prod);
        assertEquals(dao.checkExisteByNome("ProdutoTesteAdd", prodTeste.getEmpresa()), false);
        assertEquals(dao.checkExisteByNomeEMarca(prodTeste, prodTeste.getEmpresa()), false);
        assertEquals(daoEstoque.checkExisteByProduto(prodTeste), false);
    }
    
//    @Test
//    public void testDel() throws SQLException{
//        ProdutoDAO daoD = new ProdutoDAO();
//        daoD.addProduto(new Produto("produtodel"));
//        Produto prd = daoD.getProdutoByNome("produtodel");
//        assertEquals(daoD.checkExisteByNome("produtodel"), true);
//        daoD.delProduto(prd);
//        assertEquals(daoD.checkExisteByNome("produtodel"), false);
//    }
//    
//    @Test
//    public void checkExisteByNome()throws SQLException{
//        ProdutoDAO daoChk = new ProdutoDAO();
//        daoChk.addProduto(new Produto("produtoExistNome"));
//        assertEquals(daoChk.checkExisteByNome("produtoExistNome"), true);
//        daoChk.delProduto(daoChk.getProdutoByNome("produtoExistNome"));
//        assertEquals(daoChk.checkExisteByNome("produtoExistNome"), false);
//    }
//    
//    @Test
//    public void getProdutoByNome()throws SQLException{
//        ProdutoDAO daoNom = new ProdutoDAO();
//        daoNom.addProduto(new Produto("produtoGetByNome"));
//        assertEquals(daoNom.getProdutoByNome("produtoGetByNome").getNome(), "produtoGetByNome");
//        daoNom.delProduto(daoNom.getProdutoByNome("produtoGetByNome"));
//    }
//    
//    @Test
//    public void updProduto_Nome() throws SQLException{
//        ProdutoDAO daoUpd = new ProdutoDAO();
//        Produto prodUpd = new Produto();
//        prodUpd.setNome("Produtoupd");
//        daoUpd.addProduto(prodUpd);
//        assertEquals(daoUpd.checkExisteByNome("Produtoupd"), true);
//        Produto prodtest = daoUpd.getProdutoByNome("Produtoupd");
//        prodtest.setNome("Produtoupd2");
//        daoUpd.updProduto(prodtest);
//        assertEquals(daoUpd.checkExisteByNome("Produtoupd2"), true);
//        assertEquals(daoUpd.checkExisteByNome("Produtoupd"), false);
//        daoUpd.delProduto(daoUpd.getProdutoByNome("Produtoupd2"));
//        assertEquals(daoUpd.checkExisteByNome("Produtoupd2"), false);
//    }
//    
//    @Test
//    public void updProduto_Valor() throws SQLException{
//        ProdutoDAO daoUpd = new ProdutoDAO();
//        Produto prodUpd = new Produto();
//        prodUpd.setNome("Produtoupdv");
//        prodUpd.setValor(12.54);
//        daoUpd.addProduto(prodUpd);
//        assertEquals(daoUpd.checkExisteByNome("Produtoupdv"), true);
//        Produto prodtest = daoUpd.getProdutoByNome("Produtoupdv");
//        assertEquals(prodtest.getValor(), 12.54,0);
//        prodtest.setValor(13.87);
//        daoUpd.updProduto(prodtest);
//        assertEquals(prodtest.getValor(), 13.87,0);
//        daoUpd.delProduto(daoUpd.getProdutoByNome("Produtoupdv"));
//        assertEquals(daoUpd.checkExisteByNome("Produtoupdv"), false);
//    }
}
