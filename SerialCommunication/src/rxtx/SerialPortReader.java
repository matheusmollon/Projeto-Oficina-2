
package rxtx;

import JDBC.DBConnector;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;


public class SerialPortReader extends Thread implements SerialPortEventListener {
	SerialPort serialPort;
        /** Ports names by S.O. **/
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM4", // Windows
	};
        // Port name index
        private int portIndex;
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
        // Object to hold the connection with the database
        private static Connection dataBaseConnection = null;
        // Object to hold de data base mannager
        private DBConnector dataBaseMannager = null;
        // Boolean to see if the connection is on
        private boolean isConnected = false;

        public SerialPortReader(){
            
        }
        
        
        
        @Override
        public void run(){
            System.out.println("Começou: " + this.getName());
            //instancia o objeto de manuseio de conexão
            dataBaseMannager = new DBConnector();
            //Tenta abrir a conexão com o banco de dados
            try {
                dataBaseConnection = DBConnector.getDataBaseConnection();
            } catch (Exception ex1) {
                System.out.println(ex1.toString());
            }
            this.initialize();
            
            while(this.portaSerialPresente() && dataBaseMannager.isConnected()){
            
            }

        }
        
              
        
        
        // Initialize the serial port
        // get the name of the serial ports and start communication
	public void initialize() {
            
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                        int count = 0;
			for (String portName : PORT_NAMES) {
                            System.out.println("achei "+portName);
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
                                        portIndex = count;
					break;
				}
                            count++;    
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
                isConnected = true; 
	}

	//close the serial port
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
        
         public static HashMap procurarPortas() {
        
                //Cria o HashMap para guardar as portas
                HashMap portaMap = new HashMap();

                //Cria o Enumeration que vai receber as portas
                Enumeration portas = null;

                //Pega os identificadores das portas
                portas = CommPortIdentifier.getPortIdentifiers();

                while (portas.hasMoreElements()) {

                    CommPortIdentifier portaAtual = (CommPortIdentifier) portas.nextElement();

                    //seleciona apenas as portas que são seriais
                    if (portaAtual.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                        //Mapeia a porta para poder trabalhar com ela a partir de seu nome
                        portaMap.put(portaAtual.getName(), portaAtual);
                    }
                }

            return portaMap;
        }
         
         public boolean portaSerialPresente() {

            //Pega as portas disponíveis
            HashMap portasDisponiveis = SerialPortReader.procurarPortas();

            if (portasDisponiveis.get(PORT_NAMES[portIndex]) != null) {
                return true;
            } else {
                return false;
            }
        }

	//event to read the serial port
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Byte inputLine=(byte)input.read();
                                dataReceived = new String(new byte[] {inputLine});
                                if(dataReceived.equalsIgnoreCase("(")){
                                    System.out.println("Pacote recebido. Iniciar leitura");
                                    pakageReceived = true;
                                    answer = "";
                                }
                                else if(pakageReceived==true && answer.length()<3){
                                    System.out.println("Recebendo o valor da temperatura");
                                    temperature += dataReceived;
                                }
                                else if(pakageReceived == true && (answer.length()>2 && answer.length()<5)){
                                    System.out.println("Recebendo o valor de humidade");
                                    humidity +=dataReceived;                                    
                                }
                                else if(dataReceived.equalsIgnoreCase(")") && answer.length()== 5){
                                    System.out.println("Pacote recebido com sucesso e pronto para ser salvo! Recebido: "+answer+")");
                                    try{
                                         try {
                                                 dataBaseConnection = DBConnector.getDataBaseConnection();
                                         } catch (Exception ex1) {
                                                System.out.println(ex1.toString());
                                         }
                                    String sqlQuerry = "INSERT INTO Medida (temperatura,humidade,data,hora) VALUES(?,?,now(),(now()+interval 4 hour));";
                                    PreparedStatement preparedState = dataBaseConnection.prepareStatement(sqlQuerry);
                                    preparedState.setInt(1, Integer.parseInt(temperature));
                                    preparedState.setInt(2, Integer.parseInt(humidity));
                                    preparedState.executeUpdate();
                                    System.out.println("Dados salvos com sucesso!");
                                    freeBuffer();
                                    }catch(SQLException e){
                                        System.out.println("Não foi possível salvar na base: "+e.getMessage());
                                    }
                                }
                                else{
                                    System.out.println("Falha na recepção do pacote, esvaziando buffers");
                                    freeBuffer();
                                }
                                answer += dataReceived;
                                System.out.println("ESD"+answer);
				
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
		Thread t = new Thread() {
			public void run() {
				try{
                                    Thread.sleep(10);
                                    
                                }catch (InterruptedException ie) {
                                    
                                }
			}
		};
		t.start();
                
		System.out.println("Started");
                String date = "dd/MM/yyyy";
                String hour = "h:mm - a";
                java.util.Date now = new java.util.Date();
                SimpleDateFormat dateHourFormat = new SimpleDateFormat(date);
                System.out.println("Date: "+dateHourFormat.format(now));
                dateHourFormat = new SimpleDateFormat(hour);
                System.out.println("Hour: "+dateHourFormat.format(now));
//              
	}
}
