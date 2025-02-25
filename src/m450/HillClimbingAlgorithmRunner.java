/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.util.Random;

/**
 *Drives a Hill Climbing algorithm to generate a solution
 *
 * @author Liz
 */
public class HillClimbingAlgorithmRunner extends m450.AlgorithmRunner
{
    HillClimbingAlgorithmRunner(ParamSetter params)
    {
        super(params);
    }

/**
 *  Creates population with one Genome (only one is needed for Hill Climbing)
 */
    @Override
    void createPopulation()
    {
         poppy = new Population(params.BITS_PER_MODULE, 1, myStudent );
    }

/**
    *
    *  evolves population one generation and returns fittest member so far
    */
    @Override
    Genome doAGeneration()
    {
            // judge and sort this generation
            popRecords.add( poppy.judgePopulationFitness() );

            Genome genome = poppy.myGenomes.get(0);
            poppy.myGenomes.set(0, hillClimb(genome) );
         

            // do visualisation if appropriate
            if (params.VIS_TYPE != m450.VisType.NONE)
            {
                doVisualisation();
            }
            
            poppy.myGeneration++;
            return poppy.myGenomes.get(0); 
    }
    
    /**
     * Applies a hillclimbing algorithm to the argument, by mutating a random
     * bit, and replacing the original version with the mutated version if its
     * fitness is higher. This is repeated for a number of times equal to the
     * number of individuals specified for a GA (making it equivalent to one
     * generation of a GA) after which the resultant Genome is returned
     * @param A genome object
     * @return a Genome of improved or equal fitness
     */
    private Genome hillClimb(Genome genome)
    {
        
        Random randy = new Random();
        int steps = params.NUM_OF_INDIVIDUALS; // for consistency with GA
        for (int i = 0; i < steps; i++)
        {
            StringBuilder bitString = new StringBuilder(genome.bitString);
            int bitToMutate = randy.nextInt(bitString.length() );
            char newBit = genome.flip( bitString.charAt(bitToMutate) );
            bitString.setCharAt(bitToMutate, newBit);
            Genome mutated = new Genome(poppy, bitString.toString() );

            if (mutated.fitness >= genome.fitness)
            {
                //System.out.println("Found a fitter one at generation " + poppy.myGeneration + " step " + i);
                genome = mutated;
            }
        }
        return genome;
    }




    

}



