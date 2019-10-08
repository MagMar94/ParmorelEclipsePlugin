package hvl.plugin.parmorel.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import hvl.projectparmorel.ml.QLearning;

public class RepairHandler implements IHandler {

	private String shorterSequences = "Prefer shorter sequences of actions";
	private String longerSequences = "Prefer longer sequences of actions";
	private String higherInContext = "Prefer repairing higher in the context hierarchies";
	private String lowerInContext = "Prefer repairing lower in the context hierarchies";
	private String rewardModification = "Prefer modification of the original model";
	private String punishModification = "Punish modification of the original model";
	private String punishDeletion = "Punish deletion";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = new Shell();
		String[] options = { shorterSequences, longerSequences, higherInContext, lowerInContext, rewardModification,
				punishModification, punishDeletion };
		ListSelectionDialog dialog = new ListSelectionDialog(shell, options, ArrayContentProvider.getInstance(),
				new LabelProvider(), "selection message");
		dialog.setTitle("Select preferences");
		dialog.setInitialSelections(new Object[] {});
		dialog.open();
		Object[] selection = dialog.getResult();
		if (null == selection) {
			MessageDialog.openWarning(shell, "Warning", "You did not select any preferences!");
		} else {
			String[] result = new String[selection.length];
			System.arraycopy(selection, 0, result, 0, selection.length);
			List<Integer> preferences = getPreferencesFrom(result);
			QLearning qLearning = new QLearning(preferences);
			
			IFile file = getSelectedFile();
			File destinationFile = new File(file.getLocation().toFile().getParent() + "_temp_" + file.getName());
			try {
				Files.copy(file.getLocation().toFile().toPath(), destinationFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			URI uri = URI.createFileURI(destinationFile.getAbsolutePath());
			Resource model = qLearning.getResourceSet().getResource(uri, true);
			Resource auxModel = qLearning.getResourceSet().createResource(uri);
			auxModel.getContents().addAll(EcoreUtil.copyAll(model.getContents()));

//			
//			try {
//				file.copy(file.getFullPath(), 0, null);
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}

			

			

//			File dest = new File("temp_" + file.getName());
//			URI copyUri = URI.createFileURI(dest.getAbsolutePath());
			
			qLearning.fixModel(model, uri);
		}

		return null;
	}

	private List<Integer> getPreferencesFrom(String[] stringPreferences) {
		List<Integer> preferences = new ArrayList<>();
		for (String prefOption : stringPreferences) {
			int preference = getPreferenceNumberFrom(prefOption);
			preferences.add(preference);
		}
		return preferences;
	}

	private int getPreferenceNumberFrom(String preferenceAsString) {
		if(preferenceAsString.equals(shorterSequences))
			return 0;
		if(preferenceAsString.equals(longerSequences))
			return 1;
		if(preferenceAsString.equals(higherInContext))
			return 2;
		if(preferenceAsString.equals(lowerInContext))
			return 3;
		if(preferenceAsString.equals(rewardModification))
			return 6;
		if(preferenceAsString.equals(punishModification))
			return 5;
		if(preferenceAsString.equals(punishDeletion))
			return 4;
		return -1;
	}
	
	private IFile getSelectedFile() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null)
	    {
	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
	        Object firstElement = selection.getFirstElement();
	        if(firstElement instanceof IFile) {
	        	return (IFile) firstElement;
	        }
	    }
	    return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
