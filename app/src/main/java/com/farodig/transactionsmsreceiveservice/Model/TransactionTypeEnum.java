package com.farodig.transactionsmsreceiveservice.Model;

// Transaction Type
public enum TransactionTypeEnum {

    /// <summary>
    /// Не определена
    /// </summary>
    None,

    /// <summary>
    /// Покупка
    /// </summary>
    Purchase,

    /// <summary>
    /// Покупка, USD
    /// </summary>
    PurchaseUSD,

    /// <summary>
    /// Зачисление
    /// </summary>
    Enrollment,

    /// <summary>
    /// Зачисление, USD
    /// </summary>
    EnrollmentUSD,

    /// <summary>
    /// Списание
    /// </summary>
    WriteOff,

    /// <summary>
    /// Оплата
    /// </summary>
    Payment,

    /// <summary>
    /// Выдача наличных (Обналичить)
    /// </summary>
    CashOut;

    public boolean IsNegative()
    {
        return (this == TransactionTypeEnum.None
                || this == TransactionTypeEnum.Purchase
                || this == TransactionTypeEnum.PurchaseUSD
                || this == TransactionTypeEnum.WriteOff
                || this == TransactionTypeEnum.Payment
                || this == TransactionTypeEnum.CashOut);
    }

    public String ToSubcategory()
    {
        switch(this)
        {
            case None:
                return "Не определена";
            case CashOut:
                return "Выдача наличных";
            case Payment:
                return "Оплата";
            case Purchase:
                return "Покупка";
            case WriteOff:
                return "Перевод через телефон";
            case Enrollment:
                return "Зачисление";
            case PurchaseUSD:
                return "Покупка, USD";
            case EnrollmentUSD:
                return "Зачисление, USD";
            default:
                return "Не определена";
        }
    }
}
