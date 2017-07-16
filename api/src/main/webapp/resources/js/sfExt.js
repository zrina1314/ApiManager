/**
 * SfEgmas 数据处理
 */

/** 请求头信息 表单切换到自定义JS */
function convertDebugHeaderTabe2JS() {
	changeDisplay("customHeader", "formHeader");
	var json = SfTableToJs('debugHeader');
	json = format(json, false);
	$("#customHeaderJS").val(json);
}
/** 请求头信息 自定义JS切换到表单 */
function convertDebugHeaderJS2Tabe() {
	changeDisplay("formHeader", "customHeader");
}

/** 请求参数 表单切换到自定义JS */
function convertDebugParamsTabe2JS() {
	changeDisplay("customParamsDiv", "formParamsDiv");
	var json = SfTableToJs('debugParams');
	json = format(json, false);
	$("#customParamsJS").val(json);
}

/** 请求参数 自定义JS切换到表单 */
function convertDebugParamsJS2Tabe() {
	changeDisplay("formParamsDiv", "customParamsDiv");
}

function SfTableToJs(tableId) {
	var json = "{";
	var i = 0;
	$('#' + tableId).find('tbody').find('tr').each(
			function() {
				i = i + 1;
				$(this).find('td').find('input').each(
						function(i, val) {
							json += "\"" + val.name + "\":\""
									+ replaceAll(val.value, '"', '\\"') + "\""
							json += ",";
						});
				$(this).find('td').find('select').each(
						function(i, val) {
							json += "\"" + val.name + "\":\""
									+ replaceAll(val.value, '"', '\\"') + "\""
							json += ",";
						});
			});
	if (json.endsWith(","))
		json = json.substring(0, json.length - 1);
	json += "}";
	return json;
}

/** 处理原始头信息，转换成JS */
function disposeHeaderToJson(header) {
	var json = "{";
	if (header != null) {
		for (var i = 0; i < header.length; i++) {
			if (i != 0)
				json += ",";
			json += "\"" + header[i].name + "\":\""
					+ replaceAll(header[i].def, '"', '\\"') + "\""
		}
	}
	json += "}";
	return json;
}


/*   
 * MAP对象，实现MAP功能   
 *   
 * 接口：   
 * size()     获取MAP元素个数   
 * isEmpty()    判断MAP是否为空   
 * clear()     删除MAP所有元素   
 * put(key, value)   向MAP中增加元素（key, value)    
 * remove(key)    删除指定KEY的元素，成功返回True，失败返回False   
 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL   
 * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL   
 * containsKey(key)  判断MAP中是否含有指定KEY的元素   
 * containsValue(value) 判断MAP中是否含有指定VALUE的元素   
 * values()    获取MAP中所有VALUE的数组（ARRAY）   
 * keys()     获取MAP中所有KEY的数组（ARRAY）   
 *   
 * 例子：   
 * var map = new Map();   
 *   
 * map.put("key", "value");   
 * var val = map.get("key")   
 * ……   
 *   
 */     
function Map() {     
    this.elements = new Array();     
       
    //获取MAP元素个数     
    this.size = function() {     
        return this.elements.length;     
    }     
       
    //判断MAP是否为空     
    this.isEmpty = function() {     
        return(this.elements.length < 1);     
    }     
       
    //删除MAP所有元素     
    this.clear = function() {     
        this.elements = new Array();     
    }     
       
    //向MAP中增加元素（key, value)      
    this.put = function(_key, _value) {     
        this.elements.push( {     
            key : _key,     
            value : _value     
        });     
    }     
       
    //删除指定KEY的元素，成功返回True，失败返回False     
    this.remove = function(_key) {     
        var bln = false;     
        try{     
            for(i = 0; i < this.elements.length; i++) {     
                if(this.elements[i].key == _key) {     
                    this.elements.splice(i, 1);     
                    return true;     
                }     
            }     
        } catch(e) {     
            bln = false;     
        }     
        return bln;     
    }     
       
    //获取指定KEY的元素值VALUE，失败返回NULL     
    this.get = function(_key) {     
        try{     
            for(i = 0; i < this.elements.length; i++) {     
                if(this.elements[i].key == _key) {     
                    return this.elements[i].value;     
                }     
            }     
        } catch(e) {     
            return null;     
        }     
    }     
       
    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL     
    this.element = function(_index) {     
        if(_index < 0 || _index >= this.elements.length) {     
            return null;     
        }     
        return this.elements[_index];     
    }     
       
    //判断MAP中是否含有指定KEY的元素     
    this.containsKey = function(_key) {     
        var bln = false;     
        try{     
            for(i = 0; i < this.elements.length; i++) {     
                if(this.elements[i].key == _key) {     
                    bln = true;     
                }     
            }     
        } catch(e) {     
            bln = false;     
        }     
        return bln;     
    }     
       
    //判断MAP中是否含有指定VALUE的元素     
    this.containsValue = function(_value) {     
        var bln = false;     
        try{     
            for(i = 0; i < this.elements.length; i++) {     
                if(this.elements[i].value == _value) {     
                    bln = true;     
                }     
            }     
        } catch(e) {     
            bln = false;     
        }     
        return bln;     
    }     
       
    //获取MAP中所有VALUE的数组（ARRAY）     
    this.values = function() {     
        var arr = new Array();     
        for(i = 0; i < this.elements.length; i++) {     
            arr.push(this.elements[i].value);     
        }     
        return arr;     
    }     
       
    //获取MAP中所有KEY的数组（ARRAY）     
    this.keys = function() {     
        var arr = new Array();     
        for(i = 0; i < this.elements.length; i++) {     
            arr.push(this.elements[i].key);     
        }     
        return arr;     
    }     
}     