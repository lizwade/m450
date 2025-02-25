package m450;

import java.util.*;

/**
 * Simulates a chromosome, which encodes a possible Schedule
 * @author Liz
 */
public class Genome implements Comparable
{
    final Population myPop; // the population to which this Genome belongs
    String bitString; // this Genome's "DNA": the potential solution
    private final int BIT_STRING_LENGTH; // how many bits in the bit string
  
    private Schedule mySchedule; // the schedule that this genome represents
    private FitnessJudger fj;
   
    final int fitness;


    // Constructor that uses attributes of the Population to construct itself,
    // and intializes its bit string to a random pattern of 1s and 0s
    Genome(Population pop)
    {
        myPop = pop;    
        BIT_STRING_LENGTH = getGeneLength() * getModulesToBeTaken().size();
        bitString = populateBitString(BIT_STRING_LENGTH);
        mySchedule = new Schedule(getMyBank(), this);
        fj = new FitnessJudger(this, myPop.myStudent);
        fitness = fj.judgeFitness();

    }

    // Constructor that uses attributes of the Population and a provided
    // bit string to construct itself
    Genome(Population pop, String bitString)
    {
        myPop = pop;
        BIT_STRING_LENGTH = getGeneLength() * getModulesToBeTaken().size();
        this.bitString = bitString;
        mySchedule = new Schedule(getMyBank(), this);
        fj = new FitnessJudger(this, myPop.myStudent);
        fitness = fj.judgeFitness();
    }


    final List<Module> getModulesToBeTaken()
    {
        return myPop.modulesRepresented;
    }

    final int getStartingYear()
    {
        return myPop.STARTING_YEAR;
    }

    final public Schedule getMySchedule()
    {
        return mySchedule;
    }

    final int getGeneLength()
    {
        return myPop.bitsPerModule;
    }

    final int getFitness()
    {
        return fitness;
    }

   
    public void updateSchedule()
    {
        mySchedule = new Schedule(getMyBank(), this);
    }



    // returns a StringBuilder of specified length with random 1s and 0s
    private String populateBitString(int length)
    {
        StringBuilder temp = new StringBuilder( length );
        Random randy = new Random();

        for (int i = 0; i < length; i++ )
        {
            temp.append( randy.nextInt(2) ); // appends a 0 or a 1
        }

        return temp.toString();

    }




    /**
     * Returns a sorted map in which each key is an OU module
     * and each value holds the month and year for starting that module, as
     * represented by the current state of the genome's bitString. The month
     * and year are held in a Calendar object for easy comparisons, but the
     * other fields of the Calendar object are not used.
     */
        public SortedMap<Module, Calendar> getModulesWithDates()
    {
        SortedMap<Module, Calendar> modulesWithDates;
        modulesWithDates = new TreeMap<Module, Calendar>();

        // converts this object's bitString into a gene for each OU module
        String[] genes = chunkGenome();

        // stores each Module with the Calendar derived from its chunk of Genome
        for (int i = 0; i < getModulesToBeTaken().size(); i++)
        {
            modulesWithDates.put( getModulesToBeTaken().get(i),
                                  convertGeneToCalendar( genes[i], getModulesToBeTaken().get(i) ) );
        }
        return modulesWithDates;
    }




    private Calendar convertGeneToCalendar(String gene, Module mod)
    {
        if ( gene.length() != 5)
        {
           throw new UnsupportedOperationException(
                   "System assumes gene length of 5 and cannot"
                 + "currently handle other gene lengths. Sorry.");
        }

        /* These constants currently assume a gene length of 5 with
         * the first 3 bits representing a number of years from now
         * to take a module, ranging from 0 - 7,
         * and the final 2 bits representing a starting month.
         * 00 represents February
         * 01 represents May
         * 10 represents October
         * 11 represents November
         */
        final int YEAR_BEGIN_INDEX = 0; // inclusive
        final int YEAR_END_INDEX = 3; // exclusive

        final int MONTH_BEGIN_INDEX = 3; // inclusive
        final int MONTH_END_INDEX = 5; // exclusive

         // reads the gene for the year part
        String yearPart = gene.substring(YEAR_BEGIN_INDEX, YEAR_END_INDEX);

         // reads the gene for the month part
        String monthPart = gene.substring(MONTH_BEGIN_INDEX, MONTH_END_INDEX);

        // get the value of the string when treated as a binary number
        int yearsFromNow = Integer.parseInt(yearPart, 2); // 2 for binary


        int month = 0;

        if (monthPart.equals("00") )
        {
            month = 1; // February
        }
        else if(monthPart.equals("01"))
        {
            month = 4; // May
        }
        else if(monthPart.equals("10"))
        {
            month = 9; // October
        }
        else if(monthPart.equals("11"))
        {
            month = 10; // November
        }

        //System.out.println("month = " + month); // DEBUG


        // check if module can actually be started in the given month
        // and if not, increment month until we find the first one that works.

        while ( ! mod.canBeStarted(month) )
        {
            month++;
            if (month == 12) { month = 0;}
           // System.out.println(" Module " + modCode + ": month = " + month); // DEBUG
        }


        Calendar cal = new GregorianCalendar( getStartingYear() + yearsFromNow,
                                              month,
                                              1); // Just use 1st day of the month

        return cal;
    }

    void replaceBitString(String newBitString)
    {
        this.bitString = newBitString;
    }



    /*
     * Helper method used by the fillMap() method
     * which takes the object's bitString and returns it as
     * an array of strings each representing the gene for one Module
    */
     private String[] chunkGenome()
    {
        String[] geneArray = new String[getModulesToBeTaken().size()];

        // loop increments counter by bits per gene +1  on each iteration
        // thereby moving to the start of a new gene each time
        for (int i = 0; i < getModulesToBeTaken().size(); i++)
        {
            // finds appropriate substring of the genome and puts it in array
            int startBit = i * getGeneLength();
            geneArray[i] = bitString.substring( startBit, startBit + getGeneLength());
        }
        return geneArray;
    
    }



    @Override
    public String toString()
    {
        return "I am a genome with a " + BIT_STRING_LENGTH +
                " length bitString, representing Open University modules"
                + " and the dates for studying them,"
                + " with the bits " + bitString +
                ".\nEach Module is represented by " + getGeneLength() + " bits."
                +" My fitness is " + fitness + ".";
    }


    public int compareTo(Object o)
    {
        if ( !(o instanceof Genome))
        {
            throw new IllegalArgumentException("Not a Genome object!");
        }
            Genome anotherGenome = (Genome) o;
            return anotherGenome.fitness - this.fitness;
    }

    private void updateMySchedule()
    {
        mySchedule = new Schedule( getMyBank(), this);
    }

    final ModuleBank getMyBank()
    {
        return myPop.getModBank();
    }


    char flip(char charAt)
    {
        if (charAt == '0')
        {
            return '1';
        }
        else
        {
            return '0';
        }
    }

    /*
     * Returns a BitString representation of the Schedule in its current
     * state (which may have been altered by repair operators have been) 
    */
    String getRepairedBitString()
    {
        String newBitString = (convertMapToBitString(mySchedule.mapToProcess));
        return newBitString;
    }


    private String convertMapToBitString(SortedMap<Module, Calendar> mapToProcess)
    {
         /* This system assumes a gene length of 5 with
         * the first 3 bits representing a number of years from now
         * to take a module, ranging from 0 - 7,
         * and the final 2 bits representing a starting month.
         * 00 represents February
         * 01 represents May
         * 10 represents October
         * 11 represents November
         */
       
        // for each module in the BitString in turn,
        // find the recommended start date
        // and write it as bits
        StringBuilder sb = new StringBuilder();
                
        for (Module module : myPop.modulesRepresented)
        {
            Calendar correctDate = mapToProcess.get(module);

            // convert year to years from now in binary rep
            int correctYearsFromNow = (correctDate.get(Calendar.YEAR) - this.getStartingYear() );
            String yearsAsBitString = Integer.toBinaryString(correctYearsFromNow);

            while (yearsAsBitString.length() < 3) // make sure it's 3 chars long
            {
                yearsAsBitString = "0" + yearsAsBitString;
            }
            sb.append(yearsAsBitString);

            // convert month to binary rep
            int correctMonth = correctDate.get(Calendar.MONTH);

            if (correctMonth == 1)
            {
                sb.append("00"); // February
            }
            else if(correctMonth == 4)
            {
                sb.append("01") ; // May
            }
            else if(correctMonth == 9)
            {
                sb.append("10"); // October
            }
            else if(correctMonth == 10)
            {
                sb.append("11"); // November
            }
      
        } // process the next module

        if (sb.length() != myPop.modulesRepresented.size() * 5)
        {
            System.err.println("BitString ended up the wrong size");
        }

        return sb.toString();
    }



}
