package org.docear.addons.highlights.impl;

import org.docear.pdf.feature.AObjectType;
import org.docear.pdf.feature.APDMetaObject;
import org.docear.pdf.feature.COSObjectContext;

public class TrueHighlightAnnotation extends APDMetaObject {
	
	public static final AObjectType TRUE_HIGHTLIGHTED_TEXT = new AObjectType() {
		public String toString() {
			return "TRUE_HIGHTLIGHTED_TEXT";
		}
	};
	
	protected TrueHighlightAnnotation(long uid, COSObjectContext context) {
		super(uid, context);		
	}

	@Override
	public AObjectType getType() {		
		return TRUE_HIGHTLIGHTED_TEXT;
	}

}
