package hvl.plugin.parmorel.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import hvl.plugin.parmorel.views.CompareModelView;
import hvl.projectparmorel.modelrepair.Solution;

public class ShowStepsAction extends Action {
	private StructuredViewer viewer;
	
	private final String POP_UP_HEADLINE = "Show steps";

	public ShowStepsAction(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void run() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		Object obj = selection.getFirstElement();
		if (obj instanceof Solution) {
			Solution solution = (Solution) obj;
			// show steps
		} else {
			showMessage("An error occured when retrieving the solution.");
		}
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
	}
}
