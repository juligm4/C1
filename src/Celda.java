import java.util.*;
import java.util.concurrent.CyclicBarrier;;

public class Celda extends Thread{
    /* Aca se usa la barrera creada en la app a modo de control para ciertas partes de la ejecución. */
    private ArrayList<Integer> ids; 
    private Buzon buzonPropio;
    private Boolean estado;
    private CyclicBarrier barrera;

    public Celda (ArrayList<Integer> ids, Buzon buzonPropio, Boolean estado, CyclicBarrier barrera){
        this.ids = ids;
        this.buzonPropio = buzonPropio;
        this.estado = estado;
        this.barrera = barrera;
    }

    public void run (){

        while (App.getTurnoActual() < App.getTurnosTotales()){
            Celda[][] matriz = App.getMatriz();
            int fila = ids.get(0);
            int columna = ids.get(1);
            /* Lo primero que hace cada threat es averiguar en qué parte de la matriz está ubicado y a partir de eso se definirá
            * un caso espécifico en función de dicha localización (en total hay 9 casos posibles). */
            if (fila == 0){
                if (columna == 0){
                    NotificarVecinos(fila, columna, 1, estado);
                }
                else if (columna == (matriz.length)-1){
                    NotificarVecinos(fila, columna, 3, estado);
                }
                else{
                    NotificarVecinos(fila, columna, 2, estado);
                }
            }
            else if (fila == (matriz.length)-1){
                if (columna == 0){
                    NotificarVecinos(fila, columna, 7, estado);
                }
                else if (columna == (matriz.length)-1){
                    NotificarVecinos(fila, columna, 9, estado);
                }
                else{
                    NotificarVecinos(fila, columna, 8, estado);
                }
            }
            else{
                if (columna == 0){
                    NotificarVecinos(fila, columna, 4, estado);
                }
                else if (columna == (matriz.length)-1){
                    NotificarVecinos(fila, columna, 6, estado);
                }
                else{
                    NotificarVecinos(fila, columna, 5, estado);
                }
            }

            try{
                /* Aquí se hace un llamado de la barrera para corroborar que todos los threads ya se encargaron de informar
                * a sus vecinos en qué estado se encontraban (vivos/muertos -> true/false). */
                

                int vecinosVivos = buzonPropio.getVecinosVivos();

                /* Aquí se hace la actualización en la matriz booleana de la app principal para la próxima generación en función
                * de la cantidad de vecinos vivos que cada buzón haya reportado para su propia celula. */
                if (estado == false && vecinosVivos == 3){
                    App.setBoolean(ids.get(0), ids.get(1), true);
                }
                else if (estado == true && (vecinosVivos > 3 || vecinosVivos == 0)){
                    App.setBoolean(ids.get(0), ids.get(1), false);
                }
                else if (estado == true && 1 <= vecinosVivos && vecinosVivos <= 3){
                    App.setBoolean(ids.get(0), ids.get(1), true);
                }

                barrera.await();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /* Este método llama a la función auxiliar NotificarCelda para que notifique a sus vecinos específicos en función del
     * caso que se haya identificado en el run de acuerdo a la posición identificada por el ids. */
    public void NotificarVecinos (int fila, int columna, int caso, Boolean estado){
        if (caso == 1){
            NotificarCelda(fila, columna+1, estado);
            NotificarCelda(fila+1, columna, estado);
            NotificarCelda(fila+1, columna+1, estado);
            System.out.println("caso1");
        }

        else if (caso == 2){
            NotificarCelda(fila, columna-1, estado);
            NotificarCelda(fila+1, columna-1, estado);
            NotificarCelda(fila+1, columna, estado);
            NotificarCelda(fila+1, columna+1, estado);
            NotificarCelda(fila, columna+1, estado);
            System.out.println("caso2");
        }

        else if (caso == 3){
            NotificarCelda(fila, columna-1, estado);
            NotificarCelda(fila+1, columna-1, estado);
            NotificarCelda(fila+1, columna, estado);
            System.out.println("caso3");
        }

        else if (caso == 4){
            NotificarCelda(fila-1, columna, estado);
            NotificarCelda(fila-1, columna+1, estado);
            NotificarCelda(fila, columna+1, estado);
            NotificarCelda(fila+1, columna+1, estado);
            NotificarCelda(fila+1, columna, estado);
            System.out.println("caso4");
        }

        else if (caso == 5){
            NotificarCelda(fila-1, columna-1, estado);
            NotificarCelda(fila-1, columna, estado);
            NotificarCelda(fila-1, columna+1, estado);
            NotificarCelda(fila, columna-1, estado);
            NotificarCelda(fila, columna+1, estado);
            NotificarCelda(fila+1, columna-1, estado);
            NotificarCelda(fila+1, columna, estado);
            NotificarCelda(fila+1, columna+1, estado);
            System.out.println("caso5");
        }

        else if (caso == 6){
            NotificarCelda(fila-1, columna, estado);
            NotificarCelda(fila-1, columna-1, estado);
            NotificarCelda(fila, columna-1, estado);
            NotificarCelda(fila+1, columna-1, estado);
            NotificarCelda(fila+1, columna, estado);
            System.out.println("caso6");
        }

        else if (caso == 7){
            NotificarCelda(fila-1, columna, estado);
            NotificarCelda(fila-1, columna+1, estado);
            NotificarCelda(fila, columna+1, estado);
            System.out.println("caso7");
        }

        else if (caso == 8){
            NotificarCelda(fila, columna-1, estado);
            NotificarCelda(fila-1, columna-1, estado);
            NotificarCelda(fila-1, columna, estado);
            NotificarCelda(fila-1, columna+1, estado);
            NotificarCelda(fila, columna+1, estado);
            System.out.println("caso8");
        }
        else{
            NotificarCelda(fila, columna-1, estado);
            NotificarCelda(fila-1, columna-1, estado);
            NotificarCelda(fila-1, columna, estado);
            System.out.println("caso9");
        } 
    }
    
    /* Este método recibe una fila, una columna y un booleano de manera que a al buzón de la celda ubicada en la matriz
     * en esas coordenada sea notificado por el estado que entró como booleano. */
    public synchronized void NotificarCelda (int fila, int columna, Boolean estado){
        Celda celdaSeleccionada = App.getCelda(fila, columna);
        Buzon buzonSeleccionado = celdaSeleccionada.getBuzon();
        buzonSeleccionado.NotificarBuzon(estado);
        buzonSeleccionado.VaciarBuzon();
    }

    public void setEstado(Boolean cambio){
        estado = cambio;
    }

    public Buzon getBuzon(){
        return buzonPropio;
    }

    public Boolean getEstado(){
        return estado;
    }

    public ArrayList <Integer> getIds(){
        return ids;
    }


}
