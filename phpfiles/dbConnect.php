<?php
$dbhost = getenv("MYSQL_SERVICE_HOST");  //IMPORTANT: the env MYSQL_SERVICE_HOST/PORT assumes your database service name is "MYSQL"
$dbport = getenv("MYSQL_SERVICE_PORT");  //If your database service name is "FOO", then this would be "FOO_SERVICE_HOST" and "FOO_SERVICE_PORT"
$dbuser = getenv("MYSQL_USER");
$dbpwd = getenv("MYSQL_PASSWORD");
$dbname = getenv("MYSQL_DATABASE");

/*$servername = "mysql-2-x9rcs";
$username = "user76A";
$password = "Qs8kH7wexoV1p0Cy";
$database = "androiddb";*/
 
 
//creating a new connection object using mysqli 
$con = new mysqli($dbhost, $dbuser, $dbpwd, $dbname);
 
//if there is some error connecting to the database
//with die we will stop the further execution by displaying a message causing the error 
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}
?>