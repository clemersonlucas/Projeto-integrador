package model;

import controller.Consulta;
import controller.Estoque;
import controller.Evento;
import controller.Funcionario;
import controller.Medicamento;
import controller.Paciente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Conexao {
    
    public static String user = "novousuario";
    public static String password = "password";
    public static String nameClass = "com.mysql.jdbc.Driver";
    public static String url = "jdbc:mysql://localhost/Server";
    
    public static boolean result;
    public static ResultSet resultSet = null;
    public static Statement statement = null;
    public static Connection connection = null;
    
    // método responsável por estabelecer a conexao
    public static void fazerConexao (String comando){
        try {
           Class.forName(nameClass);
           connection = DriverManager.getConnection(url, user, password);
           statement = connection.createStatement();
           result = statement.execute(comando);
           resultSet = statement.getResultSet();     
           
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    // método responsável por pegar todos os eventos presentes no banco
    public static ArrayList<Evento> pegarTodosEventos (){
        ArrayList<Evento> listaRetorno = new ArrayList<>();
        String sql = "SELECT * FROM Evento;";
        fazerConexao(sql);
        
        try {
            while (resultSet.next()){
                String titulo = resultSet.getString("titulo");
                String informacoes = resultSet.getString("informacoes");
                String dataPublicacao = resultSet.getString("dataExpirar");
                int id = resultSet.getInt("id");
                String matriculaProfissional = resultSet.getString("matriculaProfissional");
                
                Evento e = new Evento(titulo, dataPublicacao, dataPublicacao, 
                        informacoes, matriculaProfissional, id);

                listaRetorno.add(e);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException ex) {
        }
        return listaRetorno;
    }
    
    // método responsável por cadastrar um funcionario no banco
    public static void cadastraFuncionario (String nome, String tipoAcesso, String funcao, 
            String senha, String matricula) throws SQLException{
        
        String sql = "INSERT INTO Profissional VALUES ('" + nome + "','" + tipoAcesso + "','" + funcao + "','" 
                + senha + "','" + matricula + "');";
        fazerConexao(sql);
        
        statement.close();
        connection.close();
    }
    
    // método responsável por adicionar um paciente no banco
    public static void adicionaPaciente(Paciente paciente) {
        String sql = "INSERT INTO Paciente VALUES ('" + paciente.getSexo() + "','" + paciente.getEtinia() + "','"
                + paciente.getSenha() + "','" + paciente.getEstadoCivil() + "','" + paciente.getRg() + "','"
                + paciente.getOrgaoEmissor() + "','" + paciente.getDataExpedicao() + "','" + paciente.getUf() + "','"
                + "" + "','" + paciente.getNomePai()+ "','" + paciente.getNomeMae() + "','" + paciente.getNome() + "','"
                + paciente.getSus() + "','" + "" + "','" + paciente.getNacionalidade() + "','" + paciente.getNaturalidade() + "','"
                + paciente.getNascimento() + "','" + paciente.getCpf() + "');";
                
        fazerConexao(sql);
    
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
      
    // método responsável por realizar o login com o usuário
    public static Funcionario fazerLogin (String matricula, String senha) throws SQLException{
        String sql = "SELECT * FROM Profissional;";
        
        fazerConexao(sql);
        
        while (resultSet.next()){
             String s = resultSet.getString("senha");
             String m = resultSet.getString("matricula");

             if (s.equals(senha) && m.equals(matricula)){
                 Funcionario funcionario = new Funcionario(resultSet.getString("nome"), 
                     senha, matricula, resultSet.getString("tipoAcesso"), resultSet.getString("funcao"));

                 return funcionario;
             }
        }

        resultSet.close();
        statement.close();
        connection.close();
           
        return null;
    }
    
    // cria uma nova matricula tendo base as ja existentes
    public static String novaMatricula () throws SQLException{
        String sql = "SELECT * FROM Profissional;"; 
        fazerConexao(sql);
        
        boolean sairLoop = true;
        while (sairLoop == true){
            String novaMatricula = Integer.toString((int)(Math.random() * 100000));
            if (novaMatricula.length() == 5){
                while (resultSet.next()){
                    String m = resultSet.getString("matricula");

                    if (novaMatricula.equals(m)){
                        break;
                    }
                }  

                if (sairLoop == true){
                    return novaMatricula;
                }
            }
        }
        resultSet.close();
        statement.close();
        connection.close();

        return null;
    }

   
    // método responsável por deletar um evento ja existente    
    public static void deletarEvento (int id) throws SQLException{
        String sql = "DELETE FROM Evento WHERE id = '"+ id +"';"; 
        fazerConexao(sql);
        
        statement.close();
        connection.close();
    } 
    
    // método responsável por adicionar um novo evento
    public static void adicionaEvento (Evento evento) throws SQLException{
        String sql = "INSERT INTO Evento VALUES ('"
                + evento.getTitulo() + "','"
                + evento.getInformacao() + "','" 
                + evento.getDataPublicao() + "','"
                + evento.getDataExpirar() + "','"
                + evento.getId() + "','"
                + evento.getMatriculaProfissional() +
                "');";
                
        fazerConexao(sql);
        
        statement.close();
        connection.close();
    } 

    // cria uma nova matricula tendo base as ja existentes
    public static String novoIdMedicamento (){
        try {
            String sql = "SELECT * FROM Medicamento;";
            fazerConexao(sql);
            
            boolean sairLoop = true;
            while (sairLoop == true){
                String novaMatricula = Integer.toString((int)(Math.random() * 100000));
                if (novaMatricula.length() == 5){
                    try {
                        while (resultSet.next()){
                            String m = resultSet.getString("id");
                            
                            if (novaMatricula.equals(m)){
                                break;
                            }
                        }
                    } catch (SQLException ex) {
                        System.err.println(ex.getMessage());
                    }
                    
                    if (sairLoop == true){
                        return novaMatricula;
                    }
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }
        
    // método responsável por criar um novo id para  
    public static int novoIdEvento () throws SQLException{
        String sql = "SELECT * FROM Evento;"; 
        fazerConexao(sql);
        
        boolean sairLoop = true;
        while (sairLoop == true){
            String novaMatricula = Integer.toString((int)(Math.random() * 100000));
            if (novaMatricula.length() == 5){
                while (resultSet.next()){
                    String m = resultSet.getString("id");

                    if (novaMatricula.equals(m)){
                        break;
                    }
                }  

                if (sairLoop == true){
                    return Integer.parseInt(novaMatricula);
                }
            }
        }
        resultSet.close();
        statement.close();
        connection.close();

        return 0;
    }    

    // método responsável por criar um novo id para  
    public static int novoIdConsulta () throws SQLException{
        String sql = "SELECT * FROM Consulta;"; 
        fazerConexao(sql);
        
        boolean sairLoop = true;
        while (sairLoop == true){
            String novaMatricula = Integer.toString((int)(Math.random() * 100000));
            if (novaMatricula.length() == 5){
                while (resultSet.next()){
                    String m = resultSet.getString("id");

                    if (novaMatricula.equals(m)){
                        break;
                    }
                }  

                if (sairLoop == true){
                    return Integer.parseInt(novaMatricula);
                }
            }
        }
        resultSet.close();
        statement.close();
        connection.close();

        return 0;
    }    
    
    // verifica se o cpf ja esta no banco
    public static boolean verificaCpf (String cpf){
        fazerConexao("SELECT * FROM Paciente");
    
        try {
            while (resultSet.next()){
                String cpfExiste = resultSet.getString("cpf");
                if (cpfExiste.equals(cpf)){
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        return false;
    }
    
    // método responsável por adicionar uma consulta ao banco 
    public static void adicionaConsulta (Consulta consulta){
        String sql = "INSERT INTO Consulta VALUES ('" + consulta.getData()
                + "','"  + consulta.getDescricao()
                + "','"  + consulta.getDiagnostico()
                + "','"  + consulta.getHora()
                + "','"  + consulta.getStatus()
                + "','"  + consulta.getInicioConsulta()
                + "','"  + consulta.getFimConsulta()
                + "','"  + consulta.getEntrada()
                + "','"  + consulta.getEspecialidade()
                + "','"  + consulta.getId()
                + "','"  + consulta.getQuemAgendou()
                + "','"  + consulta.getParaPaciente()
                + "');";
     
        fazerConexao(sql);
        
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    // método responsável por pegar todas as consultas agendadas
    public static ArrayList<Consulta> pegarTodasConsultas() {
        String sql = "SELECT * FROM Consulta";
        fazerConexao(sql);
        
        ArrayList<Consulta> listaRetorno = new ArrayList<>();
        
        try {
            while (resultSet.next()){
                // vamos verificar se esta aguardando avaliação
                String status = resultSet.getString("status");
                
                if (status.equalsIgnoreCase("Aguardando avaliação")){
                    String data = resultSet.getString("data");
                    String descricao = resultSet.getString("descricao");
                    String hora = resultSet.getString("hora");
                    String inicioConsulta = resultSet.getString("inicioConsulta");
                    String fimConsulta = resultSet.getString("fimConsulta");
                    String entrada = resultSet.getString("entrada");
                    String especialidade = resultSet.getString("especialidade");
                    String matriculaProfissional = resultSet.getString("matriculaProfissional");
                    String cpfPaciente = resultSet.getString("cpfPaciente");
                    int id = resultSet.getInt("id");

                    Consulta c = new Consulta(data, descricao, hora, descricao, 
                            status, inicioConsulta, fimConsulta, entrada, especialidade, 
                            id, matriculaProfissional, cpfPaciente);
                    listaRetorno.add(c);
                }
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return listaRetorno;
    }

    // método responsável por retornar o nome do paciente
    public static String descobriNomePaciente (String cpf){
        String sql = "SELECT nome FROM Paciente WHERE cpf = '" + cpf + "';";
        fazerConexao(sql);
        String s = null;
        try {
            while (resultSet.next()){
                String vetor [] = resultSet.getString("nome").split(" ");
                s = vetor[0];
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }    
        return s;
    }

    // método responsável por retornar o nome do paciente
    public static String[] descobriNomeProfissional (String matricula){
        String sql = "SELECT nome, funcao FROM Profissional WHERE matricula = '" + matricula + "';";
        fazerConexao(sql);
        String s [] = new String[2];
        
        try {
            while (resultSet.next()){
                String vetor [] = resultSet.getString("nome").split(" ");
                String profissao = resultSet.getString("funcao");
                s [0] = vetor[0];
                s [1] = profissao;
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
        
        return s;
    }

    // método responsável por deferir consulta 
    public static void deferirConsulta (int id){
        String sql = "UPDATE Consulta SET status = 'Deferido' WHERE id = '" + id + "';";
        fazerConexao(sql);
        
        try {
            statement.close();
            connection.close();
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    // método responsável por deferir consulta 
    public static void indeferirConsulta (int id){
        String sql = "UPDATE Consulta SET status = 'Indeferido' WHERE id = '" + id + "';";
        fazerConexao(sql);
        
        try {
            statement.close();
            connection.close();
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    // vamos pegar as consultas ja agendadas para um determinado especialista
    public static ArrayList<Consulta> ConsultasOk (String especialidade){
        ArrayList<Consulta> retorno = new ArrayList<>();
        
        String sql = "SELECT * FROM Consulta WHERE status = 'Deferido' "
                + "AND especialidade = '" + especialidade + "';";
        
        fazerConexao(sql);
        
        try {
            while (resultSet.next()){
                String status = resultSet.getString("status");
                String data = resultSet.getString("data");
                String descricao = resultSet.getString("descricao");
                String hora = resultSet.getString("hora");
                String inicioConsulta = resultSet.getString("inicioConsulta");
                String fimConsulta = resultSet.getString("fimConsulta");
                String entrada = resultSet.getString("entrada");
                String matriculaProfissional = resultSet.getString("matriculaProfissional");
                String cpfPaciente = resultSet.getString("cpfPaciente");
                int id = resultSet.getInt("id");

                Consulta c = new Consulta(data, descricao, hora, descricao, 
                        status, inicioConsulta, fimConsulta, entrada, especialidade, 
                        id, matriculaProfissional, cpfPaciente);
                retorno.add(c);

            
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
 
        return retorno;
    }

    // responsavel por consultar apenas um paciente
    public static void consultarUmPacinte(String descricao, String diagnostico, String dataInicio, String dataFim, int id) {
        String sql = "UPDATE Consulta SET descricao = '" + descricao + "', diagnostico = '" + diagnostico 
                + "', inicioConsulta = '" + dataInicio + "', fimConsulta = '" + dataFim + "' WHERE id =  '" + id + "';";
        fazerConexao(sql);
        
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    // vamos pegar todos os medicamentos do estoque
    public static ArrayList<Estoque> pegarMedicamentoEstoque (){
        ArrayList<Estoque> listaRetorno = new ArrayList<>();
        
        String sql = "SELECT * FROM Estoque";
        fazerConexao(sql);
        
        try{
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String validade = resultSet.getString("validade");
                int quantidade = resultSet.getInt("quantidade");
                String descricao = resultSet.getString("descricao");
                String fabricacao = resultSet.getString("fabricacao");
                int id = resultSet.getInt("id");
                String matriculaProfissional = resultSet.getString("matriculaProfissional");
                listaRetorno.add(new Estoque(nome, validade, quantidade, descricao, fabricacao, id, matriculaProfissional));
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        return listaRetorno;
    }

    public static void updateQuantidadeMedicamento(int idEstoque, int quantidade) {
        String sql = "UPDATE Estoque SET quantidade = '" + quantidade  + "' WHERE id = '" + idEstoque + "';";
        fazerConexao(sql);
        
        try {
            statement.close(); 
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    // método responsável por adicionar um médicamento no estoque
    public static void adicionaMedicamentoEstoque (Estoque estoque){
        String sql = "INSERT INTO Estoque VALUES ('" + estoque.getNome() + "','" +
                estoque.getValidade() + "','" + estoque.getQuantidade() + "','" +
                estoque.getDescricao() + "','" + estoque.getFabricacao() + "','" + 
                estoque.getId() + "','" + estoque.getMatriculaProfissional() + "');";
    
        fazerConexao(sql);
        
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    // método responsável por adicionar um médicamento no estoque
    public static void preescreverMedicamento (Medicamento medicamento){
        String sql = "INSERT INTO Medicamento VALUES ('" + medicamento.getNome() + "','" +
                medicamento.getDataFinal()+ "','" + medicamento.getDataInicial()+ "','" +
                medicamento.getIntervaloHoras() + "','" + medicamento.getId() + "','" + 
                medicamento.getCpfPaciente() + "','" + medicamento.getMatriculaProfissional() + "');";
    
        fazerConexao(sql);
        
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    
}