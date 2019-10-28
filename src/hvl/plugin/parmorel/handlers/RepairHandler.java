package hvl.plugin.parmorel.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonEditorInput;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
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
import hvl.projectparmorel.modelrepair.Solution;

@SuppressWarnings("restriction")
public class RepairHandler implements IHandler {
	public final ParmorelModelFixer modelFixer;

	private String shorterSequences = "Prefer shorter sequences of actions";
	private String longerSequences = "Prefer longer sequences of actions";
	private String higherInContext = "Prefer repairing higher in the hierarchies";
	private String lowerInContext = "Prefer repairing lower in the hierarchies";
	private String rewardModification = "Prefer modification of the original model";
	private String punishModification = "Punish modification of the original model";
	private String punishDeletion = "Punish deletion";

	
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
				punishModification, punishDeletion };
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
			List<Integer> preferences = getPreferencesFrom(result);
			fixSelectedModelWith(preferences);
		}
		isHandled = true;
		return null;
	}
	
	

	private void fixSelectedModelWith(List<Integer> preferences) {
		File model = getSelectedFile();
		List<Solution> possibleSolutions = modelFixer.fixModel(model, preferences);
		compare(model, possibleSolutions.get(0).getModel());
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("hvl.plugin.parmorel.views.RepairSelectorView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
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
	


	private List<Integer> getPreferencesFrom(String[] stringPreferences) {
		List<Integer> preferences = new ArrayList<>();
		for (String prefOption : stringPreferences) {
			int preference = getPreferenceNumberFrom(prefOption);
			preferences.add(preference);
		}
		return preferences;
	}

	private int getPreferenceNumberFrom(String preferenceAsString) {
		if (preferenceAsString.equals(shorterSequences))
			return 0;
		if (preferenceAsString.equals(longerSequences))
			return 1;
		if (preferenceAsString.equals(higherInContext))
			return 2;
		if (preferenceAsString.equals(lowerInContext))
			return 3;
		if (preferenceAsString.equals(rewardModification))
			return 6;
		if (preferenceAsString.equals(punishModification))
			return 5;
		if (preferenceAsString.equals(punishDeletion))
			return 4;
		return -1;
	}

	

	/**
	 * Compares to models.
	 * 
	 * @see <a href=
	 *      "https://wiki.eclipse.org/EMF_Compare/How_To_Open_Compare_Dialog_With_Comparison">Documentation
	 *      on EMF Compare</a> Both are available from the
	 *      org.eclipse.emf.compare.ide.ui plug-in, in the package
	 *      org.eclipse.emf.compare.ide.ui.internal.editor. This is still
	 *      provisional API so we may break it any time.
	 * 
	 * @param model1
	 * @param model2
	 */
	private void compare(File model1, File model2) {
		System.out.println("comparing " + model1.getName() + " and " + model2.getName());
		URI uri1 = URI.createFileURI(model1.getAbsolutePath());
		URI uri2 = URI.createFileURI(model2.getAbsolutePath());

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

		ResourceSet resourceSet1 = new ResourceSetImpl();
		ResourceSet resourceSet2 = new ResourceSetImpl();

		resourceSet1.getResource(uri1, true);
		resourceSet2.getResource(uri2, true);

		IComparisonScope scope = new DefaultComparisonScope(resourceSet1, resourceSet2, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(resourceSet1, resourceSet2, null);
		AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		CompareConfiguration configuration = new CompareConfiguration();
		CompareEditorInput input = new ComparisonEditorInput(new EMFCompareConfiguration(configuration), comparison,
				editingDomain, adapterFactory);

		CompareUI.openCompareDialog(input);
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
