package com.example;

import com.rapplogic.xbee.api.ErrorResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.util.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 *
 */
public class XBeeTest {

    public static double charToDouble(int[] data){


   //Double num= Double.parseDouble(new String(ByteUtils.toBase16(data)) );
        ByteBuffer byteBuffer= ByteBuffer.allocate(data.length*4);
        IntBuffer intBuffer= byteBuffer.asIntBuffer();
        intBuffer.put(data);
        byte[] dataBites= byteBuffer.array();
        
        double num = ByteBuffer.wrap(dataBites).getDouble();
        return num;
    }
    public static void main(String[] args){
        XBee xbee = new XBee();
        try {
            xbee.open("COM6",115200);
            int contador=0;


            while (true) {
                XBeeResponse response = xbee.getResponse();
                try{
                ZNetRxResponse ioSample = (ZNetRxResponse) response;
                
                contador++;
               // System.out.println("We received a sample from " +"Numero:"+contador+"// "+ ByteUtils.toBase16(ioSample.getData()));
                String[] head=(ByteUtils.toBase16(ioSample.getData())).split(",");


               if(ioSample.getData().length>19 && head[0].equals("0x7e") ){
                   //System.out.println("We received a sample from " +"Numero:"+contador+"// "+ ByteUtils.toBase16(ioSample.getData()));
                   // System.out.println(ioSample.getData().length);
                    int[] payload=new int[ioSample.getData().length-19];
                    for(int i=18;i<ioSample.getData().length-1;i++){
                        payload[i-18]=ioSample.getData()[i];
                    }
                    String msje=ByteUtils.toString(payload);
                    System.out.println(msje);
                }

            }catch (ClassCastException e){
                    ErrorResponse error = (ErrorResponse)response;
                    System.out.println(e.toString());
                }

            }
        } catch (XBeeException e) {
            e.printStackTrace();
        }finally {
            xbee.close();
        }
    }
}
