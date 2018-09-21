<template>
  <el-container>
    <!--   <el-header class="page-top">
       </el-header>-->

    <el-header height="250px">
      <el-row class="style_width">
        <div class="homelogo"></div>
        <div class="">
          <p class="logoTitle">泰宝智能搜索系统</p>
        </div>
        <div class=" srcon" style="width:624px; margin:25px auto 0;">
          <div class="searchl" @click="handleshow($event)">
            <el-input v-model="shiftName" @focus="handleinput($event)"
                      @input.native="handleinput($event),search($event)" @keyup.enter.native="enter($event)">
            </el-input>
            <!--<div class="sod"></div>-->
            <div class="sc" id="sermess" ref="sermess">
              <ul>
                <li style="border-bottom:#ffffff solid 1px;" v-for="(item,index) in historicalRecordsList"
                    @click="inputMess($event)" :key="index">
                  {{ item }}
                </li>
              </ul>
        <!--      <div class="cz">
                <div class="close" @click="closeSerMess($event)">取消</div>
                <div class="del" @click="closeSerMess($event)">清除记录</div>
              </div>-->
            </div>
          </div>
          <el-button slot="append" class="searchButton" @click="search()"
                     style="width:100px;background-color: #14A296;font-size: 14px; color: #FFFFFF;">
            泰宝一下
          </el-button>
        </div>
      </el-row>
    </el-header>

    <el-container class="home_width clearfix" >
      <el-main class="page-bottom">

        <el-row class="">
          <el-col :span="24">
            <div class="scomm">泰宝为您找到受影响公司结果约 <em>88</em> 个。<span class="z">正面</span><span class="f">负面</span></div>
            <div class="scommcon">
              <dl><dt class="tit">公司名称</dt><dd class="tit">影响程度及方向</dd></dl>
              <dl>
                <dt @click="showk($event)">中国石油（601857）</dt>
                <dd><span>1</span><span>2</span><span>3</span><span>4</span><span>5</span></dd>
                <process-go class="k"></process-go>
              </dl>
  <!--            <dl>
                <dt @click="showk($event)">中国石油（601857）</dt>
                <dd><span>1</span><span>2</span><span>3</span><span>4</span><span>5</span></dd>
                <process-go class="k"></process-go>
              </dl>-->




            </div>
          </el-col>
        </el-row>

      </el-main>

    </el-container>

    <!-- 底部版权信息 start-->
    <!--    <el-footer>
          <page-footer></page-footer>
        </el-footer>-->
    <!-- 底部版权信息 end-->
  </el-container>

</template>

<style>
  .home_width{width: 624px;}
  .scomm{padding:10px 20px; line-height:30px; font-size:12px; color:#666; text-align:left;}
  .scomm span{ display:inline-block; margin:0 10px; padding-left:30px;}
  .scomm em{font-style: normal}
  .scommcon{padding:0 20px 30px;}
  .scommcon dl{zoom:1; padding: 5px 0;}
  .scommcon dl:after{display:block; content:''; clear: both; height:0; font-size:0;}
  .scommcon dl dt{float:left; width:70%; line-height: 24px; font-size:14px; text-align: left; cursor: pointer;}
  .scommcon dl dd{float:left; width:30%; text-align: left}
  .scommcon dl dd span{display: inline-block; width: 24px; height: 24px; margin-right: 5px;}
  .scommcon dl .k{ float: left; width: 100%; margin:10px 0; padding:10px; border:#e1e1e1 solid 1px; box-sizing: border-box;}
  .scommcon dl .tit{line-height: 24px; font-size:14px; text-align: left; font-weight: bold;}
</style>

<script>
  import {getRecommend, getHistoricalRecords, closeSerMess} from '@/api/homePage'
  import processGo from '@/components/process-go'

  document.addEventListener('click', function () {
    document.getElementById('sermess').style.display = "none"
  }, false)
  export default{
    name: "homePage",
    components:{
      processGo
    },
    data () {
      return {
        shiftName: this.GLOBAL.searchValue,
        historicalRecordsList: ["油价","金价","股价"]
      }
    },
    mounted (){
    },
    methods: {
      search(e){
        if (/^[\u4e00-\u9fa5]{2}/g.test(this.GLOBAL.searchValue) || this.shiftName.length > 0) {
          this.$refs.sermess.style.display = 'none';
        }
      },
      showDetail(fileId, type, flag) {
      },
      handleinput(e){
          debugger
        var that = this;
        var el = e.target;
        if (el.innerText != "" && el.innerText != undefined) {
          that.$refs.sermess.style.display = 'none';
        } else {
          that.GLOBAL.searchValue = el.value;
          var keywords = that.GLOBAL.searchValue
          var querystring = require('querystring');
          this.$refs.sermess.style.display = 'block';
        }
      },
      closeSerMess(e){
        e.stopPropagation();
        e.cancelBubble = true;
        this.$refs.sermess.style.display = 'none';
      },
      inputMess(e){
        var el = e.target;
        e.stopPropagation();
        e.cancelBubble = true;
        this.shiftName=el.innerText;
        this.GLOBAL.searchValue = el.innerText;
        this.$refs.sermess.style.display = 'none';
      },
      enter(e){
        var el = e.target.value;
        this.searchValue = el;
        this.GLOBAL.searchValue = el;
        this.$refs.sermess.style.display = 'none';
        this.search()
      },
      handleshow(e) {
        e.stopPropagation();
        e.cancelBubble = true;
        this.$refs.sermess.style.display = 'block';
      },
      showk(e){
        var el = e.target;
        var eln = el.nextElementSibling.nextElementSibling;
        if(eln.style.display=="none"){
          eln.style.display="block"
        }else{
          eln.style.display="none"
        }
      }
    }
  }

</script>

<style scoped>
  .page-top {
    border: 1px solid #eaeaea;
    width: 100%;
    height: 50px;
  }

  .logoDiv {
    padding-left: 25px;
  }

  #logo {
    height: 90px;
    text-align: center;
    margin: 20px auto 0;
  }

  .logoTitle {
    font-size: 22px;
    width: 332px;
    letter-spacing: 3px;
    font-weight: bold;
    text-align: center;
    margin: 0 auto;
  }

  .button {
    width: 64px;
    background-color: #14A296;
    font-size: 14px;
    color: #FFFFFF;
    padding-left: 17px;
  }

  .button1 {
    width: 64px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    padding: 0;
    color: #666666;
    font-size: 14px;
    display: inline-block;
  }

  .topTitle {
    float: left;
    margin-left: 115px;
    font-size: 14px;
    color: #C8C8C8;
  }

  .titleMain {
    width: 100px;
  }

  .topTitle span {
    padding: 2px 5px;
    height: 20px;
    margin-right: 40px;
  }

  .page-bottom {
    border: 1px solid #eaeaea;
  }

  .bottom-header {
    border-bottom: 1px solid #eaeaea;
    height: 60px;
  }

  .bottom-header-style {
    width: 10%;
    text-align: center;
    margin-top: 10px;
    padding-left: 50px;
    font-size: 18px;
    color: #008F81;
    float: left;
  }

  .bottom-header-style1 {
    width: 10%;
    text-align: center;
    margin-top: 10px;
    font-size: 16px;
    color: #666666;
    float: left;
  }

  .page-right {
    width: 102px;
    height: 291px;
    border: 1px solid #eaeaea;
    float: right;
    opacity: 0.62;
    background: #000000;
    position: fixed;
    right: 0;
  }

  #copy {
    width: 59px;
    height: 59px;
    margin: 20px 22px 0 22px;
  }

  #message {
    width: 39px;
    height: 39px;
    margin: 20px 32px 0 32px;
  }

  #code {
    width: 44px;
    height: 44px;
    margin: 20px 29px 0 29px;
  }

  .page {
    display: block;
    width: 170px;
    height: 170px;
    float: left;
  }

  .describe1 {
    font-size: 18px;
    color: #008F81;
    letter-spacing: -0.2px;
  }

  .describe2 {
    position: absolute;
    bottom: 0;
    left: 0;
    line-height: 24px;
    font-size: 12px;
    color: #999999;
  }

  .describe3 {
    line-height: 24px;
    font-size: 16px;
    color: #333333;
  }

  .describe4 {
    position: relative;
    height: 80px;
    line-height: 20px;
    font-size: 12px;
    color: #666;
    margin-top: 10px;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;
    overflow: hidden;
  }

  .video {
    margin: 30px 10px 0 0;
    float: left;
  }

  .video1 {
    font-size: 18px;
    color: #333333;
    float: left;
    margin: 30px 0 0 0;
  }

  .video2 {
    margin: 30px 0 0 10px;
    float: left;
  }

  .video3 {
    font-size: 12px;
    color: #999999;
    float: left;
  }

  .recommend {
    margin: 0 40px 0 40px;
    height: 110px;
    border-bottom: 1px solid #eaeaea;
  }

  .footerTitle {
    font-size: 12px;
    margin-top: 30px;
  }

  .list {
    padding: 0 0 30px;
    margin-bottom: 30px;
    border-bottom: 1px #dadada solid;
  }

  .list:last-of-type {
    border-bottom: none;
  }

  .listSpan {
    float: right;
    margin-left: 15px;
    margin-right: 20px;
  }
</style>
