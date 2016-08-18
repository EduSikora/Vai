package Bean;
import java.util.Date;

public class EstoqueHistorico {
    private int idEstoqueHist;
    private Produto produto;
    private Funcionario funcionario;
    private Date data;
    private int qtd;
    private EstoqueAcao acao;
    private Fornecedor fornecedor;
    private Empresa empresa;
    
    public EstoqueHistorico() {
    }

    public EstoqueHistorico(int idEstoqueHist, Produto produto, Funcionario funcionario, Date data, int qtd, EstoqueAcao acao, Fornecedor fornecedor, Empresa empresa) {
        this.idEstoqueHist = idEstoqueHist;
        this.produto = produto;
        this.funcionario = funcionario;
        this.data = data;
        this.qtd = qtd;
        this.acao = acao;
        this.fornecedor = fornecedor;
        this.empresa = empresa;
    }

    public int getIdEstoqueHist() {
        return idEstoqueHist;
    }

    public void setIdEstoqueHist(int idEstoqueHist) {
        this.idEstoqueHist = idEstoqueHist;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public EstoqueAcao getAcao() {
        return acao;
    }

    public void setAcao(EstoqueAcao acao) {
        this.acao = acao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
