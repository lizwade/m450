package m450;

import static m450.Mode.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;


/**
 * Drives the algorithm to generate a solution.
 *
 * @author Liz
 */
abstract class AlgorithmRunner
{
    long startTime;
    ModuleBank modBank;
    ParamSetter params;
    Student myStudent;
    Population poppy;
    List<PopRecord> popRecords;
    Genome bestSolutionInRun;
    int consecutiveGenerationsWithNoImprovement = 0;
    int generationBestSolutionFound;
    UI.StudentPreferenceChooser spc;

    /** Constructor that requires a ParamSetter object to be passed
     *
     * @param params
     */
    AlgorithmRunner(ParamSetter params)
    {
        this.params = params;
        this.popRecords = new ArrayList<PopRecord>();
    }

     AlgorithmRunner(ParamSetter params, ModuleBank modBank, Student student)
    {
        this(params);
        this.modBank = modBank;
        this.myStudent = student;
    }
   
    /** Method that drives the algorithm
     *
     * @return the fittest solution found in the whole run
     */
    Genome run()
    {
       if (Main.SCIENTIST_MODE == true)
       {
           setUp(); // set up module bank and student if not using user input
       }
       createPopulation();       
       doGenerations();
       return bestSolutionInRun; 
    } 

    /**
     * Sets up the module bank and the student
     */
    void setUp()
    {
       // Create a ModuleBank
       if (modBank == null)
       {
           modBank = new ModuleBank("Module_data.txt");
       }

       // Create and set up a student according to preferences (first run only)
       if (myStudent == null)
       {
            myStudent = setUpStudent();
       }
    }


    /**
     *
     * TODO: A user would choose these themselves through a simple interface
     *
     * Makes up some lists of Modules and passes one to the
     * Student constructor, along with her max monthly hours to study,
     * the year she wants to start, and her preferred solution-finding mode
     * @return a Student object
     */
    Student setUpStudent()
    {
       
        final int year = 2012; // ought to be current year retrieved from online

        
        if (Main.SCIENTIST_MODE == false)
        {
            spc = new UI.StudentPreferenceChooser(modBank);

            // TODO need to adjust so that all desired modes get to run.
            return new Student (modBank, spc.getModulesDesiredAsStringArray(),
                spc.getWorkloadDesired(), m450.Mode.MODERATE, year);
        }
        else
        {

           String[] fourCodes = { "M255", "M257", "M366", "DD303"};

           String[] tenCodes = {"M450", "MST121", "M366", "M255", "M257",
                                    "M253", "DD303", "S104", "DSE212", "M150"};

           final int hours = 70;
           final Mode mode = MODERATE;

           return new Student( modBank, tenCodes, hours, mode, year );

        }


    }


    /**
    * Creates a new population. Overridden by subclasses
    */
    abstract void createPopulation();
    



    void doGenerations()
    {
       //start the clock!
       startTime = System.currentTimeMillis();

        // loop until we've reached our desired number of generations
       for (int i = 0; i < params.NUM_OF_GENERATIONS; i++)
       {
           // do one generation and get the fittest genome in it
           Genome bestSolutionThisGeneration = doAGeneration();

           // if it's the 1st generation or we have new high score, update stats
           if (i == 0 ||
               bestSolutionThisGeneration.fitness > bestSolutionInRun.fitness)
           {
               bestSolutionInRun = bestSolutionThisGeneration;
               consecutiveGenerationsWithNoImprovement = 0;
           }
           else  // no improvement
           {
               consecutiveGenerationsWithNoImprovement++;
           }
       }
       // calculate when the best solution was found
       // TODO could use this to keep doing gens until improvement stops
       generationBestSolutionFound = params.NUM_OF_GENERATIONS
               - consecutiveGenerationsWithNoImprovement++;
    }


    /**
    * 
    *  evolves population one generation and returns fittest member so far
    */
    abstract Genome doAGeneration();
    

    /**
     * Creates a PopulationVisualiser object to show the current generation
     */
    void doVisualisation()
    {    
            String title = "Generation " + poppy.myGeneration
                        + " - Mean fitness: " + poppy.meanFitness
                        + " - Best fitness: " + poppy.bestFitness; 

            PopulationVisualiser pv = new PopulationVisualiser(title,
                    1000, 1400, poppy, params.NUM_OF_POP_MEMBERS_TO_VISUALIZE);
    }

    /**
    * Saves the entire contents of the collection of popRecords for this run
    */
    void writePopRecords(FileWriter fw)
    {
        try
        {
            // get each PopRecord to save its data
            for (PopRecord rec : popRecords)
            {
                rec.writeToFile(fw); 
            }

            // add info about winner to end of the line
            fw.write("" + generationBestSolutionFound);

            fw.write("\n"); // start new line ready for next run

        }
        catch (Exception e)
        {
            System.out.println("Problem accessing or writing file: " + e);
        }


    }



    Student getMyStudent()
    {
        return myStudent;
    }



    void displayResults()
    {
         if (this instanceof HillClimbingAlgorithmRunner )
            {
                  System.out.print("Hill climbing algorithm, ");
            }

         else if (this instanceof GeneticAlgorithmRunner )
            {
                  System.out.print("Genetic algorithm, ");
            }

         if (params.SEL_TYPE == m450.SelectionType.ROULETTE )
            {
                  System.out.print("Roulette selection, ");
            }

         else if (params.SEL_TYPE == m450.SelectionType.TOURNAMENT )
            {
                  System.out.print("Tournament selection, ");
            }

         if (myStudent.getPreferredMode() == Mode.AGGRESSIVE)
         {
             System.out.println("Aggressive mode: ");
         }
         if (myStudent.getPreferredMode() == Mode.CONSERVATIVE)
         {
             System.out.println("Conservative mode: ");
         }
         if (myStudent.getPreferredMode() == Mode.MODERATE)
         {
             System.out.println("Moderate mode: ");
         }
         if (myStudent.getPreferredMode() == Mode.INSANE)
         {
             System.out.println("Insane mode: ");
         }

         System.out.print( popRecords.get(popRecords.size()-1) );
         System.out.print( ". Generation best solution found = "
                              + generationBestSolutionFound + "\n");

    }

}
