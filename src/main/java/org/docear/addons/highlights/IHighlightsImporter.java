package org.docear.addons.highlights;

import java.io.IOException;
import java.util.List;






import net.xeoh.plugins.base.Plugin;

import org.docear.pdf.feature.APDMetaObject;

import de.intarsys.pdf.pd.PDDocument;

public interface IHighlightsImporter extends Plugin{
	
	public List<APDMetaObject> getMetaObjects(PDDocument document, boolean importComments, boolean importHighlightedTexts, boolean importPopup) throws IOException;
	
	public boolean isDocumentModified();	
	
	public void resetAll();
}
