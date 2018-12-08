<?php

  $connect = mysqli_connect("localhost","ppmj789","whdeo1027","ppmj789") or die("error");


  mysqli_set_charset($connect,"utf8");

 $Month = $_GET['Month'];

  $res = mysqli_query($connect,"SELECT DiseaseDB.DiseaseName, DiseaseDB.DiseasePrecaution FROM DiseaseDB INNER JOIN MonthlyDiseaseDB
    ON DiseaseDB.DiseaseCode = MonthlyDiseaseDB.DiseaseCode
    WHERE MonthlyDiseaseDB.Month='".$Month."'"
   );

  $result=array();
  while($row = mysqli_fetch_array($res)){

    array_push($result,
    array(
      'disease_name'=>$row[0],
      'precaution'=>$row[1]
    ));
  }
  $json = json_encode(array("Monthdisease"=>$result), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
  echo $json;
mysqli_close($connect);

 header('Content-Type: text/html; charset=utf-8');
 header('Content-Type: application/json; charset=utf8');


?>
