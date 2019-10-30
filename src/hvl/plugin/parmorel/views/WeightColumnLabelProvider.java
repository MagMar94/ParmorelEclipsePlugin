package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.modelrepair.AppliedAction;
import hvl.projectparmorel.modelrepair.Solution;

public class WeightColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element != null && element instanceof Solution) {
			Solution solution = (Solution) element;
			return Double.toString(solution.getWeight());
		}
		if (element != null && element instanceof AppliedAction) {
			AppliedAction action = (AppliedAction) element;
			return Double.toString(action.getAction().getWeight());
		}
		return "";
	}
}
