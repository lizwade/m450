/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;
/**
 * Starts the program in either Scientist Mode or Student Mode.
 * Uncomment the relevant code to choose between modes
 * @author Liz
 */
public class Main
{
    static boolean SCIENTIST_MODE;

    public static void main(String[] args)
    {
      
        // Uncomment the following lines of code for Computer Scientist mode
        //SCIENTIST_MODE = true;
        //StatCollector myStats = new StatCollector();


        // Uncomment the following lines of code for Student (User) mode
        SCIENTIST_MODE = false;
        Initializer initializer = new Initializer();
      
    }
    
}
