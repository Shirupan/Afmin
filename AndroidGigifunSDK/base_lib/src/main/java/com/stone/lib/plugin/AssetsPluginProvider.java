package com.stone.lib.plugin;

import android.content.Context;
import android.text.TextUtils;

import com.stone.commons.codec.binary.Base64;
import com.stone.lib.internal.ResourceLoader;
import com.stone.lib.utils.LogUtils;
import com.stone.lib.utils.RSACrypt;

import org.json.JSONArray;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AssetsPluginProvider extends PluginProvider {

	private Context mContext;
	private static final String TAG = "AssetsPluginProvider";
	private static StringBuilder sKey = new StringBuilder();
	static{
		sKey.append("a");
		sKey.append("stone");
		sKey.delete(2, 3);
		sKey.append("b");
		sKey.append((int)Math.pow(3, 3)-7);
		sKey.append((int)Math.cbrt(1));
		sKey.append((int)Math.sqrt(64));
		sKey.append("0101");
	}

	public AssetsPluginProvider(Context cxt) {
		mContext = cxt;
	}

	@Override
	public ArrayList<PluginEntry> providePlugins() {
		return new PluginEntryParser().parse();
	}

	private class PluginEntryParser extends DefaultHandler {

		private PluginEntry mPluginEntry;

		private ArrayList<PluginEntry> mEntries = new ArrayList<PluginEntry>();

		PluginEntryParser() {
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				String plugins = readPlugins(mContext);
				InputSource is = new InputSource(new StringReader(plugins));
				parser.parse(is, this);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("plugins.xml can not be parsed");
			}
		}

		private String readPlugins(Context cxt) {
			ResourceLoader loader = ResourceLoader.getDefault(cxt);
			String pluginsStr = loader.readFile("stone/plugins.xml");

//			if (!pluginsStr.equals("")) {
//				try {
//					JSONArray data = new JSONArray(pluginsStr);
//					StringBuilder outputStr = new StringBuilder(2048);
//					int len = data.length();
//					for (int i = 0; i < len; i++) {
//						String inputStr = data.getString(i);
//						outputStr.append(inputStr);
//					}
//					String str = outputStr.toString();
//					LogUtils.i(TAG, "plugins.xml: " + str);
//					return str;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				LogUtils.i(TAG, "plugins.xml: " + pluginsStr);
				return pluginsStr;
//			}
//			return "";
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (qName.equals("plugin")) {
				String className = attributes.getValue("class");
				String init = attributes.getValue("required");
				String name = attributes.getValue("name");
				String version = attributes.getValue("version");
				String description = attributes.getValue("description");
				String serviceClass = attributes.getValue("serviceClass");

				String interfaces = attributes.getValue("interfaces");
				ArrayList<String> list = null;
				if (!TextUtils.isEmpty(interfaces)) {
					list = new ArrayList<String>(2);
					String[] splits = interfaces.split(",");
					if (splits != null) {
						for (String inter : splits) {
							if (inter != null) {
								String real = inter.trim();
								if (!TextUtils.isEmpty(real)) {
									list.add(real);
								}
							}
						}
					}
				}

				PluginEntry entry = new PluginEntry();
				entry.mainClass = className;
				entry.label = name;
				entry.version = version;
				entry.description = description;
				entry.required = Boolean.valueOf(init);
				entry.serviceClass = serviceClass;
				
				entry.interfaces = list;
				
				mPluginEntry = entry;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (qName.equals("plugin")) {
				mEntries.add(mPluginEntry);
			}
		}

		public ArrayList<PluginEntry> parse() {
			return mEntries;
		}
	}

}
