import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

public class App {

    private static Celda[][] matrizCeldas;
    private static Boolean[][] matrizBooleanos;
    private static int celdasTotales;
    private static int turnosTotales;
    private static int celdasNotificadas;
    private static int dimensiones;
    private static int turnoActual = 0;
    

    public static void ActualizarMatriz(Celda[][] mC, Boolean[][] mB){
        for (int i = 0; i < dimensiones; i++){
            for (int j = 0; j < dimensiones; j++){
                Celda celdaEspecifica = mC[i][j];
                celdaEspecifica.setEstado(mB[i][j]);
            }
        }
    }

    public static Celda[][] getMatriz(){
        return matrizCeldas;
    }

    public static Boolean[][] getBoolean(){
        return matrizBooleanos;
    }

    public static void setBoolean(int fila, int columna, Boolean estado){
        matrizBooleanos[fila][columna] = estado;
    }

    public void NotificarActualizacion(){
        celdasNotificadas++;
    }

    public static void ImprimirMatrizXTurno(){
        String turno = String.valueOf(turnoActual);
        String respuesta = "Turno " + turno + " \n:";
        for (int i = 0; i < dimensiones; i++){
            String extra1 = "===============";
            respuesta = respuesta + extra1;
            for (int k = 3; k < dimensiones; k++){
                String extra2 = "=====";
                respuesta = respuesta +extra2;
            }
            String extra3 = "\n";
            respuesta = respuesta + extra3;
            for (int j = 0; j < dimensiones; j++){
                String extra4;
                if (matrizBooleanos[i][j] == true){
                    extra4 = "| * |";
                }
                else{
                    extra4 = "|   |";
                }
                respuesta = respuesta + extra4;
            }
            String extra5 = "\n";
            respuesta = respuesta + extra5;
        }
        String extra6 = "===============";
        respuesta = respuesta + extra6;
        for (int l = 3; l < dimensiones; l++){
            String extra7 = "=====";
            respuesta = respuesta +extra7;
        }
        String extra8 = "\n\n\n";
        respuesta = respuesta + extra8;
        System.out.println(respuesta);
    }

    public static void main(String[] args) throws Exception {
        int lineCount = -1;
        celdasNotificadas = 0;

        Scanner scannerTurnos = new Scanner(System.in);
        System.out.println("Ingrese el numero de etapas que turnos que desea considerar: ");
        turnosTotales = scannerTurnos.nextInt();
        scannerTurnos.close();

        CyclicBarrier barrera = new CyclicBarrier(1);

        try (FileReader fr = new FileReader("C1/src/test.txt")) {
            BufferedReader br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null){
                if (lineCount == -1){
                    dimensiones = Integer.parseInt(linea);
                    matrizCeldas = new Celda[dimensiones][dimensiones];
                    matrizBooleanos = new Boolean[dimensiones][dimensiones];
                    celdasTotales = dimensiones*dimensiones;
                    lineCount ++;
                }
                else{
                    barrera = new CyclicBarrier(celdasTotales);
                    String[] raw = linea.split(",");
                    ArrayList<Boolean> estadosOriginales = new ArrayList<Boolean>();
                    for (String r : raw){
                        if (r.equals("false")){
                            estadosOriginales.add(false);
                        }
                        else{
                            estadosOriginales.add(true);
                        }
                    }
                    for (int i = 0; i < estadosOriginales.size(); i++){
                        Boolean estadoSingular = estadosOriginales.get(i);
                        ArrayList<Integer> id = new ArrayList<Integer>();
                        id.add(lineCount);
                        id.add(i);
                        int vecinos;
                        if (lineCount == 0){
                            if (i == 0){
                                vecinos = 3;                              
                            }
                            if (i == dimensiones){
                                vecinos = 3;
                            }
                            else{
                                vecinos = 5;
                            }
                        }
                        else if (lineCount == dimensiones-1){
                            if (i == 0){
  
                                vecinos = 3;
                            }
                            else if (i == dimensiones-1){
                                vecinos = 3;
                            }
                            else{
                                vecinos = 5;
                            }
                        }
                        else{
                            if (i == 0){
                                vecinos = 5;
                            }
                            else if (i == dimensiones-1){
                                vecinos = 5;
                            }
                            else{
                                vecinos = 8;
                            }
                        }

                        Buzon buzonPropio = new Buzon(id, vecinos);

                        matrizCeldas[lineCount][i] = new Celda(id, buzonPropio, estadoSingular, barrera);
                        matrizCeldas[lineCount][i].start();
                        System.out.println(matrizCeldas[lineCount][i]);
                    }
                    lineCount++;
                }
            }
            
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        while (turnoActual < turnosTotales){
            barrera.await();
            if (celdasNotificadas == celdasTotales){
                celdasNotificadas = 0;
                ImprimirMatrizXTurno();
                ActualizarMatriz(matrizCeldas, matrizBooleanos);
                turnoActual ++;
            }
            turnoActual++;
        }        
    }
}
