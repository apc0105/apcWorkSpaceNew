<template>
  <div class="main" :style="{height:height+'px'}">
    <div ref="s_search" class="s_search">
      <div class="logotit"><img src="../../../assets/images/img_wenzi.png"/><span class="newWord">（原油量化）</span></div>
      <div class=" srcon clearfix" style="">
        <div ref="serinp" id="serinp" class="searchl">
          <el-input v-model="shiftName" >
            <el-select v-model="days" slot="prepend" placeholder="请选择" style="width: 80px;background-color: #435a6c;">
              <el-option label="一天" value="0"></el-option>
              <el-option label="多天" value="1"></el-option>
            </el-select>
          </el-input>
        </div>
        <div class="searchButton" @click="search">
          泰宝一下
        </div>
      </div>

      <div ref="serres" class="home_width1 clearfix" style="display: none;">
        <div class="scomm1">泰宝为您找到受影响公司结果约 <em>{{totalNum}}</em> 个。
          <el-radio-group v-model="subfield" @change="changeHandler" style="margin-left: 40%;" >
            <el-radio class="radio" label="1">按相关性分栏</el-radio>
            <el-radio class="radio" label="2">按涨跌幅分栏</el-radio>
          </el-radio-group>
        </div>
        <div class="scommcon1" style="border-right: 10px solid #f8f8f8;">
          <dl>
            <dt class="tit">股票名称</dt>
            <dd class="tit">相关性</dd>
            <dd class="tit">涨跌幅</dd>
          </dl>

          <dl v-for="item in smartSearchList">
            <dt>{{item.name}}（{{item.code}}）</dt>
            <dd>{{item.correlation}}</dd>
            <dd>{{item.upsAndDowns}}</dd>
          </dl>




          <div class="pagebox" style="padding-left: 10px;">

            <el-pagination
              background
              @current-change="handleCurrentChange"
              layout="prev, pager, next"
              :current-page="currentPage"
              :page-count="totalPageNumber">
            </el-pagination>
          </div>
        </div>
        <div class="scommcon1">
          <dl>
            <dt class="tit">股票名称</dt>
            <dd class="tit">相关性</dd>
            <dd class="tit">涨跌幅</dd>
          </dl>

          <dl v-for="item in fsmartSearchList">
              <dt>{{item.name}}（{{item.code}}）</dt>
            <dd>{{item.correlation}}</dd>
            <dd>{{item.upsAndDowns}}</dd>
          </dl>

          <div class="pagebox" style="padding-left: 10px;">

            <el-pagination
              background
              @current-change="fhandleCurrentChange"
              layout="prev, pager, next"
              :current-page="fcurrentPage"
              :page-count="ftotalPageNumber">
            </el-pagination>
          </div>
        </div>
      </div>

    </div>

  </div>

</template>

<script>
  import '@/styles/quant.css'
  import {getQuantPage} from '@/api/quant'
  import NProgress from 'nprogress'

  export default {
    name: "quant",
    components: {},
    data() {
      return {
        shiftName: '',
        smartSearchList: [],
        fsmartSearchList: [],
        totalPageNumber: 1,
        ftotalPageNumber: 1,
        currentPage: 1,
        fcurrentPage: 1,
        totalNum: 0,
        token: '',
        days: '0',
        height: '',
        pageSize: 10,
        subfield: "1"
      }
    },
    mounted() {
      // this.height = window.screen.availHeight - 100;
      // this.height = document.documentElement.clientHeight;
      this.height = window.innerHeight;
    },
    methods: {
      changeHandler(val){
        this.subfield=val;

        this.search();
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.openPage('z');
      },
      fhandleCurrentChange(val) {
        this.fcurrentPage = val;
        this.openPage('f');
      },
      search() {
        var that = this;

        if (this.shiftName != null && this.shiftName != null) {

          if(!this.isNumber(this.shiftName)){
            alert("文本框只能输入数字");
            return;
          }

          NProgress.inc(0.2);
          NProgress.configure({easing: 'ease', speed: 500});
          NProgress.start();

          this.initPage(this.shiftName, function () {
            that.$refs.s_search.className = "s_search s_ani_d";
            that.$refs.serres.className = "home_width1 clearfix";

            setTimeout(() => {
              NProgress.done();
              that.$refs.serres.style.display = 'block';
            }, 200)

            that.height = window.innerHeight;
            if (that.height < 660) {
              that.height = that.height + 200;
            }
            if (that.height < 760) {
              that.height = that.height + 100;
            }
          });
        } else {
          this.height = window.innerHeight;
          this.$refs.serres.style.display = 'none';
          this.$refs.s_search.className = "s_search s_ani_h";
          this.$refs.serres.className = "home_width1 clearfix s_ani_sh0";
          this.totalNum = 0;
        }
      },
      initPage(searchValue, callback) {
        getQuantPage(this.pageSize, this.days, 1, 1, this.shiftName, this.subfield).then(resp => {
          let respObj = resp.data;
          if (respObj.code != 1) {
            NProgress.done();
            alert("未查询到数据！");
            return;
          }
          console.log("resp", resp)
          this.smartSearchList = respObj.data.nodes;
          this.fsmartSearchList = respObj.data.fnodes;
          this.totalPageNumber = respObj.data.total_page;
          this.ftotalPageNumber = respObj.data.ftotal_page;
          this.currentPage = respObj.data.current_page;
          this.fcurrentPage = respObj.data.fcurrent_page;

          if (respObj.data.total_num != null) {
            this.totalNum = respObj.data.total_num;
            console.log("height", this.$refs.sermess);
          }

          callback();
        }).catch(function (response) {
          NProgress.done();
        })
      },
      openPage(zf) {
        getQuantPage(this.pageSize, this.days, this.currentPage, this.fcurrentPage, this.shiftName, this.subfield).then(resp => {
          let respObj = resp.data;
          if (zf == "z") {
            this.smartSearchList = respObj.data.nodes;
            this.totalPageNumber = respObj.data.total_page;
            this.currentPage = parseInt(respObj.data.current_page);
          }
          if (zf == "f") {
            this.fsmartSearchList = respObj.data.fnodes;
            this.ftotalPageNumber = respObj.data.ftotal_page;
            this.fcurrentPage = parseInt(respObj.data.fcurrent_page);
          }
        })
      },
      isNumber(val){

        var regPos = /^\d+(\.\d+)?$/; //非负浮点数
        var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
        if(regPos.test(val) || regNeg.test(val)){
          return true;
        }else{
          return false;
        }
      }
    }
  }

</script>
