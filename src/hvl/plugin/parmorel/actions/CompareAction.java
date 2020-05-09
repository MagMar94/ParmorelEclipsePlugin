package hvl.plugin.parmorel.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import hvl.plugin.parmorel.views.CompareModelView;
import no.hvl.projectparmorel.qlearning.QSolution;

public class CompareAction extends Action {
	
	private StructuredViewer viewer;
	private final String POP_UP_HEADLINE = "Compare";
	
	public CompareAction(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void run() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		Object obj = selection.getFirstElement();
		if(obj instanceof QSolution) {
			QSolution solution = (QSolution) obj;
			CompareModelView.compare(solution.getOriginal(), solution.getModel());
		} else {
			showError("An error occured when retrieving the solution.");
		}
	}
	
	private void showError(String message) {
		MessageDialog.openError(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
	}
}
