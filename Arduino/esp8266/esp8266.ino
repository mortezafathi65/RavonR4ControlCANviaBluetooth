#include <SPI.h>
#include <SoftwareSerial.h>
#include <mcp2515.h>

struct can_frame canMsg;
struct can_frame canMsg1;
struct can_frame canMsg2;

MCP2515 mcp2515(D1);
const int INT_PIN = D2;




const int SS_RX_PIN = D3;
const int SS_TX_PIN = D4;
SoftwareSerial softwareSerial(SS_RX_PIN, SS_TX_PIN);

void setup() {
  canMsg1.can_id  = 0x0F6;
  canMsg1.can_dlc = 8;
  canMsg1.data[0] = 0x8E;
  canMsg1.data[1] = 0x87;
  canMsg1.data[2] = 0x32;
  canMsg1.data[3] = 0xFA;
  canMsg1.data[4] = 0x26;
  canMsg1.data[5] = 0x8E;
  canMsg1.data[6] = 0xBE;
  canMsg1.data[7] = 0x86;

  canMsg2.can_id  = 0x036;
  canMsg2.can_dlc = 8;
  canMsg2.data[0] = 0x0E;
  canMsg2.data[1] = 0x00;
  canMsg2.data[2] = 0x00;
  canMsg2.data[3] = 0x08;
  canMsg2.data[4] = 0x01;
  canMsg2.data[5] = 0x00;
  canMsg2.data[6] = 0x00;
  canMsg2.data[7] = 0xA0;
    
    Serial.begin(115200);
    softwareSerial.begin(115200);
  
  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS);
  mcp2515.setNormalMode();
  
  Serial.println("------- CAN Read ----------");
  Serial.println("ID  DLC   DATA");



}

void loop() {
 if(!digitalRead(INT_PIN))                         // If CAN0_INT pin is low, read receive buffer
  {
  if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK) {
    softwareSerial.print(canMsg.can_id, HEX); // print ID
    softwareSerial.print(" "); 
    softwareSerial.print(canMsg.can_dlc, HEX); // print DLC
    softwareSerial.print(" ");
    
    for (int i = 0; i<canMsg.can_dlc; i++)  {  // print the data
      softwareSerial.print(canMsg.data[i],HEX);
      softwareSerial.print(" ");
    }

    softwareSerial.println();      
  } }

String str = "";
if (softwareSerial.available()) {
  str = softwareSerial.readString();

      }

 if (str.length() > 0) {
    byte buf[str.length()];
    str.getBytes(buf, str.length());
    Serial.write(buf, sizeof(buf));
    Serial.write(10);
        if (str.equals("beep\n")) {
       mcp2515.sendMessage(&canMsg1);
    }
    str = "";
    }
    


  
   

  
  if (Serial.available())
    softwareSerial.write(Serial.read());
}
