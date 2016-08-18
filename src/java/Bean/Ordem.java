package Bean;

import java.util.List;

public class Ordem {
    private int idOrdem;
    private List<OrdemProduto> listProdutos;
    private Empresa empresa;
    private Funcionario funcionario;
    
    public Ordem() {
    }

    public int getIdOrdem() {
        return idOrdem;
    }

    public void setIdOrdem(int idOrdem) {
        this.idOrdem = idOrdem;
    }

    public List<OrdemProduto> getListProdutos() {
        return listProdutos;
    }

    public void setListProdutos(List<OrdemProduto> listProdutos) {
        this.listProdutos = listProdutos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

}
