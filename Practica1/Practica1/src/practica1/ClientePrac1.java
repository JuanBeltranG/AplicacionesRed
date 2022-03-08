package practica1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;


public class ClientePrac1 {
    
    private static boolean reuseAddres = false;
    private static boolean algoritmoNagle = false;
    private static boolean keepAlive = false;
    
    private static int tamBufferLectura = 65536;
    private static int tamBufferEscritura = 65536;
    private static int tempLectura = 0;// ponemos 0 ya que esta es la opcion por defecto y es como infinito
    
    
    
    
     public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
    
    
    public static void descargarArchivosRemoto(){
        
        try{
            String op = "";
            do{
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            
            //PARAMETROS DE COMUNICACION
            cl.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            cl.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            cl.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            cl.setReuseAddress(reuseAddres);
            cl.setKeepAlive(keepAlive);
            cl.setSoTimeout(tempLectura);
            
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
            
            //Procedemos a recibir todo el conjunto de archivos y directorios que hay ene l repo remoto
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
            br1.close();
            cl.close();
            //preguntamos que archivo quiere descargar
            System.out.println("Escriba el numero del archivo que desea descargar:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String numArcDescarga = reader.readLine();
            //Enviamos al servidor el numero del archivo que eliminaremos
            Socket c2 = new Socket(dir, pto);
            //PARAMETROS DE COMUNICACION
            c2.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            c2.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            c2.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            c2.setReuseAddress(reuseAddres);
            c2.setKeepAlive(keepAlive);
            c2.setSoTimeout(tempLectura);
            
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(c2.getOutputStream(), "ISO-8859-1"));
            pw.println(numArcDescarga);
            pw.flush();
            pw.close();
            c2.close();
            //Llegados a este punto comenzara la descargar del archivo
            //Para esto nuestro cliente se convertira momentaneamente en el servidor
            ServerSocket ra = new ServerSocket(8001);
            ra.setReuseAddress(true);
            System.out.println("Cliente esperando por archivo del servidor");
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta = "RepositorioCliente";
            String ruta_archivos = ruta + "\\" + carpeta + "\\";
            System.out.println("ruta:" + ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            
            Socket  sCliente = ra.accept();
            System.out.println("El servidor se ha conectado con el cliente para iniciar la transferencia del archivo");
            DataInputStream dis = new DataInputStream(sCliente.getInputStream());
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
                    System.out.print("\rRecibido el " + porcentaje + " % del archivo");
                }
            System.out.println("Archivo descargado con exito");
            dos.close();
            dis.close();
            sCliente.close();
            ra.close();
       
            }while(op.compareToIgnoreCase("s") == 0);
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
    
    public static void descargaCarpetaRemoto(){
        
        try{
           String op = "";
            do{
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            
            //PARAMETROS DE COMUNICACION
            cl.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            cl.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            cl.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            cl.setReuseAddress(reuseAddres);
            cl.setKeepAlive(keepAlive);
            cl.setSoTimeout(tempLectura);
            
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
            
            //Procedemos a recibir todo el conjunto de archivos y directorios que hay ene l repo remoto
            String eco = "";
            System.out.println("\nCarpetas en el repositorio del Servidor\n");
            ArrayList<String> archivos = new ArrayList<String>();
           
            while (!eco.equals("archivos enviados")) {
                eco = br1.readLine();
                if (!(eco.equals("archivos enviados"))) {
                    archivos.add(eco);
                }//while
            }
            br1.close();
            cl.close();
            
            //procedemos a imprimir unicamente las opciones que son directorios
            for(int i = 0; i < archivos.size(); i++){
              if(archivos.get(i).contains("Directorio")){
                   System.out.println("[" +i+"]"+archivos.get(i));
              }
            }
            
            //preguntamos que directorio quiere descargar
            System.out.println("Escriba el numero del directorio que desea descargar:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String numArcDescarga = reader.readLine();
            //Enviamos al servidor el numero del archivo que eliminaremos
            Socket c2 = new Socket(dir, pto);
            
            //PARAMETROS DE COMUNICACION
            c2.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            c2.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            c2.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            c2.setReuseAddress(reuseAddres);
            c2.setKeepAlive(keepAlive);
            c2.setSoTimeout(tempLectura);
            
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(c2.getOutputStream(), "ISO-8859-1"));
            pw.println(numArcDescarga);
            pw.flush();
            pw.close();
            c2.close();
            //Llegados a este punto comenzara la descargar del directorio
            //Para esto nuestro cliente se convertira momentaneamente en el servidor
            ServerSocket ra = new ServerSocket(8001);
            ra.setReuseAddress(true);
            System.out.println("Cliente esperando por archivo del servidor");
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta = "RepositorioCliente";
            String ruta_archivos = ruta + "\\" + carpeta + "\\";
            System.out.println("ruta:" + ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            
            Socket  sCliente = ra.accept();
            System.out.println("El servidor se ha conectado con el cliente para iniciar la transferencia del archivo");
            DataInputStream dis = new DataInputStream(sCliente.getInputStream());
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
                    System.out.print("\rRecibido el " + porcentaje + " % del archivo");
                }
            System.out.println("Archivo descargado con exito");
            dos.close();
            dis.close();
            sCliente.close();
            ra.close();
            
            //En esta seccion descomprimiremos el archivo zip
                String fileZip = ruta_archivos + nombre;

                //En las siguientes lineas crearemos la ruta directorio donde se guardara nuestro archivo zip 
                String destination = ruta_archivos/* + nombre.substring(0, nombre.indexOf(".zip"))*/;

                File destDir = new File(destination);
                byte[] buffer = new byte[1024];
                ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
                ZipEntry zipEntry = zis.getNextEntry();

                while (zipEntry != null) {
                    File newFile = newFile(destDir, zipEntry);
                    if (zipEntry.isDirectory()) {
                        if (!newFile.isDirectory() && !newFile.mkdirs()) {
                            throw new IOException("Failed to create directory " + newFile);
                        }
                    } else {
                        // fix for Windows-created archives
                        File parent = newFile.getParentFile();
                        if (!parent.isDirectory() && !parent.mkdirs()) {
                            throw new IOException("Failed to create directory " + parent);
                        }

                        // write file content
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                    zipEntry = zis.getNextEntry();

                }
                zis.closeEntry();
                zis.close();
                
                File borra = new File(ruta_archivos + nombre);
                borra.delete();
                
                return;
          
            }while(op.compareToIgnoreCase("s") == 0); 
         
            
        }catch(Exception e){
            
            
            
        }
        
        
    }
    
    public static void menuDescarga() throws IOException{
        
        int option = 0;
        
        while(option != 3){
            System.out.println("");
            
            
            System.out.println("1.- Descargar archivos de mi repositorio remoto");
            System.out.println("2.- Descargar carpetas de mi repositorio remoto");
            System.out.println("3.- Salir al menu principal");
            
            System.out.println("Introduce el numero de la opcion a realizar: ");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String op = reader.readLine();
            option = Integer.parseInt(op);
            
            switch (option) {
                case 1:
                    sMenuServidor(4);
                    descargarArchivosRemoto();
                    break;
                case 2:
                    sMenuServidor(5);
                    descargaCarpetaRemoto();
                    break;
                case 3:
                    
                    break;
                default:
                    System.out.println("Operación no reconocida, por favor intentelo de nuevo");
                    break;
            }
        }
        
    }

    public static void consultaRepoLocal(String carpeta) {

        //Con el sig segmento de codigo obtenemos la ruta a nuestra carpeta que contiene el repo local
        File f = new File("");
        String ruta = f.getAbsolutePath();
        String rutaRepoLocal = ruta + "\\" + carpeta + "\\";

        //Con el sig segmento de codigo obtendremos todos los archivos y directorios del repo local y los imprimiremos
        File folder = new File(rutaRepoLocal);
        System.out.println("\nArchivos en el repositorio local del cliente\n");
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
            //PARAMETROS DE COMUNICACION
            cl.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            cl.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            cl.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            cl.setReuseAddress(reuseAddres);
            cl.setKeepAlive(keepAlive);
            cl.setSoTimeout(tempLectura);
            
            //System.out.println("Conexion con el servidor " + dir + ":" + pto + " establecida para consultar repo Servidor");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
            String eco = "";
            System.out.println("\nArchivos en el repositorio del servidor\n");
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
                
                 //PARAMETROS DE COMUNICACION
                cl.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
                cl.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
                cl.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
                cl.setReuseAddress(reuseAddres);
                cl.setKeepAlive(keepAlive);
                cl.setSoTimeout(tempLectura);
                
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
                
                //PARAMETROS DE COMUNICACION
                c2.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
                c2.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
                c2.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
                c2.setReuseAddress(reuseAddres);
                c2.setKeepAlive(keepAlive);
                c2.setSoTimeout(tempLectura);
                
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(c2.getOutputStream(), "ISO-8859-1"));
                pw.println(op);
                pw.flush();
                pw.close();
                c2.close();
                System.out.println("Quieres eliminar otro elemento?  (S/N)");
                op = reader.readLine();
                Socket c3 = new Socket(dir, pto);
                
                //PARAMETROS DE CONEXION
                c3.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
                c3.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
                c3.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
                c3.setReuseAddress(reuseAddres);
                c3.setKeepAlive(keepAlive);
                c3.setSoTimeout(tempLectura);
                
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
            
             //PARAMETROS DE COMUNICACION
            c1.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            c1.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            c1.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            c1.setReuseAddress(reuseAddres);
            c1.setKeepAlive(keepAlive);
            c1.setSoTimeout(tempLectura);
            
            DataOutputStream dos = new DataOutputStream(c1.getOutputStream());
            dos.writeInt(sizeArreglo);
            dos.flush();
            dos.close();
            c1.close();
            System.out.println("Numero de archivos seleccionados " + sizeArreglo + " han enviados");

            for (int i = 0; i < sizeArreglo; i++) {
                Socket clienteArchivos = new Socket(dir, pto);
                
                 //PARAMETROS DE COMUNICACION
                clienteArchivos.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
                clienteArchivos.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
                clienteArchivos.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
                clienteArchivos.setReuseAddress(reuseAddres);
                clienteArchivos.setKeepAlive(keepAlive);
                clienteArchivos.setSoTimeout(tempLectura);
            
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
    
    public static void agregarArchivo(String ruta, String directorio, ZipOutputStream zip) throws Exception{
        
        File archivo = new File(directorio);
        if(archivo.isDirectory()){
            agregarCarpeta(ruta, directorio, zip);
        }else{
            byte[] buffer = new byte[4096];
            int leido;
            FileInputStream entrada = new FileInputStream(archivo);
            zip.putNextEntry(new ZipEntry(ruta + "/" +archivo.getName()));
            while((leido = entrada.read(buffer)) > 0){
                zip.write(buffer,0, leido);
                
            }
            
        }
    }
    
    public static void agregarCarpeta(String ruta, String carpeta, ZipOutputStream zip) throws Exception{
        File directorio = new File(carpeta);
        for(String nombreArchivo: directorio.list()){
            if(ruta.equals("")){
                agregarArchivo(directorio.getName(), carpeta + "/" + nombreArchivo, zip);
            }else{
                agregarArchivo(ruta + "/" + directorio.getName(), carpeta + "/" + nombreArchivo, zip);
            }
            
        }
        
    }
    
    public static void comprimir(String archivo, String archivoZIP) throws Exception{
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(archivoZIP));
        agregarCarpeta("",archivo,zip);
        zip.flush();
        zip.close();
        
    }
    
    public static void subirCarpetas(){
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            
             //PARAMETROS DE COMUNICACION
            cl.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            cl.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            cl.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            cl.setReuseAddress(reuseAddres);
            cl.setKeepAlive(keepAlive);
            cl.setSoTimeout(tempLectura);
            
            System.out.println("Conexion con servidor establecida.. lanzando FileChooser..");

            JFileChooser chooser;
            String choosertitle = new String();

            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(choosertitle);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            chooser.setAcceptAllFileFilterUsed(false);
            int r = chooser.showOpenDialog(null);

            String destino = "";
            String carPadre ="";
            //El chooser.getSelected file es el que hara referencia a la carpeta que subiremos
            if (r == JFileChooser.APPROVE_OPTION) {
                System.out.println("getCurrentDirectory(): "
                        + chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : "
                        + chooser.getSelectedFile());
                
                destino = chooser.getSelectedFile() + "";
                destino = destino.replaceAll("\\\\","\\\\\\\\");
                carPadre = destino;
                destino += ".zip";
                       
            } else {
                System.out.println("No Selection ");
            }
            
            comprimir(carPadre, destino);
            
            //Para este momento ya se creo el archivo zip cuya ruta esta en la variable destino
            File f = new File(destino);
            
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
            
            //borramos el archivo .zip que generamos para enviar al servidor
            f.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
    public static void menuSubirArchivos() throws IOException{
        int option = 0;
        
        while(option != 3){
            System.out.println("");
            
            System.out.println("1.- Subir archivos a mi repositorio remoto");
            System.out.println("2.- Subir una carpeta a mi repositorio remoto");
            System.out.println("3.- Salir al menu principal");
            
            System.out.println("Introduce el numero de la opcion a realizar: ");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String op = reader.readLine();
            option = Integer.parseInt(op);
            
            switch (option) {
                case 1:
                    sMenuServidor(3);
                    subirArchivosCarpetas();
                    break;
                case 2:
                    sMenuServidor(31);
                    subirCarpetas();
                    break;
                case 3:
                    
                    break;
                default:
                    System.out.println("Operación no reconocida, por favor intentelo de nuevo");
                    break;
            }
        }
    }

    public static void sMenuServidor(int opcionM) {
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
            Socket cl = new Socket(dir, pto);
            
            //PARAMETROS DE COMUNICACION
            cl.setTcpNoDelay(algoritmoNagle);// este es para activar o desactivar el algoritmo de nagle
            cl.setReceiveBufferSize(tamBufferLectura);// el tamaño del bufer receive del socket
            cl.setSendBufferSize(tamBufferEscritura); // el tamaño del buffer del send del socket
            cl.setReuseAddress(reuseAddres);
            cl.setKeepAlive(keepAlive);
            cl.setSoTimeout(tempLectura);
            

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
    
    public static void modificaParametros() throws IOException{
        int opt = 0;
        
        while(opt != 7 ){
            System.out.println("");
            System.out.println("Los parametros de comunicacion actuales son los siguientes: \n");
            System.out.println("[1] Algortimo de Nagle: " + (algoritmoNagle?"Activado":"Desactivado") );
            System.out.println("[2] Opcion Reuse Addres: " + (reuseAddres?"Activado":"Desactivado"));
            System.out.println("[3] Opcion Keep Alive: " + (keepAlive?"Activado":"Desactivado"));
            System.out.println("[4] Valor del temporizador de lectura: " + tempLectura);
            System.out.println("[5] Tamaño del buffer de lectura: " +tamBufferLectura);
            System.out.println("[6] Tamaño del buffer de escritura: " +tamBufferEscritura );
            System.out.println("[7] Salir al menu principal");
            
            System.out.println("Si desea modificar algun parametro teclee su numero:");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String op = reader.readLine();
            opt = Integer.parseInt(op);
            
            switch (opt) {
                case 1:
                    int valorNagle = 0;
                    System.out.println("Modificando el valor del algoritmo de Nagle");
                    System.out.println("-Para activarlo escribe 1");
                    System.out.println("-Para desactivarlo escribe 0");
                    System.out.println("Ingresa la opcion: ");
                    
                    BufferedReader readerOpt = new BufferedReader(new InputStreamReader(System.in));
                    valorNagle = Integer.parseInt(readerOpt.readLine());
                    
                    if(valorNagle == 0){
                        algoritmoNagle = false;
                    }else if(valorNagle == 1){
                        algoritmoNagle = true;
                    }
                        
                    
                    break;
                case 2:
                    
                    int reuseAddr = 0;
                    System.out.println("Modificando el valor para la opcion Reuse Addres");
                    System.out.println("-Para activarlo escribe 1");
                    System.out.println("-Para desactivarlo escribe 0");
                    System.out.println("Ingresa la opcion: ");
                    
                    BufferedReader readerReuse = new BufferedReader(new InputStreamReader(System.in));
                    reuseAddr = Integer.parseInt(readerReuse.readLine());
                    
                    if(reuseAddr == 0){
                        reuseAddres = false;
                    }else if(reuseAddr == 1){
                        reuseAddres = true;
                    }
                    
                    break;
                case 3:
                    
                    int keepAli = 0;
                    System.out.println("Modificando el valor para la opcion Keep Alive");
                    System.out.println("-Para activarlo escribe 1");
                    System.out.println("-Para desactivarlo escribe 0");
                    System.out.println("Ingresa la opcion: ");
                    
                    BufferedReader readerKeep = new BufferedReader(new InputStreamReader(System.in));
                    keepAli = Integer.parseInt(readerKeep.readLine());
                    
                    if(keepAli == 0){
                        keepAlive = false;
                    }else if(keepAli == 1){
                        keepAlive = true;
                    }
                    
                    break;
                case 4:
                   
                    System.out.println("Modificando el valor del temporizador de lectura");
                    System.out.println("Recuerde que el valor de 0 hace refrencia a un tiempo infinito");
                    System.out.println("Ingresa el nuevo valor para el temporizador de lectura: ");
                    
                    BufferedReader tempLet = new BufferedReader(new InputStreamReader(System.in));
                    tempLectura = Integer.parseInt(tempLet.readLine());
                    
                    
                    break;
                case 5:
                    
                    System.out.println("Modificando el tamaño del buffer de lectura");
                    System.out.println("Ingresa el nuevo valor para el tamaño del buffer: ");
                    
                    BufferedReader tamLecturaBuff = new BufferedReader(new InputStreamReader(System.in));
                    tamBufferLectura = Integer.parseInt(tamLecturaBuff.readLine());
                   
                    break;
                case 6:
                    
                    System.out.println("Modificando el tamaño del buffer de escritura");
                    System.out.println("Ingresa el nuevo valor para el tamaño del buffer: ");
                    
                    BufferedReader tamEscrituraBuff = new BufferedReader(new InputStreamReader(System.in));
                    tamBufferEscritura = Integer.parseInt(tamEscrituraBuff.readLine());
                    
                    break;
                case 7:
                    
                    break;
                default:
                    System.out.println("Opción no reconocida, por favor intentelo de nuevo");
                    break;
            }

        }
        
    }
    
    

    public static void main(String[] args) throws IOException {

        //Comenzaremos mostrando el menu de las opciones disponibles para el usuario
        int option = 0;
        while (option != 8) {
            System.out.println("");
            System.out.println("\033[0;1m" +"BIENVENIDO A TU REPOSITORIO DE ARCHIVOS");
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
                    menuSubirArchivos();
                    break;
                case 4:
                    menuDescarga();  
                    break;
                case 5:
                    eliminarArchivoRepoLocal("RepositorioCliente");
                    break;
                case 6:
                    sMenuServidor(option);
                    elimanarArchivoRepoServidor();
                    break;
                case 7:
                    modificaParametros();
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
