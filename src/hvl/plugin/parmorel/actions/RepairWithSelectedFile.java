package hvl.plugin.parmorel.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import hvl.projectparmorel.modelrepair.Solution;

public class RepairWithSelectedFile {
	private StructuredViewer viewer;
	private final String POP_UP_HEADLINE = "Model Repair";

	public RepairWithSelectedFile(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	public void fixModelWithSelectedSolution() {
		IStructuredSelection selection = viewer.getStructuredSelection();
		Object obj = selection.getFirstElement();
		if (obj instanceof Solution) {
			Solution solution = (Solution) obj;
			solution.reward();
			copyFileContent(solution.getOriginal(), solution.getModel());
		} else {
			showError("An error occured when retrieving the solution.");
		}
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
	}

	private void showError(String message) {
		MessageDialog.openError(viewer.getControl().getShell(), POP_UP_HEADLINE, message);
	}

	private void showError(String message, Exception e) {
		MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
		ErrorDialog.openError(viewer.getControl().getShell(), POP_UP_HEADLINE, message, status);
	}

	private static MultiStatus createMultiStatus(String msg, Throwable t) {

		List<Status> childStatuses = new ArrayList<>();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTrace : stackTraces) {
			Status status = new Status(IStatus.ERROR, "com.vogella.tasks.ui", stackTrace.toString());
			childStatuses.add(status);
		}

		MultiStatus ms = new MultiStatus("com.vogella.tasks.ui", IStatus.ERROR, childStatuses.toArray(new Status[] {}),
				t.toString(), t);
		return ms;
	}

	private void copyFileContent(File fileToBeFixed, File solutionFile) {
		try {
			Files.copy(solutionFile.toPath(), fileToBeFixed.toPath(), StandardCopyOption.REPLACE_EXISTING);
			showMessage("Model repaired with chosen solution.");
		} catch (IOException e) {
			showError("An error occurred when handeling the files.", e);
		}
	}
}
