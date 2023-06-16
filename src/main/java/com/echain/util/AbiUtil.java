package com.echain.util;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class AbiUtil {
    public static String encodeMint(String toAddress, BigInteger tokenId){
        Address to = new Address(toAddress);
        Uint256 tokenId256 = new Uint256(tokenId);
        Function function = new Function(
                "mint",                      // Function name
                Arrays.asList(to,tokenId256),         // Input parameters
                Collections.emptyList());  // Output parameter(s)

        return FunctionEncoder.encode(function);
    }

    public static String encodeTransferFrom(String from,String to,BigInteger tokenId){
        Address fromA = new Address(from);
        Address toA = new Address(to);
        Uint256 tokenId256 = new Uint256(tokenId);
        Function function = new Function(
                "transferFrom",                      // Function name
                Arrays.asList(fromA,toA,tokenId256),         // Input parameters
                Collections.emptyList());  // Output parameter(s)

        return FunctionEncoder.encode(function);
    }

    public static String encodeBurn(BigInteger tokenId){
        Uint256 tokenId256 = new Uint256(tokenId);
        Function function = new Function(
                "burn",                      // Function name
                Arrays.asList(tokenId256),         // Input parameters
                Collections.emptyList());  // Output parameter(s)

        return FunctionEncoder.encode(function);
    }

    public static String encodeSetApproveForAll(String operator,boolean approved){
        Function function = new Function(
                "setApprovalForAll",
                Arrays.asList(new Address(operator),
                        new Bool(approved)),
                Collections.emptyList());
        return FunctionEncoder.encode(function);
    }
}
