/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.util.*;

/**
 * Responsible for creating the next generation of genomes,
 * based on the previous generation, using Tournament selection
 * @author Liz
 */
public class TournamentSelector extends m450.Selector
{
    final double THRESHOLD = 0.8; // chance of fitter member becoming parent

    public TournamentSelector()
    {
        super();
    }

    Genome selectAParent()
    {
        Genome parent;
        int x = randy.nextInt(popSize);
        int y = randy.nextInt(popSize);
        //System.out.println("Examining the genomes at index " + x + " and " + y);
        Genome genomeX = currentGenomes.get(x);
        Genome genomeY = currentGenomes.get(y);

        //System.out.println("The genome at index " + x + " has fitness " + genomeX.getFitness() );
        //System.out.println("The genome at index " + y + " has fitness " + genomeY.getFitness() );

        if (randy.nextDouble() < THRESHOLD) // fitter will be chosen
        {
            //System.out.println("Choosing the FITTER of the pair.");
            if ( genomeX.getFitness() > genomeY.getFitness() )
            {
                parent = genomeX;
            }
            else
            {
                parent = genomeY;
            }
        }
        else // less fit candidate will be the parent
        {
            //System.out.println("Choosing the LESS FIT of the pair");
            if ( genomeX.getFitness() < genomeY.getFitness() )
            {
                parent = genomeX;
            }
            else
            {
                parent = genomeY;
            }
        }

        //System.out.println("Chose the genome with fitness " + parent.getFitness() );
        return parent;
    }

    public List<Genome> getNextGeneration(List<Genome> genomes, int popFitness)
    {
        this.currentGenomes = genomes;
        this.popSize = currentGenomes.size();
        newGenomes = new ArrayList<Genome>( popSize ); // will hold next gen

        int numberOfElites = doElitism();
        int spacesRemaining = popSize - numberOfElites;




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

}
