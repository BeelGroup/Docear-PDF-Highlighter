package org.docear.addons.test;

import java.io.File;

import org.docear.addons.highlights.IHighlightsImporter;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.addpluginsfrom.OptionReportAfter;


public class TestAddonController {

	public static void main(String[] args) {		
		File addonsDir = new File("C:\\Users\\Anwender\\.docear\\addons");	    
		PluginManager manager = PluginManagerFactory.createPluginManager();		
		manager.addPluginsFrom(addonsDir.toURI(), new OptionReportAfter());
		IHighlightsImporter addon = manager.getPlugin(IHighlightsImporter.class);
		System.out.println(addon);
	}

}
