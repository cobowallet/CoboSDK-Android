
# CoboSDK-Android
CoboSDK help develpers sign and broadcast ethereum transactions using [Cobo Wallet](https://cobo.com/).
Dapp developers can use CoboSDK to get user's ethereum addresses, sign messages, validate the signature, send transaction and call contract. Without the needs of developing private key management and wallet functions.

## Get Started

### Add dependency
In ```build.gradle``` file，add the following line in ```dependencies```:
```groovy
implementation 'com.cobo:sdk:1.0'
```

### Initialize CoboSDK
```java
CoboSDK.getInstance().initialize(this);
```

### Handling callbacks
 1. Add `CBEntryActivity`

Create a `coboapi` package under your application package. Then create a new android activity named `CBEntryActivity` in `coboapi` package.

![Add Entry Activity](entry-activity.png)

Add ```android:exported="true"``` and ```android:launchMode="singleTask"``` properties to ```CBEntryActivity``` in AndroidManifest.xml.

```xml
<activity
    android:name=".coboapi.CBEntryActivity"
    android:exported="true"
    android:launchMode="singleTask" />
```

 2. Implement `onCreate` method in `CBEntryActivity`

```java
@Override  
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cbentry);

    Intent intent = getIntent();
    CoboSDK.getInstance().handleIntent(intent);
    finish();
}
```

## Usage

### Sign message
```java
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
```

### Send transaction
```java
BigInteger gasPrice = new BigInteger("1000000000"); // 1 Gwei
BigInteger gasLimit = new BigInteger("21000");
BigInteger value = new BigInteger("1000000000000000000"); // 1 ETH
String from = <replace with address in sign message result>
String to = <replace with address to transfer to>
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
```

For more use cases please refer to the  ```Example``` project.