// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract SimpleStorage {
    uint256 public data;

    event DataChanged(uint256 indexed newValue, address indexed setter);

    function setData(uint256 _data) external {
        data = _data;
        emit DataChanged(_data, msg.sender);
    }
}
