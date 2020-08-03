void ultra_Json(float distance1, float distance2, float distance3) { //Json방식으로 표기
  StaticJsonDocument<200> doc;  //JSON변환에 필요한 버퍼 선언, 용량 200 설정
  JsonArray ultra = doc.createNestedArray("ultra");
  ultra.add(distance1);
  ultra.add(distance2);
  ultra.add(distance3);

  //serializeJson(ultra, Serial);
  Serial.println();
  serializeJsonPretty(doc, Serial);
}

void chosen_drink_Json(int num1, int num2, int num3) {
  StaticJsonDocument<200> doc;
  JsonArray chosen_drink = doc.createNestedArray("chosen drink");
  chosen_drink.add(num1);
  chosen_drink.add(num2);
  chosen_drink.add(num3);

  Serial.println();
  serializeJsonPretty(doc, Serial);
}


void accuracy(float distance, int i){ //손의 위치를 카운트 하는 함수
  if(distance<=32 && distance >=0){
    if(distance>=0 && distance <11)count[i][4]+=1;      //제일 오른쪽 음료수 
    if(distance>=11 && distance <19)count[i][3]+=1;
    if(distance>=19 && distance <26)count[i][2]+=1;      
    if(distance>=26 && distance <33)count[i][1]+=1; //제일 왼쪽 음료수
  }
  else count[i][0]+=1;   //손이 감지되지 않음     
}

void choose_drink(int q){ //어떤곳이 제일 많이 센싱되었는지 나타내는 함수
  for(int j=0;j<q;j++){
    if (count[0][j]>count[0][0]) chosen_number[0]= number_of_drink[0][j];
    if (count[1][j]>count[1][0]) chosen_number[1]= number_of_drink[1][j];
    if (count[2][j]>count[2][0]) chosen_number[2]= number_of_drink[2][j];
    }  
}
/*
 * 센싱할 때 손을 대지 않은 부분이 센싱된다면 그건 오류.
 */
void multi_chosen_check(int p){ // 음료수가 복수 체크되었는지 오류체킹
  int not_0 = 0;
  for(int i=0;i<p;i++){
    if(chosen_number[i] !=0) not_0+=1;
  }

  if(not_0 == 1 || not_0 == 0) Serial.println("normal state");
  else Serial.println("multiple selection");
}
