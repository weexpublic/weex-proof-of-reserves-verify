# weex-proof-of-reserves-verify
weex平台资产证明

### prepare

#### 1.make sure Java11(or above) installed on your platform
![java.png](src/main/resources/image/java.png)
#### 2.make sure your memory is more than 4G

### verify detail

#### 1.make sure you are on login status
![login.png](src/main/resources/image/login.png)

#### 2.download weex-proof-of-reserves-verify from github

```
git clone https://github.com/weexpublic/weex-proof-of-reserves-verify.git
```

### 3.download user balance data
![img.png](src/main/resources/image/user_balance.png)

### 4.download platform balance data
![img.png](src/main/resources/image/platform_balance.png)

### 5.create a folder name asset(such as "BTC") on resources
![img.png](src/main/resources/image/btc_folder.png)

### 6.create a file named `metadata` and copy metadata on it
![img.png](src/main/resources/image/metadata.png)
![img.png](src/main/resources/image/copy_metadata.png)

### 7.put all data on `BTC` folder
![img.png](src/main/resources/image/data_structure.png)

### 7.compile and run
#### parameter
![img.png](src/main/resources/image/verify_parameter.png)

#### result
![img_1.png](src/main/resources/image/verify_result.png)