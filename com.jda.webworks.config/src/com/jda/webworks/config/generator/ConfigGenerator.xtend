/*
 * generated by Xtext
 */
package com.jda.webworks.config.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import static extension org.eclipse.xtext.xtend2.lib.ResourceExtensions.*
import com.jda.webworks.config.config.Categories
import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import com.jda.webworks.config.config.Configuration
import com.jda.webworks.config.config.ConfigProperty

class ConfigGenerator implements IGenerator {
	@Inject extension IQualifiedNameProvider nameProvider
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {		
		for(e: resource.allContentsIterable.filter(typeof(Configuration))) {
			fsa.generateFile("webworks_config.xml",e.compile)
		}
	}
	
	def compile(Configuration e) '''
		<?xml version='1.0' ?>
		<manuConfig descriptionResourceBundleName="WebworksConfigResourceBundle" name="webworks" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="manu_config.xsd">
		�FOR category : e.categories�
		  �category.compile�
		�ENDFOR�
		</manuConfig>
	'''
	
	def compile(Categories cat) '''
			<category name="�cat.name�">
			�FOR property : cat.properties�
		  		�property.compile�
			�ENDFOR�		  		
			</category>
	'''
	
	def compile(ConfigProperty property) '''
		<configProperty name="�property.name�">
			<value>�property.value�</value>
			<descriptionResourceKey>�property.resourceKey�</descriptionResourceKey>
			<scope>�property.scope�</scope>
			<restartServer>�property.restart�</restartServer>
		</configProperty>
	'''
}
