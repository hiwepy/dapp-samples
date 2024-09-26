package com.github.hiwepy.dapp.param;

public class TonProofParam {

    private String proof;

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "TonProofParam{" +
                "proof='" + proof + '\'' +
                '}';
    }
}
