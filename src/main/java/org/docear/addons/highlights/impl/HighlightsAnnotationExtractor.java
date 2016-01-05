package org.docear.addons.highlights.impl;

import java.awt.geom.AffineTransform;

import org.docear.pdf.annotation.AnnotationExtractor;
import org.docear.pdf.annotation.HighlightAnnotation;
import org.docear.pdf.feature.APDMetaObject;
import org.docear.pdf.feature.COSObjectContext;

import de.intarsys.pdf.cds.CDSRectangle;
import de.intarsys.pdf.content.CSDeviceBasedInterpreter;
import de.intarsys.pdf.content.text.CSTextExtractor;
import de.intarsys.pdf.pd.PDAnnotation;
import de.intarsys.pdf.pd.PDDocument;
import de.intarsys.pdf.pd.PDHighlightAnnotation;
import de.intarsys.pdf.pd.PDPage;
import de.intarsys.pdf.pd.PDSquigglyAnnotation;
import de.intarsys.pdf.pd.PDStrikeOutAnnotation;
import de.intarsys.pdf.pd.PDTextMarkupAnnotation;
import de.intarsys.pdf.pd.PDUnderlineAnnotation;
import de.intarsys.pdf.tools.kernel.PDFGeometryTools;

public class HighlightsAnnotationExtractor extends AnnotationExtractor {
	
	private boolean importpopup = false;

	public HighlightsAnnotationExtractor(PDDocument document) {
		super(document);		
	}
	
	

	public boolean isImportpopup() {
		return importpopup;
	}



	public void setImportpopup(boolean importpopup) {
		this.importpopup = importpopup;
	}


	@Override
	protected APDMetaObject getHighlight(PDAnnotation annotation) {		
		if ((annotation.getClass() == PDTextMarkupAnnotation.class 
				|| annotation.getClass() == PDHighlightAnnotation.class
				|| annotation.getClass() == PDStrikeOutAnnotation.class 
				|| annotation.getClass() == PDUnderlineAnnotation.class 
				|| annotation.getClass() == PDSquigglyAnnotation.class)
				&& !ignoreHighlights()) {
			COSObjectContext context = new COSObjectContext(annotation);
			APDMetaObject meta;			
			if (annotation.getContents() != null && annotation.getContents().length() > 0 && isImportpopup()) {
				meta = new HighlightAnnotation(getOrCreateUID(context), context);
				meta.setText(annotation.getContents());
			}
			else{
				meta = new TrueHighlightAnnotation(getOrCreateUID(context), context);
				PDTextMarkupAnnotation markupAnnotation = (PDTextMarkupAnnotation)annotation;
				float[] quadpoints = markupAnnotation.getQuadPoints();				
				if(quadpoints.length % 8 == 0){
					StringBuilder sb = new StringBuilder();
					CSTextExtractor textExtractor = new CSTextExtractor();
					PDPage page = annotation.getPage();
					AffineTransform pageTx = new AffineTransform();
					PDFGeometryTools.adjustTransform(pageTx, page);
			        textExtractor.setDeviceTransform(pageTx);
			        CSDeviceBasedInterpreter interpreter = new CSDeviceBasedInterpreter(null, textExtractor);
			        CDSRectangle oldRect = null; 
					for(int i = 0; i < quadpoints.length / 8; i++){
						CDSRectangle rect = new CDSRectangle(quadpoints[8*i+4], quadpoints[8*i+5], quadpoints[8*i+2], quadpoints[8*i+3]);						
						textExtractor.setBounds(rect.toRectangle());		            
			            interpreter.process(page.getContentStream(), page.getResources());			            
			            if(oldRect != null){
			            	if(Math.abs(oldRect.getLowerLeftY() - rect.getLowerLeftY()) >= 1){
			            		sb.append(System.getProperty("line.separator"));
			            	}
			            	else if (Math.abs(oldRect.getUpperRightX() - rect.getLowerLeftX()) >= 1){
			            		sb.append(" ");
			            	}
			            }			            
			            sb.append(textExtractor.getContent());
			            oldRect = rect;
					}
					if(!sb.toString().isEmpty()) 
						meta.setText(sb.toString());
				}
			}
			if (meta.getText() == null) {
				return null;
			}
			Integer objectNumber = annotation.cosGetObject().getIndirectObject().getObjectNumber();			
			meta.setObjectNumber(objectNumber);
			meta.setDestination(getDestination(annotation));
			return meta;
		}
		return null;
	}

	@Override
	protected APDMetaObject getMetaObject(PDAnnotation annotation,
			String lastString) {		
		APDMetaObject metaObject = getComment(annotation);
		if(metaObject == null) {
			metaObject = getHighlight(annotation);
		}
		return metaObject;
	}
}
