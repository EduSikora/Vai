package Servlet;

import Bean.Funcionario;
import Bean.Setor;
import DAO.FuncionarioDAO;
import DAO.SetorDAO;
import java.io.IOException;
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

@WebServlet(name = "SetorServlet", urlPatterns = {"/SetorServlet"})
public class SetorServlet extends HttpServlet {

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
        
        //Setores #######################################################################################
        if ("listSetores".equals(action)) {
            SetorDAO daoset = new SetorDAO();
            List<Setor> lista = new ArrayList<Setor>();
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            lista = daoset.getSetorTodos(f.getEmpresa());
            session.setAttribute("modoSetor", "inclusao");
            request.setAttribute("Setores", lista);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/setor.jsp");
            rd.forward(request, response);
        }
        
        if ("updSetor".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("del")!= null){
                SetorDAO daoset = new SetorDAO();
                Setor set = daoset.getSetorById(Integer.parseInt(request.getParameter("del")));
                //Verifica se possui Funcionario e se poderia deletar -- IMPLEMENTAR
                if (daoset.checkExisteFuncionarioBySetor(set) == true){
                    session.setAttribute("msgErro", "setorPossuiFuncionario");
                }else{
                    daoset.delSetor(set);
                    session.removeAttribute("msgErro");
                }
                daoset = new SetorDAO();
                List<Setor> lista = new ArrayList<Setor>();
                lista = daoset.getSetorTodos(f.getEmpresa());

                session.setAttribute("modoSetor", "inclusao");
                session.setAttribute("Setores", lista);
                response.sendRedirect("setor.jsp");
            }
            if ((String)request.getParameter("edit")!= null){
                SetorDAO daoset = new SetorDAO();
                Setor set = daoset.getSetorById(Integer.parseInt(request.getParameter("edit")));
                session.removeAttribute("msgErro");
                session.setAttribute("modoSetor", "edicao");
                session.setAttribute("Setor", set);
                response.sendRedirect("setor.jsp");
            }
        }
        
        if ("saveSetor".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("addSave")!= null) {
                SetorDAO daoset = new SetorDAO();
                Setor set = new Setor();
                set.setNome((String)request.getParameter("setorParamNome"));
                set.setEmpresa(f.getEmpresa());
                
                if ((daoset.checkExisteByNome(set.getNome(), set.getEmpresa()))== false){
                    daoset.addSetor(set);
                    daoset = new SetorDAO();
                    
                    List<Setor> lista = new ArrayList<Setor>();
                    lista = daoset.getSetorTodos(f.getEmpresa());
                
                    session.setAttribute("modoSetor", "inclusao");
                    session.setAttribute("Setores", lista);
                    session.removeAttribute("msgErro");
                }else{
                    session.setAttribute("msgErro", "setorJaExiste");
                }
                response.sendRedirect("setor.jsp");
            }
            if ((String)request.getParameter("updSave")!= null){
                SetorDAO daoSetor = new SetorDAO();
                Setor set = daoSetor.getSetorById(Integer.parseInt(request.getParameter("updSave")));
                set.setNome((String)request.getParameter("setorParamNome"));
             
                if ((daoSetor.checkExisteByNome(set.getNome(), set.getEmpresa()))== false){
                    //Verifica se possui setor ja criada
                    daoSetor.updSetor(set);
                    session.removeAttribute("msgErro");

                    List<Setor> lista = new ArrayList<Setor>();
                    lista = daoSetor.getSetorTodos(f.getEmpresa());

                    session.setAttribute("modoSetor", "inclusao");
                    session.setAttribute("Setores", lista);
                }else{
                    session.setAttribute("msgErro", "setorJaExiste");
                }
                response.sendRedirect("setor.jsp");
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
            Logger.getLogger(SetorServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SetorServlet.class.getName()).log(Level.SEVERE, null, ex);
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
