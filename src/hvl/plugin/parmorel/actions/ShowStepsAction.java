package hvl.plugin.parmorel.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import hvl.plugin.parmorel.dialogs.ShowStepsDialog;
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
			displaySteps(steps);
		} else {
			displaySteps(null);
			showError("An error occured when retrieving the solution.");
		}
	}

	private void displaySteps(List<AppliedAction> steps) {
		ShowStepsDialog dialog = new ShowStepsDialog(viewer.getControl().getShell(), steps);
		dialog.open();
	}
	
//	private void showMessage(String message) {
//		MessageDialog.openInformation(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
//	}
	
	private void showError(String message) {
		MessageDialog.openError(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
	}
}
