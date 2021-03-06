/*
 * Task.java
 *
 * Created on 14 avril 2007, 18:20
 *$Id: Task.java 254 2007-09-23 15:21:42Z zeta $
 */
package ca.qc.bdeb.baldr.noyau;

import ca.qc.bdeb.baldr.formattage.CommentParser;
import ca.qc.bdeb.baldr.formattage.ExtrairePDF;
import ca.qc.bdeb.baldr.ihm.Observation;
import static ca.qc.bdeb.baldr.ihm.Observation.*;
import ca.qc.bdeb.baldr.ihm.WindowBaldr;
import ca.qc.bdeb.baldr.main.Main;
import ca.qc.bdeb.baldr.utils.Observable;
import ca.qc.bdeb.baldr.utils.Observateur;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.swing.text.Element;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.queryParser.QueryParserConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Représente une tâche d'analyse dans le programme.
 *
 * @author zeta
 */
public class Task implements Savable, Observable, Cloneable {

    /**
     * Indique si le thread d'analyse doit arrêter son travail.
     */
    private boolean stopNow;

    /**
     * Le rapport de l'analyse.
     */
    private String rapport;

    /**
     * Le nom de l'analyse.
     */
    private String titre;

    /**
     * La liste des taches qui composent une analyse sommaire
     */
    protected List<Task> tachesComposantes = null;

    /**
     * Les fichiers de l'analyse.
     */
    protected List<File> fichiersAnalyse;

    /**
     * Les fichiers originaux de l'analyse (la liste de tous les fichiers visés
     * par cette tâche).
     */
    private List<File> fichiersOriginaux;

    /**
     * Les préférences associées au projet.
     */
    private GestionnairePreferences prefs;

    /**
     * Les résultats de l'analyse.
     */
    private MatriceTriangulaire resultatsAnalyse;
    private Map<File, Long> precalculatedFiles;
    private Map<FilePair, Long> precalculatedPairs;
    private List<Observateur> observateurs;

    /**
     * Les différentes sources couvertes par cette tâche.
     */
    private Map<File, List<File>> sources;

    /**
     * Indique si l'analyse doit se faire par concaténation ou non.
     */
    private boolean analyseConcatenation = false;
    private boolean analyseExtrairePDF = false;
    private boolean analyseExtraireImagePDF = false;
    private boolean analysePreview = false;

    // Préférances sur le type d'analyse
    private boolean enleverCommentaires = false;
    private boolean enleverWhitespaces = false;

    /**
     * S'il n'y a qu'un seul ancêtre commun à tous les fichiers, est un tableau
     * d'un élément qui correspond à cet ancêtre commun. Sinon, contient les
     * multiples ancêtres communs (sous Windows, ce serait les multiples
     * racines, par exemple C:, D:, etc.).
     */
    private File[] filesCommonAncestors = null;

    private float state;
    private float medianeErr;
    /**
     * Vrai si l'interface a restauré cette analyse (par exemple, après un
     * import).
     */
    private boolean estRestauree;

    /**
     * Le thread qui doit faire une analyse.
     */
    private Thread thread;

    private boolean modifie = false;

    private boolean analyseEnCours;

    private List<String> listFileMD5 = new ArrayList<String>();
    private List<String> optionAnalyse = new ArrayList<String>();
    private List<Boolean> optionAnalyseBool = new ArrayList<Boolean>();

    private ExtrairePDF pdfExtractore;

    /**
     * Contient la méthode à éxécuter à chaque éxécution de l'analyse.
     */
    private final Runnable analysisAlgorithm = new Runnable() {
        @Override
        public void run() {
            analyseEnCours = true;

            if (analyseConcatenation && !sources.isEmpty()) {
                faireAnalyseConcatenation();
            } else {
                faireAnalyseNormale();
            }

            state = 1;
            analyseEnCours = false;
            modifie = true;
            aviserObservateurs(ANALYSE_TERMINEE, this);
        }
    };

    /**
     * Lit dans le fichier xml ouvert si le fichier est sommaire avec l'attribut
     * sommaire de "< titre >"
     *
     * @param node
     * @return
     */
    public boolean xmlEstSommaire(Node node) {
        String sommaire = "";
        boolean xmlSommaire = false;
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeName() != null) {
                switch (node.getChildNodes().item(i).getNodeName()) {
                    case "titre":
                        titre = node.getChildNodes().item(i).getTextContent();
                        sommaire = node.getChildNodes().item(i).getAttributes().getNamedItem("sommaire").getNodeValue();
                        if (sommaire.equals("True")) {
                            xmlSommaire = true;
                        }
                }
            }
        }
        return xmlSommaire;
    }

    /**
     * Paire de deux fichiers compressés ensemble.
     */
    public class FilePair {

        private File file1;
        private File file2;

        private FilePair(File file1, File file2) {
            this.file1 = file1;
            this.file2 = file2;
        }

        public File getFile1() {
            return file1;
        }

        public File getFile2() {
            return file2;
        }

        @Override
        public boolean equals(Object that) {
            boolean retour = false;
            if (that instanceof FilePair) {
                retour = this.file1.equals(((FilePair) that).file1)
                        && this.file2.equals(((FilePair) that).file2);
            }
            return retour;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.file1);
            hash = 83 * hash + Objects.hashCode(this.file2);
            return hash;
        }
    }

    /**
     * Créée une nouvelle instance de Task.
     */
    public Task() {
        titre = "Analyse";
        rapport = "";
        fichiersAnalyse = new ArrayList();
        fichiersOriginaux = new ArrayList();
        precalculatedFiles = new HashMap();
        precalculatedPairs = new HashMap();
        observateurs = new ArrayList();
        sources = new HashMap();
        estRestauree = false;
        analyseEnCours = false;
        pdfExtractore = new ExtrairePDF();
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        super.clone();

        Task copie = new Task();

        copie.rapport = rapport;
        copie.titre = titre + " - copie";

        if (fichiersAnalyse != null) {
            copie.fichiersAnalyse = new ArrayList(fichiersAnalyse);
        }

        if (fichiersOriginaux != null) {
            copie.fichiersOriginaux = new ArrayList(fichiersOriginaux);
        }

        copie.prefs = prefs;

        if (resultatsAnalyse != null) {
            copie.resultatsAnalyse = resultatsAnalyse.clone();
        }

        if (precalculatedFiles != null) {
            copie.precalculatedFiles = new HashMap(precalculatedFiles);
        }

        if (precalculatedPairs != null) {
            copie.precalculatedPairs = new HashMap(precalculatedPairs);
        }

        if (observateurs != null) {
            copie.observateurs = new ArrayList(observateurs);
        }

        if (sources != null) {
            copie.sources = new HashMap(sources);
        }

        if (filesCommonAncestors != null) {
            copie.filesCommonAncestors = filesCommonAncestors.clone();
        }

        copie.state = state;
        copie.medianeErr = medianeErr;
        copie.estRestauree = estRestauree;

        return copie;
    }

    public List<File> getFiles() {
        return fichiersAnalyse;
    }

    /**
     * Arrête le thread d'analyse.
     */
    public void stopAnalysis() {
        stopNow = true;

        try {
            thread.interrupt();
        } catch (Exception ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }

        aviserObservateurs();
    }

    /**
     * Permet de modifier les fichiers à partir d'une liste de fichiers
     * existante.
     *
     * @param fichiers
     */
    public void setFichiers(List<File> fichiers) {
        setFichiers(fichiers, true);
    }

    /**
     * Permet de modifier l'attribut files à partir d'un tableau de fichiers
     * existant, en spécifiant si les résultats doivent être réinitialisés ou
     * non.
     *
     * @param nouveauxFichiers
     * @param resetRes Si les résultats doivent être réinitialisés
     */
    public void setFichiers(List<File> nouveauxFichiers, boolean resetRes) {
        if (nouveauxFichiers != null) {
            fichiersAnalyse = new ArrayList(nouveauxFichiers);
            fichiersOriginaux = new ArrayList(nouveauxFichiers);
        } else {
            fichiersAnalyse = new ArrayList();
            fichiersOriginaux = new ArrayList();

            return; // Pas de traitement nécessaire.
        }

        if (fichiersAnalyse != null && fichiersAnalyse.size() > 0) {
            File commonAncestor = extractCommonAncestor();

            if (commonAncestor == null) {
                filesCommonAncestors = findAllRoots();
            } else {
                filesCommonAncestors = new File[]{commonAncestor};
            }

            if (resetRes) {
                resultatsAnalyse
                        = new MatriceTriangulaire(fichiersAnalyse.size());
            }
        }

        modifie = true;
    }

    /**
     * Dans le tableau de fichiers, donne la partie du chemin qui est commune à
     * tous les fichiers.
     *
     * @return Objet {@link java.io.File} représentant ce chemin, ou
     * {@code null} s'il n'en existe pas.
     */
    private File extractCommonAncestor() {
        List<String> pathSimilaire = new ArrayList();
        String[] pathInitial = fichiersAnalyse.get(0).getAbsolutePath()
                .split(Main.regexFileSeparator);
        boolean pareil = true;

        // Vérifier le chemin nom par nom
        for (int i = 0; i < pathInitial.length - 1 && pareil; i++) {
            pareil = true;

            for (File file : fichiersAnalyse) {
                String[] pathTest
                        = file.getAbsolutePath().split(Main.regexFileSeparator);

                if (pathTest.length <= i
                        || !pathInitial[i].equals(pathTest[i])) {
                    pareil = false;
                }
            }

            if (pareil) {
                pathSimilaire.add(pathInitial[i]);
            }
        }

        StringBuilder sb = new StringBuilder();

        if (Main.isUnix || Main.isMac) {
            sb.append(File.separator);
        }

        for (String string : pathSimilaire) {
            sb.append(string);
            sb.append(File.separator);
        }

        if (pathSimilaire.isEmpty()) {
            return null;
        } else {
            return new File(sb.toString());
        }
    }

    private File[] findAllRoots() {
        Set<File> roots = new HashSet();

        for (File file : fichiersOriginaux) {
            roots.add(file.toPath().getRoot().toFile());
        }

        return roots.toArray(new File[0]);
    }

    private OutputStream makeComp(OutputStream compresseurFichier,
            File file) throws IOException {
        FileInputStream lecteurFichier = new FileInputStream(file);
        BufferedInputStream bufferIn = new BufferedInputStream(lecteurFichier);

        // La plupart des systèmes de fichiers fonctionnent par blocs
        // de 4096 ou 8192 octets. On prend une chance avec le bloc supérieur,
        // dans le but d'améliorer les performances par rapport à d'autres
        // tailles, puisque cette étape du traitement est la plus lourde.
        byte[] tampon = new byte[8192];
        int len;
        boolean extractPdfTexte = false;
        boolean extractPdfImage = false;

        boolean b = false;
        if (FilenameUtils.getExtension(file.getPath()).equals("pdf")) {
            b = true;
        }
        if (b && (Boolean) prefs.readPref("extract_image_pdf", false)) {
            String ExtractImagePdf = pdfExtractore.ExtractImagePdf(file);
            compresseurFichier.write(ExtractImagePdf.getBytes());
            extractPdfImage = true;

        }
        if (b && (Boolean) prefs.readPref("EXTRACT_PDF", false)) {
            compresseurFichier.write(pdfExtractore.ExtrairePDF(file).getBytes());
            extractPdfTexte = true;
        } else if (!extractPdfImage || !extractPdfTexte) {
            // Enlever commentaires du fichier d'analyse

            if (enleverCommentaires) {

                enleverCommentaires(file, bufferIn, compresseurFichier, tampon);

                // Enlever lignes blanches du fichier d'analyse
            }
            if (enleverWhitespaces) {

                enleverWhitespaces(file, bufferIn, compresseurFichier, tampon);
                // Analyse du fichier normal
            } else {

                while ((len = bufferIn.read(tampon)) > 0) {
                    compresseurFichier.write(tampon, 0, len);
                }
            }
        }
        bufferIn.close();

        return compresseurFichier;
    }

    private void enleverCommentaires(File file, BufferedInputStream bufferIn,
            OutputStream compresseurFichier, byte[] tampon) throws IOException {

        int codeChar;
        String output;

        byte[] res;
        int pos = 0;

        CommentParser parser = new CommentParser(file);
        while ((codeChar = bufferIn.read()) != -1) {
            parser.lireCaractere((char) codeChar);
            output = parser.retournerCaractereChaine();

            if (!enleverWhitespaces) {
                if (codeChar == 10) {
                    output += "\n";
                }
            }
            if (!output.isEmpty()) {
                res = output.getBytes();

                if (pos + res.length >= tampon.length) {
                    compresseurFichier.write(tampon, 0, pos);
                    compresseurFichier.write(res);

                    pos = 0;
                } else {

                    for (int i = 0; i < res.length; i++) {
                        tampon[pos++] = res[i];
                    }

                }
            }
        }
        if (pos > 0) {
            compresseurFichier.write(tampon, 0, pos + 1);
        }

    }

    private void enleverWhitespaces(File file, BufferedInputStream bufferIn,
            OutputStream compresseurFichier, byte[] tampon) throws IOException {

        String ligne = "";

        Scanner scannerIn = new Scanner(bufferIn);

        while (scannerIn.hasNext()) {

            ligne = scannerIn.nextLine();

            if (!(ligne.trim().isEmpty())) {
                tampon = ligne.getBytes();
                compresseurFichier.write(tampon);
            }
        }
    }

    /**
     * Détermine le poids d'un fichier gunzippé seul.
     *
     * @param file Le fichier à anlyser.
     * @return La taille du fichier gunzippé.
     * @throws IOException
     * @throws FileNotFoundException
     */
    private long calculateGZipSize(File file)
            throws IOException, FileNotFoundException {
        long ret = 0;

        if (!enleverCommentaires && precalculatedFiles.containsKey(file)) {
            return precalculatedFiles.get(file);
        }

        OutputStreamSizer calcTaille = new OutputStreamSizer();
        GZIPOutputStream compresseurFichier = new GZIPOutputStream(calcTaille);

        makeComp(compresseurFichier, file);

        compresseurFichier.close();

        ret = calcTaille.getSize();

        calcTaille.close();

        if (!enleverCommentaires) {
            precalculatedFiles.put(file, ret);
        }

        return ret;
    }

    /**
     * Détermine le poids d'une paire de fichiers gunzippés ensembles.
     *
     * @param file1 Le premier fichier à analyser.
     * @param file2 Le deuxième fichier à analyser.
     * @return Le poids des deux fichiers gunzippés ensembles.
     * @throws IOException
     * @throws FileNotFoundException
     */
    private long calculateGZipSize(File file1, File file2)
            throws IOException, FileNotFoundException {
        long ret = 0;
        FilePair pair = new FilePair(file1, file2);

        if (!enleverCommentaires && precalculatedPairs.containsKey(pair)) {
            return precalculatedPairs.get(pair);
        }

        OutputStreamSizer compression = new OutputStreamSizer();
        BufferedOutputStream fichierBuffer
                = new BufferedOutputStream(compression);
        GZIPOutputStream compresseurFichier
                = new GZIPOutputStream(fichierBuffer);

        makeComp(compresseurFichier, file1);
        makeComp(compresseurFichier, file2);

        compresseurFichier.close();

        ret = compression.getSize();

        compression.close();

        if (!enleverCommentaires) {
            precalculatedPairs.put(pair, ret);
        }

        return ret;
    }

    /**
     * Détermine le poids d'un fichier gunzippé seul.
     *
     * @return Le poids du fichier gunzippé.
     * @throws IOException
     * @throws FileNotFoundException
     */
    private long calculateGZipSize(List<File> source)
            throws IOException, FileNotFoundException {
        long ret = 0;

        OutputStreamSizer compression = new OutputStreamSizer();
        GZIPOutputStream compresseurFichier = new GZIPOutputStream(compression);

        for (File fichier : source) {
            makeComp(compresseurFichier, fichier);
        }

        compresseurFichier.close();

        ret = compression.getSize();

        compression.close();

        return ret;
    }

    /**
     * Détermine le poids d'une paire de fichiers gunzippés ensembles.
     *
     * @return Le poids des deux fichiers gunzippés ensembles.
     * @throws IOException
     * @throws FileNotFoundException
     */
    private long calculateGZipSize(List<File> source1, List<File> source2)
            throws IOException, FileNotFoundException {
        long ret = 0;

        OutputStreamSizer compression = new OutputStreamSizer();
        BufferedOutputStream fichierBuffer
                = new BufferedOutputStream(compression);
        GZIPOutputStream compresseurFichier
                = new GZIPOutputStream(fichierBuffer);

        for (File fichier : source1) {
            makeComp(compresseurFichier, fichier);
        }
        for (File fichier : source2) {
            makeComp(compresseurFichier, fichier);
        }

        compresseurFichier.close();

        ret = compression.getSize();

        compression.close();

        return ret;
    }

    /**
     * Retire plusieurs fichiers de l'analyse
     *
     * @param fichiers les fichiers à retirer
     */
    public void retirerFichiers(List<File> fichiers) {
        for (File fichier : fichiers) {
            retirerFichierEtMettreAJourMatrice(fichier);
        }

    }

    private void faireAnalyseNormale() {
        int nbrFichiers = fichiersAnalyse.size();
        //ListeResultatCalcule resultat = new ListeResultatCalcule();
        long tailleFichier1GZip = 0;
        long tailleFichier2GZip = 0;
        long tailleFichier1et2GZip = 0;

        for (int i = 0; i < nbrFichiers; i++) {
            try {
                tailleFichier1GZip = calculateGZipSize(fichiersAnalyse.get(i));
            } catch (IOException | IndexOutOfBoundsException e) {
            }

            for (int j = 0; j < i; j++) {
                // Annulation ?
                if (stopNow) {
                    return;
                }

                if (!verifierSiMemeSource(fichiersAnalyse.get(i),
                        fichiersAnalyse.get(j))) {
                    try {
                        tailleFichier2GZip
                                = calculateGZipSize(fichiersAnalyse.get(j));
                    } catch (IOException ex) {
                    }

//                    try {
//                        if (resultat.listResultat.contains(calculateGZipSize(fichiersAnalyse.get(i))) && resultat.listResultat.contains(j)) {
//                             
//                        }
//                    } catch (IOException ex) {
//                        Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    if (resultatsAnalyse.getRes(i, j) == -1) {
                        try {
                            tailleFichier1et2GZip
                                    = calculateGZipSize(fichiersAnalyse.get(i),
                                            fichiersAnalyse.get(j));
                        } catch (IOException e) {
                        }

                        resultatsAnalyse.setRes(i, j,
                                java.lang.Math.min(1F, 1F
                                        - (float) (tailleFichier1GZip
                                        + tailleFichier2GZip
                                        - tailleFichier1et2GZip)
                                        / (float) java.lang.Math.max(
                                                tailleFichier1GZip,
                                                tailleFichier2GZip)));
                    }
                } else {
                    resultatsAnalyse.setRes(i, j, 0);
                }

                increaseState();
            }
        }
    }

    private void faireAnalyseConcatenation() {
        int nbrFichiers = sources.size();

        long tailleFichier1GZip = 0;
        long tailleFichier2GZip = 0;
        long tailleFichier1et2GZip = 0;

        for (int i = 0; i < nbrFichiers; i++) {
            try {
                tailleFichier1GZip
                        = calculateGZipSize(sources.get(fichiersAnalyse.get(i)));
            } catch (IOException e) {
            }

            for (int j = 0; j < i; j++) {
                // Annulation ?
                if (stopNow) {
                    return;             ///////////////////////////////////////////////////
                }

                if (!verifierSiMemeSource(fichiersAnalyse.get(i),
                        fichiersAnalyse.get(j))) {
                    try {
                        tailleFichier2GZip
                                = calculateGZipSize(sources.get(
                                        fichiersAnalyse.get(j)));
                    } catch (IOException ex) {
                    }

                    if (resultatsAnalyse.getRes(i, j) == -1) {
                        try {
                            tailleFichier1et2GZip = calculateGZipSize(
                                    sources.get(fichiersAnalyse.get(i)),
                                    sources.get(fichiersAnalyse.get(j)));
                        } catch (IOException e) {
                        }

                        resultatsAnalyse.setRes(i, j,
                                java.lang.Math.min(1F, 1F
                                        - (float) (tailleFichier1GZip
                                        + tailleFichier2GZip
                                        - tailleFichier1et2GZip)
                                        / (float) java.lang.Math.max(
                                                tailleFichier1GZip,
                                                tailleFichier2GZip)));
                    }
                } else {
                    resultatsAnalyse.setRes(i, j, 0);
                }

                increaseState();
            }
        }
    }

    /**
     * Crée un nouveau thread et le démarre.
     */
    public void lancerAnalyse() {
        if (prefs != null) {
            boolean cat = (Boolean) prefs.readPref("CONCATENATION", false);

            if (cat && !sources.isEmpty()) {
                fichiersAnalyse = new ArrayList(sources.keySet());
            } else {
                fichiersAnalyse = new ArrayList(fichiersOriginaux);
            }

            analyseConcatenation = cat;

            // Vérifier si options d'enlever les commentaires et activée
            enleverCommentaires = (Boolean) prefs.readPref("COMMENTAIRES", false);

            // Vérifier si options d'enlever les commentaires et activée
            enleverWhitespaces = (Boolean) prefs.readPref("WHITESPACES", false);
            System.out.println(enleverCommentaires);
        } else { // Pas de préférences gérées (par exemple : pendant un test).
            if (analyseConcatenation && !sources.isEmpty()) {
                fichiersAnalyse = new ArrayList(sources.keySet());
            } else {
                fichiersAnalyse = new ArrayList(fichiersOriginaux);
            }
        }

        state = 0;
        stopNow = false;
        resultatsAnalyse = new MatriceTriangulaire(fichiersAnalyse.size());
        thread = new Thread(analysisAlgorithm);

        analyseEnCours = true;
        thread.start();
    }

    private void setExRes(List<File> exfiles, float[][] exresults) {
        Map<File, Integer> exFilesTempo = new HashMap();

        for (int i = 0; i < exfiles.size(); i++) {
            exFilesTempo.put(exfiles.get(i), i);
        }

        List<File> fichiers = null;
        if (sources.size() > 0) {
            fichiers = new ArrayList<>(sources.keySet());
        } else {
            fichiers = fichiersAnalyse;
        }
        for (int i = 0; i < fichiers.size(); i++) {
            if (exFilesTempo.containsKey(fichiers.get(i))) { // old file
                // pas besoin de repasser avant
                for (int j = i; j < fichiers.size(); j++) {
                    if (exFilesTempo.containsKey(
                            fichiers.get(j))) { // old file
                        int indiceFile1, indiceFile2;
                        indiceFile1 = exFilesTempo.get(fichiers.get(i));
                        indiceFile2 = exFilesTempo.get(fichiers.get(j));
                        if (indiceFile2 > indiceFile1) {
                            int tempo;
                            tempo = indiceFile1;
                            indiceFile1 = indiceFile2;
                            indiceFile2 = tempo;
                        }

                        resultatsAnalyse.setRes(
                                i, j, exresults[indiceFile1][indiceFile2]);
                    }
                }
            }
        }
    }

    /**
     * Permet d'accéder aux résultats de l'analyse.
     *
     * @return Les résultats de l'analyse.
     */
    public MatriceTriangulaire getResults() {
        return resultatsAnalyse;
    }

    public float getRes(File fichier1, File fichier2) {
        int indiceResultatFichier1, indiceResultatFichier2;
        indiceResultatFichier1 = -1;
        indiceResultatFichier2 = -1;
        for (int i = 0; i < fichiersAnalyse.size(); ++i) {
            if (fichier1 == fichiersAnalyse.get(i)) {
                indiceResultatFichier1 = i;
            }
            if (fichier2 == fichiersAnalyse.get(i)) {
                indiceResultatFichier2 = i;
            }
            if (indiceResultatFichier2 != -1 && indiceResultatFichier1 != -1) {
                i = fichiersAnalyse.size();
            }
        }
        return resultatsAnalyse.getRes(indiceResultatFichier1,
                indiceResultatFichier2);
    }

    private void increaseState() {
        this.state += 1.0 / resultatsAnalyse.getNumAnalyse();
        aviserObservateurs(Observation.PROGRESS, null);
    }

    /**
     * Retire un fichier de la liste des fichiers, selon un fichier passé en
     * paramètres.
     *
     * @param fichier Le fichier à retirer.
     */
    //TODO: Separer en deux methodes et mettre a jour
    //le code dans les endroits ou elle est utiliser
    public void retirerFichierEtMettreAJourMatrice(File fichier) {
        int indexAnalyse = fichiersAnalyse.indexOf(fichier);
        int indexOriginale = fichiersOriginaux.indexOf(fichier);
        fichiersOriginaux.remove(indexOriginale);
        fichiersAnalyse.remove(indexAnalyse);
        resultatsAnalyse.enleverLigneEtColonne(indexAnalyse);

        modifie = true;
    }

    public void CacherFichierEtMettreAJourMatrice(File fichier) {
        int indexAnalyse = fichiersAnalyse.indexOf(fichier);
        resultatsAnalyse.enleverLigneEtColonne(indexAnalyse);
        fichiersAnalyse.remove(indexAnalyse);
        modifie = true;
    }

    /**
     * Retire des fichiers de l'analyse selon la différence entre les fichiers
     * originaux et les fichiers restants après la suppression.
     *
     * @param nouveauxFichiers Les fichiers restants.
     */
    //TODO: Separer en methodes
    public void mettreAJourFichiersEtMettreAJourMatrice(
            List<File> nouveauxFichiers) {
        if (state > 0) {
            if (analyseConcatenation && !sources.isEmpty()) {
                Map<File, List<File>> copieSources = new HashMap(sources);

                Object[] set = copieSources.keySet().toArray();

                setFichiers(nouveauxFichiers, false);
                regenererSources();

                int nombreSupprime = 0;
                for (int i = 0; i < set.length; i++) {
                    if (!sources.containsKey((File) set[i])) {
                        resultatsAnalyse.enleverLigneEtColonne(
                                i - nombreSupprime);
                        nombreSupprime++;
                    }
                }
            } else {
                List<File> copieFichiers = new ArrayList(fichiersOriginaux);

                setFichiers(nouveauxFichiers, false);
                regenererSources();

                int nombreSupprime = 0;
                for (int i = 0; i < copieFichiers.size(); i++) {
                    if (!fichiersAnalyse.contains(copieFichiers.get(i))) {
                        resultatsAnalyse.enleverLigneEtColonne(
                                i - nombreSupprime);
                        nombreSupprime++;
                    }
                }
            }

            aviserObservateurs(Observation.UPDATEMAT, null);
        } else {
            setFichiers(nouveauxFichiers, false);
        }

        modifie = true;
    }

    /**
     * Permet d'accéder à l'état de la tâche.
     *
     * @return L'état de la tâche.
     */
    public float getStateCount() {
        return state;
    }

    /**
     * Retourne la liste complète de tous les fichiers qui forment la tâche.
     *
     * @return Les fichiers de la tâche.
     */
    public List<File> getTousFichiers() {
        return new ArrayList(fichiersOriginaux);
    }

    /**
     * Retourne la liste des fichiers qui sont réellement analysés, par exemple,
     * dans le cas d'une analyse par concaténation, les fichiers concaténés.
     *
     * @return Les fichiers analysés.
     */
    public List<File> getFichiersResultats() {
        return fichiersAnalyse;
    }

    /**
     * Permet d'ajouter une source aux sources. Associe ensuite les fichiers aux
     * fichiers de la source.
     *
     * @param source La nouvelle source.
     */
    public void ajouterSource(File source) {
        modifie = true;

        if (sources == null) {
            sources = new HashMap();
        }

        if (!sources.containsKey(source) && source.isDirectory()) {
            sources.put(source, new ArrayList());
        }

        trouverFichiersSource(source);
    }

    /**
     * Trouve les sources des fichiers de la tâche.
     *
     * @return Les sources des fichiers de la tâche.
     */
    private void trouverFichiersSource(File source) {
        File ancetreCommun = extractCommonAncestor();
        for (File fichier : fichiersAnalyse) {
            File parent = fichier.getParentFile();

            if (parent == source) {
                sources.get(source).add(fichier);
            } else {
                while (parent != null
                        && !parent.equals(ancetreCommun)
                        && !parent.equals(source)) {
                    parent = parent.getParentFile();
                }

                if (parent != null && parent.equals(source)) {
                    sources.get(source).add(fichier);
                }
            }
        }
    }

    private void regenererSources() {
        for (File source : sources.keySet()) {
            sources.get(source).clear();
            trouverFichiersSource(source);

            if (sources.get(source).isEmpty()) {
                sources.remove(source);
            }
        }
    }

    /**
     * Retire une source des sources et replace les fichiers dans la liste de
     * fichiers pour l'analyse si l'analyse se fait par concaténation.
     *
     * @param source La source à retirer.
     */
    public void retirerSource(File source) {
        modifie = true;

        if (sources != null) {
            sources.remove(source);
        }
    }

    /**
     * Vérifie si deux fichiers appartiennent à une même source, pour ne pas les
     * comparer entre eux.
     *
     * @param fichier1 Le premier fichier à vérifier.
     * @param fichier2 Le deuxième fichier à vérifier.
     * @return Si les fichiers appartiennent à la même source.
     */
    public boolean verifierSiMemeSource(File fichier1, File fichier2) {
        for (List<File> fichiers : sources.values()) {
            if (fichiers.contains(fichier1) && fichiers.contains(fichier2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return Tous les fichiers, classés selon leur premier ancêtre commun.
     */
    public Map<File, File[]> getFilesByRoot() {
        Map<File, File[]> ret = new HashMap();

        for (File root : filesCommonAncestors) {
            List<File> filesUnderThisRoot = new ArrayList();

            for (File file : fichiersOriginaux) {
                if (file.toPath().getRoot().toFile().equals(root)) {
                    filesUnderThisRoot.add(file);
                }
            }

            ret.put(root, filesUnderThisRoot.toArray(new File[0]));
        }

        return ret;
    }

    /**
     * Permet d'accéder à un fichier selon un indice.
     *
     * @param indice L'indice du fichier.
     * @return Le fichier demandé, ou null.
     */
    public File getFile(int indice) {
        if (fichiersAnalyse != null && indice < fichiersAnalyse.size()) {
            return fichiersAnalyse.get(indice);
        } else {
            return null;
        }
    }

    /**
     * Permet d'accéder aux ancêtres communs des fichiers.
     *
     * @return Les ancêtres communs des fichiers.
     */
    public File[] getCommonAncestors() {
        if (filesCommonAncestors == null) {
            return null;
        } else {
            return filesCommonAncestors.clone();
        }
    }

    /**
     * Indique si une analyse est en cours d'exécution.
     *
     * @return Si une analyse est en cours d'exécution.
     */
    public boolean analyseEnCours() {
        return analyseEnCours;
    }

    /**
     * Lorsqu'on veut forcer le type d'analyse malgré les préférences.
     *
     * @param cat Le type d'analyse.
     */
    public void setConcatenation(boolean cat) {
        //prefs = null;
        modifie = true;

        analyseConcatenation = cat;
    }

    public void setExtrairePDF(boolean ext) {
        //prefs = null;
        //modifie = true;

        analyseExtrairePDF = ext;
    }

    public void setExtraireImagePDF(boolean ext) {
        //prefs = null;
        //modifie = true;

        analyseExtraireImagePDF = ext;
    }

    public void setPreview(boolean prev) {
        //prefs = null;
        //modifie = true;

        analysePreview = prev;
    }

    public void setEnleverCommentaire(boolean comm) {
        //prefs = null;
        //modifie = true;

        enleverCommentaires = comm;
    }

    public void setEnleverWhitespaces(boolean wts) {
        //prefs = null;
        //modifie = true;

        enleverWhitespaces = wts;
    }

    /**
     * Permet de modifier le rapport de la tâche.
     *
     * @param JReport Le nouveau rapport.
     */
    public void setJReport(String JReport) {
        modifie = true;
        rapport = JReport;
    }

    /**
     * Permet d'accéder au rapport de la tâche.
     *
     * @return Le rapport de la tâche.
     */
    public String getJReport() {
        return rapport;
    }

    /**
     * Indique si la tâche a été modifiée depuis la dernière vérification.
     *
     * @return Si la tâche a été modifiée.
     */
    public boolean getModifie() {
        return modifie;
    }

    /**
     * Permet d'accéder au titre de la tâche.
     *
     * @return Le titre de la tâche.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Permet de modifier le titre de la tâche.
     *
     * @param titre Le titre de la tâche.
     */
    public void setTitre(String titre) {
        this.titre = titre;
        modifie = true;
    }

    /**
     * Permet de modifier le gestionnaire de préférences associé à la tâche.
     *
     * @param prefs
     */
    public void setPrefs(GestionnairePreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public StringBuffer toXml() {
        modifie = false;

        StringBuffer str = new StringBuffer();

        str.append("<onglet>\n");
        if (this.estSommaire()) {
            str.append("<titre sommaire=\"True\">");
        } else {
            str.append("<titre sommaire=\"False\">");
        }
        str.append(titre);
        str.append("</titre>\n");
        str.append("<rapport>");
        str.append(rapport);
        str.append("</rapport>\n");
        str.append("<analys>\n");
        str.append("<fichs>\n");

        List<String> listMD5 = genererMD5();
        int c = 0;
        for (File f : fichiersOriginaux) {
            str.append("<file hash=" + "\"" + listMD5.get(c) + "\"" + ">");
            str.append(SaveAndRestore.escape(f.getAbsolutePath()));
            str.append("</file>\n");
            c++;
        }

        str.append("</fichs>\n");
        enregistrerOptAnalyse();
        str.append("<options>\n");
        c = 0;
        for (String opt : getOptionAnalyse()) {
            str.append("<opt name=\"" + opt + "\">");
            str.append(getOptionAnalyseBool().get(c));
            str.append("</opt>\n");
            c++;
        }
        str.append("</options>\n");

        str.append("<sources>\n");
        for (File dossier : sources.keySet()) {
            str.append("<source>").append(dossier).append("</source>\n");
        }
        str.append("</sources>\n");
        if (resultatsAnalyse != null && !analyseEnCours) {
            str.append("<res len=\"");
            str.append(resultatsAnalyse.getLength());
            str.append("\">\n");

            int i = 0;
            for (float[] t : resultatsAnalyse.getValues()) {
                str.append("<li len=\"");
                str.append(t.length);
                str.append("\">\n");

                int j = 0;
                for (float f : t) {
                    str.append("<l i=\"");
                    str.append(i);
                    str.append("\" j=\"");
                    str.append(j);
                    str.append("\">");
                    str.append(f);
                    str.append("</l>\n");

                    j++;
                }

                str.append("</li>\n");
                i++;
            }
        } else {
            // Le résultat ne peut pas être null, car on ne peut pas passer un
            // fichier null, sinon ça plante.
            str.append("<res len=\"").append(0).append("\">\n");
        }

        str.append("</res>\n");
        str.append("</analys>\n");
        str.append("</onglet>\n");

        return str;
    }

    public List<String> genererMD5() {
        List<String> listMD5 = new ArrayList<String>();

        //String[] MD5tab = null;
        //MD5tab = new String[list.size()];
        for (File file : fichiersOriginaux) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(Files.readAllBytes(file.toPath()));
                byte[] hash = messageDigest.digest();
                String md5 = DatatypeConverter.printHexBinary(hash);
                //System.out.println(md5);
                listMD5.add(md5);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(WindowBaldr.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WindowBaldr.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listMD5;
    }

    private void enregistrerOptAnalyse() {
        optionAnalyse.clear();
        optionAnalyseBool.clear();
        if (prefs == null) { //Pour faire passer le test
            optionAnalyseBool.add(false);
            optionAnalyseBool.add(false);
            optionAnalyseBool.add(false);
            optionAnalyseBool.add(false);
            optionAnalyseBool.add(false);
            optionAnalyseBool.add(false);
            optionAnalyse.add("EXTRACT_PDF");
            optionAnalyse.add("CONCATENATION");
            optionAnalyse.add("COMMENTAIRES");
            optionAnalyse.add("WHITESPACES");
            optionAnalyse.add("PREVIEW");
            optionAnalyse.add("extract_image_pdf");

        } else {
            optionAnalyseBool.add((Boolean) prefs.readPref("EXTRACT_PDF", false));
            optionAnalyse.add("EXTRACT_PDF");

            optionAnalyseBool.add((Boolean) prefs.readPref("CONCATENATION", false));
            optionAnalyse.add("CONCATENATION");

            optionAnalyseBool.add((Boolean) prefs.readPref("COMMENTAIRES", false));
            optionAnalyse.add("COMMENTAIRES");

            optionAnalyseBool.add((Boolean) prefs.readPref("WHITESPACES", false));
            optionAnalyse.add("WHITESPACES");

            optionAnalyseBool.add((Boolean) prefs.readPref("PREVIEW", false));
            optionAnalyse.add("PREVIEW");

            optionAnalyseBool.add((Boolean) prefs.readPref("extract_image_pdf", false));
            optionAnalyse.add("extract_image_pdf");
        }

    }

    public List<Boolean> getOptionAnalyseBool() {
        return optionAnalyseBool;
    }

    public List<String> getOptionAnalyse() {
        return optionAnalyse;
    }

    @Override
    public void fromDom(Node node) {
        estRestauree = false;
        state = 1;
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (node.getChildNodes().item(i).getNodeName() != null) {
                switch (node.getChildNodes().item(i).getNodeName()) {
                    case "titre":
                        titre = node.getChildNodes().item(i).getTextContent();
                        break;
                    case "rapport":
                        rapport = node.getChildNodes().item(i).getTextContent();
                        break;
                    case "analys":
                        NodeList analyse
                                = node.getChildNodes().item(i).getChildNodes();
                        setFileListFromXMLNode(analyse);
                }
            }
        }

        modifie = false;
    }

    private void setFileListFromXMLNode(NodeList analyse) {
        List<File> list = null;
        float[][] resmat = null;

        for (int j = 0; j < analyse.getLength(); j++) {
            switch (analyse.item(j).getNodeName()) {
                case "fichs": {
                    NodeList fichml = analyse.item(j).getChildNodes();
                    list = new ArrayList();

                    for (int k = 0; k < fichml.getLength(); k++) {
                        if (fichml.item(k).getNodeName().equals("file")) {
                            File f = new File(fichml.item(k)
                                    .getTextContent().trim());
                            list.add(f);
                        }
                    }
                    setFichiers(list);
                    modifie = false;
                }
                break;
                case "sources": {
                    NodeList fichml = analyse.item(j).getChildNodes();
                    for (int k = 0; k < fichml.getLength(); k++) {
                        if (fichml.item(k).getNodeName().equals("source")) {
                            File f = new File(fichml.item(k)
                                    .getTextContent().trim());
                            ajouterSource(f);
                        }
                    }
                }
                break;
                case "res": {
                    resmat = getMatFromXMLNode(Integer.parseInt(
                            analyse.item(j).getAttributes()
                                    .getNamedItem("len").getTextContent()),
                            analyse.item(j).getChildNodes());
                }
                break;
                case "options": {
                    NodeList fichml = analyse.item(j).getChildNodes();
                    for (int k = 0; k < fichml.getLength(); k++) {
                        if (fichml.item(k).getNodeName().equals("opt")) {
                            String nom = fichml.item(k).getAttributes().getNamedItem("name").getTextContent();
                            switch (nom) {
                                case "EXTRACT_PDF":
                                    setExtrairePDF(Boolean.parseBoolean(fichml.item(k).getTextContent().trim()));
                                    this.prefs.writePref("EXTRACT_PDF", analyseExtrairePDF);
                                    break;
                                case "CONCATENATION":
                                    setConcatenation(Boolean.parseBoolean(fichml.item(k).getTextContent().trim()));
                                    this.prefs.writePref("CONCATENATION", analyseConcatenation);
                                    break;
                                case "COMMENTAIRES":
                                    setEnleverCommentaire(Boolean.parseBoolean(fichml.item(k).getTextContent().trim()));
                                    this.prefs.writePref("COMMENTAIRES", enleverCommentaires);
                                    break;
                                case "WHITESPACES":
                                    setEnleverWhitespaces(Boolean.parseBoolean(fichml.item(k).getTextContent().trim()));
                                    this.prefs.writePref("WHITESPACES", enleverWhitespaces);
                                    break;
                                case "PREVIEW":
                                    setPreview(Boolean.parseBoolean(fichml.item(k).getTextContent().trim()));
                                    this.prefs.writePref("PREVIEW", analysePreview);
                                    break;
                                case "extract_image_pdf":
                                    setExtraireImagePDF(Boolean.parseBoolean(fichml.item(k).getTextContent().trim()));
                                    this.prefs.writePref("extract_image_pdf", analyseExtraireImagePDF);
                                    break;
                            }
                        }
                    }
                }
                break;
            }
        }

        if (resmat != null && resmat.length != 0) {
            setExRes(list, resmat);
        }
    }

    private float[][] getMatFromXMLNode(int taille, NodeList liste) {
        float[][] matrice = new float[taille][];

        int a = 0;
        for (int i = 0; i < liste.getLength(); i++) {
            if ("li".equals(liste.item(i).getNodeName())) {
                NodeList m = liste.item(i).getChildNodes();
                matrice[a] = new float[Integer.parseInt(
                        liste.item(i).getAttributes()
                                .getNamedItem("len").getTextContent())];

                int b = 0;
                for (int j = 0; j < m.getLength(); j++) {
                    if ("l".equals(m.item(j).getNodeName())) {
                        matrice[a][b]
                                = Float.parseFloat(m.item(j).getTextContent());
                        b++;
                    }
                }
                a++;
            }
        }

        return matrice;
    }

    /**
     * Indique ne pas être un sommaire.
     *
     * @return false
     */
    public boolean estSommaire() {
        return false;
    }

    /**
     * Retourne vrai si le dossier passé en paramètre est une source.
     *
     * @param folder Le dossier à tester.
     * @return Si le dossier est une source.
     */
    public boolean isSource(File folder) {
        return sources.keySet().contains(folder);
    }

    /**
     * Supprime la source qui lie les fichiers du dossier.
     *
     * @param folder Le dossier représentant une source.
     */
    public void supprimerSource(File folder) {
        if (sources.keySet().contains(folder)) {
            sources.remove(folder);
        }
    }

    public boolean contientTache(Task tache) {
        return false;
    }

    /**
     * Indique si la tâche a été restaurée ou non.
     *
     * @return Si la tâche a été restaurée ou non.
     */
    public boolean estRestauree() {
        return estRestauree;
    }

    /**
     * Permet de marquer la tâche comme restaurée.
     */
    public void marquerCommeRestauree() {
        estRestauree = true;
    }

    public boolean verifierAnalyseFaite() {
        for (int i = 0; i < resultatsAnalyse.getValues().length; i++) {
            for (int j = 0; j < resultatsAnalyse.getValues()[i].length; j++) {
                if (resultatsAnalyse.getResAt(i, j) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retourne vrai si le dossier passé en paramètre contient au moins un
     * sous-dossier qui se retrouve dans le tableau de fichiers.
     *
     * @param dir Dossier à vérifier.
     * @return Si le dossier se trouve dans les fichiers.
     */
    public boolean hasSubdirectoriesInFiles(File dir) {
        if (dir.isDirectory()) {
            String path = dir.getAbsolutePath();

            for (File file : fichiersAnalyse) {
                File parent = file.getParentFile();
                String chemin = parent.getAbsolutePath();

                if (!parent.equals(dir)
                        && chemin.startsWith(path)) {
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    @Override
    public void ajouterObservateur(Observateur ob) { // tester
        if (!observateurs.contains(ob)) {
            observateurs.add(ob);
        }
    }

    @Override
    public synchronized void aviserObservateurs() { //tester
        for (Observateur ob : observateurs) {
            ob.changementEtat();
        }
    }

    @Override
    public synchronized void aviserObservateurs(Enum<?> property, Object o) { // tester
        for (Observateur ob : observateurs) {
            ob.changementEtat(property, o);
        }
    }

    @Override
    public void retirerObservateur(Observateur ob) { //tester
        observateurs.remove(ob);
    }

    public Thread getThread() {
        return thread;
    }

    public boolean threadState() {
        return getThread().isAlive();
    }
}
