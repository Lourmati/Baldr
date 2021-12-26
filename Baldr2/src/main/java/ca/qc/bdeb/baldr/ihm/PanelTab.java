/*
 * PanelTab.java
 *
 * Created on 30 mars 2007, 19:03
 *$Id: PanelTab.java 249 2007-08-29 20:57:45Z cedric $
 */
package ca.qc.bdeb.baldr.ihm;

import static ca.qc.bdeb.baldr.ihm.Observation.ANALYSE_TERMINEE;
import ca.qc.bdeb.baldr.main.Main;
import ca.qc.bdeb.baldr.noyau.GestionnaireFiltres;
import ca.qc.bdeb.baldr.noyau.GestionnairePreferences;
import ca.qc.bdeb.baldr.noyau.MatriceTriangulaire;
import ca.qc.bdeb.baldr.noyau.Noyau;
import ca.qc.bdeb.baldr.noyau.Projet;
import ca.qc.bdeb.baldr.noyau.Task;

import ca.qc.bdeb.baldr.utils.Observateur;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The PanelTab Class is in charge of one tab of the application. It will
 * display 3 panes with which the user can interact
 *
 * @author Baldr Team
 *
 * @see WindowBaldr
 *
 */
public class PanelTab extends javax.swing.JPanel
        implements Observateur {

    /**
     * id number of the tab
     */
    private int tabNumber;
    private String tabTitle;
    private PanelFiles pnlFiles;
    private PanelResults pnlResults;
    private ResourceBundle messages;
    private JReport jReport;
    private GestionnaireFiltres filtre;
    private Dimension screenSize;
    private Dimension windowSize;
    public Date tempsDebutAnalyse;
    private boolean analyserFaite = false;

    /**
     * Analysis Results
     */
    private Task tache = null;
    private Noyau noyau;
    private Projet projetCourant;
    private WindowBaldr win;

    /**
     * Construct and display a tab
     *
     * @param tabNumber Le numéro de la tab
     * @param tache La nouvelle tache
     * @param noyau De type Noyau
     * @param win Fenêtre parente
     * @param filtre
     */
    public PanelTab(int tabNumber, Task tache, Noyau noyau, WindowBaldr win,
            GestionnaireFiltres filtre) {

        this.win = win;
        this.noyau = noyau;
        this.tache = tache;
        this.tabNumber = tabNumber;
        this.filtre = filtre;
        jReport = new JReport(this,win);

        initVariables();
        initFenetre();

        tache.ajouterObservateur(pnlFiles);
        tache.ajouterObservateur(this);
    }

    public PanelFiles getPanelFile() {
        return pnlFiles;
    }

    public boolean isFileListEmpty() {
        boolean isEmpty;
        isEmpty = pnlFiles.isFileListEmpty();
        return isEmpty;
    }

    public boolean estSommaire() {
        return tache.estSommaire();
    }

    /**
     * Called on closing request to ask for saving.
     *
     */
    public void ExitAndSaveOnglet() {
        int choix = JOptionPane.showConfirmDialog(
                this, messages.getString("Save_Mods"), "Baldr", 1);
        if (choix == JOptionPane.NO_OPTION) {
            tache.aviserObservateurs(Observation.EXIT, this);
        } else if (choix == JOptionPane.OK_OPTION) {
            tache.aviserObservateurs(Observation.SAUVEGARDER, this);
        }

    }

    public DefaultMutableTreeNode getFileList() {
        return pnlFiles.getFileTree();
    }

    public String getJReportText() {
        return jReport.getText();
    }

    public void setTabNumber(int i) {
        tabNumber = i;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String title) {
        this.tabTitle = title;
    }

    public Projet getProjectCourant() {
        return projetCourant;
    }

    public int getTabNumber() {
        return tabNumber;
    }

    @Override
    public void changementEtat() {
    }

    @Override
    public void changementEtat(Enum<?> property, Object o) {
        switch ((Observation) property) {
            case SAVEANDEXIT:
                ExitAndSaveOnglet();
                break;
            case UPDATEMAT:
            case ANALYSE_TERMINEE:
                pnlResults.updateMat(tache);
                break;
            case EXIT:
                win.fermerTab((PanelTab)o, false);
                break;
            case SAUVEGARDER:
                if (win.sauver() !=null) {
                  win.fermerTab((PanelTab)o, false);  
                }
                break;
        }
    }

    public String getTempsAnalyse() {
        Date tempsFinAnalyse = new Date();
        long millis = tempsFinAnalyse.getTime() - tempsDebutAnalyse.getTime();

        return String.format("%02d:%02d:%05.2f",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                (double) ((TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
                + (double) (millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))) / 1000));

    }

    public void afficherCopieAnalyse(Task newTask) {
        pnlFiles.expandFirstTreeRow();
        List<File> files = pnlFiles.getFileList();
        tache = newTask;

        if (files != null && files.size() > 2) {
            newTask.ajouterObservateur(pnlFiles);

            pnlFiles.setEtatBoutonsPeutDebuter(true);
        } else if (files != null && files.size() <= 2) {
            ErrorMessages.notEnoughFiles();
        } else {
            ErrorMessages.noFiles();
        }
    }

    /**
     * Démarre une analyse. Méthode appelée juste après l'action de
     * l'utilisateur.
     *
     * @return Si l'analyse a correctement été démarrée.
     */
    public boolean demarrerAnalyse() {
        List<File> files = pnlFiles.getFileList();
        tempsDebutAnalyse = new Date();

        boolean analyseLancee = false;

        // Minimum de 2 fichiers pour faire une analyse
        if (files != null && files.size() > 2) {
            pnlFiles.setEtatBoutonsPeutDebuter(false);
            tache.lancerAnalyse();
            //lancerAnalyse(); THREAD
            analyseLancee = true;
            analyserFaite = true;
        } else if (files != null && files.size() <= 2) {
            ErrorMessages.notEnoughFiles();
        } else {
            ErrorMessages.noFiles();
        }

        return analyseLancee;
    }

    public Task getTask() {
        return tache;
    }

    void setTask(Task task) {
        tabTitle = task.getTitre();
        jReport.setText(task.getJReport());
        pnlFiles.setFileListFromTask(task);
        tache = task;
        tache.ajouterObservateur(pnlFiles);
        tache.ajouterObservateur(this);
        pnlResults.updateMat(tache);

        //AJOUT POUR HISTOGRAMME
        jReport.analys.ajouterObservateur(pnlFiles);
        jReport.DispatchResult();
    }

    private void initFenetre() {

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        initialiserPanelFiles(c);
        this.add(pnlFiles, c);

        int windowX = Math.max(0, (screenSize.width - windowSize.width) - 480);

        intialiserPnlResults(windowX, c);
        this.add(pnlResults, c);

        initialiserJReport(c);
        this.add(jReport, c);
    }

    private void intialiserPnlResults(int windowX, GridBagConstraints c) {
        pnlResults = new PanelResults(this, noyau, win.getGestionnaireI18N());
        pnlResults.setBounds(460, 0, windowX, 645);
        c.gridx = 1;
        c.gridheight = 2;
        c.weightx = 1;
        c.weighty = 1;
    }

    private void initialiserJReport(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 1;
    }

    private void initialiserPanelFiles(GridBagConstraints c) {
        pnlFiles = new PanelFiles(this, noyau, win, filtre);
        pnlFiles.setBounds(0, 0, 420, 645);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
    }

    private void initVariables() {

        projetCourant = noyau.getProjetCourant();

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = this.getSize();

        messages = ResourceBundle.getBundle("i18n/Baldr");
    }
//    private Thread thread;
//    private boolean stopNow;
//    private GestionnairePreferences prefs;
//    private Map<File, List<File>> sources;
//    protected List<File> fichiersAnalyse;
//    private List<File> fichiersOriginaux;
//    private boolean analyseConcatenation = false;
//    // Préférances sur le type d'analyse
//    private boolean enleverCommentaires = false;
//    private boolean enleverWhitespaces = false;
//    private float state;
//    private MatriceTriangulaire resultatsAnalyse;
//    private boolean analyseEnCours;
//    private boolean modifie = false;
//    private File[] filesCommonAncestors = null;
//
//    private final Runnable analysisAlgorithm = new Runnable() {
//        @Override
//        public void run() {
//            analyseEnCours = true;
//
//            if (analyseConcatenation && !sources.isEmpty()) {
//                getTask().faireAnalyseConcatenation();
//            } else {
//                getTask().faireAnalyseNormale();
//            }
//
//            state = 1;
//            analyseEnCours = false;
//            modifie = true;
//            getTask().aviserObservateurs(ANALYSE_TERMINEE, this);
//        }
//    };
//
//    /**
//     * Arrête le thread d'analyse.
//     */
//    public void stopAnalysis() {
//        stopNow = true;
//
//        try {
//            thread.interrupt();
//        } catch (Exception ex) {
//            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        getTask().aviserObservateurs();
//    }
//
//    /**
//     * Crée un nouveau thread et le démarre.
//     */
//    public void lancerAnalyse() {
//        if (prefs != null) {
//            boolean cat = (Boolean) prefs.readPref("CONCATENATION", false);
//
//            if (cat && !sources.isEmpty()) {
//                fichiersAnalyse = new ArrayList(sources.keySet());
//            } else {
//                fichiersAnalyse = new ArrayList(fichiersOriginaux);
//            }
//
//            analyseConcatenation = cat;
//
//            // Vérifier si options d'enlever les commentaires et activée
//            enleverCommentaires = (Boolean) prefs.readPref("COMMENTAIRES", false);
//
//            // Vérifier si options d'enlever les commentaires et activée
//            enleverWhitespaces = (Boolean) prefs.readPref("WHITESPACES", false);
//
//        } else { // Pas de préférences gérées (par exemple : pendant un test).
//            if (analyseConcatenation && !sources.isEmpty()) {
//                fichiersAnalyse = new ArrayList(sources.keySet());
//            } else {
//                fichiersAnalyse = new ArrayList(fichiersOriginaux);
//            }
//        }
//
//        state = 0;
//        stopNow = false;
//        resultatsAnalyse = new MatriceTriangulaire(fichiersAnalyse.size());
//        thread = new Thread(analysisAlgorithm);
//
//        analyseEnCours = true;
//        thread.start();
//    }
//
//    public boolean analyseEnCours() {
//        return analyseEnCours;
//    }
//
//    public void setFichiers(List<File> nouveauxFichiers, boolean resetRes) {
//        if (nouveauxFichiers != null) {
//            fichiersAnalyse = new ArrayList(nouveauxFichiers);
//            fichiersOriginaux = new ArrayList(nouveauxFichiers);
//        } else {
//            fichiersAnalyse = new ArrayList();
//            fichiersOriginaux = new ArrayList();
//
//            return; // Pas de traitement nécessaire.
//        }
//
//        if (fichiersAnalyse != null && fichiersAnalyse.size() > 0) {
//            File commonAncestor = extractCommonAncestor();
//
//            if (commonAncestor == null) {
//                filesCommonAncestors = findAllRoots();
//            } else {
//                filesCommonAncestors = new File[]{commonAncestor};
//            }
//
//            if (resetRes) {
//                resultatsAnalyse
//                        = new MatriceTriangulaire(fichiersAnalyse.size());
//            }
//        }
//
//        modifie = true;
//    }
//
//    /**
//     * Dans le tableau de fichiers, donne la partie du chemin qui est commune à
//     * tous les fichiers.
//     *
//     * @return Objet {@link java.io.File} représentant ce chemin, ou
//     * {@code null} s'il n'en existe pas.
//     */
//    private File extractCommonAncestor() {
//        List<String> pathSimilaire = new ArrayList();
//        String[] pathInitial = fichiersAnalyse.get(0).getAbsolutePath()
//                .split(Main.regexFileSeparator);
//        boolean pareil = true;
//
//        // Vérifier le chemin nom par nom
//        for (int i = 0; i < pathInitial.length - 1 && pareil; i++) {
//            pareil = true;
//
//            for (File file : fichiersAnalyse) {
//                String[] pathTest
//                        = file.getAbsolutePath().split(Main.regexFileSeparator);
//
//                if (pathTest.length <= i
//                        || !pathInitial[i].equals(pathTest[i])) {
//                    pareil = false;
//                }
//            }
//
//            if (pareil) {
//                pathSimilaire.add(pathInitial[i]);
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//
//        if (Main.isUnix || Main.isMac) {
//            sb.append(File.separator);
//        }
//
//        for (String string : pathSimilaire) {
//            sb.append(string);
//            sb.append(File.separator);
//        }
//
//        if (pathSimilaire.isEmpty()) {
//            return null;
//        } else {
//            return new File(sb.toString());
//        }
//    }
//
//    private File[] findAllRoots() {
//        Set<File> roots = new HashSet();
//
//        for (File file : fichiersOriginaux) {
//            roots.add(file.toPath().getRoot().toFile());
//        }
//
//        return roots.toArray(new File[0]);
//    }
//
//    public MatriceTriangulaire getResults() {
//        return resultatsAnalyse;
//    }
//
//    public boolean verifierAnalyseFaite() {
//        for (int i = 0; i < resultatsAnalyse.getValues().length; i++) {
//            for (int j = 0; j < resultatsAnalyse.getValues()[i].length; j++) {
//                if (resultatsAnalyse.getResAt(i, j) < 0) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public List<File> getFiles() {
//        return fichiersAnalyse;
//    }
}
