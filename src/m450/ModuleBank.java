package m450;

import java.io.*;
import java.util.*;

/**
 * Reads in relevant data for modules offered by the OU, and uses it
 * to create and hold Module objects
 * @author Liz
 */
public class ModuleBank
{
    private List<Module> allModules;

    // one-argument constructor, reads data from file
    public ModuleBank(String aFileName)
    {
        allModules = new ArrayList<Module>();
        readDataFromFile( openFileForRead( aFileName ) );
    }


    public void addModule(Module mod)
    {
        getAllModules().add(mod);
    }

    private void readDataFromFile(FileReader fr)
    {
        Scanner sc = new Scanner(fr);

        while ( sc.hasNext() )
        {
            String aLine = sc.nextLine();

            //System.out.println("Got line " + aLine); //DEBUG

            addModule( readModulefromString(aLine) );
        }

    }

    public Module readModulefromString(String aString)
    {
        Scanner chunker = new Scanner(aString);

        String code = chunker.next();
        //System.out.println("Got " + code); // DEBUG

        int level = chunker.nextInt();
        //System.out.println("Got " + level); // DEBUG

        int points = chunker.nextInt();
        //System.out.println("Got " + points); //DEBUG

        int duration = chunker.nextInt();
        //System.out.println("Got " + duration); // DEBUG

        // switch gears to process the unknown number of CSV start dates
        String startDateSoup = chunker.next();
        //System.out.println("Got " + startDateSoup); //DEBUG

        Set<Integer> startDates = new HashSet<Integer>();
        
        Scanner soupChunker = new Scanner(startDateSoup);
        soupChunker.useDelimiter(",");
        while ( soupChunker.hasNext() )
        {
            startDates.add( soupChunker.nextInt() );
        } // finished reading in the start dates

        // that's all the data we're expecting!

        soupChunker.close();
        chunker.close();

        return new Module(code, level, points, duration, startDates);
    }


    private FileReader openFileForRead(String aFileName)
    {
        try
        {
            FileReader fr = new FileReader(aFileName);
            return fr;
        }
        catch (Exception e)
        {
            System.out.println("Whoops! " + e);
            return null;
        }


    }


    /*
     * will return null if module is not found, so calling code
     * should check that the returned argument is not null before using
     */
    public Module getModuleByCode(String code)
    {
        for (Module mod : getAllModules())
        {
            if ( mod.getCode().equals(code) )
            {
                return mod;
            }
        }

        // if we get this far, the module wasn't found
        System.out.println("No module with code " + code
                            + " is in the Module Bank");
        return null;
    }

    @Override
    public String toString()
    {
       StringBuilder s = new StringBuilder();

       s.append("I am a ModuleBank. I store a lot of Modules. ");
       s.append("Here's what they have to say:\n");

       for (Module m : getAllModules())
       {
            s.append(m);
            s.append("\n");
       }
       return s.toString();
    }

    /**
     * @return the allModules
     */
    public Module[] getAllModulesAsArray()
    {
        return (Module[])allModules.toArray(new Module[allModules.size()]);
        // the above line of code from www.exampledepot.com

    }

    public List<Module> getAllModules()
    {
        return allModules;
    }

}
