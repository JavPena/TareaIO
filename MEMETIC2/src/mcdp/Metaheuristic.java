package mcdp;

import java.io.IOException;
import java.util.ArrayList;

public class Metaheuristic {

    // Parametros de la metaheuristica
    private int numberIndividual;
    private int numberIteration;
    private float consultationFactor;

    private Individual bestSolution;		// Mejor Solución

    private ArrayList<Individual> poblation;    //Poblacion General

    private MCDPData data;


//<editor-fold defaultstate="collapsed" desc="Constructor">
    public Metaheuristic(int numberIndividual, int numberIteration, MCDPData data) {

        this.numberIndividual = numberIndividual;
        this.numberIteration = numberIteration;

        this.data = data;

        this.poblation = new ArrayList<Individual>(this.numberIndividual);

        this.bestSolution = new Individual();
    }
//</editor-fold>11

//<editor-fold defaultstate="collapsed" desc="run">
    public void run() throws IOException {
        
        generateInitialPoblation();
        ordenaBacterias();
        System.out.println("mejor fitness INICIAL: "+poblation.get(0).getFitness());
        
        int iteration = 0;
        float mejorFitness = 99999999;

        while (iteration < this.numberIteration) {
            
            for(int k=0 ; k< poblation.size();k++){
                int[] initial = poblation.get(k).getMachine_cell();
                for(int i=0; i <(data.getN()-1); i++){
                    for(int j = i+1 ; j<data.getN() ; j++){
                        int[] M = mutate(initial,i,j);
                        Individual pk = new Individual(M);
                        MCDPModel boctorModel= new MCDPModel(data.getF_weight(),data.getF_size(),data.getN(),M);
                        pk.setFitness(boctorModel.calculateFitness());
                        if(pk.getFitness()<=poblation.get(k).getFitness()){
                            poblation.get(k).setFitness(pk.getFitness());
                            poblation.get(k).setMachine_cell(pk.getMachine_cell());
                        }
                    }
                }
                
            }
            ordenaBacterias();
            if (poblation.get(0).getFitness() < mejorFitness) {
                mejorFitness = poblation.get(0).getFitness();
                System.out.println("mejor fitness: "+mejorFitness+ " iteracion: "+iteration);

            }
            for(int l=0;l<poblation.size();l++){
                    System.out.print(" "+poblation.get(l).getFitness());
                }
                System.out.println();
            System.out.println("iteracion: "+iteration);
            
            
            iteration++;
        }
        for(int k=0;k<poblation.size();k++){
            System.out.print(" "+poblation.get(k).getFitness());
        }
        System.out.println("\nbest solution: "+mejorFitness);
        poblation.get(0).printSolution();
        System.out.println();
        
        
        
   
        
    }
    //</editor-fold>
    
    public int[] mutate(int[] M,int A, int B){
        int aux;
        aux=M[A];
        M[A]=M[B];
        M[B]=aux;

        return M;
    }
    
    public void ordenaBacterias(){
        boolean orden = false;
        int i = 0;
        Individual aux = null;
        while(i<poblation.size() && orden == false){
            i+=1;
            orden = true;
            for (int j = 0; j < poblation.size()-1; j++) {
                if(poblation.get(j+1).getFitness()<poblation.get(j).getFitness()){
                    orden=false;
                    aux = new Individual(poblation.get(j).getMachine_cell(), poblation.get(j).getFitness());
                          poblation.get(j).setMachine_cell(poblation.get(j+1).getMachine_cell());
                          poblation.get(j).setFitness(poblation.get(j+1).getFitness()); 
                          poblation.get(j+1).setMachine_cell(aux.getMachine_cell());
                          poblation.get(j+1).setFitness(aux.getFitness()); 
                }
            }
        }
    }
    
    private void generateInitialPoblation() {
        for (int i = 0; i < numberIndividual; i++) {
            // Inicialite procedure
            MCDPRandomSolution randomSolution = new MCDPRandomSolution(data.getF_weight(), data.getF_size(), data.getN());
            float randomSolutionFitness = 0;
            randomSolution.createRandomSolution();
            MCDPModel boctorModel = new MCDPModel(data.getF_weight(), data.getF_size(), data.getN(),
                    randomSolution.getMachine_cell());
            randomSolutionFitness = boctorModel.calculateFitness();
            // Create Solution
            Individual individual = new Individual(randomSolution.getMachine_cell(), randomSolutionFitness);
            // Add Solution in poblation
            poblation.add(individual);
        }
    }
//</editor-fold>

}
