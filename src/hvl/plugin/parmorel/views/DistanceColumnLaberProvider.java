package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.modelrepair.Solution;

public class DistanceColumnLaberProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		if (element != null && element instanceof Solution) {
			Solution solution = (Solution) element;
			return Double.toString(solution.calculateDistanceFromOriginal());
		}
//		if (element != null && element instanceof AppliedAction) {
//			AppliedAction action = (AppliedAction) element;
//			return Double.toString(action.getAction().getWeight());
//		}
		return "";
	}
}
