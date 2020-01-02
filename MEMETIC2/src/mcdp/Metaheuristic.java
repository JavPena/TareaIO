package mcdp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.jfree.data.xy.XYSeries;

public class Metaheuristic {

    // Parametros de la metaheuristica
    private int numberIndividual;
    private int numberIteration;
    private int numberField;

    private float consultationFactor;
    private int numberOfVariables;
    private int numberOfVariablesToChange;

    private int iteracionMejorFitness = 0;

    private float k1;
    private float k2;

    private Individual bestSolution;		// Mejor Solución

    private ArrayList<Individual> poblation;    //Poblacion General

    // Dataset (benchmark)
    private MCDPData data;

    // Estadisticas
    private long numAcceptedMoves;
    private long numRejectedMoves;

    private float rpd;

    public XYSeries seriesIterationFitness = new XYSeries("Iteracion/Fitness");

//<editor-fold defaultstate="collapsed" desc="Constructor">
    public Metaheuristic(int numberIndividual, int numberIteration, MCDPData data) {

        System.out.println("numberIndividual: "+ numberIndividual+ " num iteraciones: " +numberIteration + " factor de consulta: "+ consultationFactor);
        this.numberIndividual = numberIndividual;
        this.numberIteration = numberIteration;
        this.numberField = numberField;
        this.consultationFactor = consultationFactor;
        this.k1 = k1;
        this.k2 = k2;

        this.data = data;

        this.poblation = new ArrayList<Individual>(this.numberIndividual);

        this.bestSolution = new Individual();

        this.numAcceptedMoves = 0;
        this.numRejectedMoves = 0;

    }
//</editor-fold>11

//<editor-fold defaultstate="collapsed" desc="run">
    public void run() throws IOException {
        
        generateInitialPoblation();
        //ordenar despues la generacion de pollos
        ordenaBacterias();
            
        System.out.println("mejor fitness INICIAL: "+poblation.get(0).getFitness());
        

        this.numberOfVariables = data.n * data.n;
        int iteration = 0;
        float mejorFitness = 99999999;
        clasificacion();

        while (iteration < this.numberIteration) {
            
            for ( int i = 0 ; i < poblation.size() ; i++ ){
                poblation.get(i).calculateDifferences(poblation.get(0).getMachine_cell());
            }
            
            
            
            for ( int i = 0 ; i < poblation.size() ; i++ ){
                int [] M = mutate(i);
                
                //se revisa que la nueva matriz cumpla con MCDP y se reemplaza con el riginal si el fitness es mejor
                Individual Pk = new Individual(poblation.get(i).getMachine_cell(), poblation.get(i).getFitness());
                Pk.setMachine_cell(M);
                MCDPModel boctorModel= new MCDPModel(data.f_weight, data.f_size, data.n,Pk.getMachine_cell());
                Boolean constraintOK = boctorModel.checkConstraint();
                if(constraintOK == true){
                    Pk.setFitness(boctorModel.calculateFitness());
                    Pk.setMachine_cell(boctorModel.getMachine());
                }
                if(Pk.getFitness()<poblation.get(i).getFitness()){
                    poblation.get(i).setMachine_cell(Pk.getMachine_cell());
                    poblation.get(i).setFitness(Pk.getFitness());
                }
               
                
                
            }
            
            ordenaBacterias();
            if (poblation.get(0).getFitness() < mejorFitness) {
                mejorFitness = poblation.get(0).getFitness();
                this.iteracionMejorFitness = iteration;
                System.out.println("mejor fitness: "+mejorFitness+ " iteracion: "+iteration);

            }
            System.out.println("Fitness ciclo "+iteration+" = "+poblation.get(0).getFitness());
            for(int i =0; i< poblation.size();i++){
                System.out.print(" "+poblation.get(i).getFitness());
            }
            System.out.print("\n");
            //System.out.println("mejor fitness: "+mejorFitness+ " iteracion: "+iteration);
            //seriesIterationFitness.add(iteration, bestSolution.getFitness());

            iteration++;
        }
   
        //chooseBestSolutionInPoblation();
        //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<END-RUN");

        //Plot.createPlots(this.modifyNumberPoblation, this.modifyNumberIteration, this.modifyConsultationFactor, resultsPath, fileName, this.seriesPoblationFitness, this.seriesIterationFitness, this.seriesConsultationFactorFitness, this.bestFitnessAuthor, this.numberIterationBestFitness, this.consultationFactorBestFitness, this.numberIterationAS);
    
    }
    //</editor-fold>
    
    
    public int[] mutate(int i){
        int M[] = this.poblation.get(i).getMachine_cell();
        Random random= new Random();
        int A,B,aux;
        if(this.poblation.get(i).getCantDifferences()!=0){
            int[] differences= poblation.get(i).getDifferences();
            do{
                A=(int)(random.nextDouble()*(this.data.n));
                if(A==8)A=7;
            }while(differences[A]==0);
            do{
                B=(int)(random.nextDouble()*(this.data.n));
                if(B==8)B=7;
            }while(B==A || differences[B]==0);

            aux=M[A];
            M[A]=M[B];
            M[B]=aux;
        }else{
            A=(int)(random.nextDouble()*(this.data.n));
            if(A==8)A=7;
            do{
                B=(int)(random.nextDouble()*(this.data.n));
                if(B==8)B=7;
            }while(B==A);

            aux=M[A];
            M[A]=M[B];
            M[B]=aux;
        }
        
        return M;
    }
    
    
    private ArrayList<Individual> Gallos;
    private ArrayList<Individual> Pollitos;
    private ArrayList<Individual> Gallinas;
    
    //Clasifica la cantidad pollos y gallinas que hay en la poblacion.
    public void clasificacion(){
        Gallos = new ArrayList();
        Pollitos = new ArrayList();
        Gallinas = new ArrayList();
        
        poblation.get(0).setChicken(1);    
        int i = 0;
        //Numero de Gallos (Rooster Number)
        int RN = 1;
        //Numero de Pollitos (Chicks Number)
        int CN = 1;
        //Numero de Gallinas (Hens Number)
        int HN = 1;
        //Número de Gallinas Mamás (MN)
        int MN = 1;
        
        
        
        while ( poblation.get(0).getFitness() == poblation.get(i).getFitness()  ){
            poblation.get(i).setChicken(1);
            Gallos.add(poblation.get(i)); 
            RN++;
            i++;
            
        }
        
        
        poblation.get(poblation.size() - 1).setChicken(3);
        i = poblation.size() - 1;
        
        while( poblation.get(i).getFitness() == poblation.get( poblation.size() - 1).getFitness() ){
            poblation.get(i).setChicken(3);
            Pollitos.add(poblation.get(i));
            CN++;
            i--;
        }
        
        for ( i = 0 ; i < poblation.size() ; i++){
            if ( poblation.get(i).getFitness() != poblation.get(0).getFitness() &&
                 poblation.get(i).getFitness() != poblation.get( poblation.size() - 1).getFitness() ){
                    HN++;
                    poblation.get(i).setChicken(2);
                    Gallinas.add(poblation.get(i));
                }
        }
        
        Individual gallina;
        for ( i = 0 ; i < Pollitos.size() && i < Gallinas.size() ; i++ ){
            Individual pollito = Pollitos.get(i);
            Random r = new Random();
            //Toma una gallina desde el índice hasta n-1, donde n corresponde al tamaño del arreglo de la
            //gallina.
            do{
                gallina = Gallinas.get(r.nextInt(Gallinas.size()));
            }while(gallina.getParentesco() != null);
            
            gallina.setParentesco(pollito);
            pollito.setParentesco(gallina);
            MN++;
            
        }
        
        
    }
    

    public void ordenaBacterias(){
        boolean orden = false;
        int i = 0;
        Individual aux = null;
        int mayor = 0;
        int indice =0;
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
        /*
        System.out.println("ANTES fitness "+poblation.get(0).getFitness());
        aux = new Individual(poblation.get(0).getMachine_cell(), poblation.get(0).getPart_cell(), poblation.get(0).getFitness());
        int[] SumCell = new int[data.C];     
        for (int y = 0; y < data.P; y++){
            mayor=0;indice=0;
            for (int c = 0; c < data.C; c++){
                SumCell[c]=0;
            }
            for (int x = 0; x < data.M; x++){
                if (this.data.A[x][y] == 1){
                    for(int z = 0; z < data.C; z++){
                      if (poblation.get(0).getMachine_cell()[x][z]==1){
                          SumCell[z]+=1;
                      }
                    }         
                }
            }

            for (int c = 0; c < data.C; c++){
                poblation.get(0).getPart_cell()[y][c]=0;
                if (mayor <= SumCell[c]){
                    mayor = SumCell[c];
                    indice=c; 
                }    
            }

            poblation.get(0).getPart_cell()[y][indice]=1;
            
        }
       
        MCDPModel  boctorModel = new MCDPModel(data.A, data.M, data.P, data.C, data.mmax,
                        poblation.get(0).getMachine_cell(),
                        poblation.get(0).getPart_cell());
        boolean constraintOK = boctorModel.checkConstraint();
        if (constraintOK == true) {
                poblation.get(0).setFitness(boctorModel.calculateFitness());
        }
        
        if(aux.getFitness()< poblation.get(0).getFitness()){
            poblation.get(0).setMachine_cell(aux.getMachine_cell());
            poblation.get(0).setPart_cell(aux.getPart_cell());
            poblation.get(0).setFitness(aux.getFitness()); 
        }
        bestSolution.setMachine_cell(poblation.get(0).getMachine_cell());
        bestSolution.setPart_cell(poblation.get(0).getPart_cell());
        bestSolution.setFitness(poblation.get(0).getFitness());
        System.out.println("DESPUES firness "+poblation.get(0).getFitness());*/
        //System.out.println("pocision despues firness "+poblation.get(0).getFitness());
        /*for (int j = 0; j < poblation.size(); j++) {
            System.out.println("pocision "+j+" firness "+poblation.get(j).getFitness());
        }*/
    }
    
    public int[][] generaRandom(){
        int[][] matriz = new int[data.n][data.n];
        for (int x = 0; x < data.n; x++){
            for (int y = 0; y < data.n; y++){
                matriz[x][y] = (int)(Math.random()*2);
            }
        }   
        return matriz;
    }
    
    
    public int bin (double x){
        double t = 0;
        double u = Math.random();
        int salida = 0;

            t = Math.tanh(x);
            if(t < 0 )
                t=t*-1;
            //break;
        
        if ( u < t){
            salida = 1;;
        }else{
            salida = 0;
        }
    
    return salida;
    }
    
   
   
    int S1 (int posicion){
        
        return (int)(Math.exp( ( poblation.get(posicion).getFitness() - poblation.get(0).getFitness() ) /
                                ( Math.abs( poblation.get(posicion).getFitness())+ Double.MIN_VALUE) ) );
    }
    
    int S2 (int posicion, int posRandom){
     
        return  (int)( Math.exp(( poblation.get(posRandom).getFitness() - poblation.get(posicion).getFitness() )) );
        
    }
    
    //la matriz debe tener valores entre 0 y 1, entonces se debe transformar y discretizar los valores flotantes y los fuera de rango
    public double transforma(double valor){
        double x= Math.tanh(valor);
        if(x<0){
            x*=-1;
        }
        return x;
    }
    public int discretiza(double x){
        double t=Math.random();
        if(t<x){
            return 1;
        }
        else{
            return 0;
        }
    }
    
    
    
    
    
    public void limpiaParentesco(){
        for(int i =0;i<poblation.size();i++){
            poblation.get(i).setParentesco(null);
        }
    }
            
    public int [][] sortMatriz(int [][] matriz,int cont,int tipo){
        
        int  segmento = (int)data.n  /3;
        //System.out.println("segmento "+segmento);
        int indice1 = 0;
        int indice2 = segmento;
        int indice3 = (data.n-1)-segmento;
        int indice4 = data.n-1;
        //System.out.println("indice1 "+indice1);
        //System.out.println("indice2 "+indice2);
        //System.out.println("indice3 "+indice3);
        int aux = 0;

        
        if (cont == 0){
                //System.out.println("cont "+cont);
                for (int x = 0; x < segmento; x++){
                    if (tipo == 0){
                        //System.arraycopy(newMatriz[indice2], 0, newMatriz[indice1], 0, data.C);
                        for (int y = 0; y < data.n; y++){
                            aux = matriz[indice1][y];
                            matriz[indice1][y] = matriz[indice2][y];
                            matriz[indice2][y] = aux;
                        }
                        indice1+=1;
                        indice2-=1;
                    }else if(tipo == 1){
                        
                        while(indice1 < indice2){
                            for (int y = 0; y < data.n; y++){
                                aux = matriz[indice1][y];
                                matriz[indice1][y] = matriz[indice1+1][y];
                                matriz[indice2][y] = aux;
                            }
                            indice1+=1;
                        }    
                    }
                    
                
                }
            }else if (cont == 1){
                //System.out.println("cont "+cont);
                for (int x = 0; x < segmento; x++){
                    if (tipo == 0){
                        for (int y = 0; y < data.n; y++){
                            aux = matriz[indice2][y];
                            matriz[indice2][y] = matriz[indice3][y];
                            matriz[indice3][y] = aux;
                        }
                        indice2+=1;
                        indice3-=1;
                    }else if(tipo == 1){
                        
                        while(indice1 < indice2){
                            for (int y = 0; y < data.n; y++){
                                aux = matriz[indice2][y];
                                matriz[indice2][y] = matriz[indice2+1][y];
                                matriz[indice2][y] = aux;
                            }
                            indice1+=1;
                        }    
                    }else if(tipo == 2){
                        
                        while(indice1 < indice2){
                            for (int y = 0; y < data.n; y++){
                                matriz[indice1][y] = matriz[indice1+1][y];
                            }
                            indice1+=1;
                        }    
                    }
                }
            }else if (cont == 2){
                //System.out.println("cont "+cont);
                for (int x = 0; x < segmento; x++){
                    //System.arraycopy(newMatriz[indice2], 0, newMatriz[indice1], 0, data.C);
                    for (int y = 0; y < data.n; y++){
                        aux = matriz[indice3][y];
                        matriz[indice3][y] = matriz[indice4][y];
                        matriz[indice4][y] = aux;
                    }
                    indice3+=1;
                    indice4-=1;

                }
            }
         return matriz;
    }   
    public  void  printMatriz(int [][] matriz){
         System.out.println("Inicio Print Matriz");
        for (int x = 0; x < data.n; x++){
                for (int y = 0; y < data.n; y++){
                    System.out.print(matriz[x][y]);
                }            
                System.out.println();
            }    
    }

//<editor-fold defaultstate="collapsed" desc="generateInitialPoblation">
    private void generateInitialPoblation() {
        for (int i = 0; i < numberIndividual; i++) {
            // Inicialite procedure
            boolean constraintOK = false;
            MCDPRandomSolution randomSolution = new MCDPRandomSolution(data.f_weight, data.f_size, data.n);
            float randomSolutionFitness = 0;

            // Estoy en el ciclo hasta generar una solución randomica que satisfaga las restricciones
            while (constraintOK == false) {
                // Create random solution
                
                randomSolution.createRandomSolution();

                // Check constraint
                MCDPModel boctorModel = new MCDPModel(data.f_weight, data.f_size, data.n,
                        randomSolution.getMachine_cell());
                constraintOK = boctorModel.checkConstraint();

                if (constraintOK == true) {
                    
                    randomSolutionFitness = boctorModel.calculateFitness();
                    this.numAcceptedMoves++;
                    break;
                } else {
                    this.numRejectedMoves++;
                }
            }

            // Create Solution
            Individual individual = new Individual(randomSolution.getMachine_cell(), randomSolutionFitness);

            // Add Solution in poblation
            poblation.add(individual);
        }
    }
//</editor-fold>

    public void writeResults(String folderPath, long miliSeconds, String fileName, int numberPoblation, int numberField, float k1, float k2, float consultationFactor, int numberIterations) {

        BufferedWriter bw = null;
        File file;
        String line = "";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        String time = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(miliSeconds),
                TimeUnit.MILLISECONDS.toSeconds(miliSeconds)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miliSeconds))
        );

        try {
            fileName = "Results - " + fileName;
            file = new File(folderPath + fileName);

            bw = new BufferedWriter(new FileWriter(file, true));

            try (PrintWriter pw = new PrintWriter(bw)) {

                line = "F = " + bestSolution.getFitness() + " | "
                        + "Time = " + time + " | "
                        + "NPoblation = " + numberPoblation + " | "
                        + "NField = " + numberField + " | "
                        + "k1 = " + k1 + "  " + " | "
                        + "k2 = " + k2 + "  " + " | "
                        + "CFactor = " + consultationFactor + " | "
                        + "Iterations = " + numberIterations + " | ";

                pw.println(line);

            }
        } catch (IOException ex) {
            //Logger.getLogger(Bat.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                //Logger.getLogger(Bat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public float getCalculatedRpd(ArrayList<BenchMark> listBenchMark, String fileName) {
        int bestFitnessAuthor = -1;
        for (int i = 0; i < listBenchMark.size(); i++) {
            if (fileName.equals(listBenchMark.get(i).getProblemName())) {
                bestFitnessAuthor = listBenchMark.get(i).getBestFitnessAuthor();
                break;
            }
        }
        return (bestSolution.getFitness() - bestFitnessAuthor) / (float) bestFitnessAuthor;
    }

    public void writeResultsExcel(String folderPath, long miliSeconds, String fileName, int numberPoblation, int numberField, float k1, float k2, float consultationFactor, int numberIterations, int iterationsForStatistics, ArrayList<BenchMark> listBenchMark, ArrayList<Integer> listFitnessPerIteration) throws FileNotFoundException, IOException {

        int bestFitnessAuthor = -1;
        for (int i = 0; i < listBenchMark.size(); i++) {
            if (fileName.equals(listBenchMark.get(i).getProblemName())) {
                bestFitnessAuthor = listBenchMark.get(i).getBestFitnessAuthor();
                break;
            }
        }
        String problemFolder = fileName.substring(0, fileName.lastIndexOf('.')) + "/";

        String excelFileName = folderPath + problemFolder + fileName.replace("txt", "xls");

        File excelFile = new File(excelFileName);
        //String time = String.format("%.5f", (double)TimeUnit.MILLISECONDS.toSeconds(miliSeconds));
        String time = String.format("%d", miliSeconds);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        HSSFWorkbook workbook;
        HSSFSheet sheet;

        if (excelFile.exists()) {

            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
            sheet = workbook.getSheetAt(0);
            Cell cell = null;

        } else {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet(fileName.replace(".txt", ""));

            Row rowHeader = sheet.createRow(0);

            String[] header = {"Instance", "Fitness", "BestKnowFitness", "Time(miliSec)", "Poblation", "Field", "k1", "k2", "ConsultationFactor", "Iterations", "Date"};

            for (int i = 0; i < header.length; i++) {
                Cell headerCell = rowHeader.createCell(i);
                headerCell.setCellValue(header[i]);
            }
        }

        int rownum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rownum);

        int cellnum = 0;

        Cell cell = row.createCell(cellnum++);
        cell.setCellValue(iterationsForStatistics);

        cell = row.createCell(cellnum++);
        if (bestSolution.getFitness() <= bestFitnessAuthor) {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
        }
        cell.setCellValue(bestSolution.getFitness());

        cell = row.createCell(cellnum++);
        cell.setCellValue(bestFitnessAuthor);

        cell = row.createCell(cellnum++);
        cell.setCellValue(time);

        cell = row.createCell(cellnum++);
        cell.setCellValue(numberPoblation);

        cell = row.createCell(cellnum++);
        cell.setCellValue(numberField);

        cell = row.createCell(cellnum++);
        cell.setCellValue(k1);

        cell = row.createCell(cellnum++);
        cell.setCellValue(k2);

        cell = row.createCell(cellnum++);
        cell.setCellValue(consultationFactor);

        cell = row.createCell(cellnum++);
        cell.setCellValue(numberIterations);

        cell = row.createCell(cellnum++);
        cell.setCellValue(date);

        try {

            FileOutputStream out
                    = new FileOutputStream(excelFileName);
            workbook.write(out);
            out.close();
            //System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeResultsExcelConsolidado(String folderPath, long miliSeconds, String fileName, int numberPoblation, int numberField, float k1, float k2, float consultationFactor, int numberIterations, int iterationsForStatistics, ArrayList<BenchMark> listBenchMark, ArrayList<Integer> listFitnessPerIteration, ArrayList<Integer> listIterationBestFitness, String subFolderProblems) throws FileNotFoundException, IOException {

        int bestFitnessAuthor = -1;
        for (int i = 0; i < listBenchMark.size(); i++) {
            if (fileName.equals(listBenchMark.get(i).getProblemName())) {
                bestFitnessAuthor = listBenchMark.get(i).getBestFitnessAuthor();
                break;
            }
        }
        String problemName = fileName.replace(".txt", "");

        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String excelFileName = folderPath + subFolderProblems + "_" + consultationFactor + "_.xls";
        File excelFile = new File(excelFileName);

        String time = String.format("%d", TimeUnit.MILLISECONDS.toSeconds(miliSeconds));

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        HSSFWorkbook workbook;
        HSSFSheet sheet;
        HSSFSheet sheetResumen;

        //Promedio de Mejores Fitnes encontrados en las N Ejecuciones ( Cada N ejeccion contiene M iteraciones )
        float promedioMejoresFitness = 0;
        int mejorFitnessDeNEjecuciones = 99999;

        for (int i = 0; i < listFitnessPerIteration.size(); i++) {
            if (listFitnessPerIteration.get(i) < mejorFitnessDeNEjecuciones) {
                mejorFitnessDeNEjecuciones = listFitnessPerIteration.get(i);
            }

            promedioMejoresFitness += listFitnessPerIteration.get(i);
        }
        promedioMejoresFitness /= (float) listFitnessPerIteration.size();

        float promedioIteracionesMejorFitness = 0;
        int iteracionMejorFitness = 99999;
        for (int j = 0; j < listIterationBestFitness.size(); j++) {
            if (listIterationBestFitness.get(j) < iteracionMejorFitness) {
                iteracionMejorFitness = listIterationBestFitness.get(j);
            }
            promedioIteracionesMejorFitness += listIterationBestFitness.get(j);
        }
        promedioIteracionesMejorFitness /= listIterationBestFitness.size();
        
        String problemNameSheet = problemName.substring(0,10);
        
        if (excelFile.exists()) {

            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
            //sheet = workbook.getSheetAt(0);
            sheetResumen = workbook.getSheet("Resumen");
            sheet = workbook.getSheet(problemNameSheet);

            boolean sheetExist = false;
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {                
                
                if( workbook.getSheetName(i).equals(problemNameSheet) ){
                    sheetExist = true;
                    break;
                }                
            }

            if (!sheetExist) {
                sheet = workbook.createSheet(problemNameSheet);
                Row rowHeader = sheet.createRow(0);
                String[] header = {"Problem", "IteracionesParaEstadisticas", "Fitness Promedio", "Mejor Fitness", "Autor", "Promedio It. Mejor Fitness", "It. Mejor Fitness ", "T.Ejecucion(s)", "Poblation", "Field", "k1", "k2", "ConsultationFactor", "Iterations", "Date"};

                for (int i = 0; i < header.length; i++) {
                    Cell headerCell = rowHeader.createCell(i);
                    headerCell.setCellValue(header[i]);
                }
            }

            Cell cell = null;

        } else {
            workbook = new HSSFWorkbook();
            sheetResumen = workbook.createSheet("Resumen");
            sheet = workbook.createSheet(problemNameSheet);

            Row rowHeader = sheet.createRow(0);

            String[] header = {"Problem", "IteracionesParaEstadisticas", "Fitness Promedio", "Mejor Fitness", "Autor", "Promedio It. Mejor Fitness", "It. Mejor Fitness ", "T.Ejecucion(s)", "Poblation", "Field", "k1", "k2", "ConsultationFactor", "Iterations", "Date"};

            for (int i = 0; i < header.length; i++) {
                Cell headerCell = rowHeader.createCell(i);
                headerCell.setCellValue(header[i]);
            }

            Row rowHeaderResumen = sheetResumen.createRow(0);

            String[] headerResumen = {"Problem", "IteracionesParaEstadisticas", "Fitness Promedio", "Mejor Fitness", "Autor", "Promedio It. Mejor Fitness", "It. Mejor Fitness ", "T.Ejecucion(s)", "Poblation", "Field", "k1", "k2", "ConsultationFactor", "Iterations", "Date"};

            for (int i = 0; i < headerResumen.length; i++) {
                Cell headerCell = rowHeaderResumen.createCell(i);
                headerCell.setCellValue(headerResumen[i]);
            }
        }

        int rownum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rownum);

        int cellnum = 0;

        Cell cell = row.createCell(cellnum++);
        cell.setCellValue(problemName);

        cell = row.createCell(cellnum++);
        cell.setCellValue(iterationsForStatistics);

        cell = row.createCell(cellnum++);
        cell.setCellValue(promedioMejoresFitness);

        cell = row.createCell(cellnum++);
        if (mejorFitnessDeNEjecuciones <= bestFitnessAuthor) {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
        }
        cell.setCellValue(mejorFitnessDeNEjecuciones);

        cell = row.createCell(cellnum++);
        cell.setCellValue(bestFitnessAuthor);

        cell = row.createCell(cellnum++);
        cell.setCellValue(promedioIteracionesMejorFitness);

        cell = row.createCell(cellnum++);
        cell.setCellValue(iteracionMejorFitness);

        cell = row.createCell(cellnum++);
        cell.setCellValue(time);

        cell = row.createCell(cellnum++);
        cell.setCellValue(numberPoblation);

        cell = row.createCell(cellnum++);
        cell.setCellValue(numberField);

        cell = row.createCell(cellnum++);
        cell.setCellValue(k1);

        cell = row.createCell(cellnum++);
        cell.setCellValue(k2);

        cell = row.createCell(cellnum++);
        cell.setCellValue(consultationFactor);

        cell = row.createCell(cellnum++);
        cell.setCellValue(numberIterations);

        cell = row.createCell(cellnum++);
        cell.setCellValue(date);

        /*
        *
        *
        *
         */
        int rownumResumen = sheetResumen.getLastRowNum() + 1;
        Row rowResumen = sheetResumen.createRow(rownumResumen);

        int cellnumResumen = 0;

        Cell cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(problemName);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(iterationsForStatistics);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(promedioMejoresFitness);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        if (mejorFitnessDeNEjecuciones <= bestFitnessAuthor) {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellResumen.setCellStyle(style);
        }
        cellResumen.setCellValue(mejorFitnessDeNEjecuciones);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(bestFitnessAuthor);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(promedioIteracionesMejorFitness);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(iteracionMejorFitness);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(time);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(numberPoblation);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(numberField);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(k1);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(k2);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(consultationFactor);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(numberIterations);

        cellResumen = rowResumen.createCell(cellnumResumen++);
        cellResumen.setCellValue(date);

        try {

            FileOutputStream out
                    = new FileOutputStream(excelFileName);
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//</editor-fold>
    public Individual getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Individual bestSolution) {
        this.bestSolution = bestSolution;
    }

    public float getRpd() {
        return rpd;
    }

    public void setRpd(float rpd) {
        this.rpd = rpd;
    }

    

    public int getIteracionMejorFitness() {
        return iteracionMejorFitness;
    }

    public void setIteracionMejorFitness(int iteracionMejorFitness) {
        this.iteracionMejorFitness = iteracionMejorFitness;
    }

}
