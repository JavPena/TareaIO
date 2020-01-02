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

/**
 *
 * @author francisco.gonzalez Human Behavior Based Optimization (HBBO) to solve
 * MCDP Author webpage : http://a-ahmadi.com/hbbo/
 */
public class hbm {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {

        int numberPoblation = 10;

        if(args.length == 0){
            numberPoblation = 31;
        }else{
            numberPoblation = Integer.parseInt(args[0]);
        }
                        
        /*if (args.length == 0) {
            consultationFactor = (float) 0.1;
        } else {
            consultationFactor = Float.parseFloat(args[0]);
        }*/

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String resultsPath = s + "/results/";
        
        //iteracion del algoritmo############
        int numberIteration = 10000;
     

        ArrayList<BenchMark> listBenchMark = BenchMark.createBenchMark();
        MCDPData data;

        String subFolderProblems = "newInstance";
        //subFolderProblems = "boctorV2";
        File dir = new File(s + "/src/problems/" + subFolderProblems);
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
        System.out.println(directoryListing[0].getName());
        int problemNumber = 0;
        int fromProblemNumber = 15;
        int toProblemNumber = 15;
        int size = toProblemNumber - fromProblemNumber + 1;
        ArrayList<Integer> range = new ArrayList<Integer>(size);
        for (int k = 0; k < size; k++) {
            range.add(fromProblemNumber);
            fromProblemNumber++;
        }
        if (directoryListing != null) {
            
            for (File fileProblem : directoryListing) {
                
                if (!range.contains(problemNumber)) {
                    problemNumber++;
                    //continue;
                }
                problemNumber++;
                
                int iterationsForStatistics = 0;
                
                ArrayList<Integer> listFitnessPerIteration = new ArrayList<Integer>();
                ArrayList<Integer> listIterationBestFitness = new ArrayList<Integer>();
                
                ArrayList<Float> listPercentualErrors = new ArrayList<Float>();
                
                Metaheuristic metaheuristic = null;
                
                long startTotalTime = System.currentTimeMillis();
                
                //ITERACION DEL programa############
                int numberIterationForStatistics = 1;
                
                while (iterationsForStatistics < numberIterationForStatistics) {
                    
                    long startTime = System.currentTimeMillis();
                    System.out.println("");
                    System.out.println(">> Processing : " + fileProblem.getName() + " Instance : " + iterationsForStatistics);
                    
                    data = new MCDPData(fileProblem);
                    //data = new MCDPData("CFP12_Askin-Subramanian_Problem01_14x24.txt", subFolderProblems);
                    
                    
                    metaheuristic = new Metaheuristic(numberPoblation, numberIteration, data);
                    
                    metaheuristic.run();
                    
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    
                    //Valores, cada X iteraciones (1000, 5000, etc)
                    //listFitnessPerIteration.add(metaheuristic.getBestSolution().getFitness());
                    //listIterationBestFitness.add(metaheuristic.getIteracionMejorFitness());
                    
                    //Graficos: Fit/Ite
                    //Plot.graphicIterationFitness(resultsPath, fileProblem.getName(), metaheuristic.seriesIterationFitness, -1);
                    
                    // metaheuristic.writeResultsExcel(resultsPath, elapsedTime, fileProblem.getName(),
                    //        numberPoblation, numberField, k1, k2, consultationFactor, numberIteration, iterationsForStatistics, listBenchMark, listFitnessPerIteration);
                    
                    iterationsForStatistics++;
                    
                    //metaheuristic.printPoblationFitness();
                    
                    //MCDPModel boctorModelbest = new MCDPModel(data.A, data.M, data.P, data.C, data.mmax,
                    //        metaheuristic.getBestSolution().getMachine_cell(),
                    //        metaheuristic.getBestSolution().getPart_cell());
                    //boolean constraintOK = boctorModelbest.checkConstraint();
                    //System.out.println("        ************************  " + constraintOK);
                    
                    //System.out.println("BEST  " + metaheuristic.getBestSolution().getFitness());
                    
                }
                
                long stopTotalTime = System.currentTimeMillis();
                long elapsedTotalTime = stopTotalTime - startTotalTime;
                
                //metaheuristic.writeResultsExcelConsolidado(resultsPath, elapsedTotalTime, fileProblem.getName(),
                //        numberPoblation, numberField, k1, k2, consultationFactor, numberIteration, numberIterationForStatistics, listBenchMark, listFitnessPerIteration, listIterationBestFitness, subFolderProblems);
                
            }//for (File fileProblem : directoryListing) {...}
        }

    }

}
