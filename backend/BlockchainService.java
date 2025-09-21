package com.example.scv.service;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bytes32;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${web3.contractAddress}")
    private String contractAddress;

    @Value("${web3.chainId}")
    private long chainId;

    @Value("${web3.gas.maxFeeGwei:50}")
    private long maxFeeGwei;

    // Method names assumed. Adjust to match your deployed contract.
    private static final String METHOD_SET = "setCertificateHash";
    private static final String METHOD_GET = "getCertificateHash";

    public String storeCertificateHash(String studentId, String fileName, String sha256Hex) {
        try {
            byte[] hashBytes = hexToBytes32(sha256Hex);
            Function function = new Function(
                METHOD_SET,
                Arrays.asList(new Utf8String(studentId), new Utf8String(fileName), new Bytes32(hashBytes)),
                Collections.emptyList()
            );
            String data = FunctionEncoder.encode(function);

            TransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);
            BigInteger gasPrice = gweiToWei(maxFeeGwei);
            BigInteger gasLimit = BigInteger.valueOf(300_000);

            EthSendTransaction response = txManager.sendTransaction(gasPrice, gasLimit, contractAddress, data, BigInteger.ZERO);
            if (response.hasError()) {
                throw new RuntimeException("Blockchain tx error: " + response.getError().getMessage());
            }
            return response.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to store hash on blockchain", e);
        }
    }

    public Optional<String> getStoredHash(String studentId, String fileName) {
        try {
            Function function = new Function(
                METHOD_GET,
                Arrays.asList(new Utf8String(studentId), new Utf8String(fileName)),
                Arrays.asList(new TypeReference<Bytes32>() {})
            );
            String data = FunctionEncoder.encode(function);
            Transaction callTx = Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, data);
            EthCall resp = web3j.ethCall(callTx, DefaultBlockParameterName.LATEST).send();
            if (resp.isReverted() || resp.getValue() == null) {
                return Optional.empty();
            }
            var results = FunctionReturnDecoder.decode(resp.getValue(), function.getOutputParameters());
            if (results.isEmpty()) return Optional.empty();
            Bytes32 out = (Bytes32) results.get(0);
            String hex = bytes32ToHex(out.getValue());
            return Optional.of(hex);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static byte[] hexToBytes32(String hex) throws DecoderException {
        byte[] bytes = Hex.decodeHex(strip0x(hex));
        if (bytes.length > 32) throw new IllegalArgumentException("Hash length > 32 bytes");
        byte[] padded = new byte[32];
        // left-pad with zeros
        System.arraycopy(bytes, 0, padded, 32 - bytes.length, bytes.length);
        return padded;
    }

    private static String bytes32ToHex(byte[] bytes) {
        // remove left zero padding
        int i = 0;
        while (i < bytes.length - 1 && bytes[i] == 0) i++;
        byte[] unpadded = Arrays.copyOfRange(bytes, i, bytes.length);
        return "0x" + Hex.encodeHexString(unpadded);
    }

    private static String strip0x(String s) {
        if (s == null) return "";
        return s.startsWith("0x") || s.startsWith("0X") ? s.substring(2) : s;
    }

    private static BigInteger gweiToWei(long gwei) {
        return BigInteger.valueOf(gwei).multiply(BigInteger.TEN.pow(9));
    }
}
