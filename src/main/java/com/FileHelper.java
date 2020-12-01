package com;
import com.encrypty.SecretKey;

import java.io.*;

public class FileHelper {
    private String fileName;

    public FileHelper(){

    }

    public FileHelper(String fileName){
        this.fileName=fileName;
    }

    /*
     * ��person���󱣴浽�ļ���
     * params:
     * 	p:person�����
     */
    public void saveObjToFile(SecretKey sk){
        try {
            //д�������Ķ���
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));

            oos.writeObject(sk);                 //��Person����pд�뵽oos��

            oos.close();                        //�ر��ļ���
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * ���ļ��ж������󣬲��ҷ���Person����
     */
    public SecretKey getObjFromFile(){
        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));

            SecretKey sk=(SecretKey)ois.readObject();              //��������

            return sk;                                       //���ض���
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
