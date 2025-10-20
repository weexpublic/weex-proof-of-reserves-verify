package com.por;

import com.alibaba.fastjson.JSON;
import com.por.utils.HashUtils;
import com.por.utils.MerkleRootUtils;
import com.por.vo.Metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Falan
 * @date 2025-10-19 21:04:29
 */
public class VerifyBalance {

    /**
     * 获取全部资源文件
     * @param path      path
     * @return
     */
    public List<String> getAllResourceFiles(String path) {

        List<String> fileList = new ArrayList<>();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resourceUrl = classLoader.getResource(path);

            if (null == resourceUrl) {
                return fileList;
            }

            File resourceDir = new File(resourceUrl.toURI());
            File[] files = resourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getName());
                }
            }
        } catch (Exception e) {
        }
        return fileList;
    }

    /**
     * 读取文件内容
     * @param path      path
     * @param fileName  file name
     */
    private void readFileContent(String path,String fileName,List<String> userBalanceStrings,String ignore,boolean calcHash){
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(path + "/" + fileName);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {

                    if (ignore.equals(line)){
                        continue;
                    }

                    userBalanceStrings.add(calcHash ? HashUtils.calcHash(line) : line);
                }
                reader.close();
            }
        }catch (Exception e){
        }
    }

    /**
     * 获取元数据
     * @param path      path
     * @return
     */
    private Metadata getMetadata(String path){

        try{
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(path + "/metadata");

            if (null != inputStream){
                String bytes = new String(inputStream.readAllBytes());

                Metadata metadata = JSON.parseObject(bytes, Metadata.class);

                inputStream.close();

                //验证元数据是否合法
                Metadata.verify(metadata);

                return metadata;
            }
        }catch (Exception e){
        }

        return null;
    }

    /**
     * 检查文件的完整性
     * @param path      path
     * @param files     files
     * @param metadata  metadata
     */
    private void checkFileIntegrity(String path,List<String> files,Metadata metadata){

        //1.必须包含metadata文件
        if (!files.contains("metadata")){
            throw new IllegalStateException("metadata file not exist");
        }

        //2.用户余额资产文件数量是否匹配
        long userBalanceCount = files.stream().filter(x -> x.startsWith("por_USER")).count();
        if (metadata.getUserFileCount() != (int) userBalanceCount){
            throw new IllegalStateException("user balance count not match");
        }

        //3.平台余额资产文件数量是否匹配
        long platformBalanceCount = files.stream().filter(x -> x.startsWith("por_PLATFORM")).count();
        if (metadata.getPlatformFileCount() != (int) platformBalanceCount){
            throw new IllegalStateException("platform balance count not match");
        }

        System.out.printf("[%s][1/4]%40s%n", path,"元数据校验成功");
    }

    /**
     * 过滤文件
     * @param pattern       pattern
     * @param files         files
     * @return
     */
    private List<String> getFilterFiles(String pattern,List<String> files){

        return files.stream().filter(x -> x.startsWith(pattern)).sorted((o1, o2) -> {

            String[] split1 = o1.split("_");
            String[] split2 = o2.split("_");

            if (5 != split1.length || 5 != split2.length){
                throw new IllegalStateException("illegal user file name size");
            }

            String fileName1 = split1[4];
            String fileName2 = split2[4];

            String[] fileSplit1 = fileName1.split("-");
            String[] fileSplit2 = fileName2.split("-");

            if (2 != fileSplit1.length || 2 != fileSplit2.length){
                throw new IllegalStateException("illegal user sort length");
            }

            Integer sort1 = Integer.parseInt(fileSplit1[0]);
            Integer sort2 = Integer.parseInt(fileSplit2[0]);

            return sort1 - sort2;
        }).collect(Collectors.toList());
    }

    /**
     * 检查用户余额是否合法
     * @param path      path
     * @param files     files
     * @param metadata  metadata
     */
    private void checkUserBalanceLegal(String path,List<String> files,Metadata metadata){

        //获取文件列表
        List<String> userBalanceFiles  = getFilterFiles("por_USER",files);

        //读取文件内容
        List<String> userBalanceStrings = new ArrayList<>();
        for (String file : userBalanceFiles){
            readFileContent(path,file,userBalanceStrings,"POR[user]",true);
        }

        //计算merkle根
        String merkleRoot = MerkleRootUtils.getMerkleRoot(userBalanceStrings);

        //验证merkle根是否合法
        System.out.printf("[%s][2/4]%40s%n", path,"用户merkle根校验" + (metadata.getUserMerkleRoot().equals(merkleRoot) ? "成功" : "失败"));
    }

    /**
     * 检查平台余额是否合法
     * @param path      path
     * @param files     files
     * @param metadata  metadata
     */
    private void checkPlatformBalanceLegal(String path,List<String> files,Metadata metadata){

        //获取文件列表
        List<String> userBalanceFiles  = getFilterFiles("por_PLATFORM",files);

        //读取文件内容
        List<String> userBalanceStrings = new ArrayList<>();
        for (String file : userBalanceFiles){
            readFileContent(path,file,userBalanceStrings,"POR[platform]",true);
        }

        //计算merkle根
        String merkleRoot = MerkleRootUtils.getMerkleRoot(userBalanceStrings);


        //验证merkle根是否合法
        System.out.printf("[%s][3/4]%40s%n", path,"平台merkle根校验" + (metadata.getPlatformMerkleRoot().equals(merkleRoot) ? "成功" : "失败"));
    }

    /**
     * 获取用户余额
     * @param path      path
     * @param files     files
     * @param metadata  metadata
     * @return
     */
    private void getUserBalance(String path,List<String> files,Metadata metadata){

        //获取文件列表
        List<String> userBalanceFiles  = getFilterFiles("por_USER",files);

        //读取文件内容
        List<String> userBalanceStrings = new ArrayList<>();
        for (String file : userBalanceFiles){
            readFileContent(path,file,userBalanceStrings,"POR[user]",false);
        }

        //遍历获取到用户余额
        BigDecimal balance = BigDecimal.ZERO;
        boolean calc = false;
        int n = metadata.getN();
        for (String balanceString : userBalanceStrings){

            if (!balanceString.startsWith(metadata.getUserHeadHash()) && !calc){
                continue;
            }

            calc = true;

            if (n-- > 0){
                balance = balance.add(new BigDecimal(balanceString.split(",")[1]));
            }
        }

        System.out.printf("[%s][4/4]%40s%n",path,String.format("用户%s资产为%s.",metadata.getUserId(),balance));
    }

    public static void main(String[] args) {

        if (1 > args.length){
            throw new IllegalArgumentException("args length must be more than 1.");
        }

        String path = args[0];

        VerifyBalance verifyBalance = new VerifyBalance();

        //1.获取目录下所有文件
        List<String> files = verifyBalance.getAllResourceFiles(path);

        //2.获取元数据
        Metadata metadata = verifyBalance.getMetadata(path);

        //3.检查文件的完整性
        verifyBalance.checkFileIntegrity(path,files,metadata);

        //4.验证用户余额合法性
        verifyBalance.checkUserBalanceLegal(path,files,metadata);

        //5.验证平台余额合法性
        verifyBalance.checkPlatformBalanceLegal(path,files,metadata);

        //6.获取用户余额
        verifyBalance.getUserBalance(path,files,metadata);
    }
}
