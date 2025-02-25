package m450;

import java.util.*;

/**
 * Holds all the Genomes in one generation
 * @author Liz
 */

public class Population
{
   
    int bitsPerModule;
    int populationSize;
    List<Genome> myGenomes;
    Student myStudent;
    List<Module> modulesRepresented;
    final int STARTING_YEAR;

    int totalFitness;
    int meanFitness;
    int bestFitness;
    Genome bestGenome;
    int myGeneration;
    long startTime;
    

    public Population(
                      int bitsPerModule,
                      int popSize,
                      Student user)
    {
        startTime = System.currentTimeMillis();
        myStudent = user;
        this.STARTING_YEAR = user.STARTING_YEAR;
        this.bitsPerModule = bitsPerModule;
        this.populationSize = popSize;
        this.modulesRepresented = user.modulesToTake;
        myGenomes = new ArrayList<Genome>(this.populationSize);
        
        totalFitness = 0;
        meanFitness = 0;
        bestFitness = 0;
        myGeneration = 1;


        // create an initial population of genomes
        for (int i = 0; i < this.populationSize; i++ )
        {
            myGenomes.add( new Genome( this ) );
        }
    } // end of constructor


    /**
     * Judges the fitness of each member of the population
     * @return a PopRecord object holding stats for the population at this generation
     */
    public PopRecord judgePopulationFitness()
    {
        int runningTotalFitness = 0;

        for (Genome genome : myGenomes)
        {
            if (genome.fitness > bestFitness) // if this is fittest, update records
            {
                bestFitness = genome.fitness;
                bestGenome = genome;
            }
            runningTotalFitness = runningTotalFitness + genome.fitness;
        }
        this.totalFitness = runningTotalFitness;
        this.meanFitness = totalFitness / populationSize;
        long totalTimeElapsed = System.currentTimeMillis() - startTime;
        return new PopRecord(myGeneration, bestFitness, meanFitness, totalTimeElapsed, bestGenome.bitString);
    }

    /**
     * Puts solutions in fitness order
     */
    public void sortMyGenomes()
    {
        Collections.sort(myGenomes);
    }


    void produceNextGeneration(Selector selector)
    {
        myGenomes = selector.getNextGeneration(myGenomes, totalFitness);
        
        for (Genome genome : myGenomes)
        {
            genome.updateSchedule();
        }    
    }




    @Override
     public String toString()
    {
         StringBuilder temp = new StringBuilder();
         temp.append("I am the Population of generation ");
         temp.append(myGeneration);
         temp.append("\nMy members have fitness ");

         for (Genome genome : myGenomes)
         {
             temp.append(genome.fitness);
             temp.append(", ");
         }
         //temp.deleteCharAt(temp.length() ); TODO How do you delete last comma?

         temp.append("making my total fitness ");
         temp.append(totalFitness);
         temp.append(" and my mean fitness ");
         temp.append(meanFitness);
         temp.append("\nThe modules represented by each of my members, in order, are ");

         for (Module mod : this.modulesRepresented)
         {
             temp.append(mod.getCode());
             temp.append(", ");
         }


         return temp.toString();
    }

    ModuleBank getModBank()
    {
        return myStudent.getModBank();
    }

    Genome getFittestGenome()
    {
        Genome fittestGenome = null;
        int best = 0;

        for (Genome genome : myGenomes)
        {
            if (genome.fitness > best) // if this is fittest, update records
            {
                best = genome.fitness;
                fittestGenome = genome;
            }
        }
        return fittestGenome;
    }



    void doRepairs()
    {
        for (Genome genome : myGenomes)
        {
            
            // do other repairs
            genome.getMySchedule().eliminateScheduleGaps();
            
            //update the bitstring
            String newString = genome.getRepairedBitString();
            genome.replaceBitString(newString);
        }      
    }
}


