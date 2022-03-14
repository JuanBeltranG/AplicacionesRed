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

    public static void main(String[] args) throws IOException {
        //Generamos la matriz de size n

        int sizeMatriz = 16;
        String[][] Matriz = new String[sizeMatriz][sizeMatriz];
        Random rad = new Random();

        for (int i = 0; i < Matriz.length; i++) {
            for (int j = 0; j < Matriz.length; j++) {
                //char letraRad = (char) (rad.nextInt(26) + 'a');
                //Matriz[i][j] = Character.toString(letraRad);
                Matriz[i][j] = " ";
            }
        }
        //
        // Leermos las palabras del archivos y se insertan a una lista
        Path path = Paths.get("C:\\Users\\rodri\\Desktop\\SextoSemestre\\Redes\\Practica2\\Archivos\\palabras.txt");
        List<String> contentList = Files.readAllLines(path, StandardCharsets.UTF_8);
        System.out.println(contentList);
        //
        //Creamos un arreglo con las posiciones posibles de las palaras
        String[] posiciones = {"h", "v", "di", "dd"};
        // h = horizontal
        // v = vertical
        // di = diagonal a la izquierda
        // dd = diagonal a la derecha
        for (int k = 0; k < 15; k++) {
            int columR = rad.nextInt(Matriz.length);
            int filaR = rad.nextInt(Matriz.length);
            int posicion = rad.nextInt(posiciones.length);
            int verticalp = rad.nextInt(Matriz.length);;
            int horizontalp = rad.nextInt(Matriz.length);;
            int diagonalC = rad.nextInt(Matriz.length) - 1;;
            int diagonalF = rad.nextInt(Matriz.length) - 1;;
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
                if (restanteF <= contentList.get(2).length()) {
                    aumentoF = (contentList.get(2).length() - restanteF);
                    diagonalF = diagonalF - aumentoF;
                }
                if (restanteC >= contentList.get(2).length()) {
                    aumentoC = (contentList.get(2).length() - diagonalC) - 1;
                    diagonalC = diagonalC + aumentoC;
                }
            } else {
                restanteC = (Matriz.length) - (diagonalC);
                restanteF = (Matriz.length) - (diagonalF);
                if (restanteF <= contentList.get(2).length()) {
                    aumentoF = (contentList.get(2).length() - restanteF);
                    diagonalF = diagonalF - aumentoF;
                }
                if (restanteC <= contentList.get(2).length()) {
                    aumentoC = (contentList.get(2).length() - restanteC);
                    diagonalC = diagonalC - aumentoC;
                }

            }

            for (int i = 0; i < Matriz.length; i++) {
                for (int j = 0; j < Matriz.length; j++) {
                    if (finalP <= contentList.get(2).length()) {
                        if (posicion == 0) {
                            if (i == horizontalp) {
                                Matriz[filaR][j + columA] = "##";
                                inicioP++;
                                finalP++;
                            }
                        } else if (posicion == 1) {
                            if (j == verticalp) {
                                Matriz[i + filaA][columR] = "##";
                                inicioP++;
                                finalP++;
                            }
                        } else if (posicion == 2) {

                            if (i == diagonalF && j == diagonalC) {
                                Matriz[i][j] = "##";
                                diagonalC--;
                                diagonalF++;
                                inicioP++;
                                finalP++;
                            }
                        } else {
                            if (i == diagonalF && j == diagonalC) {
                                //Matriz[i][j] = contentList.get(0).substring(inicioP, finalP);
                                Matriz[i][j] = "##";
                                diagonalC++;
                                diagonalF++;
                                inicioP++;
                                finalP++;
                            }
                        }
                    }
                }
            }
        }
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
