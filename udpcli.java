import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class udpcli{
    public static void main(String[] args) {
        if (args.length!=2){
            System.out.println ("Unexpected format. Introduce: udpcli ip_address port_number");
            System.exit(-1);
        }
            
        try{
            InetAddress ip_address = InetAddress.getByName(args[0]);
                    //System.out.println (ip_address);
            int ser_port_number = Integer.parseInt (args[1]);
                    //System.out.println (ser_port_number);

            System.out.print ("Write numbers separated with blanks, press intro or '0' to end the message: ");
            Scanner read = new Scanner(System.in);
            String string_message = read.nextLine(); //CREATE AN STRING WITH THE ANSWER OF THE USER
            LinkedList<String> cutted_numbers_list=new LinkedList<String>();
            LinkedList<String> numbers_list = new LinkedList<String>();

            //CREATE A LINKED LIST WITH ALL THE NUMBERS WRITTEN IN THE TERMINAL
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(string_message);
            while (m.find()) {
                numbers_list.add(m.group());
            }

            //System.out.println(numbers_list);

            //LOOK IF THE FIRST NUM OF THE LIST IS A 0
            if (Integer.parseInt(numbers_list.get(0)) == 0) //IF THE FIRST NUMBER IS 0 ENDS
                System.exit(-1);

            //CUT THE ORIGINAL LIST WHEN FIND A 0 AND CREATE A CUTTED VERSION
            for (int i=0;i<numbers_list.size();i++){
                //System.out.println(numbers_list.get(i));
                if (Integer.parseInt(numbers_list.get(i)) != 0)
                    cutted_numbers_list.add(numbers_list.get(i));
                else
                    break;
            }
            //System.out.println("Cutted list size"+cutted_numbers_list.size());

            //CONVERT THE CUTTED LINKEDLIST INTO BYTE[] FOR SEND 
            String numbers_str = ("");
            for (int j=0;j<cutted_numbers_list.size();j++){
                int eachnum_int = Integer.parseInt(cutted_numbers_list.get(j)); //CONVERT EACH NUMBER OF THE CUTTED_LIST IN INT
                //System.out.println(eachnum_int);
                numbers_str += eachnum_int+" ";//ADD TO AN EMPTY STRING ALL THE NUMBERS ONE BY ONE
                //System.out.println("String enviada"+numbers_str);
            }
            byte[] message = new byte[numbers_str.length()];
            message=numbers_str.getBytes();// CONVERT THE STRING INTO A BYTE[]

            //SEND
            DatagramSocket freeport = new DatagramSocket(); //SEARCHS A FREE PORT
            DatagramPacket datagram_to_send = new DatagramPacket(message, message.length, ip_address, ser_port_number);//CREATE THE DATAGRAM TO SEND TO THE SERVER
            freeport.send(datagram_to_send); //SEND THE DATAGRAM WITH THE MESSAGE


            //RECEIVE
            freeport.setSoTimeout(10000);//THE CLIENT WAITS A MAX OF 10 SEC FOR RECEIVING AN ANSWER FROM THE SERVER
            byte[] result = new byte[1000];
            DatagramPacket datagram_to_receive = new DatagramPacket (result, result.length); //CREATE A DATAGRAM TO RECEIVE FROM SERVER
            freeport.receive(datagram_to_receive); //RECEIVE THE RESULT OF THE ACCUMULATOR
            String accumulator = new String(datagram_to_receive.getData());
            System.out.println("The final value of the accumulator is "+ accumulator);
            freeport.close();
            

        } catch (SocketException e){

        } catch (SocketTimeoutException e){
            System.out.println("Timeout has happened");
        }catch (IOException e){

        }   
    
    }
}