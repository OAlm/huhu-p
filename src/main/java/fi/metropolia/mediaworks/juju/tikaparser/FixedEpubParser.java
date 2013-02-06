package fi.metropolia.mediaworks.juju.tikaparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.epub.EpubContentParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class FixedEpubParser implements Parser {
	private static final long serialVersionUID = 1L;

	private static final Set<MediaType> SUPPORTED_TYPES = Collections.unmodifiableSet(new HashSet<MediaType>(Arrays.asList(MediaType.application("epub+zip"))));
	
	private EpubReader epubReader = new EpubReader();

	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	@Override
	public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {
		XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
        xhtml.startDocument();
		
		Book b = epubReader.readEpub(stream);
		
		List<Resource> l = b.getContents();
		
		for (Resource r : l) {		
			Parser p;
			p = new EpubContentParser();
			p.parse(r.getInputStream(), handler, metadata, context);
		}
		
		xhtml.endDocument();
	}

}
