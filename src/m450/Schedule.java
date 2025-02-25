package m450;

import java.util.*;

/**
 * A possible solution, decoded from the Genome object into a series of
 * months, some of which hold start dates for modules
 * @author Liz
 */
public class Schedule
{
    final int STARTING_YEAR;
    final int TOTAL_MONTHS_IN_SCHEDULE = 108; // 9 years * 12 months

    private ModuleBank myBank;
    SortedMap<Module, Calendar> mapToProcess;
    SortedSet<StudyMonth> myMonths;
    StudyMonth firstMonthInSchedule;
    StudyMonth lastActiveMonth;
    int totalDuration;
    ScheduleVisualiser visualiser;



     public Schedule(ModuleBank bank, Genome genome)
    {
        myBank = bank;
        STARTING_YEAR = genome.getStartingYear();
        mapToProcess = genome.getModulesWithDates();
        myMonths = new TreeSet<StudyMonth>();
        buildCalendar();
        processMap();
        firstMonthInSchedule = setFirstMonthInSchedule();
        lastActiveMonth = setLastActiveMonth();
        totalDuration = setTotalDuration();
    }

    
    private void processMap()
    {
        
        for (Map.Entry entry : mapToProcess.entrySet())
        {
            Module module = (Module) entry.getKey();
            Calendar cal = (Calendar) entry.getValue();
            recordModule( module, cal);
        }
    }


    // for each module extracted from the map, call the following
    private void recordModule (Module mod, Calendar startMonth)
    {
        
        // TODO this is a horrible hack!! have to find a better way...
        StudyMonth aMonth;
        Iterator<StudyMonth> it = myMonths.iterator();
        boolean finished = false;

        while ( it.hasNext() && finished == false )
        {
            aMonth = it.next();
          
            if (aMonth.equals( startMonth ) )
            {
                // we've reached the month when this module should be started
                // so add the Module details
                aMonth.modulesInProgress.add(mod);
                aMonth.incrementTotalMonthlyWorkload( mod.getMonthlyWorkload() );

                // ... and for all the subsequent months til the module ends
                for (int i = 0; i < mod.getDuration()-1; i++)
                {
                    aMonth = it.next();
                    aMonth.modulesInProgress.add(mod);
                    aMonth.incrementTotalMonthlyWorkload( mod.getMonthlyWorkload() );
                }
                finished = true; // so we don't go on pointlessly iterating
                // after the one and only occurrence of the module has been handled
            }         
        }       
    }

    public int getHoursScheduledForMonth(StudyMonth month)
    {
        return month.getTotalMonthlyWorkload();
    }

    


    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("I am a Schedule. I have records for the following months: ");

        for (StudyMonth month : myMonths)
        {

            // displays the months
            s.append("\n");
            s.append(month);

            // if there are Modules in progress, displays which ones
            // and the total workload for the month
           

            SortedSet<Module> studyingThisMonth = month.getModulesInProgress();
            for (Module mod : studyingThisMonth)
            {
                s.append(" - Studying ");
                s.append(mod.getCode() );
            }

            int work = month.getTotalMonthlyWorkload();
            if ( work > 0 )
            {
                s.append(" (");
                s.append( work );
                s.append(" hours)");
            }
        }
       return s.toString();
    }


    public SortedMap<Module, Calendar> getMap()
    {
        return mapToProcess;
    }

    public ModuleBank getBank()
    {
        return myBank;
    }

    private StudyMonth setLastActiveMonth()
    {
       StudyMonth lastMonthSoFar = firstMonthInSchedule;

       for (StudyMonth aMonth : myMonths)
       {
           if (aMonth.getTotalMonthlyWorkload() > 0) // it's an active month
           {
               //System.out.println("Workload for " + aMonth + " = " + aMonth.getTotalMonthlyWorkload() );
               lastMonthSoFar = aMonth;
           }
       }

       return lastMonthSoFar;
    }

    private int setTotalDuration()
    {
        long durationInMs = lastActiveMonth.getTimeInMillis() -
                        firstMonthInSchedule.getTimeInMillis();

        long durationInDays = durationInMs / (1000*60*60*24);

        int durationInMonths = (int) durationInDays * 12 / 365;

        //System.out.println("Setting totalDuration to " + durationInMonths);

        return durationInMonths;
    }

    private StudyMonth setFirstMonthInSchedule()
    {
        StudyMonth zeroMonth = new StudyMonth(STARTING_YEAR, 0);
        //System.out.println("Setting firstMonthInSchedule to "+ zeroMonth);
        return zeroMonth;

    }

    private void buildCalendar()
    {
        for (int i = 0; i < TOTAL_MONTHS_IN_SCHEDULE; i++)
        {
            // increments the month 108 times.
            // Because the calendar is lenient, it will interpret this across years
            myMonths.add(new StudyMonth (STARTING_YEAR, i) );
        }
    }

    //looks for year long gaps and shunts every subsewuent module forwards
    void eliminateScheduleGaps()
    {       
        //System.out.println(this);

        int gap = 0;
        boolean stop = false;
        StudyMonth month;
        Iterator<StudyMonth> it = myMonths.iterator();

        while ( it.hasNext() && stop == false )
        {
            month = it.next();
            
            if (getHoursScheduledForMonth(month) == 0) // we're still in a gap
            {
                gap++;
            }
            else
            {
                gap = 0;
            }
            if (gap == 12) // we've found a year-long gap!
            {
                //System.out.println("Found a year-long gap ending at " + month.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK) + " " + month.get(Calendar.YEAR));

                //System.out.println("Getting mapToProcess. Its size is " + mapToProcess.size());
                for (Map.Entry entry : mapToProcess.entrySet())
                {
                    Module module = (Module) entry.getKey();
                    Calendar cal = (Calendar) entry.getValue();


                    // first check module's start year comes after end of gap
                    if (cal.get(Calendar.YEAR) > month.get(Calendar.YEAR)
                    || (cal.get(Calendar.YEAR) == month.get(Calendar.YEAR)
                       && cal.get(Calendar.MONTH) >= month.get(Calendar.MONTH)))
                    {                   
                        //System.out.println("Module " + module.getCode() + " starts after this gap, in " + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK) + cal.get(Calendar.YEAR));
                        //reset the year to one year earlier
                        //System.out.println("Resetting year to " + (cal.get(Calendar.YEAR)-1));

                        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);

                        //put the new value back in the map
                        mapToProcess.put(module, cal);
                    }
                } // end of for loop - finished shunting modules forward
            stop = true; // that's enough repair for one generation
            
            }
        } // end of while loop
    }   
}
