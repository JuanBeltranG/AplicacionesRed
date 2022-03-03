package practica1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorPrac1 {

    public static void subirArchivo(ServerSocket s,Socket cliente) {
        try {
            s.setReuseAddress(true);
            System.out.println("Servidor iniciado esperando por archivos..");
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta = "RepositorioServidor";
            String ruta_archivos = ruta + "\\" + carpeta + "\\";
            System.out.println("ruta:" + ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            for (;;) {
                cliente = s.accept();
                System.out.println("Cliente conectado desde " + cliente.getInetAddress() + ":" + cliente.getPort());
                DataInputStream dis = new DataInputStream(cliente.getInputStream());
                String nombre = dis.readUTF();
                long tam = dis.readLong();
                System.out.println("Comienza descarga del archivo " + nombre + " de " + tam + " bytes\n\n");
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta_archivos + nombre));
                long recibidos = 0;
                int l = 0, porcentaje = 0;
                while (recibidos < tam) {
                    byte[] b = new byte[1500];
                    l = dis.read(b);
                    System.out.println("leidos: " + l);
                    dos.write(b, 0, l);
                    dos.flush();
                    recibidos = recibidos + l;
                    porcentaje = (int) ((recibidos * 100) / tam);
                    System.out.println("\rRecibido el " + porcentaje + " % del archivo");
                }
                System.out.println("Archivo recibido..");
                dos.close();
                dis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            int pto1 = 8000;
            ServerSocket s1 = new ServerSocket(pto1);
            System.out.println("Servidor iniciado en el puerto " + pto1 + " .. esperando cliente..");
            for (;;) {
                Socket c1 = s1.accept();
                System.out.println("Cliente conectado desde " + c1.getInetAddress() + ":" + c1.getPort());
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(c1.getOutputStream(), "ISO-8859-1"));
                BufferedReader br = new BufferedReader(new InputStreamReader(c1.getInputStream(), "ISO-8859-1"));

                String msj = br.readLine(); //  \n\r (10)(13)
                System.out.println("Mensaje recibido: " + msj + " devolviendo eco");
                String option = msj;
                msj = "opcionRecibida";
                pw.println(msj);
                pw.flush();
                switch (option) {
                    case "3":
                        subirArchivo(s1,c1);
                        System.out.println("Archivo subido con exito");
                        break;
                    case "2":

                        System.out.println("Explorar servidor");
                        break;
                    default:
                        System.out.println("Operación no reconocida, por favor intentelo de nuevo");
                        break;
                }

                /*
                System.out.println("Cliente cierra conexion");
                br.close();
                pw.close();
                break;
                 */
                c1.close();
                System.out.println("se cierra el socket c1");
            }//for

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
