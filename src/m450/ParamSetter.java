/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package m450;

import static m450.Mode.*;
/**
 * Stores the parameters to be used for the entire set of runs
 * @author Liz
 */
class ParamSetter
{

    final int BITS_PER_MODULE = 5; // won't need if I switch away from binary rep

    // basic paramaters
    final m450.AlgorithmType ALG_TYPE;
    final int NUM_OF_GENERATIONS;
    final int NUM_OF_INDIVIDUALS;
    final m450.VisType VIS_TYPE;
    final int NUM_OF_POP_MEMBERS_TO_VISUALIZE;
    final m450.SelectionType SEL_TYPE;


    // constructor that sets to default values
    public ParamSetter()
    {
        // set basic parameters
        ALG_TYPE = m450.AlgorithmType.GENETIC;
        SEL_TYPE = m450.SelectionType.ROULETTE;
        NUM_OF_GENERATIONS = 50;
        NUM_OF_INDIVIDUALS = 100;
        NUM_OF_POP_MEMBERS_TO_VISUALIZE = 0;
        VIS_TYPE = m450.VisType.NONE;
    }

    // constructor that sets parameters to received values
    ParamSetter( m450.AlgorithmType algType, m450.SelectionType selType,
                 int gens, int indivs, m450.VisType visType  )
    {
        this.ALG_TYPE = algType;
        this.SEL_TYPE = selType;
        this.NUM_OF_GENERATIONS = gens;
        this.NUM_OF_INDIVIDUALS = indivs;

        if (ALG_TYPE == m450.AlgorithmType.HILL_CLIMBING)
        {
            this.NUM_OF_POP_MEMBERS_TO_VISUALIZE = 1;
        }
        else
        {
            this.NUM_OF_POP_MEMBERS_TO_VISUALIZE = 1; //change later to allow more
        }

        this.VIS_TYPE = visType;
    }

}
