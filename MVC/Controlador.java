package MVC;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextPane;

public class Controlador {
    private ConexaoChat conexao;
    public View visao;

    public Controlador(View visao, String nome, String ip) {
        this.visao = visao;
        // Inicia a rede passando a view para que ela receba as mensagens
        this.conexao = new ConexaoChat(ip, 3322, nome, visao); 

        JTextPane barraChat = visao.getBarraChat();

        barraChat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Impede pular linha
                    
                    if (barraChat.getDocument().getLength() > 0) {
                        String texto = barraChat.getText();
                        
                        
                        if (texto.trim().equalsIgnoreCase("sair")) {
                            conexao.enviar("SAIR"); // Avisa o servidor para remover este cliente
                            System.exit(0);         // Encerra o programa cliente
                            return;                         // Para a execução aqui
                        }
                        
                        
                        
                        conexao.enviar(texto);

                        
                        JTextPane paneParaOBalao = new JTextPane();
                        paneParaOBalao.setFont(barraChat.getFont());
                        
                        
                        
                        try {
                            
                            String caminhoBase ="";
                            Mensagem.processarTextoEConvertesEmojis(paneParaOBalao, texto, caminhoBase);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        
                 
                        visao.adicionarMensagem(paneParaOBalao, true); 
                        visao.limparBarraChat();             
                    }
                }
            }
        });
    }
}