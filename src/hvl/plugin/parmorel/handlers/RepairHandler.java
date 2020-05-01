package hvl.plugin.parmorel.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import hvl.plugin.parmorel.model.ParmorelModelFixer;
import hvl.projectparmorel.exceptions.NoErrorsInModelException;
import hvl.projectparmorel.qlearning.QSolution;
import hvl.projectparmorel.reward.PreferenceOption;

public class RepairHandler implements IHandler {
	public final ParmorelModelFixer modelFixer;

	private String shorterSequences = "Prefer shorter sequences of actions";
	private String longerSequences = "Prefer longer sequences of actions";
	private String higherInContext = "Prefer repairing higher in the hierarchies";
	private String lowerInContext = "Prefer repairing lower in the hierarchies";
	private String rewardModification = "Prefer modification of the original model";
	private String punishModification = "Punish modification of the original model";
	private String punishDeletion = "Punish deletion";
	private String closestDistance = "Prefer close distance to original";
	private String preferMaintainability = "Prefer maintainability";
	private String preferUnderstandability = "Prefer understandability";
	private String preferComplexity = "Prefer complexity";
	private String preferReuse = "Prefer reuse";
	private String preferRelaxation = "Prefer relaxation";

	private boolean isHandled;

	public RepairHandler() {
		modelFixer = new ParmorelModelFixer();
		isHandled = false;
	}

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
		isHandled = false;
		Shell shell = new Shell();
		String[] options = { shorterSequences, longerSequences, higherInContext, lowerInContext, rewardModification,
				punishModification, punishDeletion, closestDistance, preferMaintainability, preferUnderstandability,
				preferComplexity, preferReuse, preferRelaxation };
		ListSelectionDialog dialog = new ListSelectionDialog(shell, options, ArrayContentProvider.getInstance(),
				new LabelProvider(), "Preference options:");
		dialog.setTitle("Select preferences");
		dialog.setInitialSelections(new Object[] {});
		dialog.open();
		Object[] selection = dialog.getResult();
		if (null == selection) {
			MessageDialog.openWarning(shell, "Warning", "You did not select any preferences!");
		} else {
			String[] result = new String[selection.length];
			System.arraycopy(selection, 0, result, 0, selection.length);
			List<PreferenceOption> preferences = getPreferencesFrom(result);
			try {
				fixSelectedModelWith(preferences);
			} catch (NoErrorsInModelException e) {
				MessageDialog.openWarning(shell, "Info", "No supported errors found in the model.");
			}
		}
		isHandled = true;
		return null;
	}

	private List<QSolution> fixSelectedModelWith(List<PreferenceOption> preferences) throws NoErrorsInModelException {
		File model = getSelectedFile();
		List<QSolution> possibleSolutions = modelFixer.fixModel(model, preferences);
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("hvl.plugin.parmorel.views.RepairSelectorView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return possibleSolutions;
	}

	private File getSelectedFile() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IFile) {
				IFile selectedFile = (IFile) firstElement;
				return selectedFile.getLocation().toFile();
			}
		}
		return null;
	}

	private List<PreferenceOption> getPreferencesFrom(String[] stringPreferences) {
		List<PreferenceOption> preferences = new ArrayList<>();
		for (String prefOption : stringPreferences) {
			PreferenceOption preference = getPreferenceNumberFrom(prefOption);
			preferences.add(preference);
		}
		return preferences;
	}

	private PreferenceOption getPreferenceNumberFrom(String preferenceAsString) {
		if (preferenceAsString.equals(shorterSequences))
			return PreferenceOption.SHORT_SEQUENCES_OF_ACTIONS;
		if (preferenceAsString.equals(longerSequences))
			return PreferenceOption.LONG_SEQUENCES_OF_ACTIONS;
		if (preferenceAsString.equals(higherInContext))
			return PreferenceOption.REPAIR_HIGH_IN_CONTEXT_HIERARCHY;
		if (preferenceAsString.equals(lowerInContext))
			return PreferenceOption.REPAIR_LOW_IN_CONTEXT_HIERARCHY;
		if (preferenceAsString.equals(rewardModification))
			return PreferenceOption.REWARD_MODIFICATION_OF_MODEL;
		if (preferenceAsString.equals(punishModification))
			return PreferenceOption.PUNISH_MODIFICATION_OF_MODEL;
		if (preferenceAsString.equals(punishDeletion))
			return PreferenceOption.PUNISH_DELETION;
		if (preferenceAsString.equals(closestDistance))
			return PreferenceOption.PREFER_CLOSE_DISTANCE_TO_ORIGINAL;
		if (preferenceAsString.equals(preferMaintainability))
			return PreferenceOption.PREFER_MAINTAINABILITY;
		if (preferenceAsString.equals(preferUnderstandability))
			return PreferenceOption.PREFER_UNDERSTANDABILITY;
		if (preferenceAsString.equals(preferComplexity))
			return PreferenceOption.PREFER_COMPLEXITY;
		if (preferenceAsString.equals(preferReuse))
			return PreferenceOption.PREFER_REUSE;
		if (preferenceAsString.equals(preferRelaxation))
			return PreferenceOption.PREFER_RELAXATION;
		return null;
	}

	@Override
	public boolean isEnabled() {
//		IFile file = getSelectedFile();
//		URI uri = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());
//		Resource model = modelFixer.getModel(uri);
//		return modelFixer.modelIsBroken(model);
		return true;
	}

	@Override
	public boolean isHandled() {
		return isHandled;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
