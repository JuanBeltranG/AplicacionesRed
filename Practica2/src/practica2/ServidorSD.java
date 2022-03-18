/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Double.max;
import static java.lang.Double.min;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;


/**
 *
 * @author juan-
 */
public class ServidorSD {
    
    
    public static String seleccionaPalabrasRandom(){
        
        
        int min_val = 1;
        int max_val = 5;
        
        Random ran = new Random();
        int x = ran.nextInt(max_val) + min_val;
        
        String rutaPlabras = "Archivos\\palabras";
        rutaPlabras += x;
        rutaPlabras += ".txt";
        
        return rutaPlabras;
        
    }
    
    public static void ImprimirMatriz(String[][] Matriz) {
        for (int i = 0; i < Matriz.length; i++) {
            for (int j = 0; j < Matriz.length; j++) {
                System.out.print(Matriz[i][j] + "\t");
            }
            System.out.println("");
            System.out.println("");
        }
    }
    
    public static void ImprimirMatrizE(boolean[][] MatrizE) {
        for (int i = 0; i < MatrizE.length; i++) {
            for (int j = 0; j < MatrizE.length; j++) {
                System.out.print(MatrizE[i][j] + "\t");
            }
            System.out.println("");
            System.out.println("");
        }
    }
    
    public static void GenerarMatriz(boolean[][] MatrizE, String[][] Matriz, String[] posiciones, List<String> contentList, Hashtable respuestas) {
        int x = 0;
        Random rad = new Random();
        ArrayList<Integer> xy = new ArrayList();
        int posicionPo = 0;
        int posicion = 0;
        for (int w = 0; w < contentList.size(); w++) {
            do {
                if (posicion == 3) {
                    posicion = 0;
                }
                posicionPo = 0;
                int columR = rad.nextInt(Matriz.length);
                int filaR = rad.nextInt(Matriz.length);
                int verticalp = rad.nextInt(Matriz.length);;
                int horizontalp = rad.nextInt(Matriz.length);;
                int diagonalC = rad.nextInt(Matriz.length);
                int diagonalF = rad.nextInt(Matriz.length);
                int inicioP = 0;
                int finalP = 1;
                int aumentoC = 0;
                int aumentoF = 0;
                int restanteC = 0;
                int restanteF = 0;
                int filaA = 0;
                int columA = 0;
                //Correcciones por si la palabra se imprime incompleta
                //se suman o retan posiciones para que se impriman todas la letras
                //que la conforman
                if (posicion == 0) {
                    columA = rad.nextInt(Matriz.length / 2);
                } else if (posicion == 1) {
                    filaA = rad.nextInt(Matriz.length / 2);
                } else if (posicion == 2) {

                    restanteC = (Matriz.length - 1) - (diagonalC);
                    restanteF = (Matriz.length) - (diagonalF);
                    if (restanteF <= contentList.get(w).length()) {
                        aumentoF = (contentList.get(w).length() - restanteF);
                        diagonalF = diagonalF - aumentoF;
                    }
                    if (restanteC >= contentList.get(w).length()) {
                        aumentoC = (contentList.get(w).length() - diagonalC) - 1;
                        diagonalC = diagonalC + aumentoC;
                    }
                } else {
                    restanteC = (Matriz.length) - (diagonalC);
                    restanteF = (Matriz.length) - (diagonalF);
                    if (restanteF <= contentList.get(w).length()) {
                        aumentoF = (contentList.get(w).length() - restanteF);
                        diagonalF = diagonalF - aumentoF;
                    }
                    if (restanteC <= contentList.get(w).length()) {
                        aumentoC = (contentList.get(w).length() - restanteC);
                        diagonalC = diagonalC - aumentoC;
                    }

                }

                for (int i = 0; i < Matriz.length; i++) {
                    for (int j = 0; j < Matriz.length; j++) {
                        if (finalP <= contentList.get(w).length()) {
                            if (posicion == 0) {
                                if (i == horizontalp) {
                                    if (j > Matriz.length / 2) {
                                        //Matriz[filaR][j + columA] = contentList.get(2).substring(inicioP, finalP);
                                        if (MatrizE[filaR][j] == false) {
                                            MatrizE[filaR][j] = true;
                                            xy.add(filaR);
                                            xy.add(j);
                                            posicionPo++;
                                        }
                                    } else {
                                        //Matriz[filaR][j + columA] = contentList.get(2).substring(inicioP, finalP);
                                        if (MatrizE[filaR][j + columA] == false) {
                                            MatrizE[filaR][j + columA] = true;
                                            xy.add(filaR);
                                            xy.add(j + columA);
                                            posicionPo++;
                                        }
                                    }
                                    inicioP++;
                                    finalP++;
                                }
                            } else if (posicion == 1) {
                                if (j == verticalp) {
                                    if (i > Matriz.length / 2) {
                                        //Matriz[i][columR] = contentList.get(2).substring(inicioP, finalP);
                                        if (MatrizE[i][columR] == false) {
                                            MatrizE[i][columR] = true;
                                            posicionPo++;
                                            xy.add(i);
                                            xy.add(columR);
                                        }
                                    } else {
                                        //Matriz[i + filaA][columR] = contentList.get(2).substring(inicioP, finalP);
                                        if (MatrizE[i + filaA][columR] == false) {
                                            MatrizE[i + filaA][columR] = true;
                                            xy.add(i + filaA);
                                            xy.add(columR);
                                            posicionPo++;
                                        }

                                    }
                                    inicioP++;
                                    finalP++;
                                }
                            } else if (posicion == 2) {

                                if (i == diagonalF && j == diagonalC) {
                                    //Matriz[i][j] = contentList.get(2).substring(inicioP, finalP);
                                    if (MatrizE[i][j] == false) {
                                        MatrizE[i][j] = true;
                                        xy.add(i);
                                        xy.add(j);
                                        posicionPo++;
                                    }
                                    diagonalC--;
                                    diagonalF++;
                                    inicioP++;
                                    finalP++;
                                }
                            } else {
                                if (i == diagonalF && j == diagonalC) {
                                    //Matriz[i][j] = contentList.get(2).substring(inicioP, finalP);
                                    if (MatrizE[i][j] == false) {
                                        MatrizE[i][j] = true;
                                        xy.add(i);
                                        xy.add(j);
                                        posicionPo++;
                                    }
                                    diagonalC++;
                                    diagonalF++;
                                    inicioP++;
                                    finalP++;
                                }
                            }
                        }
                    }
                }
                x++;
            } while (posicionPo < contentList.get(w).length());
            posicion++;
            int inicioP = 0;
            int finalP = 1;
            int inicioPR = contentList.get(w).length();
            int finalPR = contentList.get(w).length() - 1;
            int orientacion = rad.nextInt(2);
            String coordenada = "";
            if (orientacion == 1) {
                for (int t = ((xy.size()) - (contentList.get(w).length() * 2)); inicioP < contentList.get(w).length(); t += 2) {
                    if (finalP <= contentList.get(w).length()) {
                        Matriz[xy.get(t)][xy.get(t + 1)] = contentList.get(w).substring(finalPR, inicioPR);
                        if (inicioP == 0) {
                            coordenada = xy.get(t) + "," + xy.get(t + 1) + "";
                        }
                        inicioP++;
                        finalP++;
                        if (inicioP == contentList.get(w).length()) {
                            coordenada = coordenada + "-" + xy.get(t) + "," + xy.get(t + 1) + "";
                        }
                        inicioPR--;
                        finalPR--;
                    }
                }

            } else {
                for (int t = ((xy.size()) - (contentList.get(w).length() * 2)); inicioP < contentList.get(w).length(); t += 2) {
                    if (finalP <= contentList.get(w).length()) {
                        Matriz[xy.get(t)][xy.get(t + 1)] = contentList.get(w).substring(inicioP, finalP);
                        if (inicioP == 0) {
                            coordenada = xy.get(t) + "," + xy.get(t + 1) + "";
                        }
                        inicioP++;
                        finalP++;
                        if (inicioP == contentList.get(w).length()) {
                            coordenada = coordenada + "-" + xy.get(t) + "," + xy.get(t + 1) + "";
                        }
                    }
                }
            }
            //System.out.println("Coordenadas i,j : " + coordenada);
            respuestas.put(contentList.get(w), coordenada);
            for (int i = xy.size() - 1; i >= 0; i--) {
                xy.remove(i);
            }

        }

    }
    
    
    
    
    
    public static void main(String args[]){
        
        int sizeMatriz = 16;
        int numAciertos = 0;
        String[][] Matriz = new String[sizeMatriz][sizeMatriz];
        boolean[][] MatrizE = new boolean[sizeMatriz][sizeMatriz];
        Hashtable respuestas = new Hashtable();
        List<String> contentList = null;
        double tiempoRecord = Double.MAX_VALUE;
        double peorTiempo = 0;
        
        /*long inicio = System.currentTimeMillis();
        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio)/1000);    
        System.out.println(tiempo +" segundos");*/
        
        long inicio = 0;
        long fin = 0;
        
        
        try{
           
            DatagramSocket s = new DatagramSocket(8888);
            s.setReuseAddress(true);
            System.out.println("Servidor iniciado, esperando cliente para inicializar un juego..");
            
            while(true){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                s.receive(p);
                
                //con el metodo connect nos aseguramos que solo reciba datgramas de nuestro cliente que se conecto primero
                s.connect(p.getAddress(), p.getPort());
                
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
                ObjetoInicioJuego o = (ObjetoInicioJuego)ois.readObject();
                System.out.println("Solicitud recibida del cliente: " +o.getEstado());
                
                if(o.getEstado().equals("Finalizacion juego")){
                    s.disconnect();    
                }
                
                //Si este if es verdadero significara que el cliente esta inicializando el juego por primera vez
                if(o.getEstado().equals("Nuevo juego")){
                    
                 numAciertos = 0;
                 respuestas.clear();
                    
                 //Inicializamos la variable para comenzar a contar el tiempo en el que se inicio el juego
                 inicio = System.currentTimeMillis();
                    
                //Comenzamos a inicializar todos los parametros de la sopa de letras
                    
                    //1- Inicializamos la matriz que contendra a la sopa de letras
                    for (int i = 0; i < Matriz.length; i++) {
                        for (int j = 0; j < Matriz.length; j++) {
                            //char letraRad = (char) (rad.nextInt(26) + 'a');
                            //Matriz[i][j] = Character.toString(letraRad);
                            Matriz[i][j] = "-";
                        }
                    }
                    
                    // Leermos las palabras del archivos y se insertan a una lista
                    String ruta = seleccionaPalabrasRandom();
                    Path path = Paths.get(ruta);
                    contentList = Files.readAllLines(path, StandardCharsets.UTF_8);
                    
                    //Creamos un arreglo con las posiciones posibles de las palaras
                    String[] posiciones = {"h", "v", "di", "dd"};
                    // h = horizontal
                    // v = vertical
                    // di = diagonal a la izquierda
                    // dd = diagonal a la derecha
                    
                    GenerarMatriz(MatrizE, Matriz, posiciones, contentList, respuestas);
                    
                    
                    ObjetoInicioJuego o1 = new ObjetoInicioJuego(Matriz,contentList,"Activo",0);
                    ByteArrayOutputStream baos= new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(o1);
                    oos.flush();
                    byte[] b = baos.toByteArray();
                    DatagramPacket p1 = new DatagramPacket(b,b.length,p.getAddress(),p.getPort());
                    s.send(p1);
                    System.out.println("Respuesta del servidor enviada"); 
                    
                    System.out.println("Respuestas del juego: ");

                    Enumeration llaves = respuestas.keys();
                    while (llaves.hasMoreElements()) {
                        String currKey = (String) llaves.nextElement();
                      System.out.println("Palabla:  " + currKey +"|| Coordenadas:  "+ respuestas.get(currKey));
                    }
                    
                        
                }
                
                //////////////////////////////////////////////////////////////////
                //Si se alzanza esta seccion significa que el juego ya esta iniciado
                if(o.getEstado().equals("Activo")){
                    
                    
                    
                    String palabraIntento = o.getIntentoPalabra();
                    String coordenada1 = o.getCoo1();
                    String coordenada2 = o.getCoo2();
                    
                    //Variables para devolver respuesta al cliente
                    String estadoJuego = "";
                    String estadoIntento = "";
                    
                    ObjetoInicioJuego respCliente = null ;
                    
                    if(!respuestas.containsKey(palabraIntento)){
                        //Entrara al if si la palabra no se encuentra en la tabla
                        System.out.println("No se encontro la palabra");
                       
                        estadoJuego = "Activo";
                        estadoIntento = "Palabra no encontrada";
                        respCliente = new ObjetoInicioJuego(contentList,estadoJuego,numAciertos,estadoIntento);
                        
                    }else{
                        //Entrara aqui en caso de que la palabra si se encuentre
                        
                        String val = (String) respuestas.get(palabraIntento);
                        String coor1 = val.substring(0,val.indexOf("-"));
                        String coor2 = val.substring(val.indexOf("-")+1,val.length());
                        
                        System.out.println("Se encontro la palabra");
                        if(coor1.equals(coordenada1)|| coor1.equals(coordenada2)){
                            if(coor2.equals(coordenada1) || coor2.equals(coordenada2)){
                                //si entramos aqui significa que si existe la palabra y las coordenadas estaban bien
                                System.out.println("Las coordenadas estan bien");
                                //aumentamos el contador de las palabras encontradas
                               numAciertos++;
                               //quitamos la palabra encontrada de la lista inicial
                               contentList.remove(palabraIntento);
                              
                               

                               if(numAciertos < 3){
                                   //si entra aqui significa que el juego aun no termina
                                    estadoJuego = "Activo";
                                    estadoIntento = "Palabra encontrada exitosamente";
                                    respCliente = new ObjetoInicioJuego(contentList,estadoJuego,numAciertos,estadoIntento);
                               }else if(numAciertos == 3){
                                   //si entramos aqui significa que ya encontro todas las palabras y termina el juego
                                  
                                   estadoJuego = "Finalizado";
                                    estadoIntento = "Palabra encontrada exitosamente";
                                    
                                    fin = System.currentTimeMillis();
                                    
                                    double tiempo = (double) ((fin - inicio)/1000);    
                                    System.out.println(tiempo +" segundos");
                                     
                                    tiempoRecord = min(tiempo, tiempoRecord);
                                    peorTiempo = max(tiempo,peorTiempo);
                                     
                                    respCliente = new ObjetoInicioJuego(contentList,estadoJuego,numAciertos,estadoIntento,tiempoRecord, tiempo, peorTiempo);
                                    
                                    //ACORDARSE DE REINICIAR LAS VARIABLES
                                    
                                    
                                    
                                    
                               }
                                
                            }
                        }else{
                            //si entra aqui significa que la palabra si existia pero no puso bien las coordenadas
                            System.out.println("Se encontro la palabra pero coordenadas mal");
                            
                            estadoJuego = "Activo";
                            estadoIntento = "Coordenadas erroneas";
                            respCliente = new ObjetoInicioJuego(contentList,estadoJuego,numAciertos,estadoIntento);
                        }
                        
                    }
                    
                    //Procedemos a devolver el objeto al jugador
                   
                    ByteArrayOutputStream baos= new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(respCliente);
                    oos.flush();
                    byte[] b = baos.toByteArray();
                    DatagramPacket p1 = new DatagramPacket(b,b.length,p.getAddress(),p.getPort());
                    s.send(p1);
                    System.out.println("Respuesta del servidor enviada"); 
                    System.out.println("-------------------------------- \n");
                    
                   
                    
                }
                

                
                
            }
            
            
            
            
            
            
            
            
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
    }
    
}
