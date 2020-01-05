package mcdp;

public class MCDPModel {
    
    //mismos datos que el data
    private int[][] f_weight;
    private int[] f_size;
    private int N;

    // Solution individuo
    private int[] superA;

    
    
    public MCDPModel(int f_weight[][], int[] f_size, int N, int[] superA) {
        this.f_weight = f_weight;
        this.f_size = f_size;
        this.N = N;
        this.superA = superA;
    }

    
    public float calculateFitness() {
        float sum = 0;
        
        for (int k = 0; k < (this.N-1); k++) {
            for (int i = k+1; i < this.N; i++) {
                sum = sum + distance(this.superA[k]-1,this.superA[i]-1)*this.f_weight[this.superA[i]-1][this.superA[k]-1];
            }
        }
        return sum;
    }
   
    
    public float distance(int i, int j){
        float sum= (this.f_size[i]/2)+ (this.f_size[j]/2);
        for(int k=i+1; k < j;k++){
            sum= sum + this.f_size[k];
        }
        return sum;
    }

}
