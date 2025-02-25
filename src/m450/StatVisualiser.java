/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import static m450.Mode.*;
import java.util.Map;
import java.awt.*;
import java.util.Iterator;
import javax.swing.*;


/**
 * Provides a visual on-screen representation of a the results of 
 * a series of runs
 *
 * @author Liz
 */
public class StatVisualiser extends JFrame
{
    private JPanel genomePanel;
    private JPanel fitnessPanel;


    //Could I create a parent class Visualiser and have this and PopVis be children?
    public StatVisualiser(String title, int width, int height, Map<Genome, Long> results, int numberToShow)
    {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // use 0 as a flag meaning show ALL the genomes in the population
        if (numberToShow == 0 || numberToShow > results.size() )
        {
            numberToShow = results.size();
        }

        this.setLayout(new BorderLayout() );

        this.genomePanel = new JPanel();
        genomePanel.setSize( (int) (width * 0.8), height);

        genomePanel.setLayout(new GridLayout( numberToShow, 1 ) );


        this.fitnessPanel = new JPanel();
        fitnessPanel.setSize( (int) (width * 0.2), height);
        fitnessPanel.setLayout(new GridLayout( numberToShow*3, 1 ) );


        // populate the panels
        // very inelegant - how can I access the map entries by index number?
        int i = 0;
        for (Genome genome : results.keySet() )
        {
            if (i < numberToShow)
            {
            genomePanel.add(new ScheduleVisualiser(genome.getMySchedule() ) );
            JLabel label1 = new JLabel("Fitness = " + genome.fitness);
            JLabel label2 = new JLabel("Ends " + genome.getMySchedule().lastActiveMonth);
            JLabel label3 = new JLabel("Gen found: " + results.get(genome) );
            label1.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            label2.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            label3.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            fitnessPanel.add(label1);
            fitnessPanel.add(label2);
            fitnessPanel.add(label3);
            }
            i++;
        }

        add(genomePanel, BorderLayout.CENTER);
        add(fitnessPanel, BorderLayout.EAST);

        setVisible(true);
    }
}

