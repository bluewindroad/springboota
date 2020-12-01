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
     * 将person对象保存到文件中
     * params:
     * 	p:person类对象
     */
    public void saveObjToFile(SecretKey sk){
        try {
            //写对象流的对象
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));

            oos.writeObject(sk);                 //将Person对象p写入到oos中

            oos.close();                        //关闭文件流
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 从文件中读出对象，并且返回Person对象
     */
    public SecretKey getObjFromFile(){
        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));

            SecretKey sk=(SecretKey)ois.readObject();              //读出对象

            return sk;                                       //返回对象
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
