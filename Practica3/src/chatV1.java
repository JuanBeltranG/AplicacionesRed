
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author rodri
 */
public class chatV1 {

    Vector<String> ListUsers = new Vector<String>();
    String mensaje_medio = "";
    String msjView = "";
    JPanel panel;
    JPanel panel3;
    JPanel panel4;
    JPanel panel5;
    JPanel panel6;
    JScrollPane sPanel;
    JEditorPane messageView;
    JEditorPane writingArea;
    JButton sendB;
    JButton fileB;
    JLabel nameChat;
    JLabel chatWith;
    JFrame frame;
    JComboBox comboUsers;
    Color Color;
    MulticastSocket m;
    File f = new File("");
    String ruta = f.getAbsolutePath();
    String mensaje_inicio = "<head><base href=\"file:" + ruta + "\\\">\n"
            + "<style>#usuarios {"
            + "font-family: Arial, Helvetica, sans-serif;"
            + "border-collapse: collapse;"
            + "width: 100%;"
            + "} #usuarios td, #usuarios th {"
            + "border: 1px solid #ddd;"
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
    String mensaje_final = "</table></body>\n";

    public chatV1() {
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

    public class SendFile {

        MulticastSocket socket;

        public SendFile(MulticastSocket m) {
            this.socket = m;
            try {
                String dir = "230.1.1.1";
                int pto = 1234;
                InetAddress gpo = InetAddress.getByName(dir);
                int sizeArreglo = 0;
                JFileChooser jf = new JFileChooser();
                jf.setMultiSelectionEnabled(true);
                int r = jf.showOpenDialog(null);
                File[] f = null;
                System.out.println("lanzando FileChooser..");
                if (r == JFileChooser.APPROVE_OPTION) {
                    f = jf.getSelectedFiles();
                    sizeArreglo = f.length;
                }
                for (int i = 0; i < sizeArreglo; i++) {
                    SendMulticast se = new SendMulticast(m, "<Archivo>" + f[i].getName() + "," + f[i].getAbsolutePath() + "," + f[i].length());
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f[i]));
                    long tam = f[i].length();
                    long enviados = 0;
                    int l = 0;
                    while (enviados < tam) {
                        byte[] b = new byte[1500];
                        l = bis.read(b);
                        System.out.println("enviados: " + l);
                        DatagramPacket p = new DatagramPacket(b, b.length, gpo, pto);
                        socket.send(p);
                        enviados = enviados + l;
                    }
                    System.out.println("\nArchivo enviado..");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }//catch
        }

    }

    public class SendMulticast {

        MulticastSocket socket;

        public SendMulticast(MulticastSocket m, String message) {
            this.socket = m;
            try {
                String dir = "230.1.1.1";
                int pto = 1234;
                InetAddress gpo = InetAddress.getByName(dir);
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
                    msj = new String(p.getData(), 0, p.getLength());
                    System.out.println("Mensaje recido : " + msj);
                    msj = classificationMsj(msj, m);
                    msjView += msj;
                    messageView.setText(mensaje_inicio + msjView + mensaje_final);
                } //for
            } catch (Exception e) {
                e.printStackTrace();
            }//catch
        }//run

    }

    public String classificationMsj(String message, MulticastSocket m) throws IOException {

        String messageAux = "";
        String usuario = "", writting = "";
        String nombre = "";
        String destino = "";
        String nameFile = "";
        String pathFile = "";
        long sizeFile;
        int i;
        int j;
        if (message.contains(":)")) {
            System.out.println("si entro al emoji");
            message = message.replace(":)", "<img src=\"Pictures\\Emoji1.png\" width=\"50\" height=\"50\"></img>");
        } else {
            if (message.contains(":(")) {
                System.out.println("si entro al emoji");
                message = message.replace(":(", "<img src=\"Pictures\\Emoji2.png\" width=\"50\" height=\"50\"></img>");
            }
        }
        if (message.contains("<inicio>")) {
            usuario = message.substring(8, message.length());
            if (!usuario.equals(nameChat.getText())) {
                if (!ListUsers.contains(usuario)) {
                    message = "<tr>\n"
                            + "<td>" + usuario + " dice: </td>\n"
                            + "<td> Usuario conectado </td>\n"
                            + "</tr>";
                    ListUsers.add(usuario);
                }
            }
            message = "";
            nombre = "<UsuarioEnLinea>" + nameChat.getText();
            SendMulticast se = new SendMulticast(m, nombre);
        } else {
            if (message.contains("<msj>")) {
                messageAux = message.substring(5, message.length());
                for (i = 0; i < messageAux.length(); i++) {
                    char c = messageAux.charAt(i);
                    if (c == '>') {
                        break;
                    }
                }
                usuario = messageAux.substring(1, i);
                writting = messageAux.substring(i + 1, messageAux.length());
                message = "<tr>\n"
                        + "<td>" + usuario + " dice: </td>\n"
                        + "<td>" + writting + "</td>\n"
                        + "</tr>";

            } else {
                if (message.contains("<privado>")) {
                    messageAux = message.substring(9, message.length());
                    for (i = 0; i < messageAux.length(); i++) {
                        char c = messageAux.charAt(i);
                        if (c == '>') {
                            break;
                        }
                    }
                    usuario = messageAux.substring(1, i);
                    messageAux = messageAux.substring(i + 1, messageAux.length());
                    for (i = 0; i < messageAux.length(); i++) {
                        char c = messageAux.charAt(i);
                        if (c == '>') {
                            break;
                        }
                    }
                    destino = messageAux.substring(1, i);
                    writting = messageAux.substring(i + 1, messageAux.length());
                    if (nameChat.getText().equals(destino) || nameChat.getText().equals(usuario)) {
                        message = "<tr>\n"
                                + "<td>" + usuario + " dice: </td>\n"
                                + "<td>" + writting + "</td>\n"
                                + "</tr>";
                    } else {
                        message = "";
                    }
                } else {
                    if (message.contains("<UsuarioEnLinea>")) {
                        usuario = message.substring(16, message.length());
                        if (!usuario.equals(nameChat.getText())) {
                            if (!ListUsers.contains(usuario)) {
                                ListUsers.add(usuario);
                                System.out.println("Agregado a la lista : " + usuario);

                            }
                        }
                        message = "";
                    } else {
                        if (message.contains("<Archivo>")) {
                            messageAux = message.substring(9, message.length());
                            for (i = 0; i < messageAux.length(); i++) {
                                char c = messageAux.charAt(i);
                                if (c == ',') {
                                    break;
                                }
                            }
                            nameFile = messageAux.substring(0, i);
                            messageAux = messageAux.substring(i + 1, messageAux.length());
                            for (i = 0; i < messageAux.length(); i++) {
                                char c = messageAux.charAt(i);
                                if (c == ',') {
                                    break;
                                }
                            }
                            pathFile = messageAux.substring(0, i);
                            sizeFile = Long.parseLong(messageAux.substring(i + 1, messageAux.length()));
                            MulticastSocket socket = m;
                            System.out.println("NanmeFIle :" + nameFile);
                            System.out.println("pathFile :" + pathFile);
                            System.out.println("sizeFile :" + sizeFile);
                            long recibidos = 0;
                            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream("C:\\Users\\rodri\\Documents\\SextoSemestre\\Redes\\Practicas\\Practica3\\Pictures\\" + nameFile));
                            while (recibidos < sizeFile) {
                                DatagramPacket p = new DatagramPacket(new byte[1500], 1500);
                                socket.receive(p);
                                fos.write(p.getData());
                                fos.flush();
                                recibidos = recibidos + p.getLength();
                                System.out.println("recibidos :" + recibidos);

                            }
                            fos.close();
                            System.out.println("Archivo recibido y creado");
                        }
                    }
                }
            }
        }

        return message;

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
            String dir = "230.1.1.1";
            InetAddress gpo = InetAddress.getByName(dir);
            SocketAddress dirm;
            try {
                dirm = new InetSocketAddress(gpo, pto);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }//catch
            m.joinGroup(dirm, ni);
            System.out.println("Socket unido al grupo " + gpo);
            mensaje_medio = "<inicio>" + nameChat.getText();
            SendMulticast se = new SendMulticast(m, mensaje_medio);
            ReceiveMulticast r = new ReceiveMulticast(m);
            r.start();
            r.join();
        } catch (Exception e) {
        }
    }

    private void initComponent() {
        String userName = JOptionPane.showInputDialog("Escribe tu nombre de usuario");
        // Making frame
        frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        // Creando el panel en la parte inferior y agregando componentes       
        panel = new JPanel(); // el panel no está visible en la salida 
        panel.setBackground(Color.GRAY);
        panel.setPreferredSize(new Dimension(500, 60));
        panel5 = new JPanel(); // el panel no está visible en la salida 
        panel5.setBackground(Color = new Color(102, 153, 153));
        panel5.setPreferredSize(new Dimension(500, 50));
        panel6 = new JPanel(); // el panel no está visible en la salida 
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
        sendB.setPreferredSize(new Dimension(80, 50));
        sendB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // aquí el código que quieres ejecutar cuando el botón sea presionado
                sendBActionPerformed(arg0);
            }
        });

        //Setting Button 'Files'
        fileB = new JButton("Add File");
        fileB.setPreferredSize(new Dimension(80, 50));
        fileB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // aquí el código que quieres ejecutar cuando el botón sea presionado
                fileBActionPerformed(arg0);
            }
        });
        //Label for name chat
        nameChat = new JLabel(userName);
        chatWith = new JLabel(" chatenado con :");

        //Writing Area 
        writingArea = new JEditorPane();
        writingArea.setPreferredSize(new Dimension(250, 50));
        //panel.add(label); // Componentes agregados usando Flow Layout     
        comboUsers = new JComboBox(ListUsers);
        comboUsers.setPreferredSize(new Dimension(150, 30));

        comboUsers.addItem("Sala comun");
        comboUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // aquí el código que quieres ejecutar cuando el botón sea presionado
                comboUsersAction(arg0);
            }
        });
        panel.add(writingArea);
        panel.add(sendB);
        panel.add(fileB);
        panel5.add(nameChat);
        panel5.add(chatWith);
        panel5.add(comboUsers);

        // Área de texto en el centro    
        // Agregar componentes al marco.      
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, panel5);
        frame.getContentPane().add(BorderLayout.CENTER, sPanel);
        frame.setVisible(true);

    }

    private void comboUsersAction(java.awt.event.ActionEvent evt) {
        System.out.println("Utilizando el oombo");
        messageView.setText("");
    }

    private void sendBActionPerformed(java.awt.event.ActionEvent evt) {
        if (comboUsers.getSelectedItem().equals("Sala comun")) {
            mensaje_medio = "<msj><" + nameChat.getText() + ">" + writingArea.getText();
            SendMulticast se = new SendMulticast(m, mensaje_medio);
            writingArea.setText("");
        } else {
            mensaje_medio = "<privado><" + nameChat.getText() + "><" + comboUsers.getSelectedItem() + ">" + writingArea.getText();
            SendMulticast se = new SendMulticast(m, mensaje_medio);
            writingArea.setText("");
        }

    }

    private void fileBActionPerformed(java.awt.event.ActionEvent evt) {
        SendFile sf = new SendFile(m);
    }

    public static void main(String args[]) {
        chatV1 chat = new chatV1();
        chat.StartMulticast();
    }
}
