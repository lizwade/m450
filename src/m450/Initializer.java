/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.util.*;

/**
 * Initializes the system for a student user,
 * with default parameters for the default GA
 * 
 * @author Liz
 */
public class Initializer
{
   /**
     * Sets up the module bank and the student
     */
    Initializer()
    {
       ParamSetter params = new ParamSetter();

       // Create a ModuleBank
       ModuleBank modBank = new ModuleBank("Module_data.txt");


       // Create and set up a student according to preferences
       final int year = 2012; // ought to be current year retrieved from online

       UI.StudentPreferenceChooser spc = new UI.StudentPreferenceChooser(modBank);


       Student student;
       AlgorithmRunner aRun;
       Map<Genome, Mode> solutionsToDeliver = new HashMap<Genome, Mode>();

       // do a run for each mode the student has specified.
       for (Mode mode : spc.getModesDesired() )
       {

            student = new Student(modBank, spc.getModulesDesiredAsStringArray(),
                            spc.getWorkloadDesired(), mode, year);

            aRun = new GeneticAlgorithmRunner( params, modBank, student );

            Genome bestSolutionThisRun = aRun.run();
            solutionsToDeliver.put(bestSolutionThisRun, mode);
            aRun.displayResults();
       }

       // deliver results to user
       Output output = new Output(800, 600, solutionsToDeliver); // should make height and width proportional to content
    }
 }


