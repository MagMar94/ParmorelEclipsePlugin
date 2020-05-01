package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.qlearning.QSolution;

public class NumberOfActionsAppliedColumnLabelProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		if(element != null && element instanceof QSolution) {
			QSolution solution = (QSolution) element;
			return Integer.toString(solution.getSequence().size());
		}
		return "";
	}
}
