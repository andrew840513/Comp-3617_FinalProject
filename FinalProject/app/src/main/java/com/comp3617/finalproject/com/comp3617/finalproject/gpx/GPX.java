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

public class GPX{
	private static GPX gpx;

	@Attribute(name = "schemaLocation")
	private static final String schemaLocation = "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd";

	@ElementList(name = "wpt", inline = true)
	private List<WPT> wpt;

	public GPX(@ElementList(name = "wpt", inline = true) List<WPT> wpt,
			@Attribute(name = "schemaLocation") String schemaLocation) {
		this.wpt = wpt;
	}

	public GPX(List<WPT> wpts) {
		this.wpt = wpts;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public List<WPT> getWpt() {
		return wpt;
	}

	public void setWpt(List<WPT> wpt) {
		this.wpt = wpt;
	}

	public static GPX getGpx() {
		return gpx;
	}

	public static void setGpx(GPX gpx) {
		GPX.gpx = gpx;
	}
}