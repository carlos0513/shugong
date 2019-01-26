/**
 * @name 获取年/月/日
 * @param isData
 * @returns {string}
 */
function timeData(isData = true){
    var mydate = new Date();
    var str = "" + mydate.getFullYear() + ".";
    str += (mydate.getMonth()+1);
    //是否获取日期
    if(isData){
        str += "." + mydate.getDate();
    }
    return str;
}

/**
 * @name 获取随机数
 * @param min
 * @param max
 * @returns {number}
 */
function getRandom(min, max){
    var r = Math.random() * (max - min);
    var re = Math.round(r + min);
    re = Math.max(Math.min(re, max), min)
    return re;
}

/**
 * @name 求和
 * @param arr
 */
function sum(arr) {
    var s = 0;
    for (var i=arr.length-1; i>=0; i--) {
        s += arr[i].value;
    }
    return s;
}