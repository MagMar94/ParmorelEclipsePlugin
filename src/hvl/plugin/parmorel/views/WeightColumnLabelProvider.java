package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.qlearning.AppliedAction;
import hvl.projectparmorel.qlearning.QSolution;

public class WeightColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element != null && element instanceof QSolution) {
			QSolution solution = (QSolution) element;
			return Double.toString(solution.getWeight());
		}
		if (element != null && element instanceof AppliedAction) {
			AppliedAction action = (AppliedAction) element;
			return Double.toString(action.getAction().getWeight());
		}
		return "";
	}
}
