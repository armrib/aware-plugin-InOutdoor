package com.aware.plugin.inoutdoor;

import android.util.Log;

/**
 * Created by Armand on 26/02/2015.
 */
public class ArbreBinaire {
    private double decision;
    private int valeur;
    private ArbreBinaire gauche, droite;
    private static double confidence;

    public ArbreBinaire(int obj, double decision, ArbreBinaire g, ArbreBinaire d) {
        this.valeur = obj;
        this.decision = decision;
        this.gauche = g;
        this.droite = d;
    }

    public ArbreBinaire() {
        this.setValeur(0);
        this.setDecision(2);
        this.setGauche(null);
        this.setDroite(null);
    }

    public static double[] deapthSearch(InOutDoor inOutDoor,ArbreBinaire a) {
        double[] i = {0,0};
        double[] res = new double[2];
        Log.d("test", "deapthSearch node recherche : " + a.valeur);
        if (a.valeur != 0) {
            if (a.valeur == 1) {
                i = inOutDoor.getBattery();
                confidence = i[1];
            } else if (a.valeur == 2) {
                i = inOutDoor.getAccelerometer();
                confidence = confidence * i[1];
            } else if (a.valeur == 3) {
                i = inOutDoor.getWifi();
                confidence = confidence * i[1];
            } else if (a.valeur == 4) {
                i = inOutDoor.getTemp();
                confidence = confidence * i[1];
            } else if (a.valeur == 5) {
                i = inOutDoor.getLight();
                confidence = confidence * i[1];
            } else if (a.valeur == 6) {
                i = inOutDoor.getScreen();
                confidence = confidence * i[1];
            } else if (a.valeur == 7){
                i = inOutDoor.getMagnetometer();
                confidence = confidence * i[1];
            }
            else {
                i = inOutDoor.getWifiP();
                confidence = confidence * i[1];
            }
        } else {
            Log.d("test", "deapthSearch node decision");
            res[0] = a.decision;
            res[1] = confidence;
            return res;
        }
        if (i[0] == 1 && a.decision == 2) {
            Log.d("test", "deapthSearch return a.gauche");
            return deapthSearch(inOutDoor, a.gauche);
        }
        if (i[0] == 0 && a.decision == 2) {
            Log.d("test", "deapthSearch return a.droite");
            return deapthSearch(inOutDoor, a.droite);
        } else return res;
    }

    public static ArbreBinaire createGraph(Boolean magnetometer, Boolean light, Boolean temperature) {
        ArbreBinaire in = new ArbreBinaire();
        in.setDecision(1);
        ArbreBinaire out = new ArbreBinaire();
        out.setDecision(0);

        ArbreBinaire k0 = new ArbreBinaire();
        k0.setValeur(1);
        ArbreBinaire k1 = new ArbreBinaire();
        k1.setValeur(2);
        ArbreBinaire k2 = new ArbreBinaire();
        k2.setValeur(6);
        ArbreBinaire k3 = new ArbreBinaire();
        k3.setValeur(6);
        ArbreBinaire k4 = new ArbreBinaire();
        k4.setValeur(5);
        ArbreBinaire k5 = new ArbreBinaire();
        k5.setValeur(7);
        ArbreBinaire k6 = new ArbreBinaire();
        k6.setValeur(5);
        ArbreBinaire k7 = new ArbreBinaire();
        k7.setValeur(3);
        ArbreBinaire k8 = new ArbreBinaire();
        k8.setValeur(2);
        ArbreBinaire k9 = new ArbreBinaire();
        k9.setValeur(3);
        ArbreBinaire k10 = new ArbreBinaire();
        k10.setValeur(8);
        ArbreBinaire k11 = new ArbreBinaire();
        k11.setValeur(3);
        ArbreBinaire k12 = new ArbreBinaire();
        k12.setValeur(3);
        ArbreBinaire k13 = new ArbreBinaire();
        k13.setValeur(8);
        ArbreBinaire k14 = new ArbreBinaire();
        k14.setValeur(7);
        ArbreBinaire k15 = new ArbreBinaire();
        k15.setValeur(8);
        ArbreBinaire k16 = new ArbreBinaire();
        k16.setValeur(4);

    if (magnetometer && light && temperature) {
        Log.d("test", "complet tree");
        k0.setGauche(k1);
        k0.setDroite(k2);

        k1.setGauche(k3);
        k1.setDroite(in);

        k2.setGauche(k4);
        k2.setDroite(k5);

        k3.setGauche(k6);
        k3.setDroite(k7);

        k4.setGauche(in);
        k4.setDroite(out);

        k5.setGauche(k8);
        k5.setDroite(k9);

        k6.setGauche(in);
        k6.setDroite(out);

        k7.setGauche(k10);
        k7.setDroite(k14);

        k8.setGauche(k11);
        k8.setDroite(k12);

        k9.setGauche(k13);
        k9.setDroite(out);

        k10.setGauche(in);
        k10.setDroite(k14);

        k11.setGauche(in);
        k11.setDroite(out);

        k12.setGauche(k15);
        k12.setDroite(k16);

        k13.setGauche(in);
        k13.setDroite(out);

        k14.setGauche(in);
        k14.setDroite(out);

        k15.setGauche(in);
        k15.setDroite(in);

        k16.setGauche(in);
        k16.setDroite(out);
    }else if(magnetometer && light){
        Log.d("test", "no temp tree");
        k0.setGauche(k1);
        k0.setDroite(k2);

        k1.setGauche(k3);
        k1.setDroite(in);

        k2.setGauche(k4);
        k2.setDroite(k5);

        k3.setGauche(k6);
        k3.setDroite(k7);

        k4.setGauche(in);
        k4.setDroite(out);

        k5.setGauche(k8);
        k5.setDroite(k9);

        k6.setGauche(in);
        k6.setDroite(out);

        k7.setGauche(k10);
        k7.setDroite(k14);

        k8.setGauche(k11);
        k8.setDroite(k12);

        k9.setGauche(k13);
        k9.setDroite(out);

        k10.setGauche(in);
        k10.setDroite(k14);

        k11.setGauche(in);
        k11.setDroite(in);

        k12.setGauche(k15);
        k12.setDroite(in);

        k13.setGauche(in);
        k13.setDroite(out);

        k14.setGauche(in);
        k14.setDroite(out);

        k15.setGauche(in);
        k15.setDroite(in);
    }else{
        Log.d("test", "reduced tree");
        k0.setGauche(k1);
        k0.setDroite(k8);

        k1.setGauche(k11);
        k1.setDroite(in);

        k8.setGauche(k11);
        k8.setDroite(k11);

        k11.setGauche(in);
        k11.setDroite(out);
    }
        return k0;
    }

    public double isDecision() {
        return decision;
    }

    public void setDecision(double decision) {
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
}