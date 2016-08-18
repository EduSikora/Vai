package Bean;

import java.io.Serializable;

public class Servico {
    private int idServico;
    private Empresa empresa;
    private String nome;
    private String desc;

    public Servico() {
    }

    public Servico(int idServico) {
        this.idServico = idServico;
    }

    public Servico(int idServico, Empresa empresa) {
        this.idServico = idServico;
        this.empresa = empresa;
    }

    public Servico(int idServico, Empresa empresa, String nome) {
        this.idServico = idServico;
        this.empresa = empresa;
        this.nome = nome;
    }

    public Servico(int idServico, Empresa empresa, String nome, String desc) {
        this.idServico = idServico;
        this.empresa = empresa;
        this.nome = nome;
        this.desc = desc;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    
}
