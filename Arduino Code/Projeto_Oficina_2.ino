#include <TimerOne.h>
#include <dht11.h>
dht11 DHT;
#define DHT11_PIN 4  // define o texto "DHT11_PIN com valor 8. (porta digital 8)
int cont =0;
void setup(){
  Serial.begin(9600); //inicializa serial com um baud-rate de 9600
  Timer1.initialize(5000000); // inicializa com 5 segundos até o estouro
  Timer1.attachInterrupt(Incrementa);
}

void loop(){
  
  if(cont ==12){
          int chk;
          chk = DHT.read(DHT11_PIN);    // Leitura de dados do sensor
          switch (chk){
                case DHTLIB_OK:  
                     Serial.print("(");
                     Serial.print(DHT.humidity,1);
                     Serial.print(DHT.temperature,1);
                     Serial.print(")");
                      break;
                case DHTLIB_ERROR_CHECKSUM:
                     Serial.print("("); 
                     Serial.print("ED");
                     Serial.print(")"); 
                     break;
                case DHTLIB_ERROR_TIMEOUT: 
                     Serial.print("("); 
                     Serial.print("ED");
                     Serial.print(")"); 
                     break;
                default: 
                     Serial.print("("); 
                     Serial.print("ED");
                     Serial.print(")");  
                break;
        }
          cont =0;
          Timer1.setPeriod(5000000);//Programa por 5 segundos
  
    }
}
void Incrementa(){
    cont ++;
    
}
