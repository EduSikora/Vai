package Bean;

import java.io.Serializable;

public class Funcionario extends Pessoa implements Serializable {
    //Variáveis
    private int idFuncionario;
    private boolean ativo;
    private String senha;
    private Endereco endereco;
    private Empresa empresa;
    private Setor setor;

    //Construtores
    public Funcionario() {
    }

    public Funcionario(String nome, Endereco endereco, String email, String cpf,
            String telefone, boolean ativo, String senha, Empresa empresa, Setor setor) {
        super(nome, endereco, email, cpf, telefone);
        this.endereco = endereco;
        this.ativo = ativo;
        this.senha = senha;
        this.empresa = empresa;
        this.setor = setor;
    }

    public Funcionario(int codFuncionario, String nome, Endereco endereco, String email, String cpf,
            String telefone, boolean ativo, String senha, Empresa empresa, Setor setor) {
        super(nome, endereco, email, cpf, telefone);
        this.idFuncionario = codFuncionario;
        this.endereco = endereco;
        this.ativo = ativo;
        this.senha = senha;
        this.empresa = empresa;
        this.setor = setor;
    }

    //Getters and Setters
    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    
     public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

}
