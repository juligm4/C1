import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

public class App {

    /* Los atributos matriz celdas y matriz booleanos hacen a una matriz que funcionara para el caso de la matriz celdas 
     * como una forma de almacenarlas y de identificar facil a sus vecinos y sus respectivos buzones, mientras que matriz boolanos
     * se usa para almacenar los datos de como se verían los valores de la matriz en el turno que está por venir. Su utlidad es 
     * se centra netamente en que a partir de esta estructura se reescriben los valores de de cada una de las celdas de matriz de 
     * celdas al pasar el turno y a través de la lectura de la misma se ejecuta la impresión o output de cada uno de los turnos.*/
    private static Celda[][] matrizCeldas;
    private static Boolean[][] matrizBooleanos;
    private static int celdasTotales;
    private static int turnosTotales;
    private static int celdasNotificadas;
    private static int dimensiones;
    private static int turnoActual = 0;
    
    /* Este metodo sobreescribe los estados de la matriz de celdas a partir de lo que le indica la matriz de booleanos. 
     * (Se ejecuta al finalizar un turno)*/
    public static void ActualizarMatriz(Celda[][] mC, Boolean[][] mB, CyclicBarrier barrera2){
        for (int i = 0; i < dimensiones; i++){
            for (int j = 0; j < dimensiones; j++){
                Boolean estado = mB[i][j];
                ArrayList <Integer> ids = new ArrayList<Integer>(2);
                ids.add(i);
                ids.add(j);
                int vecinos;
                if (i == 0){
                    if (j == 0){
                        vecinos = 3;                              
                    }
                    if (j == dimensiones-1){
                        vecinos = 3;
                    }
                    else{
                        vecinos = 5;
                    }
                }
                else if (i == dimensiones-1){
                    if (j == 0){

                        vecinos = 3;
                    }
                    else if (j == dimensiones-1){
                        vecinos = 3;
                    }
                    else{
                        vecinos = 5;
                    }
                }
                else{
                    if (j == 0){
                        vecinos = 5;
                    }
                    else if (j == dimensiones-1){
                        vecinos = 5;
                    }
                    else{
                        vecinos = 8;
                    }
                }
                int tamano = i;
                Buzon buzonPropio = new Buzon(tamano, vecinos);
                matrizCeldas[i][j] = new Celda(ids, buzonPropio, estado, barrera2);
                
            }
        }
        for (int i = 0; i < dimensiones; i++){
            for (int j = 0; j < dimensiones; j++){
                matrizCeldas[i][j].start();
            }
        }
    }

    public static Celda[][] getMatriz(){
        return matrizCeldas;
    }

    public static Celda getCelda(int fila, int columna){
        return matrizCeldas[fila][columna];
    }

    public static Boolean[][] getBoolean(){
        return matrizBooleanos;
    }

    public static void setBoolean(int fila, int columna, Boolean estado){
        matrizBooleanos[fila][columna] = estado;
    }

    public static void NotificarActualizacion(){
        celdasNotificadas++;
    }

    public static int getCeldasNotificadas(){
        return celdasNotificadas;
    }

    public static int getTurnosTotales(){
        return turnosTotales;
    }

    public static int getTurnoActual(){
        return turnoActual;
    }

    /* A partir de la matriz booleana esta función imprime una cuadricula parecida a la del output de ejemplo que viene
     * en la propia guía del caso. Es importante tener presente que ya está configurada para que crezca en función de n.
     * (Siempre y cuando n >= 3 [segun la profe se esperaba que esa condición se cumpliera siempre.])*/
    public static void ImprimirMatrizXTurno(){
        String turno = String.valueOf(turnoActual);
        String respuesta = "Turno " + turno + " \n:";
        for (int i = 0; i < dimensiones; i++){
            String extra1 = "===============";
            respuesta = respuesta + extra1;
            for (int k = 3; k < dimensiones; k++){
                String extra2 = "=====";
                respuesta = respuesta + extra2;
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
            respuesta = respuesta + extra7;
        }
        String extra8 = "\n\n\n";
        respuesta = respuesta + extra8;
        System.out.println(respuesta);
    }

    public static void main(String[] args) throws Exception {
        int lineCount = 0;
        celdasNotificadas = 0;

        /*Se trata de un input que le pregunta al usuario el número de turnos que desea tener en cuenta. */
        Scanner scannerTurnos = new Scanner(System.in);
        System.out.println("Ingrese el numero de etapas que turnos que desea considerar: ");
        turnosTotales = scannerTurnos.nextInt();
        scannerTurnos.close();

        /* Inicialización de la barrera que se usará por todo el programa para confirmar el fin de los threads */
        //CyclicBarrier barrera = new CyclicBarrier(9);
        

        /*Acá se ejecuta la lectura del archivo txt que está ubicado en el mismo lugar que las demás clases. */
        try (FileReader fr = new FileReader("src\\test.txt")) {
            BufferedReader br = new BufferedReader(fr);
            String linea=br.readLine();
            dimensiones = Integer.parseInt(linea);
            matrizCeldas = new Celda[dimensiones][dimensiones];
            matrizBooleanos = new Boolean[dimensiones][dimensiones];
            celdasTotales = dimensiones*dimensiones;

            CyclicBarrier barrera2 = new CyclicBarrier(9);

            while((linea=br.readLine())!=null){
                /* Para el caso de la primera linea (que se establece cuando linecount == -1) este crea ambas matrices de acuerdo
                * al tamaño que es indicado en este primer renglón.*/
                
                /*Por otro lado, va a leer los valores de entrada de cada fila y se los añade a un Array de booleanos */
                
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
                    /* Aquí itera obre ese array y se encarga de crear un ids para cada celda que consistirá en un array
                     * de la forma ids = [linea, columna] y adicionalmente en función de la localización que esta celda ocupe 
                     * dentro de la matriz le asigna la cantidad de vecinos que tiene adyacentes a la misma*/
                    for (int p = 0; p < dimensiones; p++){
                        Boolean estadoSingular = estadosOriginales.get(p);
                        ArrayList<Integer> ids = new ArrayList<Integer>();
                        ids.add(lineCount);
                        ids.add(p);
                        int vecinos;
                        if (lineCount == 0){
                            if (p == 0){
                                vecinos = 3;                              
                            }
                            if (p == dimensiones-1){
                                vecinos = 3;
                            }
                            else{
                                vecinos = 5;
                            }
                        }
                        else if (lineCount == dimensiones-1){
                            if (p == 0){
  
                                vecinos = 3;
                            }
                            else if (p == dimensiones-1){
                                vecinos = 3;
                            }
                            else{
                                vecinos = 5;
                            }
                        }
                        else{
                            if (p == 0){
                                vecinos = 5;
                            }
                            else if (p == dimensiones-1){
                                vecinos = 5;
                            }
                            else{
                                vecinos = 8;
                            }
                        }

                        /* Para terminos practicos el buzón también guarda el ids de la celda a la que pertenece y
                         * el número de vecinos que tiene la celda a la que él pertenece. */
                        int tamano = lineCount;
                        Buzon buzonPropio = new Buzon(tamano, vecinos);


                        /* Aquí se almacena a la celda a modo de threat dentro de la matrzi de celdas y posteriormente se
                         * inicializa a modo de threat llamandolo desde la ubicación en la que quedo guardado. */
                        matrizCeldas[lineCount][p] = new Celda(ids, buzonPropio, estadoSingular, barrera2);
                    }
                    lineCount++;
            }
            
            for (int i = 0; i < matrizCeldas.length; i++) {
                for (int j = 0; j < matrizCeldas[i].length; j++) {
                    matrizCeldas[i][j].start();
                }
            }
            
            
            /*Esta estructura propone el salto de turnos tras haber finalizado con la creación de las matrices iniciales 
            * (está a modo de prueba pues no he conseguido que corra hasta aquí pero usa la barrera para asegurarse que la 
            * la matriz booleana del siguiente turno ya haya actualizada por cada uno de los threads). */
            while (turnoActual < turnosTotales){
                Thread.currentThread().sleep(500);
                ImprimirMatrizXTurno();
                ActualizarMatriz(matrizCeldas, matrizBooleanos, barrera2);
                turnoActual++;
                
            }   
            
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }     
    }
}
