package com.example.miji.myapplication;

import android.util.Log;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class readData {

    public readData(node input[]) {

        BufferedReader bReader = null;

        int colNum=0;
        Log.e("tag", "read data start");
        try {
            String s;
            Log.e("tag", "try read data start");

            File file = new File("data.csv");
            bReader = new BufferedReader(new FileReader(file));
            Log.e("tag", "try read data start");


            // 더이상 읽어들일게 없을 때까지 읽어들이게 합니다.
            while((s = bReader.readLine()) != null) {
                String[] a = s.split(",");
                Log.e("tag", Arrays.toString(a));

                input[colNum] = new node();
                input[colNum].ClassName = a[0];

                for(int i=0;i<18;i++) {
                    if (a[1+i]== null) {
                        input[colNum].monday[i] = "empty";
                        Log.e("tag", "null");
                    }
                    else input[colNum].monday[i] = a[1+i];
                }

                for(int i=0;i<18;i++) {
                    if (a[19+i]== null) input[colNum].tuesday[i] = "empty";
                    else input[colNum].tuesday[i] = a[19+i];
                }

                for(int i=0;i<18;i++) {
                    if (a[37+i]== null) input[colNum].wednesday[i] = "empty";
                    else input[colNum].wednesday[i] = a[37+i];
                }

                for(int i=0;i<18;i++) {
                    if (a[55+i]== null) input[colNum].thursday[i] = "empty";
                    else input[colNum].thursday[i] = a[55+i];
                }

                for(int i=0;i<18;i++) {
                    if (a[73+i]== null) input[colNum].friday[i] = "empty";
                    input[colNum].friday[i] = a[73+i];
                }
                colNum++;
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bReader != null) bReader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        // System.out.println(input[20].address);
    }
}
