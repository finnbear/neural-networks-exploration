void setup() {
  Serial.begin(9600);

  // Charge the capacitor
  Serial.println("Charging...");
  pinMode(A0, INPUT);
  float voltage = 0;
  for (int i = 0; i < 200 && voltage < 4.95f; i++) {
    pinMode(3, OUTPUT);
    analogWrite(3, 255);
    delay(50);
    analogWrite(3, 0);
    pinMode(3, INPUT);
    delay(10);
    Serial.print("Voltage: ");
    voltage = analogRead(A0) / (float)1023 * 5;
    Serial.print(voltage);
    Serial.println(" volts");
  }

  // Take readings
  float charge = 1024;
  long startTime = millis();

  Serial.print("Waiting for resistor...");

  while (charge > 1000) {
    pinMode(3, OUTPUT);
    analogWrite(3, 255);
    delay(20);
    analogWrite(3, 0);
    pinMode(3, INPUT);
    delay(10);
    charge = analogRead(A0);
    startTime = millis();
  }

  Serial.println("");

  Serial.print("Discharging...");
  pinMode(3, INPUT);

  while (charge > 100) {
    delay(1);
    charge = analogRead(A0);
  }

  Serial.println("");

  long elapsedTime = millis() - startTime;

  Serial.print("Elapsed: ");
  Serial.print(elapsedTime);
  Serial.println(" milliseconds");
}

void loop() {
  
}
