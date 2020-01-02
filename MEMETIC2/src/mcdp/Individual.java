package mcdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Individual {

    // Solution matrix
    private int[][] superA;

    // Fitness solution
    private float fitness;
    
    private Individual parentesco;


    private int chicken;
    //Selection Probability
    private float individualSelectionProbability;

    //List of index with differences compared to expert individual
    private List<Index> indexDifferenceMachineCell = new ArrayList<>();
    private List<Index> indexDifferencePartCell = new ArrayList<>();

    // Constructor
    public Individual(int[][] machine_cell, float fitness) {
        this.superA = new int[machine_cell.length][machine_cell[0].length];

        // Copy original values
        for (int i = 0; i < machine_cell.length; i++) {
            for(int j = 0; j < machine_cell[0].length;j++){
                this.superA[i][j]=machine_cell[i][j];
            }
        }


        this.fitness = fitness;
        //Cuando se cree no tendrá parentesco.
        parentesco = null;
    }

    public Individual(int[][] machine_cell) {
        this.superA = new int[machine_cell.length][machine_cell[0].length];

        // Copy original values
        for (int i = 0; i < machine_cell.length; i++) {
            for(int j = 0; j < machine_cell[0].length;j++){
                this.superA[i]=machine_cell[i];
            }
        }

        
        parentesco = null;
    }

    public Individual() {
        parentesco = null;
    }

    // Get and Set
    public int[][] getMachine_cell() {
        return superA;
    }

    public void setMachine_cell(int[][] machine_cell) {
        this.superA = new int[machine_cell.length][machine_cell[0].length];

        // Copy original values
        for (int i = 0; i < machine_cell.length; i++) {
            for(int j = 0; j < machine_cell[0].length;j++){
                this.superA[i]=machine_cell[i];
            }
        }
    }

    
    public int getChicken() {
        return chicken;
    }

    public void setChicken(int chicken) {
        this.chicken = chicken;
    }
    
    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
   
    public Individual getParentesco() {
        return parentesco;
    }

    public void setParentesco(Individual parentesco) {
        this.parentesco = parentesco;
    }
    

    public List<Index> getIndexDifferenceMachineCell() {
        return indexDifferenceMachineCell;
    }

    public void setIndexDifferenceMachineCell(List<Index> indexDifferenceMachineCell) {
        this.indexDifferenceMachineCell = indexDifferenceMachineCell;
    }

    public List<Index> getIndexDifferencePartCell() {
        return indexDifferencePartCell;
    }

    public void setIndexDifferencePartCell(List<Index> indexDifferencePartCell) {
        this.indexDifferencePartCell = indexDifferencePartCell;
    }

    // To Console

    public void toConsoleFitness() {
        System.out.println("   > Fitness     : " + fitness);
    }

    

    public float getIndividualSelectionProbability() {
        return individualSelectionProbability;
    }

    public void setIndividualSelectionProbability(float individualSelectionProbability) {
        this.individualSelectionProbability = individualSelectionProbability;
    }

    


    public void moveToExpertIndividual(float rRandomMovement, Individual experIndividual) {

        System.out.println(">>Difference Machine - Part , at indexes ");
        Index index;

        for (int i = 0; i < this.indexDifferenceMachineCell.size(); i++) {
            index = this.indexDifferenceMachineCell.get(i);

            //System.out.println( index.getX() + ", " + index.getY() );
            /*
                Calculo de Fitness 
             */
        }
    }

    

}
