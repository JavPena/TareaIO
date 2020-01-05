package mcdp;

import java.util.Random;

public class MCDPRandomSolution {

    // Solution matrix

    private int[][] f_weight;
    private int[] f_size;
    private int N;

    private int[] superA;

    // Constructor
    public MCDPRandomSolution(int f_weight[][], int[] f_size, int N) {
        this.f_weight = f_weight;
        this.f_size = f_size;
        this.N = N;
    }
       
    
    
    public void createRandomSolution() {
        this.superA = new int[N];
        for(int i=0;i<N;i++){
            superA[i]=i+1;
        }
        shuffleArray(superA);
    }
    
    
    static void shuffleArray(int[] ar){
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public int[] getMachine_cell() {
        return superA;
    }

    public void setMachine_cell(int[] machine_cell) {
        this.superA = machine_cell;
    }


}
