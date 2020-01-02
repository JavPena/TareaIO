package mcdp;

import java.util.Random;
import java.util.stream.IntStream;

/**
 Esta es solo una forma de generar soluciones vecinas, existen muchas maneras.

 @author Salazar

 */
public class MCDPNeighbourSolution {

    // Solution matrix

    private int[][] machine_cell;
    private int[][] part_cell;

    // Data
    private int[][] A;
    private int M;
    private int P;
    private int C;
    private int Mmax;

    // Constructor
    public MCDPNeighbourSolution(int A[][], int M, int P, int C, int Mmax, int[][] machine_cell, int[][] part_cell) {
        this.A = A;
        this.M = M;
        this.P = P;
        this.C = C;
        this.Mmax = Mmax;

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

    public void createNeighbourSolution() {
		// Primero Generamos un cambio en la matriz MxC
        // Se llama generar una solución vecina

        // Random machine
        Random rm = new Random();
        int randomMachine = rm.nextInt(M - 0) + 0;

        // Clear row
        for (int k = 0; k < C; k++) {
            machine_cell[randomMachine][k] = 0;
        }

        // Random cell
        Random rc = new Random();
        int randomCell = rc.nextInt(C - 0) + 0;

        // Puts new value 1 in new randomic cell
        machine_cell[randomMachine][randomCell] = 1;

        // Posteriormente generamos manualmente la matriz PxC
        for (int j = 0; j < P; j++) //Rellenar la matriz pieza×celda de "0"
        {
            for (int k = 0; k < C; k++) {
                part_cell[j][k] = 0;
            }
        }

        for (int j = 0; j < P; j++) {
            int[] tempPart = new int[M];
            int[] cellCount = new int[C];

            for (int k = 0; k < C; k++) {
                for (int i = 0; i < M; i++) {
                    // Esto hace una multiplicación para revisar si: P(j) es subconjunto de C(k)
                    tempPart[i] = machine_cell[i][k] * A[i][j];
                }
                cellCount[k] = IntStream.of(tempPart).sum();
            }
            // Extraer el índice de la posición con el número más grande.
            int maxIndex = 0;
            for (int i = 1; i < cellCount.length; i++) {
                int newNumber = cellCount[i];
                if ((newNumber > cellCount[maxIndex])) {
                    maxIndex = i;
                }
            }
            // Puts un 1 in the new cell
            part_cell[j][maxIndex] = 1;
        }
    }

    // Get and Set
    public int[][] getMachine_cell() {
        return machine_cell;
    }

    public void setMachine_cell(int[][] machine_cell) {
        this.machine_cell = machine_cell;
    }

    public int[][] getPart_cell() {
        return part_cell;
    }

    public void setPart_cell(int[][] part_cell) {
        this.part_cell = part_cell;
    }
}
