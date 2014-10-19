package in.retalemine.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

	private InputParser() {
	}

	public static String getCamelCaseString(String input) {
		StringBuffer camelCaseOutput = new StringBuffer();
		List<String> unitDescList = ComputationUtil
				.getValidUnitDescriptionList();
		Matcher camelCaseMatcher = Pattern.compile("(([a-z])([a-z]*))",
				Pattern.CASE_INSENSITIVE).matcher(
				input.trim().replaceAll("\\s+", " "));
		while (camelCaseMatcher.find()) {
			if (unitDescList.contains(camelCaseMatcher.group(1).toLowerCase())) {
				camelCaseMatcher.appendReplacement(camelCaseOutput,
						camelCaseMatcher.group(1).toLowerCase());
			} else {
				camelCaseMatcher.appendReplacement(camelCaseOutput,
						camelCaseMatcher.group(2).toUpperCase()
								+ camelCaseMatcher.group(3).toLowerCase());
			}
		}
		camelCaseMatcher.appendTail(camelCaseOutput);
		return camelCaseOutput.toString();
	}

	public static String[] resolveProductUnit(String input) {
		String pattern = "(?i)([0-9]*\\.?[0-9]+)\\s*([a-z]+)";
		String[] result = null;
		Pattern quantityPattern = Pattern.compile(pattern);
		Matcher quantityMatcher = quantityPattern.matcher(input);
		StringBuffer productNameBuffer = new StringBuffer();
		while (quantityMatcher.find()) {
			quantityMatcher.appendReplacement(productNameBuffer, "");
			if (!productNameBuffer.toString().endsWith(".")) {
				String unit = ComputationUtil.getValidUnit(quantityMatcher
						.group(2));
				if (null != unit) {
					result = new String[3];
					result[1] = quantityMatcher.group(1);
					result[2] = unit;
				} else {
					productNameBuffer.append(quantityMatcher.group(1)).append(
							" ");
					productNameBuffer.append(quantityMatcher.group(2));
				}
			}
		}
		quantityMatcher.appendTail(productNameBuffer);
		if (null != result) {
			result[0] = cleanUpProductName(productNameBuffer);
		}
		return result;
	}

	private static String cleanUpProductName(StringBuffer input) {
		StringBuffer buffer = new StringBuffer(input.reverse().toString()
				.replaceFirst("^[\\s-]+", ""));
		return getCamelCaseString(buffer.reverse().toString());
	}

	public static String[] resolveQuantity(String input) {
		String pattern = "(?i)([0-9]*\\.?[0-9]+)\\s*([a-z]+)";
		String[] result = null;
		Pattern quantityPattern = Pattern.compile(pattern);
		Matcher quantityMatcher = quantityPattern.matcher(input);
		while (quantityMatcher.find()) {
			StringBuffer nonMatcher = new StringBuffer();
			quantityMatcher.appendReplacement(nonMatcher, "");
			if (!nonMatcher.toString().endsWith(".")) {
				String unit = ComputationUtil.getValidUnit(quantityMatcher
						.group(2));
				if (null != unit) {
					result = new String[2];
					result[0] = quantityMatcher.group(1);
					result[1] = unit;
				}
			}
		}
		return result;
	}

}
