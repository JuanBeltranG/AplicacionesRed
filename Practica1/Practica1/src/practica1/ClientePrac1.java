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
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class ClientePrac1 {

    public static void consultaRepoLocal(String carpeta) {

        //Con el sig segmento de codigo obtenemos la ruta a nuestra carpeta que contiene el repo local
        File f = new File("");
        String ruta = f.getAbsolutePath();
        String rutaRepoLocal = ruta + "\\" + carpeta + "\\";

        //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
        File folder = new File(rutaRepoLocal);
        System.out.println("\n Archivos en el repositorio local del cliente\n");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("Directorio: " + fileEntry.getName());
            } else {
                System.out.println("Archivo: " + fileEntry.getName());
            }
        }
    }

    public static void consultaRepoServidor() {
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            //System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida para consultar repo Servidor");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
            String eco = "";
            System.out.println("\n Archivos en el repositorio del servidor\n");
            while (!eco.equals("archivos enviados")) {
                eco = br1.readLine();
                if (!(eco.equals("archivos enviados"))) {
                    System.out.println(eco);
                }
            }//while
            //Cerramos el socket una vez enviada la opcion
            br1.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void eliminarDirectorios(File directorio) {
        for (File fileEntry : directorio.listFiles()) {
            if (fileEntry.isDirectory()) {
                eliminarDirectorios(fileEntry);
            }
            fileEntry.delete();
        }
    }

    public static void eliminarArchivoRepoLocal(String carpeta) {
        try {
            String op = "";
            do {
                //Con el sig segmento de codigo obtenemos la ruta a nuestra carpeta que contiene el repo local
                File f = new File("");
                String ruta = f.getAbsolutePath();
                String rutaRepoLocal = ruta + "\\" + carpeta + "\\";
                int option;
                //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
                File folder = new File(rutaRepoLocal);
                ArrayList<String> archivos = new ArrayList<String>();
                int i = 0;
                for (File fileEntry : folder.listFiles()) {
                    archivos.add(fileEntry.getName());
                    System.out.println("[" + i + "]" + archivos.get(i));
                    i++;
                }

                System.out.println("Escribe el numero del archivo que quieras eliminar");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                op = reader.readLine();
                option = Integer.parseInt(op);
                File rf = new File(rutaRepoLocal + (archivos.get(option)));
                if (rf.isDirectory()) {
                    eliminarDirectorios(rf);
                    rf.delete();
                } else {
                    rf.delete();
                }
                archivos.remove(option);
                System.out.println("Archivo eliminado");
                System.out.println("Quieres eliminar otro elemento?  (S/N)");
                op = reader.readLine();
            } while (op.compareToIgnoreCase("s") == 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void elimanarArchivoRepoServidor() {
        try {
            String op = "";
            do {
                int pto = 8000;
                String dir = "127.0.0.1";
                Socket cl = new Socket(dir, pto);
                BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
                String eco = "";
                System.out.println("\n Archivos en el repositorio del Servidor\n");
                ArrayList<String> archivos = new ArrayList<String>();
                int posicionA = 0;
                while (!eco.equals("archivos enviados")) {
                    eco = br1.readLine();
                    if (!(eco.equals("archivos enviados"))) {
                        archivos.add(eco);
                        System.out.println("[" + posicionA + "]" + archivos.get(posicionA));
                        posicionA++;
                    }//while
                }
                //Cerramos el socket una vez enviada la opcion
                br1.close();
                cl.close();

                System.out.println("Escribe el numero del archivo que quieras eliminar");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                op = reader.readLine();
                Socket c2 = new Socket(dir, pto);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(c2.getOutputStream(), "ISO-8859-1"));
                pw.println(op);
                pw.flush();
                pw.close();
                c2.close();
                System.out.println("Quieres eliminar otro elemento?  (S/N)");
                op = reader.readLine();
                Socket c3 = new Socket(dir, pto);
                PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(c3.getOutputStream(), "ISO-8859-1"));
                pw2.println(op);
                pw2.flush();
                pw2.close();
                c3.close();
            } while (op.compareToIgnoreCase("s") == 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void subirArchivosCarpetas() {
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
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
            Socket c1 = new Socket(dir, pto);
            DataOutputStream dos = new DataOutputStream(c1.getOutputStream());
            dos.writeInt(sizeArreglo);
            dos.flush();
            dos.close();
            c1.close();
            System.out.println("Numero de archivos seleccionados " + sizeArreglo + " han enviados");

            for (int i = 0; i < sizeArreglo; i++) {
                Socket clienteArchivos = new Socket(dir, pto);
                long tam = f[i].length();
                String nombre = f[i].getName();
                String path = f[i].getAbsolutePath();
                System.out.println("Preparandose pare enviar archivo " + path + " de " + tam + " bytes\n\n");
                DataOutputStream dosC = new DataOutputStream(clienteArchivos.getOutputStream());
                DataInputStream disC = new DataInputStream(new FileInputStream(path));
                dosC.writeUTF(nombre);
                dosC.flush();
                dosC.writeLong(tam);
                dosC.flush();
                long enviados = 0;
                int l = 0, porcentaje = 0;
                while (enviados < tam) {
                    byte[] b = new byte[1500];
                    l = disC.read(b);
                    dosC.write(b, 0, l);
                    dosC.flush();
                    enviados = enviados + l;
                    porcentaje = (int) ((enviados * 100) / tam);
                    if (porcentaje % 10 == 0) {
                        System.out.print("\rEnviado el " + porcentaje + " % del archivo");
                    }
                }
                System.out.println("\nArchivo enviado..");
                dosC.close();
                disC.close();
                clienteArchivos.close();
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
            //System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida para el menu");
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream(), "ISO-8859-1"));
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
            String eco = "";
            String mensaje = String.valueOf(opcionM);
            pw.println(mensaje);
            pw.flush();
            eco = br1.readLine();
            //System.out.println("Eco recibido desde " + cl.getInetAddress() + ":" + cl.getPort() + " " + eco + "\n");
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
            System.out.println("");
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
            System.out.println("-----------------------------------------------------------------------------------");

            switch (option) {
                case 1:
                    consultaRepoLocal("RepositorioCliente");
                    break;
                case 2:
                    sMenuServidor(option);
                    consultaRepoServidor();
                    break;
                case 3:
                    sMenuServidor(option);
                    subirArchivosCarpetas();
                    break;
                case 4:

                    break;
                case 5:
                    eliminarArchivoRepoLocal("RepositorioCliente");
                    break;
                case 6:
                    sMenuServidor(option);
                    elimanarArchivoRepoServidor();
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
