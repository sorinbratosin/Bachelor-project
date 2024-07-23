#include <Wire.h>
#include <WiFi.h>
#include <WiFiManager.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

// Pornire
String serialNumber = "187253A9J";

// setare detalii conexiune
String endpointPostSensorData = "postHumidity";
String serverURL = "http://167.172.107.65:54653";
String serverURLLocal = "http://167.172.107.65:546536";
String serviceURL = "";
boolean isLocalServer = false;
String apiKey = "6Lv8q9bJSDjs9kI9gj9T3Qb5k9LzG9cB";

// Struct pentru httpClient
struct httpResultStruct {
    int httpResponseCode;
    String httpResponseString;
};

// Display
boolean isOledEnabled = false;
bool isTempMesage = false;
bool isOnDisplay = true;
bool wasDiplaySensorDataTFTInitial = false;
String tempMessageToBeDisplayed = "";
SemaphoreHandle_t xDisplayMutex;
SemaphoreHandle_t xWifiMutex;

#include <Adafruit_SSD1306.h>
Adafruit_SSD1306 display = Adafruit_SSD1306(128, 64, &Wire);

// Taskuri
TaskHandle_t TaskReadSensors;
TaskHandle_t TaskDisplaySensors;
TaskHandle_t TaskConnectToWifi;
TaskHandle_t TaskSendSensorsToServer;

// WIFI
WiFiManager wifiManager;
String wifiState = "Off";
bool beforeWifiConnection = true;
boolean isConnectedToWifi = false;
boolean savePermanently = true;

// Relee
#define PIN_RELEU_POMPA 33
const byte relayOn = LOW;
const byte relayOff = HIGH;
uint8_t pragOnRelay = 45;
uint8_t pragOffRelay = 65;
bool isIrigated = false; // Variabilă pentru a ține minte starea irigării

// Senzor
#define PIN_SENZOR_UMIDITATE 34
int umiditateCurenta;
SemaphoreHandle_t xReadSensorsMutex;
bool sensorDataReady = false;

void setup() {
  Serial.begin(115200);
  Serial.println("start setup");

  delay(500);
  Wire.begin(21, 22);

  // SETUP SEMAFOARE
  xReadSensorsMutex = xSemaphoreCreateMutex();
  if (xReadSensorsMutex == NULL) {
    Serial.println("Error creating xReadSensorsMutex");
    while (1);
  }

  xDisplayMutex = xSemaphoreCreateMutex();
  if (xDisplayMutex == NULL) {
    Serial.println("Error creating xDisplayMutex");
    while (1);
  }

  xWifiMutex = xSemaphoreCreateMutex();
  if (xWifiMutex == NULL) {
    Serial.println("Error creating xWifiMutex");
    while (1);
  }

  // display setup
  Serial.println("set display");
  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.clearDisplay();
  display.display();
  display.setTextSize(1);
  display.setTextColor(SSD1306_WHITE);
  display.display();

  // wifi setup

  // SET BUTTON OUTPUT
  pinMode(PIN_RELEU_POMPA, OUTPUT);
  digitalWrite(PIN_RELEU_POMPA, relayOff);

  // afisarea datelor de la senzori
  xTaskCreatePinnedToCore(
    TaskDisplaySensorsData,
    "TaskDisplaySensorsData",
    48000,
    NULL,
    1,
    &TaskDisplaySensors,
    1);

  xTaskCreatePinnedToCore(
    TaskReadHumiditySensorData,
    "TaskReadHumiditySensorData",
    16000,
    NULL,
    1,
    &TaskReadSensors,
    0);

  xTaskCreate(
    TaskConnectingToWifi,
    "TaskConnectingToWifi",
    50000,
    NULL,
    1,
    &TaskConnectToWifi
  );

  xTaskCreate(
    TaskSendSensorsDataToServer,
    "TaskSendSensorsDataToServer",
    10000,
    NULL,
    3,
    &TaskSendSensorsToServer
  );

  Serial.println("stop setup");
}

void loop() {
}

//taskul de citire senzor umiditate
void TaskReadHumiditySensorData(void * pvParameters) {
  for (;;) {
    xSemaphoreTake(xReadSensorsMutex, portMAX_DELAY);
    readHumiditySensor();
    checkAndManagePump();
    sensorDataReady = true;
    xSemaphoreGive(xReadSensorsMutex);
    vTaskDelay(pdMS_TO_TICKS(3000));
  }
}

//metoda pentru verificarea si gestionarea pompei
void checkAndManagePump() {
  if(umiditateCurenta < pragOnRelay) {
    digitalWrite(PIN_RELEU_POMPA, relayOn);
    isIrigated = true; // Pompa a fost pornită, deci planta este irigată
  } else if(umiditateCurenta >= pragOffRelay) {
    digitalWrite(PIN_RELEU_POMPA, relayOff);
    isIrigated = false; // Pompa a fost oprită pentru că planta nu are nevoie de irigare
  }
}

// metoda citire senzor de umiditate
void readHumiditySensor() {
  int sensorValue = analogRead(PIN_SENZOR_UMIDITATE);  // Citeste valoarea analog a senzorului
  float humidityPercentage = (1.0 - sensorValue / 4095.0) * 100.0;
  Serial.println(String(humidityPercentage));
  umiditateCurenta = humidityPercentage;
}

// task pentru afisare senzor
void TaskDisplaySensorsData(void * pvParameters) {
  Serial.print("Task1 TaskDisplaySensorsData  on core ");
  Serial.println(xPortGetCoreID());

  for (;;) {
    vTaskDelay(pdMS_TO_TICKS(5000));
    displaySensorData();
  }
}

// afisare informatii pe display
void displaySensorData() {
  Serial.println("-----------------------ParamDisplay");
  display.clearDisplay();
  display.setFont(NULL);
  display.setTextSize(1);
  display.setCursor(0, 0);
  display.println("-      Sera IoT     -");
  display.println("*********************");
  display.println("");
  display.println("Umiditate: " + String(umiditateCurenta) + " %");
  display.println("");
  if(digitalRead(PIN_RELEU_POMPA) == relayOn) {
    display.println("Pompa: pornita");
  } else {
    display.println("Pompa: oprita");
  }
  display.display();
}


//1.VERIFICARE SI CONECTARE LA WIFI=========================================================================
//=========================================================================
//=========================================================================
void TaskConnectingToWifi( void * pvParameters ) {
  /*
   * Se verifica starea conexiunii wifi la fiecare zece secunde.
   * Daca nu device-ul nu este conectat atunci se incearca conectarea
   */
   Serial.print("START TaskConnectingToWifi");
  unsigned int temp1 = uxTaskGetStackHighWaterMark(nullptr);
  Serial.println("TaskConnectingToWifi: TaskConnectingToWifi=" + String(temp1));
    
  for(;;){
    Serial.println("Check internet connection in TaskConnectingToWifi");
    if(!isWifiConnected()){
      checkAndConnectToWifi();
    }
    vTaskDelay(pdMS_TO_TICKS(10000));
  }
  
}


//-----------------------------------GET CURRENT WIFI STATUS-------------------------------------------------
  boolean isWifiConnected(){
    /*
     * Returneaza daca device-ul este conectat la wifi sau nu
     */
    if(beforeWifiConnection){
      Serial.println("beforeWifiConnection false");
      //inseamna ca inca nu s-a incercat conectarea la wifi, deci statusul este false
      return false;
    }
    
    boolean isConnected = false;
    
//    WiFi.mode(WIFI_STA);
    int status = WL_IDLE_STATUS;
    status = WiFi.status();
    Serial.println("wifi status: " + get_wifi_status(status));
    if (get_wifi_status(status) == "WL_CONNECTED") {
      isConnectedToWifi = true;
      isConnected = true;
      Serial.println("Pornim led verde in isWifiConnected");
    }else{
      isConnectedToWifi = false;
      isConnected = false;
      Serial.println("Pornim led rosu in isWifiConnected");
    }
    
  beforeWifiConnection = false;
  return isConnected;
  }


//-----------------------------------Connecting to wifi-------------------------------------------------
boolean checkAndConnectToWifi() {
/*
 * Aceasta functionalitate conecteza devivce-ul la wifi prin wifiManager
 */
  boolean isConnected = false;
  wifiState = "Off";
  WiFi.mode(WIFI_STA);
  WiFi.setTxPower(WIFI_POWER_8_5dBm);
  int status = WL_IDLE_STATUS;
  status = WiFi.status();
  Serial.println("wifi status: " + get_wifi_status(status));
  beforeWifiConnection = false;
  Serial.println("beforeWifiConnection false: se incearca prima conectare");
  if (get_wifi_status(status) == "WL_CONNECTED") {
    xSemaphoreTake(xWifiMutex, portMAX_DELAY);
    wifiState = "On";
    isConnected = true;
    isConnectedToWifi = true;
    xSemaphoreGive(xWifiMutex);
  }
  else {
    isConnectedToWifi = false;
    Serial.println("Incearca conectarea prin wifi manager");
    wifiManager.setTimeout(120);
    bool isConnectedToWifi = wifiManager.autoConnect("seraPlant", "12345678");

    status = WiFi.status();
    Serial.println("wifi status2: " + get_wifi_status(status));

    if (get_wifi_status(status) == "WL_CONNECTED") {
      xSemaphoreTake(xWifiMutex, portMAX_DELAY);
      isConnectedToWifi = true;
      wifiState = "On";
      isConnected = true;
      xSemaphoreGive(xWifiMutex);
      //if you get here you have connected to the WiFi
      Serial.println("Conectat la wifi");
    }
    else {
      xSemaphoreTake(xWifiMutex, portMAX_DELAY);
      isConnectedToWifi = false;
      wifiState = "Off";
      isConnected = false;
      xSemaphoreGive(xWifiMutex);

      Serial.println("Failed to connect or hit timeout");

    }
  }

  return isConnected;
}


//-----------------------------------GET WIFI STATUS BY STATUS NUMBER-------------------------------------------------
String get_wifi_status(int status) {
  switch (status) {
    case WL_IDLE_STATUS:
      return "WL_IDLE_STATUS";
    case WL_SCAN_COMPLETED:
      return "WL_SCAN_COMPLETED";
    case WL_NO_SSID_AVAIL:
      return "WL_NO_SSID_AVAIL";
    case WL_CONNECT_FAILED:
      return "WL_CONNECT_FAILED";
    case WL_CONNECTION_LOST:
      return "WL_CONNECTION_LOST";
    case WL_CONNECTED:
      return "WL_CONNECTED";
    case WL_DISCONNECTED:
      return "WL_DISCONNECTED";
  }
}

// task pentru trimitere date senzor catre server
void TaskSendSensorsDataToServer(void * pvParameters) {
  Serial.print("Task1 TaskSendSensorsDataToServer  on core ");
  Serial.println(xPortGetCoreID());
  Serial.println("TaskSendSensorsDataToServer: Asteptam calibrarea senzorilor inainte de a trimite datele la server");
  vTaskDelay(pdMS_TO_TICKS(3000));
  bool isTimeToSendDataToServer = false;
  bool wasPostedDataByParamExceeded = false;
  unsigned long initialPostDataTime = millis();
  unsigned long initialPostDataTimeByParamExceeded = millis();

  int maxPostDataTime = 50000; // la maxim un minut trimitem datele la server
  int maxPostedDataByParamExceededTime = 20000; // daca e depasit un parametru trimitem datele la fiecare 20 de secunde

  for (;;) {
    if (isConnectedToWifi == true) {
      unsigned long currentTimePostData = millis();
      unsigned long postDataTimeElapsed = currentTimePostData - initialPostDataTime;
      unsigned long postedDataByParamExceededTimeElapsed = currentTimePostData - initialPostDataTimeByParamExceeded;

      // creem pointer pt struct httpResultStruct
      httpResultStruct temp = {0, ""};
      httpResultStruct *httpResultPtr = &temp;

      if (isLocalServer==false) {
        serviceURL = serverURL + "/" + endpointPostSensorData;
      } else {
        serviceURL = serverURLLocal + "/" + endpointPostSensorData;
      }

      Serial.print("TaskSendSensorsDataToServer: serviceURL: " + serviceURL);
      httpResultStruct httpResult = httpPostAndValidateKey(httpResultPtr, generateJSONData(), serviceURL);

      Serial.println("TaskSendSensorsDataToServer: Response code: " + String(httpResult.httpResponseCode));
      Serial.println("TaskSendSensorsDataToServer: Response string: " + httpResult.httpResponseString);

      if (httpResult.httpResponseCode > 0) {
        Serial.print("TaskSendSensorsDataToServer: HTTP Response code: ");
        Serial.println(httpResult.httpResponseCode);
        Serial.println(httpResult.httpResponseString);
      } else {
        Serial.print("TaskSendSensorsDataToServer: http post error code: ");
        Serial.println(httpResult.httpResponseCode);
      }

      if(isTimeToSendDataToServer) {
        initialPostDataTime = millis(); // reinitializam timpul de la care am facut/facem postarea
      }
      vTaskDelay(pdMS_TO_TICKS(5000)); // asteptam 3 de secunde si verificam din nou daca senzorii sunt calibrati si apoi trimitem datele la server

    } else { // nu e conectat la wifi
      Serial.println("TaskSendSensorsDataToServer: Nu s-a putut realiza conexiunea la wifi, incercam iar in 10 secunde");
      vTaskDelay(pdMS_TO_TICKS(10000)); // asteptam 10 de secunde si verificam din nou daca s-a conectat aparatul la wifi
    }

    // asteptam 1 secunda intainte de a reincerca/verifica daca a trecut timpul sau daca s-a depasit vreun parametru de calitate
    vTaskDelay(pdMS_TO_TICKS(1000));

    unsigned int temp1 = uxTaskGetStackHighWaterMark(nullptr);
    Serial.print("TaskSendSensorsDataToServer: TaskSendSensorsDataToServer="); Serial.println(temp1);
  }
}

// Post catre server pentru validare API key
httpResultStruct httpPostAndValidateKey(httpResultStruct *httpResult, String httpJSON, String serviceURL) {
  HTTPClient httpClient;
  WiFiClient wifiClient;

  httpClient.begin(wifiClient, serviceURL);

  // Content-type header
  httpClient.addHeader("Content-Type", "application/json");

  // Adaug header si key pentru acces
  httpClient.addHeader("DeviceIOT", apiKey); 

  httpResult->httpResponseCode = httpClient.POST(httpJSON);
  httpResult->httpResponseString = httpClient.getString();

  httpClient.end();
  return *httpResult;
}

// trimite sub forma de JSON datele
String generateJSONData() {
  String httpJSON;

  xSemaphoreTake(xReadSensorsMutex, portMAX_DELAY);
  const size_t bufferSize = JSON_OBJECT_SIZE(3) + JSON_OBJECT_SIZE(10) + 70;
  StaticJsonDocument<bufferSize> data;

  data["device_serial_number"] = serialNumber;
  data["humidity"] = umiditateCurenta;
  data["irigat"] = isIrigated;
  data["user_id"] = 1;

  serializeJson(data, httpJSON);
  Serial.println(httpJSON);
  xSemaphoreGive(xReadSensorsMutex);

  return httpJSON;
}
