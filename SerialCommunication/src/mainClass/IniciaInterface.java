/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainClass;

import java.sql.SQLException;
import rxtx.SerialPortReader;

/**
 *
 * @author yusei
 */
public class IniciaInterface {

    public static void main(String[] args) throws InterruptedException {

        boolean continua = true;

        SerialPortReader ls = new SerialPortReader();
        ls.setName("SerialReader");
        ls.start();
        
        while (continua) {
            ls.join();
            
            if(!ls.isAlive()) {
                Thread.currentThread().sleep(5000);
                ls = new SerialPortReader();
                ls.setName("SerialReader");
                ls.start();
            }
            
            System.out.println("THREADS ATIVAS = " + Thread.activeCount());
        }

    }
}
