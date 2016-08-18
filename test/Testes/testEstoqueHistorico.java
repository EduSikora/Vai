package Testes;

import Bean.EstoqueHistorico;
import Bean.Produto;
import DAO.EmpresaDAO;
import DAO.EstoqueAcaoDAO;
import DAO.EstoqueHistoricoDAO;
import DAO.FornecedorDAO;
import DAO.FuncionarioDAO;
import DAO.ProdutoDAO;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testEstoqueHistorico {
    
    public testEstoqueHistorico() {
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
    public void getHistoricoById_ValidaRetorno()throws SQLException{
        EstoqueHistoricoDAO dao = new EstoqueHistoricoDAO();
        EstoqueHistorico estoque = dao.getEstoqueHistById(1);
        assertEquals(estoque.getProduto().getNome(), "Produto01");
        assertEquals(estoque.getFornecedor().getNome(), "Fornecedor01");
        assertEquals(estoque.getAcao().getAcao(), "INCLUSAO");
        assertEquals(estoque.getQtd(), 10);
        
    }
    
    @Test
    public void getEstoqueHistTodos() throws SQLException{
        EstoqueHistoricoDAO dao = new EstoqueHistoricoDAO();
        EmpresaDAO daoE = new EmpresaDAO();
        List<EstoqueHistorico> lista = new ArrayList();
        lista = dao.getEstoqueHistTodos(daoE.getEmpresaById(1));
        assertEquals(lista.isEmpty(), false);
    }
    
    @Test
    public void testAdd() throws SQLException, ParseException{
        EstoqueHistorico historico = new EstoqueHistorico();
        EstoqueHistoricoDAO daoH = new EstoqueHistoricoDAO();
        ProdutoDAO daoProd = new ProdutoDAO();
        FuncionarioDAO daoFuncionario = new FuncionarioDAO();
        EstoqueAcaoDAO daoEstoqueAcao = new EstoqueAcaoDAO();
        FornecedorDAO daoFornecedor = new FornecedorDAO();
        EmpresaDAO daoEmp = new EmpresaDAO();
        historico.setProduto(daoProd.getProdutoById(1));
        historico.setFuncionario(daoFuncionario.getFuncionarioById(1));
        historico.setFornecedor(daoFornecedor.getFornecedorById(1));
        historico.setAcao(daoEstoqueAcao.getAcaoById(2));
        historico.setQtd(301);
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //String str = "2016-04-10 14:12";
        //Date dt = format.parse(str);
        //historico.setData(dt);
        historico.setEmpresa(daoEmp.getEmpresaById(1));
        daoH.addEstoqueHist(historico);
        
    }
}
