package mcdp;

import java.util.Arrays;

public class MCDPModel {
    
    //mismos datos que el data
    private int[][] f_weight;
    private int[] f_size;
    private int N;

    // Solution individuo
    private int[][] superA;

    
    
    public MCDPModel(int f_weight[][], int[] f_size, int N, int[][] superA) {
        this.f_weight = f_weight;
        this.f_size = f_size;
        this.N = N;
        this.superA = superA;
    }

    /**
     @return true si esta correta la restriccion, en caso contrario retorna falso.
     */
    public boolean consistencyConstraint_1() {
        for (int i = 0; i < this.N; i++) {
            int sum= 0;
            for (int k = 0; k < this.N; k++) {
                // Check that there is only one true value in the column.
                sum = sum+ this.superA[i][k];
            }
            if (sum != 1) {
                return false;
            }
        }

        return true;
    }
    
    public boolean consistencyConstraint_2() {
        for (int i = 0; i < this.N; i++) {
            int sum= 0;
            for (int k = 0; k < this.N; k++) {
                // Check that there is only one true value in the column.
                sum = sum+ this.superA[k][i];
            }
            if (sum != 1) {
                return false;
            }
        }

        return true;
    }

    

    public boolean checkConstraint() {
        boolean c1 = consistencyConstraint_1();
        boolean c2 = consistencyConstraint_2();
        if(!(c1 && c2)){
            MCDPRandomSolution m=new MCDPRandomSolution(this.f_weight,this.f_size,this.N);
            m.createRandomSolution();
            superA=m.getMachine_cell();
            return true;
        }
        return c1 && c2;
              
    }
    
    public int[][] getMachine(){
        return superA;
    }

    // Objective function 
    //TODO: cambiar
    public float calculateFitness() {
        float sum = 0;
        int[] posiciones=transform();
        
        for (int k = 0; k < (this.N-1); k++) {
            for (int i = k+1; i < this.N; i++) {
                sum= sum + distance(posiciones[k],posiciones[i])*this.f_weight[posiciones[i]][posiciones[k]];
            }
        }
        return sum;
    }
    
    public int[] transform(){
        int[] vector=new int[N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(superA[i][j]==1){
                    vector[i]=j;
                    break;
                }
            }
        }
        return vector;
    }
    
    public float distance(int i, int j){
        float sum= (this.f_size[i]/2)+ (this.f_size[j]/2);
        for(int k=i+1; k < j;k++){
            sum= sum + this.f_size[k];
        }
        return sum;
    }

}
