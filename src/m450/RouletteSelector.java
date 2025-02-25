/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.util.*;
/**
 * Responsible for creating the next generation of genomes,
 * based on the previous generation, using a biased roulette wheel
 * @author Liz
 */
public class RouletteSelector extends m450.Selector
{
    RouletteWheel myWheel;
   

    public RouletteSelector()
    {
        super();
       

    }

    Genome selectAParent()
    {
        return myWheel.selectAGenome();
    }

     public List<Genome> getNextGeneration(List<Genome> genomes, int popFitness)
    {
        this.currentGenomes = genomes;
        this.popSize = currentGenomes.size();
        newGenomes = new ArrayList<Genome>( popSize ); // will hold next gen

        int numberOfElites = doElitism();
        int spacesRemaining = popSize - numberOfElites;


        // create a biased roulette wheel specific to this generation of genomes
        myWheel = new RouletteWheel(popFitness);


        while (spacesRemaining > 0)
        {
            // pick a pair and send to Breeder
            Genome genomeA = selectAParent();
            Genome genomeB = selectAParent();
            Genome[] pairToAdd = breeder.breed(genomeA, genomeB);

            // try to add the new genome and decrement spaces remaining
            // if successsful
            for (Genome genome : pairToAdd)
            {
                 if ( includeInNextGen(genome) ) {spacesRemaining--;}
            }
        }
        return newGenomes;
    }



    // inline class
    private class RouletteWheel
    {
        float[] borderlines;

        RouletteWheel(int popFitness)
        {
            // create an array to hold as many borderlines as there are genomes
            borderlines = new float[currentGenomes.size()];
            float previousBorder = 0;

            for (int i = 0; i < currentGenomes.size(); i++)
            {


                int fitness = currentGenomes.get(i).fitness;

                //TODO - should check these values are positive
                // or throw exception (they should be though!)
                float slice = (fitness / (float) popFitness);

                borderlines[i] = previousBorder + slice;
                previousBorder = borderlines[i];
            }
        }

        float spin()
        {
            return randy.nextFloat();
        }

        Genome selectAGenome()
        {
            float result = spin();
           //System.out.println( "Result of spin is " + result);

            Genome selectedGenome = null;
            for (int i = 0; i < borderlines.length; i++)
            {
                if (result < borderlines[i] )
                {
                    selectedGenome = currentGenomes.get(i);
                    //System.out.println("Selecting genome #" + i);
                    return selectedGenome;
                }
            }
            // if we are here it is because a very high random float has
            // exceeded the last border due to rounding, so return last Genome

            System.out.println("Huge float! Selecting last genome");
            return currentGenomes.get( currentGenomes.size()-1 );
         }


        @Override
        public String toString()
        {
            StringBuilder temp = new StringBuilder();
            temp.append("I am a biased roulette wheel with ");
            temp.append( borderlines.length );
            temp.append(" borders, at " );

            for (Float border : borderlines)
            {
               temp.append(border);
               temp.append(", ");
            }

            return temp.toString();
        }

    }
}

