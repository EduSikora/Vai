package Servlet;

import Bean.Funcionario;
import Bean.Categoria;
import DAO.FuncionarioDAO;
import DAO.CategoriaDAO;
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

@WebServlet(name = "CategoriaServlet", urlPatterns = {"/CategoriaServlet"})
public class CategoriaServlet extends HttpServlet {

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
        
        //Categorias #######################################################################################
        if ("listCategorias".equals(action)) {
            CategoriaDAO daocat = new CategoriaDAO();
            List<Categoria> lista = new ArrayList<Categoria>();
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            lista = daocat.getCategoriaTodos(f.getEmpresa());
            session.setAttribute("modoCategoria", "inclusao");
            request.setAttribute("Categorias", lista);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/categoria.jsp");
            rd.forward(request, response);
        }
        
        if ("updCategoria".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("del")!= null){
                CategoriaDAO daocat = new CategoriaDAO();
                Categoria cat = daocat.getCategoriaById(Integer.parseInt(request.getParameter("del")));
                //Verifica se possui Servico e se poderia deletar -- IMPLEMENTAR
                if (daocat.checkExisteServicoByCategoria(cat) == true){
                    session.setAttribute("msgErro", "categoriaPossuiServico");
                }else{
                    daocat.delCategoria(cat);
                    session.removeAttribute("msgErro");
                }
                daocat = new CategoriaDAO();
                List<Categoria> lista = new ArrayList<Categoria>();
                lista = daocat.getCategoriaTodos(f.getEmpresa());

                session.setAttribute("modoCategoria", "inclusao");
                session.setAttribute("Categorias", lista);
                response.sendRedirect("categoria.jsp");
            }
            if ((String)request.getParameter("edit")!= null){
                CategoriaDAO daocat = new CategoriaDAO();
                Categoria cat = daocat.getCategoriaById(Integer.parseInt(request.getParameter("edit")));
                session.removeAttribute("msgErro");
                session.setAttribute("modoCategoria", "edicao");
                session.setAttribute("Categoria", cat);
                response.sendRedirect("categoria.jsp");
            }
        }
        
        if ("saveCategoria".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("addSave")!= null) {
                CategoriaDAO daocat = new CategoriaDAO();
                Categoria cat = new Categoria();
                cat.setNome((String)request.getParameter("categoriaParamNome"));
                cat.setEmpresa(f.getEmpresa());
                
                if ((daocat.checkExisteByNome(cat.getNome(), cat.getEmpresa()))== false){
                    daocat.addCategoria(cat);
                    daocat = new CategoriaDAO();
                    
                    List<Categoria> lista = new ArrayList<Categoria>();
                    lista = daocat.getCategoriaTodos(f.getEmpresa());
                
                    session.setAttribute("modoCategoria", "inclusao");
                    session.setAttribute("Categorias", lista);
                    session.removeAttribute("msgErro");
                }else{
                    session.setAttribute("msgErro", "categoriaJaExiste");
                }
                response.sendRedirect("categoria.jsp");
            }
            if ((String)request.getParameter("updSave")!= null){
                CategoriaDAO daoCat = new CategoriaDAO();
                Categoria cat = daoCat.getCategoriaById(Integer.parseInt(request.getParameter("updSave")));
                cat.setNome((String)request.getParameter("categoriaParamNome"));
             
                if ((daoCat.checkExisteByNome(cat.getNome(), cat.getEmpresa()))== false){
                    //Verifica se possui categoria ja criada
                    daoCat.updCategoria(cat);
                    session.removeAttribute("msgErro");

                    List<Categoria> lista = new ArrayList<Categoria>();
                    lista = daoCat.getCategoriaTodos(f.getEmpresa());

                    session.setAttribute("modoCategoria", "inclusao");
                    session.setAttribute("Categorias", lista);
                }else{
                    session.setAttribute("msgErro", "categoriaJaExiste");
                }
                response.sendRedirect("categoria.jsp");
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
            Logger.getLogger(CategoriaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CategoriaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
