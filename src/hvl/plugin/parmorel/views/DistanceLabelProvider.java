package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.modelrepair.Solution;

public class DistanceLabelProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		if(element != null && element instanceof Solution) {
			Solution solution = (Solution) element;
			return Double.toString(solution.calculateDistanceFromOriginal());
		}
		return "";
	}
}
