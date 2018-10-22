package com.farodig.transactionsmsreceiveservice.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

///
/// Parsed SMS
///
public class SmsItem
{
    // Appear sms DateTime
    public Date DateTime;

    // sms text
    public final String Text;

    // transaction sum
    public double TransactionValue;

    // current balance
    public double Balance;

    // transaction type
    public TransactionTypeEnum TransactionType;

    // constructor
    public SmsItem(String message, Date dateTime)
    {
        // Appear sms DateTime
        this.DateTime = dateTime;

        // sms text
        this.Text = message;

        for(Map.Entry<String, TransactionTypeEnum> entry : transactionDict.entrySet())
        {
            TransactionTypeEnum value = entry.getValue();

            Pattern regex = Pattern.compile(entry.getKey());
            Matcher founded = regex.matcher(this.Text.toLowerCase());
            if (founded.find() && founded.groupCount() > 1)
            {
                String group = founded.group();

                // transaction sum
                TransactionValue = Double.parseDouble(founded.group(1));

                // transaction type
                TransactionType = entry.getValue();

                if (TransactionType == TransactionTypeEnum.PurchaseUSD || TransactionType == TransactionTypeEnum.EnrollmentUSD)
                {
                    // usd 19.10.2018
                    TransactionValue *= 65.50;

                    // negative transaction
                    if (TransactionType.IsNegative())
                    {
                        TransactionValue *= -1;
                    }
                }
                break;
            }
        }

        {
            Pattern regex = Pattern.compile("баланс: (\\d+(.{1}\\d{2})?)р");
            Matcher founded = regex.matcher(this.Text.toLowerCase());
            if (founded.find() && founded.groupCount() > 1)
            {
                // current balance
                Balance = Double.parseDouble(founded.group(1));
            }
        }
    }

    // Regex map
    private static final Map<String, TransactionTypeEnum> transactionDict = new HashMap<String, TransactionTypeEnum>()
    {
        { put("покупка (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Purchase); }
        { put("покупка (\\d+(.{1}\\d{2})?)usd", TransactionTypeEnum.PurchaseUSD); }
        { put("выдача наличных (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.CashOut); }
        { put("выдача (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.CashOut); }
        { put("оплата (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Payment); }
        { put("оплата услуг (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Payment); }
        { put("оплата мобильного банка за \\d{2}/\\d{2}/\\d{4}-\\d{2}/\\d{2}/\\d{4} (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Payment); }
        { put("оплата годового обслуживания карты (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Payment); }
        { put("списание (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.WriteOff); }
        { put("зачисление (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Enrollment); }
        { put("зачисление зарплаты (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Enrollment); }
        { put("зачисление аванса (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Enrollment); }
        { put("отмена покупки (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Enrollment); }
        { put("отмена покупки (\\d+(.{1}\\d{2})?)usd", TransactionTypeEnum.EnrollmentUSD); }
        { put("возврат покупки (\\d+(.{1}\\d{2})?)р", TransactionTypeEnum.Enrollment); }
    };
}
