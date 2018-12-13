<?php

  $connect = mysqli_connect("localhost","ppmj789","whdeo1027","ppmj789") or die("error");


  mysqli_set_charset($connect,"utf8");

  $ID = $_GET['ID'];

//  WHERE DiseaseRecordDB.UserID=ppmj789
  $res = mysqli_query($connect,"SELECT DiseaseDB.DiseaseName, DiseaseRecordDB.Date FROM DiseaseDB INNER JOIN DiseaseRecordDB
    ON DiseaseRecordDB.DiseaseCode = DiseaseDB.DiseaseCode
  WHERE DiseaseRecordDB.UserID = '".$ID."'"
   );

  $result=array();
  while($row = mysqli_fetch_array($res)){

    array_push($result,
    array(
      'disease_name'=>$row[0],
      'date'=>$row[1]
    ));
  }
  $json = json_encode(array("DiseaseRecord"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
  echo $json;
mysqli_close($connect);

 header('Content-Type: text/html; charset=utf-8');
 header('Content-Type: application/json; charset=utf8');


?>
