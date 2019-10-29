package hvl.plugin.parmorel.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import hvl.projectparmorel.modelrepair.Solution;

public class RepairWithSelectedFile {
	private StructuredViewer viewer;
	
	public RepairWithSelectedFile(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	public void fixModelWithSelectedSolution() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		Object obj = selection.getFirstElement();
		if(obj instanceof Solution) {
			Solution solution = (Solution) obj;
			copyFileContent(solution.getOriginal(), solution.getModel());
		} else {
			showMessage("An error occured when retrieving the solution.");
		}
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Repairing status", message);
	}
	
	private void copyFileContent(File fileToBeFixed, File solutionFile) {
		try {
			Files.copy(solutionFile.toPath(), fileToBeFixed.toPath(), StandardCopyOption.REPLACE_EXISTING);
			showMessage("Model repaired with chosen solution.");
		} catch (IOException e) {
			showMessage("An error occurred: " + e.getMessage());
		}
	}
}
