package com.xf.zhang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.BitSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootHttpsApplicationTests {



    @Test
    public void contextLoads() {

        //int [] array = new int [] {1,2,3,4,0,5,8,9,126};
        String string="9080106013051";
        BitSet bitSet  = new BitSet(5);
        //将数组内容组bitmap
        for(int i=string.length()-1,k=0;i>=0;i--)
        {
           // System.out.println(i+"--->"+string.charAt(k++));
            if((Integer.parseInt(String.valueOf(string.charAt(i)))!=0)){
                System.out.println(i+"<xxxx>"+string.charAt(i));
                bitSet.set(i);
            }

        }
        System.out.println(bitSet.length());
        System.out.println(bitSet.size());
        System.out.println(bitSet.get(11));
        int j=0;
        for(int i=0;i< bitSet.length();i++){
            if(bitSet.get(i)){
                j++;

            }
            System.out.println(bitSet.get(i)+"<-------");
        }
        System.out.println(j);
    }

    @Test
    public void stringANDChar() {
        String str="1222222";
        System.out.println(str);
        char []chars=str.toCharArray();
        for (char c:chars){
           System.out.print(c+"---");
        }
        System.out.println();
        System.out.println("-----------------");
        chars[3]='3';
        StringBuffer SB=new StringBuffer();
        for (char c:chars){
            System.out.print(c+"---");
            SB.append(c);
        }
        System.out.println();
        System.out.println(SB.toString());

        int number=1;
        char c=(number+"").charAt(0);
        System.out.println(c);
    }

    @Test
    public void stringANDBitMap() {
       // BitSet

    }

}

