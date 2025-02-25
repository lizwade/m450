/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.io.*;

/**
 * Holds a record of the key statistics for a population at a particular stage,
 * including which generation the population is at, how many millisaconds have
 * elapsed since the algorithm began, the fitness score and the bitString of
 * the fittest member, and the mean fitness score of the entire population.
 *
 * @author Liz
 */
public class PopRecord
{

     int gen;
     int best;
     int mean;
     long time;
     String bestBitString;

     // constructor that sets the object's attributes to the received values
     PopRecord(int myGeneration, int bestFitness, int meanFitness, long totalTimeElapsed, String bitString)
     {
         gen = myGeneration;
         best = bestFitness;
         mean = meanFitness;
         time = totalTimeElapsed;
         bestBitString = bitString;
     }

    @Override
     public String toString()
    {
             return "Generation " + gen + ": "
                    + time + " ms elapsed. Best fitness found = "
                    + best + ". Mean fitness = " + mean;
    }

    //TODO
    public void visualiseWinner()
    {
        //ScheduleVisualiser sv = new ScheduleVisualiser();
    }

     // writes gen number, times since start, best fitness, and mean fitness
     // delimited by commas
     void writeToFile(FileWriter fw) throws IOException
    {
              fw.write("" + gen); // write gen # as String
              fw.write(",");
              fw.write("" + time); // write time up to that gen as String
              fw.write(",");
              fw.write("" + best); // write best score that gen as String
              fw.write(",");
              fw.write("" + mean); // write mean score that gen as String
              fw.write(",");
              fw.write(bestBitString); // write winner that gen as String
              fw.write(",");


     }

}
