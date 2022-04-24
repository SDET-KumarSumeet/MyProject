package com.java.programs;

import java.io.*;
import java.util.*;

public class StringToken {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if(!scan.hasNext())
        {
            System.out.println(0);
        }
        else{
        String s = scan.nextLine();
        String[] myArr = s.trim().split("[ !,?._'@]+");
        scan.close();
        System.out.println(myArr.length);
        ArrayList<String> List = new ArrayList<String>(Arrays.asList(myArr));
        for(String str: List)
        {
            System.out.println(str);
        }
        }
    }
}

