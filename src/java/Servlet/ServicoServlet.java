package Servlet;

import Bean.Funcionario;
import Bean.Servico;
import DAO.FuncionarioDAO;
import DAO.ServicoDAO;
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

@WebServlet(name = "ServicoServlet", urlPatterns = {"/ServicoServlet"})
public class ServicoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        session.removeAttribute("msgErro");
        
        FuncionarioDAO daoFunc = new FuncionarioDAO();
        Funcionario func = daoFunc.getFuncionarioById(1);
        session.setAttribute("funcionario", func);
        
        //Serviços #######################################################################################
        
        if ("updServico".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("del")!= null){
                ServicoDAO daoserv = new ServicoDAO();
                Servico serv = daoserv.getServicoById(Integer.parseInt(request.getParameter("del")));

                daoserv.delServico(serv);
                session.removeAttribute("msgErro");
                
                daoserv = new ServicoDAO();
                List<Servico> lista = new ArrayList<Servico>();
                lista = daoserv.getServicoTodos(f.getEmpresa());

                session.setAttribute("modoServico", "inclusao");
                session.setAttribute("Servicos", lista);
                response.sendRedirect("servico.jsp");
            }
            if ((String)request.getParameter("edit")!= null){
                ServicoDAO daoserv = new ServicoDAO();
                Servico serv = daoserv.getServicoById(Integer.parseInt(request.getParameter("edit")));
                session.removeAttribute("msgErro");
                session.setAttribute("modoServico", "edicao");
                session.setAttribute("Servico", serv);
                response.sendRedirect("servico.jsp");
            }
        }
        
        if ("acaoServico".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            ServicoDAO daoserv = new ServicoDAO();
            if ((String)request.getParameter("buscar")!= null) {
                List<Servico> lista = new ArrayList<Servico>();
                lista = daoserv.getServicoByNomeOuDesc((String)request.getParameter("servicoParamNome"), (String)request.getParameter("servicoParamDesc"), f.getEmpresa());
                session.setAttribute("modoServico", "inclusao");
                request.setAttribute("Servicos", lista);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/servico.jsp");
                rd.forward(request, response);
            }
            
            if ((String)request.getParameter("addSave")!= null) {
                Servico serv = new Servico();
                serv.setNome((String)request.getParameter("servicoParamNome"));
                serv.setDesc((String)request.getParameter("servicoParamDesc"));
                serv.setEmpresa(f.getEmpresa());
                
                if ((daoserv.checkExisteByNome(serv.getNome(), serv.getEmpresa()))== false){
                    daoserv.addServico(serv);
                    daoserv = new ServicoDAO();
                    List<Servico> lista = new ArrayList<Servico>();
                    lista = daoserv.getServicoTodos(f.getEmpresa());                
                    session.setAttribute("modoServico", "inclusao");
                    session.setAttribute("Servicos", lista);
                    session.removeAttribute("msgErro");
                }else{
                    session.setAttribute("msgErro", "servicoJaExiste");
                }                
                response.sendRedirect("servico.jsp");
            }
            
            if ((String)request.getParameter("updSave")!= null){
                Servico servOld = daoserv.getServicoById(Integer.parseInt(request.getParameter("updSave")));
                Servico servNew = new Servico();
                servNew.setNome((String)request.getParameter("servicoParamNome"));
                servNew.setDesc((String)request.getParameter("servicoParamDesc"));
                servNew.setIdServico(servOld.getIdServico());
                
                //Alteracao somente da descrição
                if(servOld.getNome().equals(servNew.getNome()) || servOld.getDesc().equals(servNew.getDesc())){
                    daoserv.updServico(servNew);
                    session.removeAttribute("msgErro");
                    List<Servico> lista = new ArrayList<Servico>();
                    lista = daoserv.getServicoTodos(f.getEmpresa());
                    session.setAttribute("modoServico", "inclusao");
                    session.setAttribute("Servicos", lista);
                }else{//Com alteracao de nome ou descriçao
                    //Verifica se possui serviço ja criado
                    daoserv.updServico(servNew);
                    session.removeAttribute("msgErro");
                    List<Servico> lista = new ArrayList<Servico>();
                    lista = daoserv.getServicoTodos(f.getEmpresa());
                    session.setAttribute("modoServico", "inclusao");
                    session.setAttribute("Servicos", lista);
                }
                response.sendRedirect("servico.jsp");
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
            Logger.getLogger(ServicoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServicoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
