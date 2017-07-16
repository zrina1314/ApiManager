/**
 * 用途：加解密服务
 * 描述：与EGMAS通信的加解密服务
 * 作者：花心大萝卜
 * 创建时间：2016/11/23
 */
app.factory('encryptionService', function () {
  return {
    encrypt: function (encodeStr) {
      return encryptExecute(encodeStr);
    },
    decrypt: function (decodeStr) {
      return decryptExecute(decodeStr);
    }
  };
});

var teaKey = [267461445, -2023406815, -1988933582, -1412567294];
/**
 * 执行加密过程，可优化点把_base64Util和_teaUtil写成全局的
 * @param encodeStr
 * @returns {null}
 */
function encryptExecute(encodeStr) {
  var _base64Util = new Base64Util();
  var _teaUtil = new TeaUtil();
  try {
    var tempTeaBytes = _teaUtil.encryptByTea(encodeStr, teaKey);
    var tempResult = _base64Util.encode(tempTeaBytes);
    return tempResult;
  } catch (var3) {
    return null;
  }
}

/**
 * 执行解密过程，可优化点把_base64Util和_teaUtil写成全局的
 * @param decodeStr
 * @returns {null}
 */
function decryptExecute(decodeStr) {
  var _base64Util = new Base64Util();
  var _teaUtil = new TeaUtil();
  try {
    var tempBytes = _base64Util.decode(decodeStr);
    var result = _teaUtil.decrypByTea(tempBytes, teaKey);
    return result;
  } catch (var2) {
    return null;
  }
}

