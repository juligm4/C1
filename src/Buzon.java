import java.util.ArrayList;

public class Buzon extends Thread{

    /*La estructura estados vecinos es un array que almacena los reportes de los estados de las celdas vecinas */
    private ArrayList<Boolean> estadosVecinos;
    private int vecinosVivos = 0;
    private int vecinosVerificados = 0;
    private int vecinosTotales;
    

    public Buzon (int tamano, int vecinosTotales){
        this.vecinosTotales = vecinosTotales;
        this.estadosVecinos = new ArrayList<Boolean>(tamano);
    }
    
    /* Este método limpia o elimina el primer elemento que esté en el array de estadosVecinos y se encarga de cambiar
     * los valores de los demás atributos en función de cuál fue el estado que eliminó (o dicho en otras palabras: que ya analizó). */
    public void VaciarBuzon (){
        if (estadosVecinos.get(0) == true){
            estadosVecinos.remove(0);
            vecinosVivos ++;
            vecinosVerificados ++;
        }
        
        else{
            estadosVecinos.remove(0);
            vecinosVerificados++;
        }
    }

    /* Una celda vecina (a) le avisa a este buzon que el estado de (a) es el parametro de entrada para que este valor sea
     * considerado dentro de los estados de vecinos de la celda a la que este buzón pertenece (llamemosle celda B). */
    public void NotificarBuzon (Boolean estadoVecino){
        estadosVecinos.add(estadoVecino);
    }

    /* Este método busca verificar si todos los vecinos de la celda a la que pertence este buzon ya reportaron sus estados */
    public Boolean VerificarLimite (){
        if (vecinosVerificados == vecinosTotales){
            return true;
        }
        else{
            return false;
        }
    }

    public int getVecinosVivos(){
        return vecinosVivos;
    }
}
