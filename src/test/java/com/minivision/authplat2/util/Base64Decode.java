package com.minivision.authplat2.util;

import com.minivision.authplat2.util.Base64;

public class Base64Decode {

	public static void main(String[] args) {
		String baseStr = "XV1Cs2D94FsYfO3tmRo/kD2nUcOlaOdwp2otYFy7tpKO67jj0jDAChuUOFyOfW6hU6P93NNEwX70/q6EtSNi32j6VPdEvrImFau5gwKn23Aw8r7F9CZCehzx+EUqAKGpZGnezvJBuwh01nmvzhkr6OfCAX7ZO/xpRjsNHCfQtLkblDhcjn1uoVOj/dzTRMF+7t7s8KCrF//MdfP7NniJNc8EHOmldSPnnFEJW/5haIasgCIue/TJ8Nh3HWG7h4Lihq374TXt4bM=";
		System.out.println(new String(Base64.decode(baseStr, Base64.DEFAULT)));
	}

}
