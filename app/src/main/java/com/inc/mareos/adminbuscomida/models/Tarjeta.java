package com.inc.mareos.adminbuscomida.models;

public class Tarjeta {

    private double saldo;
    private boolean activa;
    private boolean cobrada;

    public Tarjeta() {
    }

    public Tarjeta(double saldo, boolean activa, boolean cobrada) {
        this.saldo = saldo;
        this.activa = activa;
        this.cobrada = cobrada;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isCobrada() {
        return cobrada;
    }

    public void setCobrada(boolean cobrada) {
        this.cobrada = cobrada;
    }
}
