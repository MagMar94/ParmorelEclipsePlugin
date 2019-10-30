package hvl.plugin.parmorel.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import hvl.projectparmorel.modelrepair.AppliedAction;
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
			List<AppliedAction> steps = solution.getSequence();
			// show steps
		} else {
			showError("An error occured when retrieving the solution.");
		}
	}

//	private void showMessage(String message) {
//		MessageDialog.openInformation(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
//	}
	
	private void showError(String message) {
		MessageDialog.openError(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
	}
}
