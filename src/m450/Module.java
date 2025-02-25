package m450;

import java.util.*;

/**
 * Models an OU Module
 * @author Liz
 */
public class Module implements Comparable
{
    private String code; // the code for this module, eg "M255"
    private String title; // eg "Object Oriented Programming with Java"
    private String description; // need this?? could get it from website
    private int level; // is this a level 1, 2, or 3 module?
    private int points; // how many points this module is worth, eg 30
    private int duration; // how many months this module runs for, eg 9
    private int monthlyWorkload; // how many study per month this module takes
    private Set<Integer> startableMonths; // which months in the year you can start this module
                               // with months represented by a number 0-11
    //TODO use this field?
    boolean hasExam; // true if you must take an exam for this course, false otherwise

    //constructor
    public Module(String code, int level, int points, int duration, Set<Integer> startableMonths)
    {
        this.code = code;
        this.level = level;
        this.points = points;
        this.duration = duration;
        this.startableMonths = startableMonths;

        this.monthlyWorkload = (points * 10) / duration; // assuming 1 point = 10 hours of study, as stated on http://www3.open.ac.uk/study/explained/credit-points.shtml



       // TODO get title and description from web (and the rest??)
        // or read in from database
    }

    public int getDuration()
    {
        return duration;
    }

    public String getCode()
    {
        return code;
    }

    public int getMonthlyWorkload()
    {
        return monthlyWorkload;
    }


    public boolean canBeStarted(int month)
    {
        return startableMonths.contains(month);
    }

    @Override
    public String toString()
    {
        String me;
        me =    "I am a level " + level + " OU module with code " + code +
                ". I am worth " + points + " points and take " + duration +
                " months to complete. That's about " + monthlyWorkload +
                " study hours per month. You can start me in month ";

        StringBuilder monthPhrase = new StringBuilder();

        Iterator<Integer> iterator = startableMonths.iterator();


        monthPhrase.append(iterator.next() ); // always at least 1 value here

        while ( iterator.hasNext() )
        {
            monthPhrase.append(" or ");
            monthPhrase.append( iterator.next() );
        }
        monthPhrase.append(".");

        return me + monthPhrase;

    }

    @Override
    public boolean equals(Object o)
    {
        if ( o instanceof Module )
        {

            Module toCompare = (Module) o;
            return  this.getCode().equals( toCompare.getCode() ) ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.code != null ? this.code.hashCode() : 0);
        hash = 47 * hash + this.level;
        hash = 47 * hash + this.points;
        hash = 47 * hash + this.duration;
        return hash;
    }

    public int compareTo(Object o)
    {
        if ( ! (o instanceof Module))
        {
            throw new IllegalArgumentException();
        }

        Module toCompare = (Module) o;

        int levelDifference = this.getLevel() - toCompare.getLevel();

        if (levelDifference == 0) // the Modules are the same level
        {
            // return a small number based on letter/number difference
            return this.getCode().compareTo(toCompare.getCode());
        }
        //return a big number that ensures level difference is prioritized
        else return levelDifference * 100000;
        

    }

    int getLevel()
    {
        return level;
    }


}
