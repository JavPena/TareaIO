package tareaIo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Metaheuristic {

    // Parametros de la metaheuristica
    private int numberIndividual;
    private int numberIteration;
    private float probabilityFactor;

    private Individual bestSolution;		// Mejor Solución

    private ArrayList<Individual> poblation;    //Poblacion General
    private ArrayList<Individual> tourneyPoblation;    //Poblacion General

    private Data data;


//<editor-fold defaultstate="collapsed" desc="Constructor">
    public Metaheuristic(int numberIndividual, int numberIteration, Data data, float prob) {

        this.numberIndividual = numberIndividual;
        this.numberIteration = numberIteration;
        this.probabilityFactor = prob;

        this.data = data;

        this.poblation = new ArrayList<Individual>(this.numberIndividual);
        this.tourneyPoblation = new ArrayList<Individual>(this.numberIndividual);

        this.bestSolution = new Individual();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="run">
    public void run() throws IOException {
        
        generateInitialPoblation();
        ordenaBacterias();
        System.out.println("mejor fitness INICIAL: "+poblation.get(0).getFitness());
        
        int iteration = 0;
        float mejorFitness = 99999999;

        while (iteration < this.numberIteration) {
            
            selectTourney();
            
            mating(1-this.probabilityFactor);
            
            mutate(this.probabilityFactor);
            
            selectBestSolutions();
            
            System.out.println("iteracion: "+iteration);
            
            iteration++;
            
            if(iteration%5 == 0){
                for(int k=0 ; k< poblation.size();k++){
                    int[] initial = poblation.get(k).getMachine_cell();
                    for(int i=0; i <(data.getN()-1); i++){
                        for(int j = i+1 ; j<data.getN() ; j++){
                            int[] M = switchPosition(initial,i,j);
                            Individual pk = new Individual(M);
                            Model boctorModel= new Model(data.getF_weight(),data.getF_size(),data.getN(),M);
                            pk.setFitness(boctorModel.calculateFitness());
                            if(pk.getFitness()<=poblation.get(k).getFitness()){
                                poblation.get(k).setFitness(pk.getFitness());
                                poblation.get(k).setMachine_cell(pk.getMachine_cell());
                            }
                        }
                    }

                }
            }
            
            if (poblation.get(0).getFitness() < mejorFitness) {
                mejorFitness = poblation.get(0).getFitness();
                System.out.println("mejor fitness: "+mejorFitness+ " iteracion: "+iteration);

            }
            for(int l=0;l<poblation.size();l++){
                    System.out.print(" "+poblation.get(l).getFitness());
                }
                System.out.println();
            
        }
        
        System.out.println("\nbest solution: "+mejorFitness);
        poblation.get(0).printSolution();
        System.out.println();
        
        
        
   
        
    }
    //</editor-fold>
   
    public void selectTourney(){
        this.tourneyPoblation.clear();
        Individual fight1,fight2;
        Random random = new Random();
        int aux;
        for (Individual poblation1 : poblation) {
            aux=random.nextInt(poblation.size());
            fight1=poblation.get(aux);
            aux=random.nextInt(poblation.size());
            fight2=poblation.get(aux);
            if(random.nextBoolean()){
                fight2=new Individual(fight1.getMachine_cell(),fight1.getFitness());
                this.tourneyPoblation.add(fight2);
            }else{
                fight1=new Individual(fight2.getMachine_cell(),fight2.getFitness());
                this.tourneyPoblation.add(fight1);
            }
        }
    }
    
    public void mating(float prob){
        ArrayList<Individual> matePool = new ArrayList<Individual>();
        Random random = new Random();
        for(Individual individual : tourneyPoblation){
            if(random.nextDouble()>prob){
                matePool.add(individual);
                prob=1-this.probabilityFactor;
            }
            else{
                prob=prob-this.probabilityFactor;
            }
        }
        for(Individual mate : matePool){
            int[] aux = mate.getMachine_cell();
            int[] partner = poblation.get(random.nextInt(poblation.size())).getMachine_cell();
            double cross = random.nextDouble();
            for(int i=0; i<aux.length;i++){
                if(cross > i/aux.length){
                    aux[i]=partner[i];
                }
            }
            mate.setMachine_cell(aux);
        }
    }
    
    public void mutate(float prob){
        Random random = new Random();
        for(Individual individual : tourneyPoblation){
            if(prob < random.nextDouble()){
                prob=this.probabilityFactor;
                int[] aux = individual.getMachine_cell();
                int pos1 = random.nextInt(aux.length);
                int pos2 = random.nextInt(aux.length);
                if(pos1<pos2){
                    for(int i = pos1 ; i< pos2 ; i++){
                        aux=switchPosition(aux,i,i+1);
                    }
                }
            }else{
                prob= prob + this.probabilityFactor;
            }
        }
    }
    
    public void selectBestSolutions(){
        for(Individual individual : tourneyPoblation){
            Model boctorModel= new Model(data.getF_weight(),data.getF_size(),data.getN(),individual.getMachine_cell());
            individual.setFitness(boctorModel.calculateFitness());
            this.poblation.add(individual);
        }
        ordenaBacterias();
        while(this.poblation.size()>this.numberIndividual){
            this.poblation.remove(this.poblation.size()-1);
        }
    }
    
    public int[] switchPosition(int[] M,int A, int B){
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
            RandomSolution randomSolution = new RandomSolution(data.getF_weight(), data.getF_size(), data.getN());
            float randomSolutionFitness = 0;
            randomSolution.createRandomSolution();
            Model boctorModel = new Model(data.getF_weight(), data.getF_size(), data.getN(),
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
