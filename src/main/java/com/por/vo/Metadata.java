package com.por.vo;

/**
 * @author Falan
 * @date 2025-10-19 21:46:53
 */
public class Metadata {

    /**
     * 用户merkle根
     */
    private String userMerkleRoot;
    /**
     * 用户文件数量
     */
    private Integer userFileCount;
    /**
     * 平台merkle根
     */
    private String platformMerkleRoot;
    /**
     * 平台文件数量
     */
    private Integer platformFileCount;
    /**
     * 用户第一条数据hash
     */
    private String userHeadHash;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户分片数
     */
    private Integer n;

    public static void verify(Metadata origin){

        if (null == origin || null == origin.getUserMerkleRoot() || null == origin.getUserFileCount() || null == origin.getPlatformMerkleRoot() || null == origin.getPlatformFileCount() || null == origin.getUserHeadHash() || null == origin.getUserId() || null == origin.getN()){
            throw new IllegalArgumentException("empty param exist.");
        }
    }

    public Metadata(String userMerkleRoot, Integer userFileCount, String platformMerkleRoot, Integer platformFileCount, String userHeadHash, String userId, Integer n) {
        this.userMerkleRoot = userMerkleRoot;
        this.userFileCount = userFileCount;
        this.platformMerkleRoot = platformMerkleRoot;
        this.platformFileCount = platformFileCount;
        this.userHeadHash = userHeadHash;
        this.userId = userId;
        this.n = n;
    }

    public String getUserMerkleRoot() {
        return userMerkleRoot;
    }

    public void setUserMerkleRoot(String userMerkleRoot) {
        this.userMerkleRoot = userMerkleRoot;
    }

    public Integer getUserFileCount() {
        return userFileCount;
    }

    public void setUserFileCount(Integer userFileCount) {
        this.userFileCount = userFileCount;
    }

    public String getPlatformMerkleRoot() {
        return platformMerkleRoot;
    }

    public void setPlatformMerkleRoot(String platformMerkleRoot) {
        this.platformMerkleRoot = platformMerkleRoot;
    }

    public Integer getPlatformFileCount() {
        return platformFileCount;
    }

    public void setPlatformFileCount(Integer platformFileCount) {
        this.platformFileCount = platformFileCount;
    }

    public String getUserHeadHash() {
        return userHeadHash;
    }

    public void setUserHeadHash(String userHeadHash) {
        this.userHeadHash = userHeadHash;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }
}
