package com.java.programs;

import java.io.*;
import java.util.*;

public class subArray {
    
    public static int subArrCount(int myArray[]){
        int count=0, sum=0;
        for(int i=0; i<myArray.length; i++){
           for(int j=i; j<myArray.length; j++){
               sum=0;
               for(int k=i; k<=j; k++){
                   sum = sum + myArray[k];
               }
               if(sum<0)count++;
           }            
        }
        
        return count;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);
        int size = sc.nextInt();
        int myArr[] = new int[size];
        for(int i=0; i<size; i++){
            myArr[i]=sc.nextInt();
        }
        sc.close();
        System.out.println(subArrCount(myArr));
    }
}