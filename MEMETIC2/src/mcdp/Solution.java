package mcdp;

import java.util.Arrays;

public class Solution {

    // Solution matrix

    private int[][] machine_cell;
    private int[][] part_cell;

    // Fitness solution
    private int fitness;

    // Constructor
    public Solution(int[][] machine_cell, int[][] part_cell, int fitness) {
        this.machine_cell = new int[machine_cell.length][machine_cell[0].length];
        this.part_cell = new int[part_cell.length][part_cell[0].length];

        // Copy original values
        for (int i = 0; i < machine_cell.length; i++) {
            System.arraycopy(machine_cell[i], 0, this.machine_cell[i], 0, machine_cell[0].length);
        }

        for (int i = 0; i < part_cell.length; i++) {
            System.arraycopy(part_cell[i], 0, this.part_cell[i], 0, part_cell[0].length);
        }

        this.fitness = fitness;
    }

    public Solution(int[][] machine_cell, int[][] part_cell) {
        this.machine_cell = new int[machine_cell.length][machine_cell[0].length];
        this.part_cell = new int[part_cell.length][part_cell[0].length];

        // Copy original values
        for (int i = 0; i < machine_cell.length; i++) {
            System.arraycopy(machine_cell[i], 0, this.machine_cell[i], 0, machine_cell[0].length);
        }

        for (int i = 0; i < part_cell.length; i++) {
            System.arraycopy(part_cell[i], 0, this.part_cell[i], 0, part_cell[0].length);
        }
    }

    public Solution() {
    }

    // Get and Set
    public int[][] getMachine_cell() {
        return machine_cell;
    }

    public void setMachine_cell(int[][] machine_cell) {
        this.machine_cell = new int[machine_cell.length][machine_cell[0].length];

        // Copy original values
        for (int i = 0; i < machine_cell.length; i++) {
            System.arraycopy(machine_cell[i], 0, this.machine_cell[i], 0, machine_cell[0].length);
        }
    }

    public int[][] getPart_cell() {
        return part_cell;
    }

    public void setPart_cell(int[][] part_cell) {
        this.part_cell = new int[part_cell.length][part_cell[0].length];

        for (int i = 0; i < part_cell.length; i++) {
            System.arraycopy(part_cell[i], 0, this.part_cell[i], 0, part_cell[0].length);
        }
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    // To Console
    public void toConsoleMachineCell() {
        System.out.println("   > Machine Cell: " + Arrays.deepToString(machine_cell));
    }

    public void toConsolePartCell() {
        System.out.println("   > Part Cell   : " + Arrays.deepToString(part_cell));
    }

    public void toConsoleFitness() {
        System.out.println("   > Fitness     : " + fitness);
    }

}
