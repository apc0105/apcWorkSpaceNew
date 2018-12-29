<template>
  <div class="main" :style="{height:height+'px'}">
    <div style="padding-top: 60px"><span class="aboutLink">详细新闻</span></div>
    <div ref="s_search" class="s_search" style="padding-top: 40px;">
      <div class="logotit">
        <div style="float: left;">{{title}}（{{code}}）</div>
        <div v-html="numChangeStar(star)" style="float: left;"></div>
      </div>
      <div ref="serres" class="home_width clearfix">
        <div class="scommcon" v-for="item in eventAnalysisDetailList">
          <dl>
            <dt style="width: 15%;">关联系数：</dt>
            <dd style="line-height: 24px;">{{numFilter(item.coeffi)}}</dd>
          </dl>
          <dl>
            <dt style="width: 15%;">日期：</dt>
            <dd style="line-height: 24px;"> {{item.detailDate}}</dd>
          </dl>
          <dl>
            <dt style="width: 15%;">标题：</dt>
            <dd class="content"><a :href="item.url" target="_blank">{{item.title}}</a></dd>
          </dl>
        </div>
        <!--   <div class="scommcon">
             <dl>
               <dt style="width: 15%;">关联系数：</dt>
               <dd style="line-height: 24px;">0.345</dd>
             </dl>
             <dl>
               <dt style="width: 15%;">日期：</dt>
               <dd style="line-height: 24px;"> 2018-12-20</dd>
             </dl>
             <dl>
               <dt style="width: 15%;">标题：</dt>
               <dd class="content"><a href="">新东方25年服务品质始终如一的中国教育品牌。现已发展成为涵盖早教现已发展成为涵盖早教现已发展成为涵盖早教。</a></dd>
             </dl>
           </div>-->


        <div class="pagebox" v-if="eventAnalysisDetailList.length > 0">
          <el-pagination
            background
            @current-change="handleCurrentChange"
            layout="prev, pager, next"
            :current-page="currentPage"
            :page-count="totalPageNumber">
          </el-pagination>
        </div>
      </div>

    </div>

  </div>

</template>

<script>
  import '@/styles/global.css'
  import '@/styles/homePage.css'
  import {getAnalysisDetail} from '@/api/analysis'

  export default {
    name: "detail",
    components: {},
    data() {
      return {
        totalPageNumber: 1,
        currentPage: 1,
        pageSize: 5,
        height: '',
        eventAnalysisDetailList: [],
        title: '',
        code: '',
        index: 0,
        star: 0,
        UID: '',
        newstarget: ''
      }
    },
    mounted() {
      this.height = window.screen.availHeight - 100;

      this.initPageData();

      this.openPage(1);
    },
    methods: {
      numFilter(value) {

        // 截取当前数据到小数点后两位

        let realVal = Number(value).toFixed(4);

        // num.toFixed(2)获取的是字符串

        return Number(realVal)

      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.openPage(val);
      },
      numChangeStar(num) {
        var starHtml = '';
        var span = '<span></span>';
        var zSpan = '<span class="z"></span>';
        var zbSpan = '<span class="zb"></span>';
        var fSpan = '<span class="f"></span>';
        var fbSpan = '<span class="fb"></span>';

        if ((num < 0.5 && num > 0) || num == 0.0 || num == 0) {
          starHtml = span + span + span + span + span;
        }
        if (num == 0.5) {
          starHtml = zbSpan + span + span + span + span;
        }
        if (num > 0.5 && num < 1.5) {
          starHtml = zSpan + span + span + span + span;
        }
        if (num == 1.5) {
          starHtml = zSpan + zbSpan + span + span + span;
        }
        if (num > 1.5 && num < 2.5) {
          starHtml = zSpan + zSpan + span + span + span;
        }
        if (num == 2.5) {
          starHtml = zSpan + zSpan + zbSpan + span + span;
        }
        if (num > 2.5 && num < 3.5) {
          starHtml = zSpan + zSpan + zSpan + span + span;
        }
        if (num == 3.5) {
          starHtml = zSpan + zSpan + zSpan + zbSpan + span;
        }
        if (num > 3.5 && num < 4.5) {
          starHtml = zSpan + zSpan + zSpan + zSpan + span;
        }
        if (num == 4.5) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zbSpan;
        }
        if (num > 4.5 && num <= 5.0) {
          starHtml = zSpan + zSpan + zSpan + zSpan + zSpan;
        }

        if (num < 0 && num >= -0.5) {
          starHtml = fbSpan + span + span + span + span;
        }
        if (num < -0.5 && num > -1.5) {
          starHtml = fSpan + span + span + span + span;
        }
        if (num == -1.5) {
          starHtml = fSpan + fbSpan + span + span + span;
        }
        if (num < -1.5 && num > -2.5) {
          starHtml = fSpan + fSpan + span + span + span;
        }
        if (num == -2.5) {
          starHtml = fSpan + fSpan + fbSpan + span + span;
        }
        if (num < -2.5 && num > -3.5) {
          starHtml = fSpan + fSpan + fSpan + span + span;
        }
        if (num == -3.5) {
          starHtml = fSpan + fSpan + fSpan + fbSpan + span;
        }
        if (num < -3.5 && num > -4.5) {
          starHtml = fSpan + fSpan + fSpan + fSpan + span;
        }
        if (num == -4.5) {
          starHtml = fSpan + fSpan + fSpan + fSpan + fbSpan;
        }
        if (num < -4.5 && num >= -5.0) {
          starHtml = fSpan + fSpan + fSpan + fSpan + fSpan;
        }

        return starHtml;
      },
      openPage(num) {
        getAnalysisDetail(this.UID, this.index, this.newstarget, num, this.pageSize).then(resp => {
          var respObj = resp.data;

          if (respObj.code == "1") {
            this.eventAnalysisDetailList = respObj.data.eventAnalysisDetailList;
            this.totalPageNumber = respObj.data.total_page;
            this.currentPage = parseInt(respObj.data.current_page);
          }

        }).catch(resp => {
          console.log(resp)
        })
      },
      initPageData() {

        this.title = sessionStorage.getItem("title");
        this.code = sessionStorage.getItem("code");
        this.index =sessionStorage.getItem("index");
        this.star = sessionStorage.getItem("star");
        this.UID = sessionStorage.getItem("UID");
        this.newstarget = sessionStorage.getItem("newstarget");

      }
    }
  }

</script>
