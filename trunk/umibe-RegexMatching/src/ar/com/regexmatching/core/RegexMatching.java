package ar.com.regexmatching.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexMatching {

	private boolean matchRegex(String input, String regex){
	      Pattern p = Pattern.compile(regex);
	      Matcher m = p.matcher(input);
	      return (m.find()) ? true : false;
	}
}
