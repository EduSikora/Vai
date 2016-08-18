package Bean;

public class EstoqueAcao {
    private int idAcao;
    private String acao;
    private Empresa empresa;

    public EstoqueAcao() {
    }

    public EstoqueAcao(String acao) {
        this.acao = acao;
    }

    public EstoqueAcao(int idAcao, String acao) {
        this.idAcao = idAcao;
        this.acao = acao;
    }

    public EstoqueAcao(int idAcao, String acao, Empresa empresa) {
        this.idAcao = idAcao;
        this.acao = acao;
        this.empresa = empresa;
    }

    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
