package Bean;

public class Fornecedor {
    private int idFornecedor;
    private String nome;
    private Empresa empresa;

    public Fornecedor() {
    }

    public Fornecedor(String nome) {
        this.nome = nome;
    }

    public Fornecedor(int idFornecedor, String nome) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
    }

    public Fornecedor(int idFornecedor, String nome, Empresa empresa) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.empresa = empresa;
    }
    
    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
