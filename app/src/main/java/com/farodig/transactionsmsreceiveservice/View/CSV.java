package com.farodig.transactionsmsreceiveservice.View;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import com.farodig.transactionsmsreceiveservice.Model.SmsItem;

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
        //out.append('\n');
        //out.append("(");
        out.append(Character.toChars(10));
        //out.append(")");

        //out.append(Character.toChars(10));

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
            //else
                //out.append('\n');
        }
//        for (int i = 1; i < fields.size(); i++) {
//            out.append(fields.get(i));
//            out.append(CSV_SEPARATOR);
//        }
//        out.append("\n");
    }

    public String GetString(SmsItem item)
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
        AddField(gf.format(item.TransactionValue));

        Finish();

        return out.toString();
    }
}
