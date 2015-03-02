package com.aware.plugin.InOutdoor;

/**
 * Created by Armand on 26/02/2015.
 */
public class ArbreBinaire {
    private static Singleton instance = Singleton.getInstance();
    private boolean decision;
    private int valeur;
    private ArbreBinaire previous, gauche, droite;

    public ArbreBinaire(int obj, boolean decision, ArbreBinaire g, ArbreBinaire d) {
        this.valeur = obj;
        this.decision = decision;
        this.gauche = g;
        this.droite = d;
    }

    public ArbreBinaire() {
        this.setValeur(0);
        this.setGauche(null);
        this.setDroite(null);
    }

    public static boolean deapthSearch(ArbreBinaire a) {
        boolean i;
        if (a.valeur != 0) {
            switch (a.valeur) {
                case 1:
                    i = instance.getInOut().getBattery();
                    break;
                case 2:
                    i = instance.getInOut().getBarometer();
                    break;
                case 3:
                    i = instance.getInOut().getLinear();
                    break;
                case 4:
                    i = instance.getInOut().getAccelerometer();
                    break;
                case 5:
                    i = instance.getInOut().getMagnetic();
                    break;
                case 6:
                    i = instance.getInOut().getGravity();
                    break;
                case 7:
                    i = instance.getInOut().getWifi();
                    break;
                case 8:
                    i = instance.getInOut().getLocation();
                    break;
                case 9:
                    //i = instance.getInOut().getProxy();
                    break;
                case 10:
                    i = instance.getInOut().getTemp();
                    break;
                case 11:
                    i = instance.getInOut().getGyroscope();
                    break;
                case 12:
                    // i = instance.getInOut().getLux();
                    break;
                case 13:
                    //i = instance.getInOut().getGoogleRec();
                    break;
                case 14:
                    //i = instance.getInOut().getAmbientNoise();
                    break;
                default:
                    break;
            }
        } else
            return a.decision;
        if (i = false)
            return deapthSearch(a.gauche);
        else
            return deapthSearch(a.droite);
    }

    public static void createGraph() {
        ArbreBinaire t = new ArbreBinaire();
        ArbreBinaire f = new ArbreBinaire();
        ArbreBinaire k0 = new ArbreBinaire();
        k0.setValeur(1);
        ArbreBinaire k2 = new ArbreBinaire();
        k2.setValeur(3);
        ArbreBinaire k12 = new ArbreBinaire();
        k12.setValeur(13);
        ArbreBinaire k5 = new ArbreBinaire();
        k5.setValeur(6);
        ArbreBinaire k8 = new ArbreBinaire();
        k8.setValeur(9);

        k0.add(t, true);
        k0.add(f, false);
/*
        k2.add(k5,false);
        k2.add(t,true);

        k12.add(k8,false);
*/
        Singleton.getInstance().setTree(k0);
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public ArbreBinaire getGauche() {
        return gauche;
    }

    public void setGauche(ArbreBinaire gauche) {
        this.gauche = gauche;
    }

    public ArbreBinaire getDroite() {
        return droite;
    }

    public void setDroite(ArbreBinaire droite) {
        this.droite = droite;
    }

    public void add(ArbreBinaire a, boolean b) {
        if (b)
            this.gauche = a;
        else
            this.droite = a;
    }
}