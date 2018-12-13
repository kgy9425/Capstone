<?php

  $connect = mysql_connect("localhost","ppmj789","whdeo1027") or die("error");

  $dbname = "ppmj789";

  $dbconn = mysql_select_db($dbname,$connect);

  $username = $_POST['username'];
  $password = $_POST['password'];

//  $query_search = "select * from UserDB";
  $query_search = "select * from UserDB where UserID = '".$username."' AND Password = '".$password. "'";


  $query_exec = mysql_query($query_search) or die(mysql_error());

  $rows = mysql_num_rows($query_exec);

    if($rows == 0) {

        echo "No Such User Found";

    }

    else  {
      echo "User Found";
    }



?>
