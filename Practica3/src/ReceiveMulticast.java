
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rodri
 */
public class ReceiveMulticast extends Thread {
    MulticastSocket socket;
    
    public ReceiveMulticast(MulticastSocket m){
        this.socket=m;
    }
    public void run(){
       try{
           
        for(;;){
           DatagramPacket p = new DatagramPacket(new byte[65535],65535);
            System.out.println("Listo para recibir mensajes...");
           socket.receive(p);
           String msj = new String(p.getData(),0,p.getLength());
            System.out.println("Mensaje recibido: "+msj);
       } //for
       }catch(Exception e){
           e.printStackTrace();
       }//catch
    }//run
}
