package m450;

import java.awt.*;
import javax.swing.*;

/**
 * Provides an on-screen visual representation of a Population object
 * @author Liz
 */
public class PopulationVisualiser extends JFrame
{
    private JPanel genomePanel;
    private JPanel fitnessPanel;



    public PopulationVisualiser(String title, int width, int height, Population aPop, int numberToShow)
    {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // use 0 as a flag meaning show ALL the genomes in the population
        if (numberToShow == 0)
        {
            numberToShow = aPop.myGenomes.size();
        }

        this.setLayout(new BorderLayout() );

        this.genomePanel = new JPanel();
        genomePanel.setSize( (int) (width * 0.8), height);
      
        genomePanel.setLayout(new GridLayout( numberToShow, 1 ) );


        this.fitnessPanel = new JPanel();
        fitnessPanel.setSize( (int) (width * 0.2), height);
        fitnessPanel.setLayout(new GridLayout(numberToShow *2, 1 ) );
      

        // populate the panels
        for (int i = 0; i < numberToShow; i++)
        {
            Genome genome = aPop.myGenomes.get(i);
            genomePanel.add(new ScheduleVisualiser(genome.getMySchedule() ) );
            JLabel label1 = new JLabel("Fitness = " + genome.fitness);
            JLabel label2 = new JLabel("Ends " + genome.getMySchedule().lastActiveMonth);
            label1.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            label2.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            fitnessPanel.add(label1);
            fitnessPanel.add(label2);
        }

        add(genomePanel, BorderLayout.CENTER);
        add(fitnessPanel, BorderLayout.EAST);

        setVisible(true);
    }
 

}
