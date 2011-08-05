/*
 *	Mario Sangiorgio - mariosangiorgio AT gmail DOT com
 *
 *  This file is part of lyrics.
 * 
 *  lyrics is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  lyrics is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with lyrics.  If not, see <http://www.gnu.org/licenses/>.
 */

package lyrics.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class AlternateNames {
	private static AlternateNames alternateNames;

	private HashMap<String, Collection<String>> names = new HashMap<String, Collection<String>>();

	public static AlternateNames getAlternateNames(String filePath) {
		if (alternateNames == null) {
			alternateNames = new AlternateNames(filePath);
		}
		return alternateNames;
	}

	private AlternateNames(String filePath) {
		BufferedReader reader;
		try {
			URL path = getClass().getResource(filePath);
			BufferedInputStream stream = (BufferedInputStream) path
					.getContent();
			InputStreamReader streamReader = new InputStreamReader(stream);
			reader = new BufferedReader(streamReader);

			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\t");
				String artistName = tokens[0];
				for (int i = 1; i < tokens.length; i++) {
					addAlternateName(artistName, tokens[i]);
				}
			}
			stream.close();
			streamReader.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addAlternateName(String artistName, String alternateName) {
		Collection<String> alternates;
		if (names.containsKey(artistName)) {
			alternates = names.get(artistName);
		} else {
			alternates = new Vector<String>();
		}
		alternates.add(alternateName);
		names.put(artistName, alternates);
	}

	public Collection<String> getAlternateNameList(String artistName) {
		Collection<String> alternates;
		alternates = new Vector<String>();
		alternates.add(artistName);
		if (names.containsKey(artistName)) {
			alternates.addAll(names.get(artistName));
		}
		return alternates;
	}
}