package hvl.plugin.parmorel.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonEditorInput;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeEditorInput;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import hvl.projectparmorel.ml.ModelFixer;
import hvl.projectparmorel.ml.QModelFixer;

public class RepairHandler implements IHandler {

	private String shorterSequences = "Prefer shorter sequences of actions";
	private String longerSequences = "Prefer longer sequences of actions";
	private String higherInContext = "Prefer repairing higher in the hierarchies";
	private String lowerInContext = "Prefer repairing lower in the hierarchies";
	private String rewardModification = "Prefer modification of the original model";
	private String punishModification = "Punish modification of the original model";
	private String punishDeletion = "Punish deletion";
	
	private ModelFixer modelFixer;
	private boolean isHandled;

	public RepairHandler() {
		modelFixer = new QModelFixer();
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
	
	/**
	 * Takes the selected file and creates a duplicate of the file that will represent the repaired model.
	 * 
	 * @return the created duplicate
	 */
	private File createDuplicateFileFrom(File selectedFile) {
		File destinationFile = new File(selectedFile.getParent() + "_temp_" + selectedFile.getName());
		try {
			Files.copy(selectedFile.toPath(), destinationFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destinationFile;
	}
	
	/**
	 * Fixes the selected model with the specified preferences.
	 * 
	 * @param preferences
	 */
	private void fixSelectedModelWith(List<Integer> preferences) {
		modelFixer.setPreferences(preferences);
		
		File file = getSelectedFile();
		File destinationFile = createDuplicateFileFrom(file);
		URI uri = URI.createFileURI(destinationFile.getAbsolutePath());
		Resource model = modelFixer.getModel(uri);
		
		modelFixer.fixModel(model, uri);
		
		compare(file, destinationFile);
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
	
	private File getSelectedFile() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null)
	    {
	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
	        Object firstElement = selection.getFirstElement();
	        if(firstElement instanceof IFile) {
	        	IFile selectedFile = (IFile) firstElement;
	        	return selectedFile.getLocation().toFile();
	        }
	    }
	    return null;
	}

	private void compare(File model1, File model2) {
		URI uri1 = URI.createFileURI(model1.getAbsolutePath());
		URI uri2 = URI.createFileURI(model2.getAbsolutePath());

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

		ResourceSet resourceSet1 = new ResourceSetImpl();
		ResourceSet resourceSet2 = new ResourceSetImpl();

		resourceSet1.getResource(uri1, true);
		resourceSet2.getResource(uri2, true);

		CompareConfiguration configuration = new CompareConfiguration();
		
		//ComparisonEditorInput
		IComparisonScope scope = new DefaultComparisonScope(resourceSet1, resourceSet2, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
	    EMFCompare comparator = EMFCompare.builder().build();
		
		ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(resourceSet1, resourceSet2, null);
		AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
//	    CompareEditorInput input = new ComparisonEditorInput(new EMFCompareConfiguration(configuration), comparison, editingDomain, adapterFactory);
	    CompareEditorInput input = new ComparisonScopeEditorInput(new EMFCompareConfiguration(configuration), 
	            editingDomain, adapterFactory, comparator, scope);
//		CompareEditorInput input = new ComparisonEditorInput(new CompareConfiguration(), comparison, scope);
		
		 CompareUI.openCompareDialog(input); // or CompareUI.openCompareEditor(input);
//		List<Diff> differences = comparison.getDifferences();
//		// Let's merge every single diff
//		IMerger.Registry mergerRegistry = new IMerger.RegistryImpl();
//		IBatchMerger merger = new BatchMerger(mergerRegistry);
//		merger.copyAllLeftToRight(differences, new BasicMonitor());
		
	}
	
	
//	public void compare(Notifier left, Notifier right, Notifier ancestor) {
//	    EMFCompare comparator = EMFCompare.builder().build();
//	    Comparison comparison = comparator.compare(EMFCompare.createDefaultScope(left, right, ancestor));
//
//	    ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(left, right, ancestor);
//	    AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
//	    CompareEditorInput input = new CompareEditorInput(new CompareConfiguration(), comparison, editingDomain, adapterFactory);
//
//	   
//	}
//	private void compare(Notifier left, Notifier right, Notifier ancestor) {
//	    EMFCompare comparator = EMFCompare.builder().build();
//	    Comparison comparison = comparator.compare(new DefaultComparisonScope(left, right, ancestor));
//
//	    ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(left, right, ancestor);
//	    AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
//	    CompareEditorInput input = new ComparisonEditorInput(new CompareConfiguration(), comparison, editingDomain, adapterFactory);
//
//	    CompareUI.openCompareDialog(input); // or CompareUI.openCompareEditor(input);
//	}

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
