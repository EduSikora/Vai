package Servlet;


import Bean.Empresa;
import Bean.Endereco;
import Bean.Funcionario;
import Bean.Login;
import Bean.Setor;
import DAO.EmpresaDAO;
import DAO.FuncionarioDAO;
import DAO.SetorDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@WebServlet(name = "FuncionarioServlet", urlPatterns = {"/FuncionarioServlet"})
public class FuncionarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, RuntimeException, NoSuchAlgorithmException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            
            if ("buscafuncionario".equals(action)) { //------------------- BUSCA FUNCIONARIO ---------------------
                FuncionarioDAO funcdao;
                funcdao = new FuncionarioDAO();
                List<Funcionario> listafunc = newArrayList();
                HttpSession session = request.getSession();
                Funcionario fun = (Funcionario) session.getAttribute("funcionario");
                listafunc = funcdao.getFuncuncionarioTodos((String)request.getParameter("buscafunc"), 1); // listafunc = funcdao.getFuncuncionarioTodos((String)request.getParameter("buscafunc"), );
                request.setAttribute("listafunc", listafunc);
                
                EmpresaDAO empDAO = new EmpresaDAO();
                Empresa emp = empDAO.getEmpresaById(1);
                
                SetorDAO daoSet = new SetorDAO();
                List<Setor> listaSetor = new ArrayList<Setor>();
                listaSetor = daoSet.getSetorTodos(emp);
                session.setAttribute("cmbSetores",listaSetor);
                
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/funcionario.jsp");
                rd.forward(request, response);
//                response.sendRedirect("funcionario.jsp");
            }
            
            else if ("listafuncionario".equals(action)) { //------------------- LISTA FUNCIONARIO ---------------------
                FuncionarioDAO funcdao;
                funcdao = new FuncionarioDAO();
                List<Funcionario> listafunc = newArrayList();
                HttpSession session = request.getSession();
                Funcionario fun = (Funcionario) session.getAttribute("funcionario");
                listafunc = funcdao.ListaFunc(1); // listafunc = funcdao.getFuncuncionarioTodos((String)request.getParameter("buscafunc"), );
                request.setAttribute("listafunc", listafunc);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/funcionario.jsp");
                rd.forward(request, response);
//                response.sendRedirect("funcionario.jsp");
            }
                
            else if ("salvafuncionario".equals(action)) { //------------------- SALVA FUNCIONARIO ---------------------
                Funcionario func = new Funcionario();
                Endereco end = new Endereco();
                boolean invalido=false;
                HttpSession session = request.getSession();
                session.removeAttribute("func");
                session.removeAttribute("listafunc");
                Funcionario fun = (Funcionario) session.getAttribute("funcionario");
                Empresa emp = new Empresa();
                emp.setIdEmpresa(1);
                SetorDAO setDAO = new SetorDAO();
                Setor set = new Setor();
                
                if(!((String)request.getParameter("idFuncionario")).isEmpty())
                    func.setIdFuncionario(Integer.parseInt((String)request.getParameter("idFuncionario")));
                
                func.setEmpresa(emp); //fun.getEmpresa()
                // AQUI tem que ser o setEmpresaId(session.getEmpresaId), entende?
                
                boolean ativ = false;
                if ("1".equals(request.getParameter("ativo")))
                    func.setAtivo(true);
                else func.setAtivo(false);
                
                        
                if (!((String)request.getParameter("nome")).isEmpty())
                    func.setNome((String)request.getParameter("nome"));
                else invalido = true;
                
                if (!((String)request.getParameter("cpf")).isEmpty())
                    func.setCpf((String)request.getParameter("cpf"));
                else invalido = true;
                
                if (!((String)request.getParameter("telefone")).isEmpty())
                    func.setTelefone((String)request.getParameter("telefone"));
                else invalido = true;
                
                if(((String)request.getParameter("idFuncionario")).isEmpty())
                    if (!((String)request.getParameter("email")).isEmpty())
                        func.setEmail((String)request.getParameter("email"));
                    else invalido = true;
                
                if(!((String)request.getParameter("senha")).isEmpty())
                    func.setSenha((String)request.getParameter("senha"));
                else if(((String)request.getParameter("idFuncionario")).isEmpty())
                    invalido = true;

                if(!((String)request.getParameter("idFuncionario")).isEmpty())
                    end.setCodEndereco(Integer.parseInt((String)request.getParameter("codEndereco")));
                
                if (!((String)request.getParameter("cep")).isEmpty())
                    end.setCep((String)request.getParameter("cep"));
                else invalido = true;
                
                end.setEstado((String)request.getParameter("estado"));
                
                if (!((String)request.getParameter("cidade")).isEmpty())
                    end.setCidade((String)request.getParameter("cidade"));
                else invalido = true;
                
                if (!((String)request.getParameter("bairro")).isEmpty())
                    end.setBairro((String)request.getParameter("bairro"));
                else invalido = true;
                
                if (!((String)request.getParameter("rua")).isEmpty())
                    end.setRua((String)request.getParameter("rua"));
                else invalido = true;
                
                if (!((String)request.getParameter("numero")).isEmpty())
                    end.setNumero(Integer.parseInt((String)request.getParameter("numero")));
                else invalido = true;
                
                if (!((String)request.getParameter("complemento")).isEmpty())
                    end.setComplemento((String)request.getParameter("complemento"));
                else 
                    end.setComplemento(null);
                
                func.setEndereco(end);
                
                if (!((String)request.getParameter("cmbSetor")).isEmpty())
                    func.setSetor(setDAO.getSetorById(Integer.parseInt(request.getParameter("cmbSetor"))));
                else invalido = true;
                    
                if (!invalido){
                    session.removeAttribute("func");
                    FuncionarioDAO funcdao;
                    funcdao = new FuncionarioDAO();
                    if(((String)request.getParameter("idFuncionario")).isEmpty())
                        funcdao.addFuncionario(func);
                    else
                        funcdao.updFuncionario(func);

                    List<Funcionario> lista = newArrayList(); 
                    
                    lista = funcdao.getFuncuncionarioTodos(func.getCpf(),1); // fun.getEmpresa().getIdEmpresa()
                    session.setAttribute("listafunc", lista);
    //                request.setAttribute("mensagem", "Funcionário salvo com sucesso.");
    //                RequestDispatcher rd = getServletContext().getRequestDispatcher("/funcionario.jsp");
                    response.sendRedirect("funcionario.jsp");
                }
                else{
                    session.setAttribute("mensagem", "errofunc");
                    session.setAttribute("func", func);
    //                request.setAttribute("mensagem", "Funcionário salvo com sucesso.");
    //                RequestDispatcher rd = getServletContext().getRequestDispatcher("/funcionario.jsp");
                    response.sendRedirect("funcionario.jsp");
                }
            }
            
            else if ("editafuncionario".equals(action)) { //------------------- EDITA FUNCIONARIO ---------------------
                HttpSession session = request.getSession();
                session.removeAttribute("func");
                request.removeAttribute("func");
                session.removeAttribute("listafunc");
                request.removeAttribute("listafunc");
                FuncionarioDAO funcdao;
                funcdao = new FuncionarioDAO();
                int cod = Integer.parseInt((String)request.getParameter("idFuncionario"));
                Funcionario f = funcdao.getFuncionarioById(cod);
                request.setAttribute("func", f);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/funcionario.jsp");
                rd.forward(request, response);
//                HttpSession session = request.getSession();
//                session.setAttribute("func", f);
//                response.sendRedirect("funcionario.jsp");
            }
            
       
            
            else if ("realizalogin".equals(action)) { //----------------------- REALIZA LOGIN ---------------------
                FuncionarioDAO funcdao;
                funcdao = new FuncionarioDAO();
                HttpSession session = request.getSession();
                if(!(((String)request.getParameter("logemail")).isEmpty())&&(!((String)request.getParameter("logsenha")).isEmpty())){
                    Login login = new Login((String)request.getParameter("logemail"),(String)request.getParameter("logsenha"));
                    Login feito = funcdao.getLogin(login);
                    if ((feito.getNome() != null) && (feito.getAtivo()==1)){
                        
                        Funcionario fun = funcdao.getFuncionarioById(feito.getIdFuncionario());
                        session.setAttribute("funcionario", fun);
                        session.setAttribute("login", feito);
                        response.sendRedirect("funcionario.jsp");
                    }
                    else{
                        session.setAttribute("mensagem", "errologin");
                        response.sendRedirect("index.jsp");
                    }
                }
                else{
                    session.setAttribute("mensagem", "errologin");
                    response.sendRedirect("index.jsp");
                }
            }
            
            else if ("logout".equals(action)) { //----------------------------- REALIZA LOGOUT ---------------------
                HttpSession session = request.getSession(false);
                if (session!=null)
                    session.invalidate();
                response.sendRedirect("index.jsp");
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
            try {
                processRequest(request, response);
            } catch (RuntimeException ex) {
                Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            try {
                processRequest(request, response);
            } catch (RuntimeException ex) {
                Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
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
