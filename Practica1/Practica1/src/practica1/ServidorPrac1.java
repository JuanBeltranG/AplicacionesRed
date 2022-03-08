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
import static practica1.ClientePrac1.eliminarDirectorios;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ServidorPrac1 {
    
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
    
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
    
    public static void subirCarpetaDeCliente(ServerSocket s){
        try {
            
<<<<<<< HEAD
            s.setReuseAddress(true);
=======
>>>>>>> menuservidor
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
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde " + cl.getInetAddress() + ":" + cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
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
                System.out.println("Archivo recibido..");
                dos.close();
                dis.close();
                cl.close();

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
                
<<<<<<< HEAD
                return;
=======
>>>>>>> menuservidor

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
    
    public static void descargaArchivosServidor(ServerSocket s){
        try{
            String op = "";
            do{
               //enviamos al cliente la lista de todos los archivos que hay en el repo remoto
               consultaRepoServidor("RepositorioServidor",s);
               
               // obtenemos el numero de archivo que desea descargar el cliente
               Socket cl = s.accept();
               BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
               String archivoE = br1.readLine();
               br1.close();
               cl.close();
               
                System.out.println("el numero recibido fue:" +archivoE);
               // obtenemos una referencia de tipo file al archivo que se desea descargar
               File f = new File("");
               String ruta = f.getAbsolutePath();
               String rutaRepoLocal = ruta + "\\" + "RepositorioServidor" + "\\";
               int option = Integer.parseInt(archivoE);
                System.out.println("La opcion para la descarga fue" +option);
               
               File folder = new File(rutaRepoLocal);
                ArrayList<String> archivos = new ArrayList<String>();
                for (File fileEntry : folder.listFiles()) {
                    archivos.add(fileEntry.getName());
                }
               File archivoDescargar = new File(rutaRepoLocal + (archivos.get(option)));
               
               //Nuestro servidor se convertira momentaneamente en un cliente que le enviara datos a nuestro cliente
               int pto = 8001;
               String dir = "127.0.0.1";
               Socket c2 = new Socket(dir, pto);
               System.out.println("Conexion con cliente establecido, comenzando a mandar el archivo");
               
               String nombre = archivoDescargar.getName();
               String path = archivoDescargar.getAbsolutePath();
               long tam = archivoDescargar.length();
               DataOutputStream dos = new DataOutputStream(c2.getOutputStream());
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
                c2.close();
               
                
            }while(op.compareToIgnoreCase("s")== 0);
            
        
        }catch(Exception e){
            
        }
        
    }
    
    public static void descargaCarpetaServidor(ServerSocket s){
        try{
            String op = "";
            do{
               //enviamos al cliente la lista de todos los archivos que hay en el repo remoto
               consultaRepoServidor("RepositorioServidor",s);
               
               // obtenemos el numero de directorio que desea descargar el cliente
               Socket cl = s.accept();
               BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream(), "ISO-8859-1"));
               String archivoE = br1.readLine();
               br1.close();
               cl.close();
               
               //System.out.println("el numero recibido fue:" +archivoE);
               
               File f = new File("");
               String ruta = f.getAbsolutePath();
               String rutaRepoLocal = ruta + "\\" + "RepositorioServidor" + "\\";
               int option = Integer.parseInt(archivoE);
               
               File folder = new File(rutaRepoLocal);
                ArrayList<String> archivos = new ArrayList<String>();
                for (File fileEntry : folder.listFiles()) {
                    archivos.add(fileEntry.getName());
                }
               File archivoDescargar = new File(rutaRepoLocal + (archivos.get(option)));
               
              //Nuestro servidor se convertira momentaneamente en un cliente que le enviara datos a nuestro cliente
              int pto = 8001;
               String dir = "127.0.0.1";
               Socket c2 = new Socket(dir, pto);
               System.out.println("Conexion con cliente establecido, comenzando a mandar el directorio");
               
               String destino = "";
               String carPadre ="";
               
               destino = archivoDescargar.getAbsolutePath();
               destino = destino.replaceAll("\\\\","\\\\\\\\");
               carPadre = destino;
               destino += ".zip";
               
               comprimir(carPadre, destino);
               
               //Para este momento ya se creo el archivo zip cuya ruta esta en la variable destino
                f = new File(destino);
                
                String nombre = f.getName();
                String path = f.getAbsolutePath();
                long tam = f.length();
                System.out.println("Preparandose pare enviar archivo " + path + " de " + tam + " bytes\n\n");
                DataOutputStream dos = new DataOutputStream(c2.getOutputStream());
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
                c2.close();
            
            //borramos el archivo .zip que generamos para enviar al servidor
            f.delete();
              

            }while(op.compareToIgnoreCase("s")== 0);
            
        
        }catch(Exception e){
            
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
                    case "4":
                        //Este sera para descargar archivos del repo remoto
                        descargaArchivosServidor(s1);
                        break;
                    case "5":
                        //Este sera para descargar carpetas del repo remoto
                        descargaCarpetaServidor(s1);
                        break;
                    case "6":
                        eliminarRepoServidor("RepositorioServidor", s1);
                        //System.out.println("Archivo subido con exito");
                        break;
<<<<<<< HEAD
                        
                    case "31":
                        subirCarpetaDeCliente(s1);
=======
                    case "31":
                        subirCarpetaDeCliente(s1);
                        //System.out.println("Carpeta subida con exito");
>>>>>>> menuservidor
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
