package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SW {

    /*private int PUERTO = 8000;
    private ServerSocket ss;
		
    public SW() throws Exception {
        System.out.println("Iniciando Servidor.......");
        this.ss=new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:---OK");
        System.out.println("Esperando por Cliente....");
        for(;;){
            Socket accept = ss.accept();
            new Manejador(accept).start();
        }
    }*/
    public static void main(String[] args) throws Exception {

        Manejador man = new Manejador();
    }

}
