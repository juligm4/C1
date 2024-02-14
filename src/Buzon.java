import java.util.ArrayList;

public class Buzon extends Thread{
    private ArrayList<Integer> id;
    private ArrayList<Boolean> estadosVecinos;
    private int vecinosVivos = 0;
    private int vecinosVerificados = 0;
    private int vecinosTotales;

    public Buzon (ArrayList<Integer> id, int vecinosTotales){
        this.id = id;
        this.vecinosTotales = vecinosTotales;
        this.estadosVecinos = new ArrayList<Boolean>(id.get(0)+1);

    }
    
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

    public void NotificarBuzon (Boolean estadoVecino){
        estadosVecinos.add(estadoVecino);
    }

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
