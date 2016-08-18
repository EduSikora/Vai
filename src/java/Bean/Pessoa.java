package Bean;

import java.io.Serializable;

public class Pessoa implements Serializable {
    //Variáveis
    String nome, email, cpf, telefone;
    private Endereco endereco;
    
    //Construtores
    public Pessoa (){}
    
    public Pessoa (String nome, Endereco endereco, String email, String cpf, String telefone){
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
    }
    
    //Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEnderecoB() {
        return endereco;
    }

    public void setEnderecoB(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
