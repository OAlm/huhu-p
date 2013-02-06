package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import com.google.common.base.Predicate;

import fi.metropolia.mediaworks.juju.document.OmorfiToken;
import fi.metropolia.mediaworks.juju.document.PosToken;
import fi.metropolia.mediaworks.juju.document.PartOfSpeech;
import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.syntax.fi.omorfi.OmorfiPartOfSpeech;

/**
 * Description of class
 * 
 * @author ollial
 * 
 */
public class EntityFilter implements Predicate<Gram> {
	private static final String CHECK = "\\p{L}{1,}(-\\p{L}{1,})*";

	@Override
	public boolean apply(Gram gram) {
		if ((gram.size() > 1 && isEntity(gram.firstToken()) && isEntity(gram.lastToken())) || (gram.size() == 1 && isEntity(gram.firstToken()))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEntity(Token token) {
		if (token instanceof OmorfiToken) {
			OmorfiToken ot = (OmorfiToken)token;
			if (ot.getOmorfiPartOfSpeech() == OmorfiPartOfSpeech.PROPER_NOUN) {
				return true;
			} else if (ot.getOmorfiPartOfSpeech() == OmorfiPartOfSpeech.UNIDENTIFIED && checkToken(ot)) {
				return true;
			}
		} else if (token instanceof PosToken) {
			PosToken ct = (PosToken) token;
			if ((ct.getPartOfSpeech() == PartOfSpeech.BASE || ct.getPartOfSpeech() == PartOfSpeech.UNIDENTIFIED) && checkToken(ct)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean checkToken(PosToken token) {
		return token.getCaseChange().upperCase && token.getText().matches(CHECK);
	}
}
