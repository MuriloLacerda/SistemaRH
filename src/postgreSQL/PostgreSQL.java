package postgreSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 
 * @author Imaginatio
 */
public class PostgreSQL {
    private final Scanner input = new Scanner(System.in);
    private final String URL_DB;
    private Statement stm = null;
    private ResultSet rs = null;
    private String user = "postgres";
    private String pw = "";
    private String url = "jdbc:postgresql://localhost:5432/";

    public PostgreSQL(String user, String pw, String url) {
        this.URL_DB = url.concat(url.charAt(url.length() - 1) == '/' ? "empresa" : "/empresa");
        this.user = user;
        this.pw = pw;
        this.url = url;
    }
       
    private Connection conectaDB() { 
        Connection c =  null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(URL_DB, user, pw);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return c;
    }
    
    public void addFuncionario(String id, String nome, String idade, String endereco, String salario) {
        Connection c = conectaDB();
        try {
            c.setAutoCommit(false);
            stm = c.createStatement();
            String sql = "INSERT INTO COMPANIA (ID, NOME, IDADE, ENDERECO, SALARIO) "
            + "VALUES (" + id + ", " 
                    + "'" + nome + "'" + ", " 
                    + idade + ", " 
                    + "'" + endereco + "'" + ", " 
                    + salario + ");";
            stm.executeUpdate(sql);     
            stm.close();
            c.commit();
            c.close();
            listarFuncionarios();
            System.out.println("Funcionario criado com sucesso!");
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    
    public void delFuncionario(String id) {
        Connection c = conectaDB();
        try { 
            c.setAutoCommit(false);
            stm = c.createStatement();
            stm.executeUpdate("DELETE from COMPANIA where ID=" + id + ";");
            c.commit();
            stm.close();
            c.close();
            listarFuncionarios();
            System.out.println("Funcionario deletado com sucesso.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public void listarFuncionarios()  {
        Connection c = conectaDB();
        try {
            c.setAutoCommit(false);
            stm = c.createStatement();
            rs = stm.executeQuery("SELECT * FROM COMPANIA");
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            System.out.println("Tabela COMPANIA\n");
            
            for (int i = 1; i <= n; i++) 
                System.out.printf("%-8s\t", rsmd.getColumnName(i));  
            System.out.println("");
            
            while(rs.next()) {
                for (int i = 1; i <= n; i++) 
                    System.out.printf("%-8s\t", rs.getObject(i));
                System.out.println("");
            }
            System.out.println("\n\n");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            try {
                c.commit();
                stm.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }
    
    public void alterarDados() {
        Connection c =conectaDB();
        String col = null;
        char op;      
        try {
            c.setAutoCommit(false);
            stm = c.createStatement();
            
            System.out.print("Digite o ID: ");
            String id = input.next();
                     
            System.out.print("Digite o nome: ");
            col = input.next();
            stm.executeUpdate("UPDATE COMPANIA set NOME =" + "'" + col + "'" + "where ID ="  + id + ";");
            System.out.println("Nome alterado com sucesso.");

            System.out.print("Digite a idade: ");
            col = input.next();
            stm.executeUpdate("UPDATE COMPANIA set IDADE =" + col + "where ID=" + id + ";");
            System.out.println("Idade alterada com sucesso.");

            System.out.print("Digite o endereco: ");
            col = input.next();
            stm.executeUpdate("UPDATE COMPANIA set ENDERECO =" + "'" + col + "'" + "where ID=" + id + ";");
            System.out.println("Endereco alterado com sucesso.");

            System.out.print("Digite o salario: ");
            col = input.next();
            stm.executeUpdate("UPDATE COMPANIA set SALARIO =" + col + "where ID=" + id + ";");
            System.out.println("Nome alterado com sucesso.");
                    
            stm.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }   
    }

    //Metodo de conexao para verificacao
    private Connection conectaPG() {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url.concat(url.charAt(url.length() - 1) == '/' ? "" : "/"), user, pw);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return c;
    }
    
    //Metodos de verificacao e criacao de Banco de Dados.
    private void criaDB() {
        Connection c = conectaPG();
        if (naoExisteDB()) {
            try {
                stm = c.createStatement();
                stm.executeQuery("CREATE DATABASE EMPRESA;");
                stm.close();
                c.close();
                System.out.println("Banco de dados 'empresa' criado com sucesso!");
            } catch (Exception e) {
                System.err.println(e.getClass().getName()+": "+e.getMessage());
            }
        }
        else
            System.out.println("Banco de dados 'empresa' [OK!].");
    }
    
    private boolean naoExisteDB() {
        Connection c = conectaPG();
        String emp =  null;
        try {
            stm = c.createStatement();
            rs = stm.executeQuery("SELECT datname FROM pg_database WHERE datname = 'empresa';");
            while(rs.next()) {
                emp = rs.getString("datname");
            }
            stm.close();
            c.close();
            if("empresa".equalsIgnoreCase(emp)) return false;
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return true;
    }
    
    //Metodos de verificacao e criacao de tabela.
    private void criaTB() {
        Connection c = conectaDB();
        if (naoExisteTB()) {
            try {
                stm = c.createStatement();
                String sql = "CREATE TABLE COMPANIA " +
                      "(ID INT PRIMARY KEY     NOT NULL," +
                      " NOME           TEXT    NOT NULL, " +
                      " IDADE            INT     NOT NULL, " +
                      " ENDERECO        CHAR(50), " +
                      " SALARIO         REAL)";
                stm.executeUpdate(sql);
                stm.close();
                c.close();
                System.out.println("tabela 'compania' criado com sucesso!");
            } catch (Exception e) {
                System.err.println(e.getClass().getName()+": "+e.getMessage());
            }
        }
        else
            System.out.println("Tabela 'compania' [OK!].");
    }
    private boolean naoExisteTB() {
        Connection c = conectaDB();
        String tab =  null;
        try {
            stm = c.createStatement();
            rs = stm.executeQuery("select relname from pg_class where relname = 'compania' and relkind='r';");
            while(rs.next()) {
                tab = rs.getString("relname");
            }
            stm.close();
            c.close();
            if(tab != null) return false;
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return true;
    }
    
    public void verificacao() {
        criaDB();
        criaTB();
    }
}
