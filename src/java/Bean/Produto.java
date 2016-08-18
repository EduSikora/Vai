package Bean;

public class Produto {
    private int idProduto;
    private String nome;
    private double valor;
    private Marca marca;
    private Estoque estoque;
    private Empresa empresa;

    public Produto() {
    }
    
    public Produto(String nome) {
        this.nome = nome;
    }
    
    public Produto(int codProduto, String nome) {
        this.idProduto = codProduto;
        this.nome = nome;
    }

    public Produto(int idProduto, String nome, double valor, Marca marca) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.valor = valor;
        this.marca = marca;
    }

    public Produto(int idProduto, String nome, double valor, Marca marca, Estoque estoque) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.valor = valor;
        this.marca = marca;
        this.estoque = estoque;
    }

    public Produto(int idProduto, String nome, double valor, Marca marca, Estoque estoque, Empresa empresa) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.valor = valor;
        this.marca = marca;
        this.estoque = estoque;
        this.empresa = empresa;
    }
 
    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
      
    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
