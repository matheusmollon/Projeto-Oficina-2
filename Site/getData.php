<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 *    
 */

//Create Database connection
    $db = mysql_connect("p3plcpnl0206.prod.phx3.secureserver.net","apoo_endo","Aquaticchinchila1");
    if (!$db) {
        die('Could not connect to db: ' . mysql_error());
    }
 
    //Select the Database
    mysql_select_db("apoo",$db);
    
    //Replace * in the query with the column names.
    $result = mysql_query("select * from Medida", $db);  
    
    //Create an array
    $rows = array();
    
      $Tlinhas = mysql_num_rows($result)+1;
      while($r = mysql_fetch_assoc($result)) {
      $temp = array();
      // the following line will be used to slice the Pie chart
      $temp[] = array('v' => (string) $r['hora']); 

      // Values of each slice
      $temp[] = array('v' => (int) $r['temperatura']); 
      $temp[] = array('v' => (int) $r['humidade']); 
      $rows[] = array('c' => $temp);
      }
      
      $table['rows'] = $rows;
      
      $jsonTable = json_encode($table);
      
      //echo $jsonTable;
    //Close the database connection
    fclose($db);
 