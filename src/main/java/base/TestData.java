package base;

import java.io.File;

public class TestData {
	public static String systemUsername = "sumeeth"; // veltris // LikhithaR // sumeeth
	public static String testEnvironment = "Soft Release Second Prod"; // Dev/QA/Soft Release Prod/Soft Release Second Prod
													                   // Prod/Second Prod
	public static boolean isAppLogInRequired = true;
	public static boolean useOfficeOtdr = true;
	public static boolean useExternalCamera = false;

	public static int numberOfFibersToTest = 12; // 12

	public static String expectedAppVersion() {
		switch (testEnvironment) {
		case "Dev":
			return "Version: 7.8.5.0";

		case "QA":
			return "Version: 7.9.3.0";

		case "Soft Release Prod":
		case "Soft Release Second Prod":
			return "Version: 7.9.1.0";

		case "Prod":
		case "Second Prod":
			return "Version: 7.9.2.1";

		default:
			return null;
		}
	}

	public static String jobSearchOrg = "CAB";
	public static String jobSearchLocation = "Hillside";
	public static String jobSearchOperator = "QE1";

	public static String OCR_Report_Path = "C:\\Users\\" + systemUsername + "\\AppData\\Local\\Packages\\"
			+ packageName(testEnvironment) + "\\LocalState\\Downloads\\" + jobSearchOrg + "";
	// Deleting this SecureStorage folder will have app in logged out state
	public static String secureStorageFolderPath = "C:\\Users\\" + systemUsername + "\\AppData\\Local\\Packages\\"
			+ packageName(testEnvironment) + "\\LocalState\\SecureStorage";
	public static String powerSyncFolderPath = "C:\\Users\\" + systemUsername + "\\AppData\\Local\\Packages\\"
			+ packageName(testEnvironment) + "\\LocalState\\PowerSync";
	public static String openVpnAppPath = "C:\\Program Files\\OpenVPN Connect\\OpenVPNConnect.exe";
	public static String SOR_Files_Path = new File("src/test/Resources/SOR_Files_Downloaded_From_Automation")
			.getAbsolutePath();
	public static String winAppDriverPath = new File(
			"C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe").getAbsolutePath();
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

	public static final String importDataModuleName = "Import Data";
	public static String importOrg = "CAB";
	public static String importCutNumber = "11";
	public static String importItemOrgCode = "SPC";
	public static String importcutNumberInfo = "ZTEST01";
	public static String importType = "Fiber";

	public static String prysmianImportModuleName = "Prysmian";
	public static String prysmianExpectedIncompleteTests = "56";
	public static String prysmianExpectedPassedTests = "103";
	public static String prysmianExpectedFailedTests = "2";
	public static String prysmianExpectedHelixFactor = "1.0233";
	public static String prysmianExpectedOtdrLength = "7879 m";
	public static String prysmianJobNumberStartsWith = "90043053";
	public static String prysmianJobExpectedItemNumber = "DNA-31683-01";

	public static String swindonImportModuleName = "Swindon";
	public static String swindonExpectedIncompleteTests = "159";
	public static String swindonExpectedPassedTests = "295";
	public static String swindonExpectedFailedTests = "1";
	public static String swindonExpectedHelixFactor = "1.02";
	public static String swindonExpectedOtdrLength = "6353 m";
	public static String swindonJobNumberStartsWith = "250592";
	public static String swindonJobExpectedItemNumber = "DNL-6169-01";

	public static String taihanImportModuleName = "Taihan";
	public static String taihanExpectedIncompleteTests = "51";
	public static String taihanExpectedPassedTests = "96";
	public static String taihanExpectedFailedTests = "2";
	public static String taihanExpectedHelixFactor = "1.02";
	public static String taihanExpectedOtdrLength = "6181 m";
	public static String taihanJobNumberStartsWith = "7887";
	public static String taihanJobExpectedItemNumber = "DNL-6536-01";

	public static final String fiberTestModuleName = "Fiber Test";
	public static String fiberTestJobSearchJobNumber = "25305754"; // 25305754
	public static String fiberTestJobSearchCutNumber = getCurrentDateTimeStamp();
	public static String fiberTestJobSearchCutNumberInfo = "ZTEST01";
	public static String fiberTestExpectedIncompleteTestsCount = "0";
	public static String fiberTestExpectedPassedTestsCount = "64";
	public static String fiberTestExpectedFailedTestsCount = "27";
	public static String fiberTestEditAdjLengthValue = "9836";
	public static String fiberTestExpectedItemNumber = "DNA-32673-02";
	public static String fiberTestCompletionTabIseSeqValue = "9886";
	public static String fiberTestReelItem = "REL00235";
	public static String fiberTestExpectedIseReelLabel = "15791";
	public static String fiberTestExpectedOseReelLabel = "15791";
	public static String fiberTestExpectedReelLabel = "YES";
	public static String fiberTestExpectedReelId = "15791";

	public static String fiberTestJobSearchJobNumberForReelIdAndSalesOrderVerification = "48944910";
	public static String fiberTestExpectedSalesOrderForReelIdAndSalesOrderVerification = "2797719-30";
	public static String incompleteTestCountForReelIdAndSalesOrderVerification = "299";
	public static String passedTestCountForReelIdAndSalesOrderVerification = "4";
	public static String failedTestCountForReelIdAndSalesOrderVerification = "0";
	public static String fiberTestExpectedItemNumberForReelIdAndSalesOrderVerification = "DNO-12759";
	public static String fiberTestReelItemForReelIdAndSalesOrderVerification = "REL00291";

	public static final String PK_FiberTestModuleName = "PK Fiber Test";
	public static String PK_FiberTestJobSearchJobNumber = "15811448";
	public static String PK_FiberTestJobSearchCutNumber = getCurrentDateTimeStamp();
	public static String PK_FiberTestJobSearchCutNumberInfo = "ZTEST01";
	public static String PK_FiberTestExpectedIncompleteTestsCount = "889";
	public static String PK_FiberTestExpectedPassedTestsCount = "13";
	public static String PK_FiberTestExpectedFailedTestsCount = "4";
//	public static String PK_FiberTestAdjLengthValue = "9836";
//	public static String PK_FiberTestCompletionTabIseSeqValue = "9886";
	public static String PK_FiberTestExpectedItemNumber = "DNL-5356-01";
	public static String PK_FiberTestCutLength = "10015";

	public static final String wtcTestModuleName = "WTC Test";
	public static String wtcTestJobSearchJobNumber = "51022495-3028378";
	public static String wtcTestJobSearchCutNumber = getCurrentDateTimeStamp();
	public static String wtcTestJobSearchCutNumberInfo = "ZTEST01";
	public static String wtcTestBufferTube = "10-BLUE";
	public static String wtcTestExpectedIncompleteTestsCount = "883";
	public static String wtcTestExpectedPassedTestsCount = "20";
	public static String wtcTestExpectedFailedTestsCount = "6";
	public static String wtcTestCompletionTabIseSeqValue = "9886";
	public static String wtcTestExpectedItemNumber = "PR02256-01";
	public static String wtcTestEditAdjLengthValue = "2634";

	public static final String copyResultsModuleName = "Copy Results";
	public static String copyJobModule = fiberTestModuleName;
	public static String copyJobOrg = "CAB";
	public static String copyJobSourceJobNumber = "51297060";
	public static String copyJobSourceCutNumber = "00";
	public static String copyJobSourceCutNumberInfo = "";
	public static String copyJobDestinationJobNumber = "51297060";
	public static String copyJobDestinationCutNumber = getCurrentDateTimeStamp();
	public static String copyJobDestinationCutNumberInfo = "ZTEST01";
	public static String copyJobDestinationJobExpectedIncompleteTestsBeforeCompletionLayer = "6";
	public static String copyJobDestinationJobExpectedPassedTestsBeforeCompletionLayer = "305";
	public static String copyJobDestinationJobExpectedFailedTestsBeforeCompletionLayer = "0";
	public static String copyJobDestinationJobExpectedIncompleteTestsAfterCompletionLayer = "3";
	public static String copyJobDestinationJobExpectedPassedTestsAfterCompletionLayer = "308";
	public static String copyJobDestinationJobExpectedFailedTestsAfterCompletionLayer = "0";
	public static String copyJobDestinationJobExpectedHelixFactor = "1.003";
	public static String copyJobDestinationJobExpectedOtdrLength = "7030 m";
	public static String copyJobDestinationJobExpectedItemNumber = "DNS-5869";

	public static final String downTimeModuleName = "Down Time";
	public static String expectedStartDateTime = java.time.LocalDate.now()
			.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy")) + " 11:59 PM";
	public static String expectedEndDateTime = java.time.LocalDate.now().plusDays(1)
			.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy")) + " 12:01 AM";
	public static String expectedTotalDownTime = "Total Down Time : 00:02 hrs";
	public static String expectedDownTimeReason = "Internet Down";
	public static String newExpectedEndDateTime = java.time.LocalDate.now().plusDays(1)
			.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy")) + " 12:00 AM";
	public static String newExpectedTotalDownTime = "Total Down Time : 00:01 hrs";
	public static String newExpectedDownTimeReason = "Connectivity issue";

	public static final String tightBufferModuleName = "Tight Buffer";
	public static String tightBufferJobNumber = ""; // Overriding the job number during Job search
	public static String expectedTightBufferJobNumberStartsWith = "TB"
			+ java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
	public static String tightBufferExpectedItemNumber = "PT04136-07";
	public static String tightBufferTestJobSearchCutNumber = getCurrentDateTimeStamp();
	public static String tightBufferTestJobSearchCutNumberInfo = "ZTEST01";
	public static String tightBufferExpectedIncompleteTestsCount = "0";
	public static String tightBufferExpectedPassedTestsCount = (testEnvironment.equals("Dev")
			|| testEnvironment.equals("QA")) ? "6" : "2";
	public static String tightBufferExpectedFailedTestsCount = (testEnvironment.equals("Dev")
			|| testEnvironment.equals("QA")) ? "1" : "5";

	public static final String QE_LabsModuleName = "QE Labs";

	public static int expectedIncompleteOrFailedTestsOnHoldForApprovalPopupInAnomalyVerificationFlow = 13;
	public static int numberOfFibersToTestForAnomalyVerification = (testEnvironment.equals("Dev")
			|| testEnvironment.equals("QA")) ? 2 : 6;
	public static int expectedAnomalyFibersCount = 2;
	public static String expectedAnomalyStatus = (testEnvironment.equals("Dev") || testEnvironment.equals("QA"))
			? "More Likely"
			: "Less Likely";

	public static String ecqtsAppUsername() {

		switch (testEnvironment) {

		case "Dev":
			if (systemUsername.equals("LikhithaR")) {
				return "TesterDev";
			}
			return "TesterDev01"; // sumeetDev Mandeep executiveDev

		case "Dev executive":
			if (systemUsername.equals("LikhithaR")) {
				return "likhithaExecutive";
			}
			return "executiveDev";

		case "QA":
			if (systemUsername.equals("LikhithaR")) {
				return "TesterQA";
			}
			return "testQA01"; // globalAdmin SumeethQA teamLead QA testQA01

		case "QA executive":
			if (systemUsername.equals("LikhithaR")) {
				return "likhithaExecutive";
			}
			return "executiveQA";

		case "Soft Release Prod":
		case "Soft Release Second Prod":
		case "Prod":
		case "Second Prod":
			if (systemUsername.equals("LikhithaR")) {
				return "LikhithaTester";
			}
			return "testerProd";

		case "Soft Release Prod executive":
		case "Soft Release Second Prod executive":
		case "Prod executive":
		case "Second Prod executive":
			if (systemUsername.equals("LikhithaR")) {
				return "likhithaExecutive";
			}
			return "executiveProd";

		default:
			return null;
		}
	}

	public static String ecqtsAppPassword() {

		switch (testEnvironment) {

		case "Dev":
			return "Welcome@123"; // Password123#

		case "Dev executive":
			return "Welcome@123";

		case "QA":
			return "Welcome@123";

		case "QA executive":
			return "Welcome@123";

		case "Soft Release Prod":
		case "Soft Release Second Prod":
		case "Prod":
		case "Second Prod":
			return "Welcome@0650";

		case "Soft Release Prod executive":
		case "Soft Release Second Prod executive":
		case "Prod executive":
		case "Second Prod executive":
			return "Welcome@0650";

		default:
			return null;
		}
	}

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
		} else if (module.equals(tightBufferModuleName)) {
			profileName = connectionProfileName_Simulator;
		}
		return profileName;
	}

	public static String OTDR_Settings_LaunchLength(String module) {
		String launchLength = "";
		if (module.equals(fiberTestModuleName)) {
			launchLength = useOfficeOtdr ? "55" : "1000";
		} else if (module.equals(wtcTestModuleName) || module.equals(tightBufferModuleName)) {
			launchLength = "1000";
		}
		return launchLength;
	}

	public static String OTDR_Settings_CutLength(String module) {
		String cutLength = "";
		if (module.equals(fiberTestModuleName)) {
			cutLength = useOfficeOtdr ? "10000" : "1000";
		} else if (module.equals(wtcTestModuleName) || module.equals(tightBufferModuleName)) {
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
