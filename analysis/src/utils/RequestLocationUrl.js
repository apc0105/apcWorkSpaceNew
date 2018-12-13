import router from '@/router'

//获取当前地址中的所有参数
function GetRequestParameters () {

	var paramMap = new HashMap();

	var url = location.search; //获取url中"?"符后的字串

	if (url.indexOf("?") != -1) {

		var str = url.substr(1);

		var strs = str.split("&");

		for ( var i = 0; i < strs.length; i++) {

		  if(strs[i].split("=")[1] != "" && strs[i].split("=")[1] != undefined){

        paramMap.put(strs[i].split("=")[0], strs[i].split("=")[1]);
      }

		}

	}

	return paramMap;

}

//转化参数
function toStringNameValuePairs(excludedKeys, additionalParameters) {

  if (additionalParameters != null || additionalParameters != undefined) {
    additionalParameters = "&" + additionalParameters;
  }

  var paramMap = GetRequestParameters();

  var coreElements = "";

  var tempFlag = true;

  paramMap.each(function(key, value, index) {

    if (excludedKeys != null || excludedKeys != undefined) {

      for ( var i = 0; i < excludedKeys.length; i++) {

        if (key == excludedKeys[i]) {

          tempFlag = false;

        }

        if(key == "forwardName"){
          tempFlag = false;
        }

      }

    }

    if (tempFlag) {

      coreElements += key + "=" + value

      if (index < paramMap.entrys().length - 1) {

        coreElements += "&";
      }

    }

    tempFlag = true;

  });

  if (additionalParameters != null || additionalParameters != undefined) {

    return coreElements + additionalParameters;
  }

  return coreElements;

}

//获取跳转地址
function getActionUrl (action, additionalParameters,excludedKeys){

  return "/"+action+ "?" + toStringNameValuePairs(excludedKeys, additionalParameters);

}


/*
*  跳转页面
* @param action  需要跳转的地址
* @param additionalParameters  主动增加的参数,多个用'&'隔开  格式为paramName=paramValue
* @param excludedKeys  需要过滤的参数名 格式为["paramName"]
*/
export function forwardWindowByAction(action, additionalParameters,excludedKeys){

  let url = getActionUrl(action, additionalParameters,excludedKeys);
  router.push(url);
}
export function windowHrefByAction(action, additionalParameters,excludedKeys){

  let url = getActionUrl(action, additionalParameters,excludedKeys);
  
  window.location.href=url;
}

export function  openWindowTagByAction(action, additionalParameters,excludedKeys) {

  let url = getActionUrl(action, additionalParameters,excludedKeys);

  window.open(url,"_blank");
}

//后台请求地址参数拼接
export function  addUrlParam(url) {

}

//向链接后拼接参数
export function addUrlParameters (url , parameterStr){

	if(url.indexOf("?") != -1){

		return url + "&" + parameterStr;

	}

	return url + "?" + parameterStr;
}

//通过参数名获取链接后边的参数
export function getSingleUrlParam(paramName) {
  let reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return decodeURI(r[2]);
  return null;
}

//HashMap
function HashMap() {
  /** 存放键的数组(遍历用到) */
  this.keys = new Array();
  /** 存放数据 */
  this.data = new Object();

  this.clear = function(){
    var obj;
    for(var i=this.keys.length-1;i>=0;i--){
      obj = this.keys.pop();
      obj = null;
    }

    this.keys = null;

    for(var i=this.data.length-1;i>=0;i--){
      obj = this.data.pop();
      obj = null;
    }

    this.data =null;

    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();
  }

  /**
   * 放入一个键值对
   * @param {String} key
   * @param {Object} value
   */
  this.put = function(key, value) {
    if(this.data[key] == null){
      this.keys.push(key);
    }
    this.data[key] = value;
  };

  /**
   * 获取某键对应的值
   * @param {String} key
   * @return {Object} value
   */
  this.get = function(key) {
    return this.data[key];
  };

  this.containsKey = function(key) {
    return this.data[key]!=null;
  };
  /**
   * 删除一个键值对
   * @param {String} key
   */
  //this.remove = function(key) {
  //	this.keys.remove(key);
  //	this.data[key] = null;
  //};

  /**
   * 遍历HashMap,执行处理函数
   *
   * @param {Function} 回调函数 function(key,value,index){..}
   */
  this.each = function(fn){
    if(typeof fn != 'function'){
      return;
    }
    var len = this.keys.length;
    for(var i=0;i<len;i++){
      var k = this.keys[i];
      fn(k,this.data[k],i);
    }
  };

  /**
   * 获取键值数组(类似Java的entrySet())
   * @return 键值对象{key,value}的数组
   */
  this.entrys = function() {
    var len = this.keys.length;
    var entrys = new Array(len);
    for (var i = 0; i < len; i++) {
      entrys[i] = {
        key : this.keys[i],
        value : this.data[i]
      };
    }
    return entrys;
  };

  /**
   * 判断HashMap是否为空
   */
  this.isEmpty = function() {
    return this.keys.length == 0;
  };

  /**
   * 获取键值对数量
   */
  this.size = function(){
    return this.keys.length;
  };

  /**
   * 重写toString
   */
  this.toString = function(){
    var s = "{";
    for(var i=0;i<this.keys.length;i++,s+=','){
      var k = this.keys[i];
      s += k+"="+this.data[k];
    }
    s+="}";
    return s;
  };
}
