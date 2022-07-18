#include "DHT.h"
#include <SoftwareSerial.h>
SoftwareSerial mySerial( 7,8);
#define DHTTYPE DHT22
#define DHTPIN 12  
#define BUZZER 9
#define FAN 13
#define PIR 2
#define MOTOR 3
#define UECHO 4
#define UTRIG 5
#define IR   6
DHT dht(DHTPIN, DHTTYPE);
int temp,humi,input,ir,rpm,pir;
int alarm=1;
int distance;
float count=0;
int TIME;
int prevtime=0;

void intrupt(){
  count++; // increasing bace on every IR sencor intrupt 
}
void setup() {
    Serial.begin(9600);
    pinMode(FAN,OUTPUT);
    pinMode(MOTOR,OUTPUT);
    pinMode(BUZZER,OUTPUT);
    pinMode(UTRIG,OUTPUT);
    pinMode(PIR,INPUT);
    pinMode(UECHO,INPUT);
    pinMode(IR,INPUT);
    mySerial.begin(9600);
    dht.begin(); 
    attachInterrupt(IR,intrupt,RISING);
}
int ultrasoinc_calc(){
  int dist;
  digitalWrite(UTRIG, LOW);
  delayMicroseconds(2);
  digitalWrite(UTRIG, HIGH);
  delayMicroseconds(10);
  digitalWrite(UTRIG, LOW);
  const unsigned long duration= pulseIn(UECHO, HIGH);
  dist= duration/29/2;
  return dist;
  
}
void loop() {
   


  //--------------- ultrasonic distance calulator------------------//
  distance= ultrasoinc_calc(); 

  
  //--------------calculating motors rpm base on ir sensor----------//
  detachInterrupt(0);     
  TIME = millis()-prevtime;
  rpm = (count/TIME)*60000;
  prevtime=millis();//saving current time;
  count=0;                

   
 

  // ------------ cooling fan ----------------------//
  if (temp>28) //turning fan ON when temp is higher than 28 degree
      digitalWrite(FAN,HIGH);
  if (temp<28) // turning fan OFF when temp is less than 28 degree
      digitalWrite(FAN,LOW);
  
  // ------------------ buzzer ---------------------//
  pir = digitalRead(PIR); // -----reading pir sensor state-----
  if (pir==HIGH) //---- sounding the alarm when something is detected----
      digitalWrite(BUZZER,alarm);
      
  while (Serial.available()){ // data form => 0/1 = buzzer ||  2-100 -> rpm
    input =Serial.read();
  }
  if (input==0 || input ==1) //setting alarm state (turning off/on)
    alarm==input;
  if (input>1 && input <101){
    float speedd=input*2.5;
    analogWrite(MOTOR,speedd);
    mySerial.println(speedd);
    // setting motor speed base on PWM 
  }
  
  //{*temp#humidity*%distance%$rpm$@decetion@} <- output form
  // {*10#20*%1500%$4000$@1@}
   //--------------- setting temp/humi randomly------------------//
  temp = random(10, 100);   humi=random(20, 60);
  distance=random (500,3000);
  rpm= random (1500,5000);
  pir = random (0,2);
  String output= "{*" + String(temp)+ "#" + String(humi) + "*" + "%" + String(distance) + "%" + "$" + String(rpm) + "$" + "@" + String(pir) + "@}";
  Serial.println(output); 

  mySerial.println(output);
  
  
  delay(1000);
  }