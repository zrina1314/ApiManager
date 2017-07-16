/**
 * 用途：Base64加解密
 * 描述：只有对二进制数据进行加密，使用场景：与EGMAS交互使用
 * 作者：花心大萝卜
 * 创建时间：2016/11/23
 */
function Base64Util() {
  this.last2byte = String.fromCharCode(parseInt("00000011", 2));
  this.last4byte = String.fromCharCode(parseInt("00001111", 2));
  this.last6byte = String.fromCharCode(parseInt("00111111", 2));
  this.lead6byte = String.fromCharCode(parseInt("11111100", 2));
  this.lead4byte = String.fromCharCode(parseInt("11110000", 2));
  this.lead2byte = String.fromCharCode(parseInt("11000000", 2));
  this.encodeTable = ['A', 'B', 'C', 'D',
    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
    'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
    '4', '5', '6', '7', '8', '9', '+', '/'];

  this._keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

  /**
   * Base64 encoding.
   * @param bytes The src data.
   * @return
   */
  this.encode = function (bytesSource) {
    try {
      var to = "";
      var num = 0;
      var currentByte = 0;
      for (var i = 0; i < bytesSource.length; i++) {
        num = num % 8;
        while (num < 8) {
          switch (num) {
            case 0:
              currentByte = bytesSource[i] & this.lead6byte.charCodeAt(0);
              currentByte = (currentByte >>> 2);
              break;
            case 2:
              currentByte = (bytesSource[i] & this.last6byte.charCodeAt(0));
              break;
            case 4:
              currentByte = (bytesSource[i] & this.last4byte.charCodeAt(0));
              currentByte = (currentByte << 2);
              if ((i + 1) < bytesSource.length) {
                currentByte |= (bytesSource[i + 1] & this.lead2byte.charCodeAt(0)) >>> 6;
              }
              break;
            case 6:
              currentByte = bytesSource[i] & this.last2byte.charCodeAt(0);
              currentByte = (currentByte << 4);
              if ((i + 1) < bytesSource.length) {
                currentByte |= (bytesSource[i + 1] & this.lead4byte.charCodeAt(0)) >>> 4;
              }
              break;
          }
          to += this.encodeTable[currentByte];
          num += 6;
        }
      }
      if (to.length % 4 != 0) {
        for (var i = 4 - to.length % 4; i > 0; i--) {
          to += "=";
        }
      }
      var result = to.toString();
      to = null;
    } catch (e) {
      return null;
    }
    return result;
  }



  /**
   * Base64 decode.
   * @param from The src data.
   * @return
   */
  this.decode = function (input) {
    //get last chars to see if are valid
    var lkey1 = this._keyStr.indexOf(input.charAt(input.length - 1));
    var lkey2 = this._keyStr.indexOf(input.charAt(input.length - 2));

    var bytes = Math.ceil((3 * input.length) / 4.0);
    if (lkey1 == 64) bytes--; //padding chars, so skip
    if (lkey2 == 64) bytes--; //padding chars, so skip

    var uarray = new Int8Array(bytes);
    var chr1, chr2, chr3;
    var enc1, enc2, enc3, enc4;
    var i = 0;
    var j = 0;

    input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

    for (i = 0; i < bytes; i += 3) {
      //get the 3 octects in 4 ascii chars
      enc1 = this._keyStr.indexOf(input.charAt(j++));
      enc2 = this._keyStr.indexOf(input.charAt(j++));
      enc3 = this._keyStr.indexOf(input.charAt(j++));
      enc4 = this._keyStr.indexOf(input.charAt(j++));

      chr1 = (enc1 << 2) | (enc2 >> 4);
      chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
      chr3 = ((enc3 & 3) << 6) | enc4;

      uarray[i] = chr1;
      if (enc3 != 64) uarray[i + 1] = chr2;
      if (enc4 != 64) uarray[i + 2] = chr3;
    }
    return uarray;
  }
}
