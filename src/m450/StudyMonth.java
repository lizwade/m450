package m450;

import java.util.*;

/**
 * Models a calendar month, in which a module may start or be in progress
 * @author Liz
 */
public class StudyMonth extends GregorianCalendar
{

    SortedSet<Module> modulesInProgress;
    int totalMonthlyWorkload;

    public StudyMonth(int year, int month)
    {
        super(year, month, 1); // sets year and month to params and dayOfMonth to 1
        modulesInProgress = new TreeSet<Module>();
        totalMonthlyWorkload = 0;
    }

    public void incrementTotalMonthlyWorkload(int workload)
    {
        totalMonthlyWorkload = totalMonthlyWorkload + workload;
    }
    
    public int getTotalMonthlyWorkload()
    {
        return totalMonthlyWorkload;
    }

    boolean isEmpty()
    {
        return totalMonthlyWorkload == 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Calendar)
        {
            Calendar toCompare = (Calendar) o;
            return ( this.get(YEAR) == toCompare.get(YEAR)
                    && this.get(MONTH) == toCompare.get(MONTH));
        }
        return false;
    }

    public SortedSet<Module> getModulesInProgress()
    {
        return modulesInProgress;
    }

    @Override
    public String toString()
    {
            return getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK)
                    + " " + get(Calendar.YEAR);
    }





}
