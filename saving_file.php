<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
$_POST['pillcount']="100";
$_POST['next_tca']="50";
$_POST['matching_tca']="1";
$_POST['have_adr']="1";
$_POST['yes_adr_specify']="aiajshdfklh sdlaaaaaaaaaaaa lafsdhhhhhhhh";
$_POST['partner_tested']="0";
$_POST['selfkit_issued']="";
$_POST['selfkit_results']="";
$_POST['stressful']="1";
$_POST['created_by']="vicky@hotmail.com";


if (isset($_POST['pillcount']) && isset($_POST['next_tca']) &&
isset($_POST['matching_tca']) && isset($_POST['have_adr']) && isset($_POST['yes_adr_specify']) &&
isset($_POST['partner_tested']) && isset($_POST['selfkit_issued']) && isset($_POST['selfkit_results']) &&
isset($_POST['stressful']) && isset($_POST['created_by'])) {

    // receiving the post params
    $pillcount = $_POST['pillcount'];
    $next_tca = $_POST['next_tca'];
    $matching_tca = $_POST['matching_tca'];
    $have_adr = $_POST['have_adr'];
    $yes_adr_specify = $_POST['yes_adr_specify'];
    $partner_tested = $_POST['partner_tested'];
    $selfkit_issued = $_POST['selfkit_issued'];
    $selfkit_results = $_POST['selfkit_results'];
    $stressful = $_POST['stressful'];
    $created_by = $_POST['created_by'];

    // check if user is already existed with the same email

        // create a new client data
        $newclient = $db->storeNewClient($pillcount, $next_tca, $matching_tca, $have_adr, $yes_adr_specify,
      $partner_tested, $selfkit_issued, $selfkit_results, $stressful, $created_by);
        if ($newclient) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["id"] = $newclient["id"];
            $response["newclient"]["pillcount"] = $newclient["pillcount"];
            $response["newclient"]["next_tca"] = $newclient["next_tca"];
            $response["newclient"]["have_adr"] = $newclient["have_adr"];
            $response["newclient"]["yes_adr_div"] = $newclient["yes_adr_div"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred while saving!";
            echo json_encode($response);
        }
    }
?>
