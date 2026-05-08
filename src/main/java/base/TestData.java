package base;

public class TestData {
	public static String testEnvironment = "QA"; // Dev /QA/Soft Release Prod/Soft Release Second Prod/Prod/Second Prod
	public static String expectedAppVersion = "7.6.6.1";
	public static String org = "CAB";
	public static boolean useOfficeOtdr = true;
	public static String vpnAppPassword = "kumar@123";

	// All local paths to be updated from system
	public static String openVpnAppPath = "C:\\Program Files\\OpenVPN Connect\\OpenVPNConnect.exe";
	public static String SOR_Files_Path = "C:\\Users\\sumeeth\\Downloads\\SOR_Files_Downloaded_From_Automation";
	public static String winAppDriverPath = "c:\\Program Files\\Windows Application Driver\\winappdriver.exe";
	public static String OCR_Report_Path = "C:\\Users\\sumeeth\\AppData\\Local\\Packages\\"+appId().split("!")[0]+"\\LocalState\\Downloads\\"+org+"";
	public static String secureStorageFolderPath = "C:\\Users\\sumeeth\\AppData\\Local\\Packages\\"
			+ appId().split("!")[0] + "\\LocalState\\SecureStorage"; // Deleting this SecureStorage folder will delete
																		// the login cache and app by default will be in
																		// logged out state
	
	public static String ecqtsAppUsername() {
		String username = null;
		switch (testEnvironment) {
		case "QA":
			username = "testQA01"; // globalAdmin SumeethQA teamLead QA testQA01
			break;
		case "Dev":
			username = "sumeetDev"; // sumeetDev Mandeep executiveDev
			break;
		case "Prod":
			username = "executiveProd";
			break;
		case "Second Prod":
			username = "sumeethProd";
			break;
		case "Soft Release Prod":
			username = "executiveProd";
			break;
		case "Soft Release Second Prod":
			username = "executiveProd";
			break;
		}
		return username;
	}

	public static String ecqtsAppPassword() {
		String password = null;
		switch (testEnvironment) {
		case "QA":
			password = "Welcome@123";
			break;
		case "Dev":
			password = "Welcome@123"; // Password123#
			break;
		case "Prod":
			password = "";
			break;
		case "Second Prod":
			password = "";
			break;
		case "Soft Release Prod":
			password = "";
			break;
		case "Soft Release Second Prod":
			password = "";
			break;
		}
		return password;
	}
	
	public static String prodWebUrl = "https://www.ecqts.aflglobal.com";
	public static String simulatorIP_Address = "54.219.138.1";
	public static String simulatorPort = "65431";
	public static String officeOTDR_IP_Address = "10.9.10.200";
	public static String officeOTDR_Port = "2288";
	public static String anritsu_9085_Ip_Address="192.168.10.10";
	
	public static String appId() {
		String appId = null;
		switch (testEnvironment) {
		case "Dev":
			appId = "com.ecsite.afl.dev_aqd9xyv20zq6r!App";
			break;
		case "QA":
			appId = "com.ecsite.afl.qa_aqd9xyv20zq6r!App";
			break;
		case "Soft Release Prod":
			appId = "com.ecsite.afl.softreleaseprod_aqd9xyv20zq6r!App";
			break;
		case "Soft Release Second Prod":
			appId = "com.ecsite.afl.softreleasesecondprod_aqd9xyv20zq6r!App";
			break;
		case "Prod":
			appId = "com.ecsite.afl.prod_aqd9xyv20zq6r!App";
			break;
		case "Second Prod":
			appId = "com.ecsite.afl.secondprod_aqd9xyv20zq6r!App";
			break;
		}
		return appId;
	}
}
