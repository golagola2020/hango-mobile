import serial
import json

def main() :
    arduino_port="/dev/ttyACM0" #when port is changed,then you must be change too.
    port = serial.Serial(arduinoPort, 9600)
    port.flushInput()

    drink_mid = 'non'
    hand_mid= 'non'
    dup = 'zero'
    serialNumber = '20200814042555141'

    cnt = 0
    while port.readable :
        input = port.readline()
        arr = input.split()

        dict_result = {'serialNumber' :20200814042555141, 'sensing' :{ 'hand' : hand_mid[2:-1], 'drink' : drink_mid[2:-1] ,'duplicate' : dup[2:-1]  }}
        
        json_vending_result = json.dumps(dict_result) 

        if cnt == 3 :
            print(json_vending_result)
            cnt = 0
        else cnt += 1

        arr_len = len(arr)
        for i in range(arr_len) :
            if str(type(arr[i])) == "<class 'bytes'>" : 
                arr[i] = str(arr[i])

        if arr_len == 2 :
            dup = arr[0]
            dup_tail = arr[1]
        elif arr_len == 3 :
            head = arr[0]
            mid = arr[1]
            tail = int(arr[2])
            if tail == 1:
                hand_head = arr[0]
                hand_mid = arr[1]
            elif tail == 2 :
                drink_head = arr[0]
                drink_mid = arr[1]
        
if __name__ == "__main__" :
    main()
