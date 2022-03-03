package practica1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFileChooser;

public class ClientePrac1 {

    public static void consultaRepoLocal() {

        //Con el sig segmento de codigo obtenemos la ruta a nuestra carpeta que contiene el repo local
        File f = new File("");
        String ruta = f.getAbsolutePath();
        String carpeta = "RepositorioCliente";
        String rutaRepoLocal = ruta + "\\" + carpeta + "\\";

        //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
        File folder = new File(rutaRepoLocal);

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("Directorio: " + fileEntry.getName());
            } else {
                System.out.println("Archivo: " + fileEntry.getName());
            }
        }
    }

    public static void subirArchivosCarpetas() {

        //DE MOMENTO ESTO SOLO ENVIA ARCHIVOS AUN NO ENVIA VARIOS ARCHIVOS NI DIRECTORIOS ENTEROS
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            System.out.println("Conexion con servidor establecida.. lanzando FileChooser..");
            JFileChooser jf = new JFileChooser();
            //jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);
            jf.setRequestFocusEnabled(true);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile();
                String nombre = f.getName();
                String path = f.getAbsolutePath();
                long tam = f.length();
                System.out.println("Preparandose pare enviar archivo " + path + " de " + tam + " bytes\n\n");
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(new FileInputStream(path));
                dos.writeUTF(nombre);
                dos.flush();
                dos.writeLong(tam);
                dos.flush();
                long enviados = 0;
                int l = 0, porcentaje = 0;
                while (enviados < tam) {
                    byte[] b = new byte[1500];
                    l = dis.read(b);
                    System.out.println("enviados: " + l);
                    dos.write(b, 0, l);
                    dos.flush();
                    enviados = enviados + l;
                    porcentaje = (int) ((enviados * 100) / tam);
                    System.out.print("\rEnviado el " + porcentaje + " % del archivo");
                }//while
                System.out.println("\nArchivo enviado..");
                dis.close();
                dos.close();
                cl.close();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void sMenuServidor(int opcionM) {
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida para el menu");
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream(), "ISO-8859-1"));
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
            String eco = "";
            while (!eco.equals("opcionRecibida")) {
                String mensaje = String.valueOf(opcionM);
                pw.println(mensaje);
                pw.flush();
                eco = br1.readLine();
                System.out.println("Eco recibido desde " + cl.getInetAddress() + ":" + cl.getPort() + " " + eco + "\n");

            }//while
//Cerramos el socket una vez enviada la opcion
            br1.close();
            br1.close();
            pw.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        //Comenzaremos mostrando el menu de las opciones disponibles para el usuario
        int option = 0;
        while (option != 8) {

            System.out.println("BIENVENIDO A TU REPOSITORIO DE ARCHIVOS");
            System.out.println("Estas son las operaciones disponibles");
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("1.- Listar contenido de mi repositorio local");
            System.out.println("2.- Listar contenido de mi repositorio remoto");
            System.out.println("3.- Subir archivos/carpetas al repositorio remoto");
            System.out.println("4.- Descargar archivos/carpetas del repositorio remoto hacia mi repositorio local");
            System.out.println("5.- Eliminar archivos/carpetas del repositorio local");
            System.out.println("6.- Eliminar archivos/carpetas del repositorio remoto");
            System.out.println("7.- Modificar parametros de conexión con el repositorio remoto");
            System.out.println("8.- Salir");
            System.out.println("-----------------------------------------------------------------------------------");

            System.out.println("Introduce el numero de la opcion a realizar: ");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String op = reader.readLine();
            option = Integer.parseInt(op);

            switch (option) {
                case 1:
                    consultaRepoLocal();
                    break;
                case 2:
                    sMenuServidor(option);
                    break;
                case 3:
                    sMenuServidor(option);
                    subirArchivosCarpetas();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    System.out.println("Hata la proxima");
                    break;
                default:
                    System.out.println("Operación no reconocida, por favor intentelo de nuevo");
                    break;
            }

        }

    }

}
