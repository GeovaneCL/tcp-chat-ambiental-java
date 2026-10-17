package MVC;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class Mensagem extends JPanel {

    // Ajustado para receber o JTextPane formatado
    public Mensagem(JTextPane conteudoFormatado, String hora, boolean enviadaPorMim) {
        this.setLayout(new FlowLayout(enviadaPorMim ? FlowLayout.RIGHT : FlowLayout.LEFT));
        this.setOpaque(false);

        // O Balão
        JPanel balao = new JPanel(new BorderLayout());
        balao.setBackground(enviadaPorMim ? Color.decode("#CFD6DD") : Color.WHITE);
        balao.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // Margem interna elegante
        
       
        conteudoFormatado.setOpaque(false);
        
      
        int larguraMaxima = 220;
        //  Define um limite de largura para o balão não cobrir a tela inteira, mas deixa a altura livre
        conteudoFormatado.setMaximumSize(new Dimension(larguraMaxima, Integer.MAX_VALUE));
        //  Força o componente a quebrar as linhas com base na largura limite antes de calcular o tamanho real
        conteudoFormatado.setSize(new Dimension(larguraMaxima, conteudoFormatado.getPreferredSize().height));
      
        int larguraCalculada = Math.min(conteudoFormatado.getPreferredSize().width + 20, larguraMaxima);
        conteudoFormatado.setPreferredSize(new Dimension(larguraCalculada, conteudoFormatado.getPreferredSize().height));


        // JLabel da Hora
        JLabel labelHora = new JLabel(hora);
        labelHora.setFont(new Font("Arial", Font.PLAIN, 10));
        labelHora.setForeground(Color.GRAY);
        labelHora.setHorizontalAlignment(SwingConstants.RIGHT);
        

        // Montagem final do balão
        balao.add(conteudoFormatado, BorderLayout.CENTER);
        balao.add(labelHora, BorderLayout.SOUTH);
        this.add(balao);
    }

    public static ImageIcon formatarIcone(String caminho, int largura, int altura) {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) {
            System.err.println("ERRO: Arquivo não encontrado em: " + arquivo.getAbsolutePath());
            return null; 
        }
        ImageIcon icon = new ImageIcon(caminho);
        Image img = icon.getImage();
        Image novaImg = img.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(novaImg);
    }
    public static void processarTextoEConvertesEmojis(JTextPane pane, String texto, String caminhoBase) {
        pane.setText(""); // Limpa o texto padrão
        
        
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(":[^:\\s]+?(\\.(png|jpg|gif))?:");
        java.util.regex.Matcher matcher = pattern.matcher(texto);
        
        int ultimoIndice = 0;
        while (matcher.find()) {
            // Insere o texto que estava antes do emoji (Ex: "[Secretaria]: ")
            String textoAnterior = texto.substring(ultimoIndice,matcher.start());
            if(!textoAnterior.isEmpty()){
            try {
                int fimDoc = pane.getDocument().getLength();
                pane.setCaretPosition(fimDoc);
                pane.getStyledDocument().insertString(fimDoc,textoAnterior, null);
            } catch (Exception e) { e.printStackTrace(); }
        }
            
            
            String tag = matcher.group();
            String nomeEmoji = tag.replace(":", "");

              if(!nomeEmoji.toLowerCase().endsWith(".png")){
                nomeEmoji +=".png";
            }
            
            // Carrega o ícone do emoji com base no caminho absoluto do sistema
            ImageIcon icone = formatarIcone(caminhoBase + "Emojis/" + nomeEmoji, 20, 20);
          
            if (icone != null) {
                pane.setCaretPosition(pane.getDocument().getLength());
                pane.insertIcon(icone);
            } else {
                // Fallback caso a imagem suma do disco
                try {
                    int fimDoc = pane.getDocument().getLength();
                    pane.setCaretPosition(fimDoc);
                    pane.getStyledDocument().insertString(fimDoc, tag, null);
                } catch (Exception e) { e.printStackTrace(); }
            }
            
            ultimoIndice = matcher.end();
        }
        
        
        if (ultimoIndice < texto.length()) {
            try {
                int fimDoc = pane.getDocument().getLength();
                pane.setCaretPosition(fimDoc);

                pane.getStyledDocument().insertString(fimDoc, texto.substring(ultimoIndice), null);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}