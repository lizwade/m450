/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

/**
 * Drives a Genetic Algorithm to generate a solution
 *
 * @author Liz
 */
public class GeneticAlgorithmRunner extends m450.AlgorithmRunner
{

    GeneticAlgorithmRunner(ParamSetter params)
    {
        super(params);
    }

    GeneticAlgorithmRunner(ParamSetter params, ModuleBank modBank, Student student)
    {
        super(params, modBank, student);
    }

    /**
    * Creates a new population with the specified bits per module, number
    * of individuals, and modules and preferences as per the student object.
    */
    @Override
    void createPopulation()
    {
        poppy = new Population(params.BITS_PER_MODULE,
                              params.NUM_OF_INDIVIDUALS,
                              myStudent );

    }

    /**
    *
    *  evolves population one generation and returns fittest member so far
    */
    @Override
    Genome doAGeneration()
    {
            // judge and sort this generation
            popRecords.add( poppy.judgePopulationFitness() );
            //poppy.sortMyGenomes(); can we do without this now?

            // do visualisation if appropriate
            if (params.VIS_TYPE != m450.VisType.NONE)
            {
                doVisualisation();
            }

            // create a Selector and get the next generation
            Selector sel;


        if (params.SEL_TYPE == m450.SelectionType.ROULETTE)
        {
            sel = new RouletteSelector();
        }
        else
        {
            sel = new TournamentSelector();
        }
            
            poppy.produceNextGeneration(sel);
            poppy.doRepairs();
            poppy.myGeneration++;
            return poppy.getFittestGenome();
    }

}
