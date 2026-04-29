package base;

public class TestData {
	public static String testEnvironment = "Dev"; // Dev /QA/Soft Release/Soft Release Second Prod/Prod/Second Prod
	public static String expectedAppVersion = "7.6.6.1";
	
	// All local paths to be updated from system
	public static String localStateFolderPath = "C:\\Users\\sumeeth\\AppData\\Local\\Packages\\" + appId().split("!")[0]
			+ "\\LocalState";
	public static String SOR_Files_Path = "C:\\Users\\sumeeth\\Downloads\\SOR_Files_Downloaded_From_Automation";
	public static String winAppDriverPath = "c:\\Program Files\\Windows Application Driver\\winappdriver.exe";

	public static String username = usernameBasedOnEnvironment();
	public static String password = passwordBasedOnEnvironment();

	public static String appId() {
		String appId = null;
		switch (testEnvironment) {
		case "Dev":
			appId = "com.ecsite.afl.dev_aqd9xyv20zq6r!App";
			break;
		case "QA":
			appId = "com.ecsite.afl.qa_aqd9xyv20zq6r!App";
			break;
		case "Soft Release":
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

	public static String usernameBasedOnEnvironment() {
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
		case "Soft Release":
			username = "executiveProd";
			break;
		case "Soft Release Second Prod":
			username = "executiveProd";
			break;
		}
		return username;
	}

	public static String passwordBasedOnEnvironment() {
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
		case "Soft Release":
			password = "";
			break;
		case "Soft Release Second Prod":
			password = "";
			break;
		}
		return password;
	}
}
