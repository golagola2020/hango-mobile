import serial

def main():
    arduinoPort="/dev/ttyACM0" #when port is changed,then you must be change too.
    port = serial.Serial(arduinoPort,9600)
    port.flushInput()

    while port.readable:
        input = port.readline()
        arr = input.split()

        print(arr)

        if len(arr) == 3:
            head = arr[0]
            mid = arr[1]
            tail = int(arr[2])
        if len(arr) == 2:
            dup = arr[0]
            tail = int(arr[1])

if __name__ == "__main__":
    main()
