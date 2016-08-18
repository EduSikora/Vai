package Bean;

import java.io.Serializable;

public class Empresa implements Serializable {
    private int idEmpresa;
    private String nome, cnpj;

    public Empresa(){
    }
    
    public Empresa(int id, String nome, String cnpj) {
        this.idEmpresa = id;
        this.nome = nome;
        this.cnpj = cnpj;
    }
    
    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    
    
}
