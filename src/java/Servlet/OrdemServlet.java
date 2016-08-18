package Servlet;

import Bean.Funcionario;
import Bean.Ordem;
import Bean.OrdemProduto;
import Bean.Produto;
import DAO.FuncionarioDAO;
import DAO.OrdemDAO;
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

@WebServlet(name = "OrdemServlet", urlPatterns = {"/OrdemServlet"})
public class OrdemServlet extends HttpServlet {

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

        FuncionarioDAO daoF = new FuncionarioDAO();
        Funcionario func = daoF.getFuncionarioById(1);
        session.setAttribute("funcionario", func);
        
        
        if ("getOrdem".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            
            OrdemDAO daoO = new OrdemDAO();
            
            //Lista todas ordens
            List<Ordem> listaO = new ArrayList<Ordem>();
            listaO = daoO.getOrdemTodas(f.getEmpresa());
            request.setAttribute("Ordens", listaO);
            
            //Incluir a ordem
            Ordem ordem = daoO.getOrdemById(Integer.parseInt(request.getParameter("ordemParamId")));
            session.setAttribute("Ordem", ordem);
            
            //Inclui produtos da ordem
            List<OrdemProduto> listaOP = new ArrayList<OrdemProduto>();
            listaOP = ordem.getListProdutos();
            request.setAttribute("OrdemProduto", listaOP);
            
            //Inclui todos produtos
            ProdutoDAO daoprod = new ProdutoDAO();
            List<Produto> listaP = new ArrayList<Produto>();
            listaP = daoprod.getProdutoTodos(f.getEmpresa());
            request.setAttribute("Produtos", listaP);
            
            //session.setAttribute("modoProduto", "inclusao");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/ordem.jsp");
            rd.forward(request, response);
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
            Logger.getLogger(OrdemServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(OrdemServlet.class.getName()).log(Level.SEVERE, null, ex);
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
