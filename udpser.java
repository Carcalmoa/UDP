import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class udpser{
    public static void main(String[] args) {
        if (args.length!=1){
            System.out.println ("Unexpected format. Introduce: udpser port_number");
            System.exit(-1);
        }
        int accumulator_int = 0; 
        int ser_port = Integer.parseInt (args[0]); 
        try {
            DatagramSocket conect_ser_port = new DatagramSocket(ser_port);//CONNECT TO THE PORT RECEIVED BY ARG
            while(true){ //KEEP THE SERVER OPENED
                byte[] message = new byte[1000];
                DatagramPacket datagram_to_receive = new DatagramPacket (message, message.length);//CREATE THE DATAGRAM TO RECEIVE THE ANSWER FROM CLIENT
                //System.out.println("datagram size "+ datagram_to_receive.getLength());
                conect_ser_port.receive(datagram_to_receive);//receive the message in the datagram
                String received_data_string = new String(datagram_to_receive.getData());
                //System.out.println("received string "+received_data_string);
                LinkedList<String> received_data_list = new LinkedList<String>();

                //CREATE A LINKEDLIST WITH THE NUMBERS OF THE STRING RECEIVED FROM THE CLIENT
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(received_data_string);
                while (m.find()) {
                    received_data_list.add(m.group());
                }
                //System.out.println(received_data_list);
                
                //CATCH EACH NUMBER OF THE LINKED LIST
                for (int i=0;i<received_data_list.size();i++){
                    //System.out.println(received_data_list.get(i)+ "geti");
                    int eachnum_int = Integer.parseInt(received_data_list.get(i));
                    accumulator_int = accumulator_int + eachnum_int;
                    System.out.println("Accumulator is "+accumulator_int);

                }

                int cli_port = datagram_to_receive.getPort();//TAKE THE CLIENT PORT FROM THE DATAGRAM USED TO RECEIVE INFO
                InetAddress cli_ip = datagram_to_receive.getAddress();//TAKE THE IP FROM THE DATAGRAM USED TO RECEIVE INFO

                String accumulator_string = Integer.toString(accumulator_int); 
                byte[] accumulator = accumulator_string.getBytes();

                DatagramPacket datagram_to_send = new DatagramPacket (accumulator, accumulator.length, cli_ip, cli_port);//CREATE A DATAGRAM TO SEND FROM SERVER TO CLIENT
                conect_ser_port.send(datagram_to_send);//SEND THE FINAL VALUE OF THE ACCUMULATOR
            }
        }catch (SocketException ex){ 

        }catch (IOException e){

    }



}

}