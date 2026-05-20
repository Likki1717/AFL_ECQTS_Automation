package base;

import java.io.File;

public class TestData {
	public static String testEnvironment = "QA"; // Dev /QA/Soft Release Prod/Soft Release Second Prod/Prod/Second Prod
	public static String expectedAppVersion = "Version: 7.6.11.0";
	public static boolean useOfficeOtdr = true;
	public static boolean useExternalCamera = true;
	public static int numberOfFibersToTest = 2;
	public static int delayInSecondsBeforeClickingOnOkButtonOnRunTestGraphs = 0;
	public static String vpnAppPassword = "kumar@123";

	public static String fiberTestJobSearchOrg = "CAB";

	// All below local paths to be updated from system
	public static String OCR_Report_Path = "C:\\Users\\sumeeth\\AppData\\Local\\Packages\\" + appId().split("!")[0]
			+ "\\LocalState\\Downloads\\" + fiberTestJobSearchOrg + "";
	public static String secureStorageFolderPath = "C:\\Users\\sumeeth\\AppData\\Local\\Packages\\"
			+ appId().split("!")[0] + "\\LocalState\\SecureStorage"; // Deleting this SecureStorage folder will delete
																		// the login cache and app by default will be in
																		// logged out state

	public static String openVpnAppPath = "C:\\Program Files\\OpenVPN Connect\\OpenVPNConnect.exe";
	public static String SOR_Files_Path = new File("src/test/Resources/SOR_Files_Downloaded_From_Automation")
			.getAbsolutePath();
	public static String winAppDriverPath = new File("src/test/Resources/WinAppDriver/WinAppDriver.exe")
			.getAbsolutePath();
	public static String prysmianAttenuationFilePath = new File(
			"src/test/Resources/ImportFiles/PrysmianFiles/attenuation.csv").getAbsolutePath();
	public static String prysmianJacketOdFilePath = new File(
			"src/test/Resources/ImportFiles/PrysmianFiles/jacketOD.csv").getAbsolutePath();
	public static String swindonAttenuationFilePath = new File(
			"src/test/Resources/ImportFiles/SwindonFiles/attenuation.csv").getAbsolutePath();
	public static String swindonJacketOdFilePath = new File("src/test/Resources/ImportFiles/SwindonFiles/jacketOD.csv")
			.getAbsolutePath();
	public static String taihanAttenuationFilePath = new File(
			"src/test/Resources/ImportFiles/TaihanFiles/attenuation.csv").getAbsolutePath();

	public static String importOrg = "CAB";
	public static String importCutNumber = "11";
	public static String importItemOrgCode = "SPC";
	public static String importcutNumberInfo = "ZTEST01";
	public static String importType = "Fiber";
	
	public static String prysmianExpectedIncompleteTests = "56";
	public static String prysmianExpectedPassedTests = "103";
	public static String prysmianExpectedFailedTests = "2";
	public static String prysmianExpectedHelixFactor = "1.0233";
	public static String prysmianExpectedOtdrLength = "7879 m";
	public static String prysmianJobNumberStartsWith = "90043053-";

	public static String swindonExpectedIncompleteTests = "159";
	public static String swindonExpectedPassedTests = "295";
	public static String swindonExpectedFailedTests = "1";
	public static String swindonExpectedHelixFactor = "1.02";
	public static String swindonExpectedOtdrLength = "6253 m";
	public static String swindonJobNumberStartsWith = "250592-";

	public static String taihanExpectedIncompleteTests = "291";
	public static String taihanExpectedPassedTests = "576";
	public static String taihanExpectedFailedTests = "2";
	public static String taihanExpectedHelixFactor = "1.017";
	public static String taihanExpectedOtdrLength = "6205 m";
	public static String taihanJobNumberStartsWith = "455-";

	public static String fiberTestJobSearchJobNumber = "25305754"; // Large Job - 15811448
	public static String fiberTestJobSearchCutNumber = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
			.format(new java.util.Date());
	public static String fiberTestJobSearchCutNumberInfo = "ZTEST01";
	public static String fiberTestBufferTube = "10-BLUE";

	public static String fiberTestExpectedIncompleteTestsCount = "0";
	public static String fiberTestExpectedPassedTestsCount = "88";
	public static String fiberTestExpectedFailedTestsCount = "3";

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

	public static String simulatorProfileName = "Simulator";
	public static String jgrOneProfileName = "JGR-1";
	public static String jgrTwoProfileName = "JGR-2";
	public static String officeOtdrProfileName = "Office OTDR";
	public static String simulatorIP_Address = "54.219.138.1";
	public static String simulatorPort = "65431";
	public static String officeOTDR_IP_Address = "10.9.10.200";
	public static String officeOTDR_Port = "2288";
	public static String anritsu_9085_Ip_Address = "192.168.10.10";

	public static String OTDR_Settings_ConnectionProfile = useOfficeOtdr ? officeOtdrProfileName : simulatorProfileName;
	public static String OTDR_Settings_LaunchLength = useOfficeOtdr ? "55" : "1000";
	public static String OTDR_Settings_CutLength = useOfficeOtdr ? "10000" : "1000";
	public static String OTDR_Settings_Horizontal = "10";
	public static String OTDR_Settings_Vertical = "-10";

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
