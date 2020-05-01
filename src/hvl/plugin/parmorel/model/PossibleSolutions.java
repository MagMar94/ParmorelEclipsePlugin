package hvl.plugin.parmorel.model;

import java.util.ArrayList;
import java.util.List;

import hvl.plugin.parmorel.views.RepairSelectorView;
import hvl.projectparmorel.qlearning.QSolution;

public enum PossibleSolutions {
	INSTANCE;

	private List<QSolution> solutions;
	private RepairSelectorView view;
	
	private PossibleSolutions() {
		solutions = new ArrayList<>();
		view = null;
	}
	
	public List<QSolution> getSolutions(){
		return solutions;
	}
	
	public void updateSolutions(List<QSolution> updatedSolutions) {
		solutions.clear();
		for(QSolution solution : updatedSolutions) {
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
