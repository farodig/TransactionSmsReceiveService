package com.farodig.transactionsmsreceiveservice.Model;

import com.farodig.transactionsmsreceiveservice.SmsReceiverBroadcast;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SmsItemUnitTest {
    @Test
    public void ConstructMessage_writeoff1234_isCorrect() throws Exception {
        SmsItem item = new SmsItem("\"ECMC1234 16:54 списание 1234р Баланс: 1234.12р\"", Calendar.getInstance().getTime());

        DecimalFormat gf = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMAN);
        gf.applyPattern("#.00");
        assertEquals("-1234,00", (item.TransactionType.IsNegative() ? "-" : "") + gf.format(item.TransactionValue));
    }
    @Test
    public void ConstructMessage_Enrollment102_isCorrect() throws Exception {
        SmsItem item = new SmsItem("\"ECMC1234 22.10.18 20:10 зачисление 102р Баланс: 1234.12р\"", Calendar.getInstance().getTime());
        DecimalFormat gf = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMAN);
        gf.applyPattern("#.00");
        assertEquals("102,00", (item.TransactionType.IsNegative() ? "-" : "") + gf.format(item.TransactionValue));
    }

    @Test
    public void ConstructMessage_WriteOffOffset_isInCorrect() throws Exception {
        SmsItem item = new SmsItem("Для перевода 500р получателю чч чч чч. на карту MAES1234 с карты ECM4321 отправьте код 1234 на номер 900. Комиссия не взимается. Добавьте сообщение получателю, набрав его после кода. Например, 1234 сообщение получателю.", Calendar.getInstance().getTime());
        assertFalse(SmsItem.IsValid(item.Text));
    }
}