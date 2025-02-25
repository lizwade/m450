/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.io.*;
import static m450.Mode.*;
import java.util.*;

/**
 * Performs runs and collects statistics
 * @author Liz
 */
public class StatCollector
{

    private final int NUMBER_OF_RUNS; // later, set this with dialog box
    ParamSetter params;
    private SortedMap <Genome, Long > solutionsWithTimes;
    StudyMonth earliestFinishDateFound;
    Mode mode;
    FileWriter outputWriter;
    UI.AlgorithmChooser ac;
    int sumOfBestScores = 0;


    public StatCollector()
    {
    ac = new UI.AlgorithmChooser();
    NUMBER_OF_RUNS = ac.getNumOfRuns();

    params = new ParamSetter(ac.getAlgorithmType(),
                             ac.getSelectionType(),
                             ac.getNumOfGenerations(),
                             ac.getNumOfIndividuals(),
                             m450.VisType.NONE);
    
    solutionsWithTimes = new TreeMap<Genome, Long>();
    
    // initialise to a date far in future: Jan 3000 (ought to be last possible month of Schedule, ie number of months
    earliestFinishDateFound = new StudyMonth( 3000, 0 );

    setUpOutputFile(ac.getFilenameForSave() );
    doRuns();
    closeOutputFile();
    displayStats(1); // this is number to show. tweak later

    System.out.println("Average of best solutions found across all runs = "
            + sumOfBestScores / NUMBER_OF_RUNS);

    }



    private void doRuns()
    {


        for (int i = 1; i <= this.NUMBER_OF_RUNS; i++)
        {

            System.out.println("Run " + i + "...");
            

            // do a run and do stuff with the returned (best) genome
            Genome bestSolution = doRun(i);

            sumOfBestScores = sumOfBestScores + bestSolution.fitness;
      
            // update earliest finish month across all runs
            // if this winner finishes quicker than any others we've found
            StudyMonth lastMonth = bestSolution.getMySchedule().lastActiveMonth;

            if (lastMonth.getTimeInMillis()
                    < this.earliestFinishDateFound.getTimeInMillis() )
            {
                earliestFinishDateFound = lastMonth;
            }
        }
    }

    // performs an entire run and returns the fittest solution found
    private Genome doRun(int run)
    {
        AlgorithmRunner aRun;

        if (params.ALG_TYPE == m450.AlgorithmType.GENETIC)
        {
            aRun = new GeneticAlgorithmRunner( params );
        }
        else
        {
            aRun = new HillClimbingAlgorithmRunner( params );
        }


        Genome bestSolutionThisRun = aRun.run();
        aRun.displayResults();

        //Record what the mode was so we can use it in the final display
        mode = aRun.getMyStudent().getPreferredMode();



        // put the best solution and generation it was found in the map
        solutionsWithTimes.put(aRun.bestSolutionInRun, (long) aRun.generationBestSolutionFound);
        
        //save the collection of popRecords for this run to the file
        aRun.writePopRecords(outputWriter);




        return bestSolutionThisRun; // also return this best solution for more stat analysis eg milliseconds
    }

    private void displayStats(int numberToShow)
    {
        String title = "Fittest solutions found in "
                        + NUMBER_OF_RUNS + " runs with " 
                        + params.NUM_OF_GENERATIONS + " generations and "
                        + params.NUM_OF_INDIVIDUALS + " individuals. Mode - "
                        + mode +
                        ". Earliest finish date: " + earliestFinishDateFound;
        
        StatVisualiser sv = new StatVisualiser(title,
                    1000, 1400, solutionsWithTimes, numberToShow);
    }

    private void setUpOutputFile(String filename)
    {
       

        // create the background info to go in the output file
        String info =   params.NUM_OF_GENERATIONS + " generations and "
                        + params.NUM_OF_INDIVIDUALS + " individuals. Mode - "
                        + mode + "\n";

        try
        {
            File toWrite = new File(filename);
            outputWriter = new FileWriter(toWrite);
            outputWriter.write(info);
            
            // now set up column headings (one run per row)
            for (int i = 0; i < params.NUM_OF_GENERATIONS; i++)
            {
                outputWriter.write("gen,time,best,mean,bitString,");
            }
            outputWriter.write("Gen winner found"); // one more column
            outputWriter.write("\n");
        }
        catch (Exception e)
        {
            System.out.println("Trouble opening or writing file");
        }
    }

    private void closeOutputFile() {
        try
        {
            outputWriter.close();
        }
        catch (Exception e)
        {
            System.out.println("Problem closing file");
        }
    }

}
