    /*
 * Best PARAMETERS for MODES // TODO make mode an enum in Student object
 *
 * "CONSERVATIVE: Stick to my bandwidth" mode:
 * LPM=2; BPM=1; SSW=1; BSW=1; PSS=1000; PBS=1000;
 *
 *
 * "MODERATE: Stretch bandwidth SLIGHTLY to finish sooner" mode:
 * LPM=10; BPM=1; SSW=1; BSW=1; PSS=1000; PBS=1000;
 *
 *
 * "AGGRESSIVE: Stretch bandwidth A FAIR BIT to finish sooner" mode:
 * LPM=10; BPM=1; SSW=2; BSW=1; PSS=1000; PBS=1000;

 *
 * "INSANE: I'll take everything now!" mode
 * LPM=10; BPM=1; SSW=1; BSW=0; PSS=1000; PBS=1000;
 * Note: for 10 modules, LPM=5 works better (LPM=10 kills diversity too soon)
 *
 *
 */
package m450;

import java.util.*;
import static m450.Mode.*;

/**
 * Judges the Schedule object produced from a Genome object,
 * and assigns an appropriate fitness score to the Genome object
 * @author Liz
 */
public class FitnessJudger
{
    //constants to tweak to change mode // TODO Implement as modes (enums?)
    final int LENGTH_PENALTY_MULTIPLIER;
    final int BANDWIDTH_PENALTY_MULTIPLIER;
    final int LEVEL_PENALTY_MULTIPLIER;

    final int SHORTNESS_SCORE_WEIGHTING;
    final int BANDWIDTH_SCORE_WEIGHTING;
    final int LEVEL_SCORE_WEIGHTING;
    
    final int PERFECT_SHORTNESS_SCORE = 1000;
    final static int PERFECT_BANDWIDTH_SCORE = 1000;
    final int PERFECT_LEVEL_SCORE = 1000;

    final int PENALTY_PER_MODULE_OUT_OF_LEVEL_ORDER = 75;

    Genome genome;
    Schedule sched;
    Student student;

    int shortnessScore;
    int withinBandwidthScore;
    int levelScore;

    /*
     * Constructor takes Schedule object to judge, and a
     * Student object for preferences to judge against
     */
    public FitnessJudger( Genome genome, Student student )
    {
        this.genome = genome;
        this.sched = genome.getMySchedule();
        this.student = student;

        // tweak constants for level score (later these might be part of Mode)
        LEVEL_PENALTY_MULTIPLIER = 1;
        LEVEL_SCORE_WEIGHTING = 1;


        //PERFECT_SHORTNESS_SCORE = sched.TOTAL_MONTHS_IN_SCHEDULE;

        // set params
        if (student.preferredMode == CONSERVATIVE)
        {
            LENGTH_PENALTY_MULTIPLIER = 2;
            BANDWIDTH_PENALTY_MULTIPLIER = 1;

            SHORTNESS_SCORE_WEIGHTING = 1;
            BANDWIDTH_SCORE_WEIGHTING = 1;
        }

        else if(student.preferredMode == INSANE)
        {
            LENGTH_PENALTY_MULTIPLIER = 10;
            BANDWIDTH_PENALTY_MULTIPLIER = 1;

            SHORTNESS_SCORE_WEIGHTING = 1;
            BANDWIDTH_SCORE_WEIGHTING = 0;
        }

        else if(student.preferredMode == AGGRESSIVE)
        {
            LENGTH_PENALTY_MULTIPLIER = 10;
            BANDWIDTH_PENALTY_MULTIPLIER = 1;

            SHORTNESS_SCORE_WEIGHTING = 2;
            BANDWIDTH_SCORE_WEIGHTING = 1;
        }

        else // go with MODERATE mode
        {
            LENGTH_PENALTY_MULTIPLIER = 10;
            BANDWIDTH_PENALTY_MULTIPLIER = 1;

            SHORTNESS_SCORE_WEIGHTING = 1;
            BANDWIDTH_SCORE_WEIGHTING = 1;
        }



    }


    // returns a score representing this genome's fitness
    // higher is better
    public int judgeFitness()
    {
        int fitness;
   
        shortnessScore = judgeLength();
        withinBandwidthScore = judgeMonthlyHoursAgainstStudentMax();
        levelScore = judgeLevel();

        fitness = (shortnessScore * SHORTNESS_SCORE_WEIGHTING)
                + (withinBandwidthScore * BANDWIDTH_SCORE_WEIGHTING )
                + (levelScore * LEVEL_SCORE_WEIGHTING);

        if (fitness < 0)
        {
            fitness = 1; // even terrible ones get a tiny chance
        }


        //System.out.println("Returning fitness for this schedule of " + fitness);
        return fitness;
  
    }

    /**
    Returns a value where 1 point has been subtracted for every month
    in the Schedule
    */
    private int judgeLength()
    {
        int score = PERFECT_SHORTNESS_SCORE -
                (sched.totalDuration * LENGTH_PENALTY_MULTIPLIER);
        //System.out.println("Shortness score is " + score);

        return score;

    }

    /*
     * Returns a score which penalizes schedule for each month
     * which exeeeds student's max hours per month
     */
    private int judgeMonthlyHoursAgainstStudentMax()
    {
        int runningBandwidthPenalty = 0;
        int BandwidthPenaltyThisMonth = 0;

        for (StudyMonth aMonth: sched.myMonths)
        {
            // see how many hours we are over the Student's limit
            int excessiveHours = aMonth.getTotalMonthlyWorkload() 
                                  - student.maxMonthlyHours;
            
            
            // penalise the schedule for exceeding the Student's limit this month
            if (excessiveHours > 0)
            {
                //System.out.println(aMonth + " is " + excessiveHours  + " hours over limit");
                
                BandwidthPenaltyThisMonth = excessiveHours
                                       * BANDWIDTH_PENALTY_MULTIPLIER;

                //System.out.println("Applying penalty of " + BandwidthPenaltyThisMonth);

                runningBandwidthPenalty = runningBandwidthPenalty + BandwidthPenaltyThisMonth;
            }   
        }

        //System.out.println("Adherence to study limit score is " + PERFECT_BANDWIDTH_SCORE
          //      + " - " + runningBandwidthPenalty * BANDWIDTH_PENALTY_MULTIPLIER
            //    + " = " + (PERFECT_BANDWIDTH_SCORE -
              //  (runningBandwidthPenalty * BANDWIDTH_PENALTY_MULTIPLIER) )  );
        
        return PERFECT_BANDWIDTH_SCORE - 
                (runningBandwidthPenalty * BANDWIDTH_PENALTY_MULTIPLIER);
    }

    private int judgeLevel()
    {
        Map<Module, Calendar> theMap = genome.getModulesWithDates();

        int runningLevelOrderPenalty = 0;

        // get a key value pair from the map and assign to local variables
        for (Map.Entry<Module,Calendar> e : theMap.entrySet())
        {
            Module mod = e.getKey();
            Calendar startDate = e.getValue();

            for (Map.Entry<Module,Calendar> otherEntry : theMap.entrySet())
            {
                Module otherMod = otherEntry.getKey();
                Calendar otherStartDate = otherEntry.getValue();

                if (
                     otherMod.compareTo(mod) < 0 // if otherMod is easier
                     && otherStartDate.getTimeInMillis()
                            > startDate.getTimeInMillis() // but starts later
                     )
                {
                    //penalise!
                    runningLevelOrderPenalty = runningLevelOrderPenalty
                            + PENALTY_PER_MODULE_OUT_OF_LEVEL_ORDER;
                }
              //  System.out.print(runningLevelOrderPenalty + ", ");
            }
        }
        //System.out.println("returning" + (PERFECT_LEVEL_SCORE - runningLevelOrderPenalty) );
        return PERFECT_LEVEL_SCORE - runningLevelOrderPenalty;


    }   
}
