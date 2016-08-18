    DROP TABLE ORDEM_PRODUTO;
    DROP TABLE ORDEM;
    DROP TABLE ESTOQUE_HIST;
    DROP TABLE ESTOQUE_ACAO;
    DROP TABLE ESTOQUE;
    DROP TABLE PRODUTO;
    DROP TABLE FORNECEDOR;
    DROP TABLE MARCA;
    DROP TABLE FUNCIONARIO;
    DROP TABLE SERVICO;
    DROP TABLE CATEGORIA;
    DROP TABLE SETOR;
    DROP TABLE ENDERECO;
    DROP TABLE EMPRESA;
    DROP TABLE IMPACTO;

    CREATE TABLE EMPRESA(
         ID_EMPRESA INT AUTO_INCREMENT
        ,ST_NOMEEMP VARCHAR(100)
        ,ST_CNPJ VARCHAR(100)
        ,CONSTRAINT pkEmpresa PRIMARY KEY (ID_EMPRESA)
    );

    CREATE TABLE ENDERECO (
         ID_ENDERECO INT(11) AUTO_INCREMENT
        ,ST_ESTADO VARCHAR(15)
        ,ST_CIDADE VARCHAR(25)
        ,ST_BAIRRO VARCHAR(25)
        ,ST_CEP VARCHAR(9)
        ,ST_RUA VARCHAR(100)
        ,NB_NUMERO INT(11)
        ,ST_COMPLEMENTO VARCHAR(25)
        ,CONSTRAINT pkEndereco PRIMARY KEY (ID_ENDERECO)
    ); 

    CREATE TABLE SERVICO(
        ID_SERVICO INT AUTO_INCREMENT
       ,ID_EMPRESA INT
       ,ST_NOME VARCHAR(50)
       ,ST_DESC VARCHAR(200)
       ,CONSTRAINT pkServico PRIMARY KEY (ID_SERVICO)
       ,CONSTRAINT fkServicoEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
    );

    CREATE TABLE SETOR(
         ID_EMPRESA INT
        ,ID_SETOR INT AUTO_INCREMENT
        ,ST_NOME VARCHAR(50)
        ,CONSTRAINT pkSetor PRIMARY KEY (ID_SETOR)
        ,CONSTRAINT fkSetorEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukSetor UNIQUE(ID_EMPRESA, ST_NOME)
    );

    CREATE TABLE CATEGORIA(
         ID_EMPRESA INT
        ,ID_CATEGORIA INT AUTO_INCREMENT
        ,ST_NOME VARCHAR(100)
        ,CONSTRAINT pkCategoria PRIMARY KEY (ID_CATEGORIA)
        ,CONSTRAINT fkCategoriaEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukCategoria UNIQUE(ID_EMPRESA, ST_NOME)
    );

    CREATE TABLE FUNCIONARIO(
         ID_FUNCIONARIO INT(11) AUTO_INCREMENT
        ,ID_EMPRESA INT(11)
        ,ID_SETOR INT(11)
        ,ST_NOME VARCHAR(150)
        ,ST_CPF VARCHAR(14)
        ,ST_TELEFONE VARCHAR(13)
        ,ST_EMAIL VARCHAR(25)
        ,ST_SENHA VARCHAR(50)
        ,ID_ENDERECO INT(11)
        ,NB_ATIVO INT(11)
        ,CONSTRAINT pkFuncionario PRIMARY KEY (ID_FUNCIONARIO)
        ,CONSTRAINT fkFuncionarioEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT fkFunciEndereco FOREIGN KEY (ID_ENDERECO) REFERENCES ENDERECO (ID_ENDERECO)
        ,CONSTRAINT fkFunciSetor FOREIGN KEY (ID_SETOR) REFERENCES SETOR (ID_SETOR)
    );

    CREATE TABLE MARCA(
         ID_EMPRESA INT
        ,ID_MARCA INT AUTO_INCREMENT
        ,ST_NOME VARCHAR(50)
        ,CONSTRAINT pkMarca PRIMARY KEY (ID_MARCA)
        ,CONSTRAINT fkMarcaEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukMarca UNIQUE(ID_EMPRESA, ST_NOME)
    );

    CREATE TABLE FORNECEDOR(
         ID_EMPRESA INT
        ,ID_FORNECEDOR INT AUTO_INCREMENT
        ,ST_NOME VARCHAR(50)
        ,CONSTRAINT pkFornecedor PRIMARY KEY (ID_FORNECEDOR)
        ,CONSTRAINT fkFornecedorEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukFornecedor UNIQUE(ID_EMPRESA, ST_NOME)
    );

    CREATE TABLE PRODUTO(
         ID_EMPRESA INT
        ,ID_PRODUTO INT AUTO_INCREMENT
        ,ST_NOME VARCHAR(50)
        ,NB_VALOR DECIMAL(6,2)
        ,ID_MARCA INT
        ,CONSTRAINT pkProduto PRIMARY KEY (ID_PRODUTO)
        ,CONSTRAINT fkProdMarca FOREIGN KEY (ID_MARCA) REFERENCES MARCA (ID_MARCA)
        ,CONSTRAINT fkProdutoEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukProduto UNIQUE(ID_EMPRESA, ST_NOME, ID_MARCA)
    );

    CREATE TABLE ORDEM(
         ID_EMPRESA INT
        ,ID_ORDEM INT AUTO_INCREMENT
        ,ST_NOME VARCHAR(100)
        ,ID_FUNCIONARIO INT
        ,CONSTRAINT pkOrdem PRIMARY KEY (ID_ORDEM)
        ,CONSTRAINT fkOrdemEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT fkOrdemFuncionario FOREIGN KEY (ID_FUNCIONARIO) REFERENCES FUNCIONARIO (ID_FUNCIONARIO)
    );

    CREATE TABLE ORDEM_PRODUTO(
         ID_ORDEM INT
        ,ID_PRODUTO INT
        ,NB_QTD INT
        ,CONSTRAINT pkOrdemProd PRIMARY KEY (ID_ORDEM, ID_PRODUTO)
        ,CONSTRAINT fkOrdemOrdem FOREIGN KEY (ID_ORDEM) REFERENCES ORDEM (ID_ORDEM)
        ,CONSTRAINT fkOrdemProduto FOREIGN KEY (ID_PRODUTO) REFERENCES PRODUTO (ID_PRODUTO)
    );

    CREATE TABLE ESTOQUE(
         ID_PRODUTO INT
        ,NB_QTD INT
        ,CONSTRAINT pkEstoque PRIMARY KEY (ID_PRODUTO)
        ,CONSTRAINT fkEstoqueProduto FOREIGN KEY (ID_PRODUTO) REFERENCES PRODUTO (ID_PRODUTO)
    );

    CREATE TABLE ESTOQUE_ACAO(
         ID_EMPRESA INT
        ,ID_ACAO INT AUTO_INCREMENT
        ,ST_ACAO VARCHAR(30)
        ,CONSTRAINT pkEstoqueAcao PRIMARY KEY (ID_ACAO)
        ,CONSTRAINT fkEstoqueAcaoEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukEstoqueAcao UNIQUE(ID_EMPRESA, ST_ACAO)
    );

    CREATE TABLE ESTOQUE_HIST(
         ID_EMPRESA INT
        ,ID_ESTOQUE_HIST INT AUTO_INCREMENT
        ,ID_PRODUTO INT
        ,ID_FUNCIONARIO INT
        ,ID_FORNECEDOR INT
        ,ID_ACAO INT
        ,NB_QTD INT
        ,DT_DATA DATETIME
        ,CONSTRAINT pkEstoqueHistorico PRIMARY KEY (ID_ESTOQUE_HIST)
        ,CONSTRAINT fkEstoqueHistProduto FOREIGN KEY (ID_PRODUTO) REFERENCES PRODUTO (ID_PRODUTO)
        ,CONSTRAINT fkEstoqueHistFuncionario FOREIGN KEY (ID_FUNCIONARIO) REFERENCES FUNCIONARIO (ID_FUNCIONARIO)
        ,CONSTRAINT fkEstoqueHistFornecedor FOREIGN KEY (ID_FORNECEDOR) REFERENCES FORNECEDOR (ID_FORNECEDOR)
        ,CONSTRAINT fkEstoqueHistAcao FOREIGN KEY (ID_ACAO) REFERENCES ESTOQUE_ACAO (ID_ACAO)
        ,CONSTRAINT fkEstoqueHistEmpresa FOREIGN KEY (ID_EMPRESA) REFERENCES EMPRESA (ID_EMPRESA)
        ,CONSTRAINT ukEstoqueHistorico UNIQUE(ID_EMPRESA, ID_PRODUTO, ID_FUNCIONARIO, ID_FORNECEDOR, ID_ACAO, DT_DATA)
    );

    CREATE TABLE IMPACTO(
         ID_IMPACTO INT
        ,ST_NOME VARCHAR(30)
        ,ID_EMPRESA INT
        ,CONSTRAINT pkImpacto PRIMARY KEY (ID_IMPACTO)
        ,CONSTRAINT fkImpactoEmpresa FOREIGN KEY (ID_IMPACTO) REFERENCES EMPRESA (ID_EMPRESA)
    );

    INSERT INTO `empresa` (`ID_EMPRESA`, `ST_NOMEEMP`, `ST_CNPJ`) VALUES
        (1, 'Teste1', '123456789'),
        (2, 'Teste2', '123456782'),
        (3, 'Teste3', '123456754')
    ;

    INSERT INTO `endereco` (`ID_ENDERECO`, `ST_ESTADO`, `ST_CIDADE`, `ST_BAIRRO`, `ST_CEP`, `ST_RUA`, `NB_NUMERO`, `ST_COMPLEMENTO`) VALUES
        (1, 'PR', 'Curitiba', 'Centro', '81850241', 'Teste blabla', 123, 'mais blabla'),
        (2, 'PR', 'Curitiba', 'Xaxim', '81830270', 'teste', 501, ''),
        (3, 'PR', 'Curitiba', 'Xaxim', '81830250', 'teste2', 1100, '')
    ;

    INSERT INTO SETOR (ID_EMPRESA, ST_NOME) VALUES 
        (1, 'Setor01')
       ,(1, 'Setor02')
       ,(1, 'Setor03')
       ,(1, 'Setor04')
       ,(1, 'Setor05')
       ,(2, 'Setor06')
       ,(2, 'Setor07')
       ,(2, 'Setor08')
       ,(2, 'Setor09')
       ,(2, 'Setor10')
    ;

    INSERT INTO `funcionario` (`ID_FUNCIONARIO`, `ID_EMPRESA`, `ST_NOME`, `ST_CPF`, `ST_TELEFONE`, `ST_EMAIL`, `ST_SENHA`, `ID_ENDERECO`, `NB_ATIVO`,`ID_SETOR`) VALUES
        (1, 1, 'Teste1', '09915767994', '4192929292', 'test@teste.com', '123', 1, 1, 1),
        (2, 1, 'edu', '05753048986', '41959595', 'edu@edu.com', '123', 2, 1, 1),
        (3, 1, 'edu1', '05753048986', '419595951', 'edu@edu.com1', '1231', 3, 1, 2)
    ;

    INSERT INTO MARCA (ID_EMPRESA, ST_NOME) VALUES 
        (1, 'Marca01')
       ,(1, 'Marca02')
       ,(1, 'Marca03')
       ,(1, 'Marca04')
       ,(1, 'Marca05')
       ,(2, 'Marca06')
       ,(2, 'Marca07')
       ,(2, 'Marca08')
       ,(2, 'Marca09')
       ,(2, 'Marca10')
    ;

    INSERT INTO SERVICO (ID_EMPRESA, ST_NOME, ST_DESC) VALUES
        (1, 'Formatação de computador', 'Formatar e reinstalar Windows')
       ,(1, 'Instalação de telefone', 'Passar cabos e ativar o ramal')
    ;

    INSERT INTO CATEGORIA (ID_EMPRESA, ST_NOME) VALUES 
        (1, 'Categoria01')
       ,(1, 'Categoria02')
       ,(1, 'Categoria03')
       ,(1, 'Categoria04')
       ,(1, 'Categoria05')
       ,(2, 'Categoria06')
       ,(2, 'Categoria07')
       ,(2, 'Categoria08')
       ,(2, 'Categoria09')
       ,(2, 'Categoria10')
    ;

    INSERT INTO FORNECEDOR (ID_EMPRESA, ST_NOME) VALUES 
        (1, 'Fornecedor01')
       ,(1, 'Fornecedor02')
       ,(1, 'Fornecedor03')
       ,(1, 'Fornecedor04')
       ,(1, 'Fornecedor05')
       ,(2, 'Fornecedor06')
       ,(2, 'Fornecedor07')
       ,(2, 'Fornecedor08')
       ,(2, 'Fornecedor09')
       ,(2, 'Fornecedor10')
    ;

    INSERT INTO PRODUTO (ID_EMPRESA, ST_NOME, NB_VALOR, ID_MARCA) VALUES 
        (1, 'Produto01', 10.50, 1)
       ,(1, 'Produto02', 5.40, 1)
       ,(1, 'Produto03', 150.00, 3)
       ,(1, 'Produto04', 4.99, 4)
       ,(1, 'Produto05', 315.55, 2)
       ,(2, 'Produto06', 1800.50, 6)
       ,(2, 'Produto07', 13.50, 6)
       ,(2, 'Produto08', 12.50, 7)
       ,(2, 'Produto09', 187.50, 9)
       ,(2, 'Produto10', 158.00, 8)
    ;

    INSERT INTO ORDEM (ID_EMPRESA, ST_NOME, ID_FUNCIONARIO) VALUES 
        (1, 'Ordem01', 1)
       ,(1, 'Ordem02', 2)
       ,(1, 'Ordem03', 1)
    ;   

    INSERT INTO ORDEM_PRODUTO (ID_ORDEM, ID_PRODUTO, NB_QTD) VALUES 
        (1, 1, 10)
       ,(1, 2, 51)
    ;
    INSERT INTO ESTOQUE (ID_PRODUTO, NB_QTD) VALUES 
        (1,200)
        ,(2,0)
        ,(3,250)
        ,(4,0)
        ,(5,50)
        ,(6,300)
        ,(7,30)
        ,(8,10)
        ,(9,5)
        ,(10,3)
    ;

    INSERT INTO ESTOQUE_ACAO (ID_EMPRESA, ST_ACAO) VALUES 
        (1, 'INCLUSAO')
       ,(1, 'EXCLUSAO')
       ,(2, 'INCLUSAO')
       ,(2, 'EXCLUSAO')
    ;

    INSERT INTO ESTOQUE_HIST (ID_EMPRESA, ID_PRODUTO, ID_FUNCIONARIO, ID_FORNECEDOR, ID_ACAO, NB_QTD, DT_DATA) VALUES
        (1, 1, 1, 1, 1, 10, '2014-02-28 08:14:57')
       ,(1, 2, 2, 2, 1, 160, '2016-04-10 14:32:55')
       ,(1, 1, 1, 3, 1, 360, '2016-04-11 17:32:55')
       ,(2, 7, 3, 8, 1, 160, '2016-04-12 18:32:55')
       ,(2, 6, 3, 7, 1, 860, '2016-04-13 15:32:55')
       ,(2, 9, 3, 9, 1, 560, '2016-04-14 13:32:55')
    ;

    INSERT INTO IMPACTO (ID_IMPACTO, ST_NOME) VALUES
        (1, 'Formatação de computador')
       ,(1, 'Instalação de telefone')
    ;