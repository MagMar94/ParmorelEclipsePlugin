package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.general.AppliedAction;

public class ActionDescriprionLabelProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		if(element != null && element instanceof AppliedAction) {
			AppliedAction action = (AppliedAction) element;
			return action.getAction().getMessage();
		}
		return "";
	}
}
