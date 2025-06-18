package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class Loan(
    @SerializedName("client_id")
    val clientId: Int = 0,

    @SerializedName("product_id")
    val productId: Int = 0,

    @SerializedName("principal_amount")
    val principalAmount: String = "0",

    @SerializedName("principal_amount_proposed")
    val principalAmountProposed: String = "0",

    @SerializedName("approved_principal")
    val approvedPrincipal: String = "0",

    @SerializedName("loan_status_id")
    val loanStatusId: Int = 0,

    @SerializedName("account_no")
    val accountNumber: String = "",

    @SerializedName("currency_code")
    val currencyCode: String = "KES",

    @SerializedName("currency_digits")
    val currencyDigits: Int = 2,

    @SerializedName("currency_multiplesof")
    val currencyMultiplesOf: Int = 1,

    @SerializedName("arrearstolerance_amount")
    val arrearsToleranceAmount: String? = null,

    @SerializedName("fund_id")
    val fundId: Int = 0,

    @SerializedName("nominal_interest_rate_per_period")
    val interestRatePerPeriod: String = "0",

    @SerializedName("interest_period_frequency_enum")
    val interestPeriodFrequency: Int = 0,

    @SerializedName("annual_nominal_interest_rate")
    val annualInterestRate: String = "0",

    @SerializedName("interest_method_enum")
    val interestMethod: Int = 0,

    @SerializedName("interest_calculated_in_period_enum")
    val interestCalculatedInPeriod: Int = 0,

    @SerializedName("allow_partial_period_interest_calcualtion")
    val allowPartialInterestCalculation: Boolean = false,

    @SerializedName("repay_every")
    val repaymentFrequency: Int = 0,

    @SerializedName("repayment_period_frequency_enum")
    val repaymentPeriodFrequency: Int = 0,

    @SerializedName("number_of_repayments")
    val numberOfRepayments: Int = 0,

    @SerializedName("grace_on_principal_periods")
    val gracePrincipalPeriods: Int? = null,

    @SerializedName("recurring_moratorium_principal_periods")
    val moratoriumPrincipalPeriods: Int? = null,

    @SerializedName("grace_on_interest_periods")
    val graceInterestPeriods: Int? = null,

    @SerializedName("grace_interest_free_periods")
    val interestFreePeriods: Int? = null,

    @SerializedName("amortization_method_enum")
    val amortizationMethod: Int = 0,

    @SerializedName("loan_transaction_strategy_id")
    val transactionStrategyId: Int = 0,

    @SerializedName("external_id")
    val externalId: String? = null,

    @SerializedName("max_outstanding_loan_balance")
    val maxLoanBalance: String? = null,

    @SerializedName("grace_on_arrears_ageing")
    val graceArrearsAgeing: Int? = null,

    @SerializedName("days_in_month_enum")
    val daysInMonth: Int = 30,

    @SerializedName("days_in_year_enum")
    val daysInYear: Int = 365,

    @SerializedName("interest_recalculation_enabled")
    val interestRecalculationEnabled: Int = 0,

    @SerializedName("is_equal_amortization")
    val equalAmortization: Boolean = false,

    @SerializedName("m_fund")
    val fund: String? = null,

    @SerializedName("ref_loan_transaction_processing_strategy")
    val transactionProcessingStrategy: String? = null,

    @SerializedName("submittedon_date")
    val submittedDate: String? = null,

    @SerializedName("loan_type_enum")
    val loanType: Int = 0,

    @SerializedName("id")
    val id: Int = 0
)
