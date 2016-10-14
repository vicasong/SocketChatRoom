package test;

import com.vica.ui.ClientForm;
import com.vica.ui.ServerForm;

import java.util.Scanner;

/**
 * Created by Vica-tony on 10/14/2016.
 */
public class Program {

    public static void main(String[] args) {
        ServerForm serverForm = new ServerForm();
        serverForm.show();
        String line = null;
        Scanner scanner = new Scanner(System.in);
        while ((line = scanner.nextLine())!=null){
            if(line.toLowerCase().equals("exit")){
                System.exit(0);
            }else{
                new ClientForm().show();
            }
        }
    }


}
