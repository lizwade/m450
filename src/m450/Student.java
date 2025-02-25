package m450;

import static m450.Mode.*;
import java.util.*;

/**
 * Models the preferences of a student,
 * such as which modules they wish to study
 * and the max monthly hours they want to spend studying
 * It is used by the system to create and evaluate Schedules
 * @author Liz
 */

//TODO Have a Student object returned by the User Interface
// or orchestrating object instead of main

public class Student
{
    ModuleBank modBank;
    List<Module> modulesToTake;
    int maxMonthlyHours;
    final int STARTING_YEAR;
    Mode preferredMode;

    Student(ModuleBank modBank, String[] modCodes, int maxMonthlyHours, Mode mode, int startingYear)
    {

        this.modBank = modBank;
        this.maxMonthlyHours = maxMonthlyHours;
        this.preferredMode = mode;
        this.STARTING_YEAR = startingYear;

        modulesToTake = new ArrayList<Module>();

        for (String aCode : modCodes )
        {
            modulesToTake.add(modBank.getModuleByCode(aCode) );
        }
        // Puts the modules in level order (because that's what Comparable
        // has been set up to do in the Module class.
        Collections.sort(modulesToTake);
    }

    Mode getPreferredMode()
    {
        return this.preferredMode;
    }

    @Override
    public String toString()
    {
        return ("I'm a Student. My mode is " + preferredMode
                + " and I want to start studying in " + STARTING_YEAR
                + " and study for " + maxMonthlyHours + " hours per month.");
    }

    ModuleBank getModBank()
    {
        return modBank;
    }

}
