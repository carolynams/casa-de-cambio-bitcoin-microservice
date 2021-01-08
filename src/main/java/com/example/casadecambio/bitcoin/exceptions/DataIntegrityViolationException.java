package com.example.casadecambio.bitcoin.exceptions;

public class DataIntegrityViolationException extends RuntimeException {

    public static final String SALDO_INSUFICIENTE = "Saldo do cliente insuficiente para realizar a compra de Bitcoins";
    public static final String CLIENTE_NÃO_POSSUI_COMPRAS = "Cliente informaddo ainda não possui compras de bitcoins";


    public DataIntegrityViolationException(String msg) {
        super(msg);
    }
}
