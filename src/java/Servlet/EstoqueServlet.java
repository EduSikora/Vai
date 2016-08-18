package Servlet;

import Bean.EstoqueAcao;
import Bean.EstoqueHistorico;
import Bean.Fornecedor;
import Bean.Funcionario;
import Bean.Marca;
import Bean.Produto;
import DAO.EmpresaDAO;
import DAO.EstoqueAcaoDAO;
import DAO.EstoqueDAO;
import DAO.EstoqueHistoricoDAO;
import DAO.FornecedorDAO;
import DAO.FuncionarioDAO;
import DAO.MarcaDAO;
import DAO.ProdutoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "EstoqueServlet", urlPatterns = {"/EstoqueServlet"})
public class EstoqueServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        session.removeAttribute("msgErro");

        FuncionarioDAO daoFunc = new FuncionarioDAO();
        Funcionario func = daoFunc.getFuncionarioById(1);
        session.setAttribute("funcionario", func);
        
        //EstoqueHistorico #######################################################################################
        if ("listEstoqueHist".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            EstoqueHistoricoDAO dao = new EstoqueHistoricoDAO();
            List<EstoqueHistorico> lista = new ArrayList<EstoqueHistorico>();
            lista = dao.getEstoqueHistTodos(f.getEmpresa());
            session.setAttribute("modoEstoqueHist", "inclusao");
            request.setAttribute("EstoqueHist", lista);
            
            //preenche o combo Produto *************************************** trocar de lugar e chamar na inicializacao da pagina
            ProdutoDAO daoProd = new ProdutoDAO();
            List<Produto> listaProd = new ArrayList<Produto>();
            listaProd = daoProd.getProdutoTodos(f.getEmpresa());
            request.setAttribute("cmbProdutos",listaProd);

            MarcaDAO daoMarc = new MarcaDAO();
            List<Marca> listaMarca = new ArrayList<Marca>();
            listaMarca = daoMarc.getMarcaTodos(f.getEmpresa());
            session.setAttribute("cmbMarcas",listaMarca);
            
            FornecedorDAO daoFornec = new FornecedorDAO();
            List<Fornecedor> listaFornec = new ArrayList<Fornecedor>();
            listaFornec = daoFornec.getFornecedorTodos(f.getEmpresa());
            request.setAttribute("cmbFornecedores",listaFornec);
            
            EstoqueAcaoDAO daoEstoqueAcao = new EstoqueAcaoDAO();
            List<EstoqueAcao> listaAcao = new ArrayList<EstoqueAcao>();
            listaAcao = daoEstoqueAcao.getAcaoTodos(f.getEmpresa());
            request.setAttribute("cmbAcoes",listaAcao);
            
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/estoque.jsp");
            rd.forward(request, response);
        }
        
        if ("saveEstoqueHist".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("addSave")!= null) {
                EstoqueHistorico historico = new EstoqueHistorico();
                EstoqueHistoricoDAO daoH = new EstoqueHistoricoDAO();
                ProdutoDAO daoProd = new ProdutoDAO();
                EstoqueAcaoDAO daoEstoqueAcao = new EstoqueAcaoDAO();
                FornecedorDAO daoFornecedor = new FornecedorDAO();
                historico.setAcao(daoEstoqueAcao.getAcaoById(Integer.parseInt(request.getParameter("cmbAcao"))));
                historico.setFuncionario(f);
                historico.setEmpresa(f.getEmpresa());
                historico.setFornecedor(daoFornecedor.getFornecedorById(Integer.parseInt(request.getParameter("cmbFornecedor"))));
                historico.setQtd(Integer.parseInt(request.getParameter("historicoParamQtd")));
                historico.setProduto(daoProd.getProdutoById(Integer.parseInt(request.getParameter("cmbProduto"))));
                
                EstoqueDAO daoEs = new EstoqueDAO();
                daoEs.updEstoqueByHistorico(historico);
                daoH.addEstoqueHist(historico);

                List<EstoqueHistorico> lista = new ArrayList<EstoqueHistorico>();
                lista = daoH.getEstoqueHistTodos(f.getEmpresa());
                session.setAttribute("modoEstoqueHist", "inclusao");
                request.setAttribute("EstoqueHist", lista);
                
                session.removeAttribute("msgErro");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/estoque.jsp");
                rd.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EstoqueServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EstoqueServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
