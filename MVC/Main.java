package MVC;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        String escolha = JOptionPane.showInputDialog("Digite 'S' para Servidor ou 'C' para Cliente:");

        if (escolha != null && escolha.equalsIgnoreCase("S")) {
            Servidor.main(args); 
        } else if (escolha != null && escolha.equalsIgnoreCase("C")) {
            String nome = JOptionPane.showInputDialog("Seu nome:");
            String ip = JOptionPane.showInputDialog("IP do Servidor:", "666.666.6.66");//{ATENÇÃO} PARA O CÓDIGO RODAR VOCÊ PRIMEIRO PRECISA
            //  NO DISPOSITIVO DA SECRETÁRIA ABRIR O CMD(PROMPT DE COMANDO, DIGITAR: IPCONFIG E EDITAR ONDE ESTÁ ESCRITO 666.666.6.66 
            // NESTE CÓDIGO E NA CLASSE CLIENTE PARA O ATUAL IPV4 DA MÁQUINA DA SECRETÁRIA(O MESMO VALE PARA O CLIENTE NO QUAL DEVE SE COLOCAR O IPV4 DA SECRETÁRIA).)
            
            // Controlador precisa ser criado 
            // para "ouvir" os eventos da View e gerenciar a rede.
            View visao = new View();
            new Controlador(visao, nome, ip); 
        }
    }
}