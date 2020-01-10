package tareaIo;

public class Individual {

    // Solution matrix
    private int[] superA;

    // Fitness solution
    private float fitness;


    // Constructor
    public Individual(int[] machine_cell, float fitness) {
        this.superA = new int[machine_cell.length];

        // Copy original values
        System.arraycopy(machine_cell, 0, this.superA, 0, machine_cell.length);


        this.fitness = fitness;
    }
    
    

    public Individual(int[] machine_cell) {
        this.superA = new int[machine_cell.length];

        // Copy original values
        System.arraycopy(machine_cell, 0, this.superA, 0, machine_cell.length);

    }
    
    public Individual(){
    }

    

    // Get and Set
    public int[] getMachine_cell() {
        return superA;
    }
    
    public void printSolution(){
        for(int i=0;i<superA.length;i++){
            System.out.print(superA[i]+" ");
        }
    }

    public void setMachine_cell(int[] machine_cell) {
        this.superA = new int[machine_cell.length];

        // Copy original values
        System.arraycopy(machine_cell, 0, this.superA, 0, machine_cell.length);
    }
    
    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
    
}
