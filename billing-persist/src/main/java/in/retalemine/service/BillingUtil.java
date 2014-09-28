package in.retalemine.service;

public class BillingUtil {

	public static String fieldSelectionJSON(String[] fields) {
		StringBuffer result = new StringBuffer("{");
		for (String field : fields) {
			result.append("'").append(field).append("'").append(":1")
					.append(",");
		}
		int lastCommaIndex = result.lastIndexOf(",");
		result.replace(lastCommaIndex, lastCommaIndex, "}");
		return result.toString();
	}

}
