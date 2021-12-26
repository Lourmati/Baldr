/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 *
 * @author Cesar
 */
public class OuvertureFichier {
    private static HashMap<String, File> a;

    public OuvertureFichier() {
        a = new HashMap<>();
    }
    public File Ouverture(String fichier) throws URISyntaxException{
      
        if(a.containsKey(fichier)==true){
           return  (File) a.get(fichier);
        }else{
           
        File file;
        file = new File(getClass().getResource(fichier).toURI());
        a.put(fichier,file);
            System.out.println(a.get(fichier));
                return file;
        }
       
    }
    
}
