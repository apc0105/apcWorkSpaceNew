<template>

  <div>
    <!--   <el-header class="page-top">
       </el-header>-->
    <div ref="s_search" class="s_search">
      <div class="logotit"><img src="../../assets/images/img_wenzi.png"></div>
      <div class=" srcon clearfix" style="">
        <div ref="serinp" id="serinp" class="searchl" @click="handleshow($event)">
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
        <div class="searchButton" @click="search()">
          泰宝一下
        </div>
      </div>

      <div ref="serres" class="home_width clearfix" style="display: none;">
        <div class="scomm">泰宝为您找到受影响公司结果约 <em>{{totalNum}}</em> 个。<span class="z">正面影响力</span><span class="f">负面影响力</span></div>
        <div class="scommcon">
          <dl>
            <dt class="tit">公司名称</dt>
            <dd class="tit">影响程度及方向</dd>
          </dl>

          <dl v-for="item in smartSearchList">
            <dt @click="showDialog(item)">{{item.name}}（{{item.code}}）</dt>
            <dd v-html="numChangeStar(item.score)"></dd>
          </dl>

          <div class="pagebox">
            <a href="javascript:void(0);" v-for="(item,index) in totalPageNumber" :class="currentPage==item?'active':''"
               @click="openPage(item)">{{item}}</a>
            <a href="javascript:void(0);" class="next" @click="nextPage()">下一页</a>
          </div>
        </div>

      </div>

    </div>

    <el-dialog :title="record.name" v-if="dialogTableVisible" :visible.sync="dialogTableVisible" width="90%"
               @close="closeDialog">
      <process-go v-bind:record="record" ref="processGoPage"></process-go>
    </el-dialog>

  </div>

</template>

<style>
  .el-dialog__body {
    padding: 10px 30px 30px;
  }

  .el-dialog__header {
    padding: 20px 30px 10px;
  }

  .el-dialog__headerbtn {
    right: 30px;
  }

  .home_width {
    overflow: hidden;
    width: 680px;
    margin: 10px 0 20px;
    background-color: #fff;
  }

  .scomm {
    position: relative;
    padding: 5px 20px;
    line-height: 30px;
    font-size: 14px;
    color: #666;
    text-align: left;
    background-color: #f8f8f8;
  }

  .scomm span {
    position: absolute;
    top: 5px;
    display: inline-block;
    padding-left: 20px;
  }

  .scomm span.z {
    right: 150px;
  }

  .scomm span:before {
    display: block;
    content: '';
    position: absolute;
    top: 10px;
    left: 0;
    width: 10px;
    height: 10px;
    border-radius: 10px;
    background-color: #fd5530;
  }

  .scomm span.f {
    right: 44px;
  }

  .scomm span.f:before {
    background-color: #14a296
  }

  .scomm em {
    font-style: normal
  }

  .scommcon {
    padding: 10px 0px 0px;
  }

  .scommcon dl {
    zoom: 1;
    padding: 5px 0;
  }

  .scommcon dl:hover {
    background-color: #f8f8f8;
  }

  .scommcon dl:after {
    display: block;
    content: '';
    clear: both;
    height: 0;
    font-size: 0;
  }

  .scommcon dl dt {
    float: left;
    width: 70%;
    padding-left: 40px;
    line-height: 24px;
    font-size: 14px;
    text-align: left;
    cursor: pointer;
    box-sizing: border-box;
  }

  .scommcon dl dd {
    float: left;
    width: 30%;
    height: 24px;
    padding-right: 40px;
    text-align: left;
    box-sizing: border-box;
  }

  .scommcon dl dd span {
    display: inline-block;
    width: 24px;
    height: 24px;
    margin-right: 5px;
    background: url(../../assets/images/img_04.png) center no-repeat;
  }

  .scommcon dl dd span.z {
    background: url(../../assets/images/img_01.png) center no-repeat;
  }

  .scommcon dl dd span.zb {
    background: url(../../assets/images/img_02.png) center no-repeat;
  }

  .scommcon dl dd span.f {
    background: url(../../assets/images/img_03.png) center no-repeat;
  }

  .scommcon dl dd span.fb {
    background: url(../../assets/images/img_05.png) center no-repeat;
  }

  .scommcon dl .k {
    float: left;
    width: 100%;
    margin: 10px 0;
    padding: 10px;
    border: #e1e1e1 solid 1px;
    box-sizing: border-box;
  }

  .scommcon dl .tit {
    line-height: 24px;
    font-size: 14px;
    text-align: left;
    font-weight: bold;
  }

  .scommcon .pagebox {
    margin-top: 10px;
    padding: 10px 40px;
    text-align: right;
    background-color: #f8f8f8;
  }

  .scommcon .pagebox a {
    display: inline-block;
    padding: 0 10px;
    line-height: 26px;
    font-size: 14px;
    text-decoration: none;
    color: #9d9d9d;
  }

  .scommcon .pagebox a.active {
    color: #fff;
    background-color: #00aeb1;
  }

  .scommcon .pagebox a.next {
    color: #020202;
  }

  .scommcon .pagebox a:hover {
    color: #fff;
    background-color: #00aeb1;
  }

  .sou {
    font-size: 84px;
    color: #fbb240;
  }

  .el-input__inner {
    height: 54px;
  }
</style>

<script>
  import {smartSearch, smartSearchPage, smartSearchEdge} from '@/api/homePage'
  import processGo from '@/components/process-go'

  document.addEventListener('click', function () {
    document.getElementById('sermess').style.display = "none";
    var si = document.getElementById('serinp');
    if (si.firstElementChild.firstElementChild.value == "") {
      si.className = "searchl"
    } else {
      si.className = "searchl focus"
    }
  }, false);

  export default{
    name: "homePage",
    components: {
      processGo
    },
    data () {
      return {
        shiftName: '',
        historicalRecordsList: ["油价", "金价", "铁价"],
        dialogTableVisible: false,
        searchResultVisible: false,
        smartSearchList: [],
        totalPageNumber: 1,
        currentPage: 1,
        totalNum:0,
        token: '',
        record: {
          name: '',
          code: ''
        }
      }
    },
    mounted (){

    },
    methods: {
      search(e){
        if (/^[\u4e00-\u9fa5]/g.test(this.shiftName) || this.shiftName.length > 0) {
          this.initPage(this.shiftName);

          this.$refs.sermess.style.display = 'none';
          this.$refs.s_search.className = "s_search s_ani_d";
          this.$refs.serres.className = "home_width clearfix";

          setTimeout(() => {
            this.$refs.serres.style.display = 'block';
          }, 200)
        } else {
          this.$refs.serres.style.display = 'none';
          this.$refs.s_search.className = "s_search s_ani_h";
          this.$refs.serres.className = "home_width clearfix s_ani_sh0";
        }

      },
      handleinput(e){
        var that = this;
        var el = e.target;
        if (el.innerText != "" && el.innerText != undefined) {
          that.$refs.sermess.style.display = 'none';
        } else {
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
        this.shiftName = el.innerText;
        this.$refs.sermess.style.display = 'none';
        this.$refs.serinp.className = "searchl focus";
      },
      enter(e){
        var el = e.target.value;
        this.searchValue = el;
        this.$refs.sermess.style.display = 'none';
        this.search()
      },
      handleshow(e) {
        e.stopPropagation();
        e.cancelBubble = true;
        this.$refs.sermess.style.display = 'block';
      },
      showDialog(item){
        this.record.name = item.name;
        this.record.code = item.code;
        this.dialogTableVisible = true;
      },
      closeDialog(){
        this.dialogTableVisible = false;
      },
      initPage(searchValue){
        smartSearch(searchValue).then(resp => {
          this.smartSearchList = resp.data.nodes;
          this.totalPageNumber = resp.data.total_page;
          this.currentPage = resp.data.current_page;
          this.totalNum = resp.data.total_num;
          this.token = resp.data.token;
          sessionStorage.setItem('token', this.token);
        })
      },
      numChangeStar(num){

        var starHtml = '';
        var span = '<span></span>';
        var zSpan = '<span class="z"></span>';
        var zbSpan = '<span class="zb"></span>';
        var fSpan = '<span class="f"></span>';
        var fbSpan = '<span class="fb"></span>';

        if (num == 0.0 || num == 0) {
          starHtml = span + span + span + span + span;
        }
        if (num == 0.5) {
          starHtml = zbSpan + span + span + span + span;
        }
        if (num == 1.0) {
          starHtml = zSpan + span + span + span + span;
        }
        if (num == 1.5) {
          starHtml = zSpan + zbSpan + span + span + span;
        }
        if (num == 2.0) {
          starHtml = zSpan + zSpan + span + span + span;
        }
        if (num == 2.5) {
          starHtml = zSpan + zSpan + zbSpan + span + span;
        }
        if (num == 3.0) {
          starHtml = zSpan + zSpan + zSpan + span + span;
        }
        if (num == 3.5) {
          starHtml = zSpan + zSpan + zbSpan + span + span;
        }
        if (num == 4.0) {
          starHtml = zSpan + zSpan + zSpan + zSpan + span;
        }
        if (num == 4.5) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zbSpan;
        }
        if (num == 5.0) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zSpan;
        }

        if (num == -0.5) {
          starHtml = fbSpan + span + span + span + span;
        }
        if (num == -1.0) {
          starHtml = fSpan + span + span + span + span;
        }
        if (num == -1.5) {
          starHtml = fSpan + fbSpan + span + span + span;
        }
        if (num == -2.0) {
          starHtml = fSpan + fSpan + span + span + span;
        }
        if (num == -2.5) {
          starHtml = fSpan + fSpan + fbSpan + span + span;
        }
        if (num == -3.0) {
          starHtml = fSpan + fSpan + fSpan + span + span;
        }
        if (num == -3.5) {
          starHtml = fSpan + fSpan + fSpan + fbSpan + span;
        }
        if (num == -4.0) {
          starHtml = fSpan + fSpan + fSpan + fSpan + span;
        }
        if (num == -4.5) {
          starHtml = fSpan + fSpan + fSpan + fSpan + fbSpan;
        }
        if (num == -5.0) {
          starHtml = fSpan + fSpan + fSpan + fSpan + fSpan;
        }

        return starHtml;
      },
      openPage(num){
        smartSearchPage(num, this.token).then(resp => {
          this.smartSearchList = resp.data.nodes;
          this.totalPageNumber = resp.data.total_page;
          this.currentPage = parseInt(resp.data.current_page);
        })
      },
      nextPage(){
        var num = this.currentPage + 1;

        if (num > this.totalPageNumber) {
          alert("已经到最后一页");
          return false;
        }

        this.openPage(num);
      }
    }
  }

</script>
