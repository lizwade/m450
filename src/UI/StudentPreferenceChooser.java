/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import m450.Module;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import m450.Mode;

/**
 * A simple interface that lets the student specify
 * her preferences for modules, workload and mode(s) for schedule
 *
 * @author Liz
 */
public class StudentPreferenceChooser extends JFrame
{
    //private m450.ModuleBank modbank;
    private Module[] allModules;

    // variables to hold student choices
    private List<m450.Mode> modesDesired;
    private Set<String> modulesDesired;
    private int workloadDesired;
    private boolean selectionsMade = false;

    // attributes for JFrame
    private JPanel panel;
    private JPanel modulePanel;

    private JLabel modulesLabel;
    private JCheckBox[] availableModules;

    private JLabel bandwidthLabel;
    private JTextField bandwidthField;

    private JLabel modesLabel;
    private JCheckBox conservative;
    private JCheckBox moderate;
    private JCheckBox aggressive;
    

    private JButton okayButton;


    // constructor
    public StudentPreferenceChooser(m450.ModuleBank modbank)
    {
        super("Choose your study preferences");
        setSize(600, 350);
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        modulePanel = new JPanel();
        modulePanel.setLayout(new GridLayout(5,5));

        // choose modules in first row
        modulesLabel = new JLabel("Tick all modules you want in the schedule");
        panel.add(modulesLabel);

        // get data for all modules in modbank
        allModules = modbank.getAllModulesAsArray();
        availableModules = new JCheckBox[allModules.length];

        // create a check box for each module, named with the module's code
        for ( int i = 0; i<allModules.length; i++ )
        {
            availableModules[i] = new JCheckBox(allModules[i].getCode());
            modulePanel.add(availableModules[i]);
        }
        panel.add(modulePanel);

        // choose desired workload
        bandwidthLabel = new JLabel("Up to how many hours a month do you generally wish to study?");
        panel.add(bandwidthLabel);
        bandwidthField = new JTextField("70");
        panel.add(bandwidthField);

        // choose desired modes
        modesLabel = new JLabel("Which of the following types of schedule do you want to see?");
        panel.add(modesLabel);
        conservative = new JCheckBox("Conservative - Hardly ever exceeds your stated monthly hours", true);
        moderate = new JCheckBox("Moderate - Sometimes exceeds your stated monthly hours to complete study faster", true);
        aggressive = new JCheckBox("Aggressive - Often exceeds your stated monthly hours to complete study much faster", true);
      
        panel.add(conservative);
        panel.add(moderate);
        panel.add(aggressive);
      
        conservative.setSelected(true);
        moderate.setSelected(true);
        aggressive.setSelected(true);

        
        // okay button
        okayButton = new JButton("GO");
        panel.add(okayButton);

        okayButton.addActionListener(new ButtonListener() );

        add(panel);
        setVisible(true);

        // wait for input
        while (!selectionsMade)
        {
            try
            {
                // wait until okay button is pressed
                Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
                // this was auto-generated by netbeans
                Logger.getLogger(AlgorithmChooser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    } // end constructor

    /**
     * @return the modesDesired
     */
    public List<m450.Mode> getModesDesired()
    {
        return modesDesired;
    }

    /**
     * @return the modulesDesired
     */
    public Set<String> getModulesDesired()
    {
        return modulesDesired;
    }

     /**
     * @return the modulesDesired
     */
    public String[] getModulesDesiredAsStringArray()
    {
        return (String[])modulesDesired.toArray(new String[modulesDesired.size()]);

    }

    /**
     * @return the workloadDesired
     */
    public int getWorkloadDesired()
    {
        return workloadDesired;
    }


    // inner class
    private class ButtonListener implements ActionListener
    {


        public void actionPerformed(ActionEvent e)
        {
            // record user's preference for Modules
            modulesDesired = new HashSet<String>();
            for (int i = 0; i < availableModules.length; i++)
            {
                if (availableModules[i].isSelected())
                {
                    getModulesDesired().add(allModules[i].getCode() );
                }
            }

            // record user's preference for workload
            workloadDesired = Integer.parseInt(bandwidthField.getText() );

            // record user's preference for modes
            modesDesired = new ArrayList<m450.Mode>();

            if (conservative.isSelected())
            {
                getModesDesired().add(Mode.CONSERVATIVE);
            }

            if (moderate.isSelected())
            {
                getModesDesired().add(Mode.MODERATE);
            }

            if (aggressive.isSelected())
            {
                getModesDesired().add(Mode.AGGRESSIVE);
            }


            if ( getModesDesired().isEmpty() )
            {
                System.out.println("No modes selected!"); // should be handled within GUI
            }

            setVisible(false); // we don't need to see it any more
            selectionsMade = true; // jump out of the waiting loop

        }
    }


}
