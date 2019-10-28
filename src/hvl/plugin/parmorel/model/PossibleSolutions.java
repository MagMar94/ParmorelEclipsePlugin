package hvl.plugin.parmorel.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import hvl.projectparmorel.modelrepair.Solution;

public enum PossibleSolutions {
	INSTANCE;

	private List<Solution> solutions;
	private TableViewer viewer;
	
	private PossibleSolutions() {
		solutions = new ArrayList<>();
		solutions.add(new Solution());
		viewer = null;
	}
	
	public List<Solution> getSolutions(){
		return solutions;
	}
	
	public void updateSolutions(List<Solution> updatedSolutions) {
		solutions.clear();
		for(Solution solution : updatedSolutions) {
			solutions.add(solution);
		}
		if(viewer != null) {
			viewer.refresh();
		}
	}
	
	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}
}
