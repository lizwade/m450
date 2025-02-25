/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Displays the recommended schedules for the student user
 * @author Liz
 */
public class Output extends JFrame
{
    private JPanel genomePanel;
    private JPanel descriptionPanel;


    Output(int width, int height, Map<Genome, Mode> genomes)
    {
        super("Recommended schedules");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout() );

        this.genomePanel = new JPanel();
        this.descriptionPanel = new JPanel();

        genomePanel.setSize( (int) (width * 0.8), height);
        descriptionPanel.setSize( (int) (width * 0.2), height);

        genomePanel.setLayout(new GridLayout( genomes.size(), 1 ) );
        descriptionPanel.setLayout(new GridLayout( genomes.size(), 1 ) );

  
        // populate the genome panel
        for (Map.Entry<Genome, Mode> entry : genomes.entrySet())
        {
            Genome genome = entry.getKey();
            Mode mode = entry.getValue();

            genomePanel.add(new ScheduleVisualiser(genome.getMySchedule() ) );

            // add string representation to description panel
            StringBuilder description = new StringBuilder();
            description.append("Mode: ");
            description.append(mode);
            description.append("\n");

            SortedMap<Module, Calendar> mapToProcess = genome.getMySchedule().getMap();
        
            for (Map.Entry<Module, Calendar> entry2 : mapToProcess.entrySet())
            {
                Module module = entry2.getKey();
                Calendar cal = entry2.getValue();

                description.append("Start ");
                description.append(module.getCode());
                description.append(" in ");
                description.append(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK));
                description.append(" ");
                description.append(cal.get(Calendar.YEAR));
                description.append("\n");
              
                
            }
            description.append("Study complete by ");
            description.append(genome.getMySchedule().lastActiveMonth);
            JTextArea textArea = new JTextArea();
            textArea.setSize(width, 3); // doesn't work
            textArea.setText(description.toString());
            descriptionPanel.add(textArea);

        }

        add(descriptionPanel, BorderLayout.EAST);
        add(genomePanel, BorderLayout.CENTER);

     

        setVisible(true);
    }




}
