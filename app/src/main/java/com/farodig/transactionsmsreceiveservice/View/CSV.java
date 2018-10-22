package com.farodig.transactionsmsreceiveservice.View;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;

import com.farodig.transactionsmsreceiveservice.Model.SmsItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.lang.String;

public class CSV
{
    private static final String CSV_SEPARATOR = ";";

    private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    private static final String Header = "\"Дата\";\"Заголовок\";\"Комментарий\";\"Основная категория\";\"Подкатегория\";\"Счет\";\"Сумма\"";

    private StringBuffer out;

    private List<String> fields = new ArrayList<String>();

    public CSV()
    {
        // init
        out = new StringBuffer(Header);
        out.append(Character.toChars(10));
    }

    private void AddField(String text)
    {
        fields.add("\"" + text + "\"");
    }

    private void Finish()
    {
        for (int i = 0; i <= fields.size() - 1; i++) {
            out.append(fields.get(i));
            if (i != fields.size() - 1)
                out.append(CSV_SEPARATOR);
            else
                out.append(Character.toChars(10));
                //out.append('\n');
        }
    }

    public void SaveCSV(SmsItem item)
    {
        // Дата
        AddField(df.format(item.DateTime));

        // Заголовок
        AddField("Automatic");

        // Комментарий
        AddField(item.Text);

        // Основная категория
        AddField(item.TransactionType.IsNegative() ? "Расходы" : "Доходы");

        // Подкатегория
        AddField(item.TransactionType.ToSubcategory());

        // Счет
        AddField("Текущий счет");

        DecimalFormat gf = (DecimalFormat)NumberFormat.getNumberInstance(Locale.GERMAN);
        gf.applyPattern("#.00");

        // Сумма
        AddField((item.TransactionType.IsNegative() ? "-" : "") + gf.format(item.TransactionValue));

        Finish();

        File externalDir = new File(Environment.getExternalStorageDirectory(), "/Мой бюджет/" + new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss").format(item.DateTime) + ".csv");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (externalDir);
            outputStream.write(out.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
