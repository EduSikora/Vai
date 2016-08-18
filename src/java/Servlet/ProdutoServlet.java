package Servlet;

import Bean.Funcionario;
import Bean.Marca;
import Bean.Produto;
import DAO.EstoqueDAO;
import DAO.FuncionarioDAO;
import DAO.MarcaDAO;
import DAO.ProdutoDAO;
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

@WebServlet(name = "ProdutoServlet", urlPatterns = {"/ProdutoServlet"})
public class ProdutoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        session.removeAttribute("msgErro");
        
        FuncionarioDAO daoFunc = new FuncionarioDAO();
        Funcionario func = daoFunc.getFuncionarioById(1);
        session.setAttribute("funcionario", func);
        
        //Produtos #######################################################################################
        if ("listProdutos".equals(action)) {
            ProdutoDAO daoprod = new ProdutoDAO();
            List<Produto> lista = new ArrayList<Produto>();
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            lista = daoprod.getProdutoTodos(f.getEmpresa());
            session.setAttribute("modoProduto", "inclusao");
            request.setAttribute("Produtos", lista);
            
            //preenche o combo *************************************** trocar de lugar e chamar na inicializacao da pagina
            MarcaDAO daoMarc = new MarcaDAO();
            List<Marca> listaForn = new ArrayList<Marca>();
            listaForn = daoMarc.getMarcaTodos(f.getEmpresa());
            session.setAttribute("cmbMarcas",listaForn);
            
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/produto.jsp");
            rd.forward(request, response);
        }
        
        if ("updProduto".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("del")!= null){
                ProdutoDAO daoprod = new ProdutoDAO();
                EstoqueDAO daoestoq = new EstoqueDAO();
                Produto prod = daoprod.getProdutoById(Integer.parseInt(request.getParameter("del")));
                //Verifica se possui produto estoque
                if (daoestoq.getEstoqueByProduto(prod).getQtd()>0){
                    session.setAttribute("msgErro", "produtoPossuiEstoque");
                }else{
                    daoprod.delProduto(prod);
                    session.removeAttribute("msgErro");
                }
                daoprod = new ProdutoDAO();
                List<Produto> lista = new ArrayList<Produto>();
                lista = daoprod.getProdutoTodos(f.getEmpresa());

                session.setAttribute("modoProduto", "inclusao");
                session.setAttribute("Produtos", lista);
                response.sendRedirect("produto.jsp");
            }
            if ((String)request.getParameter("edit")!= null){
                ProdutoDAO daoprod = new ProdutoDAO();
                Produto prod = daoprod.getProdutoById(Integer.parseInt(request.getParameter("edit")));
                //Verifica se possui produto ja criado
                session.removeAttribute("msgErro");
                session.setAttribute("modoProduto", "edicao");
                session.setAttribute("Produto", prod);
                response.sendRedirect("produto.jsp");
            }
        }
        
        if ("saveProduto".equals(action)) {
            Funcionario f = (Funcionario)session.getAttribute("funcionario");
            if ((String)request.getParameter("addSave")!= null) {
                ProdutoDAO daoprod = new ProdutoDAO();
                Produto prod = new Produto();
                prod.setNome((String)request.getParameter("produtoParamNome"));
                prod.setValor(Double.parseDouble(request.getParameter("produtoParamValor")));
                
                MarcaDAO daoMarc = new MarcaDAO();
                prod.setMarca(daoMarc.getMarcaById(Integer.parseInt(request.getParameter("cmbMarca"))));
                
                prod.setEmpresa(f.getEmpresa());
                
                if ((daoprod.checkExisteByNomeEMarca(prod, prod.getEmpresa()))== false){
                    daoprod.addProduto(prod);
                    daoprod = new ProdutoDAO();
                    
                    List<Produto> lista = new ArrayList<Produto>();
                    lista = daoprod.getProdutoTodos(f.getEmpresa());
                
                    session.setAttribute("modoProduto", "inclusao");
                    session.setAttribute("Produtos", lista);
                    session.removeAttribute("msgErro");
                }else{
                    session.setAttribute("msgErro", "produtoEMarcaJaExiste");
                }
                response.sendRedirect("produto.jsp");
            }
            if ((String)request.getParameter("updSave")!= null){
                ProdutoDAO daoProd = new ProdutoDAO();
                Produto prodOld = daoProd.getProdutoById(Integer.parseInt(request.getParameter("updSave")));
                
                Produto prodNew = new Produto();
                prodNew.setNome((String)request.getParameter("produtoParamNome"));
                prodNew.setValor(Double.parseDouble(request.getParameter("produtoParamValor")));
                prodNew.setIdProduto(prodOld.getIdProduto());
                
                MarcaDAO daoMarc = new MarcaDAO();
                prodNew.setMarca(daoMarc.getMarcaById(Integer.parseInt(request.getParameter("cmbMarca"))));
                prodNew.setEmpresa(prodOld.getEmpresa());
                
                //Alteracao somente do valor
                if(prodOld.getNome().equals(prodNew.getNome()) || prodOld.getMarca().getNome().equals(prodNew.getMarca().getNome())){
                    daoProd.updProduto(prodNew);
                    session.removeAttribute("msgErro");
                    
                    List<Produto> lista = new ArrayList<Produto>();
                    lista = daoProd.getProdutoTodos(f.getEmpresa());
                    session.setAttribute("modoProduto", "inclusao");
                    session.setAttribute("Produtos", lista);
                }else{//Com alteracao de nome ou marca
                    //Verifica se possui produto ja criado
                    if ((daoProd.checkExisteByNomeEMarca(prodNew, prodNew.getEmpresa()))== false){
                    daoProd.updProduto(prodNew);
                    session.removeAttribute("msgErro");

                    List<Produto> lista = new ArrayList<Produto>();
                    lista = daoProd.getProdutoTodos(f.getEmpresa());
                    session.setAttribute("modoProduto", "inclusao");
                    session.setAttribute("Produtos", lista);
                }else{
                    session.setAttribute("msgErro", "produtoEMarcaJaExiste");
                }
                }
                response.sendRedirect("produto.jsp");
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
            Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProdutoServlet.class.getName()).log(Level.SEVERE, null, ex);
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
