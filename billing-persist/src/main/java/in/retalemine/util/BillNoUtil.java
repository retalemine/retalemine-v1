package in.retalemine.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class BillNoUtil {

	private static final int i = 0;
	private static final String yearMapper = "0IHGFEDCBA";
	public static final int suffixLen = 4;

	/**
	 * return bill number
	 * 
	 * @param suffixLen
	 * @return bill number
	 */
	public static String generateBillNo(int suffixLen) {
		return getBillNoPrefix() + getBillNoSuffix(suffixLen);
	}

	/**
	 * Random value that get suffixed to form the bill number
	 * 
	 * @param suffixLen
	 * @return
	 */
	private static String getBillNoSuffix(int suffixLen) {
		Random nRandom = new Random();
		Random cRandom = new Random();
		DecimalFormat format = new DecimalFormat(String.valueOf(
				(int) Math.pow(10, suffixLen)).substring(1));
		Integer billNo = nRandom.nextInt((int) Math.pow(10, suffixLen) - 1);
		// A-65 Z-90
		int code = cRandom.nextInt((90 - 65) + 1) + 65;
		return String.valueOf(format.format(billNo)) + (char) code;
	}

	/**
	 * Year represented as alpha along with day of year
	 * 
	 * @return
	 */
	private static String getBillNoPrefix() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		int year = calendar.get(Calendar.YEAR);
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		int Q = year, R;
		DecimalFormat format = new DecimalFormat("000");
		StringBuffer buffer = new StringBuffer();
		do {
			R = Q % 10;
			Q = Q / 10;
			buffer.append(yearMapper.charAt(R));
		} while (Q > 0);
		return String.valueOf(buffer.reverse() + format.format(dayOfYear));
	}

}
