/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 *
 * @author juan-
 */
public class ClienteSD {

    public static void ImprimirMatriz(String[][] Matriz) {
        for (int i = 0; i < Matriz.length; i++) {
            for (int j = 0; j < Matriz.length; j++) {
                System.out.print(Matriz[i][j] + "\t");
            }
            System.out.println("");
            System.out.println("");
        }
    }

    public static void main(String args[]) throws IOException {

        int pto = 8888;
        String host = "127.0.0.1";
        
        System.out.println("Se recomienda utilizar el puerto 8888 y la direccion Ip 127.0.0.1");

        System.out.println("Introduce el puerto : ");
        BufferedReader puertoReader = new BufferedReader(new InputStreamReader(System.in));
        pto = Integer.parseInt(puertoReader.readLine());
        
        
        System.out.println("Introduce la direccion IP : ");
        BufferedReader ipReader = new BufferedReader(new InputStreamReader(System.in));
        host = ipReader.readLine();
        
        
        //El estadoJuego tendra 2 posibles valores: activo y finalizado
        String estadoJuego = "";
        String[][] sopaLetras;
        List<String> palabras;
        int palabrasEncontradas = 0;

        String optRepetirJuego = "S";
        
        InetAddress dst = InetAddress.getByName(host);
        DatagramSocket cl = new DatagramSocket();

        do {
            
            //Primero establecemos conexion con el servidor para inicializar el juego por primera vez
            try {
                
                System.out.println("Inicializando juego, pidiendo datos de la sopa al servidor en " + host + ":" + pto);
                System.out.println("");

                ObjetoInicioJuego objetoConexionInicial = new ObjetoInicioJuego("Nuevo juego");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(objetoConexionInicial);
                oos.flush();
                byte[] b = baos.toByteArray();
                DatagramPacket p = new DatagramPacket(b, b.length, dst, pto);
                cl.send(p);

                //System.out.println("Objeto enviado con los datos:\nX:"+o.getX()+" Y:"+o.getY()+" Z:"+o.getZ());
                System.out.println("Esperando datos para iniciar el juego ...");
                DatagramPacket p1 = new DatagramPacket(new byte[65535], 65535);
                cl.receive(p1);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p1.getData()));
                ObjetoInicioJuego o1 = (ObjetoInicioJuego) ois.readObject();

                //Obtenemos del servidor los datos iniciales para comenzar a jugar 
                estadoJuego = o1.getEstado();
                sopaLetras = o1.getMatrizInicial();
                palabras = o1.getPalabras();
                palabrasEncontradas = o1.getPalabrasEncontradas();
                
                System.out.println("Sopa de letras");
                System.out.println("-------------------------------");
                System.out.println("Concepto: partes del cuerpo");
                System.out.println("-------------------------------");
                System.out.println("\n");

                ImprimirMatriz(sopaLetras);
                System.out.println("Las palabras que debes encontrar son las siguientes: ");
                System.out.println(palabras);

               //cl.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Para poder jugar sigue los siguientes pasos");
            System.out.println("1.- Escribe la palabra que has encontrado");
            System.out.println("2.- Ingresa la coordenada donde inicia la palabra");
            System.out.println("3.- Ingresa la coordenada donde termina la palabra");
            System.out.println("");
            System.out.println("Las cooordenadas deben ser ingresadas con el sig formato");
            System.out.println("Ejemplo: 14,15");
            System.out.println("\n");
            
            

            while (!estadoJuego.equals("Finalizado") && palabrasEncontradas < 3) {

                try {

                    System.out.println("Introduce la palabra : ");
                    BufferedReader palabraReader = new BufferedReader(new InputStreamReader(System.in));
                    String palabra = palabraReader.readLine();

                    System.out.println("Introduce la coordenada donde inicia : ");
                    BufferedReader coo1Reader = new BufferedReader(new InputStreamReader(System.in));
                    String coordenada1 = (String) coo1Reader.readLine();

                    //System.out.println("Coordenada1 " +coordenada1);
                    System.out.println("Introduce la coordenada donde termina : ");
                    BufferedReader coo2Reader = new BufferedReader(new InputStreamReader(System.in));
                    String coordenada2 = (String) coo2Reader.readLine();

                    //System.out.println("coordenada2 " + coordenada2);
                    ObjetoInicioJuego resp = new ObjetoInicioJuego("Activo", palabrasEncontradas, palabra, coordenada1, coordenada2);

                    //Comenzamos a realizar la conexion con el server para amndar la respuesta

                    //InetAddress dst = InetAddress.getByName(host);
                    //DatagramSocket cl = new DatagramSocket();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(resp);
                    oos.flush();
                    byte[] b = baos.toByteArray();
                    DatagramPacket p = new DatagramPacket(b, b.length, dst, pto);
                    cl.send(p);

                    //System.out.println("Esperando respuesta del servidor");
                    DatagramPacket p1 = new DatagramPacket(new byte[65535], 65535);
                    cl.receive(p1);

                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p1.getData()));
                    ObjetoInicioJuego o1 = (ObjetoInicioJuego) ois.readObject();

                    String respuestIntento = o1.getEstadoIntento();
                    estadoJuego = o1.getEstado();
                    palabras = o1.getPalabras();
                    palabrasEncontradas = o1.getPalabrasEncontradas();

                    System.out.println("El servidor indico que: " + respuestIntento);
                    System.out.println("Palabras encontradas hasta el momento: " + palabrasEncontradas);
                    System.out.println("");
                    System.out.println("---------------------------------------------------------------");
                    System.out.println("La lista de palabras que aun debes encontrar: ");
                    System.out.println(palabras);

                    if (estadoJuego.equals("Finalizado")) {
                        
                        System.out.println("------------------------------------------------");
                        System.out.println("FELICIDADES, COMPLETASTE LA SOPA DE LETRAS");
                        System.out.println("------------------------------------------------");
                        
                        System.out.println("El tiempo en el que se completo el juego es: " + o1.getTiempoJuegoActual());
                        System.out.println("El mejor tiempo hasta ahora es: " + o1.getTiempoRecord());
                        System.out.println("El peor tiempo hasta ahora es: " +o1.getPeorTiempo());

                        System.out.println("¿Desea jugar otra vez?   S/N");
                        BufferedReader repetirReader = new BufferedReader(new InputStreamReader(System.in));
                        optRepetirJuego = (String) repetirReader.readLine();

                        if (optRepetirJuego.compareToIgnoreCase("s") == 0) {
                            palabrasEncontradas = 0;

                        } else {
                            ObjetoInicioJuego finaljue = new ObjetoInicioJuego("Finalizacion juego");
                            baos = new ByteArrayOutputStream();
                            oos = new ObjectOutputStream(baos);
                            oos.writeObject(finaljue);
                            oos.flush();
                            b = baos.toByteArray();
                            p = new DatagramPacket(b, b.length, dst, pto);
                            cl.send(p);
                            
                            cl.close();
                            System.out.println("\nHasta la proxima");

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } while (optRepetirJuego.compareToIgnoreCase("s") == 0);

    }

}