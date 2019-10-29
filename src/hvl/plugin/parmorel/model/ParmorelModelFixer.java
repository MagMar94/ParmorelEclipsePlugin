package hvl.plugin.parmorel.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hvl.projectparmorel.modelrepair.ModelFixer;
import hvl.projectparmorel.modelrepair.QModelFixer;
import hvl.projectparmorel.modelrepair.Solution;

public class ParmorelModelFixer {
	private List<Solution> possibleSolutions;
	
	private ModelFixer modelFixer;
	
	public ParmorelModelFixer() {
		modelFixer = new QModelFixer();
		possibleSolutions = new ArrayList<Solution>();
		
	}
	
	/**
	 * Fixes the selected model with the specified preferences.
	 * 
	 * @param preferences
	 */
	public List<Solution> fixModel(File model, List<Integer> preferences) {
		deletePossibleSolutions();
		
		modelFixer.setPreferences(preferences);
		modelFixer.fixModel(model);
		
		possibleSolutions = modelFixer.getPossibleSolutions();
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
