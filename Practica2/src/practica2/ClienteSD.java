/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 *
 * @author rodrigo
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
            System.out.println("Coordenadas i,j : " + coordenada);
            respuestas.put(w, coordenada);
            for (int i = xy.size() - 1; i >= 0; i--) {
                xy.remove(i);
            }

        }

    }

    public static void main(String[] args) throws IOException {
        //Generamos la matriz de size n

        int sizeMatriz = 16;
        String[][] Matriz = new String[sizeMatriz][sizeMatriz];
        boolean[][] MatrizE = new boolean[sizeMatriz][sizeMatriz];

        for (int i = 0; i < Matriz.length; i++) {
            for (int j = 0; j < Matriz.length; j++) {
                //char letraRad = (char) (rad.nextInt(26) + 'a');
                //Matriz[i][j] = Character.toString(letraRad);
                Matriz[i][j] = "-";
            }
        }
        //
        // Leermos las palabras del archivos y se insertan a una lista
        Path path = Paths.get("C:\\Users\\rodri\\Desktop\\SextoSemestre\\Redes\\Practicas\\Practica2\\Archivos\\palabras.txt");
        List<String> contentList = Files.readAllLines(path, StandardCharsets.UTF_8);
        System.out.println(contentList);
        //
        //Creamos un arreglo con las posiciones posibles de las palaras
        String[] posiciones = {"h", "v", "di", "dd"};
        // h = horizontal
        // v = vertical
        // di = diagonal a la izquierda
        // dd = diagonal a la derecha
        Hashtable respuestas = new Hashtable();
        GenerarMatriz(MatrizE, Matriz, posiciones, contentList, respuestas);
        ImprimirMatriz(Matriz);

    }

    /*
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
            String sm = br.readLine();
        } catch (Exception e) {
        }
     */
}
