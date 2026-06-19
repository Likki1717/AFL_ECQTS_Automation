package base;

import java.io.File;

public class TestData {
	public static String systemUsername = "sumeeth"; // veltris
	public static String testEnvironment = "Dev"; // Dev /QA/Soft Release Prod/Soft Release Second Prod/Prod/Second Prod
	public static String expectedAppVersion = "Version: 7.6.21.0";
	public static boolean useOfficeOtdr = true;
	public static boolean useExternalCamera = true;
	public static int numberOfFibersToTest = 1;

	public static String vpnAppPassword() {
		if (systemUsername.equals("sumeeth")) {
			return "kumar@123";
		} else if (systemUsername.equals("veltris")) {
			return "manohar@123";
		} else // Update below if other than Sumeeth and Mandeep is using automation script
		{
			return "kumar@1234";
		}
	}

	public static String fiberTestJobSearchOrg = "CAB";
	public static String wtcTestJobSearchOrg = "CAB";

	public static String OCR_Report_Path = "C:\\Users\\" + systemUsername + "\\AppData\\Local\\Packages\\"
			+ packageName(testEnvironment) + "\\LocalState\\Downloads\\" + fiberTestJobSearchOrg + "";
	// Deleting this SecureStorage folder will have app in logged out state
	public static String secureStorageFolderPath = "C:\\Users\\" + systemUsername + "\\AppData\\Local\\Packages\\"
			+ packageName(testEnvironment) + "\\LocalState\\SecureStorage";
	public static String openVpnAppPath = "C:\\Program Files\\OpenVPN Connect\\OpenVPNConnect.exe";
	public static String SOR_Files_Path = new File("src/test/Resources/SOR_Files_Downloaded_From_Automation")
			.getAbsolutePath();
	public static String winAppDriverPath = new File("C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe")
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
	public static String prysmianJobNumberStartsWith = "90043053";

	public static String swindonExpectedIncompleteTests = "159";
	public static String swindonExpectedPassedTests = "295";
	public static String swindonExpectedFailedTests = "1";
	public static String swindonExpectedHelixFactor = "1.02";
	public static String swindonExpectedOtdrLength = "6353 m";
	public static String swindonJobNumberStartsWith = "250592";

	public static String taihanExpectedIncompleteTests = "51";
	public static String taihanExpectedPassedTests = "98";
	public static String taihanExpectedFailedTests = "0";
	public static String taihanExpectedHelixFactor = "1.02";
	public static String taihanExpectedOtdrLength = "6181 m";
	public static String taihanJobNumberStartsWith = "7887";

	public static String fiberTestModuleName = "Fiber Test";
	public static String fiberTestJobSearchJobNumber = "25305754"; // Large Job - 15811448

	public static String fiberTestJobSearchCutNumber = getCurrentDateTimeStamp();

	public static String fiberTestJobSearchCutNumberInfo = "ZTEST01";
	public static String fiberTestBufferTube = "10-BLUE";
	public static String fiberTestExpectedIncompleteTestsCount = fiberTestJobSearchJobNumber.equals("25305754") ? "66"
			: "883";
	public static String fiberTestExpectedPassedTestsCount = fiberTestJobSearchJobNumber.equals("25305754") ? "20"
			: "17";
	public static String fiberTestExpectedFailedTestsCount = fiberTestJobSearchJobNumber.equals("25305754") ? "5" : "6";

	public static String fiberTestCompletionTabIseSeqValue = "9886";

	public static String wtcTestModuleName = "WTC Test";
	public static String wtcTestJobSearchJobNumber = "51022495-3028378";
	public static String wtcTestJobSearchCutNumber = getCurrentDateTimeStamp();
	public static String wtcTestJobSearchCutNumberInfo = "ZTEST01";
	public static String wtcTestBufferTube = "10-BLUE";
	public static String wtcTestExpectedIncompleteTestsCount = "883";
	public static String wtcTestExpectedPassedTestsCount = "20";
	public static String wtcTestExpectedFailedTestsCount = "6";
	public static String wtcTestCompletionTabIseSeqValue = "9886";

	public static String ecqtsAppUsername() {
		String username = null;
		switch (testEnvironment) {
		case "QA":
			username = "testQA01"; // globalAdmin SumeethQA teamLead QA testQA01
			break;
		case "Dev":
			username = "testerProd"; // sumeetDev Mandeep executiveDev
			break;
		case "Prod":
			username = "testerProd";
			break;
		case "Second Prod":
			username = "testerProd";
			break;
		case "Soft Release Prod":
			username = "testerProd";
			break;
		case "Soft Release Second Prod":
			username = "testerProd";
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
			password = "Welcome@0650";
			break;
		case "Second Prod":
			password = "Welcome@0650";
			break;
		case "Soft Release Prod":
			password = "Welcome@0650";
			break;
		case "Soft Release Second Prod":
			password = "Welcome@0650";
			break;
		}
		return password;
	}

	public static String prodWebUrl = "https://www.ecqts.aflglobal.com";

	public static String connectionProfileName_Simulator = "Simulator";
	public static String connectionProfileName_JGR_One = "JGR-1";
	public static String connectionProfileName_JGR_Two = "JGR-2";
	public static String connectionProfileName_JGR_Three = "JGR-3";
	public static String connectionProfileName_JGR_Four = "JGR-4";
	public static String connectionProfileName_JGR_Five = "JGR-5";
	public static String connectionProfileName_Office_OTDR = "Office OTDR";
	public static String connectionProfile_Simulator_IP_Address = "54.219.138.1";
	public static String connectionProfile_Simulator_Port = "65431";
	public static String connectionProfile_Office_OTDR_IP_Address = "10.9.10.200";
	public static String connectionProfile_Office_OTDR_Port = "2288";
	public static String connectionProfile_Anritsu_9085_IP_Address = "192.168.10.10";

	public static String OTDR_Settings_ConnectionProfile_Name(String module) {
		String profileName = "";
		if (module.equals(fiberTestModuleName)) {
			profileName = useOfficeOtdr ? connectionProfileName_Office_OTDR : connectionProfileName_Simulator;
		} else if (module.equals(wtcTestModuleName)) {
			profileName = connectionProfileName_JGR_One;
		}
		return profileName;
	}

	public static String OTDR_Settings_LaunchLength(String module) {
		String launchLength = "";
		if (module.equals(fiberTestModuleName)) {
			launchLength = useOfficeOtdr ? "55" : "1000";
		} else if (module.equals(wtcTestModuleName)) {
			launchLength = "1000";
		}
		return launchLength;
	}

	public static String OTDR_Settings_CutLength(String module) {
		String cutLength = "";
		if (module.equals(fiberTestModuleName)) {
			cutLength = useOfficeOtdr ? "10000" : "1000";
		} else if (module.equals(wtcTestModuleName)) {
			cutLength = "1000";
		}
		return cutLength;
	}

	public static String OTDR_Settings_LaunchLength2 = "1000";
	public static String OTDR_Settings_manufacturedLength = "1000";
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

	public static String packageName(String testEnvironment) {
		switch (testEnvironment) {
		case "Soft Release Prod":
			return "com.ecsite.afl.softrelease_aqd9xyv20zq6r";

		default:
			return appId().split("!")[0];
		}
	}

	public static String getCurrentDateTimeStamp() {
		return new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
	}

	/*
	 * To get appId, use below command in windows powershell : Get-StartApps |
	 * Where-Object {$_.Name -like "*ECQTS*"} Name AppID ---- ----- ECQTS Dev
	 * com.ecsite.afl.dev_aqd9xyv20zq6r!App ECQTS QA
	 * com.ecsite.afl.qa_aqd9xyv20zq6r!App ECQTS Soft Release
	 * com.ecsite.afl.softreleaseprod_aqd9xyv20zq6r!App ECQTS Soft Release Second
	 * Prod com.ecsite.afl.softreleasesecondprod_aqd9xyv20zq6r!App ECQTS Prod
	 * com.ecsite.afl.prod_aqd9xyv20zq6r!App ECQTS Second Prod
	 * com.ecsite.afl.secondprod_aqd9xyv20zq6r!App
	 */
}
