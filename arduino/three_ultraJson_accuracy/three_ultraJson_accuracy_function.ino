void accuracy(float distance, int i) { //손의 위치를 카운트 하는 함수
  if (distance <= 60 && distance >= 0) {
    if (distance >= 0 && distance < 12)count[i][5] += 1; //제일 오른쪽 음료수
    if (distance >= 12 && distance < 22)count[i][4] += 1;
    if (distance >= 22 && distance < 34)count[i][3] += 1;
    if (distance >= 34 && distance < 45)count[i][2] += 1; //제일 왼쪽 음료수
    if (distance >= 45 && distance < 56)count[i][1] += 1;
  }
  else count[i][0] += 1; //손이 감지되지 않음
}

void ultra_Json(float distance1, float distance2) { //, float distance3) { //Json방식으로 표기//* 오류 확인 부분이라 마지막엔 지움.
  StaticJsonDocument<200> doc;  //JSON변환에 필요한 버퍼 선언, 용량 200 설정
  JsonArray ultra = doc.createNestedArray("ultra");
  ultra.add(distance1); Serial.print(" ");
  ultra.add(distance2); Serial.print(" ");
  Serial.println();
  serializeJsonPretty(doc, Serial);
}

void choose_drink(int q) { //어떤곳이 제일 많이 센싱되었는지 나타내는 함수
  for (int j = 0; j < q; j++) {
    if (count[0][j] > count[0][0]) chosen_number[0] = number_of_drink[0][j];
    if (count[1][j] > count[1][0]) chosen_number[1] = number_of_drink[1][j];
  }
}

void multi_chosen_check(int p) { // 음료수가 복수 체크되었는지 오류체킹
  int not_0 = 0;
  for (int i = 0; i < p; i++) {
    if (chosen_number[i] != "non") not_0 += 1;
  }

  if ( not_0 == 0) {
    Serial.println("normal state");
  }
  else if (not_0 == 1) {
    Serial.println("normal state");
    if (chosen_number[0] != "non") final_check = chosen_number[0];
    if (chosen_number[1] != "non") final_check = chosen_number[1];
    //if(chosen_number[2] != "non") final_check = chosen_number[2];
  }
  else Serial.println("multiple selection");

  Serial.print("not_0 ==");
  Serial.println(not_0);
}

void chosen_drink_Json(String num1, String num2, String num4) { //* 오류 확인 부분,이라 마지막엔 지움.
  // JSON 객체 선언 (동적 메모리 할당)
  DynamicJsonDocument json(200);
  String jsonData = "";

  json["first line"] = num1;
  json["second line"] = num2;
  json["selected dirnk"] = num4;

  serializeJson(json, jsonData);

  Serial.println(jsonData);
}
