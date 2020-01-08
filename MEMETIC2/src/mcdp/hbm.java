/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static java.util.stream.IntStream.range;


public class hbm {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        int numberPoblation = 30;
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        //iteracion del algoritmo############
        int numberIteration = 50;
        MCDPData data;
        String subFolderProblems = "newInstance";
        File dir = new File(s + "/src/problems/" + subFolderProblems);
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
        if (directoryListing != null) {
            for (File fileProblem : directoryListing) {
                int iterationsForStatistics = 0;
                Metaheuristic metaheuristic = null;
                //ITERACION DEL programa############
                int numberIterationForStatistics = 1;
                while (iterationsForStatistics < numberIterationForStatistics) {
                    data = new MCDPData(fileProblem);
                    metaheuristic = new Metaheuristic(numberPoblation, numberIteration, data);
                    metaheuristic.run();
                    iterationsForStatistics++;
                }
            }
        }
    }
}
