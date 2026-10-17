package MVC;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextPane;

public class Servidor {
    public static List<PrintStream> clientes = new ArrayList<>();

    //broadcast
    public static void broadcast(String mensagem) {
        for (PrintStream cliente : clientes) {
            cliente.println(mensagem);
        }
    }

    public static void main(String[] args) {
        //Inicia a View do Servidor
        View visaoServidor = new View();
        
        // Configura a lógica de envio pelo botão (Enter) da View
        // Aqui você está dizendo: "Quando digitar na View do servidor, envie para todos"
        visaoServidor.getBarraChat().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume(); // Não pula linha
                    String msg = visaoServidor.getBarraChat().getText();
                    if (msg !=null && !msg.trim().isEmpty()) {
                        //Pegar o texto bruto (que contém as tags criadas na View)
                        // Envia para todos os clientes conectados
                        for (PrintStream cliente : clientes) {
                            cliente.println("[Secretaria]: " + msg);
                        }
                        // Adiciona na própria tela do servidor
                         JTextPane balao = new JTextPane();
                         balao.setFont(visaoServidor.getBarraChat().getFont());
                         String caminhoBase= "";
                         // Executa o processador inteligente para apagar o texto bruto e desenhar a imagem
                         Mensagem.processarTextoEConvertesEmojis(balao, "[Secretaria]: "+ msg,caminhoBase);
                         visaoServidor.adicionarMensagem(balao, true);
                         visaoServidor.limparBarraChat();
                        
                    
                    }
                }
            }
        });

        // 3. Thread do Servidor (aceitando conexões)
        try (ServerSocket servidor = new ServerSocket(3322)) {
            System.out.println("=== SERVIDOR DA SECRETARIA ATIVO ===");

            while (true) {
                Socket cliente = servidor.accept();
                new Thread(new TratadorCliente(cliente, visaoServidor)).start();
            }
        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
        
    }
}

class TratadorCliente implements Runnable {
    private Socket cliente;
    private View visao;
    private PrintStream saida;
    private String nome;

    public TratadorCliente(Socket cliente, View visao) { 
        this.cliente = cliente; 
        this.visao = visao;
    }

    @Override
    public void run() {

           
            
        try (java.util.Scanner leitor = new java.util.Scanner(cliente.getInputStream());
             PrintStream saida = new PrintStream(cliente.getOutputStream(), true)) {
            
            this.saida = saida;
            Servidor.clientes.add(saida);
            nome = leitor.hasNextLine() ? leitor.nextLine() : "Desconhecido";

            // --- ADICIONE O BROADCAST DE ENTRADA ---
            String msgEntrada = ">> " + nome + " entrou no chat.";
            Servidor.broadcast(msgEntrada);

            JTextPane avisoEntrada = new JTextPane();
            avisoEntrada.setText(msgEntrada);
            visao.adicionarMensagem(avisoEntrada, false);
            // ---------------------------------------
            
            while (leitor.hasNextLine()) {
                String msg = leitor.nextLine();

                // ALTERAÇÃO AQUI: Verifica se é o comando de sair
                if (msg.trim().equalsIgnoreCase("SAIR")) {
                    System.out.println(nome + " desconectou.");
                    break; // Sai do loop while e vai para o bloco finally
                }

                // Mostra na tela do servidor quem enviou
                JTextPane balao = new JTextPane();
                balao.setFont(new java.awt.Font("Arial",java.awt.Font.PLAIN,22));
              

                String caminhoBase = "";
                Mensagem.processarTextoEConvertesEmojis(balao, "[" + nome + "]: " + msg, caminhoBase);

                visao.adicionarMensagem(balao, false);
                
                // Repassa para os outros
                for (PrintStream c : Servidor.clientes) {
                    if (c != saida) c.println("[" + nome + "]: " + msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Conexão interrompida com o cliente. " + nome);
        } finally {
            if (saida != null) {
                Servidor.clientes.remove(saida);

                // 1. Monta a mensagem única
                String msgSaida = ">> " + nome + " saiu do chat.";

                // 2. Avisa todos os outros clientes conectados
                Servidor.broadcast(msgSaida);

                // 3. Avisa a tela da secretaria (a própria view do servidor)
                JTextPane avisoSaida = new JTextPane();
                avisoSaida.setText(msgSaida);
                this.visao.adicionarMensagem(avisoSaida, false);
            }

            try {
                cliente.close();
            } catch (Exception e) {
                
            }
        }
    }
}