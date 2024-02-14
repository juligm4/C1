import java.util.*;
import java.util.concurrent.CyclicBarrier;;

public class Celda extends Thread{
    private ArrayList<Integer> id; 
    private Buzon buzonPropio;
    private Boolean estado;
    private CyclicBarrier barrera;

    public Celda (ArrayList<Integer> id, Buzon buzonPropio, Boolean estado, CyclicBarrier barrera){
        this.id = id;
        this.buzonPropio = buzonPropio;
        this.estado = estado;
        this.barrera = barrera;
    }

    public void run (){
        Celda[][] matriz = App.getMatriz();
        int fila = id.get(0);
        int columna = id.get(1);
        System.out.println("IDDDDDD" + id);
        if (fila == 0){
            if (columna == 0){
                NotificarVecinos(fila, columna, 1, estado);
            }
            if (columna == matriz.length){
                NotificarVecinos(fila, columna, 3, estado);
            }
            else{
                NotificarVecinos(fila, columna, 2, estado);
            }
        }
        else if (fila == matriz.length-1){
            if (columna == 0){
                NotificarVecinos(fila, columna, 7, estado);
            }
            else if (columna == matriz.length-1){
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
            else if (columna == matriz.length-1){
                NotificarVecinos(fila, columna, 6, estado);
            }
            else{
                NotificarVecinos(fila, columna, 5, estado);
            }
        }

        try{
            
            barrera.await();

            int vecinosVivos = buzonPropio.getVecinosVivos();

            if (estado == false && vecinosVivos == 3){
                App.setBoolean(id.get(0), id.get(1), true);
            }
            else if (estado == true && (vecinosVivos > 3 || vecinosVivos == 0)){
                App.setBoolean(id.get(0), id.get(1), false);
            }
            else if (estado == true && 1 <= vecinosVivos && vecinosVivos <= 3){
                App.setBoolean(id.get(0), id.get(1), true);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


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
    

    public synchronized void NotificarCelda (int fila, int columna, Boolean estado){
        Celda[][] matriz = App.getMatriz();
        System.out.println(matriz.toString());
        Celda celdaSeleccionada = matriz[fila][columna];
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


}
