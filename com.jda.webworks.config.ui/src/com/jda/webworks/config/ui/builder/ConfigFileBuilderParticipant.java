package com.jda.webworks.config.ui.builder;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IBuildContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess;
import org.eclipse.xtext.builder.IXtextBuilderParticipant;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.util.Pair;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Sven Efftinge - Initial contribution and API
 * @author Jan Koehnlein
 */
public class ConfigFileBuilderParticipant implements IXtextBuilderParticipant {

	@Inject
	private IGenerator generator;

	@Inject
	private Provider<EclipseResourceFileSystemAccess> fileAccessProvider;

	@Inject
	private IResourceServiceProvider resourceServiceProvider;

	@Inject
	private IStorage2UriMapper storage2UriMapper;

	private Map<URI, Set<String>> sourceTargetMap = newHashMap();

	public void build(IBuildContext context, IProgressMonitor monitor) throws CoreException {
		final IProject builtProject = context.getBuiltProject();
		final IFolder srcGenFolder = getSrcGenFolder(builtProject);
		if (!srcGenFolder.exists())
			return;
		if (!isValidOutputFolder(builtProject, srcGenFolder))
			return;
		for (IResourceDescription.Delta delta : context.getDeltas()) {
			final Set<String> oldFiles = newHashSet();
			if (sourceTargetMap.containsKey(delta.getUri())) {
				oldFiles.addAll(sourceTargetMap.get(delta.getUri()));
			}
			final Set<String> newFiles = newHashSet();
			IFileSystemAccess fileSystemAccess = getConfiguredFileSystemAccess(srcGenFolder, new IAcceptor<String>() {
				public void accept(String fileName) {
					oldFiles.remove(fileName);
					newFiles.add(fileName);
				}
			});
			if (delta.getNew() == null) {
				handleDeletion(delta, context, fileSystemAccess);
			} else {
				handleChangedContents(delta, context, fileSystemAccess);
			}
			for (String removeFile : oldFiles) {
				fileSystemAccess.deleteFile(removeFile);
			}
			if (!newFiles.isEmpty()) {
				sourceTargetMap.put(delta.getUri(), newFiles);
			}
		}
	}

	/**
	 * @since 2.0
	 */
	protected boolean isValidOutputFolder(IProject javaProject, final IFolder srcGenFolder) {
		return srcGenFolder != null && srcGenFolder.exists();
	}

	/**
	 * @since 2.0
	 */
	protected IFolder getSrcGenFolder(final IProject builtProject) {
		return builtProject.getFolder("src-gen");
	}

	protected IFileSystemAccess getConfiguredFileSystemAccess(IFolder srcGenFolder, IAcceptor<String> newFileAcceptor) {
		EclipseResourceFileSystemAccess access = fileAccessProvider.get();
		access.setOutputPath(srcGenFolder.getFullPath().toString());
		access.setNewFileAcceptor(newFileAcceptor);
		return access;
	}

	protected void handleChangedContents(Delta delta, IBuildContext context, IFileSystemAccess fileSystemAccess) {
		if (!resourceServiceProvider.canHandle(delta.getUri()))
			return;
		Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
		if (shouldGenerate(resource, context)) {
			generator.doGenerate(resource, fileSystemAccess);
			context.needRebuild();
		}
	}

	protected boolean shouldGenerate(Resource resource, IBuildContext context) {
		try {
			Iterable<Pair<IStorage, IProject>> storages = storage2UriMapper.getStorages(resource.getURI());
			for (Pair<IStorage, IProject> pair : storages) {
				if (pair.getFirst() instanceof IFile && pair.getSecond().equals(context.getBuiltProject())) {
					return((IFile) pair.getFirst()).findMaxProblemSeverity(null, true, IResource.DEPTH_INFINITE) != IMarker.SEVERITY_ERROR;
				}
			}
			return false;
		} catch (CoreException exc) {
			throw new WrappedException(exc);
		}
	}

	protected void handleDeletion(Delta delta, IBuildContext context, IFileSystemAccess fileSystemAccess) {
	}

}

