package Bean;

public class Setor {
    private int idSetor;
    private String nome;
    private int qtdFuncionarios;
    private Empresa empresa;

    public Setor() {
    }

    public Setor(int idSetor) {
        this.idSetor = idSetor;
    }

    public Setor(String nome) {
        this.nome = nome;
    }

    public Setor(int idSetor, String nome) {
        this.idSetor = idSetor;
        this.nome = nome;
    }

    public Setor(int idSetor, String nome, int qtdFuncionarios) {
        this.idSetor = idSetor;
        this.nome = nome;
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public Setor(int idSetor, String nome, int qtdFuncionarios, Empresa empresa) {
        this.idSetor = idSetor;
        this.nome = nome;
        this.qtdFuncionarios = qtdFuncionarios;
        this.empresa = empresa;
    }

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdFuncionarios() {
        return qtdFuncionarios;
    }

    public void setQtdFuncionarios(int qtdFuncionarios) {
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
}
