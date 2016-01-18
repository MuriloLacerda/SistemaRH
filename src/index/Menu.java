/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package index;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import postgreSQL.PostgreSQL;

/**
 * 
 * @author Imaginatio
 */
public class Menu {
    private final Scanner input;
    private PostgreSQL cmd;

    public Menu() {
        this.input = new Scanner(System.in);
        verificaoConexao();
    }
    
    private void verificaoConexao() {
        System.out.print("Digite a url do servidor postgresql: ");
        String url = input.next();
        System.out.print("Digite o login: ");
        String user = input.next();
        System.out.print("Digite a senha: ");
        String pw = input.next();
        this.cmd = new PostgreSQL(user, pw, url);     
    }
    
    public void cabecalho() {
        System.out.println("\tSistema de RH");
        System.out.println("Este programa foi criado utilizando postgreSQL v1.18.1");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
    
    public int menu() throws SQLException, IOException {
        cmd.verificacao();
        cabecalho();
        char op, v;
        do {            
            System.out.println("1. Incluir funcionario.");
            System.out.println("2. Listar funcionarios.");
            System.out.println("3. Deletar funcionario.");
            System.out.println("4. Alterar dados de um funcionario.");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opcao: ");
            op = input.next(".").charAt(0);
            
            switch(op) {
                case '1':
                    opAddFunc();
                    v = validaResp();
                    if(v == 's') {
                        cls();
                        break;
                    }
                    else System.exit(0);
                case '2':
                    cmd.listarFuncionarios();
                    v = validaResp();
                    if(v == 's') {
                        cls();
                        break;
                    }
                    else System.exit(0);
                case '3':
                    opDelFunc();
                    v = validaResp();
                    if(v == 's') {
                        cls();
                        break;
                    }
                    else System.exit(0);
                case '4':
                    cmd.alterarDados();
                    v = validaResp();
                    if(v == 's') {
                        cls();
                        break;
                    }
                    else System.exit(0);
                case '5':
                    System.exit(0);
                default:
                    System.err.println("Opcao invalida");
                    cls();
                    break;
            }
        } while (op != '6');
        return op;
    }
    
    public void opAddFunc() {
        System.out.print("Digite o id: ");
        String id = input.next();
        System.out.print("Digite o nome: ");
        String nome = input.next();
        System.out.print("Digite a idade: ");
        String idade = input.next();
        System.out.print("Digite o endereco: ");
        String endereco = input.next();
        System.out.print("Digite o salario: ");
        String salario = input.next();
        cmd.addFuncionario(id, nome, idade, endereco, salario);
    }
    
    public void opDelFunc()  {
        System.out.print("Insirado o ID do funcionario: ");
        String id = input.next();
        cmd.delFuncionario(id);
    }
    
    private char validaResp() {
        char ch;
        do {            
            System.out.println("Deseja realizar outra operacao? [S ou N] ? ");
            ch = input.next(".").charAt(0);
        } while ((ch != 's' && ch != 'S') && (ch != 'n' && ch != 'N'));
        return ch;
    }
    
    public static void cls() throws IOException {  
        Process aProc = Runtime.getRuntime().exec("cls");        
        byte arr[] = new byte[5000];  
        aProc.getInputStream().read(arr);  
        System.out.println(new String(arr));  
    } 
}
