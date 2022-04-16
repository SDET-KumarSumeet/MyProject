SELECT
    [FIRST 5] (customer_nr)CUSTOMERNUMBER_DB,
    (AGREE_NR)             PA_DB,
    TRIM(
    (  SELECT  [FIRST 1] na_nr
        FROM   $DV1020.TDDBSJ.GS0NANU na1  /* 1NA-NUMBER */
        WHERE  SERVICE_PROVIDER_NR = 1
        --AND na1.customer_NR<620780502
        AND NA_NR_END_DATE = 99999999
        AND NA_NR LIKE '04%' ))PHONENUMBER_DB,
    TRIM(
    (  SELECT  [FIRST 1] na_nr
        FROM   $DV1020.TDDBSJ.GS0NANU na2 /* 2NA-NUMBER */
        WHERE  SERVICE_PROVIDER_NR = 1
        and na2.contract_nr>500000024
        --AND na2.customer_NR<620780502
        AND NA_NR_END_DATE = 99999999
        AND NA_NR LIKE 'EQP%' ))EQPNUMBER_DB
FROM  $DV0105.TDDBSJ.GS0BAFT PA  /* payment-agree */
      WHERE  SERVICE_PROVIDER_NR = 1
      --AND PA.CUSTOMER_NR<620780502
      AND AGREE_END_DATE = 99999999 
FOR browse access;