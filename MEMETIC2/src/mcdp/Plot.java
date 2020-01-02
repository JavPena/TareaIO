/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdp;


import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.ExportUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

/*import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;*/
/**
 *
 * @author francisco
 */
public class Plot {

    private static boolean modifyNumberPoblation;
    private static boolean modifyNumberIteration;
    private static boolean modifyConsultationFactor;
    private static int numberIteration;
    private static float consultationFactor;
    private static int numberIterationBestFitness;
    
    
    

    public static void createPlots(boolean modifyNumberPoblation, boolean modifyNumberIteration, boolean modifyConsultationFactor, String resultsPath, String fileName, XYSeries seriesPoblationFitness, XYSeries seriesIterationFitness, XYSeries seriesConsultationFactorFitness, int bestFitnessAuthor, int numberIterationsBestFitnnes, float consultationFactor, int numberIteration) throws IOException {

        Plot.modifyNumberPoblation = modifyNumberPoblation;
        Plot.modifyNumberIteration = modifyNumberIteration;
        Plot.modifyConsultationFactor = modifyConsultationFactor;
        Plot.numberIteration = numberIteration;
        Plot.consultationFactor = consultationFactor;
        Plot.numberIterationBestFitness = numberIterationsBestFitnnes;

        if (modifyNumberPoblation) {
            Plot.graphicPoblationFitness(resultsPath, fileName, seriesPoblationFitness, bestFitnessAuthor);
        }
       
        Plot.graphicIterationFitness(resultsPath, fileName, seriesIterationFitness, bestFitnessAuthor);
        

        if (modifyConsultationFactor) {
            Plot.graphicConsultationFactorFitness(resultsPath, fileName, seriesConsultationFactorFitness, bestFitnessAuthor);
        }



    }

    //<editor-fold defaultstate="collapsed" desc="graphicPoblationFitness">
    public static void graphicPoblationFitness(String folderName, String fileName, XYSeries seriesPoblationFitness, int bestFitnessAuthor) throws IOException {

        String problemFolder = fileName.substring(0, fileName.lastIndexOf('.')) + "/";

        File imageFolderProblem = new File(folderName + problemFolder + "image/");
        if (!imageFolderProblem.exists()) {
            imageFolderProblem.mkdirs();
        }

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        final XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(seriesPoblationFitness);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdf.format(now);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Poblacion vs Fitness " ,
                "Poblacion",
                "Fitness",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        String txt = "Iteration : " + numberIteration + " Variable : " + String.valueOf(Plot.modifyNumberIteration).toUpperCase() + "\n"
                //+ " BestFitness on Iteration : " + numberIterationBestFitness + "\n"
                + "CF : " + consultationFactor + " Variable : " + String.valueOf(Plot.modifyConsultationFactor).toUpperCase();

        //TextTitle legendText = new TextTitle(txt);
        //legendText.setPosition(RectangleEdge.TOP);
        //legendText.setHorizontalAlignment(HorizontalAlignment.RIGHT);


        xylineChart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(204, 0, 0));
        xylineChart.getPlot().setBackgroundPaint(new Color(255, 255, 255));

        xylineChart.getXYPlot().setDomainMinorGridlinePaint(Color.GRAY);
        xylineChart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        xylineChart.getXYPlot().setRangeMinorGridlinePaint(Color.GRAY);
        xylineChart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);

        xylineChart.getXYPlot().getDomainAxis().setAutoRangeMinimumSize(10);
        xylineChart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        now = new Date();
        strDate = sdf.format(now);

        int width = 640;
        int height = 480;

        File XYChart = new File(folderName + problemFolder + "image/" + strDate + "_PoblationFitness.jpeg");

        ChartUtilities.saveChartAsJPEG(XYChart, xylineChart, width, height);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="graphicIterationFitness">
    public static void graphicIterationFitness(String folderName, String fileName, XYSeries seriesIterationFitness, int bestFitnessAuthor) throws IOException {

        String problemFolder = fileName.substring(0, fileName.lastIndexOf('.')) + "/";

        File imageFolderProblem = new File(folderName + problemFolder + "image/");
        if (!imageFolderProblem.exists()) {
            imageFolderProblem.mkdirs();
        }

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesIterationFitness);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdf.format(now);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Iteracion vs Fitness ",
                "Iteracion",
                "Fitness",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

       
        xylineChart.getXYPlot().setDomainMinorGridlinePaint(Color.GRAY);
        xylineChart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        xylineChart.getXYPlot().setRangeMinorGridlinePaint(Color.GRAY);
        xylineChart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);

        xylineChart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(0, 153, 0));
        xylineChart.getPlot().setBackgroundPaint(new Color(255, 255, 255));
        xylineChart.getXYPlot().getDomainAxis().setAutoRangeMinimumSize(1.0);
        xylineChart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        now = new Date();
        strDate = sdf.format(now);

        int width = 640;
        /* Width of the image */
        int height = 480;
        /* Height of the image */

        File XYChart = new File(folderName + problemFolder + "image/" + strDate + "_IterationFitness.jpeg");
        ChartUtilities.saveChartAsJPEG(XYChart, xylineChart, width, height);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="graphicConsultationFactorFitness">
    public static void graphicConsultationFactorFitness(String folderName, String fileName, XYSeries seriesConsultationFactorFitness, int bestFitnessAuthor) throws IOException {

        String problemFolder = fileName.substring(0, fileName.lastIndexOf('.')) + "/";

        File imageFolderProblem = new File(folderName + problemFolder + "image/");
        if (!imageFolderProblem.exists()) {
            imageFolderProblem.mkdirs();
        }

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesConsultationFactorFitness);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdf.format(now);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "ConsultationFactor vs Fitness " + strDate,
                "ConsultationFactor",
                "Fitness",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ValueMarker marker = new ValueMarker(bestFitnessAuthor);
        marker.setPaint(Color.BLUE);
        marker.setLabel("Fitness Author");
        marker.setLabelTextAnchor(TextAnchor.BASELINE_LEFT);
        marker.setLabelAnchor(RectangleAnchor.LEFT);
        marker.setLabelFont(new Font("Tahoma", 1, 12));

        xylineChart.getXYPlot().setDomainMinorGridlinePaint(Color.GRAY);
        xylineChart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        xylineChart.getXYPlot().setRangeMinorGridlinePaint(Color.GRAY);
        xylineChart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);

        //xylineChart.getXYPlot().addRangeMarker(marker);

        xylineChart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(96, 96, 96));
        xylineChart.getPlot().setBackgroundPaint(new Color(255, 255, 255));
        xylineChart.getXYPlot().getDomainAxis().setAutoRangeMinimumSize(1.0);

        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        now = new Date();
        strDate = sdf.format(now);

        int width = 1280;
        /* Width of the image */
        int height = 720;
        /* Height of the image */

        File XYChart = new File(folderName + problemFolder + "image/" + strDate + "_ConsultationFactorFitness.jpeg");
        ChartUtilities.saveChartAsJPEG(XYChart, xylineChart, width, height);

    }
//</editor-fold>

   

}
