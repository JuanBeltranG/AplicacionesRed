/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author juan-
 */
public class ObjetoInicioJuego implements Serializable {
    
    String [][] MatrizInicial;
    List<String> Palabras;
    String Estado;
    int palabrasEncontradas;


    String intentoPalabra;
    String coo1;
    String coo2;
    
    String estadoIntento;
    
    double tiempoRecord;
    double tiempoJuegoActual;
    double peorTiempo;

    
    
    public ObjetoInicioJuego(List<String> Palabras, String Estado, int palabrasEncontradas, String estadoIntento, double tiempoRecord, double tiempoJuegoActual, double peorTiempo) {
        this.Palabras = Palabras;
        this.Estado = Estado;
        this.palabrasEncontradas = palabrasEncontradas;
        this.estadoIntento = estadoIntento;
        this.tiempoRecord = tiempoRecord;
        this.tiempoJuegoActual = tiempoJuegoActual;
        this.peorTiempo = peorTiempo;
    }

    
    public ObjetoInicioJuego(List<String> Palabras, String Estado, int palabrasEncontradas, String estadoIntento, double tiempoRecord, double tiempoJuegoActual) {
        this.Palabras = Palabras;
        this.Estado = Estado;
        this.palabrasEncontradas = palabrasEncontradas;
        this.estadoIntento = estadoIntento;
        this.tiempoRecord = tiempoRecord;
        this.tiempoJuegoActual = tiempoJuegoActual;
    }



    public ObjetoInicioJuego(List<String> Palabras, String Estado, int palabrasEncontradas, String estadoIntento) {
        this.Palabras = Palabras;
        this.Estado = Estado;
        this.palabrasEncontradas = palabrasEncontradas;
        this.estadoIntento = estadoIntento;
    }


    public ObjetoInicioJuego(String Estado, String intentoPalabra, String coo1, String coo2) {
        this.Estado = Estado;
        this.intentoPalabra = intentoPalabra;
        this.coo1 = coo1;
        this.coo2 = coo2;
    }

    public ObjetoInicioJuego(String Estado, int palabrasEncontradas, String intentoPalabra, String coo1, String coo2) {
        this.Estado = Estado;
        this.palabrasEncontradas = palabrasEncontradas;
        this.intentoPalabra = intentoPalabra;
        this.coo1 = coo1;
        this.coo2 = coo2;
    }
    
    public ObjetoInicioJuego(String[][] MatrizInicial, List<String> Palabras, String Estado) {
        this.MatrizInicial = MatrizInicial;
        this.Palabras = Palabras;
        this.Estado = Estado;
    }

    public ObjetoInicioJuego(String Estado) {
        this.Estado = Estado;
    }

    public ObjetoInicioJuego(String[][] MatrizInicial, List<String> Palabras, String Estado, int palabrasEncontradas) {
        this.MatrizInicial = MatrizInicial;
        this.Palabras = Palabras;
        this.Estado = Estado;
        this.palabrasEncontradas = palabrasEncontradas;
    }
    
    
    
    
    
    
    public String getEstadoIntento() {
        return estadoIntento;
    }

    public void setEstadoIntento(String estadoIntento) {
        this.estadoIntento = estadoIntento;
    }

    public int getPalabrasEncontradas() {
        return palabrasEncontradas;
    }

    public void setPalabrasEncontradas(int palabrasEncontradas) {
        this.palabrasEncontradas = palabrasEncontradas;
    }
    

    public String[][] getMatrizInicial() {
        return MatrizInicial;
    }

    public void setMatrizInicial(String[][] MatrizInicial) {
        this.MatrizInicial = MatrizInicial;
    }

    public List<String> getPalabras() {
        return Palabras;
    }

    public void setPalabras(List<String> Palabras) {
        this.Palabras = Palabras;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }
    
    public String getIntentoPalabra() {
        return intentoPalabra;
    }

    public void setIntentoPalabra(String intentoPalabra) {
        this.intentoPalabra = intentoPalabra;
    }

    public String getCoo1() {
        return coo1;
    }

    public void setCoo1(String coo1) {
        this.coo1 = coo1;
    }

    public String getCoo2() {
        return coo2;
    }

    public void setCoo2(String coo2) {
        this.coo2 = coo2;
    }

    public double getTiempoRecord() {
        return tiempoRecord;
    }

    public void setTiempoRecord(double tiempoRecord) {
        this.tiempoRecord = tiempoRecord;
    }

    public double getTiempoJuegoActual() {
        return tiempoJuegoActual;
    }

    public void setTiempoJuegoActual(double tiempoJuegoActual) {
        this.tiempoJuegoActual = tiempoJuegoActual;
    }
    
    public double getPeorTiempo() {
        return peorTiempo;
    }

    public void setPeorTiempo(double peorTiempo) {
        this.peorTiempo = peorTiempo;
    }
    
    
    
    
}
