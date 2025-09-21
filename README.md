# Decentralized Student Identity System (DSIS)

This is a simple, browser-based decentralized application (dApp) for managing student identities and certificates on a blockchain. It serves as a starter project demonstrating key Web3 development concepts using ethers.js and a Solidity smart contract.

The dApp provides a user-friendly interface for:

🔗 Connecting to a Web3 wallet like MetaMask.

📝 Registering a new student with a unique ID and name on the blockchain.

➕ Adding academic or extracurricular certificates by computing a secure hash of the file and immutably storing it on-chain.

🔍 Verifying a certificate's authenticity by retrieving its hash from the blockchain for comparison.

🖼️ Generating a QR code containing transaction details for easy sharing and verification.

## 🚀 Getting Started
To get this dApp running, you need a smart contract deployed on an Ethereum-compatible network (like Ethereum, Polygon, or BSC) and a browser with a Web3 wallet extension.

Prerequisites
A Web3 wallet (e.g., MetaMask).

Your deployed smart contract's address and its ABI (Application Binary Interface).

## Setup
### Download: Save the provided HTML file as index.html.

### Configure: Open index.html in a text editor and locate the `` section.

### Paste: Replace the placeholder values with your contract's details:

JavaScript

const CONTRACT_ADDRESS = "YOUR_DEPLOYED_CONTRACT_ADDRESS_HERE";
const CONTRACT_ABI = [ YOUR_ABI_ARRAY_HERE ];
Launch: Open the file in your Web3-enabled browser.

## 🛠️ How It Works
Connect Wallet
The Connect MetaMask button initiates a connection request using ethers.js. Once connected, it displays the public address of the user's wallet.

##### Register Student
This action calls the registerStudent function on your smart contract, creating a permanent, on-chain record of a student's identity linked to their wallet address.

##### Add Certificate
Hashing: The dApp uses the browser's native Web Crypto API to calculate a SHA-256 hash of any chosen file (e.g., PDF or image). This unique digital fingerprint ensures the file's integrity.

On-Chain Storage: The addCertificate function is then called, which stores this hash on the blockchain, linked to the student and the certificate's name.

###### Verify Certificate
Anyone can use the Verify Certificate section to query the blockchain. By providing the student ID and certificate name, they can retrieve the stored hash. This hash can be compared to the hash of the original file to cryptographically prove its authenticity.

Transaction QR Code
After a certificate is added, a QR code is generated. This QR code contains key details like the student ID and the transaction hash, allowing for quick, verifiable lookups on the blockchain.

### Licensed by Google

🎨 Technology Stack
Frontend: The interface is built with a single HTML file, styled using Tailwind CSS for a modern, responsive design.

Web3 Integration: Ethers.js (v6) is the core library for all blockchain interactions, from connecting wallets to sending transactions.

Utility: QRCode.js is used for generating scannable QR codes.

Security: File hashing is handled client-side using the browser's built-in Web Crypto API.

🛡️ Important Notice
This project is a development starter kit and should be considered a proof-of-concept. For production environments, a more robust and secure smart contract is recommended, with proper access controls and comprehensive error handling.
