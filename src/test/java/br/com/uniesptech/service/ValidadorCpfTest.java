package br.com.uniesptech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidadorCpfTest {

    @Test
    void aceitaCpfValidoComESemPontuacao() {
        assertTrue(ValidadorCpf.ehValido("52998224725"));
        assertTrue(ValidadorCpf.ehValido("529.982.247-25"));
        assertTrue(ValidadorCpf.ehValido("11144477735"));
    }

    @Test
    void recusaDigitoVerificadorErrado() {
        assertFalse(ValidadorCpf.ehValido("52998224724"));
        assertFalse(ValidadorCpf.ehValido("12345678900"));
    }

    @Test
    void recusaTamanhoErrado() {
        assertFalse(ValidadorCpf.ehValido("1234567890"));
        assertFalse(ValidadorCpf.ehValido("123456789012"));
        assertFalse(ValidadorCpf.ehValido(""));
        assertFalse(ValidadorCpf.ehValido(null));
    }

    @Test
    void recusaDigitosRepetidos() {
        assertFalse(ValidadorCpf.ehValido("11111111111"));
        assertFalse(ValidadorCpf.ehValido("00000000000"));
    }

    @Test
    void normalizaRemovendoPontuacao() {
        assertEquals("52998224725", ValidadorCpf.normalizar(" 529.982.247-25 "));
    }
}
