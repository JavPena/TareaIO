package mcdp;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class MCDPRandomSolution {

    // Solution matrix

    private int[][] f_weight;
    private int[] f_size;
    private int N;

    // Solution individuo
    private int[] superA;

    // Constructor
    public MCDPRandomSolution(int f_weight[][], int[] f_size, int N) {
        this.f_weight = f_weight;
        this.f_size = f_size;
        this.N = N;
    }
       
    
    
    public void createRandomSolution() {
        // create random permutation of X e machine_cell
        this.superA = new int[N];
        int[] vector= new int[N];
        
        for(int i=0;i<N;i++){
            superA[i]=i+1;
            
        }
        
        shuffleArray(superA);
        
        
    }
    
    
    static void shuffleArray(int[] ar){
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    // Get and Set
    public int[] getMachine_cell() {
        return superA;
    }

    public void setMachine_cell(int[] machine_cell) {
        this.superA = machine_cell;
    }


}
