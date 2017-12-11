<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $username = $_POST['username'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    require_once('dbConnect.php');

	$sql = "SELECT * FROM users WHERE username = '$username'";
	if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result)> 0) {
			echo "Username not available";
		}
		else {
			$sql2 = "INSERT INTO users (username,password,email) VALUES ('$username','$password','$email')";
			if(mysqli_query($con,$sql2)){
				echo "Successfully Registered";
			}else{
				echo "Could not register";
			}
		}
	}
	
}else{
echo 'error';
}
?>