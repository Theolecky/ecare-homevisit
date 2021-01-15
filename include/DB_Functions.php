<?php



class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
    /**
     * Storing new home visit data
     */
    public function storeNewClient($pillcount, $next_tca, $matching_tca, $have_adr, $yes_adr_specify,
  $partner_tested, $selfkit_issued, $selfkit_results, $stressful, $created_by) {

        $stmt = $this->conn->prepare("INSERT INTO tb_newclient(pillcount, number_of_next_tca_days, tca_days_match, have_adr, specify_adr, partner_tested, issued_selfkit, selfkit_results, have_stressful_situation, created_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssssssss",$pillcount, $next_tca, $matching_tca, $have_adr, $yes_adr_specify, $partner_tested, $selfkit_issued, $selfkit_results, $stressful, $created_by);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            // $stmt = $this->conn->prepare("SELECT * FROM newclient WHERE id = ?");
            // $stmt->bind_param("s", $next_tca);
            // $stmt->execute();
            // $data = $stmt->get_result()->fetch_assoc();
            // $stmt->close();
            //
            // return $data;
            echo "Data saved successfully";
        } else {
            return false;
        }
    }

    public function storeHomevisit($date, $serviceno, $facility, $newclient, $defaulter, $tbclient,
    $pmtctclient, $heiclient, $viralclient,
    $pillcount, $nexttca, $matching_tca, $have_adr, $yes_adr_specify,
  $partner_tested, $selfkit_issued, $selfkit_results, $stressful,
  $clientfound, $reasonfordefault, $defaulteraction,$tracingoutcome, $shiftedto, $selftransferredto,
  $pcr, $pcrreason, $antibody,$antibodyreason, $prophylaxis, $prophylaxisreason, $heiaction, $viralpartnertested,
  $viralselftestkit, $lastvl, $drugstime,$nodrugsreason, $clientmdt, $reasonnomdt, $createdby) {

        $stmt = $this->conn->prepare("INSERT INTO tb_homevisit(Date,ServiceNumber,Facility,newclient,
          defaulter_tracing,tb_client,pmtct_client,hei_client,high_viral_load_client,
          pillcount, number_of_next_tca_days, tca_days_match, have_adr, specify_adr,
          new_client_partner_tested, issued_selfkit, selfkit_results, have_stressful_situation,
          defaulter_found, reason_for_defaulting, defaulter_action_taken_taken,tracing_outcome,
          shifted_to, client_self_transferred_to,pcr, reason_no_pcr, antibody_test, reason_no_antibody,
          taking_prophylaxis,reason_no_prophylaxis,
          hei_action_taken_taken,high_viral_load_partner_tested,self_test_kit,last_VL,rigt_drug_time,
          reason_for_not_right_time,
          client_on_MDT,reason_for_not_MDT, CreatedBy) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?)");
        //print_r($this->conn->error_list);
        $stmt->bind_param("sssssssssssssssssssssssssssssssssssssss",$date, $serviceno, $facility,
        $newclient, $defaulter, $tbclient,
        $pmtctclient, $heiclient, $viralclient,
        $pillcount, $nexttca, $matching_tca, $have_adr, $yes_adr_specify,
      $partner_tested, $selfkit_issued, $selfkit_results, $stressful,
      $clientfound, $reasonfordefault, $defaulteraction,$tracingoutcome, $shiftedto,
      $selftransferredto,
      $pcr, $pcrreason, $antibody,$antibodyreason, $prophylaxis, $prophylaxisreason, $heiaction,
      $viralpartnertested,
      $viralselftestkit, $lastvl, $drugstime,$nodrugsreason, $clientmdt, $reasonnomdt, $createdby);


        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            // $stmt = $this->conn->prepare("SELECT * FROM newclient WHERE id = ?");
            // $stmt->bind_param("s", $next_tca);
            // $stmt->execute();
            // $data = $stmt->get_result()->fetch_assoc();
            // $stmt->close();
            //
            // return $data;
            echo "Data saved successfully";
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
