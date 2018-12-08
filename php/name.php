<?php

  $connect = mysql_connect("localhost","ppmj789","whdeo1027") or die("error");

  $dbname = "ppmj789";

  $dbconn = mysql_select_db($dbname,$connect);

  $ID = $_POST['ID'];

//  $query_search = "select * from UserDB";
  $query_search = "SELECT Name FROM UserDB WHERE UserID ='".$ID."'";

 mysql_query("set names utf8");
  $result = mysql_query($query_search) or die(mysql_error());



    if(!$result) {

       die('Could not query:' . mysql_error());

    }

  echo (mysql_result($result, 0));
  header('Content-Type: text/html; charset=utf-8');
  header('Content-Type: application/json; charset=utf8');

 mysql_close($connect);


?>





d
