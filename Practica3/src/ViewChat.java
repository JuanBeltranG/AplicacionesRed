
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author rodri
 */
public class ViewChat {

    String path, tmp_u = "", tmp_m = "";
    int bandera;
    String mensaje_inicio = "", mensaje_medio = "", mensaje_final = "";
    JPanel panel;
    JPanel panel3;
    JPanel panel4;
    JPanel panel5;
    JScrollPane sPanel;
    JEditorPane messageView;
    JEditorPane writingArea;
    JButton sendB;
    JButton emojiB;
    JButton audioB;
    JButton fileB;
    JLabel nameChat;
    JLabel Emoji1;
    JLabel Emoji2;
    JLabel Emoji3;
    JLabel Emoji4;
    JFrame frame;
    Color Color;
    MulticastSocket m;
    int counter = 0;

    public ViewChat() {
        initComponent();
    }

    public void despliegaInfoNIC(NetworkInterface netint) throws SocketException {
        System.out.printf("Nombre de despliegue: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        String multicast = (netint.supportsMulticast()) ? "Soporta multicast" : "No soporta multicast";
        System.out.printf("Multicast: %s\n", multicast);
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("Direccion: %s\n", inetAddress);
        }
        System.out.printf("\n");
    }

    public class SendMulticast extends Thread {

        MulticastSocket socket;

        public SendMulticast(MulticastSocket m, String message) {
            this.socket = m;
            try {
                String dir6 = "ff3e::1234:1";
                int pto = 1234;
                InetAddress gpo = InetAddress.getByName(dir6);
                byte[] b = message.getBytes();
                DatagramPacket p = new DatagramPacket(b, b.length, gpo, pto);
                socket.send(p);

            } catch (Exception e) {
                e.printStackTrace();
            }//catch
        }

    }

    public class ReceiveMulticast extends Thread {

        MulticastSocket socket;
        String msj = "";

        public ReceiveMulticast(MulticastSocket m) {
            this.socket = m;
        }

        public String getMsj() {
            return msj;
        }

        public void run() {
            try {
                for (;;) {
                    DatagramPacket p = new DatagramPacket(new byte[65535], 65535);
                    System.out.println("Listo para recibir mensajes...");
                    socket.receive(p);
                    msj = msj + new String(p.getData(), 0, p.getLength());
                    messageView.setText(msj);
                    System.out.println("Mensaje recibido: ");
                    System.out.println(msj);
                } //for
            } catch (Exception e) {
                e.printStackTrace();
            }//catch
        }//run

    }

    public void StartMulticast() {
        try {
            int pto = 1234, z = 0;
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            int interfaz = 9;
            NetworkInterface ni = NetworkInterface.getByIndex(interfaz);
            System.out.println("\nElegiste " + ni.getDisplayName());
            m = new MulticastSocket(pto);
            m.setReuseAddress(true);
            m.setTimeToLive(255);
            String dir6 = "ff3e::1234:1";
            InetAddress gpo = InetAddress.getByName(dir6);
            SocketAddress dirm;
            try {
                dirm = new InetSocketAddress(gpo, pto);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }//catch
            m.joinGroup(dirm, ni);
            System.out.println("Socket unido al grupo " + gpo);

            ReceiveMulticast r = new ReceiveMulticast(m);
            r.start();
            r.join();
        } catch (Exception e) {
        }
    }

    private void initComponent() {
        //html for emojis
        File f = new File("");
        String ruta = f.getAbsolutePath();
        mensaje_inicio = "<head><base href=\"file:" + ruta + "\\\">\n"
                + "<style>#usuarios {"
                + "font-family: Arial, Helvetica, sans-serif;"
                + "border-collapse: collapse;"
                + "width: 100%;"
                + "} #usuarios td, #usuarios th {"
                + "border: 0px solid #ddd;"
                + " padding: 8px;"
                + "}#usuarios tr:nth-child(even){background-color: #f2f2f2;}"
                + "#usuarios tr:hover {background-color: #ddd;}"
                + "#usuarios th {"
                + " padding-top: 12px;"
                + "padding-bottom: 12px;"
                + "text-align: left;"
                + "background-color: #04AA6D;"
                + "color: white;}"
                + "</style>\n"
                + "</head>\n"
                + "<doby>\n<table id=\"usuarios\">\n";
        mensaje_final = "</table></body>\n";
        // Making frame

        frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);

        // Creando el panel en la parte inferior y agregando componentes       
        panel = new JPanel(); // el panel no está visible en la salida 
        panel.setBackground(Color.GRAY);
        panel.setPreferredSize(new Dimension(500, 200));
        panel3 = new JPanel(); // el panel no está visible en la salida 
        panel3.setBackground(Color = new Color(194, 214, 214));
        panel3.setPreferredSize(new Dimension(450, 150));
        panel4 = new JPanel(); // el panel no está visible en la salida 
        panel4.setVisible(false);
        panel4.setBackground(Color = new Color(194, 214, 214));
        panel4.setPreferredSize(new Dimension(400, 100));
        panel5 = new JPanel(); // el panel no está visible en la salida 
        panel5.setBackground(Color = new Color(102, 153, 153));
        panel5.setPreferredSize(new Dimension(500, 50));
        ////////////
        messageView = new JEditorPane();
        messageView.setContentType("text/html");
        messageView.setEditable(false);

        messageView.setBackground(Color = new Color(194, 214, 214));
        //
        sPanel = new JScrollPane(messageView);
        sPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Setting Button 'Send'
        sendB = new JButton("Enviar");
        sendB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // aquí el código que quieres ejecutar cuando el botón sea presionado
                sendBActionPerformed(arg0);
            }
        });
        //Setting Button 'Emoji'
        emojiB = new JButton("Emojis");
        emojiB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // aquí el código que quieres ejecutar cuando el botón sea presionado
                emojiBActionPerformed(arg0);
            }
        });
        //Setting Button 'Record Audio'
        audioB = new JButton("Record Audio");
        //Setting Button 'Files'
        fileB = new JButton("Add File");
        //Label for name chat
        nameChat = new JLabel("Chat 1");
        //Setting Button 'Emoji'
        Emoji1 = new JLabel("E1");
        Emoji1.setSize(new Dimension(80, 80));
        Emoji1.setPreferredSize(new Dimension(80, 80));
        ImageIcon emo1 = new ImageIcon(ruta + "\\Pictures\\Emoji1.png");
        Icon icono1 = new ImageIcon(emo1.getImage().getScaledInstance(Emoji1.getWidth(), Emoji1.getHeight(), Image.SCALE_DEFAULT));
        Emoji1.setIcon(icono1);

        Emoji1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Emoji1MouseClicked();
            }
        });
        //Setting Button 'Emoji'
        Emoji2 = new JLabel("E2");
        Emoji2.setSize(new Dimension(80, 80));
        Emoji2.setPreferredSize(new Dimension(80, 80));
        ImageIcon emo2 = new ImageIcon(ruta + "\\Pictures\\Emoji2.png");
        Icon icono2 = new ImageIcon(emo2.getImage().getScaledInstance(Emoji2.getWidth(), Emoji2.getHeight(), Image.SCALE_DEFAULT));
        Emoji2.setIcon(icono2);

        Emoji2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Emoji2MouseClicked();
            }
        });
        //Setting Button 'Emoji'
        Emoji3 = new JLabel("E3");
        Emoji3.setSize(new Dimension(80, 80));
        ImageIcon emo3 = new ImageIcon(ruta + "\\Pictures\\Emoji3.png");
        Icon icono3 = new ImageIcon(emo3.getImage().getScaledInstance(Emoji3.getWidth(), Emoji3.getHeight(), Image.SCALE_DEFAULT));
        Emoji3.setIcon(icono3);
        Emoji3.setPreferredSize(new Dimension(80, 80));

        Emoji3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Emoji3MouseClicked();
            }
        });
        //Setting Button 'Emoji'
        Emoji4 = new JLabel("E4");
        Emoji4.setSize(new Dimension(80, 80));
        ImageIcon emo4 = new ImageIcon(ruta + "\\Pictures\\Emoji4.png");
        Icon icono4 = new ImageIcon(emo4.getImage().getScaledInstance(Emoji4.getWidth(), Emoji4.getHeight(), Image.SCALE_DEFAULT));
        Emoji4.setIcon(icono4);
        Emoji4.setPreferredSize(new Dimension(80, 80));

        Emoji4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Emoji4MouseClicked();
            }
        });
        //Writing Area 
        writingArea = new JEditorPane();
        writingArea.setPreferredSize(new Dimension(250, 50));
        //panel.add(label); // Componentes agregados usando Flow Layout     

        panel.add(writingArea);
        panel.add(sendB);
        panel.add(panel3);
        panel3.add(emojiB);
        panel3.add(audioB);
        panel3.add(fileB);
        panel3.add(panel4);
        panel5.add(nameChat);
        panel4.add(Emoji1);
        panel4.add(Emoji2);
        panel4.add(Emoji3);
        panel4.add(Emoji4);
        // Área de texto en el centro    

        // Agregar componentes al marco.      
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, panel5);
        frame.getContentPane().add(BorderLayout.CENTER, sPanel);
        frame.setVisible(true);
    }

    private void sendBActionPerformed(java.awt.event.ActionEvent evt) {
        mensaje_medio = "<tr>\n"
                + "<td>" + nameChat.getText() + " dice: </td>\n"
                + "<td>" + writingArea.getText() + "</td>\n"
                + "</tr>";
        if (counter == 0) {
            counter++;
            SendMulticast se = new SendMulticast(m, mensaje_inicio + mensaje_medio);
        } else {
            SendMulticast se = new SendMulticast(m, mensaje_medio);
        }

        //messageView.setText(mensaje_inicio + mensaje_medio + mensaje_final);
        //System.out.println(mensaje_inicio + mensaje_medio + mensaje_final);
        writingArea.setText("");
    }

    private void emojiBActionPerformed(java.awt.event.ActionEvent evt) {
        if (panel4.isVisible()) {
            //chande the backgroung color
            panel4.setVisible(false);
        } else {
            panel4.setVisible(true);
        }
    }

    private void Emoji1MouseClicked() {
        System.out.println("<img src=\"Pictures\\Emoji1.png\" width=\"50\" height=\"50\"></img>");
        mensaje_medio = mensaje_medio + "<tr>\n"
                + "<td>" + nameChat.getText() + " dice: </td>\n"
                + "<td>" + "<img src=\"Pictures\\Emoji1.png\" width=\"50\" height=\"50\"></img>" + "</td>\n"
                + "</tr>";
        messageView.setText(mensaje_inicio + mensaje_medio + mensaje_final);
    }

    private void Emoji2MouseClicked() {
        System.out.println("<img src=\"Pictures\\Emoji2.png\" width=\"50\" height=\"50\"></img>");
        mensaje_medio = mensaje_medio + "<tr>\n"
                + "<td>" + nameChat.getText() + " dice: </td>\n"
                + "<td>" + "<img src=\"Pictures\\Emoji2.png\" width=\"50\" height=\"50\"></img>" + "</td>\n"
                + "</tr>";
        messageView.setText(mensaje_inicio + mensaje_medio + mensaje_final);
    }

    private void Emoji3MouseClicked() {
        System.out.println("<img src=\"Pictures\\Emoji3.png\" width=\"50\" height=\"50\"></img>");
        mensaje_medio = mensaje_medio + "<tr>\n"
                + "<td>" + nameChat.getText() + " dice: </td>\n"
                + "<td>" + "<img src=\"Pictures\\Emoji3.png\" width=\"50\" height=\"50\"></img>" + "</td>\n"
                + "</tr>";
        messageView.setText(mensaje_inicio + mensaje_medio + mensaje_final);
    }

    private void Emoji4MouseClicked() {
        System.out.println("<img src=\"Pictures\\Emoji4.png\" width=\"50\" height=\"50\"></img>");
        mensaje_medio = mensaje_medio + "<tr>\n"
                + "<td>" + nameChat.getText() + " dice: </td>\n"
                + "<td>" + "<img src=\"Pictures\\Emoji4.png\" width=\"50\" height=\"50\"></img>" + "</td>\n"
                + "</tr>";
        messageView.setText(mensaje_inicio + mensaje_medio + mensaje_final);
    }

    public static void main(String args[]) {
        ViewChat chat = new ViewChat();
        chat.StartMulticast();
    }
}
