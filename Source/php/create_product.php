
<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();

// check for required fields
if(isset($_POST['net_name'])) {
 
		$net_name = $_POST['net_name'];
		$net_type = $_POST['net_type'];
		$sig_strength = $_POST['sig_strength'];
		$sig_percent = $_POST['sig_percent'];
		$latitude = $_POST['latitude'];
		$longitude = $_POST['longitude'];
		$marker_color = $_POST['marker_color'];
	 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO sigmap_cellular(net_name,net_type,sig_strength,sig_percent,latitude,longitude,marker_color) VALUES('$net_name', '$net_type', '$sig_strength', '$sig_percent', '$latitude', '$longitude', '$marker_color')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>