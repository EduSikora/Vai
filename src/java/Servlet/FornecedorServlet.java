package Servlet;

import Bean.Funcionario;
import Bean.Fornecedor;
import DAO.FornecedorDAO;
import DAO.FuncionarioDAO;
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

@WebServlet(name = "FornecedorServlet", urlPatterns = {"/FornecedorServlet"})
public class FornecedorServlet extends HttpServlet {

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
        
        //Fornecedores #######################################################################################
        if ("listFornecedores".equals(action)) {
            FornecedorDAO daoFornec = new FornecedorDAO();
            List<Fornecedor> lista = new ArrayList<Fornecedor>();
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            lista = daoFornec.getFornecedorTodos(f.getEmpresa());
            session.setAttribute("modoFornecedor", "inclusao");
            request.setAttribute("Fornecedores", lista);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/fornecedor.jsp");
            rd.forward(request, response);
        }
        
        if ("updFornecedor".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("del")!= null){
                FornecedorDAO daoFornec = new FornecedorDAO();
                Fornecedor fornec = daoFornec.getFornecedorById(Integer.parseInt(request.getParameter("del")));
                //Verifica se possui estoque e se poderia deletar *******************************************************************************REVER
//                if (daoFornec.checkExisteProdutoByMarca(fornec) == true){
//                    session.setAttribute("msgErro", "marcaPossuiProduto");
//                }else{
                    daoFornec.delFornecedor(fornec);
                    session.removeAttribute("msgErro");
//                }
                daoFornec = new FornecedorDAO();
                List<Fornecedor> lista = new ArrayList<Fornecedor>();
                lista = daoFornec.getFornecedorTodos(f.getEmpresa());

                session.setAttribute("modoFornecedor", "inclusao");
                session.setAttribute("Fornecedores", lista);
                response.sendRedirect("fornecedor.jsp");
            }
            if ((String)request.getParameter("edit")!= null){
                FornecedorDAO daoFornec = new FornecedorDAO();
                Fornecedor fornec = daoFornec.getFornecedorById(Integer.parseInt(request.getParameter("edit")));
                session.removeAttribute("msgErro");
                session.setAttribute("modoFornecedor", "edicao");
                session.setAttribute("Fornecedor", fornec);
                response.sendRedirect("fornecedor.jsp");
            }
        }
        
        if ("saveFornecedor".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("addSave")!= null) {
                FornecedorDAO daoFornec = new FornecedorDAO();
                Fornecedor fornec = new Fornecedor();
                fornec.setNome((String)request.getParameter("fornecedorParamNome"));
                fornec.setEmpresa(f.getEmpresa());
                if ((daoFornec.checkExisteByNome(fornec.getNome(), fornec.getEmpresa()))== false){
                    daoFornec.addFornecedor(fornec);
                    daoFornec = new FornecedorDAO();
                    
                    List<Fornecedor> lista = new ArrayList<Fornecedor>();
                    lista = daoFornec.getFornecedorTodos(f.getEmpresa());
                
                    session.setAttribute("modoFornecedor", "inclusao");
                    session.setAttribute("Fornecedores", lista);
                    session.removeAttribute("msgErro");
                }else{
                    session.setAttribute("msgErro", "fornecedorJaExiste");
                }
                response.sendRedirect("fornecedor.jsp");
            }
            if ((String)request.getParameter("updSave")!= null){
                FornecedorDAO daoMarc = new FornecedorDAO();
                Fornecedor fornec = daoMarc.getFornecedorById(Integer.parseInt(request.getParameter("updSave")));
                fornec.setNome((String)request.getParameter("fornecedorParamNome"));
             
                if ((daoMarc.checkExisteByNome(fornec.getNome(), fornec.getEmpresa()))== false){
                    //Verifica se possui fornecedor ja criado
                    daoMarc.updFornecedor(fornec);
                    session.removeAttribute("msgErro");

                    List<Fornecedor> lista = new ArrayList<Fornecedor>();
                    lista = daoMarc.getFornecedorTodos(f.getEmpresa());

                    session.setAttribute("modoFornecedor", "inclusao");
                    session.setAttribute("Fornecedores", lista);
                }else{
                    session.setAttribute("msgErro", "fornecedorJaExiste");
                }
                response.sendRedirect("fornecedor.jsp");
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
            Logger.getLogger(FornecedorServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FornecedorServlet.class.getName()).log(Level.SEVERE, null, ex);
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
