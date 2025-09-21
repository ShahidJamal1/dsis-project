# Student Certificate Verification System (Spring Boot + MongoDB + Web3j)

Backend service for student registration, certificate upload with SHA-256 hashing, on-chain hash anchoring (Ethereum/Polygon testnet via Web3j), and verification.

## Tech Stack
- Java 17
- Spring Boot 3
- MongoDB (Spring Data MongoDB)
- Web3j (Ethereum/Polygon)
- Validation (Jakarta Validation)
- Swagger/OpenAPI via springdoc
- Lombok

## Endpoints
- POST `/api/students/register`
- POST `/api/students/{id}/certificates` (multipart form-data: file)
- GET  `/api/students/{id}/certificates/{certName}/verify`
- GET  `/api/students/{id}/profile`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Configuration
Set via environment variables or edit `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/scv}

file:
  storageDir: ${FILE_STORAGE_DIR:uploads}

web3:
  rpcUrl: ${WEB3_RPC_URL:https://rpc-mumbai.maticvigil.com}
  privateKey: ${WEB3_PRIVATE_KEY:replace_with_private_key}
  chainId: ${WEB3_CHAIN_ID:80001}
  contractAddress: ${CONTRACT_ADDRESS:0x0000000000000000000000000000000000000000}
```

Required env vars for testnet:
- `WEB3_RPC_URL` (e.g., Alchemy/Infura/MaticVigil endpoint for Mumbai)
- `WEB3_PRIVATE_KEY` (funded testnet account)
- `CONTRACT_ADDRESS` (deployed contract)

## Run
```bash
mvn spring-boot:run
```

Or build jar:
```bash
mvn clean package
java -jar target/student-cert-verifier-0.0.1-SNAPSHOT.jar
```

## Sample cURL
Register student:
```bash
curl -X POST http://localhost:8080/api/students/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aisha",
    "rollNo": "CS-001",
    "course": "B.Tech CSE",
    "skills": ["Java","Spring"],
    "projects": ["SCV App"],
    "achievements": ["Hackathon Winner"]
  }'
```

Upload certificate:
```bash
STUDENT_ID=... # from register response
curl -X POST "http://localhost:8080/api/students/$STUDENT_ID/certificates" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/certificate.pdf"
```

Verify certificate:
```bash
curl -X GET "http://localhost:8080/api/students/$STUDENT_ID/certificates/certificate.pdf/verify"
```

Get profile:
```bash
curl -X GET "http://localhost:8080/api/students/$STUDENT_ID/profile"
```

## MongoDB Document Structure (example)

Collection: `students`
```json
{
  "_id": "66f3...",
  "name": "Aisha",
  "rollNo": "CS-001",
  "course": "B.Tech CSE",
  "skills": ["Java","Spring"],
  "projects": ["SCV App"],
  "achievements": ["Hackathon Winner"],
  "certificates": [
    {
      "fileName": "certificate.pdf",
      "sha256Hash": "0xabc...",
      "transactionHash": "0xdef...",
      "storagePath": "uploads/66f3.../certificate.pdf",
      "verified": false,
      "uploadedAt": "2025-09-21T06:45:00Z"
    }
  ]
}
```

## Smart Contract Integration (Web3j)
This project uses raw ABI encoding to call functions. It expects a contract with methods like:

```solidity
// Example interface
function setCertificateHash(string studentId, string fileName, bytes32 hash) external;
function getCertificateHash(string studentId, string fileName) external view returns (bytes32);
```

- On upload, we compute SHA-256 of the file, left-pad to `bytes32`, and submit via `setCertificateHash`.
- On verify, we `eth_call` `getCertificateHash` and compare with the local SHA-256 of the stored file.

If your contract differs, update `METHOD_SET` and `METHOD_GET` and the parameters in `BlockchainService`.

## Notes
- SHA-256 is used (not Keccak-256). Ensure your contract stores the same hash computed off-chain.
- Mumbai testnet: chainId `80001`. If using Amoy/Polygon PoS or Sepolia, adjust `web3.chainId` and RPC.
- For production, consider storing files in S3/Blob storage instead of local disk and add auth.

## License
MIT
