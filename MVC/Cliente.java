package MVC;
import java.net.Socket;
import java.io.PrintStream;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String ipServidor = "666.666.6.66"; // IP do notebook servidor
        int porta = 3322;

        try (Socket conexao = new Socket(ipServidor, porta);
             Scanner teclado = new Scanner(System.in);
             PrintStream saida = new PrintStream(conexao.getOutputStream(), true);
             Scanner entradaServidor = new Scanner(conexao.getInputStream())) {

            System.out.print("Digite seu nome de inspetor: ");
            String nomeInspetor = teclado.nextLine();
            saida.println(nomeInspetor);
            
            System.out.println("Conectado! Digite suas mensagens:");
                
            // Thread para receber mensagens do servidor
            new Thread(() -> { 
                try {
                    while (entradaServidor.hasNextLine()) {
                        System.out.println("\n" + entradaServidor.nextLine());
                        System.out.print("[Chat] Digite sua mensagem: "); 
                    }
                } catch (Exception e) {
                    System.out.println("\nConexão com servidor perdida.");
                }
            }).start();
            
            // Loop principal para enviar mensagens
            while (true) {
                System.out.print("[Chat] Digite sua mensagem: ");
                String texto = teclado.nextLine();
                saida.println(texto);
                
                if (texto.equalsIgnoreCase("sair")) break;
            }

        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}