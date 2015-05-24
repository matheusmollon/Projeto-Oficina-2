    google.load('visualization', '1.1', {packages: ['line','gauge']});
    google.setOnLoadCallback(drawChart);
    google.setOnLoadCallback(termometro);
    function drawChart() {


         var jsonData = $.ajax({
          url: "getData.php",
          dataType:"json",
          async: false
          }).responseText;
          
          
     var data = new google.visualization.DataTable(jsonData);
     
         
      var options = {
        chart: {
          
           axes: {
            x: {
            0: {side: 'top'}
            }
          },
          legend: { position: 'bottom' }  
        },
        width: 980,
        height: 270
      };

      var chart = new google.charts.Line(document.getElementById('linechart_material'));

      chart.draw(data, options);
    }

function termometro() {

        var data = google.visualization.arrayToDataTable([
          ['Label', 'Value'],
          ['Temperatura', 55],
         
        ]);

        var options = {
          width: 400, height: 120,
          redFrom: 90, redTo: 100,
          yellowFrom:75, yellowTo: 90,
          minorTicks: 5
        };

        var chart = new google.visualization.Gauge(document.getElementById('chart_div'));

        chart.draw(data, options);

        setInterval(function() {
          data.setValue(0, 1, 40 + Math.round(60 * Math.random()));
          chart.draw(data, options);
        }, 13000);
        setInterval(function() {
          data.setValue(1, 1, 40 + Math.round(60 * Math.random()));
          chart.draw(data, options);
        }, 5000);
        setInterval(function() {
          data.setValue(2, 1, 60 + Math.round(20 * Math.random()));
          chart.draw(data, options);
        }, 26000);
      }