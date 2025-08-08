/*******************************************************************************
 * Copyright 2019 Yohanes Randy Kurnianto
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package id.co.corsys.faspay.va.security;

import java.util.Arrays;

public class CorsysPasswordEncoder {
	public static String encode(String userid, String password, String key) {
		String d = userid;
		String e = password;
		String b = key;
		String param = "";
		for (int i = 0; i < e.length(); i++) {
			if (d.length() < e.length()) {
				for (int j = d.length(); j < e.length(); j++) {
					d = d + " ";
				}
			}
			String newPass = e.substring(i, i + 1);
			String newUser = d.substring(i, i + 1);
			param = param + newPass + newUser;
		}
		d = d.trim();

		int c = d.substring(1, 2).charAt(0) + d.substring(d.length() - 1, d.length()).charAt(0) - 128;
		return fencrypthexa(param + d, c, b);
	}

	public static String decode(String v, String q) {
		String[] p = q.split("");
		double x = v.substring(1, 2).charAt(0) + v.substring(v.length() - 1, v.length()).charAt(0) - 128;
		String r = "", z = "", n = "";
		String[] k = "0123456789abcdef".split("");
		for (int j = 0; j < p.length; j++) {
			if ((j + 1) % 2 == 1) {
				String A = p[j];
				int a = Arrays.asList(k).indexOf(A);
				String B = p[j + 1];
				int b = Arrays.asList(k).indexOf(B);
				for (int i = 0; i <= 127; i++) {
					double c = (i + x);
					double f = c / 16.0;
					if (f == a + b / 16.0)
						n += Character.toString((char) i);
				}
			}
		}
		for (int i = 0; i < n.length(); i++)
			if ((i + 1) % 2 == 1)
				z += n.split("")[i];
		if ((z.length() - v.length()) < 0)
			return r;
		z = z.substring(0, z.length() - v.length());
		for (int i = 0; i < z.length(); i++)
			if ((i + 1) % 2 == 1)
				r += z.split("")[i];
		return r;
	}

	public static String fencrypthexa(String d, int e, String b) {
		String c = "";
		int a;
		for (a = 0; a < d.length(); a++)
			c = c + d.substring(a, a + 1) + facakpwd(d, a, b);
		return fascihexa(c, e);
	}

	public static String facakpwd(String d, int a, String b) {
		return (b + "QAZWSXEDCRFVTGBYHNUJMIKLOP" + d + "1,2.3_4+5#6!7*8/90").substring(a, a + 1);
	}

	public static String fascihexa(String d, int e) {
		String b = null;
		int c = 0, a = 0, g;
		String h = "";
		int f = 0;
		String[] k = "0123456789abcdef".split("");

		for (g = 0; g < d.length(); g++) {
			c = d.substring(g, g + 1).charAt(0) + e;
			for (a = 7; 0 <= a; a--) {
				f = (int) Math.floor(c / Math.pow(16, a));
				c -= f * Math.pow(16, a);
				b += k[f];
			}
			h += b.substring(b.length() - 2);
		}
		return h;
	}
}
