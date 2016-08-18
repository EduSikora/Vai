package Servlet;

import Bean.Funcionario;
import Bean.Marca;
import DAO.FuncionarioDAO;
import DAO.MarcaDAO;
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

@WebServlet(name = "MarcaServlet", urlPatterns = {"/MarcaServlet"})
public class MarcaServlet extends HttpServlet {

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
        
        //Marcas #######################################################################################
        if ("listMarcas".equals(action)) {
            MarcaDAO daomarc = new MarcaDAO();
            List<Marca> lista = new ArrayList<Marca>();
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            lista = daomarc.getMarcaTodos(f.getEmpresa());
            session.setAttribute("modoMarca", "inclusao");
            request.setAttribute("Marcas", lista);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/marca.jsp");
            rd.forward(request, response);
        }
        
        if ("updMarca".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("del")!= null){
                MarcaDAO daomarc = new MarcaDAO();
                Marca marc = daomarc.getMarcaById(Integer.parseInt(request.getParameter("del")));
                //Verifica se possui produto e se poderia deletar
                if (daomarc.checkExisteProdutoByMarca(marc) == true){
                    session.setAttribute("msgErro", "marcaPossuiProduto");
                }else{
                    daomarc.delMarca(marc);
                    session.removeAttribute("msgErro");
                }
                daomarc = new MarcaDAO();
                List<Marca> lista = new ArrayList<Marca>();
                lista = daomarc.getMarcaTodos(f.getEmpresa());

                session.setAttribute("modoMarca", "inclusao");
                session.setAttribute("Marcas", lista);
                response.sendRedirect("marca.jsp");
            }
            if ((String)request.getParameter("edit")!= null){
                MarcaDAO daomarc = new MarcaDAO();
                Marca marc = daomarc.getMarcaById(Integer.parseInt(request.getParameter("edit")));
                session.removeAttribute("msgErro");
                session.setAttribute("modoMarca", "edicao");
                session.setAttribute("Marca", marc);
                response.sendRedirect("marca.jsp");
            }
        }
        
        if ("saveMarca".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("addSave")!= null) {
                MarcaDAO daomarc = new MarcaDAO();
                Marca marc = new Marca();
                marc.setNome((String)request.getParameter("marcaParamNome"));
                marc.setEmpresa(f.getEmpresa());
                
                if ((daomarc.checkExisteByNome(marc.getNome(), marc.getEmpresa()))== false){
                    daomarc.addMarca(marc);
                    daomarc = new MarcaDAO();
                    
                    List<Marca> lista = new ArrayList<Marca>();
                    lista = daomarc.getMarcaTodos(f.getEmpresa());
                
                    session.setAttribute("modoMarca", "inclusao");
                    session.setAttribute("Marcas", lista);
                    session.removeAttribute("msgErro");
                }else{
                    session.setAttribute("msgErro", "marcaJaExiste");
                }
                response.sendRedirect("marca.jsp");
            }
            if ((String)request.getParameter("updSave")!= null){
                MarcaDAO daoMarc = new MarcaDAO();
                Marca marc = daoMarc.getMarcaById(Integer.parseInt(request.getParameter("updSave")));
                marc.setNome((String)request.getParameter("marcaParamNome"));
             
                if ((daoMarc.checkExisteByNome(marc.getNome(), marc.getEmpresa()))== false){
                    //Verifica se possui marca ja criada
                    daoMarc.updMarca(marc);
                    session.removeAttribute("msgErro");

                    List<Marca> lista = new ArrayList<Marca>();
                    lista = daoMarc.getMarcaTodos(f.getEmpresa());

                    session.setAttribute("modoMarca", "inclusao");
                    session.setAttribute("Marcas", lista);
                }else{
                    session.setAttribute("msgErro", "marcaJaExiste");
                }
                response.sendRedirect("marca.jsp");
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
            Logger.getLogger(MarcaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MarcaServlet.class.getName()).log(Level.SEVERE, null, ex);
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
