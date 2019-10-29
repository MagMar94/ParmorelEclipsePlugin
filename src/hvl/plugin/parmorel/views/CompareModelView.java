package hvl.plugin.parmorel.views;

import java.io.File;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
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

@SuppressWarnings("restriction")
public class CompareModelView {
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
	public static void compare(File model1, File model2) {
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
}
