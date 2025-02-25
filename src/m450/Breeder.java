package m450;

import java.util.*;

/**
 * Performs genetic operations on Genome objects
 * @author Liz
 */
public class Breeder
{
    Population thePop;
    final float CHANCE_OF_MUTATION = 0.2f;
    final float CHANCE_OF_CROSSOVER = 0.55f;
    Random randy;

    Breeder()
    {
        randy = new Random();
    }

    Genome[] breed(Genome genomeA, Genome genomeB)
    {

        // get the genomes population so we can use it for replacement genome
        this.thePop = genomeA.myPop;
        // possibly mutate genomeA
        if (shouldMutate() )
        {
            genomeA = mutate(genomeA);
        }

        // possibly mutate genomeB
        if (shouldMutate() )
        {
            genomeB = mutate(genomeB);
        }

        Genome[] pair = {genomeA, genomeB};

        //possibly do crossover
        if (shouldCrossover() )
        {
            pair = doCrossover(pair);
        }
        return pair;
    }


    private boolean shouldCrossover()
    {
        return randy.nextFloat() < CHANCE_OF_CROSSOVER;
    }

    private Genome[] doCrossover(Genome[] pair)
    {
        //System.out.println("Doing crossover...");
        StringBuilder bitStringA = new StringBuilder(pair[0].bitString);
        StringBuilder bitStringB = new StringBuilder(pair[1].bitString);
        int length = bitStringA.length();

        int crossoverPoint = randy.nextInt(length);

        String backOfA = bitStringA.substring(crossoverPoint);
        String backOfB = bitStringB.substring(crossoverPoint);

        bitStringA.replace(crossoverPoint, length, backOfB);
        bitStringB.replace(crossoverPoint, length, backOfA);

        Genome childA = new Genome( thePop, bitStringA.toString() );
        Genome childB = new Genome( thePop, bitStringB.toString() );


        return new Genome[] {childA, childB};

    }

    private boolean shouldMutate()
    {
         return randy.nextFloat() < CHANCE_OF_MUTATION;
    }




    private Genome mutate(Genome genome)
    {
        //System.out.println("Mutating..");
        StringBuilder bitString = new StringBuilder(genome.bitString);
        int bitToMutate = randy.nextInt(bitString.length() );
        //System.out.println("Getting the bit at position " + bitToMutate);

        char newBit = genome.flip( bitString.charAt(bitToMutate) );
        //System.out.println(" and flipping it to " + newBit);

        bitString.setCharAt(bitToMutate, newBit);

        Genome mutated = new Genome(thePop, bitString.toString() );

        return mutated;
    }



}
