import serial
import json



def main():
    arduinoPort="/dev/ttyACM0" #when port is changed,then you must be change too.
    port = serial.Serial(arduinoPort,9600)
    port.flushInput()

    drink_mid = 'non'
    hand_mid= 'non'
    dup = 'zero'
    serialNumber = '20200814042555141'
    i = 0
    while port.readable:
        input = port.readline()
        arr = input.split()

        dict_result = {'serialNumber' :20200814042555141, 'sensing' :{ 'hand' : hand_mid[2:-1], 'drink' : drink_mid[2:-1] ,'duplicate' : dup[2:-1]  }}
        
        json_vending_result = json.dumps(dict_result) 
        

        if i == 3:
            print(json_vending_result)
            i = 0
        i += 1

        if str(type(arr[0])) == "<class 'bytes'>":
            arr[0] = str(arr[0])
        if str(type(arr[1])) == "<class 'bytes'>":
            arr[1] = str(arr[1])
                    

        if len(arr) == 3:
            head = arr[0]
            mid = arr[1]
            tail = int(arr[2])
            if tail is 1:
                hand_head = arr[0]
                hand_mid = arr[1]
            if tail is 2:
                drink_head = arr[0]
                drink_mid = arr[1]

        if len(arr) == 2:
            dup = arr[0]
            dup_tail = arr[1]

        
if __name__ == "__main__":
    main()
