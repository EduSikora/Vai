package Bean;

public class Marca {
    private int idMarca;
    private String nome;
    private int qtdProdutos;
    private Empresa empresa;

    public Marca() {
    }

    public Marca(int idMarca) {
        this.idMarca = idMarca;
    }

    public Marca(String nome) {
        this.nome = nome;
    }

    public Marca(int idMarca, String nome) {
        this.idMarca = idMarca;
        this.nome = nome;
    }

    public Marca(int idMarca, String nome, int qtdProdutos) {
        this.idMarca = idMarca;
        this.nome = nome;
        this.qtdProdutos = qtdProdutos;
    }

    public Marca(int idMarca, String nome, int qtdProdutos, Empresa empresa) {
        this.idMarca = idMarca;
        this.nome = nome;
        this.qtdProdutos = qtdProdutos;
        this.empresa = empresa;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdProdutos() {
        return qtdProdutos;
    }

    public void setQtdProdutos(int qtdProdutos) {
        this.qtdProdutos = qtdProdutos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
}
