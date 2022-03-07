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
import static practica1.ClientePrac1.consultaRepoLocal;

public class ServidorPrac1 {

    public static void subirArchivoDeCliente(ServerSocket s) {
        try {
            System.out.println("Servidor iniciado esperando por archivos..");
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta = "RepositorioServidor";
            String ruta_archivos = ruta + "\\" + carpeta + "\\";
            System.out.println("ruta:" + ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            //Inicializamos el servidor

            Socket cliente1 = s.accept();
            System.out.println("Cliente conectado desde " + cliente1.getInetAddress() + ":" + cliente1.getPort());
            DataInputStream disS = new DataInputStream(cliente1.getInputStream());
            int numArchivos = disS.readInt();
            System.out.println("Numero de archivos seleccionados recibidos "+ numArchivos);
            disS.close();
            cliente1.close();
            /*
            for (int i = 0; i < numArchivos; i++) {
                Socket clienteArchivos = s.accept();
                System.out.println("Cliente conectado desde " + clienteArchivos.getInetAddress() + ":" + cliente1.getPort());
                DataInputStream dis = new DataInputStream(clienteArchivos.getInputStream());
                String nombre = dis.readUTF();
                long tam = dis.readLong();
                System.out.println("Comienza descarga del archivo " + nombre + " de " + tam + " bytes\n\n");
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta_archivos + nombre));
                long recibidos = 0;
                int l = 0, porcentaje = 0;
                while (recibidos < tam) {
                    byte[] b = new byte[1500];
                    l = dis.read(b);
                    dos.write(b, 0, l);
                    dos.flush();
                    recibidos = recibidos + l;
                    porcentaje = (int) ((recibidos * 100) / tam);
                    if (porcentaje % 10 == 0) {
                        System.out.println("\rRecibido el " + porcentaje + " % del archivo");
                    }
                }
                System.out.println("Archivo recibido..");
                dis.close();
                dos.close();
                clienteArchivos.close();
                
            }
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void consultaLocalRepoServidor(String carpeta, ServerSocket s) {

        //Con el sig segmento de codigo obtenemos la ruta a nuestra carpeta que contiene el repo local
        File f = new File("");
        String ruta = f.getAbsolutePath();
        String rutaRepoLocal = ruta + "\\" + carpeta + "\\";
        String mensaje = "";
        //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
        File folder = new File(rutaRepoLocal);
        try {
            Socket c1 = s.accept();
            System.out.println("Cliente conectado desde " + c1.getInetAddress() + ":" + c1.getPort());
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(c1.getOutputStream(), "ISO-8859-1"));
            for (File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    mensaje = "Directorio:" + fileEntry.getName();
                } else {
                    mensaje = "Archivo:" + fileEntry.getName();
                }
                pw.println(mensaje);
                pw.flush();

            }
            mensaje = "archivos enviados";
            pw.println(mensaje);
            pw.flush();
            pw.close();
            c1.close();

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
                br.close();
                pw.close();
                c1.close();
                System.out.println("se cierra el socket c1 Main");
                switch (option) {
                    case "2":
                        consultaLocalRepoServidor("RepositorioServidor", s1);
                        System.out.println("Explorar servidor");
                        break;
                    case "3":
                        subirArchivoDeCliente(s1);
                        System.out.println("Archivo subido con exito");
                        break;

                    default:
                        System.out.println("Operación no reconocida, por favor intentelo de nuevo");
                        break;
                }
            }//for

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
