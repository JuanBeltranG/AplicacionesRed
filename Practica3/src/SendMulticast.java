
import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rodri
 */
public class SendMulticast extends Thread{
    MulticastSocket socket;
    BufferedReader br;
    
    public SendMulticast(MulticastSocket m, BufferedReader br){
        this.socket=m;
        this.br=br;
        
    }
    public void run(){
     try{
        //BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        String dir = "231.1.1.1";
        String dir6 = "ff3e::1234:1";
        int pto=1234;
        InetAddress gpo = InetAddress.getByName(dir6);

        for(;;){
            System.out.println("Escribe un mensaje para ser enviado:");
            String mensaje= br.readLine();
            byte[] b = mensaje.getBytes();
            DatagramPacket p = new DatagramPacket(b,b.length,gpo,pto);
            socket.send(p);
        }//for
     }catch(Exception e){
         e.printStackTrace();
     }//catch
     }//run
}

