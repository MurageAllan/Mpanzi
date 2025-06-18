package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class SavingsAccountData(
    @SerializedName("client_id") val clientId: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("account_no") val accountNumber: String,
    @SerializedName("submittedon_date") val submittedOnDate: String,
    @SerializedName("deposit_type_enum") val depositTypeEnum: Int,
    @SerializedName("currency_code") val currencyCode: String,
    @SerializedName("currency_digits") val currencyDigits: Int,
    @SerializedName("currency_multiplesof") val currencyMultiplesOf: Int,
    @SerializedName("nominal_annual_interest_rate") val nominalAnnualInterestRate: String,
    @SerializedName("interest_compounding_period_enum") val interestCompoundingPeriodEnum: Int,
    @SerializedName("interest_posting_period_enum") val interestPostingPeriodEnum: Int,
    @SerializedName("interest_calculation_type_enum") val interestCalculationTypeEnum: Int,
    @SerializedName("interest_calculation_days_in_year_type_enum") val interestCalculationDaysInYearTypeEnum: Int,
    @SerializedName("min_required_opening_balance") val minRequiredOpeningBalance: Double?,
    @SerializedName("lockin_period_frequency") val lockInPeriodFrequency: String,
    @SerializedName("lockin_period_frequency_enum") val lockInPeriodFrequencyEnum: Int,
    @SerializedName("withdrawal_fee_for_transfer") val withdrawalFeeForTransfer: Int,
    @SerializedName("allow_overdraft") val allowOverdraft: Boolean,
    @SerializedName("overdraft_limit") val overdraftLimit: String,
    @SerializedName("nominal_annual_interest_rate_overdraft") val nominalAnnualInterestRateOverdraft: String,
    @SerializedName("min_overdraft_for_interest_calculation") val minOverdraftForInterestCalculation: String,
    @SerializedName("min_required_balance") val minRequiredBalance: String,
    @SerializedName("enforce_min_required_balance") val enforceMinRequiredBalance: Boolean,
    @SerializedName("min_balance_for_interest_calculation") val minBalanceForInterestCalculation: String,
    @SerializedName("withhold_tax") val withholdTax: Int,
    @SerializedName("tax_group_id") val taxGroupId: Int?,
    @SerializedName("total_deposits_derived") val totalDepositsDerived: String,
    @SerializedName("account_balance_derived") val accountBalanceDerived: String,
    @SerializedName("id") val id: Int
)
