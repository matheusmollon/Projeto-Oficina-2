<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<?php 
    //$db = mysql_connect("p3plcpnl0206.prod.phx3.secureserver.net","apoo_endo","Aquaticchinchila1");
$db = mysql_connect("localhost","root","");
    if (!$db) {
        die('Could not connect to db: ' . mysql_error());
    }
 
    //Select the Database
    mysql_select_db("apoo",$db);
    
  
     $resultdata = mysql_query("select data from Medida group by data",$db);
     
     $temperatura = mysql_query("select temperatura, humidade from Medida where data=now() and hora=(select max(hora) from Medida)",$db);
    
     $maxTemp = mysql_query("select max(temperatura) as temperatura  from Medida",$db);
     
     $maxTemp1 =mysql_fetch_array($maxTemp);
     
     $temperatura1 = mysql_fetch_array($temperatura);
     
     $diaAtual = date('Y-m-d');
     
     $qq = $_POST['dtacomp'];
     
     if(empty($qq)){
       $qq=$diaAtual;
     }
    
     echo '<pre>';
     print_r( $qq);
     echo '</pre>';
?>
<html style="overflow: hidden;"><head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Portal endoWEB</title>

        <!-- CSS -->
        <link href="css/sweet-alert/sweet-alert.min.css" rel="stylesheet">
         <link href="css/app.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/material-design-iconic-font/css/material-design-iconic-font.min.css">
     
        <!-- JS grafico -->
        
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        
       
        
        
   
      <body>
        <header id="header">
            <ul class="header-inner">
                <li id="menu-trigger" data-trigger="#sidebar">
                    <div class="line-wrap">
                        <div class="line top"></div>
                        <div class="line center"></div>
                        <div class="line bottom"></div>
                    </div>
                </li>
            
                <li class="logo hidden-xs">
                    <a href="">endoWEB</a>
                </li>
                
                            
            </ul>
     </header>
            <section id="content">
                <div class="container">
                    
                    <div class="card">
                        <div class="card-header">
                           <div class="row">
                           <div class="col-md-8">
                                 <h2>Estação Meteorológica - Dia <?php echo date('d/m/Y',strtotime($qq)); ?>

                                    <small>Observações das últimas 24 horas, atualizadas a cada 5 minutos</small></h2>

                            </div>
                           <div class=".col-xs-6 .col-md-4">
                              <div class="formData">
                                    <form  name="dataSelect" method="post" >
                                    <select name="dtacomp" class="btn btn-default" id="dtacomp" >
                                       <?php
                                       while ( $diasCapturado = mysql_fetch_array($resultdata))
                                                if ($diaAtual == $diasCapturado['data'] || $diaAtual == $_POST['dtacomp']) {
                                                 echo '<option selected value="'.$diasCapturado['data'].'">' .date('d/m/Y',strtotime($diasCapturado['data'])) . '</option>';
                                                  
                                                }else{
                                                echo '<option value="'.$diasCapturado['data'].'">' .date('d/m/Y',strtotime($diasCapturado['data'])) . '</option>';
                                                }
                                      ?>
                                  </select>
                                        <button type="submit" class="btn btn-default" name="sendData">Selecionar!</button>
                                     </form>
                               </div>
                              
                           </div>
                        </div>
                           </div>
                           
                        
                        <div class="card-body">
                          
                          
                                <?php  include 'grafico.php'; ?>
                              
                        </div>
                    </div>
                    
                    <div class="card">
                        <div class="card-header">
                           <div class="row">
                           <div class="col-md-8">
                               <h2>Observações no Mês </h2>

                                  

                            </div>
                           <div class=".col-xs-6 .col-md-4">
                              <div class="formData">
                                    
                               </div>
                              
                           </div>
                        </div>
                           </div>
                           
                        
                        <div class="card-body">
                         <!--//Grafico do Mês--> 
                           <div  id="chartMes_div"></div>
                         
                              
                        </div>
                    </div>
                    
                    <div class="dash-widgets">
                        <div class="row">
                            <div class="col-md-3 col-sm-6">
                                <div id="site-visits" class="dash-widget-item bgm-teal">
                                    <div class="dash-widget-header">
                                        <div class="p-20">
                                            <div class="dash-widget-visits">
                                                <!--graficoUltimaHora-->
                                                    <div id="chart_pequeno"></div>
                                                </div>
                                        </div>
                                        
                                        <div class="dash-widget-title">Nos ultimos 60 mim</div>
                                        
                                    </div>
                                    
                                    <div class="p-20">
                                        
                                        <small>Temperatura Atual</small>
                                        <h3 class="m-0 f-400"><?php echo $temperatura1['temperatura'];?></h3>
                                        
                                        <br>
                                        
                                        <small>Umidade Atual</small>
                                        <h3 class="m-0 f-400"><?php echo $temperatura1['humidade'];?></h3>
                                        
                                        <br>
                                        
                                        <small>Maior temperatura registrada</small>
                                        <h3 class="m-0 f-400"><?php echo $maxTemp1['temperatura'];?></h3>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="col-md-3 col-sm-6">
                                <div id="pie-charts" class="dash-widget-item">
                                    <div class="bgm-pink">
                                        <div class="dash-widget-header">
                                            <div class="dash-widget-title">Umidade do Ar</div>
                                        </div>
                                        
                                        <div class="clearfix"></div>
                                        
                                        <div class="text-center p-20 m-t-25">
                                            <div class="easy-pie main-pie" data-percent="75">
                                                <div class="percent">45</div>
                                                <div class="pie-title"></div>
                                            <canvas height="148" width="148"></canvas></div>
                                        </div>
                                    </div>
                                   
    
                                </div>
                            </div>
                            
                            <div class="col-md-3 col-sm-6">
                                <div class="dash-widget-item bgm-lime">
                                   <div class="clearfix"></div>
                                   <div class="text-center p-20">
                                          <div id="termometro_div"></div>
                                          <div id="umidade_div"></div>
                                   </div>
                                </div>
                                
                            </div>
    
                            <div class="col-md-3 col-sm-6">
                                <div class="clearfix"></div>
                                <div id="pie-charts" class="dash-widget-item">
                                   <div class="bgm-limel">
                                    <div class="p-t-20 p-b-20 text-center">
                                        <div class="easy-pie sub-pie-1" data-percent="56">
                                            <div class="percent">56</div>
                                            <div class="pie-title">Bounce Rate</div>
                                        <canvas height="95" width="95"></canvas></div>
                                        <div class="easy-pie sub-pie-2" data-percent="84">
                                            <div class="percent">84</div>
                                            <div class="pie-title">Total Opened</div>
                                        <canvas height="95" width="95"></canvas></div>
                                    </div>
                                   </div>
                                  </div>
                            </div>
                        </div>
                    </div>

                </div>
            </section>
        </section>
        
        <!-- Older IE warning message -->
        <!--[if lt IE 9]>
            <div class="ie-warning">
                <h1 class="c-white">IE SUCKS!</h1>
                <p>You are using an outdated version of Internet Explorer, upgrade to any of the following web browser <br/>in order to access the maximum functionality of this website. </p>
                <ul class="iew-download">
                    <li>
                        <a href="http://www.google.com/chrome/">
                            <img src="img/browsers/chrome.png" alt="">
                            <div>Chrome</div>
                        </a>
                    </li>
                    <li>
                        <a href="https://www.mozilla.org/en-US/firefox/new/">
                            <img src="img/browsers/firefox.png" alt="">
                            <div>Firefox</div>
                        </a>
                    </li>
                    <li>
                        <a href="http://www.opera.com">
                            <img src="img/browsers/opera.png" alt="">
                            <div>Opera</div>
                        </a>
                    </li>
                    <li>
                        <a href="https://www.apple.com/safari/">
                            <img src="img/browsers/safari.png" alt="">
                            <div>Safari</div>
                        </a>
                    </li>
                    <li>
                        <a href="http://windows.microsoft.com/en-us/internet-explorer/download-ie">
                            <img src="img/browsers/ie.png" alt="">
                            <div>IE (New)</div>
                        </a>
                    </li>
                </ul>
                <p>Upgrade your browser for a Safer and Faster web experience. <br/>Thank you for your patience...</p>
            </div>   
        <![endif]-->
        
        <!-- Javascript Libraries -->
        <script src="js/jquery-2.1.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        
        <!--<script src="js/jquery.flot.min.js"></script>-->
        <!--<script src="js/jquery.flot.resize.min.js"></script>-->
        <!--<script src="js/curvedLines.js"></script>-->
        <!--<script src="js/jquery.sparkline.min.js"></script>-->
        <!--<script src="js/jquery.easypiechart.min.js"></script>-->
        
        <!--<script src="js/jquery.simpleWeather.min.js"></script>-->
        <script src="js/jquery.autosize.min.js"></script>
        <script src="js/jquery.nicescroll.min.js"></script>
        <script src="js/bootstrap-growl.min.js"></script>
        
        <!--<script src="js/flot-charts/curved-line-chart.js"></script>-->
        <!--<script src="js/flot-charts/line-chart.js"></script>-->
        <!--<script src="js/charts.js"></script>-->
        
        <!--<script src="js/charts.js"></script>-->
        <script src="js/functions.js"></script>


        
    
  <div class="flot-tooltip" style="top: 385px; left: 146px; display: none;">Product 1 of 0.25 = 11.33</div><div class="flot-tooltip" style="top: 385px; left: 146px; display: none;">Product 1 of 0.25 = 11.33</div><div id="ascrail2000" class="nicescroll-rails nicescroll-rails-vr" style="width: 5px; z-index: auto; cursor: default; position: fixed; top: 0px; height: 100%; right: 0px; display: block; opacity: 0;"><div class="nicescroll-cursors" style="position: relative; top: 0px; float: right; width: 5px; height: 214px; border: 0px; border-top-left-radius: 0px; border-top-right-radius: 0px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px; background-color: rgba(0, 0, 0, 0.298039); background-clip: padding-box;"></div></div><div id="ascrail2000-hr" class="nicescroll-rails nicescroll-rails-hr" style="height: 5px; z-index: auto; position: fixed; left: 0px; width: 100%; bottom: 0px; cursor: default; display: none; opacity: 0;"><div class="nicescroll-cursors" style="position: absolute; top: 0px; height: 5px; width: 1366px; border: 0px; border-top-left-radius: 0px; border-top-right-radius: 0px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px; background-color: rgba(0, 0, 0, 0.298039); background-clip: padding-box;"></div></div></body></html>