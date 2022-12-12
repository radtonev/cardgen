# Cardgen

Setup:
1. Clone the repository
2. Install maven dependencies
3. Configure all necessary parameters as you need them in CardGenerator.java

```
    private static final String HOST = "http://localhost:8080/ppp/"; //Integration: APIConstants.Environment.INTEGRATION_HOST
    private static final Constants.HashAlgorithm HASH_ALGO = Constants.HashAlgorithm.MD5;
    private static final String MERCHANT_KEY = "";
    private static final String MERCHANT_SITE = "";
    private static final String MERCHANT_ID = "";
    private static final String NAME_ON_CARD = "";
    private static final String EXPIRATION_MONTH = "05";
    private static final String EXPIRATION_YEAR = "25";
    private static final String TEST_AMOUNT = "1";
    private static final String DEFAULT_USER_TOKEN = "darin";
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String VISA_BIN = "4";
    private static final String MC_BIN = "5";
    private static final String CURRENT_BIN = VISA_BIN; //Change this to MC_BIN for mastercard
```

4. Run CardGenerator.main() as java application

Example output:
```
4478734269171566 -> ERROR Reason: Issuer data is not available, please try again later
4675490917699786 -> DECLINED Reason: Invalid Issue Number
4182852409802978 -> ERROR Reason: Issuer data is not available, please try again later
4046672469225167 -> ERROR Reason: Issuer data is not available, please try again later
4481095822447975 -> ERROR Reason: Issuer data is not available, please try again later
Working card found = 4031999844223139
Working card found = 4031991586674066
Working card found = 4031997372666620
Working card found = 4031993252152435
Working card found = 4031997865862975
Working card found = 4031996353402856
```
