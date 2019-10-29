package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.modelrepair.Solution;

public class NumberOfActionsAppliedColumnLabelProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		if(element != null && element instanceof Solution) {
			Solution solution = (Solution) element;
			return Integer.toString(solution.getSequence().size());
		}
		return "";
	}
}
