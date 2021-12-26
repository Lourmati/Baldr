 /*
 * SaveAndRestore.java
 *
 * Created on 14 mai 2007, 11:00
 * $Id: SaveAndRestore.java 200 2007-05-26 23:02:02Z zeta $
 */
package ca.qc.bdeb.baldr.noyau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Une classe responsable de sauvegarder et de restaurer les tâches des
 * componsantes du programme qui sont sauvegardable.
 *
 * @see Savable
 * @author zeta
 */
public class SaveAndRestore {

    /**
     * L'objet à sauvegarder.
     */
    private Savable obj;

    /**
     * Créée une nouvelle instance de sauvegarde.
     *
     * @param object L'objet à sauvegarder
     */
    public SaveAndRestore(Savable object) {
        obj = object;
    }

    /**
     * Pour la sauvegarde si contient un sommaire
     * @param object l'objet`a sauvegarder
     * @param projet le projet courant
     */
    
    /**
     * Remplace des caractères spécifiques à l'aide du code XML approprié.
     *
     * @param str La chaine à corriger
     * @return La chaine corrigée
     */
    public static String escape(String str) {
        return str.replace("&", "&amp;").replace("<", "&lt;");

    }

    /**
     * Sauvegarde l'élément dans le fichier spécifié, selon le format choisi.
     *
     * @param fichier Le fichier dans lequel sauvegarder
     * @throws ca.qc.bdeb.baldr.noyau.RienASauvegarderException
     */
    public void save(File fichier) throws RienASauvegarderException {
        if (obj == null) {
            throw new RienASauvegarderException();
        } else {

            String informations = creationInformationSauvegarder();
            
            BufferedWriter out = ouvrirEnEcriture(fichier);

            ecritureDonnee(out, informations);
        }
    }

    /**
     * Ouvre un fichier dans le but d'y écrire, selon un format donné.
     *
     * @param fichier Le fichier à ouvrir
     * @return Un écrivain en mode tampon
     */
    private static BufferedWriter ouvrirEnEcriture(File fichier) {
        OutputStream stream;

        BufferedWriter out = null;

        try {
            stream = new FileOutputStream(fichier);

            out = new BufferedWriter(new OutputStreamWriter(stream));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return out;
    }

    /**
     * Crée les informations de sauvegarde en XML.
     *
     * @return Les informations de sauvegarde, en XML
     */
    private String creationInformationSauvegarder() {
        StringBuilder str = new StringBuilder();

        // TODO(pascal) : Ça serait bien d'implémenter une indentation du
        // code XML généré, question de rendre le fichier de sauvegarde
        // plus lisible par un être humain!
        str.append("<?xml version=\"1.0\" ?>\n");
        str.append("<save>\n");
        str.append(obj.toXml()); // Écrit les onglets, réf. : Noyau.toXml()        
        str.append("</save>\n");

        return str.toString();
    }

    /**
     * Écrit les données fournies dans le fichier de sortie fourni.
     *
     * @param out Le fichier de sortie
     * @param informations Les données à écrire
     */
    private static void ecritureDonnee(
            BufferedWriter out, String informations) {
        try {
            out.write(informations);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Restaure l'application dans un état donné depuis un fichier de
     * sauvegarde.
     *
     * @param fichier Le fichier depuis lequel se retaurer
     */
    public void restore(File fichier) {

        BufferedReader reader = ouvrirEnLecture(fichier);
        Node node = lectureDonnee(reader);

        if (node != null && "save".equals(node.getNodeName())) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                obj.fromDom(node.getChildNodes().item(i));
                //System.out.println(node.getChildNodes().item(i) + "*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&");
            }
        }
        
    }

    /**
     * Ouvre un fichier en lecture, selon un format donné.
     *
     * @param fichier Le fichier à ouvrir
     * @return Le flux du fichier, en mode tampon
     */
    private static BufferedReader ouvrirEnLecture(File fichier) {
        BufferedReader reader = null;

        try {
            FileInputStream file = new FileInputStream(fichier);
            InputStream st = file;

            reader = new BufferedReader(new InputStreamReader(st));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return reader;
    }

    /**
     * Lit les données d'un fichier XML et renvoie la racine du document.
     *
     * @param reader La source d'où lire le fichier XML
     * @return La racine du document XML lu
     */
    private Node lectureDonnee(BufferedReader reader) {
        Node node = null;

        try {
            DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new InputSource(reader));

            node = doc.getChildNodes().item(0);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.err.println("[ERREUR] Impossible d'ouvrir le fichier XML : "
                    + ex.getLocalizedMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                System.err.println("[ERREUR] Impossible de fermer le fichier "
                        + "en lecture : " + ex.getLocalizedMessage());
            }
        }

        return node;
    }

    /**
     * Exportation d'un tâche dans un fichier déjà créé.
     *
     * @param s Une tâche à sauvegarder
     * @param fichier Le fichier dans lequel exporter la tâche
     */
    public static void exportTo(Savable s, File fichier) {

        StringBuilder str = new StringBuilder();
        BufferedReader reader = ouvrirEnLecture(fichier);

        String temp;
        try {
            while ((temp = reader.readLine()) != null) {
                str.append(temp);
                str.append("\n");
            }

            str.insert(str.indexOf("</save>"), s.toXml());
        } catch (IOException ex) {
            return;
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        BufferedWriter out = ouvrirEnEcriture(fichier);

        ecritureDonnee(out, str.toString());
    }

}
