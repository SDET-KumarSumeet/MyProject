SELECT  identifier[0].id AS MBA_ACCOUNT_DB,    identifier[1].id AS PNI_ACCOUNT_DB,    billDelivery[0].channel.`value` AS CHANNEL1_DB,    billDelivery[0].status.`value` AS STATUS1_DB,    CASE WHEN DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"day") <10 THEN "0" || TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"day")) ELSE TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"day")) END || "/" || CASE WHEN DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"month") <10 THEN "0" || TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"month")) ELSE TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"month")) END || "/"|| TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`startTimeStamp`,"year")) AS VALID_FOR_FROM1_DB,       CASE WHEN DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"day") <10 THEN "0" || TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"day")) ELSE TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"day")) END || "/" || CASE WHEN DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"month") <10 THEN "0" || TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"month")) ELSE TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"month")) END || "/"|| TO_STRING(DATE_PART_STR(billDelivery[0].validFor.`endTimeStamp`,"year")) AS VALID_FOR_TO1_DB,    CASE WHEN billDelivery[0].isLegal='true' then 'Y' ELSE 'N' END AS IS_LEGAL1_DB,    case when billDelivery[0].address is NOT null then billDelivery[0].address  else null END ADDRESS1_DB,    billDelivery[0].addressType.`value` AS ADDRESSTYPE1_DB  FROM `Core` A WHERE META().id LIKE 'BA-%' and billDelivery[0].channel.`value` ='EMAIL' and billDelivery[0].status.`value` ='ACTIVE' AND NOT EXISTS (SELECT B.identifier[1].id AS MBA_ACCOUNT_DB FROM `Core` B USE KEYS A.identifier[0].id WHERE B.billDelivery[0].channel.`value` ='SMS'  AND B.billDelivery[0].status.`value` ='ACTIVE' ) LIMIT 5