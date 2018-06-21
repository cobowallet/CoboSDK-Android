package com.example.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cobo.sdk.CoboSDK;
import com.cobo.sdk.deeplink.SignMessageAction;
import com.cobo.sdk.deeplink.SignMessageResult;
import com.cobo.sdk.deeplink.SignTransactionAction;
import com.cobo.sdk.deeplink.SignTransactionResult;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoboSDK.getInstance().initialize(this);
    }

    void signMessage() {
        final String message = "Hello Cobo!";
        CoboSDK.getInstance().signMessage(message, new SignMessageAction.ResultHandler() {
            @Override
            public void onResult(SignMessageResult result) {
                Log.i("DEMO", "result: " + result.address);
                boolean match = CoboSDK.getInstance().verifyMessage(result.address, result.signature, message);
                Log.i("DEMO", "match: " + match);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("DEMO", "error: " + code + " message: " + message);
            }
        }, this);
    }

    void signTransaction() {
        BigInteger gasPrice = new BigInteger("1000000000"); // 1 Gwei
        BigInteger gasLimit = new BigInteger("21000");
        BigInteger value = new BigInteger("1000000000000000000"); // 1 ETH
        String from = "0xaf2a164ab85c9171e4132a0e59bff81c468a254f"; // !!! <replace with address in sign message result>
        String to = "0xaf2a164ab85c9171e4132a0e59bff81c468a254f"; // !!! <replace with address to transfer to>
        RawTransaction tx = RawTransaction.createTransaction(null, gasPrice, gasLimit, to, value, null);
        CoboSDK.getInstance().sendTransaction(tx, from, new SignTransactionAction.ResultHandler() {
            @Override
            public void onResult(SignTransactionResult result) {
                Log.i("DEMO", "result: " + result.hash);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("DEMO", "error: " + code + " message: " + message);
            }
        }, this);
    }

    void callContract() {
        // To encode contract data, please refer to web3j documentation:
        // https://docs.web3j.io/transactions.html#transacting-with-a-smart-contract

        String contractAddress = "0x725DcFdf065d4e41839E0d3B6b15A7725551B93B"; // !!! <replace with your contract address>

        List<Type> inputs = new ArrayList<>();
        inputs.add(new Bool(true));
        Function function = new Function("bet", inputs, Collections.<TypeReference<?>>emptyList());

        String data = FunctionEncoder.encode(function);
        BigInteger gasPrice = new BigInteger("1000000000"); // 1 Gwei
        BigInteger gasLimit = new BigInteger("50000");
        String from = "0xaf2a164ab85c9171e4132a0e59bff81c468a254f"; // !!! <replace with address in sign message result>
        RawTransaction tx = RawTransaction.createTransaction(null, gasPrice, gasLimit, contractAddress, new BigInteger("0"), data);

        CoboSDK.getInstance().sendTransaction(tx, from, new SignTransactionAction.ResultHandler() {
            @Override
            public void onResult(SignTransactionResult result) {
                Log.i("DEMO", "result: " + result.hash);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("DEMO", "error: " + code + " message: " + message);
            }
        }, this);
    }
}
