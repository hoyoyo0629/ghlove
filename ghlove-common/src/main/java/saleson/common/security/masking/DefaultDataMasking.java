package saleson.common.security.masking;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DefaultDataMasking implements DataMasking {

	@Override
	public String mask(String str, Masking masking) {
		if (str == null) return str;


		String result = str;
		Matcher matcher = null;
		StringBuilder resultStr = new StringBuilder();



		switch (masking) {
			case BIRTHDAY:
				if (str.length() == 8 || str.length() == 10) {

					result = resultStr
							.append(str.substring(0, 2))
							.append("**")
							.append(str.substring(4))
							.toString();
				}

				break;
			case NAME:
				String pattern = "";
				if(str.length() == 2) {
					pattern = "^(.)(.+)$";
				} else {
					pattern = "^(.)(.+)(.)$";
				}
				matcher = Pattern.compile(pattern).matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');

							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
					}
				}
				break;
			case BANK_ACCOUNT:
				if(str.split("-").length > 2){
					char[] acc = new char[str.split("-")[1].length()];
					Arrays.fill(acc, '*');
					resultStr.append(str.split("-")[0]);
					resultStr.append("-");
					resultStr.append(String.valueOf(acc));
					resultStr.append("-");
					resultStr.append(str.split("-")[2]);
					result = resultStr.toString();
				} else {
					StringBuffer sb = new StringBuffer();
					for (var i = 0; i < str.length(); i++) {
						if (((i + 1) / 3) % 2 == 1) {
							sb.append("*");
						} else {
							sb.append(str.charAt(i));
						}
					}

					result = sb.toString();
				}
				break;
			case PHONE_NUMBER:
			case TEL_NUMBER:
				matcher = Pattern.compile("^(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$").matcher(str);
				if(matcher.matches()) {
					result = "";
					boolean isHyphen = false;
					if(str.indexOf("-") > -1) {
						isHyphen = true;
					}
					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');
							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
						if(isHyphen && i < matcher.groupCount()) {
							result = result + "-";
						}
					}
				}
				break;
			case ADDRESS:
				String[] ary = result.split(" ");
				StringBuffer resultBf = new StringBuffer();
				char[] adc = null;
				for(int i = 0; i < ary.length; i ++){
					if(i == (ary.length-1)){
						adc = new char[ary[i].split("").length-1];
						Arrays.fill(adc, '*');
						resultBf.append(adc);
					} else {
						resultBf.append(ary[i].toString()+" ");
					}
				}
				result = resultBf.toString();
				break;
			case ADDRESS_DETAIL:
				String aryDetail[] =  result.split(" ");
				StringBuffer bufferDetail = new StringBuffer();
				char[] addc = null;
				for(int i = 0; i < aryDetail.length; i ++){
					addc = new char[aryDetail[i].split("").length-1];
					Arrays.fill(addc, '*');
					bufferDetail.append(String.valueOf(addc)+" ");
				}
				result = bufferDetail.toString();
				break;

			case ZIPCODE:
				if(result.split("-").length > 1){
					char[] zc = new char[result.split("-")[1].length()];
					Arrays.fill(zc, '*');
					resultStr.append(result.split("-")[0]);
					resultStr.append("-");
					resultStr.append(String.valueOf(zc));
				}
				result = resultStr.toString();
				break;
			case NEW_ZIPCODE:
				char[] zc = new char[result.length()];
				Arrays.fill(zc, '*');
				resultStr.append(String.valueOf(zc));
				result = resultStr.toString();
				break;

			case EMAIL:
				matcher = Pattern.compile("^(..)(.*)([@]{1})(.*)$").matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');

							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
					}
				}
				break;
			case IP:
				matcher = Pattern.compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$").matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 3) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');

							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
						if(i < matcher.groupCount()) {
							result =result + ".";
						}
					}
				}
				break;
			case GH_ADDRESS:
				String[] ary2 = result.split(" ");
				StringBuffer resultBf2 = new StringBuffer();
				char[] adc2 = null;
				for(int i = 0; i < ary2.length; i ++){
					if(i < 2){
						resultBf2.append(ary2[i].toString()+" ");
					} else {
						adc2 = new char[ary2[i].split("").length-1];
						Arrays.fill(adc2, '*');
						resultBf2.append(adc2);
					}
				}
				result = resultBf2.toString();
				break;
			case GH_PHONE_NUMBER:
				matcher = Pattern.compile("^(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$").matcher(str);
				if(matcher.matches()) {
					result = "";
					boolean isHyphen = false;
					if(str.indexOf("-") > -1) {
						isHyphen = true;
					}
					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i > 1) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');
							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
						if(isHyphen && i < matcher.groupCount()) {
							result = result + "-";
						}
					}
				}
				break;
			case GH_EMAIL:
				matcher = Pattern.compile("^(.)(.*)([@]{1})(.*)$").matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							if(replaceTarget.length() < 2) {
								char[] c = new char[result.length() + replaceTarget.length()];
								Arrays.fill(c, '*');
								result = String.valueOf(c);
							} else {
								char[] c = replaceTarget.toCharArray();
								Arrays.fill(c, 1, replaceTarget.length(), '*');
								result = result + String.valueOf(c);
							}
						} else {
							result = result + replaceTarget;
						}
					}
				}
				break;
			default :
		}

		return result;
	}
}
