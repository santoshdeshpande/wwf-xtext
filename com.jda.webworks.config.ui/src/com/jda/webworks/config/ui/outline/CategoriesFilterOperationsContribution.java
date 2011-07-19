package com.jda.webworks.config.ui.outline;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.ui.PluginImageHelper;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.actions.AbstractFilterOutlineContribution;
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode;
import org.eclipse.xtext.ui.util.ResourceUtil;

import com.google.inject.Inject;
import com.jda.webworks.config.config.ConfigPackage;
import com.jda.webworks.config.config.ConfigProperty;

public class CategoriesFilterOperationsContribution extends
		AbstractFilterOutlineContribution {

	public static final String PREFERENCE_KEY = "ui.outline.filterOperations";
	@Inject
	private PluginImageHelper imageHelper;

	@Override
	protected boolean apply(IOutlineNode node) {
		if (!(node instanceof EObjectNode))
			return false;
		EObjectNode objectNode = (EObjectNode) node;
		EClass clazz = objectNode.getEClass();
		return true;
	}

	@Override
	public String getPreferenceKey() {
		return PREFERENCE_KEY;
	}

	@Override
	protected void configureAction(Action action) {
		action.setText("Hide Local");
		action.setDescription("Hide Local");
		action.setToolTipText("Hide Local");
		action.setImageDescriptor(getImageDescriptor("/icons"));
	}

	protected ImageDescriptor getImageDescriptor(String imagePath) {
		return ImageDescriptor.createFromImage(imageHelper
				.getImage("public_co.gif"));
	}

}