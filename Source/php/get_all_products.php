<?php
 
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all products from products table
$result = mysql_query("SELECT *FROM sigmap_cellular WHERE net_name = 'Vodafone IN'") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["products"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["id"] = $row["id"];
        $product["net_name"] = $row["net_name"];
        $product["net_type"] = $row["net_type"];
	$product["sig_strength"] = $row["sig_strength"]; 
	$product["sig_percent"] = $row["sig_percent"];        
	$product["latitude"] = $row["latitude"];
	$product["longitude"] = $row["longitude"];
	$product["marker_color"] = $row["marker_color"];
	$product["comments"] = $row["comments"];
	$product["updated_at"] = $row["updated_at"];
        
 
        // push single product into final response array
        array_push($response["products"], $product);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>