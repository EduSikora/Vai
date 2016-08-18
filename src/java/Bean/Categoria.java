package Bean;

public class Categoria {
    private int idCategoria;
    private String nome;
    private int qtdServicos;
    private Empresa empresa;

    public Categoria() {
    }

    public Categoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Categoria(int idCategoria, String nome) {
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

    public Categoria(int idCategoria, String nome, int qtdServicos) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.qtdServicos = qtdServicos;
    }

    public Categoria(int idCategoria, String nome, int qtdServicos, Empresa empresa) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.qtdServicos = qtdServicos;
        this.empresa = empresa;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdServicos() {
        return qtdServicos;
    }

    public void setQtdServicos(int qtdServicos) {
        this.qtdServicos = qtdServicos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
}
