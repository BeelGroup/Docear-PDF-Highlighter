package org.docear.addons.highlights.impl;

import java.io.IOException;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;

import org.docear.addons.highlights.IHighlightsImporter;
import org.docear.pdf.feature.APDMetaObject;

import de.intarsys.pdf.pd.PDDocument;

@PluginImplementation
public class HighlightsImporter implements IHighlightsImporter {
	
	private HighlightsAnnotationExtractor extractor;
	
	@Init
	public void init(){
		System.out.println("HighlightsImporter loaded successfully.");
	}

	public List<APDMetaObject> getMetaObjects(PDDocument document,	boolean importComments, boolean importHighlightedTexts, boolean importPopup) throws IOException {
		HighlightsAnnotationExtractor extractor = new HighlightsAnnotationExtractor(document);
		extractor.setIgnoreComments(!importComments);
		extractor.setIgnoreHighlights(!importHighlightedTexts);
		extractor.setImportpopup(importPopup);
		return extractor.getMetaObjects();		
	}

	public boolean isDocumentModified() {
		if(this.extractor != null) return extractor.isDocumentModified();
		return false;
	}
	
	public void resetAll(){
		if(this.extractor != null) extractor.resetAll();;
	}

}
