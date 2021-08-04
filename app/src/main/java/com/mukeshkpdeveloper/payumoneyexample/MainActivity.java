package com.mukeshkpdeveloper.payumoneyexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.activities.PayUmoneyActivity;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView mTxvProductPrice, mTxvBuy;

  /*  String txnid ="txt12346", amount ="20", phone ="7770878087",
            productinfo ="BlueApp Course", firstname ="yogi", email ="yogiii363@gmail.com",
            merchantId ="5884494", merchantkey="qZtEgloC";
    private String salt = "BuvBXdsVpm";*/

    //for my credentials
    String txnid =System.currentTimeMillis() + "", amount ="20", phone ="7803994667",
            productinfo ="Nike Power", firstname ="Mukesh", email ="kumarmukeshpatel57@gmail.com",
            merchantId ="8250101", merchantkey="gtKFFx";//merchant key issue
    private String salt = "eCwWELxi";

    String hashSequence;
    PayUmoneySdkInitializer.PaymentParam paymentParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        String priceNo = getString(R.string.txt_product_price);
        String price = getResources().getString(R.string.Rupees) + priceNo;
        mTxvProductPrice.setText(price);

        mTxvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxvBuy.setEnabled(false);

                //its working
//                launchPaymentFlow();

                //from my credentials
                payment();
            }
        });

    }

    private void payment() {
            hashSequence=merchantkey+"|"+txnid+"|"+amount+"|"+productinfo+"|"+firstname+"|"+email+"|"
                    +"udf1"+"|"+"udf2"+"|"+"udf3"+"|"+"udf4"+"|"+"udf5"+"|"+"udf6"+"|"+"udf7"+"|"+"udf8"+"|"
                    +"udf9"+"|"+"udf10"+"|"+salt;

            String serverCalculatedHash= getHashkey("SHA-512", hashSequence);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        builder.setAmount(amount)
                .setTxnId(txnid)
                .setPhone(phone)
                .setProductName(productinfo)
                .setFirstName(firstname)
                .setEmail(email)
                .setsUrl(Constants.SURL)
                .setfUrl(Constants.FURL)
                .setUdf1("udf1")
                .setUdf2("udf2")
                .setUdf3("udf3")
                .setUdf4("udf4")
                .setUdf5("udf5")
                .setUdf6("udf6")
                .setUdf7("udf7")
                .setUdf8("udf8")
                .setUdf9("udf9")
                .setUdf10("udf10")
                .setIsDebug(false) // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)
                .setMerchantId(merchantId);

        try {
            paymentParam = builder.build();
            paymentParam.setMerchantHash(serverCalculatedHash);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "e: "+e.toString());
        }

        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, MainActivity.this,
                R.style.AppTheme_default, false);
    }


    private void initViews() {
        mTxvProductPrice = findViewById(R.id.txv_product_price);
        mTxvBuy = findViewById(R.id.txt_buy_product);
    }

    private void launchPaymentFlow() {
        hashSequence=merchantkey+"|"+txnid+"|"+amount+"|"+productinfo+"|"+firstname+"|"+email+"|"
                +"udf1"+"|"+"udf2"+"|"+"udf3"+"|"+"udf4"+"|"+"udf5"+"|"+"udf6"+"|"+"udf7"+"|"+"udf8"+"|"
                +"udf9"+"|"+"udf10"+"|"+salt;

        String serverCalculatedHash= getHashkey("SHA-512", hashSequence);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        builder.setAmount(amount)
                .setTxnId(txnid)
                .setPhone(phone)
                .setProductName(productinfo)
                .setFirstName(firstname)
                .setEmail(email)
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("udf1")
                .setUdf2("udf2")
                .setUdf3("udf3")
                .setUdf4("udf4")
                .setUdf5("udf5")
                .setUdf6("udf6")
                .setUdf7("udf7")
                .setUdf8("udf8")
                .setUdf9("udf9")
                .setUdf10("udf10")
                .setIsDebug(false) // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)
                .setMerchantId(merchantId);

        try {
            paymentParam = builder.build();
            paymentParam.setMerchantHash(serverCalculatedHash);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "sent1: "+e.toString());
        }

        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, MainActivity.this,
                R.style.AppTheme_default, false);
    }


    // Method to create hash
    public static String getHashkey(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("PaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("TAG", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("TAG", "Both objects are null!");
            }
        }
    }

    private Double convertStringToDouble(String str) {
        return Double.parseDouble(str);
    }

}