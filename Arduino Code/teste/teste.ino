
void setup()  
{
  // Open serial communications and wait for port to open:
  Serial.begin(9600);

}

void loop() // run over and over
{ 
 Serial.print("(");
 Serial.print("24");
 Serial.print("32");
 Serial.print(")");
 delay(10000);
 
}

