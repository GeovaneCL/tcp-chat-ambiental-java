package MVC;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Color;
import java.io.File;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class View {

    private JFrame janela;
    private JTextPane barraChat = new JTextPane();
    
    private JPanel painelGaveta = new JPanel();
    private JPanel painelArquivos = new JPanel();
    private JPanel containerMensagens;
    private JScrollPane scrollChat;

    public void adicionarMensagem(JTextPane paneFormatado, boolean enviadaPorMim) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        // Instancia o novo balão passando o painel de texto clonado
        Mensagem novaMensagem = new Mensagem(paneFormatado, hora, enviadaPorMim);
        containerMensagens.add(novaMensagem);

        containerMensagens.revalidate();
        containerMensagens.setOpaque(true);
        containerMensagens.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollChat.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    public JTextPane getBarraChat() {
    return barraChat;
    } 
    public void limparBarraChat() {
        barraChat.setText("");
    } 

    public View(){
        janela = new JFrame("Chat Principal");

        // Definição das configurações dos novos painéis (Gaveta e Arquivos)
        painelGaveta.setLayout(null);
        painelGaveta.setBounds(73, 325, 550, 35);
        painelGaveta.setBackground(Color.decode("#E1E5EC"));
        painelGaveta.setVisible(false);

        painelArquivos.setLayout(null);
        painelArquivos.setBounds(73, 315, 550, 30);
        painelArquivos.setBackground(Color.LIGHT_GRAY);
        painelArquivos.setVisible(false);

        JPanel janelaChat = new JPanel();
        JPanel bordasComJanela = new JPanel();
        JPanel bordaBarraChat = new JPanel();
        
        Color CorDeFundo1 = Color.decode("#F0F2F6");
        Color CorDeFundo2 = Color.decode("#E1E5EC");


        Font fonteGrande = new Font("Futura", Font.PLAIN, 20);
        Font fonteGrande2 = new Font("Arial", Font.PLAIN, 22);

        JSeparator divisor2 = new JSeparator(SwingConstants.HORIZONTAL);
        divisor2.setBounds(1, 362, 685, 2);
        
        
        String caminhoBase = "";
        ImageIcon iconeEmoji = Mensagem.formatarIcone(caminhoBase + "Icones/EmojiSorriso.png", 25, 25);
        ImageIcon iconeAnexo = Mensagem.formatarIcone(caminhoBase + "Icones/clipe.jpg", 25, 25);
 
        JButton btnEmoji = new JButton(iconeEmoji);
        btnEmoji.setBounds(10, 380, 25, 25);
        
        JButton btnAnexo = new JButton(iconeAnexo);
        btnAnexo.setBounds(42, 380, 25, 25);
        
        

        
        String[] listaEmojis = {"EmojiJoia.png", "EmojiOrando.png", "EmojiChorando.png", "EmojiLagrimasAlegria.png", "EmojiSurpreso.png","EmojiCoracao.png","EmojiAgua.png","EmojiConfete.png","EmojiMuda.png"};
        int xEmoji = 5;
        for (String nome : listaEmojis) {
            JButton b = new JButton(Mensagem.formatarIcone(caminhoBase + "Emojis/" + nome, 25, 25));
            b.setBounds(xEmoji, 5, 30, 30);
            xEmoji += 35;
            
            b.addActionListener(e -> {
                try {
                    // que o motor de rede usa para serializar a mensagem sem corromper a ordem!
                    int pos = barraChat.getCaretPosition();
                    barraChat.getStyledDocument().insertString(pos, ":" + nome + ":", null);
                    
                    // Coloca também o feedback visual do ícone pequeno na barra
                    barraChat.insertIcon(Mensagem.formatarIcone(caminhoBase + "Emojis/" + nome, 20, 20));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                barraChat.requestFocus();
            });
            painelGaveta.add(b);
        }

        // Alternar visualização da gaveta de emojis
        btnEmoji.addActionListener(e -> {
            painelGaveta.setVisible(!painelGaveta.isVisible());
            janela.repaint();
        });

        
        btnAnexo.addActionListener(e -> {
            JFileChooser buscador = new JFileChooser();
            if (buscador.showOpenDialog(janela) == JFileChooser.APPROVE_OPTION) {
                File arquivo = buscador.getSelectedFile();
                painelArquivos.removeAll();
                
                JLabel labelArquivo = new JLabel("Arquivo selecionado: " + arquivo.getName());
                labelArquivo.setBounds(5, 0, 500, 20);
                painelArquivos.add(labelArquivo);
                
                painelArquivos.setVisible(true);
                janela.revalidate();
                janela.repaint();
            }
        });

        

     
        barraChat.setFont(fonteGrande2);
   
        barraChat.setBounds(73, 377, 550, 36); 
        
        

        JLabel abaNomedaConversa = new JLabel("Grupo de Conversa: ");
        abaNomedaConversa.setBounds(73, 10, 250, 30);
        abaNomedaConversa.setFont(fonteGrande);

        
        // --- CONFIGURAÇÃO DO CONTAINER DE MENSAGENS ---
        containerMensagens = new JPanel();
        containerMensagens.setLayout(new BoxLayout(containerMensagens, BoxLayout.Y_AXIS));
        containerMensagens.setBackground(CorDeFundo1);

        
        scrollChat = new JScrollPane(containerMensagens);
        scrollChat.setBounds(73, 50, 550, 260); 
        scrollChat.setBorder(null);

        bordasComJanela.setLayout(null);
        bordasComJanela.setBounds(0, 0, 685, 50);
        bordasComJanela.setBackground(CorDeFundo2);

        bordaBarraChat.setLayout(null);
        bordaBarraChat.setBounds(0, 361, 685, 80);
        bordaBarraChat.setBackground(CorDeFundo2);
        
        janelaChat.setLayout(null);
        janelaChat.setBounds(0, 0, 700, 423);
        janelaChat.setBackground(CorDeFundo1);
        
        // Adicionar componentes ao painel principal do chat
        janelaChat.add(abaNomedaConversa);
        janelaChat.add(scrollChat);
        janelaChat.add(painelArquivos);
        janelaChat.add(painelGaveta);
        janelaChat.add(barraChat);
        janelaChat.add(btnEmoji);
        janelaChat.add(btnAnexo);
        janelaChat.add(bordaBarraChat);
        janelaChat.add(bordasComJanela);
        
        janela.add(divisor2);
        janela.add(janelaChat);
                    
        janela.getContentPane().setBackground(CorDeFundo1);
        janela.setLayout(null);
        janela.setBounds(330, 150, 700, 460);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);
        janela.setVisible(true);

        
        janela.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
            // Envia o comando SAIR antes de fechar
            
            System.exit(0);
            }
        });
    }
}