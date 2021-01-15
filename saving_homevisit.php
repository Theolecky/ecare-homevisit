<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
// $_POST['date']="2021-01-05";
// $_POST['serviceno']="10501217283";
// $_POST['facility']="Kayole";
// $_POST['newclient']="1";
// $_POST['defaulter']="0";
// $_POST['tbclient']="1";
// $_POST['pmtctclient']="0";
// $_POST['heiclient']="0";
// $_POST['viralclient']="0";
// $_POST['pillcount']="90";
// $_POST['nexttca']="30";
// $_POST['matching_tca']="1";
// $_POST['have_adr']="1";
// $_POST['yes_adr_specify']="ojjklsfjjkldfsajowieiir bndsjiao";
// $_POST['partner_tested']="0";
// $_POST['selfkit_issued']="1";
// $_POST['selfkit_results']="1";
// $_POST['stressful']="0";
// $_POST['clientfound']="";
// $_POST['reasonfordefault']="";
// $_POST['defaulteraction']="";
// $_POST['tracingoutcome']="";
// $_POST['shiftedto']="";
// $_POST['selftransferredto']="";
// $_POST['pcr']="";
// $_POST['pcrreason']="";
// $_POST['antibody']="";
// $_POST['antibodyreason']="";
// $_POST['prophylaxis']="";
// $_POST['prophylaxisreason']="";
// $_POST['heiaction']="";
// $_POST['viralpartnertested']="";
// $_POST['viralselftestkit']="";
// $_POST['lastvl']="";
// $_POST['drugstime']="";
// $_POST['nodrugsreason']="";
// $_POST['clientmdt']="";
// $_POST['reasonnomdt']="";
// $_POST['created_by']="vicky@hotmail.com";


if (isset($_POST['date']) && isset($_POST['serviceno'])) {

    // receiving the post params
    $date = $_POST['date'];
    $serviceno = $_POST['serviceno'];
    $facility = $_POST['facility'];
    $newclient = $_POST['newclient'];
    $defaulter = $_POST['defaulter'];
    $tbclient = $_POST['tbclient'];
    $pmtctclient = $_POST['pmtctclient'];
    $heiclient = $_POST['heiclient'];
    $viralclient = $_POST['viralclient'];

    $pillcount = $_POST['pillcount'];
    $nexttca = $_POST['nexttca'];
    $matching_tca = $_POST['matching_tca'];
    $have_adr = $_POST['have_adr'];
    $yes_adr_specify = $_POST['yes_adr_specify'];
    $partner_tested = $_POST['partner_tested'];
    $selfkit_issued = $_POST['selfkit_issued'];
    $selfkit_results = $_POST['selfkit_results'];
    $stressful = $_POST['stressful'];

    $clientfound = $_POST['clientfound'];
    $reasonfordefault = $_POST['reasonfordefault'];
    $defaulteraction = $_POST['defaulteraction'];
    $tracingoutcome = $_POST['tracingoutcome'];
    $shiftedto = $_POST['shiftedto'];
    $selftransferredto = $_POST['selftransferredto'];
    $pcr = $_POST['pcr'];
    $pcrreason = $_POST['pcrreason'];
    $antibody = $_POST['antibody'];
    $antibodyreason = $_POST['antibodyreason'];
    $prophylaxis = $_POST['prophylaxis'];

    $prophylaxisreason = $_POST['prophylaxisreason'];
    $heiaction = $_POST['heiaction'];
    $viralpartnertested = $_POST['viralpartnertested'];
    $viralselftestkit = $_POST['viralselftestkit'];
    $lastvl = $_POST['lastvl'];
    $drugstime = $_POST['drugstime'];
    $nodrugsreason = $_POST['nodrugsreason'];
    $clientmdt = $_POST['clientmdt'];
    $reasonnomdt = $_POST['reasonnomdt'];
    $createdby = $_POST['created_by'];

    // check if user is already existed with the same email

        // create a new client data
        $newhomevisit = $db->storeHomevisit($date,$serviceno,$facility,$newclient,$defaulter,
        $tbclient,$pmtctclient,$heiclient,$viralclient,$pillcount, $nexttca, $matching_tca, $have_adr,
        $yes_adr_specify,
      $partner_tested, $selfkit_issued, $selfkit_results, $stressful,$clientfound,
      $reasonfordefault,$defaulteraction,$tracingoutcome,$shiftedto,$selftransferredto,$pcr,
      $pcrreason,$antibody,$antibodyreason,$prophylaxis,$prophylaxisreason,$heiaction,$viralpartnertested,
      $viralselftestkit,$lastvl,$drugstime,$nodrugsreason,$clientmdt,$reasonnomdt, $createdby);
        if ($newhomevisit) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["facility"] = $newhomevisit["facility"];
            $response["newhomevist"]["newclient"] = $newhomevisit["newclient"];
            $response["newhomevist"]["tbclient"] = $newhomevisit["tbclient"];
            $response["newhomevist"]["defaulter"] = $newhomevisit["defaulter"];
            $response["newhomevist"]["pmtctclient"] = $newhomevisit["pmtctclient"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred while saving!";
            echo json_encode($response);
        }
    }
?>
