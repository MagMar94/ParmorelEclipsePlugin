package hvl.plugin.parmorel.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import hvl.plugin.parmorel.views.CompareModelView;
import hvl.projectparmorel.modelrepair.Solution;

public class CompareAction extends Action {
	
	private StructuredViewer viewer;
	
	public CompareAction(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	public void run() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		Object obj = selection.getFirstElement();
		if(obj instanceof Solution) {
			Solution solution = (Solution) obj;
			CompareModelView.compare(solution.getOriginal(), solution.getModel());
		} else {
			showMessage("An error occured when retrieving the solution.");
		}
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Select solution", message);
	}
	
//	private Action getCompareAction() {
//		return new Action() {
//			public void run() {
//				IStructuredSelection selection = viewer.getStructuredSelection();
//				Object obj = selection.getFirstElement();
//				if(obj instanceof Solution) {
//					Solution solution = (Solution) obj;
//					CompareModelView.compare(solution.getOriginal(), solution.getModel());
//				} else {
//					showMessage("An error occured when retrieving the solution.");
//				}
//			}
//		};
//	}

}
