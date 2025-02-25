package m450;

import java.util.*;

/**
 * Responsible for creating the next generation of genomes,
 * based on the previous generation.
 * @author Liz
 */
abstract class Selector
{
    final int PERCENTAGE_CONSIDERED_ELITE = 5;
    final int MINIMUM_CONSIDERED_ELITE = 1;
    
    List<Genome> currentGenomes;
    int popSize;
    List<Genome> newGenomes;
    Random randy;
    Breeder breeder;
    
    public Selector()
    {
        randy = new Random();
        breeder = new Breeder();
    }

    abstract public List<Genome> getNextGeneration(List<Genome> genomes, int popFitness);
   
    abstract Genome selectAParent();


    int calculateNumberOfEliteGenomes()
    {
        int numberOfEliteGenomes = ( popSize
                                    * PERCENTAGE_CONSIDERED_ELITE ) /100;

        if (numberOfEliteGenomes < MINIMUM_CONSIDERED_ELITE )
        {
            numberOfEliteGenomes = MINIMUM_CONSIDERED_ELITE;
        }
        //System.out.println("Preparing to preserve " + numberOfEliteGenomes
         //                   + " elite genome(s)");
        return numberOfEliteGenomes;
    }


    /*
     * Attempts to add a new copy of the Genome object genome 
     * to the next generation. Succeeds and returns true if the
     * next generation is not already full, otherwise returns false.
     */
    boolean includeInNextGen(Genome genome)
    {
        if (newGenomes.size() < popSize )
        {
            // still space in the next generation, so add this one
            return newGenomes.add(genome);
        }

        // new generation is full!
        return false;
    }

    /*
     * Causes some individuals to pass into the next generation unscathed,
     * according to the constants defined at the top of this class.
     * Returns an int representing the number of individuals that were added
     */

    int doElitism()
    {
        int numberToPreserve = calculateNumberOfEliteGenomes();
        int numberAdded = 0;

        // genomes are already sorted by fitness, so add first i to next gen
        for (int i = 0; i < numberToPreserve; i++)
        {
            // send it to be added; use the return value to check it really was
            if (includeInNextGen( currentGenomes.get(i) ) )
            {
                numberAdded++;
            }
        }

        return numberAdded;
    }

}
