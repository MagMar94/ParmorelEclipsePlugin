package hvl.plugin.parmorel.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
		MessageDialog.openInformation(viewer.getControl().getShell(), "Select solution", message);
	}
	
	private void copyFileContent(File fileToBeFixed, File solutionFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(solutionFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileToBeFixed, false));
			String line = reader.readLine();
			while(line != null) {
				writer.write(line);
				writer.newLine();
				line = reader.readLine();
			}
			writer.close();
			reader.close();		
			showMessage("Model repaired with chosen solution.");
		} catch (IOException e) {
			showMessage("An error occurred.");
		}
	}
}
