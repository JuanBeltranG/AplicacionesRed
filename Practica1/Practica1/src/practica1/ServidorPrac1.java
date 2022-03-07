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
import java.util.ArrayList;
import static practica1.ClientePrac1.eliminarDirectorios;

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
            //Inicializamos el servidor para saber el numero de archivos a recibir
            Socket cliente1 = s.accept();
            //System.out.println("Cliente conectado desde " + cliente1.getInetAddress() + ":" + cliente1.getPort());
            DataInputStream disS = new DataInputStream(cliente1.getInputStream());
            int numArchivos = disS.readInt();
            System.out.println("Numero de archivos seleccionados recibidos " + numArchivos);
            disS.close();
            cliente1.close();
            //for para recibir los archivos
            for (int i = 0; i < numArchivos; i++) {
                Socket clienteArchivos = s.accept();
                //System.out.println("Cliente conectado desde " + clienteArchivos.getInetAddress() + ":" + cliente1.getPort());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void consultaRepoServidor(String carpeta, ServerSocket s) {

        //Con el sig segmento de codigo obtenemos la ruta a nuestra carpeta que contiene el repo local
        File f = new File("");
        String ruta = f.getAbsolutePath();
        String rutaRepoLocal = ruta + "\\" + carpeta + "\\";
        String mensaje = "";
        //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
        File folder = new File(rutaRepoLocal);
        try {
            Socket c1 = s.accept();
            //System.out.println("Cliente conectado desde " + c1.getInetAddress() + ":" + c1.getPort());
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

    public static void eliminarRepoServidor(String carpeta, ServerSocket s) {

        try {
            String op = "";
            do {
                consultaRepoServidor(carpeta, s);
                int pto = 8000;
                String dir = "127.0.0.1";
                Socket cl = s.accept();
                //System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida para consultar repo Servidor");
                BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
                String archivoE = "";
                archivoE = br1.readLine();
                //Cerramos el socket una vez enviada la opcion
                br1.close();
                cl.close();
                File f = new File("");
                String ruta = f.getAbsolutePath();
                String rutaRepoLocal = ruta + "\\" + carpeta + "\\";
                int option = Integer.parseInt(archivoE);
                //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
                File folder = new File(rutaRepoLocal);
                ArrayList<String> archivos = new ArrayList<String>();
                for (File fileEntry : folder.listFiles()) {
                    archivos.add(fileEntry.getName());
                }
                File rf = new File(rutaRepoLocal + (archivos.get(option)));
                if (rf.isDirectory()) {
                    eliminarDirectorios(rf);
                    rf.delete();
                } else {
                    rf.delete();
                }
                archivos.remove(option);
                System.out.println("Archivo eliminado");
                Socket c2 = s.accept();
                //System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida para consultar repo Servidor");
                BufferedReader br2 = new BufferedReader(new InputStreamReader(c2.getInputStream(), "ISO-8859-1"));
                op = br2.readLine();
                //Cerramos el socket una vez enviada la opcion
                br2.close();
                c2.close();
            } while (op.compareToIgnoreCase("s") == 0);

        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {
        try {

            int pto1 = 8000;
            ServerSocket s1 = new ServerSocket(pto1);
            System.out.println("Servidor iniciado en el puerto " + pto1 + " .. esperando cliente..");
            for (;;) {
                Socket c1 = s1.accept();
                //System.out.println("Cliente conectado desde " + c1.getInetAddress() + ":" + c1.getPort());
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
                        consultaRepoServidor("RepositorioServidor", s1);
                        //System.out.println("Explorar servidor");
                        break;
                    case "3":
                        subirArchivoDeCliente(s1);
                        //System.out.println("Archivo subido con exito");
                        break;
                    case "6":
                        eliminarRepoServidor("RepositorioServidor", s1);
                        //System.out.println("Archivo subido con exito");
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
