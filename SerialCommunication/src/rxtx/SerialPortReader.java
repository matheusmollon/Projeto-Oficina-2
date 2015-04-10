
package rxtx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;


public class SerialPortReader implements SerialPortEventListener {
	SerialPort serialPort;
        /** Ports names by S.O. **/
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
        
        //To read the bytes in the serial port
	private BufferedReader input;
	// The output stream to the port 
	private OutputStream output;
	// Milliseconds to block while waiting for port open 
	private static final int TIME_OUT = 2000;
	// Default bits per second for COM port. 
	private static final int DATA_RATE = 9600;
        // Receive the byte comming from the serial port
        private String dataReceived = "";
        // Holds the entire data received
        private String answer = "";
        // Holds the temperature received to sendo to the database
        private String temperature = "";
        // Holds the humidity received to sendo to the database
        private String humidity = "";
        // Flag to see if data it's being received
        private boolean pakageReceived = false;
        // Initialize the serial port
        // get the name of the serial ports and start communication
	public void initialize() {
            
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
                            System.out.println("achei "+portName);
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	//close the serial port
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	//event to read the serial port
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Byte inputLine=(byte)input.read();
                                dataReceived = new String(new byte[] {inputLine});
                                if(dataReceived.equalsIgnoreCase("(")){
                                    pakageReceived = true;
                                }
                                if(pakageReceived==true && answer.length()<3){
                                    temperature += dataReceived;
                                }
                                if(pakageReceived == true && (answer.length()>2 && answer.length()<=5)){
                                    humidity +=dataReceived;                                    
                                }
                                if(dataReceived.equalsIgnoreCase(")") && answer.length()== 5){
                                    System.out.println("Pacote recebido com sucesso e pronto para ser salvo! Recebido: "+answer+")");
                                    freeBuffer();
                                }
                                else{
                                    System.out.println("Falha na recepção do pacote, esvaziando buffers");
                                    freeBuffer();
                                }
                                answer += dataReceived;
				
			} catch (Exception e) {
                                System.out.println("é nóis o/");
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
        
    
    private void freeBuffer() {
        dataReceived = "";
        answer = "";
        pakageReceived = false;
        temperature = "";
        humidity = "";
    }
        /**main class to test the serial reading.
            This will read the serial port, analyse the data and do something with the data
            In this current implementation it's just showing the data in the terminal**/
	public static void main(String[] args) throws Exception {
                
                SerialPortReader main = new SerialPortReader();
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				try {Thread.sleep(10);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
}
