package com.comp3617.finalproject.com.comp3617.finalproject.gpx;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Andrew on 2017-07-12.
 */
@Root(name = "gpx")
@NamespaceList({ @Namespace(reference = "http://www.topografix.com/GPX/1/1"),
		@Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi") })
public class GPX {
	@Attribute(name = "schemaLocation")
	static final String schemaLocation = "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd";

	@ElementList(name = "wpt", inline = true)
	private List<WPT> wpt;

	public GPX(List<WPT> wpt) {
		this.wpt = wpt;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public List<WPT> getWpt() {
		return wpt;
	}
}