package hvl.plugin.parmorel.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import no.hvl.projectparmorel.Solution;
import no.hvl.projectparmorel.exceptions.NoErrorsInModelException;
import no.hvl.projectparmorel.qlearning.QModelFixer;
import no.hvl.projectparmorel.qlearning.QSolution;
import no.hvl.projectparmorel.qlearning.ecore.EcoreQModelFixer;
import no.hvl.projectparmorel.qlearning.reward.PreferenceOption;

public class ParmorelModelFixer {
	private List<QSolution> possibleSolutions;
	
	private QModelFixer modelFixer;
	
	public ParmorelModelFixer() {
		modelFixer = new EcoreQModelFixer();
		possibleSolutions = new ArrayList<QSolution>();
		
	}
	
	/**
	 * Fixes the selected model with the specified preferences.
	 * 
	 * @param preferences
	 * @throws NoErrorsInModelException 
	 */
	public List<QSolution> fixModel(File model, List<PreferenceOption> preferences) throws NoErrorsInModelException {
		deletePossibleSolutions();
		
		modelFixer.setPreferences(preferences);
		modelFixer.fixModel(model);
		
		List<Solution> solutions = modelFixer.getPossibleSolutions();
		
		for(Solution s : solutions) {
			if(s instanceof QSolution) {
				QSolution solution = (QSolution) s;
				possibleSolutions.add(solution);
			}
		}
		 
		PossibleSolutions.INSTANCE.updateSolutions(possibleSolutions);
		return possibleSolutions;
	}

	

	/**
	 * Deletes all the files in the possible solutions and clears the list.
	 */
	private void deletePossibleSolutions() {
		for(Solution solution : possibleSolutions) {
			solution.getModel().delete();
		}
		possibleSolutions.clear();
	}
}
