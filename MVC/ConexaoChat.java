package MVC;
//import MVC.View;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class ConexaoChat {
    private Socket socket;
    private PrintStream saida;
    public View visao;

    public ConexaoChat(String ip, int porta, String nome, View visao) {
        this.visao = visao;
        try {
            socket = new Socket(ip, porta);
            saida = new PrintStream(socket.getOutputStream(), true);
            saida.println(nome); // Envia o nome ao conectar

            // Thread para escutar o servidor
            new Thread(() -> {
                try (Scanner entrada = new Scanner(socket.getInputStream())) {
                    while (entrada.hasNextLine()) {
                        String msg = entrada.nextLine();
                        JTextPane pane = new JTextPane();
                        pane.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 22));
                        String caminhoBase = "";
                        Mensagem.processarTextoEConvertesEmojis(pane, msg, caminhoBase);
                        SwingUtilities.invokeLater(() -> visao.adicionarMensagem(pane, false));
                    }
                } catch (Exception e) {
                    System.err.println("Conexão perdida.");
                }
            }).start();
        } catch (java.net.ConnectException e) {
            System.err.println("AVISO: Servidor offline ou IP inválido! Modo offline ativado para testes locais.");
            socket = null;
            saida = null;
        } catch (Exception e){
            System.err.println("Erro inesperado na rede: "+ e.getMessage());
        }
    }

    public void enviar(String mensagem) {
        if (saida != null) saida.println(mensagem);
    }
}