package hvl.plugin.parmorel.model;

import java.util.ArrayList;
import java.util.List;

import hvl.plugin.parmorel.views.RepairSelectorView;
import hvl.projectparmorel.modelrepair.Solution;

public enum PossibleSolutions {
	INSTANCE;

	private List<Solution> solutions;
	private RepairSelectorView view;
	
	private PossibleSolutions() {
		solutions = new ArrayList<>();
		view = null;
	}
	
	public List<Solution> getSolutions(){
		return solutions;
	}
	
	public void updateSolutions(List<Solution> updatedSolutions) {
		solutions.clear();
		for(Solution solution : updatedSolutions) {
			solutions.add(solution);
		}
		if(view != null) {
			view.updateSolutions(updatedSolutions);
		}
	}
	
	public void setViewer(RepairSelectorView view) {
		this.view = view;
	}
}
