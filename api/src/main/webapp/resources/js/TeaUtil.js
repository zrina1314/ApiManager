/**
 * 用途：TEA加解密
 * 描述：
 * 作者：花心大萝卜
 * 创建时间：2016/11/26
 */
function TeaUtil() {
  this.encrypt = function (content, offset, key, times) {
    var tempInt = this.byteToInt(content, offset);
    var y = tempInt[0];
    var z = tempInt[1];
    var sum = 0;
    var delta = -1640531527;
    var a = key[0];
    var b = key[1];
    var c = key[2];
    var d = key[3];

    for (var i = 0; i < times; i++) {
      sum += delta;
      sum = new Int32Array([sum])[0];
      y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
      y = new Int32Array([y])[0];
      z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
      z = new Int32Array([z])[0];
    }
    tempInt[0] = y;
    tempInt[1] = z;
    return this.intToByte(tempInt, 0);
  }

  this.decrypt = function (encryptContent, offset, key, temes) {
    var tempInt = this.byteToInt(encryptContent, offset);
    var y = tempInt[0];
    var z = tempInt[1];
    var sum = -957401312;
    var delta = -1640531527;
    var a = key[0];
    var b = key[1];
    var c = key[2];
    var d = key[3];

    for (var i = 0; i < temes; ++i) {
      z -= (y << 4) + c ^ y + sum ^ (y >> 5) + d;
      z = new Int32Array([z])[0];
      y -= (z << 4) + a ^ z + sum ^ (z >> 5) + b;
      y = new Int32Array([y])[0];
      sum -= delta;
      sum = new Int32Array([sum])[0];
    }

    tempInt[0] = y;
    tempInt[1] = z;
    return this.intToByte(tempInt, 0);
  }

  this.byteToInt = function (content, offset) {
    var result = new Array(2);
    var i = 0;

    for (var j = offset; j < content.length && i < 2; j += 4) {
      result[i] = this.transform(content[j + 3]) | this.transform(content[j + 2]) << 8 | this.transform(content[j + 1]) << 16 | content[j] << 24;
      ++i;
    }

    return result;
  }

  this.intToByte = function (content, offset) {
    var result = new Int8Array(8);
    var i = 0;
    for (var j = offset; j < result.length && i < 2; j += 4) {
      result[j + 3] = (content[i] & 255);
      result[j + 2] = ((content[i] >> 8) & 255);
      result[j + 1] = ((content[i] >> 16) & 255);
      result[j] = (content[i] >> 24) & 255;
      ++i;
    }
    return result;
  }

  this.transform = function (temp) {
    var tempInt = temp;
    if (temp < 0) {
      tempInt = temp + 256;
    }
    return tempInt;
  }

  /**
   * 通过TEA加密,返回byte数组
   */
  this.encryptByTea = function (info, key) {
    var temp = this.unicodeStrToUtf8Byte(info);
    var n = 8 - temp.length % 8;
    var encryptStr = new Array(n);
    encryptStr[0] = n;
    for (var i = 1; i < n; i++) {
      encryptStr[i] = 0;
    }
    encryptStr = encryptStr.concat(temp);
    var result = new Int8Array(encryptStr.length);
    for (var offset = 0, i = 0; offset < result.length; offset += 8, i++) {
      var tempEncrpt = this.encrypt(encryptStr, offset, key, 32);
      for (var j = 0; j < 8; j++) {
        result[i * 8 + j] = tempEncrpt[j];
      }
    }
    return result;
  }

  /**
   * 通过TEA算法解密,返回字符串
   *
   * @param secretInfo
   * @param KEY
   * @return
   */
  this.decrypByTea = function (secretInfo, key) {
    var decyptStr;
    var len = secretInfo.length;
    var tempDecrypt = new Array(len);

    for (var offset = 0, i = 0; offset < secretInfo.length; offset += 8, i++) {
      decryptStr = this.decrypt(secretInfo, offset, key, 32);
      for (var j = 0; j < 8; j++) {
        tempDecrypt[i * 8 + j] = decryptStr[j];
      }
    }
    var n = tempDecrypt[0];

    var ei = this.utf8ByteToUnicodeStr(tempDecrypt.slice(n));
    return ei;
    //return this.bin2String(tempDecrypt,n, len - n);
  }


  /**
   * utf8 byte to unicode string
   * @param utf8Bytes
   * @returns {string}
   */
  this.utf8ByteToUnicodeStr = function (utf8Bytes) {
    var unicodeStr = "";
    for (var pos = 0; pos < utf8Bytes.length;) {
      var flag = utf8Bytes[pos];
      var unicode = 0;
      if ((flag >>> 7) === 0) {
        unicodeStr += String.fromCharCode(utf8Bytes[pos]);
        pos += 1;

      } else if ((flag & 0xFC) === 0xFC) {
        unicode = (utf8Bytes[pos] & 0x3) << 30;
        unicode |= (utf8Bytes[pos + 1] & 0x3F) << 24;
        unicode |= (utf8Bytes[pos + 2] & 0x3F) << 18;
        unicode |= (utf8Bytes[pos + 3] & 0x3F) << 12;
        unicode |= (utf8Bytes[pos + 4] & 0x3F) << 6;
        unicode |= (utf8Bytes[pos + 5] & 0x3F);
        unicodeStr += String.fromCharCode(unicode);
        pos += 6;

      } else if ((flag & 0xF8) === 0xF8) {
        unicode = (utf8Bytes[pos] & 0x7) << 24;
        unicode |= (utf8Bytes[pos + 1] & 0x3F) << 18;
        unicode |= (utf8Bytes[pos + 2] & 0x3F) << 12;
        unicode |= (utf8Bytes[pos + 3] & 0x3F) << 6;
        unicode |= (utf8Bytes[pos + 4] & 0x3F);
        unicodeStr += String.fromCharCode(unicode);
        pos += 5;

      } else if ((flag & 0xF0) === 0xF0) {
        unicode = (utf8Bytes[pos] & 0xF) << 18;
        unicode |= (utf8Bytes[pos + 1] & 0x3F) << 12;
        unicode |= (utf8Bytes[pos + 2] & 0x3F) << 6;
        unicode |= (utf8Bytes[pos + 3] & 0x3F);
        unicodeStr += String.fromCharCode(unicode);
        pos += 4;

      } else if ((flag & 0xE0) === 0xE0) {
        unicode = (utf8Bytes[pos] & 0x1F) << 12;
        ;
        unicode |= (utf8Bytes[pos + 1] & 0x3F) << 6;
        unicode |= (utf8Bytes[pos + 2] & 0x3F);
        unicodeStr += String.fromCharCode(unicode);
        pos += 3;

      } else if ((flag & 0xC0) === 0xC0) { //110
        unicode = (utf8Bytes[pos] & 0x3F) << 6;
        unicode |= (utf8Bytes[pos + 1] & 0x3F);
        unicodeStr += String.fromCharCode(unicode);
        pos += 2;

      } else {
        unicodeStr += String.fromCharCode(utf8Bytes[pos]);
        pos += 1;
      }
    }
    return unicodeStr;
  }

  /**
   * unicode string to utf8 byte
   * @param unicodeStr
   * @returns {bytes}
   */
  this.unicodeStrToUtf8Byte = function (str) {
    var bytes = new Array();
    for (var i = 0; i < str.length; i++) {
      var c = str.charCodeAt(i);
      var s = parseInt(c).toString(2);
      if (c >= parseInt("000080", 16) && c <= parseInt("0007FF", 16)) {
        var af = "";
        for (var j = 0; j < (11 - s.length); j++) {
          af += "0";
        }
        af += s;
        var n1 = parseInt("110" + af.substring(0, 5), 2);
        var n2 = parseInt("110" + af.substring(5), 2);
        if (n1 > 127) n1 -= 256;
        if (n2 > 127) n2 -= 256;
        bytes.push(n1);
        bytes.push(n2);
      } else if (c >= parseInt("000800", 16) && c <= parseInt("00FFFF", 16)) {
        var af = "";
        for (var j = 0; j < (16 - s.length); j++) {
          af += "0";
        }
        af += s;
        var n1 = parseInt("1110" + af.substring(0, 4), 2);
        var n2 = parseInt("10" + af.substring(4, 10), 2);
        var n3 = parseInt("10" + af.substring(10), 2);
        if (n1 > 127) n1 -= 256;
        if (n2 > 127) n2 -= 256;
        if (n3 > 127) n3 -= 256;
        bytes.push(n1);
        bytes.push(n2);
        bytes.push(n3);
      } else if (c >= parseInt("010000", 16) && c <= parseInt("10FFFF", 16)) {
        var af = "";
        for (var j = 0; j < (21 - s.length); j++) {
          af += "0";
        }
        af += s;
        var n1 = parseInt("11110" + af.substring(0, 3), 2);
        var n2 = parseInt("10" + af.substring(3, 9), 2);
        var n3 = parseInt("10" + af.substring(9, 15), 2);
        var n4 = parseInt("10" + af.substring(15), 2);
        if (n1 > 127) n1 -= 256;
        if (n2 > 127) n2 -= 256;
        if (n3 > 127) n3 -= 256;
        if (n4 > 127) n4 -= 256;
        bytes.push(n1);
        bytes.push(n2);
        bytes.push(n3);
        bytes.push(n4);
      } else {
        bytes.push(c & 0xff);
      }
    }
    return bytes;
  }
}
