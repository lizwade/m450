package m450;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Provides a visual on-screen representation of a Schedule object
 * @author Liz
 */
public class ScheduleVisualiser extends JPanel
{
    private final int SCALE_FACTOR_HOR = 8;
    private final double SCALE_FACTOR_VERT = 1; // less than 1 causes problems with overlapping modules
    private SortedMap<Module, Calendar> myData;
    private Schedule scheduleToDraw;
    private Set<ModRect> rectanglesToDraw;



    // constructor
    public ScheduleVisualiser(Schedule schedule)
    {
        


        scheduleToDraw = schedule;
        myData = scheduleToDraw.getMap();
        rectanglesToDraw = new HashSet<ModRect>();
        addModRects();
        
       setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }


    /*
     * Produces a set of non-overlapping rectangles
     * representing all the modules in this schedule,
     * ready to be drawn
     */
    private void addModRects()
    {
        for (Module mod : myData.keySet() )
        {
            rectanglesToDraw.add( addRectangle(mod) );
        }
    }


    /*
     * Takes the code for a module, gets the modules details,
     * and calculates the arguments needed to create a rectangle
     * to represent this module. Adjusts the rectangle so that it
     * does not overlap with others in the collection,
     * and then returns the rectangle.
     */
    private ModRect addRectangle(Module modToDraw)
    {
      

        // create a rectangle to represent the module, with the following arguments:

        // x: shift right by the number of months since 'year zero'
        Calendar startDate = myData.get(modToDraw);
        int fromYears = (startDate.get(Calendar.YEAR) - scheduleToDraw.STARTING_YEAR ) *12;
        int fromMonths = startDate.get(Calendar.MONTH);
        int x = (fromYears + fromMonths) * SCALE_FACTOR_HOR;
       

        // y: Initially 0, but we'll increase it later if necessary
        int y = 0;
       
        // width: corresponds to module's duration
        int width = modToDraw.getDuration() * SCALE_FACTOR_HOR;

        // height: corresponds to module's workload
        int height = (int) (modToDraw.getMonthlyWorkload() * SCALE_FACTOR_VERT);

        // set the color according to the level of the module
        Color color = getColorForLevel(modToDraw);
       
        
        // Create the rectangle
        ModRect modRect = new ModRect(x, y, width, height, color, modToDraw.getCode());
        
        // Check if it overlaps with any other rectangles,
        // and if so, increase y until it does not
        
        // First, copy the instance's set of ModRect's to a new variable
        Set<ModRect> rectanglesToCheck = new HashSet<ModRect>(rectanglesToDraw);

        // Now send that set of ModRect objects as an argument to checking method
        getOutOfTheWay( modRect, rectanglesToCheck );

        
        //System.out.println( modRect.myCode + " now ready to be drawn."); //DEBUG
        return modRect;

    }

    private void getOutOfTheWay(ModRect modRect, Set<ModRect> rectanglesToCheck)
    {
        Iterator<ModRect> it = rectanglesToCheck.iterator();
        ModRect existingRect;
        while ( it.hasNext() )

        {
            existingRect = it.next();
            //System.out.println("I am " + modRect.myCode); // DEBUG
            //System.out.println("Checking for clash with " + existingRect.myCode); // DEBUG

            if (modRect.intersects(existingRect) )
            {
                // move it out the way
                while (modRect.intersects(existingRect) )
                {
                    modRect.translate(0,1);
                  //  System.out.println("Translating " + modRect.myCode); // DEBUG
                }

                // remove the ModRect we just checked from the set
                // and check we didn't move ourselves into any of the others
                it.remove();
                Set<ModRect> smallerSet = new HashSet<ModRect>(rectanglesToCheck);

                getOutOfTheWay( modRect, smallerSet); // my first recursion!
            }

            //System.out.println("Finished getting out of " + existingRect.myCode + "'s way!" ); // DEBUG
       
        }
    }



    @Override
    /* Draws the schedule across an x axis allowing for 8 years of study
     * and a y axis allowing for 360 points of concurrent study
     * 
     */

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
   
        for (ModRect modRect : rectanglesToDraw)
        {
            g.setColor(modRect.getColor() );

            int x = (int) modRect.getX();
            int y = (int) modRect.getY();
            int width = (int) modRect.getWidth();
            int height = (int) modRect.getHeight();
        
            g.fillRect(x, y, width, height );

            g.setColor(Color.BLUE);
            g.drawString( modRect.myCode, x, y+10 );
        }
    }





    private Color getColorForLevel(Module mod)
    {
        int level = mod.getLevel();
        if (level == 1)
        {
            return (new Color(255,80,0) );
        }
        if (level == 2)
        {
            return (new Color(255,165,0 ) );
        }
        if (level == 3)
        {
            return (new Color(255,255,0 ) );
        }
        return Color.BLACK;

    }



    // inner class
    private class ModRect extends Rectangle
    {
        Color myColor;
        String myCode;


        // 4 argument constructor, includes color
        public ModRect(int x, int y, int width, int height, Color color)
        {
            super(x, y, width, height);
            myColor = color;

        }

        // 5 argument contructor, includes color and code to be displayed
        public ModRect(int x, int y, int width, int height, Color color, String code)
        {
            super(x, y, width, height);
            myColor = color;
            myCode = code;

        }

        private Color getColor()
        {
            return myColor;
        }
    }

}
